package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<String> items = new ArrayList<>();

    public ListSection() {
    }

    public ListSection(String... items) {
        this(Arrays.asList(items));
    }

    public ListSection(List<String> items) {
        Objects.requireNonNull(items, "items must be not null!");
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public void view() {
        for (String str : items) {
            System.out.println(str);
        }
    }

    @Override
    public String getItemsAsText() {
        StringBuilder text = new StringBuilder();
        for (String str : items) {
            text.append(str);
            text.append('\n');
        }
        text.deleteCharAt(text.length() - 1);
        return text.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
