package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.util.JsonParser;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = new ConnectionFactory() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        };
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException("Resume not exist " + r.getUuid());
                }
            }
            deleteContacts(conn, r);
            insertContacts(conn, r);
            deleteSections(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            } catch (SQLException e) {
                throw new ExistStorageException("Resume already exist " + r.getUuid());
            }
            insertContacts(conn, r);
            insertSection(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume r;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                String fullname = rs.getString("full_name").trim();
                r = new Resume(uuid, fullname);
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ContactType contactType = ContactType.valueOf(rs.getString("type"));
                    String value = rs.getString("value");
                    r.addContact(contactType, value);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section where resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SectionType sectionType = SectionType.valueOf(rs.getString("section_type"));
                    String sectionValue = rs.getString("section_value");
                    addSection(r, sectionType, sectionValue);
                }
            }
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
        return sqlHelper.transactionalExecute(conn -> {
            Map<String, Resume> resumeMap = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid").trim();
                    resumeMap.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid").trim();
                    Resume r = resumeMap.get(uuid);
                    ContactType contactType = ContactType.valueOf(rs.getString("type"));
                    String value = rs.getString("value");
                    r.addContact(contactType, value);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid").trim();
                    Resume r = resumeMap.get(uuid);
                    SectionType sectionType = SectionType.valueOf(rs.getString("section_type"));
                    String sectionValue = rs.getString("section_value");
                    addSection(r, sectionType, sectionValue);
                }
            }
            return new ArrayList<>(resumeMap.values());
        });
    }

    private void addSection(Resume r, SectionType sectionType, String sectionValue) {
        r.addSection(sectionType, JsonParser.read(sectionValue, Section.class));
    }

    public List<Resume> getAllSortedWithOneRequest() {
        return sqlHelper.execute("" +
                "SELECT * " +
                "FROM resume r " +
                "LEFT JOIN contact c ON r.uuid = c.resume_uuid " +
                "ORDER BY full_name, uuid, type ASC", ps -> {
            ResultSet rs = ps.executeQuery();

            List<Resume> list = new ArrayList<>();
            String currentUuid = null;
            Resume r = null;
            while (rs.next()) {
                System.out.println(rs.getString("uuid") + "   " +
                        rs.getString("full_name") + "   " +
                        rs.getString("type"));
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

    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        deleteAttr(conn, r, "DELETE FROM contact WHERE resume_uuid = ?");
    }

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        deleteAttr(conn, r, "DELETE FROM section WHERE resume_uuid = ?");
    }

    private void deleteAttr(Connection conn, Resume r, String query) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement psc = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> contact : r.getContacts().entrySet()) {
                psc.setString(1, r.getUuid());
                psc.setString(2, contact.getKey().name());
                psc.setString(3, contact.getValue());
                psc.addBatch();
            }
            psc.executeBatch();
        }
    }

    private void insertSection(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, section_type, section_value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                Section section = entry.getValue();
                ps.setString(3, JsonParser.write(section, Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

}
