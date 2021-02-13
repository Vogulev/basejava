package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public Resume get(String uuid) {
        return receiveResume(workWithIndexAboveZero(uuid));
    }

    public void save(Resume resume) {
        saveResume(resume, workWithIndexBelowOrEqualsZero(resume.getUuid()));
    }

    public void delete(String uuid) {
        deleteResume(workWithIndexAboveZero(uuid));
    }

    public void update(Resume resume) {
        updateResume(resume, workWithIndexAboveZero(resume.getUuid()));
    }

    protected abstract void saveResume(Resume resume, int index);

    protected abstract void deleteResume(int index);

    protected abstract void updateResume(Resume resume, int index);

    protected abstract Resume receiveResume(int index);

    protected abstract int getIndex(String uuid);

    private int workWithIndexAboveZero(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) throw new NotExistStorageException(uuid);
        else return index;
    }

    private int workWithIndexBelowOrEqualsZero(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) throw new ExistStorageException(uuid);
        else return index;
    }
}
