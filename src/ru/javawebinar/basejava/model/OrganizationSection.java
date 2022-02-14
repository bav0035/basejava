package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;
    List<Organization> organizations = new ArrayList<>();

    public OrganizationSection() {

    }

    public OrganizationSection(List<Organization> list) {
        organizations = list;
    }

    public OrganizationSection(Organization... items) {
        organizations = Arrays.asList(items);
    }

    public void add(Organization org) {
        organizations.add(org);
    }

    @Override
    public void view() {
        for (Organization org : organizations) {
            System.out.println(org);
        }
    }

    @Override
    public String getItemsAsText() {
        System.out.println("You must implements method takeItemsAsText()!");
        return null;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return Objects.equals(organizations, that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }
}
