<%-- On this page, the user will be able to proceed to K-Means algorithm --%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>kMeans</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        <h1>kMeans  </h1>
        <br />
        <div id="normalForm">
        	<form method="post" action="<c:url value="/kMeans"/>" >
            	<%--User has to choose if he wants to sort by group or by user, and the number of cluster he wants. --%>
            	<fieldset>
					Please select the indiviual type : 
					<select name="Individual" id = "Individual" >																				
						<option value='<c:out value="User"/>'><c:out value="User"/></option>  
						<option value='<c:out value="Group"/>'><c:out value="Group"/></option>                   
                	</select>
                	and the number of clusters : 
                	<input type="number" name="Clusters" min="1" max="5" required="required">
                	<br/><br>
                	<input type="submit" name="kMeans" value="Magic button"  />
        		</fieldset>
        	</form>
        </div>
        
        <%--If the algorithm succeed... --%>
        <c:if test="${!empty requestScope.kmeansData}">
	       	<br><br><br><br>
	       	<%-- The program shows the different clusters with their components. --%>
	       	<c:forEach items="${ requestScope.kmeansData }" var="cluster" varStatus="boucle">
	       		<div id="normalForm">
		       		Cluster n° <c:out value="${boucle.index +1}" />
		       		<table >
			       		<tr>
		                	<th>Individual name</th>
		                    <th>Activity count</th>                   
			            </tr>
			       		<c:forEach items="${cluster}" var="clusterData" varStatus="boucle2">
               				 <%--A test to put a different color every other time  --%>
			       			<tr class="${boucle2.index % 2 == 0 ? 'pair' : 'impair'}">
				       			<td><c:out value="${clusterData.username}"/></td>
				       			<td>
				       				<c:forEach items="${clusterData.data}" var="data" varStatus="boucle2">
				       					<c:out value="${data}"/>
				       				</c:forEach>
				       			</td>
			       			</tr>
			       		</c:forEach>
		       		</table>
		       		<br><br>
	       		</div>
	       	</c:forEach>
	       	<br><br>
        </c:if>
    </body>
</html>