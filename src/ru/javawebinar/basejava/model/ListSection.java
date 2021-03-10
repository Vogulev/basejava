package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> contentList;

    public ListSection(String... items) {
        this(Arrays.asList(items));
    }

    public ListSection(List<String> contentList) {
        Objects.requireNonNull(contentList, "contentList must not be null");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return contentList.equals(that.contentList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentList);
    }
}
