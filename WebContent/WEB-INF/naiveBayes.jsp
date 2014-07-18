<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Student list</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        
        <h1>Naive Bayes  </h1>
        <br />
        
        <div id="normalForm">
        	<form method="post" action="<c:url value="/naiveBayes"/>" >
            	<fieldset>
            	<label for="privs">Please choose a  student</label>
					<c:choose>
            			<c:when test="${ empty requestScope.students }">
                		<p class="error">No Student in database</p>
            			</c:when>
            			<c:otherwise>
		                    <select name="students" id = "students" >																				
								<%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
		                		<c:forEach items="${ requestScope.students }" var="mapStudents" varStatus="boucle">
		                    		<option value='<c:out value="${mapStudents.value.username}"/>'><c:out value="${mapStudents.value.username}"/></option>                    
		                		</c:forEach>
		                	</select>
		                	<span class="error">${form.errors['students']}</span> 
            			</c:otherwise>
        			</c:choose>
        		</fieldset>
        	</form>
        </div>
    </body>
</html>