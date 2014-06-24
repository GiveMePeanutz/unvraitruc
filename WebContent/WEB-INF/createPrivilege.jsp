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
        <div>
            <form method="post" action="<c:url value="/createPrivilege"/>" enctype="multipart/form-data">
                <fieldset>
                    <legend>Privilege Information</legend>
                    <label for="namePrivilege">Name <span class="requis">*</span></label>
					<input type="text" id="namePrivilege" name="namePrivilege" value="<c:out value="${privilege.name}"/>" size="30" maxlength="30" />
					<span class="error">${form.errors['namePrivilege']}</span>
					<br />

					<label for="descriptionPrivilege">Description</label>
					<input type="text" id="descriptionPrivilege" name="descriptionPrivilege" value="<c:out value="${privilege.description}"/>" size="30" maxlength="30" />
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