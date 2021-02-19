package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class MapUuidStorage extends AbstractMapStorage {

    @Override
    protected Resume doGet(Object uuid) {
        return storage.get((String) uuid);
    }

    @Override
    protected void doSave(Resume resume, Object uuid) {
        storage.put((String) uuid, resume);
    }

    @Override
    protected void doDelete(Object uuid) {
        storage.remove((String) uuid);
    }

    @Override
    protected void doUpdate(Resume resume, Object uuid) {
        storage.replace((String) uuid, resume);
    }

    @Override
    protected boolean isExist(Object uuid) {
        return storage.containsKey((String) uuid);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }
}
