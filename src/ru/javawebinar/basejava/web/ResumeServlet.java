package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;
import ru.javawebinar.basejava.util.HtmlUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {

    private void refillDB() {
        final String UUID_1 = UUID.randomUUID().toString();
        final String UUID_2 = UUID.randomUUID().toString();
        final String UUID_3 = UUID.randomUUID().toString();
        final String UUID_4 = UUID.randomUUID().toString();

        Resume RESUME_1 = ResumeTestData.getResume(UUID_1, "Name1");
        Resume RESUME_2 = ResumeTestData.getResume(UUID_2, "Name2");
        Resume RESUME_3 = ResumeTestData.getResume(UUID_3, "Name3");
        Resume RESUME_4 = ResumeTestData.getResume(UUID_4, "Name4");
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_3);
        storage.save(RESUME_2);
    }

    private final static SqlStorage storage = Config.get().getSqlStorage();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("list", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r = null;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "create":
                r = Resume.EMPTY;
                break;
            case "view":
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
                    OrganizationSection section = (OrganizationSection) r.getSection(type);
                    List<Organization> emptyFirstOrganization = new ArrayList<>();
                    emptyFirstOrganization.add(Organization.EMPTY);
                    if (section != null) {
                        for (Organization org : section.getOrganizations()) {
                            List<Organization.Position> emptyFirstPosition = new ArrayList<>();
                            emptyFirstPosition.add(Organization.Position.EMPTY);
                            emptyFirstPosition.addAll(org.getPositions());
                            emptyFirstOrganization.add(new Organization(org.getCompany().getName(), org.getCompany().getUrl(), emptyFirstPosition));
                        }
                    }
                    r.addSection(type, new OrganizationSection(emptyFirstOrganization));
                }
                break;
            case "refill":
                refillDB();
                response.sendRedirect("resume");
                return;
            default:
                throw new IllegalArgumentException("Action " + action + " is not recognized");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                "view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        boolean isNew = false;
        Resume r;
        try {
            r = storage.get(uuid);
        } catch (NotExistStorageException exc) {
            r = new Resume(uuid, fullName);
            isNew = true;
        }

        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (HtmlUtil.isEmpty(value)) {
                r.getContacts().remove(type);
            } else {
                r.addContact(type, value);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (HtmlUtil.isEmpty(value) && values.length < 2) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> list = HtmlUtil.stringToList(value);
                        r.addSection(type, new ListSection(list));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!HtmlUtil.isEmpty(name)) {
                                List<Organization.Position> positions = new ArrayList<>();
                                String namedIndex = type.name() + i;
                                String[] startDates = request.getParameterValues(namedIndex + "startDate");
                                String[] endDates = request.getParameterValues(namedIndex + "endDate");
                                String[] titles = request.getParameterValues(namedIndex + "title");
                                String[] descriptions = request.getParameterValues(namedIndex + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        positions.add(new Organization.Position(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                    }
                                }
                                orgs.add(new Organization(name, urls[i], positions));
                            }
                        }
                        r.addSection(type, new OrganizationSection(orgs));
                        break;
                }
            }

        }
        if (isNew) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }
}
