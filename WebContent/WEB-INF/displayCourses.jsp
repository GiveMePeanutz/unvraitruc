<%-- This page displays all courses created and stored in database --%>

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
        <%--Test if the user is a Student --%>
        <c:set var="stu" value="false"/>
        <c:forEach var="group" items="${sessionScope.userSession.groupNames}">
	           <c:if test="${group eq 'Student'}">
	           		<c:set var="stu" value="true"/>
	           </c:if>
		</c:forEach>
        <div id="normalForm">
        <h1>Courses  </h1>
        <br />
        <c:choose>
            <%-- If there is no course in database--%>
            <c:when test="${ empty requestScope.availableCourses }">
                <p class="erreur">No available courses</p>
            </c:when>
            <%-- Else, table is displayed --%>
            <c:otherwise>
            <h3>Available Courses  </h3>
            <table >
                <tr>
                	<th>Course name</th>
                    <th>Description</th>
                    <th class="action">More</th>
					<%--Delete row is displayed if the user has the right privilege --%>
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Course'}">
                    	<th class="action">Delete</th>
                    </c:if>
	                </c:forEach>
	                <%--If the user is a student : interest probability row is displayed --%>
	                <c:if test="${stu eq 'true'}">
	                	<th class="action">Interest Probability</th>
	                </c:if>
	                <%--Subscription row is displayed if the user has the right privilege --%>
	                <c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Course Subscription'}">                   					
                    	<th class="action">Inscription</th>
                    </c:if>                   
					</c:forEach>
					
                </tr>
                <c:forEach items="${ requestScope.availableCourses }" var="mapCourses" varStatus="boucle">
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <td><c:out value="${ mapCourses.value.courseName }"/></td>
                    <td><c:out value="${ mapCourses.value.courseDescription }"/></td>
                    <td class="action">
                        <a href="<c:url value="/profileCourse"><c:param name="courseName" value="${  mapCourses.value.courseName }" /></c:url>">
                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
                        </a>
                    </td>
                    
					<%--Delete row is displayed if the user has the right privilege --%>
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
						<c:if test="${item eq 'Delete Course'}">
		                    <td class="action">
		                        <a href="<c:url value="/deleteCourse"><c:param name="courseName" value="${ mapCourses.value.courseName }" /></c:url>">
		                            <img src="<c:url value="/inc/supprimer.png"/>" alt="Delete" />
		                        </a>
		                    </td>
		                </c:if>
		            </c:forEach>
		            
		            <%--If the user is a student : naive bayes results are displayed --%>
		            <c:if test="${stu eq 'true'}">
		                <td class = "action">
							<c:forEach items="${ requestScope.mapResult }" var="courses" varStatus="boucle">
								<c:if test="${courses.key == mapCourses.value.courseName }">
									<c:out value="${courses.value}"/> %
								</c:if>
		        			</c:forEach>
						</td>
		            </c:if>
		            
		            <%--Subscription row is displayed if the user has the right privilege --%>
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
		                <c:if test="${item eq 'Course Subscription'}">                   
		                    <td>
		                    <a  href="<c:url value="/inscriptionCourse" ><c:param name="courseName" value="${ mapCourses.value.courseName }" /></c:url>" class = "button">           
		            			<img src="<c:url value="/inc/inscription.gif"/>" alt="Inscription" />
		          			</a>  
		          			</td>
	          			</c:if>
          			</c:forEach>
                </tr>
                </c:forEach>
            </table>
            </c:otherwise>
            </c:choose>
            <%--If the user is not registered to at least one course --%>
            <c:choose>
            <c:when test="${ empty requestScope.userCourses }">
                <br>
                <c:if test="${stu eq 'true'}">
                <p class="erreur">You are not registered to any courses</p>
                </c:if>
            </c:when>
            <%-- else, table of his courses is displayed --%>
            <c:otherwise>
            <h3>Your Courses  </h3>
            <div class="course">
            <table >
                <tr>
                	<th>Course name</th>
                    <th>Description</th>
                    <th class="action">More</th>
                    <%--Delete row is displayed if the user has the right privilege --%>
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Delete Course'}">
                    	<th class="action">Delete</th>
                    </c:if>
					</c:forEach>                   					
                    <th class="action">Cancel</th>                   

                </tr>
                <c:forEach items="${ requestScope.userCourses }" var="mapCourses" varStatus="boucle">
                <%--A test to put a different color every other time  --%>
                <tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
                    <td><c:out value="${ mapCourses.value.courseName }"/></td>
                    <td><c:out value="${ mapCourses.value.courseDescription }"/></td>
                    <td class="action">
                        <a href="<c:url value="/profileCourse"><c:param name="courseName" value="${  mapCourses.value.courseName }" /></c:url>">
                            <img src="<c:url value="/inc/info.gif"/>" alt="info" />
                        </a>
                    </td>
                    
                    <%--Delete row is displayed if the user has the right privilege --%>
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