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
        <h1>Profile of <c:out value="${ user.firstName }"/> <c:out value="${ user.lastName }"/> </h1>
        <br />      
        <div id="normalForm">

            <p class="info">${ form.result }</p>
            <dl><dt class ="important">Username :</dt> <dd><c:out value="${ user.username }"/></dd></dl>  
            <dl><dt class="important">Password :</dt> <dd><c:out value="${ user.password }"/></dd></dl>
            <dl><dt class="important">Register Date :</dt> <dd><c:out value="${ user.regDate }"/></dd></dl>           
                     
            <dl><dt class="important">FirstName :</dt> <dd><c:out value="${ user.firstName }"/></dd></dl>
            <dl><dt class="important">Last Name :</dt> <dd><c:out value="${ user.lastName }"/></dd></dl>
            <c:if test="${user.sex==0}">
            <dl><dt class="important">Sex :</dt> <dd>  <c:out value="Male"/></dd></dl>
            </c:if> 
            <c:if test="${user.sex==1}">
            <dl><dt class="important">Sex :</dt> <dd><c:out value="Female"/></dd></dl>
            </c:if>
            
            <dl><dt class="important">Birth Date :</dt> <dd><c:out value="${ user.birthDate }"/></dd></dl>
            <dl><dt class="important">Address :</dt> <dd><c:out value="${ user.address }"/></dd></dl>
            <dl><dt class="important">Phone Number :</dt><dd><c:out value="${ user.phone }"/></dd></dl>
            <dl><dt class="important">Email :</dt> <dd><c:out value="${ user.email }"/></dd></dl>
            <dl><dt class="important">Promotion :</dt> <dd><c:out value="${ user.promotion }"/></dd></dl>
            <dl>
             <c:if test="${ !empty user.photoURL }">
                            <c:set var="photo"><c:out value="${ user.photoURL }"/></c:set>
                            <dt class="important">Photo :</dt> <dd><a href="<c:url value="/images/${ photo }"/>"><c:out value="${ user.photoURL }"/></a></dd>
                        </c:if>
            </dl>

            <p class="important">Groups :</p> 
            <ul class="prof">
            	<c:forEach items="${ user.groupNames }" var="groups" >
            		<li><c:out value="${ groups }"/></li>
            		<br/>
            	</c:forEach>
            </ul>

            <p class="important">Courses :</p> 
            <ul class="prof">
            	<c:forEach items="${ user.courseNames }" var="courses">
            		<li><c:out value="${ courses }"/></li>
            		<br/>
            	</c:forEach>
            </ul>

            <br>
            <a  href="<c:url value="/userCreation" ><c:param name="username" value="${user.username }" /><c:param name="modify" value="true" /></c:url>" class = "button">           
            <input type="button" value="Modify" />
          	</a>
            <a  href="<c:url value="/availableCourses" ><c:param name="username" value="${user.username }" /></c:url>" class = "button">           
            <input type="button" value="Inscription" />
          	</a>  
        </div>
    </body>
</html>