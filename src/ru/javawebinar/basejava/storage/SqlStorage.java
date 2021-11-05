package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    // Общий код методов save и update
//    private void saveUpdateCommon(String sql, Resume r) {
//        sqlHelper.transactionalExecute(conn -> {
//            try (PreparedStatement ps = conn.prepareStatement(sql)) {
//                ps.setString(1, r.getFullName());
//                ps.setString(2, r.getUuid());
//            }
//        });
//    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException("Resume not exist " + r.getUuid());
                }

                try (PreparedStatement psc = conn.prepareStatement("UPDATE contact SET value = ? WHERE resume_uuid = ? AND type = ?")) {
                    for (Map.Entry<ContactType, String> contact : r.getContacts().entrySet()) {
                        psc.setString(1, contact.getValue());
                        psc.setString(2, r.getUuid());
                        psc.setString(3, contact.getKey().name());
                        psc.addBatch();
                    }
                    psc.executeBatch();
                }
            }
            return null;
        });

/*        sqlHelper.<Void>execute("UPDATE resume SET full_name = ? WHERE uuid = ?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException("Resume not exist " + r.getUuid());
            }
            return null;
        });*/
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();

                try (PreparedStatement psc = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
                    for (Map.Entry<ContactType, String> contact : r.getContacts().entrySet()) {
                        psc.setString(1, r.getUuid());
                        psc.setString(2, contact.getKey().name());
                        psc.setString(3, contact.getValue());
                        psc.addBatch();
                    }
                    psc.executeBatch();
                }
            } catch (SQLException e) {
                throw new ExistStorageException("Resume already exist " + r.getUuid());
            }

            return null;
        });

      /*  sqlHelper.<Void>execute("INSERT INTO resume (uuid, full_name) VALUES (?, ?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
            return null;
        });
        for (Map.Entry<ContactType, String> contact : r.getContacts().entrySet()) {
            sqlHelper.<Void>execute("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)", ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, contact.getKey().name());
                ps.setString(3, contact.getValue());
                ps.execute();
                return null;
            });
        }*/
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "WHERE r.uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            do {
                String contactValue = rs.getString("value");
                ContactType contactType = ContactType.valueOf(rs.getString("type"));
                r.addContact(contactType, contactValue);
            } while (rs.next());
            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.<Void>execute("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException("Resume not exist " + uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("" +
                "SELECT uuid, full_name, type, value, resume_uuid " +
                "FROM resume r " +
                "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                "ORDER BY full_name, uuid ASC", ps -> {
            ResultSet rs = ps.executeQuery();

            List<Resume> list = new ArrayList<>();
            String currentUuid = null;
            Resume r = null;
            while (rs.next()) {
                if (!Objects.equals(rs.getString("uuid"), currentUuid)) {
                    if (Objects.nonNull(r)) {
                        list.add(r);
                    }
                    currentUuid = rs.getString("uuid");
                    r = new Resume(currentUuid.trim(), rs.getString("full_name"));
                }
                r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
            }
            list.add(r);
            return list;
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        });
    }
}
