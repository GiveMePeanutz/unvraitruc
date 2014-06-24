<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Create a group</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <div>
            <form method="post" action="<c:url value="/createGroup"/>" enctype="multipart/form-data">
                <fieldset>
                    <legend>Group Information</legend>
                    <label for="nameGroup">Name <span class="requis">*</span></label>
					<input type="text" id="nameGroup" name="nameGroup" value="<c:out value="${group.name}"/>" size="30" maxlength="30" />
					<span class="error">${form.errors['nameGroup']}</span>
					<br />

					<label for="descriptionGroup">Description</label>
					<input type="text" id="descriptionGroup" name="descriptionGroup" value="<c:out value="${group.description}"/>" size="30" maxlength="30" />
					<span class="error">${form.errors['descriptionGroup']}</span>
					<br />
                    
                </fieldset>  
                <p class="info">${ form.result }</p>
                <input type="submit" value="Create"  />
                <input type="reset" value="Reset all" /> <br />
            </form>
        </div>
    </body>
</html>