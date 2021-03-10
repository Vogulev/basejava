package ru.javawebinar.basejava.model;

import java.io.Serial;
import java.io.Serializable;

public class Link implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String title;
    private String url;

    public Link(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
