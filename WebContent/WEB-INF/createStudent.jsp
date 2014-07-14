<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <c:set var="modif" value="true" />
         <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        	<title>Create a Student</title>
        	</c:when>
        	<c:otherwise>
        		<title>Modify student</title>
        	</c:otherwise>
        </c:choose>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
        
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        		<h1>Create a student</h1>
        	</c:when>
        	<c:otherwise>
        		<h1>Modify ${user.username}</h1>
        	</c:otherwise>
        </c:choose>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/studentCreation"/>" enctype="multipart/form-data">
                <fieldset>
                    <legend>User Information</legend>
                    <c:import url="/inc/inc_user_form.jsp" />
                    <br />
                    
                    	<p class="hidden">
		        			<select name="groups" id = "groups"  required>																				
		                    <option  value='Student' selected="selected" >Student</option>                    
			                </select>
				       </p>       							                		
                </fieldset>  
                <p class="info">${ form.result }</p>
                 <c:choose>
        	<c:when test="${ !requestScope.modify eq modif }">
        				<input type="submit" name = "Create" value="Create"  /> 			
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