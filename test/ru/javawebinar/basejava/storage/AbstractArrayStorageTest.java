package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.StorageException;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void storageOverflow() {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(ResumeTestData.createResume(String.valueOf(Math.random()), "Name" + i));
            }
        } catch (StorageException e) {
            Assert.fail("Overflow too early!");
        }
        storage.save(ResumeTestData.createResume(String.valueOf(Math.random()), "Name10001"));
    }
}