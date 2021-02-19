package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private List<Resume> storage = new ArrayList<>();

    public int size() {
        return storage.size();
    }

    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> getListFromStorage() {
        return new ArrayList<>(storage);
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey != null;
    }

    @Override
    protected void doSave(Resume resume, Integer searchKey) {
        storage.add(resume);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        storage.remove(searchKey.intValue());
    }

    @Override
    public void doUpdate(Resume resume, Integer searchKey) {
        storage.set(searchKey, resume);
    }

    @Override
    public Resume doGet(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }
}
