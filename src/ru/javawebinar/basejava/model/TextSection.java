package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.util.Objects;

public class TextSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final TextSection EMPTY = new TextSection("");

    private String text;

    public TextSection(String text) {
        Objects.requireNonNull(text, "text must be not null!");
        this.text = text;
    }

    public TextSection() {
    }

    public String getText() {
        return text;
    }

    @Override
    public void view() {
        System.out.println(text);
    }

    @Override
    public String getItemsAsText() {
        return text;
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
        return Objects.hash(text);
    }
}
