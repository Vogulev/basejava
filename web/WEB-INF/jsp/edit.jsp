<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.util.DateUtil" %>
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
                    <textarea name="${type.name()}" rows=5
                              cols=75><%=String.join("\n", ((ListSection) section).getContentList())%></textarea>
                </c:when>
                <c:when test="${type == SectionType.EXPERIENCE || type == SectionType.EDUCATION}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>"
                               varStatus="counter">
                        <dl>
                            <dt>Название организации</dt>
                            <dd><input type="text" name="${type}" size=100 value="${org.companyName.title}"></dd>
                        </dl>
                        <dl>
                            <dt>URL:</dt>
                            <dd><input type="text" name="${type}url" size=100 value="${org.companyName.url}"></dd>
                        </dl>
                        <br>
                        <div style="margin-left: 30px">
                            <c:forEach var="position" items="${org.position}">
                                <jsp:useBean id="position"
                                             type="ru.javawebinar.basejava.model.Organization.Position"/>
                                <dl>
                                    <dt>Начальная дата:</dt>
                                    <dd><input type="text" name="${type}${counter.index}startDate" size=10
                                               value="<%=DateUtil.format(position.getBeginDate())%>" placeholder="MM/yyyy">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Конечная дата:</dt>
                                    <dd><input type="text" name="${type}${counter.index}endDate" size=10
                                               value="<%=DateUtil.format(position.getEndDate())%>" placeholder="MM/yyyy">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Позиция:</dt>
                                    <dd><input type="text" name="${type}${counter.index}title" size=75
                                               value="${position.title}">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Описание:</dt>
                                    <dd><textarea name="${type}${counter.index}description" rows=2
                                                  cols=75>${position.description}</textarea></dd>
                                </dl>
                            </c:forEach>
                        </div>
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
