package ru.javawebinar.basejava.model;

public enum ContactType {
    PHONENUMBER("Номер телефона"),
    EMAIL("e-Mail"),
    SKYPE("Skype"),
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
