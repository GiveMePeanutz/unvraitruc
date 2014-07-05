<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
		<c:choose>
        	<c:when test="${ empty requestScope.priv }">
       			<title>Create a privilege</title>
        	</c:when>
        	<c:otherwise>
        		<title>Modify a privilege</title>
        	</c:otherwise>
        </c:choose>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        <c:choose>
        	<c:when test="${ empty requestScope.priv }">
        		<h1>Create a privilege</h1>
        	</c:when>
        	<c:otherwise>
        		<h1>Modify ${priv.privName} Privilege</h1>
        	</c:otherwise>
        </c:choose>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/privCreation"/>" >
                <fieldset>
                    <legend>Privilege Information</legend>
                    <label for="privName">Name</label>
					<input type="text" id="privName" name="privName" value="<c:out value="${priv.privName}"/>" size="30" maxlength="30" />
					<span class="error">${form.errors['privName']}</span>
					<br />

					<label for="privDescription">Description</label>
					<TEXTAREA name="privDescription" rows="5" cols="22" ><c:out value="${priv.privDescription}"/></TEXTAREA>
					<span class="error">${form.errors['privDescription']}</span>
					<br />
                    <label for="menus">Please assign menus<br /> to this group</label>
					<c:choose>
            			<c:when test="${ empty requestScope.menus }">
                		<p class="error">No menus in database</p>
            			</c:when>
            			<c:otherwise>
		                    <select name="menus" id = "menus" multiple="multiple" >																				
								<%-- Parcours de la Map des menus en session, et utilisation de l'objet varStatus. --%>
		                		<c:forEach items="${ requestScope.menus }" var="mapMenus" varStatus="boucle">
		                    		<option value='<c:out value="${mapMenus.key}"/>'><c:out value="${mapMenus.value}"/></option>                    
		                		</c:forEach>
		                	</select>
		                	<span class="error">${form.errors['menus']}</span>
		                	
                		</c:otherwise>
        			</c:choose>
                </fieldset>  
                <p class="info">${ form.result }</p>
                <c:choose>
        			<c:when test="${ empty requestScope.priv }">
        				<input type="submit" value="Create"  />
        			</c:when>
        			<c:otherwise>
        				<input type="submit" value="Modify"  />
        			</c:otherwise>
        		</c:choose>
                <input type="reset" value="Reset all" /> <br />
            </form>
        </div>
    </body>
</html>