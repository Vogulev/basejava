package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractStorageTest {

    protected static final File STORAGE_DIR = new File("D:\\Lessons\\basejava\\storage");

    protected final Storage storage;

    private static final String UUID1 = "uuid1";
    private static final String UUID2 = "uuid2";
    private static final String UUID3 = "uuid3";
    private static final String UUID4 = "uuid4";

    private static final Resume resume1 = ResumeTestData.createResume(UUID1, "Name" + UUID1);
    private static final Resume resume2 = ResumeTestData.createResume(UUID2, "Name" + UUID2);
    private static final Resume resume3 = ResumeTestData.createResume(UUID3, "Name" + UUID3);
    private static final Resume resume4 = ResumeTestData.createResume(UUID4, "Name" + UUID4);

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
        assertGet(resume4);
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(resume3);
    }

    @Test
    public void get() {
        assertGet(resume1);
        assertGet(resume2);
        assertGet(resume3);
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
        List<Resume> resumes = Arrays.asList(resume1, resume2, resume3);
        Assert.assertEquals(getAllSortedResumes.size(), resumes.size());
        Assert.assertEquals(getAllSortedResumes, resumes);
    }

    @Test
    public void update() {
        Resume newResume = new Resume(UUID1, "New FullName");
        storage.update(newResume);
        Assert.assertEquals(newResume, storage.get(UUID1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(resume4);
    }

    public void assertGet(Resume resume) {
        Assert.assertEquals(resume, storage.get(resume.getUuid()));
    }
}
