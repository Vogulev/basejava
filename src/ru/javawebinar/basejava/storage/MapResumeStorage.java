package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public class MapResumeStorage extends AbstractMapStorage<Resume> {


    @Override
    protected Resume doGet(Resume resume) {
        return resume;
    }

    @Override
    protected void doSave(Resume saveResume, Resume resume) {
        storage.put(saveResume.getUuid(), saveResume);
    }

    @Override
    protected void doDelete(Resume resume) {
        String uuid = resume.getUuid();
        storage.remove(uuid);
    }

    @Override
    protected boolean isExist(Resume resume) {
        return resume != null;
    }

    @Override
    protected void doUpdate(Resume updateResume, Resume resume) {
        storage.replace(updateResume.getUuid(), updateResume);
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

}
 