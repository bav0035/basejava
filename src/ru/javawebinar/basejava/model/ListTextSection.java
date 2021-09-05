package ru.javawebinar.basejava.model;

import java.util.List;

public class ListTextSection extends Section {
    private List<String> text;

    public ListTextSection(List<String> text) {
        this.text = text;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    @Override
    public void view() {
        for (String str : text) {
            System.out.println(str);
        }
        System.out.println();
    }
}
