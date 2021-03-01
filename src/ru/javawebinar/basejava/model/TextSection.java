package ru.javawebinar.basejava.model;

class TextSection extends AbstractSection {
    private final String content;

    public TextSection(String content) {
        this.content = content;
    }

    public void getContent() {
        System.out.println(content);
    }
}
