<%-- This page shows a user profile --%>

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
            <c:set var="contains" value="false" />
            
            <%--Password is displayed only if this profile is the profile of the connected user or if the user could modify the users.(explained in the servlet) --%>
			<c:choose>
			<c:when test='${ user.username eq sessionScope.userSession.username }'>
			<dl><dt class="important">Password :</dt> <dd><c:out value="${ user.password }"/></dd></dl>
			</c:when>
			<c:otherwise>
			<c:forEach var="item" items="${sessionScope.userSessionAccess}">
			<c:if test="${item eq 'Modify User' }">  
            <dl><dt class="important">Password :</dt> <dd><c:out value="${ user.password }"/></dd></dl>
            </c:if>
			</c:forEach>
			</c:otherwise>
			</c:choose>
			
            <dl><dt class="important">Register Date :</dt> <dd><c:out value="${ user.regDate.toString('MM/dd/yyyy') }"/></dd></dl>           
                     
            <dl><dt class="important">FirstName :</dt> <dd><c:out value="${ user.firstName }"/></dd></dl>
            <dl><dt class="important">Last Name :</dt> <dd><c:out value="${ user.lastName }"/></dd></dl>
            <c:if test="${user.sex==0}">
            <dl><dt class="important">Sex :</dt> <dd>  <c:out value="Male"/></dd></dl>
            </c:if> 
            <c:if test="${user.sex==1}">
            <dl><dt class="important">Sex :</dt> <dd><c:out value="Female"/></dd></dl>
            </c:if>
            
            <dl><dt class="important">Birth Date :</dt> <dd><c:out value="${ user.birthDate.toString('MM/dd/yyyy') }"/></dd></dl>
            <dl><dt class="important">Address :</dt> <dd><c:out value="${ user.address }"/></dd></dl>
            <dl><dt class="important">Phone Number :</dt><dd><c:out value="${ user.phone }"/></dd></dl>
            <dl><dt class="important">Email :</dt> <dd><c:out value="${ user.email }"/></dd></dl>
            <c:if test ="${!empty user.className }">
            <dl><dt class="important">Class Name :</dt> <dd><c:out value="${ user.className }"/></dd></dl>
            </c:if>
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
            <%-- If the user is enrolled to at least one course, list of his courses is displayed,
             and he could cancel his enrollment here --%>
            
				<c:if test="${ !empty user.courseNames }">
	            <p class="important">Courses :</p>
	             
	            <table class="profcourses" >
		            <tr>
		            	<th>Course name</th>
		            	<th class="action">More</th>
		            	<c:if test='${ user.username eq sessionScope.userSession.username }'>
		                <th class="action">Cancel</th>	
		                </c:if>			                                     
		            </tr>
		            
					<c:forEach items="${ user.courseNames }" var="course">
               			 <%--A test to put a different color every other time  --%>
			             <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
			                 <td><c:out value="${ course }"/></td>	
			                 <td class="action">
		                        <a href="<c:url value="/profileCourse"><c:param name="courseName" value="${  course }" /></c:url>">
		                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
		                        </a>
	                    	</td>
	                    	<c:if test='${ user.username eq sessionScope.userSession.username }'>	                  
			                <td class="action">
			                    <a href="<c:url value="/cancelCourse"><c:param name="courseName" value="${  course }" /></c:url>">
			                        <img src="<c:url value="/inc/supprimer.png"/>" alt="cancel" />
			                    </a>
			                </td>
			                </c:if>				                    
			             </tr>
		            </c:forEach>
			    </table>
		    </c:if>
		    <br />
		    <br />
		    <%--If the connected user could modify this user, modify button is displayed --%>
			<c:if test="${requestScope.userModifiable}">
            <a  href="<c:url value="/userCreation" ><c:param name="username" value="${user.username }" /><c:param name="modify" value="true" /></c:url>" class = "button">           
            <input type="button" value="Modify" />
          	</a>
          	</c:if>            
        </div>
    </body>
</html>