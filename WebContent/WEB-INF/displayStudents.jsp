<%-- This page displays all students created and stored in database --%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Student list</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        
        <div id="normalForm">
        <h1>Students  </h1>
        <br />
        <c:choose>
            <%-- If there is no student in database--%>
            <c:when test="${ empty requestScope.users }">
                <p class="erreur">No student in database</p>
            </c:when>
            <%-- Else, table is displayed --%>
            <c:otherwise>
            <CENTER>          
            <table >
                <tr>
                	<th>Username</th>
                    <th>Lastname</th>
                    <th>Firstname</th>
                    <th>Groups(s)</th>
                    <th class="action">More</th>
					<%--Delete row is displayed if the user has the right privilege --%>
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Student'}">
                    <th class="action">Delete</th>
                    </c:if>
					</c:forEach>                   
                </tr>
                <c:forEach items="${ requestScope.users }" var="mapUsers" varStatus="boucle">
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <td><c:out value="${ mapUsers.value.username }"/></td>
                    <td><c:out value="${ mapUsers.value.lastName }"/></td>
                    <td><c:out value="${ mapUsers.value.firstName }"/></td>
                    <td>
	                    <c:forEach items="${mapUsers.value.groupNames}" var="item">
						    ${item} / 
						</c:forEach>
					</td>
					<td class="action">
                        <a href="<c:url value="/profile"><c:param name="username" value="${ mapUsers.key }" /><c:param name="personnal" value="false" /></c:url>">
                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
                        </a>
                    </td>
					<%--Delete row is displayed if the user has the right privilege --%>
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Student'}">
                    <td class="action">
                        <a href="<c:url value="/deleteUser"><c:param name="username" value="${ mapUsers.key }" /></c:url>">
                            <img src="<c:url value="/inc/supprimer.png"/>" alt="Delete" />
                        </a>
                    </td>
                    </c:if>
					</c:forEach> 
                </tr>
                </c:forEach>
            </table>
            </CENTER> 
            </c:otherwise>
        </c:choose>
        </div>
    </body>
</html>