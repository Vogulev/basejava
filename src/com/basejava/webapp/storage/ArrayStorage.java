package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (!isExist(r.getUuid()) && size < 10000) {
            storage[size] = r;
            size++;
        }
        else System.out.println("ERROR! Resume is already exist or storage is full!");
    }

    public Resume get(String uuid) {
        if (isExist(uuid)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(uuid)) {
                    return storage[i];
                }
            }
        }
        return null;
    }

    public void delete(String uuid) {
        if (isExist(uuid)) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(uuid)) {
                    System.arraycopy(storage, i + 1, storage, i, size - i);
                    size--;
                }
            }
        }
        else System.out.println("No resume for delete!");
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        Resume[] allResumes = new Resume[size];
        System.arraycopy(storage, 0, allResumes, 0, size);
        return allResumes;
    }

    public int size() {
        return size;
    }

    public void update(Resume r) {
        if (isExist(r.getUuid())) {
            for (int i = 0; i < size; i++) {
                if (storage[i].getUuid().equals(r.getUuid())) {
                    storage[i] = r;
                }
            }
        }
        else System.out.println("No resume for update!");
    }

    public boolean isExist(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }
}
