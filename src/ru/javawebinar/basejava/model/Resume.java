package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String uuid;
    private final String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, AbstractSection> sections = new EnumMap<>(SectionType.class);

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

    public void setContacts(ContactType contactType, String contact) {
        contacts.put(contactType, contact);
    }

    public void setSections(SectionType sectionType, AbstractSection section) {
        sections.put(sectionType, section);
    }

    @Override
    public String toString() {
        StringBuilder contactsBuilder = new StringBuilder();
        for (Map.Entry<ContactType, String> pair : contacts.entrySet()) {
            contactsBuilder.append('\n').append(pair.getKey().getTitle()).append(" - ").append(pair.getValue());
        }
        return "Resume" +
                "\nuuid='" + uuid + "'\n" +
                "ФИО:  " + fullName + '\n' +
                contactsBuilder.toString() + '\n' +
                sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        return Objects.equals(fullName, resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public int compareTo(Resume o) {
        int result = fullName.compareTo(o.fullName);
        return result == 0 ? uuid.compareTo(o.uuid) : result;
    }
}
