<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
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
        <h3>Секции:</h3>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:choose>
                <c:when test="${type == SectionType.OBJECTIVE || type == SectionType.PERSONAL}">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><input type="text" name="${type.name()}" size=107 value="${resume.getSection(type)}"></dd>
                    </dl>
                </c:when>
                <c:when test="${type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS}">
                    <c:set var="listSection" value="${resume.getSection(type)}"/>
                    <jsp:useBean id="listSection"
                                 type="ru.javawebinar.basejava.model.ListSection"/>
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><textarea name="${type.name()}" rows=10 cols=100>
                        <c:forEach var="achievement" items="<%=listSection.getContentList()%>">
                            <jsp:useBean id="achievement"
                                         type="java.lang.String"/>
                            ${achievement}
                        </c:forEach>
                    </textarea>
                        <dd>
                    </dl>
                </c:when>

                <c:when test="${type == SectionType.EXPERIENCE || type == SectionType.EDUCATION}">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><textarea name="${type.name()}" rows=10 cols=100>${resume.getSection(type)}</textarea></dd>
                    </dl>
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
