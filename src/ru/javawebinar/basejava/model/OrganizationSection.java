package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizationSection extends Section {
    Map<Link, List<Organization>> organizations = new HashMap<>();

    public OrganizationSection() {

    }

    public void add(Organization org) {
        if (!organizations.containsKey(org.getCompany())) {
            List<Organization> newList = new ArrayList<>();
            newList.add(org);
            organizations.put(org.getCompany(), newList);
        } else {
            organizations.get(org.getCompany()).add(org);
        }
    }

    @Override
    public void view() {
        for (Map.Entry<Link, List<Organization>> entry : organizations.entrySet()) {
            List<Organization> orgList = entry.getValue();
            if (orgList.size() == 1) {
                System.out.println(orgList.get(0));
            } else {
                System.out.println(entry.getKey().toString());
                for (Organization org : orgList) {
                    System.out.println(org.printContent());
                }
            }
        }
    }
}
