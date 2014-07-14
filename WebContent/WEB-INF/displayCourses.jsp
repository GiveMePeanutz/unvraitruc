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
            <c:when test="${ empty requestScope.availableCourses }">
                <p class="erreur">No available courses</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <h3>Available Courses  </h3>
            <table >
                <tr>
                	<th>Course name</th>
                    <th>Description</th>
                    <th class="action">More</th>
					<c:set var="contains" value="false" />
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Course'}">
                    	<th class="action">Delete</th>
                    </c:if>
					</c:forEach>                   					
                    <th class="action">Inscription</th>                   

                </tr>
                <%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
                <c:forEach items="${ requestScope.availableCourses }" var="mapCourses" varStatus="boucle">
                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapCourses.value.courseName }"/></td>
                    <td><c:out value="${ mapCourses.value.courseDescription }"/></td>
                    <td class="action">
                        <a href="<c:url value="/profileCourse"><c:param name="courseName" value="${  mapCourses.value.courseName }" /></c:url>">
                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
                        </a>
                    </td>

                    <c:set var="contains" value="false" />
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Course'}">
	                    <td class="action">
	                        <a href="<c:url value="/deleteCourse"><c:param name="courseName" value="${ mapCourses.value.courseName }" /></c:url>">
	                            <img src="<c:url value="/inc/supprimer.png"/>" alt="Delete" />
	                        </a>
	                    </td>
	                </c:if>
					</c:forEach>
                   
                    <td>
                    <a  href="<c:url value="/inscriptionCourse" ><c:param name="courseName" value="${ mapCourses.value.courseName }" /></c:url>" class = "button">           
            			<img src="<c:url value="/inc/inscription.gif"/>" alt="Inscription" />
          			</a>  
          			</td>

                </tr>
                </c:forEach>
            </table>
            </c:otherwise>
            </c:choose>
            <c:choose>
            <c:when test="${ empty requestScope.userCourses }">
                <p class="erreur">You are not registered to any courses</p>
            </c:when>
            <%-- Sinon, affichage du tableau. --%>
            <c:otherwise>
            <h3>Your Courses  </h3>
            <div class="course">
            <table >
                <tr>
                	<th>Course name</th>
                    <th>Description</th>
                    <th class="action">More</th>
					<c:set var="contains" value="false" />
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Course'}">
                    	<th class="action">Delete</th>
                    </c:if>
					</c:forEach>                   					
                    <th class="action">Cancel</th>                   

                </tr>
                <%-- Parcours de la Map des clients en session, et utilisation de l'objet varStatus. --%>
                <c:forEach items="${ requestScope.userCourses }" var="mapCourses" varStatus="boucle">
                <%-- Simple test de parité sur l'index de parcours, pour alterner la couleur de fond de chaque ligne du tableau. --%>
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <%-- Affichage des propriétés du bean Client, qui est stocké en tant que valeur de l'entrée courante de la map --%>
                    <td><c:out value="${ mapCourses.value.courseName }"/></td>
                    <td><c:out value="${ mapCourses.value.courseDescription }"/></td>
                    <td class="action">
                        <a href="<c:url value="/profileCourse"><c:param name="courseName" value="${  mapCourses.value.courseName }" /></c:url>">
                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
                        </a>
                    </td>

                    <c:set var="contains" value="false" />
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Course'}">
	                    <td class="action">
	                        <a href="<c:url value="/deleteCourse"><c:param name="courseName" value="${ mapCourses.value.courseName }" /></c:url>">
	                            <img src="<c:url value="/inc/supprimer.png"/>" alt="Delete" />
	                        </a>
	                    </td>
	                </c:if>
					</c:forEach>
                   
                    <td>
                    <a  href="<c:url value="/cancelCourse" ><c:param name="courseName" value="${ mapCourses.value.courseName }" /></c:url>" class = "button">           
            			<img src="<c:url value="/inc/supprimer.png"/>" alt="cancel" />
          			</a>  
          			</td>

                </tr>
                </c:forEach>
            </table>
            </div>
            </c:otherwise>
        </c:choose>
        </div>
    </body>
</html>