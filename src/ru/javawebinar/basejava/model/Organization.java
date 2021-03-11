package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Organization extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private final List<Experience> experience;

    public Organization(Experience... experiences) {
        this(Arrays.asList(experiences));
    }

    public Organization(List<Experience> experience) {
        Objects.requireNonNull(experience, "experience must not be null");
        this.experience = experience;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Experience exp : experience) {
            sb.append(exp.toString());
        }
        return sb.toString() + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return experience.equals(that.experience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(experience);
    }

    public static class Experience implements Serializable {
        private static final long serialVersionUID = 1L;

        private final Link companyName;
        private final List<Position> position;

        public Experience(String title, String url, Position... positions) {
            this(new Link(title, url), Arrays.asList(positions));
        }

        public Experience(Link link, List<Position> position) {
            this.companyName = link;
            this.position = position;
        }

        @Override
        public String toString() {
            return '\n' +
                    companyName.getUrl() + '\n' +
                    position.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Experience that = (Experience) o;
            return Objects.equals(companyName, that.companyName) && Objects.equals(position, that.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(companyName, position);
        }
    }
}
