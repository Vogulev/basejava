package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class MapResumeStorage extends AbstractMapStorage {


    @Override
    protected Resume doGet(Object resume) {
        return (Resume) resume;
    }

    @Override
    protected void doSave(Resume saveResume, Object resume) {
        storage.put(saveResume.getUuid(), saveResume);
    }

    @Override
    protected void doDelete(Object resume) {
        String uuid = ((Resume) resume).getUuid();
        storage.remove(uuid);
    }

    @Override
    protected boolean isExist(Object resume) {
        return resume != null;
    }

    @Override
    protected void doUpdate(Resume updateResume, Object resume) {
        storage.replace(updateResume.getUuid(), updateResume);
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

}
 