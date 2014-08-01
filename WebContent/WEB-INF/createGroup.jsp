<%--Group creation or modification page--%>
<%--Value of each field is the value registered in the group bean. If it's a creation page nothing appears in the fields,
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
       			<title>Create a group</title>
        	</c:when>
        	<c:otherwise>
        		<title>Modify a group</title>
        	</c:otherwise>
        </c:choose>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        		<h1>Create a group</h1>
        	</c:when>
        	<c:otherwise>
        		<h1>Modify ${group.groupName} Group</h1>
        	</c:otherwise>
        </c:choose>
        	
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/groupCreation"/>" >
                <fieldset>
                    <legend>Group Information</legend>
                    <label for="groupName">Name </label>
					<c:choose>
        				<c:when test="${ !requestScope.modify eq modif }">
							<input type="text" id="groupName" name="groupName" value="<c:out value="${group.groupName}"/>" size="30" maxlength="30" />
						</c:when>
        				<c:otherwise>							
							<input type="text" id="groupName" name="groupName" value="<c:out value="${group.groupName}" />" size="30" maxlength="30" disabled="disabled"/>
							<p class="hidden"><input type="text" id="groupName" name="groupName" value="<c:out value="${group.groupName}"/>" size="30" maxlength="30" /></p>
						
						</c:otherwise>
        			</c:choose>
					<span class="error">${form.errors['groupName']}</span>
					<br />
					<br />

					<label for="groupDescription">Description</label>
					<TEXTAREA name="groupDescription" id="groupDescription" rows="5" cols="22" ><c:out value="${group.groupDescription}"/></TEXTAREA>
					<span class="error">${form.errors['groupDescription']}</span>
					<br />
					<br />
					
					<label for="privs">Please assign privileges<br /> to this group</label>
					<c:choose>
            			<c:when test="${ empty requestScope.privs }">
                		<p class="error">No privileges in database</p>
            			</c:when>
            			<c:otherwise>
		                    <select name="privileges" id = "privileges" multiple="multiple" >																				
								<%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
		                		<c:forEach items="${ requestScope.privs }" var="mapPrivs" varStatus="boucle">
		                    		<option value='<c:out value="${mapPrivs.value.privName}"/>'><c:out value="${mapPrivs.value.privName}"/></option>                    
		                		</c:forEach>
		                	</select>
		                	<span class="error">${form.errors['privileges']}</span>
		                	
                		</c:otherwise>
        			</c:choose>
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