<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.HtmlUtil" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" class="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <p>
            <c:forEach var="cType" items="${ContactType.values()}">
            <dt>${cType.title}</dt>
        <dd><input type="text" name="${cType.name()}" size=30 value="${resume.getContact(cType)}"></dd>
        <br>
        </c:forEach>
        <c:forEach var="sType" items="${SectionType.values()}">
            <jsp:useBean id="sType" type="ru.javawebinar.basejava.model.SectionType"/>
            <dt>${sType.title}</dt>
            <textarea name="${sType.name()}" rows="7" cols="100"
                      style="resize: none"><%=HtmlUtil.sectionToText(resume.getSection(sType))%></textarea>
            <br>
        </c:forEach>
        </p>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>