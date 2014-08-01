<%--User creation or modification page--%>


<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <c:set var="modif" value="true" />
        
        <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        	<title>Create a user</title>
        	</c:when>
        	<c:otherwise>
        		<title>Modify user</title>
        	</c:otherwise>
        </c:choose>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
        
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        		<h1>Create a user</h1>
        	</c:when>
        	<c:otherwise>
        		<h1>Modify ${user.username}</h1>
        	</c:otherwise>
        </c:choose>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/userCreation"/>" enctype="multipart/form-data">
                <fieldset>
                    <legend>User Information</legend>
                    <%--Here is the import of the user form --%>
                    <c:import url="user_form.jsp" />
                    <br />
                    
                    	<label for="groups">Please assign groups<br /> to the user</label>                                      
	                    <c:choose>
	            			<c:when test="${ empty requestScope.groups }">
	                		<p class="error">No groups in database</p>
	            			</c:when>
	            			<c:otherwise>
			                    <select name="groups" id = "groups" multiple="multiple" size=10 required="required">																				
									<%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
			                		<c:forEach items="${ requestScope.groups }" var="mapGroups" varStatus="boucle">
			                    		<option value='<c:out value="${mapGroups.value.groupName}"/>'><c:out value="${mapGroups.value.groupName}"/></option>                    
			                		</c:forEach>
			                	</select>
			                	<span class="error">${form.errors['groups']}</span>
        					</c:otherwise>
	        			</c:choose>
        			
        			
                </fieldset>  
                <p class="info">${ form.result }</p>
                <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        				<input type="submit" name = "Create" value="Create"  /> 			
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