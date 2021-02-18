package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

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

    @Override
    protected boolean isExist(Object index) {
        return (Integer) index >= 0;
    }

    @Override
    public void doSave(Resume resume, Object searchKey) {
        if (size == storage.length) {
            throw new StorageException("Storage overflow", resume.getUuid());
        }
        insertResume(resume, (Integer) searchKey);
        size++;
    }

    @Override
    public void doDelete(Object searchKey) {
        deleteResumeFromArray((Integer) searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected List<Resume> getListFromStorage() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    @Override
    public void doUpdate(Resume resume, Object searchKey) {
        storage[(int) searchKey] = resume;
    }

    @Override
    public Resume doGet(Object searchKey) {
        return storage[(int) searchKey];
    }

    protected abstract void insertResume(Resume resume, int index);

    protected abstract void deleteResumeFromArray(int index);

}
