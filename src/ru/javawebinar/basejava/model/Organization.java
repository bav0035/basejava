package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Organization {
    private final Link company;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String title;
    private final String description;

    public Organization(String name, String url, LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(startDate, "startDate must be not null!");
        Objects.requireNonNull(title, "title must be not null!");
        this.company = new Link(name, url);
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public Link getCompany() {
        return company;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String printCompany() {
        return company.toString();
    }

    public String printContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(startDate + " - " + endDate == null ? "now" : endDate); sb.append("\n");
        sb.append(title); sb.append("\n");
        sb.append(description);
        return new String(sb);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(printCompany()); sb.append("\n");
        sb.append(printContent()); sb.append("\n");
        return new String(sb);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!company.equals(that.company)) return false;
        if (!startDate.equals(that.startDate)) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (!title.equals(that.title)) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = company.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
