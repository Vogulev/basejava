package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataStreamSerializer implements SaveStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());

            writeWithException(resume.getContacts().entrySet(), dos, pair -> {
                dos.writeUTF(pair.getKey().name());
                dos.writeUTF(pair.getValue());
            });

            writeWithException(resume.getSections().entrySet(), dos, pair -> {
                SectionType type = pair.getKey();
                AbstractSection section = pair.getValue();
                dos.writeUTF(type.name());
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(((TextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> contentList = ((ListSection) section).getContentList();
                        writeWithException(contentList, dos, dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = ((OrganizationSection) section).getOrganizations();
                        writeWithException(organizations, dos, org -> {
                            dos.writeUTF(org.getCompanyName().getTitle());
                            String url = org.getCompanyName().getUrl();
                            dos.writeUTF(url == null ? " " : url);
                            writeWithException(org.getPosition(), dos, position -> {
                                writeDate(dos, position.getBeginDate());
                                writeDate(dos, position.getEndDate());
                                dos.writeUTF(position.getTitle());
                                String description = position.getDescription();
                                dos.writeUTF(description == null ? " " : description);
                            });
                        });
                        break;
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readWithException(dis, () -> resume.setContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));
            readWithException(dis, () -> {
                SectionType section = SectionType.valueOf(dis.readUTF());
                resume.setSections(section, readSection(dis, section));
            });
            return resume;
        }
    }

    @FunctionalInterface
    private interface WriteConsumer<T> {
        void action(T t) throws IOException;
    }

    @FunctionalInterface
    private interface ReadProcessor {
        void action() throws IOException;
    }

    @FunctionalInterface
    private interface ReadSupplier<T> {
        T get() throws IOException;
    }

    private <T> void writeWithException(Collection<T> collection, DataOutputStream dos, WriteConsumer<T> write) throws IOException {
        dos.writeInt(collection.size());
        for (T c : collection) {
            write.action(c);
        }
    }

    private void readWithException(DataInputStream dis, ReadProcessor read) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            read.action();
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType section) throws IOException {
        switch (section) {
            case OBJECTIVE:
            case PERSONAL:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return new ListSection(readListWithException(dis, dis::readUTF));
            case EXPERIENCE:
            case EDUCATION:
                return new OrganizationSection(
                        readListWithException(dis, () -> new Organization(new Link(dis.readUTF(), checkStringForNull(dis.readUTF())),
                                readListWithException(dis, () -> new Organization.Position(
                                        readDate(dis), readDate(dis), dis.readUTF(), checkStringForNull(dis.readUTF())
                                ))
                        )));
            default:
                throw new IllegalStateException();
        }
    }

    private String checkStringForNull(String str) {
        return str.equals(" ") ? null : str;
    }

    private <T> List<T> readListWithException(DataInputStream dis, ReadSupplier<T> readList) throws IOException {
        int size = dis.readInt();
        List<T> resultList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            resultList.add(readList.get());
        }
        return resultList;
    }

    private void writeDate(DataOutputStream dos, LocalDate date) throws IOException {
        dos.writeInt(date.getYear());
        dos.writeInt(date.getMonthValue());
    }

    private LocalDate readDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }
}
