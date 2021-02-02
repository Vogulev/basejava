package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.Field;

public class mainReflection {

    public static void main(String[] args) throws IllegalAccessException {
        Resume resume = new Resume();
        Field field = resume.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(resume));
        field.set(resume, "new_uuid");
        // TODO : invoke...
        System.out.println(resume);
    }
}
