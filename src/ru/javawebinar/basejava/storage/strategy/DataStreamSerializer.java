package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements SaveStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            Map<ContactType, String> contacts = resume.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> pair : contacts.entrySet()) {
                dos.writeUTF(pair.getKey().name());
                dos.writeUTF(pair.getValue());
            }

            Map<SectionType, AbstractSection> sections = resume.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, AbstractSection> pair : sections.entrySet()) {
                SectionType type = pair.getKey();
                AbstractSection section = pair.getValue();
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(type.name());
                        dos.writeUTF(((TextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        dos.writeUTF(type.name());
                        List<String> contentList = ((ListSection) section).getContentList();
                        dos.writeInt(contentList.size());
                        for (String str : contentList) {
                            dos.writeUTF(str);
                        }
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        dos.writeUTF(type.name());
                        List<Organization> organizations = ((OrganizationSection) section).getOrganizations();
                        dos.writeInt(organizations.size());
                        for (Organization org : organizations) {
                            List<Organization.Position> positions = org.getPosition();
                            dos.writeInt(positions.size());
                            for (Organization.Position position : positions) {
                                writeDate(dos, position.getBeginDate());
                                writeDate(dos, position.getEndDate());
                                dos.writeUTF(position.getTitle());
                                dos.writeUTF(position.getDescription());
                            }
                            dos.writeUTF(org.getCompanyName().getTitle());
                            dos.writeUTF(org.getCompanyName().getUrl());
                        }
                        break;
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.setContacts(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                SectionType section = SectionType.valueOf(dis.readUTF());
                switch (section) {
                    case OBJECTIVE:
                    case PERSONAL:
                        resume.setSections(section, new TextSection(dis.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        int listSize = dis.readInt();
                        ArrayList<String> contentList = new ArrayList<>(listSize);
                        for (int j = 0; j < listSize; j++) {
                            contentList.add(dis.readUTF());
                        }
                        resume.setSections(section, new ListSection(contentList));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        int organizationsSize = dis.readInt();
                        ArrayList<Organization> organizations = new ArrayList<>(organizationsSize);
                        for (int j = 0; j < organizationsSize; j++) {
                            int positionsSize = dis.readInt();
                            ArrayList<Organization.Position> positions = new ArrayList<>(positionsSize);
                            for (int x = 0; x < positionsSize; x++) {
                                positions.add(new Organization.Position(readDate(dis), readDate(dis), dis.readUTF(), dis.readUTF()));
                            }
                            organizations.add(new Organization(new Link(dis.readUTF(), dis.readUTF()), positions));
                        }
                        resume.setSections(section, new OrganizationSection(organizations));
                        break;
                }
            }
            return resume;
        }
    }

    private void writeDate(DataOutputStream dos, LocalDate date) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonthValue());
    }

    private LocalDate readDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }
}
