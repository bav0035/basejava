package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serial;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final Resume EMPTY = new Resume();
    static {
        EMPTY.addSection(SectionType.OBJECTIVE, TextSection.EMPTY);
        EMPTY.addSection(SectionType.PERSONAL, TextSection.EMPTY);
        EMPTY.addSection(SectionType.ACHIEVEMENT, ListSection.EMPTY);
        EMPTY.addSection(SectionType.QUALIFICATIONS, ListSection.EMPTY);
        EMPTY.addSection(SectionType.EXPERIENCE, new OrganizationSection(Organization.EMPTY));
        EMPTY.addSection(SectionType.EDUCATION, new OrganizationSection(Organization.EMPTY));
    }

    private String uuid;

    private String fullName;

    private Map<ContactType, String> contacts = new EnumMap<ContactType, String>(ContactType.class);

    private Map<SectionType, Section> sections = new EnumMap<SectionType, Section>(SectionType.class);

    public Resume() {
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "required not null uuid");
        Objects.requireNonNull(uuid, "required not null fullName");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getContact(ContactType type) {
        return contacts.get(type);
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

    public Section getSection(SectionType type) {
        return sections.get(type);
    }

    public void addContact(ContactType ct, String contact) {
        contacts.put(ct, contact);
    }

    public void addSection(SectionType st, Section section) {
        sections.put(st, section);
    }

    public void view() {
        System.out.println(uuid);
        System.out.println(fullName);

        if (contacts.size() > 0) {
            for (ContactType ct : ContactType.values()) {
                System.out.print(ct.getTitle() + ": ");
                System.out.println(getContact(ct));
            }
        }

        System.out.println();
        if (sections.size() > 0) {
            for (SectionType st : SectionType.values()) {
                if (sections.containsKey(st)) {
                    System.out.println(st.getTitle());
                    getSection(st).view();
                }
            }
        }
        System.out.println("=======================");
        System.out.println();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        boolean result = Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName)
                && Objects.equals(contacts, resume.contacts)
                && Objects.equals(sections, resume.sections);
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public String toString() {
        return uuid + "(" + fullName + ")";
    }

    @Override
    public int compareTo(Resume o) {
        int comp = fullName.compareTo(o.getFullName());
        return comp != 0 ? comp : uuid.compareTo(o.getUuid());
    }
}