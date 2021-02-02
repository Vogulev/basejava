package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (size == storage.length) {
            System.out.println("ERROR! Storage is full!");
        } else if (index < 0) {
            insertResume(resume, index);
            size++;
        } else System.out.println("ERROR! Resume with ID " + resume.getUuid() + " is already exist!");
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            overrideElement(index);
            size--;
        } else System.out.println("No resume with ID " + uuid + " for delete!");
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        System.out.println("ERROR! Resume with ID " + uuid + " not exist!");
        return null;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        Resume[] allResumes = new Resume[size];
        System.arraycopy(storage, 0, allResumes, 0, size);
        return allResumes;
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else System.out.println("No resume with ID " + resume.getUuid() + " for update!");
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void overrideElement(int index);

    protected abstract int getIndex(String uuid);
}
