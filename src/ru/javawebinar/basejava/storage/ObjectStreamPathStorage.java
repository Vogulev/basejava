package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.SaveStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ObjectStreamPathStorage extends AbstractPathStorage {

    private final SaveStrategy strategy;

    protected ObjectStreamPathStorage(String dir, SaveStrategy strategy) {
        super(dir);
        this.strategy = strategy;
    }

    @Override
    protected void doWrite(Resume resume, OutputStream os) throws IOException {
        strategy.doWrite(resume, os);
    }

    @Override
    protected Resume doRead(InputStream is) throws IOException {
        return strategy.doRead(is);
    }
}
