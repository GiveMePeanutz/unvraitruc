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
            <%-- Si aucun client n'existe en session, affichage d'un message par défaut. --%>
            <c:when test="${ empty requestScope.users }">
                <p class="erreur">No student in database</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <CENTER>          
            <table >
                <tr>
                	<th>Username</th>
                    <th>Lastname</th>
                    <th>Firstname</th>
                    <th>Groups(s)</th>
                    <th class="action">More</th>
                    <c:set var="contains" value="false" />
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Student'}">
                    <th class="action">Delete</th>
                    </c:if>
					</c:forEach>                   
                </tr>
                <%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
                <c:forEach items="${ requestScope.users }" var="mapUsers" varStatus="boucle">
                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapUsers.value.username }"/></td>
                    <td><c:out value="${ mapUsers.value.lastName }"/></td>
                    <td><c:out value="${ mapUsers.value.firstName }"/></td>
                    <td>
	                    <c:forEach items="${mapUsers.value.groupNames}" var="item">
						    ${item} / 
						</c:forEach>
					</td>
					<td class="action">
                        <a href="<c:url value="/profile"><c:param name="username" value="${ mapUsers.value.username }" /></c:url>">
                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
                        </a>
                    </td>
                    <c:set var="contains" value="false" />
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Student'}">
                    <td class="action">
                        <a href="<c:url value="/deleteUser"><c:param name="username" value="${ mapUsers.value.username }" /></c:url>">
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