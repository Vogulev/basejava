package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void insertResume(Resume resume, int index) {
        System.arraycopy(storage, -index - 1, storage, -index, size);
        storage[-index - 1] = resume;
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchResume = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchResume);
    }
}
