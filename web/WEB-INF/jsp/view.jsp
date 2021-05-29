<%@ page import="java.util.List" %>
<%@ page import="ru.javawebinar.basejava.model.*" %>
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
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.AbstractSection>"/>
                <c:set var="type" value="${sectionEntry.key}"/>
                <c:set var="section" value="${sectionEntry.value}"/>
                <jsp:useBean id="section" type="ru.javawebinar.basejava.model.AbstractSection"/>
    <h4>${type.title}</h4>
    <c:choose>
        <c:when test="${type == SectionType.OBJECTIVE || type == SectionType.PERSONAL}">
            <%=((TextSection)section).getContent()%>
        </c:when>
        <c:when test="${type == SectionType.ACHIEVEMENT || type == SectionType.QUALIFICATIONS}">
            <c:forEach var="achievement" items="<%=((ListSection)section).getContentList()%>">
                <c:if test="${!achievement.blank}">
                ➫ ${achievement}<br/>
                </c:if>
            </c:forEach>
        </c:when>
        <c:when test="${type == SectionType.EDUCATION || type == SectionType.EXPERIENCE}">
            <c:set var="organizationSection" value="${sectionEntry.value}"/>
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
                ➫ <%=organization.getCompanyName().getTitle()%><br/>
                <c:if test="${organization.getCompanyName().getUrl() != null}">
                <%=organization.getCompanyName().getUrl()%><br/>
                </c:if>
                <c:forEach var="position" items="${positionList}">
                    <jsp:useBean id="position"
                                 type="ru.javawebinar.basejava.model.Organization.Position"/>
                    <%=position.getBeginDate()%> - <%=position.getEndDate()%><br/>
                    <%=position.getTitle()%><br/>
                    <c:if test="${position.description != null}">
                    <%=position.getDescription()%><br/>
                    </c:if>
                </c:forEach>
            </c:forEach>
        </c:when>
    </c:choose>
    </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
