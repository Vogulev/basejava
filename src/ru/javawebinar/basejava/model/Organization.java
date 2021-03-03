package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Organization extends AbstractSection {
    private final List<Experience> experience;

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

    public static class Experience {
        private final Link companyName;
        private final LocalDate beginDate;
        private final LocalDate endDate;
        private final String position;
        private final String description;

        public Experience(String companyName, String url, LocalDate beginDate, LocalDate endDate, String position, String description) {
            Objects.requireNonNull(beginDate, "experience must not be null");
            Objects.requireNonNull(position, "experience must not be null");
            this.companyName = new Link(companyName, url);
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.position = position;
            this.description = description;
        }

        @Override
        public String toString() {
            return '\n' +
                    companyName.getUrl() + '\n' +
                    "с " + beginDate +
                    " по " + endDate + '\n' +
                    position + '\n' +
                    description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Experience that = (Experience) o;
            return Objects.equals(companyName, that.companyName) && beginDate.equals(that.beginDate) && Objects.equals(endDate, that.endDate) && position.equals(that.position) && Objects.equals(description, that.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(companyName, beginDate, endDate, position, description);
        }
    }
}
