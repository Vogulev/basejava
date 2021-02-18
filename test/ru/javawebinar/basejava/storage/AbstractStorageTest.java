package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractStorageTest {

    protected final Storage storage;

    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";
    private static final String UUID4 = "uuid4";

    private static final Resume resume1 = new Resume(UUID1, "Vasiliy Ivanovich Ivanov");
    private static final Resume resume2 = new Resume(UUID2, "Petrov Petr Petrovich");
    private static final Resume resume3 = new Resume(UUID3, "Ivanov Vasiliy Antonovich");
    private static final Resume resume4 = new Resume(UUID4, "Gorka Man Womanovich");

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void save() {
        storage.save(resume4);
        Assert.assertEquals(resume4, storage.get(UUID4));
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(resume3);
    }

    @Test
    public void get() {
        Assert.assertEquals(resume1, storage.get(UUID1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(UUID4);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID2);
        Assert.assertEquals(2, storage.size());
        storage.get(UUID2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(UUID4);
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void getAllSorted() {
        List<Resume> getAllSortedResumes = storage.getAllSorted();
        List<Resume> resumes = new ArrayList<>();
        resumes.add(resume1);
        resumes.add(resume2);
        resumes.add(resume3);
        Collections.sort(resumes);
        for (int i = 0; i < resumes.size(); i++) {
            Assert.assertEquals(getAllSortedResumes.get(i), resumes.get(i));
        }
    }

    @Test
    public void update() {
        storage.update(resume1);
        Assert.assertEquals(resume1, storage.get(UUID1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(resume4);
    }
}
