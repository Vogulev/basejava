package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class Position {
    private LocalDate beginDate;
    private LocalDate endDate;
    private String position;
    private String description;

    public Position(LocalDate beginDate, LocalDate endDate, String position, String description) {
        Objects.requireNonNull(beginDate, "Дата не может быть null");
        Objects.requireNonNull(endDate, "Дата конца не может быть null");
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.position = position;
        this.description = description;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "с " + beginDate +
                " по " + endDate + '\n' +
                position + '\n' +
                description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position1 = (Position) o;
        return beginDate.equals(position1.beginDate) && endDate.equals(position1.endDate) && Objects.equals(position, position1.position) && Objects.equals(description, position1.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginDate, endDate, position, description);
    }
}
