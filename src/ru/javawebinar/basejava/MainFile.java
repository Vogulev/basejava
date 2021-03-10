package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        MainFile mainFile = new MainFile();
        mainFile.walkFileTree(new File("./src/ru/javawebinar/basejava"));
    }

    public void walkFileTree(File dir) {
        File[] list = dir.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isFile()) {
                    System.out.println("File: " + file.getName());
                }
                if (file.isDirectory()) {
                    System.out.println("Directory: " + file.getName());
                    walkFileTree(file);
                }
            }
        }
    }

}
