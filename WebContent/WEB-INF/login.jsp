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
	                <p>Please connect yourself to access the application.</p>
					<c:if test="${empty sessionScope.sessionUser && !empty requestScope.intervalleConnexions}">
	                	<p class="info">(Your last connection was ${requestScope.intervalleConnexions} ago)</p>
	                </c:if>
	                <label for="name">Username<span class="required">*</span></label>
	                <input type="text" id="username" name="username" value="<c:out value="${User.username}"/>" size="20" maxlength="60" />
	                <span class="error">${form.errors['username']}</span>
	                <br />
	
	                <label for="password">Password <span class="required">*</span></label>
	                <input type="password" id="password" name="password" value="" size="20" maxlength="20" />
	                <span class="error">${form.errors['password']}</span>
	                <br />
	 				<br />
	                <label for="remember">Remember Me ?</label>
	                <input type="checkbox" id="remember" name="remember" />
	                <br />
	                
	                <input type="submit" value="Connection" class="connectButton" />
	                <br />
	                
	                <p class="${empty form.errors ? 'success' : 'error'}">${form.result}</p>
	           		<%-- Vérification de la présence d'un objet utilisateur en session --%>
	                <c:if test="${!empty sessionScope.sessionUtilisateur}">
	                    <%-- Si l'utilisateur existe en session, alors on affiche son adresse email. --%>
	                    <p class="succes">You are connected with the username : ${sessionScope.sessionUser.username}</p>
	                </c:if>
	            </fieldset>
	        </form>
	        
        </div>
        <img src="inc/stikom.gif" alt="StikomPicture" class="stikom" /> 
    	
    </body>
</html>