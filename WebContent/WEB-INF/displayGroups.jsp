<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>User list</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        
        <div id="normalForm">
        <h1>Groups  </h1>
        <br />
        <c:choose>
            <%-- Si aucun client n'existe en session, affichage d'un message par défaut. --%>
            <c:when test="${ empty requestScope.groups }">
                <p class="erreur">No groups</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <table>
                <tr>
                	<th>Group name</th>
                    <th>Description</th>
                    <th>Privileges</th>
                    <th class="action">Delete</th>                   
                </tr>
                <%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
                <c:forEach items="${ requestScope.groups }" var="mapGroups" varStatus="boucle">
                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapGroups.value.groupName }"/></td>
                    <td><c:out value="${ mapGroups.value.groupName }"/></td>
                    <td>
	                    <c:forEach items="${mapGroups.value.privNames}" var="item">
						    ${item} / 
						</c:forEach>
					</td>
                    <td class="action">
                        <a href="<c:url value="/deleteGroup"><c:param name="groupName" value="${ mapGroups.value.groupName }" /></c:url>">
                            <img src="<c:url value="/inc/supprimer.png"/>" alt="Delete" />
                        </a>
                    </td>
                </tr>
                </c:forEach>
            </table>
            </c:otherwise>
        </c:choose>
        </div>
    </body>
</html>