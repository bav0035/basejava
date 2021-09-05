package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatedTextSection extends Section {
    List<String> company = new ArrayList<>();
    List<Date> startDate = new ArrayList<>();
    List<Date> endDate = new ArrayList<>();
    List<String> position = new ArrayList<>();
    List<String> description = new ArrayList<>();

    public void add(String company, Date startDate, Date endDate, String position, String description) {
        this.company.add(company);
        this.startDate.add(startDate);
        this.endDate.add(endDate);
        this.position.add(position);
        this.description.add(description);
    }


    @Override
    public void view() {
        for (int i = 0; i < company.size(); i++) {
            System.out.println(company.get(i));
            System.out.println(startDate.get(i) + " - " + (endDate == null ? "now" : endDate.get(i)));
            System.out.println(position.get(i));
            System.out.println(description.get(i));
        }
    }
}
