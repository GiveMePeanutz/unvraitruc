<%-- This page displays all privileges created and stored in database --%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Privilege list</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        
        <div id="normalForm">
        <h1>Privileges  </h1>
        <br />
        <c:choose>
            <%-- If there is no privilege in database--%>
            <c:when test="${ empty requestScope.privs }">
                <p class="erreur">No privileges</p>
            </c:when>
            <%-- Else, table is displayed --%>
            <c:otherwise>
            <CENTER> 
            <table>
                <tr>
                	<th>Privilege name</th>
                    <th>Description</th>
                    <th>Menus</th>
                    <th class="action">Modify</th>
                    <th class="action">Delete</th>                   
                </tr>
                <c:forEach items="${ requestScope.privs }" var="mapPrivs" varStatus="boucle">
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <td><c:out value="${ mapPrivs.value.privName }"/></td>
                    <td><c:out value="${ mapPrivs.value.privDescription }"/></td>
                    <td>
	                    <c:forEach items="${mapPrivs.value.menuNames}" var="item">
						    ${item} / 
						</c:forEach>
					</td>
					<td class="action">
                        <a href="<c:url value="/privCreation"><c:param name="privName" value="${ mapPrivs.value.privName }" /><c:param name="modify" value="true" /></c:url>">
                            <img src="<c:url value="/inc/modify.gif"/>" alt="mod" />
                        </a>
                    </td>
                    <td class="action">
                        <a href="<c:url value="/deletePriv"><c:param name="privName" value="${ mapPrivs.value.privName }" /></c:url>">
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