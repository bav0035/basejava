package ru.javawebinar.basejava.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
public class Resume implements Comparable<Resume> {
    private final String uuid;

    private final String fullName;

    public Contacts contacts = new Contacts();

    public Map<SectionType, Section> sections = new HashMap<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
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

//    @Override
//    public int compareTo(Resume o) {
//        return uuid.compareTo(o.uuid);
//    }
}