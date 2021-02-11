package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return returnResume(index);
        }
        throw new NotExistStorageException(uuid);
    }

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            saveResume(resume, index);
        } else throw new ExistStorageException(resume.getUuid());
    }

    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            deleteResume(index);
        } else throw new NotExistStorageException(uuid);
    }

    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            updateResume(resume, index);
        } else throw new NotExistStorageException(resume.getUuid());
    }

    protected abstract void saveResume(Resume resume, int index);

    protected abstract void deleteResume(int index);

    protected abstract void updateResume(Resume resume, int index);

    protected abstract Resume returnResume(int index);

    protected abstract int getIndex(String uuid);
}
