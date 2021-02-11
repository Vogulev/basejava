package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    public static final int STORAGE_LIMIT = 10_000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
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

    @Override
    public void saveResume(Resume resume, int index) {
        if (size == storage.length) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, index);
        size++;
    }

    @Override
    public void deleteResume(int index) {
        arrayDeleteResume(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public void updateResume(Resume resume, int index) {
        storage[index] = resume;
    }

    @Override
    public Resume returnResume(int index) {
        return storage[index];
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void arrayDeleteResume(int index);

}
