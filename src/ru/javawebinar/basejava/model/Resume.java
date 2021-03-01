package ru.javawebinar.basejava.model;

import java.util.*;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);
    private Organization experience;
    private Organization education;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "UUID must not be null!");
        Objects.requireNonNull(fullName, "fullName must not be null!");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public void printContacts() {
        for (Map.Entry<ContactType, String> pair : contacts.entrySet()) {
            System.out.println(pair.getKey().getTitle() + " " + pair.getValue());
        }
    }

    public void printSections() {
        for (Map.Entry<SectionType, AbstractSection> pair : sections.entrySet()) {
            System.out.println(pair.getKey().getTitle());
            pair.getValue().getContent();
        }
    }

    public void addContact(ContactType contactType, String contact) {
        contacts.put(contactType, contact);
    }

    public void addTextSection(SectionType sectionType, String content) {
        sections.put(sectionType, new TextSection(content));
    }

    public void addListSection(SectionType sectionType, List<String> content) {
        sections.put(sectionType, new ListSection(content));
    }

    public void addExperienceSection(SectionType sectionType, String companyName, String date, String position, String description) {
        if (experience == null) {
            experience = new Organization(companyName, date, position, description);
            sections.put(sectionType, experience);
        } else experience.addContent(companyName, date, position, description);
    }

    public void addEducationSection(SectionType sectionType, String companyName, String date, String position, String description) {
        if (education == null) {
            education = new Organization(companyName, date, position, description);
            sections.put(sectionType, education);
        } else education.addContent(companyName, date, position, description);
    }

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public int compareTo(Resume o) {
        int result = fullName.compareTo(o.fullName);
        return result == 0 ? uuid.compareTo(o.uuid) : result;
    }
}
