<%@ page import="ru.javawebinar.basejava.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp" />
<section>
    <table border="1" cellpadding="1" cellspacing="1">
        <tr>
            <th>Имя</th>
            <th>E-mail</th>
        </tr>
        <%
            for (Resume resume : (List<Resume>) request.getAttribute("list")) {
        %>
        <tr>
            <td><a href="resume?uuid=<%=resume.getUuid()%>"><%=resume.getFullName()%></a></td>
            <td><%=resume.getContact(ContactType.EMAIL)%></td>
        </tr>

        <% } %>
    </table>
</section>
<jsp:include page="fragments/footer.jsp" />
</body>
</html>
