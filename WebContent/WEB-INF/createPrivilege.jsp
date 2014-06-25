<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Create a privilege</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        	<h1>Create a Privilege</h1>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/privilegeCreation"/>" enctype="multipart/form-data">
                <fieldset>
                    <legend>Privilege Information</legend>
                    <label for="namePrivilege">Name</label>
					<input type="text" id="namePrivilege" name="namePrivilege" value="<c:out value="${privilege.name}"/>" size="30" maxlength="30" />
					<span class="error">${form.errors['namePrivilege']}</span>
					<br />

					<label for="descriptionPrivilege">Description</label>
					<TEXTAREA name="descriptionPrivilege" rows="5" cols="22" value="<c:out value="${privilege.description}"/>"  ></TEXTAREA>
					<span class="error">${form.errors['descriptionPrivilege']}</span>
					<br />
                    
                </fieldset>  
                <p class="info">${ form.result }</p>
                <input type="submit" value="Create"  />
                <input type="reset" value="Reset all" /> <br />
            </form>
        </div>
    </body>
</html>