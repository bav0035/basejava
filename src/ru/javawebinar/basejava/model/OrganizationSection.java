package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section {
    List<Organization> organizations = new ArrayList<>();

    public OrganizationSection(List<Organization> organizations) {
        Objects.requireNonNull(organizations, "organizations must be not null!");
        this.organizations = organizations;
    }

    public void add (Organization org) {
        organizations.add(org);
    }

    public List<Organization> getOrganizations() {
        return new ArrayList<>(organizations);
    }

    @Override
    public void view() {
        for (Organization org : organizations) {
            System.out.println(org.toString());
            System.out.println();
        }
    }
}
