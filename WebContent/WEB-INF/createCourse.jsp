<%--Course creation or modification page--%>
<%--Value of each field is the value registered in the course bean. If it's a creation page nothing appears in the fields,
if it's a modification page or if this page is displayed after an error
all the registered values appear--%>
<%--After each field, field's error appears if there is one --%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <c:set var="modif" value="true" />
        <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
       			<title>Create a course</title>
        	</c:when>
        	<c:otherwise>
        		<title>Modify a course</title>
        	</c:otherwise>
        </c:choose>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
       
        <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        		<h1>Create a course</h1>
        	</c:when>
        	<c:otherwise>
        		<h1>Modify ${course.courseName}</h1>
        	</c:otherwise>
        </c:choose>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/courseCreation"/>" >
                <fieldset>
                    <legend>Course Information</legend>
                    <label for="courseName">Name </label>
                    <c:choose>
        				<c:when test="${ !requestScope.modify eq modif }">
							<input type="text" id="courseName" name="courseName" value="<c:out value="${course.courseName}"/>" size="30" maxlength="30" />
						</c:when>
        				<c:otherwise>							
							<input type="text" id="courseName" name="courseName"  disabled="disabled" value="<c:out value="${course.courseName}"/>" size="30" maxlength="30" />
							<p class="hidden"><input type="text" id="courseName" name="courseName" value="<c:out value="${course.courseName}"/>" size="30" maxlength="30" /></p>
						
						</c:otherwise>
        			</c:choose>
					<span class="error">${form.errors['courseName']}</span>
					<br />
					<br />

					<label for="courseDescription">Description</label>
					<TEXTAREA name="courseDescription" id="courseDescription" rows="5" cols="22" ><c:out value="${course.courseDescription}"/></TEXTAREA>
					<span class="error">${form.errors['courseDescription']}</span>
					<br />
					<br />
					
					<label for="courseYear">Course Year</label>
					<input type="number" name="courseYear" id="courseYear"  value="<c:out value="${course.courseYear}"/>" required/>
					<span class="error">${form.errors['courseYear']}</span>
					<br />
					<br />
					
<%--User has to affect a teacher to a course, but if the user is a teacher, he will be automatically affected to courses he creates. --%>					
					
					<c:set var="bool" value="true" />
					<c:forEach var="item" items="${sessionScope.userSession.groupNames}">
						<c:if test="${item ne 'Teacher'}">
							<c:set var="bool" value="false" />
							<label for="teacher">Teacher :</label>
							<c:choose>
		            			<c:when test="${ empty requestScope.teachers }">
		                		<p class="error">No teachers in database</p>
		            			</c:when>
		            			<c:otherwise>
		            				<select name="teacher" id = "teacher" >																				
				                		<c:forEach items="${ requestScope.teachers }" var="mapTeachers" varStatus="boucle">
				                    		<option value='<c:out value="${mapTeachers.value.username}"/>'><c:out value="${mapTeachers.value.username}"/></option>                    
				                		</c:forEach>
				                	</select>
                				</c:otherwise>
        					</c:choose>
        				</c:if>
        				</c:forEach>
        				
        				<c:if test="${bool eq 'true' }">
        				<p class="hidden">
        					<select name="teacher" id = "teacher" >																				
				                    		<option value='<c:out value="${sessionScope.userSession.username}"/>'><c:out value="${sessionScope.userSession.username}"/></option>                    
				            </select>
				       </p>
				       </c:if>
            		<span class="error">${form.errors['teacher']}</span>
				</fieldset>
                 
                <p class="info">${ form.result }</p>
                <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        				<input type="submit" name="Create" value="Create"  />
        			</c:when>
        			<c:otherwise>
        				<input type="submit" name="Create" value="Modify"  />
        			</c:otherwise>
        		</c:choose>
                <input type="reset" value="Reset all" /> <br />
            </form>
        </div>
    </body>
</html>