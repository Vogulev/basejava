package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        MainFile mainFile = new MainFile();
        mainFile.walkFileTree(new File("./src"), "");
    }

    public void walkFileTree(File dir, String spaces) {
        File[] list = dir.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isFile()) {
                    System.out.println(spaces + "File: " + file.getName());
                }
                if (file.isDirectory()) {
                    System.out.println(spaces + "Directory: " + file.getName());
                    walkFileTree(file, spaces.concat(" "));
                }
            }
        }
    }

}
