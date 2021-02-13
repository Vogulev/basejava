package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {
    private Map<Integer, Resume> storage = new TreeMap<>();

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    protected void saveResume(Resume resume, int index) {
        storage.put(storage.size(), resume);
    }

    @Override
    protected void deleteResume(int index) {
        storage.remove(index);
    }

    @Override
    protected void updateResume(Resume resume, int index) {
        storage.replace(index, resume);
    }

    @Override
    protected Resume receiveResume(int index) {
        return storage.get(index);
    }

    @Override
    protected int getIndex(String uuid) {
        for (Map.Entry<Integer, Resume> pair : storage.entrySet()) {
            String searchUuid = pair.getValue().getUuid();
            if (uuid.equals(searchUuid)) {
                return pair.getKey();
            }
        }
        return -1;
    }
}
