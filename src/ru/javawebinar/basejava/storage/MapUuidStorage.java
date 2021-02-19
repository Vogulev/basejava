package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class MapUuidStorage extends AbstractMapStorage<String> {

    @Override
    protected Resume doGet(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doSave(Resume resume, String uuid) {
        storage.put(uuid, resume);
    }

    @Override
    protected void doDelete(String uuid) {
        storage.remove(uuid);
    }

    @Override
    protected void doUpdate(Resume resume, String uuid) {
        storage.replace(uuid, resume);
    }

    @Override
    protected boolean isExist(String uuid) {
        return storage.containsKey(uuid);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }
}
