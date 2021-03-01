package ru.javawebinar.basejava.model;

import java.util.List;

class ListSection extends AbstractSection {
    private final List<String> contentList;

    public ListSection(List<String> contentList) {
        this.contentList = contentList;
    }

    @Override
    public void getContent() {
        for (String text : contentList) {
            System.out.println("- " + text);
        }
    }
}
