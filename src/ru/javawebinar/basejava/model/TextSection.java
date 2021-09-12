package ru.javawebinar.basejava.model;

import java.util.Objects;

public class TextSection extends Section {
    private final String text;

    public TextSection(String text) {
        Objects.requireNonNull(text, "text must be not null!");
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void view() {
        System.out.println(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextSection that = (TextSection) o;

        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}