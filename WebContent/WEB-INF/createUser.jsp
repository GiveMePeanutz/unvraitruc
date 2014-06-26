<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Create a user</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
        
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        	<h1>Create a User</h1>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/userCreation"/>" enctype="multipart/form-data">
                <fieldset>
                    <legend>User Information</legend>
                    <c:import url="/inc/inc_user_form.jsp" />
                    <br />
                    <label for="groups">Please assign groups<br /> to the user</label>
                    <c:choose>
            			<c:when test="${ empty requestScope.groups }">
                		<p class="errorr">No groups in database</p>
            			</c:when>
            			<c:otherwise>
		                    <select name="groups" id = "groups" multiple="multiple">																				
								<%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
		                		<c:forEach items="${ requestScope.groups }" var="mapGroups" varStatus="boucle">
		                    		<option value='<c:out value="${mapGroups.value.groupName}"/>'><c:out value="${mapGroups.value.groupName}"/></option>                    
		                		</c:forEach>
		                	</select>
                		</c:otherwise>
        			</c:choose>
                </fieldset>  
                <p class="info">${ form.result }</p>
                <input type="submit" value="Create"  />
                <input type="reset" value="Reset all" /> <br />
            </form>
        </div>
    </body>
</html>