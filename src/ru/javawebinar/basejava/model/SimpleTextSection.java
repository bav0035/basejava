package ru.javawebinar.basejava.model;

public class SimpleTextSection extends Section {
    private String text;

    public SimpleTextSection(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void view() {
        System.out.println(text);
        System.out.println();
    }
}
