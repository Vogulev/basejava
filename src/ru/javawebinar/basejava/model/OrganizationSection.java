package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private List<Organization> organization;

    public OrganizationSection() {
    }

    public OrganizationSection(Organization... organizations) {
        this(Arrays.asList(organizations));
    }

    public OrganizationSection(List<Organization> organization) {
        Objects.requireNonNull(organization, "experience must not be null");
        this.organization = organization;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Organization exp : organization) {
            sb.append(exp.toString());
        }
        return sb.toString() + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationSection that = (OrganizationSection) o;
        return organization.equals(that.organization);
    }

    @Override
    public int hashCode() {
        return organization.hashCode();
    }

}
