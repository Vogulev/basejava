<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
<%@ page import="ru.javawebinar.basejava.model.TextSection" %>
<%@ page import="ru.javawebinar.basejava.model.ListSection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<jsp:include page="fragments/header.jsp"/>
<body>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size="50" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
            <h2><a>${type.title}</a></h2>
            <c:choose>
                <c:when test="${type == SectionType.OBJECTIVE || type == SectionType.PERSONAL}">
                    <input type="text" name="${type.name()}" size=107 value="<%=section%>">
                </c:when>
                <c:when test="${type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS}">
                    <textarea name="${type.name()}" rows=10
                              cols=100><%=String.join("\n", ((ListSection) section).getContentList())%></textarea>
                </c:when>
                <c:when test="${type == SectionType.EXPERIENCE || type == SectionType.EDUCATION}">
                    <c:set var="organizationSection" value="${resume.getSection(type)}"/>
                    <jsp:useBean id="organizationSection"
                                 type="ru.javawebinar.basejava.model.OrganizationSection"/>
                    <c:set var="organizationList" value="${organizationSection.organizations}"/>
                    <jsp:useBean id="organizationList"
                                 type="java.util.List"/>
                    <c:forEach var="organization" items="${organizationList}">
                        <jsp:useBean id="organization"
                                     type="ru.javawebinar.basejava.model.Organization"/>
                        <c:set var="positionList" value="${organization.position}"/>
                        <jsp:useBean id="positionList"
                                     type="java.util.List"/>
                        <dl>
                            <dt>Название организации</dt>
                            <dd><input type="text" name="${type.name()}" size=20
                                       value="<%=organization.getCompanyName().getTitle()%>"></dd>
                        </dl>
                        <br/>
                        <c:forEach var="position" items="${positionList}">
                            <jsp:useBean id="position"
                                         type="ru.javawebinar.basejava.model.Organization.Position"/>
                            <dl>
                                <dt>Начальная дата:</dt>
                                <dd><input type="text" name="${type.name()}" size=20
                                           value="<%=position.getBeginDate()%>"></dd>
                            </dl>
                            <dl>
                                <dt>Конечная дата:</dt>
                                <dd><input type="text" name="${type.name()}" size=20 value="<%=position.getEndDate()%>">
                                </dd>
                            </dl>
                            <br/>
                            <dl>
                                <dt>Позиция:</dt>
                                <dd><input type="text" name="${type.name()}" size=20 value="<%=position.getTitle()%>">
                                </dd>
                            </dl>
                            <br/>
                            <dl>
                                <dt>Описание:</dt>
                                <dd><textarea name="${type.name()}" rows=10
                                              cols=100><%=position.getDescription()%></textarea></dd>
                            </dl>
                            <br/>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
