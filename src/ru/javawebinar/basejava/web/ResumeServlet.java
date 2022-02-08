package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;
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
/*        String UUID_1 = "uuid1";
        String UUID_2 = "uuid2";
        String UUID_3 = "uuid3";
        String UUID_4 = "uuid4";*/

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
                r = new Resume("");
                break;
            case "view":
            case "edit":
                r = storage.get(uuid);
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
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            if (type.equals(SectionType.PERSONAL) || type.equals(SectionType.OBJECTIVE)) {
                String value = request.getParameter(type.name());
                if (value != null && value.trim().length() != 0) {
                    r.addSection(type, new TextSection(value));
                } else {
                    r.getSections().remove(type);
                }
            }

            if (type.equals(SectionType.ACHIEVEMENT) || type.equals(SectionType.QUALIFICATIONS)) {
                String value = request.getParameter(type.name());
                if (value != null && value.trim().length() != 0) {
                    List<String> list = HtmlUtil.stringToList(value);
                    r.addSection(type, new ListSection(list));
                } else {
                    r.getSections().remove(type);
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
