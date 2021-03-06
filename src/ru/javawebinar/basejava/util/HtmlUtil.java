package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.model.ListSection;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.Section;
import ru.javawebinar.basejava.model.TextSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlUtil {
    public static String toHtml(Section section) {
        if (section instanceof TextSection) {
            return "<ul><li>" + ((TextSection) section).getText() + "</ul></li>";
        }
        if (section instanceof ListSection) {
            StringBuilder sb = new StringBuilder();
            sb.append("<ul>");
            for (String str : ((ListSection) section).getItems()) {
                sb.append("<li>");
                sb.append(str);
                sb.append("</li>");
            }
            sb.append("</ul>");
            return sb.toString();
        }
        return "in HtmlUtil.toHtml";
    }

    public static String sectionToText(Section section) {
        if (section instanceof TextSection) {
            return ((TextSection) section).getText();
        }
        if (section instanceof ListSection) {
            StringBuilder sb = new StringBuilder();
            for (String str : ((ListSection) section).getItems()) {
                sb.append(str);
                sb.append("\n");
            }
            return sb.toString();
        }
        return "in HtmlUtil.sectionToText";
    }

    public static List<String> stringToList(String value) {
        return Arrays.asList(value.split("\n"));
    }

    public static String formatDates(Organization.Position position) {
        return DateUtil.format(position.getStartDate()) + "&nbsp;-&nbsp;" + DateUtil.format(position.getEndDate());
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
