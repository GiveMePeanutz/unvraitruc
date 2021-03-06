<%-- This page shows a course profile, i.e the features of the course --%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Profile</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />             
        <h1><c:out value="${ course.courseName }"/> </h1>
        <br />      
        <div id="normalForm">

            <p class="info">${ form.result }</p>
            <dl><dt class ="important">Name :</dt> <dd><c:out value="${ course.courseName }"/></dd></dl>  
            <dl><dt class="important">Description :</dt> <dd><c:out value="${ course.courseDescription }"/></dd></dl>
            <dl><dt class="important">Course Year :</dt> <dd><c:out value="${ course.courseYear }"/></dd></dl>           
       		<dl><dt class="important">Teacher :</dt> <dd><c:out value="${ course.teacher }"/></dd></dl>           
       		
            <p class="important">Students :</p> 
            <ul class="prof">
            	<c:forEach items="${ course.usernames }" var="users" >
            		<li><c:out value="${ users }"/></li>
            		<br/>
            	</c:forEach>
            </ul>
            <br>
            <%--If the connected user could modify this course, modify button is displayed --%>
            <c:forEach var="item" items="${sessionScope.userSessionAccess}">
				<c:if test="${item eq 'Modify Course'}">
		            <a href="<c:url value="/courseCreation"><c:param name="courseName" value="${course.courseName }" /><c:param name="modify" value="true" /></c:url>" class = "button">           
		            	<input type="button" value="Modify" />
		          	</a> 
		        </c:if>
		    </c:forEach>
        </div>
    </body>
</html>