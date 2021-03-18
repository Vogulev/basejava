package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.LocaleDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.basejava.util.DateUtil.NOW;
import static ru.javawebinar.basejava.util.DateUtil.of;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private Link companyName;
    private List<Position> position = new ArrayList<>();

    public Organization() {
    }

    public Organization(String title, String url, Position... positions) {
        this(new Link(title, url), Arrays.asList(positions));
    }

    public Organization(Link link, List<Position> position) {
        this.companyName = link;
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(companyName, that.companyName) &&
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, position);
    }

    @Override
    public String toString() {
        return '\n' +
                companyName.getUrl() + '\n' +
                position.toString();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        @XmlJavaTypeAdapter(LocaleDateAdapter.class)
        private LocalDate beginDate;
        @XmlJavaTypeAdapter(LocaleDateAdapter.class)
        private LocalDate endDate;
        private String title;
        private String description;

        public Position() {
        }

        public Position(int beginYear, Month beginMonth, String title, String description) {
            this(of(beginYear, beginMonth), NOW, title, description);
        }

        public Position(int beginYear, Month beginMonth, int endYear, Month endMonth, String title, String description) {
            this(of(beginYear, beginMonth), of(endYear, endMonth), title, description);
        }

        public Position(LocalDate beginDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(beginDate, "beginDate must not be null");
            Objects.requireNonNull(endDate, "endDate must not be null!");
            Objects.requireNonNull(title, "Position must not be null!");
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return Objects.equals(beginDate, position.beginDate) &&
                    Objects.equals(endDate, position.endDate) &&
                    Objects.equals(title, position.title) &&
                    Objects.equals(description, position.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(beginDate, endDate, title, description);
        }

        @Override
        public String toString() {
            return "с " + beginDate +
                    " по " + endDate + '\n' +
                    title + '\n' +
                    description;
        }
    }
}
