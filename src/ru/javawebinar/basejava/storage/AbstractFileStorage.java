package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }

    @Override
    @SuppressWarnings("all")
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void doWrite(Resume resume, File file) throws IOException;

    protected abstract Resume doRead(File file);

    @Override
    @SuppressWarnings("all")
    protected void doDelete(File file) {
        file.delete();
    }

    @Override
    protected void doUpdate(Resume resume, File file) {
        try {
            doWrite(resume, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    @SuppressWarnings("all")
    protected List<Resume> getListFromStorage() {
        List<Resume> resumeList = new ArrayList<>();
        for (File file : directory.listFiles()) {
            resumeList.add(doRead(file));
        }
        return resumeList;
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    @SuppressWarnings("all")
    public int size() {
        int size = 0;
        for (File file : directory.listFiles()) {
            if (!file.isDirectory()) size++;
        }
        return size;
    }

    @Override
    @SuppressWarnings("all")
    public void clear() {
        for (File file : directory.listFiles()) {
            file.delete();
        }
    }
}
