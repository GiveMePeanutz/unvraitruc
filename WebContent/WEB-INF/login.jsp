<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Login</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <h1>Welcome to the new <br /> Stikom Web Application</h1>
        
        <div id = "connectionForm">
	        <form method="post" action="<c:url value="/login" />">
			    <fieldset>
	                <legend>Login</legend>
					
	                <c:if test="${empty sessionScope.userSession}">
	                	<p>Please connect yourself to access the application.</p>
	                	
		                <label for="name">Username<span class="required">*</span></label>
		                <input type="text" id="username" name="username" value="<c:out value="${User.username}"/>" size="20" maxlength="60" />
		                <span class="error">${form.errors['username']}</span>
		                <br />
		
		                <label for="password">Password <span class="required">*</span></label>
		                <input type="password" id="password" name="password" value="" size="20" maxlength="20" />
		                <span class="error">${form.errors['password']}</span>
		                <br />
		 				<br />

		                <input type="submit" value="Connection" class="connectButton" />
		                <br />
	                </c:if>
	                <p class="${empty form.errors ? 'success' : 'error'}">${form.result}</p>
	           		<%-- Vérification de la présence d'un objet utilisateur en session --%>
	                <c:if test="${!empty sessionScope.userSession}">
	                    <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
	                    <p class="succes">You are connected with the username : ${requestScope.username}</p>
	                </c:if>
	                <c:if test="${!empty sessionScope.userSession}">
        				<a href="<c:url value="/logout"/>"  class="button">           
							<input type="button" value="Logout" />
						</a> 
    				</c:if>
	            </fieldset>
	        </form>	        
        </div>
        <img src="inc/stikom.gif" alt="StikomPicture" class="stikom" />
        <br />
        
    	
    </body>
</html>