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
        <h1><c:out value="${ user.firstName }"/> <c:out value="${ user.lastName }"/> Profile</h1>
        <br />      
        <div id="corps">
            <p class="info">${ form.result }</p>
            <p>Username : <c:out value="${ user.username }"/></p>  
            <p>Password : <c:out value="${ user.password }"/></p>
            <p>Register Date : <c:out value="${ user.regDate }"/></p>           
                     
            <p>FirstName : <c:out value="${ user.firstName }"/></p>
            <p>Last Name : <c:out value="${ user.lastName }"/></p>
            <p>Sex : <c:out value="${ user.sex }"/></p>
            <p>Birth Date : <c:out value="${ user.birthDate }"/></p>
            <p>Address : <c:out value="${ user.address }"/></p>
            <p>Phone Number : <c:out value="${ user.phone }"/></p>
            <p>Email : <c:out value="${ user.email }"/></p>
            <p>Promotion : <c:out value="${ user.promotion }"/></p>
            <p>Photo : <c:out value="${ user.photoURL }"/></p>
            
            <p>
            Groups : 
            <br>
            	<c:forEach items="${ user.groupNames }" var="groups" >
            		<c:out value="${ groups }"/>
            		<br/>
            	</c:forEach>
            </p>
            <p>
            Courses : 
            <br />
            	<c:forEach items="${ user.courseNames }" var="courses">
            		<c:out value="${ courses }"/>
            		<br/>
            	</c:forEach>
            </p>
            <br>
            <input type="button" value="Modify"  />
          
        </div>
    </body>
</html>