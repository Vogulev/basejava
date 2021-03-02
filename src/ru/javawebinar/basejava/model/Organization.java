package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.List;

public class Organization extends AbstractSection {
    private final List<Experience> experience;

    public Organization(List<Experience> experience) {
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

    public static class Experience {
        private final String companyName;
        private final LocalDate beginDate;
        private final LocalDate endDate;
        private final String position;
        private final String description;

        public Experience(String companyName, LocalDate beginDate, LocalDate endDate, String position, String description) {
            this.companyName = companyName;
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.position = position;
            this.description = description;
        }

        @Override
        public String toString() {
            return '\n' +
                    companyName + '\n' +
                    "с " + beginDate +
                    " по " + endDate + '\n' +
                    position + '\n' +
                    description;
        }
    }
}
