<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <c:choose>
        	<c:when test="${ empty requestScope.modify }">
        	<title>Create a teacher</title>
        	</c:when>
        	<c:otherwise>
        		<title>Modify teacher</title>
        	</c:otherwise>
        </c:choose>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
        
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
       <c:choose>
        	<c:when test="${ empty requestScope.modify }">
        		<h1>Create a teacher</h1>
        	</c:when>
        	<c:otherwise>
        		<h1>Modify ${user.username}</h1>
        	</c:otherwise>
        </c:choose>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/teacherCreation"/>" enctype="multipart/form-data">
                <fieldset>
                    <legend>User Information</legend>
                    <c:import url="/inc/inc_user_form.jsp" />
                    <br />
                    
                    	<label for="groups">Please assign groups<br /> to the user</label>                                      
	                     <c:choose>
	            			<c:when test="${ empty requestScope.groups }">
	                		<p class="error">No groups in database</p>
	            			</c:when>
            		        <c:otherwise>
            		        	<p class="hidden">
            					<select name="groups" id = "groups"  required>																				
				                    <option  value='Teacher' selected="selected" >Teacher</option>                    
				                </select>
				                	<span class="error">${form.errors['groups']}</span>
				                </p>
				                Teacher <br>(you can't affect users to an other group.)
       						</c:otherwise>
        				</c:choose>
                </fieldset>  
                <p class="info">${ form.result }</p>
                 <c:choose>
        			<c:when test="${ empty requestScope.user }">
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