package ru.javawebinar.basejava.model;

public class TextSection extends AbstractSection {
    private final String content;

    public TextSection(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return '\n' + content + "\n";
    }
}
