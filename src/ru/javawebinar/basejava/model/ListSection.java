package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends Section {
    private final List<String> items;

    public ListSection(List<String> items) {
        Objects.requireNonNull(items, "items must be not null!");
        this.items = items;
    }

    public List<String> getItems() {
        return items;
    }
//
//    public void setItems(List<String> items) {
//        this.items = items;
//    }

    @Override
    public void view() {
        for (String str : items) {
            System.out.println(str);
        }
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
