package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            int sectionCount = 0;
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                if (sectionCount < 2) {
                    writeTextSection(entry, dos);
                } else if (sectionCount < 4) {
                    writeListSection(entry, dos);
                } else {
                    writeOrganizationSection(entry, dos);
                }
                sectionCount++;
            }
        }
    }

    private void writeTextSection(Map.Entry<SectionType, Section> entry, DataOutputStream dos) throws IOException {
        dos.writeUTF(((TextSection) entry.getValue()).getText());
    }

    private void writeListSection(Map.Entry<SectionType, Section> entry, DataOutputStream dos) throws IOException {
        List<String> list = ((ListSection) entry.getValue()).getItems();
        dos.writeInt(list.size());
        for (String str : list) {
            dos.writeUTF(str);
        }
    }

    private void writeOrganizationSection(Map.Entry<SectionType, Section> entry, DataOutputStream dos) throws IOException {
        List<Organization> organizationList = ((OrganizationSection) entry.getValue()).getOrganizations();
        dos.writeInt(organizationList.size());
        for (Organization organization : organizationList) {
            Link company = organization.getCompany();
            dos.writeUTF(company.getName());
            dos.writeUTF(Objects.nonNull(company.getUrl()) ? company.getUrl() : "NULL");

            List<Organization.Position> positionList = organization.getPositions();
            dos.writeInt(positionList.size());
            for (Organization.Position position : positionList) {
                dos.writeUTF(position.getStartDate().toString());
                dos.writeUTF(position.getEndDate().toString());
                dos.writeUTF(position.getTitle());
                dos.writeUTF(Objects.nonNull(position.getDescription()) ? position.getDescription() : "NULL");
            }
        }

    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int contactSize = dis.readInt();
            for (int i = 0; i < contactSize; i++) {
                ContactType contactType = ContactType.valueOf(dis.readUTF());
                String contact = dis.readUTF();
                resume.addContact(contactType, contact);
            }

            int sectionSize = dis.readInt();
            for (int i = 0; i < sectionSize; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                Section section;
                if (i < 2) {
                    section = readTextSection(dis);
                } else if (i < 4) {
                    section = readListSection(dis);
                } else {
                    section = readOrganizationSection(dis);
                }
                resume.addSection(sectionType, section);
            }
            return resume;
        }
    }

    private TextSection readTextSection(DataInputStream dis) throws IOException {
        return new TextSection(dis.readUTF());
    }

    private ListSection readListSection(DataInputStream dis) throws IOException {
        List<String> list = new ArrayList<>();
        int count = dis.readInt();
        for (int i = 0; i < count; i++) {
            list.add(dis.readUTF());
        }
        return new ListSection(list);
    }

    private OrganizationSection readOrganizationSection(DataInputStream dis) throws IOException {
        OrganizationSection organizationSection = new OrganizationSection();
        int organizationCount = dis.readInt();
        for (int i = 0; i < organizationCount; i++) {
            String companyName = dis.readUTF();
            String companyURL = dis.readUTF();
            if (companyURL.equals("NULL")) {
                companyURL = null;
            }

            int positionCount = dis.readInt();
            List<Organization.Position> positionList = new ArrayList<>();
            for (int j = 0; j < positionCount; j++) {
                LocalDate startDate = LocalDate.parse(dis.readUTF());
                LocalDate endDate = LocalDate.parse(dis.readUTF());
                String title = dis.readUTF();
                String description = dis.readUTF();
                if (description.equals("NULL")) {
                    description = null;
                }
                Organization.Position position = new Organization.Position(startDate, endDate, title, description);
                positionList.add(position);
            }
            Organization organization = new Organization(companyName, companyURL, positionList);
            organizationSection.add(organization);
        }
        return organizationSection;
    }


}
