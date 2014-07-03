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
            <dl>
            <dt class="important">Users :</dt> 
            <dd>
            <ul>
            	<c:forEach items="${ course.usernames }" var="users" >
            		<li><c:out value="${ users }"/></li>
            		<br/>
            	</c:forEach>
            </ul>
            </dd>
            </dl>   
            <br>
            <a href="<c:url value="/modifyCourse"><c:param name="courseName" value="${course.courseName }" /></c:url>" class = "button">           
            <input type="button" value="Modify" />
          	</a> 
        </div>
    </body>
</html>