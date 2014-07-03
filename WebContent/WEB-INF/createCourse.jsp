<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Create a course</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        	<h1>Create a Course</h1>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/courseCreation"/>" >
                <fieldset>
                    <legend>Course Information</legend>
                    <label for="courseName">Name </label>
					<input type="text" id="courseName" name="courseName" value="<c:out value="${course.courseName}"/>" size="30" maxlength="30" />
					<span class="error">${form.errors['courseName']}</span>
					<br />

					<label for="courseDescription">Description</label>
					<TEXTAREA name="courseDescription" id="courseDescription" rows="5" cols="22" ><c:out value="${course.courseDescription}"/></TEXTAREA>
					<span class="error">${form.errors['courseDescription']}</span>
					<br />
					
					<label for="courseYear">Course Year</label>
					<input type="number" name="courseYear" id="courseYear"  value="<c:out value="${course.courseYear}"/>"/>
					<span class="error">${form.errors['courseYear']}</span>
					<br />
					
				</fieldset>
                 
                <p class="info">${ form.result }</p>
                <input type="submit" value="Create"  />
                <input type="reset" value="Reset all" /> <br />
            </form>
        </div>
    </body>
</html>