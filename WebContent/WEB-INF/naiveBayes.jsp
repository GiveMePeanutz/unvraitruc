<%-- On this page, the user will be able to proceed to naive bayes algorithm --%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Naive Bayes</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        
        <h1>Naive Bayes  </h1>
        <br />
        
        <div id="normalForm">
        	<form method="post" action="<c:url value="/naiveBayes"/>" >
            	<fieldset>
            	<%--User has to choose a student... --%>
            	<label for="privs">Please choose a  student</label>
					<c:choose>
            			<c:when test="${ empty requestScope.students }">
                		<p class="error">No Student in database</p>
            			</c:when>
            			<c:otherwise>
		                    <select name="students" id = "students" required ="required">																				
		                		<c:forEach items="${ requestScope.students }" var="mapStudents" varStatus="boucle">
		                    		<option value='<c:out value="${mapStudents.value.username}"/>'><c:out value="${mapStudents.value.username}"/></option>                    
		                		</c:forEach>
		                	</select>
        					<input type="submit" name = "Submit" value="Submit"  /> 			

            			</c:otherwise>
        			</c:choose>
        		</fieldset>
        	</form>
        	<%-- and if he selects a student and submit, the program will advise a course for the students, thanks to naive bayes algorithm. --%>
        	<c:if test="${! empty requestScope.selectedUser }">
        		<h3 >Naive Bayes algorithms advise <span class="important"><c:out value="${requestScope.result }"/></span> for <c:out value="${requestScope.selectedUser }"/></h3>
        		<br>
        		<%-- Moreover, the algorithm return the student interest probability for each existing course --%>
        		<h3 >Likelihood Comparative</h3>
        	</c:if>
        	<c:if test="${! empty requestScope.mapResult }">
        		<table>
	        		<tr>
		        		<th>
		        			Courses
		        		</th>
		        		<th>
		        			Affinity Probability<br> for <c:out value="${requestScope.selectedUser }"/> 
		        		</th>
	        		</tr>
	        		<c:forEach items="${ requestScope.mapResult }" var="courses" varStatus="boucle">
		        		<tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
		        			<td>
								<c:out value="${courses.key}"/>
							</td>
							<td class="action">
								<c:out value="${courses.value}"/> %
							</td>
		        		</tr>
	        		</c:forEach>
        		</table>
        	</c:if>
        	
        </div>
       
        
        
    </body>
</html>