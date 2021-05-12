package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.util.UUID;

public class TestData {
    public static final String UUID1 = UUID.randomUUID().toString();
    public static final String UUID2 = UUID.randomUUID().toString();
    public static final String UUID3 = UUID.randomUUID().toString();
    public static final String UUID4 = UUID.randomUUID().toString();

    public static final Resume resume1 = ResumeTestData.createResume(UUID1, "Vasya");
    public static final Resume resume2 = ResumeTestData.createResume(UUID2, "Petya");
    public static final Resume resume3 = ResumeTestData.createResume(UUID3, "Vasya");
    public static final Resume resume4 = ResumeTestData.createResume(UUID4, "Artem");
}
