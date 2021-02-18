package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    public Resume get(String uuid) {
        Object searchKey = getSearchKeyIfResumeExist(uuid);
        return doGet(searchKey);
    }

    public void save(Resume resume) {
        Object searchKey = getSearchKeyIfResumeNotExist(resume.getUuid());
        doSave(resume, searchKey);
    }

    public void delete(String uuid) {
        Object searchKey = getSearchKeyIfResumeExist(uuid);
        doDelete(searchKey);
    }

    public void update(Resume resume) {
        Object searchKey = getSearchKeyIfResumeExist(resume.getUuid());
        doUpdate(resume, searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> result = getListFromStorage();
        Collections.sort(result);
        return result;
    }

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doSave(Resume resume, Object searchKey);

    protected abstract void doDelete(Object searchKey);

    protected abstract void doUpdate(Resume resume, Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract List<Resume> getListFromStorage();

    protected abstract boolean isExist(Object searchKey);

    private Object getSearchKeyIfResumeExist(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) throw new NotExistStorageException(uuid);
        return searchKey;
    }

    private Object getSearchKeyIfResumeNotExist(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) throw new ExistStorageException(uuid);
        return searchKey;
    }
}
