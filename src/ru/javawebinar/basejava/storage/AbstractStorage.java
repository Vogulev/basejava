package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public Resume get(String uuid) {
        LOG.fine("Get " + uuid);
        SK searchKey = getSearchKeyIfResumeExist(uuid);
        return doGet(searchKey);
    }

    public void save(Resume resume) {
        LOG.fine("Save " + resume);
        SK searchKey = getSearchKeyIfResumeNotExist(resume.getUuid());
        doSave(resume, searchKey);
    }

    public void delete(String uuid) {
        LOG.fine("Delete " + uuid);
        SK searchKey = getSearchKeyIfResumeExist(uuid);
        doDelete(searchKey);
    }

    public void update(Resume resume) {
        LOG.fine("Update " + resume);
        SK searchKey = getSearchKeyIfResumeExist(resume.getUuid());
        doUpdate(resume, searchKey);
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.fine("GetAllSorted");
        List<Resume> result = getAll();
        Collections.sort(result);
        return result;
    }

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doSave(Resume resume, SK searchKey);

    protected abstract void doDelete(SK searchKey);

    protected abstract void doUpdate(Resume resume, SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract List<Resume> getAll();

    protected abstract boolean isExist(SK searchKey);

    private SK getSearchKeyIfResumeExist(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            LOG.warning("Resume with UUID " + uuid + " not exist!");
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SK getSearchKeyIfResumeNotExist(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            LOG.warning("Resume with UUID " + uuid + " already exist!");
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}
