package ru.javawebinar.basejava.model;

import java.util.HashMap;
import java.util.Map;

public class Contacts {
    private Map<ContactType, String> map = new HashMap<>();

    public String getContact(ContactType contactType) {
        return map.get(contactType);
    }

    public void setContact(ContactType contactType, String contact) {
        map.put(contactType, contact);
    }

    public Map<ContactType, String> getMap() {
        return map;
    }

    public void setMap(Map<ContactType, String> map) {
        this.map = map;
    }
}
