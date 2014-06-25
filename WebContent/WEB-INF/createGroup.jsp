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
					<TEXTAREA name="descriptionGroup" rows="5" cols="22" value="<c:out value="${group.description}"/>"  ></TEXTAREA>
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