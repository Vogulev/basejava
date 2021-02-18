package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Map;

public class MapUuidStorage extends AbstractMapStorage {

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void doSave(Resume resume, Object searchKey) {
        storage.put(String.valueOf(storage.size()), resume);
    }

    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(searchKey);
    }

    @Override
    protected void doUpdate(Resume resume, Object searchKey) {
        storage.replace(searchKey.toString(), resume);
    }


    @Override
    protected Object getSearchKey(String uuid) {
        for (Map.Entry<String, Resume> pair : storage.entrySet()) {
            String searchUuid = pair.getValue().getUuid();
            if (uuid.equals(searchUuid)) {
                return pair.getKey();
            }
        }
        return null;
    }
}
