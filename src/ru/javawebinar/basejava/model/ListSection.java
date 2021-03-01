package ru.javawebinar.basejava.model;

import java.util.List;

public class ListSection extends AbstractSection {
    private final List<String> contentList;

    public ListSection(List<String> contentList) {
        this.contentList = contentList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String str : contentList) {
            sb.append("\n").append("- ").append(str);
        }
        return sb.toString() + '\n';
    }
}
