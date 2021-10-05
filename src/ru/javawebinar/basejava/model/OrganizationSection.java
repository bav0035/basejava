package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizationSection extends Section {
    List<Organization> organizations = new ArrayList<>();

    public OrganizationSection() {

    }

    public void add(Organization org) {
        organizations.add(org);
    }

    @Override
    public void view() {
        for (Organization org : organizations) {
            System.out.println(org);
        }
//        System.out.println(organizations);
    }
}
