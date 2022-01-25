package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;
import ru.javawebinar.basejava.storage.SqlStorage;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private final SqlStorage storage = Config.get().getSqlStorage();
    @Override
    public void init() throws ServletException {
        super.init();
//        storage = Config.get().getSqlStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        List<Resume> list = new ArrayList<>();
        String uuid = request.getParameter("uuid");
        PrintWriter writer = response.getWriter();
        writeString(writer, "Request resume with uuid = " + uuid + "<br>");
        if (uuid != null) {
            try {
                Resume r = storage.get(uuid);
                list.add(r);
            } catch (StorageException exc) {
                writeString(writer, "Resume with uuid = " + uuid + " not exist<br>");
                uuid = null;
            }
        }
        if (uuid == null) {
            list = storage.getAllSorted();
        }
        writeString(writer, "<table border = 1>\n" +
                "<tr>\n" +
                "<th>Resume UUID</th>\n" +
                "<th>Full uuid</th>\n" +
                "</tr>\n");
        for (Resume resume : list) {
            showResume(writer, resume);
        }
        writeString(writer, "</table>\n");
        response.getWriter().write(uuid == null ? "Hello!" : "Hello " + uuid + "!");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void writeString(PrintWriter writer, String str) {
        writer.write(str);
    }

    private void showResume(PrintWriter writer, Resume r) throws IOException {
        writeString(writer,"<tr>\n" +
                "<td>" + r.getUuid() + "</td>\n" +
                "<td>" + r.getFullName() + "</td>\n" +
                "</tr>\n");
    }
}
