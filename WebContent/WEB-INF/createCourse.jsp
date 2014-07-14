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

					<label for="courseDescription">Description</label>
					<TEXTAREA name="courseDescription" id="courseDescription" rows="5" cols="22" ><c:out value="${course.courseDescription}"/></TEXTAREA>
					<span class="error">${form.errors['courseDescription']}</span>
					<br />
					
					<label for="courseYear">Course Year</label>
					<input type="number" name="courseYear" id="courseYear"  value="<c:out value="${course.courseYear}"/>" required/>
					<span class="error">${form.errors['courseYear']}</span>
					<br />
					
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