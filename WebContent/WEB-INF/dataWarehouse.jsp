<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8" />
        <title>Data Warehouse</title>
        <link type="text/css" rel="stylesheet" href="<c:url value="/inc/design.css"/>" />
    </head>
    <body>
        <c:import url="/inc/menu.jsp" />
        
        <div id="normalForm">
	        <h1>Data Warehouse</h1>
	        <br />
	        <form method="post" action="<c:url value="/dataWarehouse"/>" enctype="multipart/form-data">
	        
	            <table >
	                <tr>
	                	<th>#</th>
	                	<th>Sex</th>
	                    <th>Group</th>
	                    <th>Year</th>
	                    <th>Month</th>
	                    <th>Week</th>
	                    <th>Day</th>
	                    <th>Day of Week</th>
	                    <th>Hour</th>
	                    <th>Activity</th>
	                    <th class="action">Count</th>
	                    <th class="action">Delete</th>
	                </tr>
	                <c:if test="${ ! empty sessionScope.results }">
	                	<c:forEach items="${ sessionScope.results }" var="mapResult" varStatus="boucle">
	                		<tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
		                		<td>
		                    		<c:out value="${mapResult.key}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getSex()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getGroup()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getYear()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getMonth()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getWeek()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getDay()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getDayOfWeek()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getHour()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getActivity()}"/>
								</td>
								<td>
		                    		<c:out value="${mapResult.value.getCount()}"/>
								</td>
								<td class="action">
			                        <a href="<c:url value="/deleteDataWarehouse"><c:param name="idCount" value="${ mapResult.key }" /></c:url>">
			                            <img src="<c:url value="/inc/supprimer.png"/>" alt="Delete" />
			                        </a>
			                    </td>
		                    </tr>
	                	</c:forEach>
	                </c:if>
	                
	                <tr>
	                    <td>
	                    	<select name="sexValue" id = "sexValue">
							    <option value="<c:out value="0"/>">All</option> 
							    <option value="<c:out value="1"/>">Male</option> 
							    <option value="<c:out value="2"/>">Female</option> 
							</select>
						</td>
						<td>
		                    <select name="group" id = "group">
		                    <c:forEach items="${ requestScope.groups }" var="group" varStatus="boucle">
							    <option value='<c:out value="${group}"/>'><c:out value="${group}"/></option> 
							</c:forEach>
							</select>
						</td>
						<td>
		                    <select name="year" id = "year">
		                    <c:forEach items="${ requestScope.years }" var="year" varStatus="boucle">
							    <option value='<c:out value="${year}"/>'><c:out value="${year}"/></option> 
							</c:forEach>
							</select>
						</td>
						<td>
		                    <select name="month" id = "month">
		                    <c:forEach items="${ requestScope.months }" var="month" varStatus="boucle">
							    <option value='<c:out value="${month}"/>'><c:out value="${month}"/></option> 
							</c:forEach>
							</select>
						</td>
						<td>
		                    <select name="week" id = "week">
		                    <c:forEach items="${ requestScope.weeks }" var="week" varStatus="boucle">
							    <option value='<c:out value="${week}"/>'><c:out value="${week}"/></option> 
							</c:forEach>
							</select>
						</td>
						<td>
		                    <select name="day" id = "day">
		                    <c:forEach items="${ requestScope.days }" var="day" varStatus="boucle">
							    <option value='<c:out value="${day}"/>'><c:out value="${day}"/></option> 
							</c:forEach>
							</select>
						</td>
						<td>
		                    <select name="dayOfWeek" id = "dayOfWeek">
		                    <c:forEach items="${ requestScope.daysOfWeek }" var="dayOfWeek" varStatus="boucle">
							    <option value='<c:out value="${dayOfWeek}"/>'><c:out value="${dayOfWeek}"/></option> 
							</c:forEach>
							</select>
						</td>
						<td>
		                    <select name="hour" id = "hour">
		                    <c:forEach items="${ requestScope.hours }" var="hour" varStatus="boucle">
							    <option value='<c:out value="${hour}"/>'><c:out value="${hour}"/></option> 
							</c:forEach>
							</select>
						</td>
						<td>
		                    <select name="activity" id = "activity">
		                    	<option value='<c:out value="0"/>'>All</option> 
							    <option value='<c:out value="1"/>'>Page Visited</option> 
							    <option value='<c:out value="2"/>'>Action Done</option> 
							</select>
						</td>
						<td>
							<input type="submit" name="Calculate" value="Calculate"  >
						</td>
						<td class="action">
	                        <input type="reset" value="Reset all" />
	                    </td>
	                </tr>
	            </table>
	        </form>
        </div>
        <a href="<c:url value="/updateDataWarehouse"></c:url>">
        <input type="button" value="Update" />
        </a>
    </body>
</html>