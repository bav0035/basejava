package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

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
        return sqlHelper.execute("" +
                "SELECT * FROM resume r " +
                "LEFT JOIN contact c " +
                "ON r.uuid = c.resume_uuid " +
                "LEFT JOIN section s " +
                "ON r.uuid = s.resume_uuid " +
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
                String sectionValue = rs.getString("section_value");
                SectionType sectionType = SectionType.valueOf(rs.getString("section_type"));
                addSection(r, sectionType, sectionValue);

            } while (rs.next());
            return r;
        });
    }

    private void addSection(Resume r, SectionType sectionType, String sectionValue) {
        if (sectionType.toString().equals("PERSONAL") ||sectionType.toString().equals("OBJECTIVE")) {
            r.addSection(sectionType, new TextSection(sectionValue));
        }
        if (sectionType.toString().equals("ACHIEVEMENT") ||sectionType.toString().equals("QUALIFICATIONS")) {
            r.addSection(sectionType, new ListSection(Arrays.asList(sectionValue.split("\n"))));
        }
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
        List<Resume> list = new ArrayList<>();
        Map<String, String> resumeMap = sqlHelper.execute("SELECT * FROM resume ORDER BY uuid", psr -> {
            ResultSet rs = psr.executeQuery();
            HashMap<String, String> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("uuid"), rs.getString("full_name"));
            }
            System.out.println();
            return map;
        });

        Map<String, Resume> fullResumeMap = sqlHelper.execute("SELECT * FROM contact ORDER BY resume_uuid", psc -> {
            ResultSet contactsRS = psc.executeQuery();
            String uuid = null;
            Resume r = null;
            Map<String, Resume> map = new HashMap<>();
            String resume_uuid = null;
            while (contactsRS.next()) {
                resume_uuid = contactsRS.getString("resume_uuid");
                if (!Objects.equals(uuid, resume_uuid)) {
                    if (r != null) {
                        map.put(uuid, r);
                    }
                    uuid = resume_uuid;
                    r = new Resume(resume_uuid.trim(), resumeMap.get(resume_uuid));
                }
                r.addContact(ContactType.valueOf(contactsRS.getString("type")),
                        contactsRS.getString("value"));
            }
            map.put(resume_uuid, r);
            return map;
        });

        sqlHelper.execute("SELECT * FROM section ORDER BY resume_uuid", pss -> {
            ResultSet sectionsRS = pss.executeQuery();

            String resume_uuid = null;
            while (sectionsRS.next()) {
                String uuid = sectionsRS.getString("resume_uuid");
                String sectionValue = sectionsRS.getString("section_value");
                SectionType sectionType = SectionType.valueOf(sectionsRS.getString("section_type"));
                Resume r = fullResumeMap.get(uuid);
                addSection(r, sectionType, sectionValue);
            }
            return null;
        });

        List<String> keyList = new ArrayList<>(fullResumeMap.keySet());
        Collections.sort(keyList);
        for (String str: keyList) {
//            fullResumeMap.get(str).view();
            list.add(fullResumeMap.get(str));
        }
        return list;
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

    private void deleteContacts(Connection conn, Resume r) {
        sqlHelper.execute("DELETE FROM contact WHERE resume_uuid = ?", ps -> {
            ps.setString(1, r.getUuid());
            ps.execute();
            return null;
        });
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
        try (PreparedStatement psc = conn.prepareStatement("INSERT INTO section (resume_uuid, section_type, section_value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> section : r.getSections().entrySet()) {
                psc.setString(1, r.getUuid());
                psc.setString(2, section.getKey().name());
                psc.setString(3, section.getValue().getItemsAsText());
                psc.addBatch();
            }
            psc.executeBatch();
        }
    }

}
