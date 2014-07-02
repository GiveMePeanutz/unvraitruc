<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Course list</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        
        <div id="normalForm">
        <h1>Courses  </h1>
        <br />
        <c:choose>
            <%-- Si aucun client n'existe en session, affichage d'un message par défaut. --%>
            <c:when test="${ empty requestScope.courses }">
                <p class="erreur">No courses</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <CENTER>
            <table >
                <tr>
                	<th>Course name</th>
                    <th>Description</th>
                    <th class="action">More</th>
                    <th class="action">Delete</th>                   
                </tr>
                <%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
                <c:forEach items="${ requestScope.courses }" var="mapCourses" varStatus="boucle">
                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapCourses.value.courseName }"/></td>
                    <td><c:out value="${ mapCourses.value.courseDescription }"/></td>
                    <td class="action">
                        <a href="<c:url value="/profile"><c:param name="username" value="${ mapClients.key }" /></c:url>">
                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
                        </a>
                    </td>
                    <td class="action">
                        <a href="<c:url value="/deleteCourse"><c:param name="courseName" value="${ mapCourses.value.courseName }" /></c:url>">
                            <img src="<c:url value="/inc/supprimer.png"/>" alt="Delete" />
                        </a>
                    </td>
                </tr>
                </c:forEach>
            </table>
            </CENTER>
            </c:otherwise>
        </c:choose>
        </div>
    </body>
</html>