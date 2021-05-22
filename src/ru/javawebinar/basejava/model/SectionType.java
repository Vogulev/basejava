package ru.javawebinar.basejava.model;

public enum SectionType {
    OBJECTIVE("Позиция"),
    PERSONAL("Личные качества"),
    ACHIEVEMENT("Достижения"),
    QUALIFICATIONS("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(AbstractSection value) {
        return title + ": " + value;
    }

    public String toHtml(AbstractSection value) {
        return (value == null) ? "" : toHtml0(value);
    }
}
