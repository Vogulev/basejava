package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class Organization extends AbstractSection {
    private final List<Experience> experience = new ArrayList<>();

    public Organization(String companyName, String date, String position, String description) {
        experience.add(new Experience(companyName, date, position, description));
    }

    @Override
    public void getContent() {
        for (Experience exp : experience) {
            System.out.println(exp.companyName + "\n" + exp.date + " | " + exp.position + "\n" + exp.description);
        }
    }

    public void addContent(String companyName, String date, String position, String description) {
        experience.add(new Experience(companyName, date, position, description));
    }

    static class Experience {
        private final String companyName;
        private final String date;
        private final String position;
        private final String description;

        public Experience(String companyName, String date, String position, String description) {
            this.companyName = companyName;
            this.date = date;
            this.position = position;
            this.description = description;
        }
    }
}
