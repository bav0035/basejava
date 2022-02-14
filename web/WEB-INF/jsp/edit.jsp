<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.util.HtmlUtil" %>
<%@ page import="ru.javawebinar.basejava.model.OrganizationSection" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>


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
        <hr>
        <h3>Контакты:</h3>
        <table border="0">
            <c:forEach var="sType" items="${SectionType.values()}">
                <c:set var="section" value="${resume.getSection(sType)}"/>
                <jsp:useBean id="section" type="ru.javawebinar.basejava.model.Section"/>
                <c:choose>
                    <c:when test="${sType == SectionType.PERSONAL || sType == SectionType.OBJECTIVE}">
                        <tr>
                            <td>
                                <h4>${sType.title}</h4>
                            </td>
                            <td>
                                <input type="text" name="${sType.name()}" size="100"
                                       value="${HtmlUtil.sectionToText(section)}">
                            </td>
                        </tr>
                    </c:when>

                    <c:when test="${sType == SectionType.QUALIFICATIONS || sType == SectionType.ACHIEVEMENT}">
                        <tr>
                            <td>
                                <h4>${sType.title}</h4>
                            </td>
                            <td>
                        <textarea name="${sType.name()}" rows="7" cols="100"
                                  style="resize: none">${HtmlUtil.sectionToText(section)}</textarea>
                            </td>
                        </tr>
                    </c:when>
                    <c:when test="${sType == SectionType.EDUCATION || sType == SectionType.EXPERIENCE}">
                        <tr>
                        <td colspan="2">
                            <h4>${sType.title}</h4>
                        </td>
                        <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>"
                                   varStatus="counter">
                            <tr>
                                <td>
                                    Компания:
                                </td>
                                <td>
                                    <input type="text" name="${sType}" value="${org.company.name}" size="100"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    URL:
                                </td>
                                <td>
                                    <input type="text" name="${sType}url" value="${org.company.url}" size="100"/>
                                </td>
                            </tr>
                            <c:forEach var="position" items="${org.positions}">
                                <jsp:useBean id="position" type="ru.javawebinar.basejava.model.Organization.Position"/>
                                <tr>
                                    <td style="padding-left: 20px">Начальная дата:</td>
                                    <td>
                                        <input type="text" name="${sType}${counter.index}startDate" size=10
                                               value="<%=DateUtil.format(position.getStartDate())%>"
                                               placeholder="MM/yyyy">
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding-left: 20px">Конечная дата:</td>
                                    <td>
                                        <input type="text" name="${sType}${counter.index}endDate" size=10
                                               value="<%=DateUtil.format(position.getEndDate())%>"
                                               placeholder="MM/yyyy">
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding-left: 20px">
                                        Должность:
                                    </td>
                                    <td>
                                        <input type="text" name="${sType}${counter.index}title" value="${position.title}" size="100"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding-left: 20px">
                                        Функции:
                                    </td>
                                    <td>
                                        <textarea name="${sType}${counter.index}description" rows="7" cols="100"
                                                  style="resize: none">
                                            <%=position.getDescription() == null ? "" : position.getDescription()%>
                                        </textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" height="50">
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </c:forEach>
        </table>
        </p>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>