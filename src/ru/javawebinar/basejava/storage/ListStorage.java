package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();


    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void save(Resume resume) {
        if (storage.contains(resume)) {
            throw new ExistStorageException(resume.getUuid());
        }
        storage.add(resume);
    }

    @Override
    public Resume get(String uuid) {
        for (Resume resume : storage) {
            if (resume.getUuid().equals(uuid)) {
                return resume;
            }
        }
        throw new NotExistStorageException(uuid);
    }

    @Override
    public void delete(String uuid) {
        Iterator<Resume> iterator = storage.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getUuid().equals(uuid)) {
                iterator.remove();
            }
        }
        throw new NotExistStorageException(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return (Resume[]) storage.toArray();
    }

    @Override
    public void update(Resume resume) {
        if (!storage.contains(resume)) {
            throw new NotExistStorageException(resume.getUuid());
        }
        storage.set(storage.indexOf(resume), resume);
    }
}
