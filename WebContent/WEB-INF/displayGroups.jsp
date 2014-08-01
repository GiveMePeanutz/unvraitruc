<%-- This page displays all groups created and stored in database --%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Group list</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        
        <div id="normalForm">
        <h1>Groups  </h1>
        <br />
        <c:choose>
            <%-- If there is no group in database--%>
            <c:when test="${ empty requestScope.groups }">
                <p class="erreur">No groups</p>
            </c:when>
            <%-- Else, table is displayed --%>
            <c:otherwise>
            <CENTER>
            <table >
                <tr>
                	<th>Group name</th>
                    <th>Description</th>
                    <th>Privileges</th>
                    <th class="action">More</th>
                    <th class="action">Delete</th>                   
                </tr>
                <c:forEach items="${ requestScope.groups }" var="mapGroups" varStatus="boucle">
                <%--A test to put a different color every other time  --%>
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <td><c:out value="${ mapGroups.value.groupName }"/></td>
                    <td><c:out value="${ mapGroups.value.groupDescription }"/></td>
                    <td>
	                    <c:forEach items="${mapGroups.value.privNames}" var="item">
						    ${item} / 
						</c:forEach>
					</td>
					<td class="action">
                        <a href="<c:url value="/profileGroup"><c:param name="groupName" value="${ mapGroups.value.groupName }" /></c:url>">
                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
                        </a>
                    </td>
                    <td class="action">
                        <a href="<c:url value="/deleteGroup"><c:param name="groupName" value="${ mapGroups.value.groupName }" /></c:url>">
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