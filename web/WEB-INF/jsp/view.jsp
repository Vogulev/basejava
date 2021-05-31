<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="ru.javawebinar.basejava.util.HtmlUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Resume ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <p>
    <hr>
    <table cellpadding="2">
        <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.AbstractSection>"/>
        <c:set var="type" value="${sectionEntry.key}"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>

        <tr>
            <td><h3><a name="type.name">${type.title}</a></h3></td>
        </tr>
        <c:choose>
            <c:when test="${type == SectionType.OBJECTIVE || type == SectionType.PERSONAL}">
                <tr>
                    <td>
                        <%=((TextSection) section).getContent()%>
                    </td>
                </tr>
            </c:when>
            <c:when test="${type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS}">
                <tr>
                    <td>
                        <ul>
                            <c:forEach var="achievement" items="<%=((ListSection)section).getContentList()%>">
                                <li>${achievement}</li>
                            </c:forEach>
                        </ul>
                    </td>
                </tr>
            </c:when>
            <c:when test="${type == SectionType.EDUCATION || type == SectionType.EXPERIENCE}">
                <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${empty org.companyName.url}">
                                    <h3>${org.companyName.title}</h3>
                                </c:when>
                                <c:otherwise>
                                    <h3><a href="${org.companyName.url}">${org.companyName.title}</a></h3>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <c:forEach var="position" items="${org.position}">
                        <jsp:useBean id="position" type="ru.javawebinar.basejava.model.Organization.Position"/>
                        <tr>
                            <td>
                                <%=HtmlUtil.formatDates(position)%>
                            </td>
                            <td><b>${position.title}</b><br>${position.description}</td>
                        </tr>
                    </c:forEach>
                </c:forEach>
            </c:when>
        </c:choose>
    </c:forEach>
    </table>
    <button onclick="window.history.back()">OK</button>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
