package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (size == storage.length) {
            System.out.println("ERROR! Storage is full!");
        } else if (index < 0) {
            System.arraycopy(storage, -index - 1, storage, -index, size);
            storage[-index - 1] = resume;
            size++;
        } else System.out.println("ERROR! Resume with ID " + resume.getUuid() + " is already exist!");
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchResume = new Resume();
        searchResume.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchResume);
    }
}
