<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Create a group</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        	<h1>Create a Group</h1>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/groupCreation"/>" enctype="multipart/form-data">
                <fieldset>
                    <legend>Group Information</legend>
                    <label for="nameGroup">Name </label>
					<input type="text" id="nameGroup" name="nameGroup" value="<c:out value="${group.name}"/>" size="30" maxlength="30" />
					<span class="error">${form.errors['nameGroup']}</span>
					<br />

					<label for="descriptionGroup">Description</label>
					<TEXTAREA name="descriptionGroup" rows="5" cols="22" ><c:out value="${group.description}"/></TEXTAREA>
					<span class="error">${form.errors['descriptionGroup']}</span>
					<br />
					<label for="privileges">Please assign privileges<br /> to this group</label>
					<c:choose>
            			<c:when test="${ empty requestScope.privs }">
                		<p class="errorr">No privileges in database</p>
            			</c:when>
            			<c:otherwise>
		                    <select name="privileges" id = "privileges" multiple="multiple" >																				
								<%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
		                		<c:forEach items="${ requestScope.privs }" var="mapPrivs" varStatus="boucle">
		                    		<option value='<c:out value="${mapPrivs.value.privName}"/>'><c:out value="${mapPrivs.value.privName}"/></option>                    
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