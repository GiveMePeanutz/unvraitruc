<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <c:set var="student" value="Student"/>
        <c:set var="teacher" value="Teacher"/>
        <c:choose>
        	<c:when test="${ empty requestScope.user }">
        		<c:choose>
        		<c:when test="${ empty requestScope.grp }"> 
        			<title>Create a user</title>
        		</c:when>
        		<c:when test="${requestScope.grp eq student }"> 
        			<title>Create a student</title>
        		</c:when>
        		<c:when test="${ requestScope.grp eq teacher }"> 
        			<title>Create a teacher</title>
        		</c:when>
        		</c:choose>
        	</c:when>
        	<c:otherwise>
        		<title>Modify users</title>
        	</c:otherwise>
        </c:choose>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
        
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <br />
        <c:choose>
        	<c:when test="${ empty requestScope.user }">
        		<c:choose>
        		<c:when test="${ empty requestScope.grp }"> 
        			<h1>Create a user</h1>
        		</c:when>
        		<c:when test="${requestScope.grp eq student }"> 
        			<h1>Create a student</h1>
        		</c:when>
        		<c:when test="${ requestScope.grp eq teacher }"> 
        			<h1>Create a teacher</h1>
        		</c:when>
        		</c:choose>
        	</c:when>
        	<c:otherwise>
        		<h1>Modify ${user.username}</h1>
        	</c:otherwise>
        </c:choose>
       		<h3>Please, fill the following form and validate</h3>
        <div id="normalForm">
            <form method="post" action="<c:url value="/userCreation"/>" enctype="multipart/form-data">
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
	            				<c:choose>
                    				<c:when test="${ empty requestScope.grp }">
					                    <select name="groups" id = "groups" multiple="multiple">																				
											<%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
					                		<c:forEach items="${ requestScope.groups }" var="mapGroups" varStatus="boucle">
					                    		<option value='<c:out value="${mapGroups.value.groupName}"/>'><c:out value="${mapGroups.value.groupName}"/></option>                    
					                		</c:forEach>
					                	</select>
					                	
			                		</c:when>
        							<c:otherwise>
        								<select name="groups" id = "groups" multiple="multiple" disabled="disabled">																				
					                    	<option  value='<c:out value="${requestScope.grp}"/>' selected="selected" ><c:out value="${requestScope.grp}"/></option>                    
					                	</select>
        							</c:otherwise>
        						</c:choose>
        						<span class="error">${form.errors['groups']}</span>
	                		</c:otherwise>
	        			</c:choose>
        			
        			
                </fieldset>  
                <p class="info">${ form.result }</p>
                <c:choose>
        			<c:when test="${ empty requestScope.user }">
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