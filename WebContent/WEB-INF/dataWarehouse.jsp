<%-- On this page, the user will be able to update the datawarehouse and make some analysis --%>

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
        
       <CENTER>
	        <h1>Data Warehouse</h1>
	        <%--This button will feed the Data Warehouse  --%>
	        <a href="<c:url value="/updateDataWarehouse"/>" class="button">
	        	<input type="button" value=" ETL DataWarehouse"/>
	        </a>
	   </CENTER> 
        <br />
        <form method="post" action="<c:url value="/dataWarehouse"/>" >
        <%--There are two tables : one for calculation through Year month and day... --%>
            <table >
            <CAPTION>Display by Year>Month>Day>Hour </CAPTION>
                <tr>
                	<th>Sex</th>
                    <th>Group</th>
                    <th>Year</th>
                    <th>Month</th>
                    <th>Day</th>
                    <th>Hour</th>
                    <th>Activity</th>
                    <th class="action">Count</th>
                </tr>
                <%--Previous calculation are displayed first --%>
                <%--For number attribute, if the value is -1 it means the "All" option --%>
                <c:if test="${ ! empty sessionScope.resultsMonth }">
                	<c:forEach items="${ sessionScope.resultsMonth }" var="listResult" varStatus="boucle">
                		<%--A test to put a different color every other time  --%>
                		<tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
	                		
							<td>
	                    		<c:choose>
		                    		<c:when test="${ listResult.getSex()=='-1' }" >
		                    			<c:out value ="All"/>
		                    		</c:when>
		                    		<c:when test="${ listResult.getSex()=='0' }" >
		                    			<c:out value ="Male"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<c:out value="Female"/>
		                    		</c:otherwise>
	                    		</c:choose>
							</td>
							<td>
	                    		<c:out value="${listResult.getGroup()}"/>
							</td>
							<td>
	                    		<c:out value="${listResult.getYear()}"/>
							</td>
							<td>
	                    		<c:out value="${listResult.getMonth()}"/>
							</td>
							<td>
								<c:choose>
		                    		<c:when test="${ listResult.getDay()=='-1' }" >
		                    			<c:out value ="All"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<c:out value="${listResult.getDay()}"/>
		                    		</c:otherwise>
	                    		</c:choose>
							</td>
							<td>
								<c:choose>
		                    		<c:when test="${ listResult.getHour()=='-1' }" >
		                    			<c:out value ="All"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<c:out value="${listResult.getHour()}"/>
		                    		</c:otherwise>
		                    	</c:choose>
							</td>
							<td>
								<c:choose>
		                    		<c:when test="${ listResult.getActivity()=='-1' }" >
		                    			<c:out value ="All"/>
		                    		</c:when>
		                    		<c:when  test="${ listResult.getActivity()=='0'}" >
		                    			<c:out value="Visited Pages"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<c:out value="Performed Actions"/>
		                    		</c:otherwise>
		                    	</c:choose>
							</td>
							<td>
	                    		<c:out value="${listResult.getCount()}"/>
							</td>
	                    </tr>
                	</c:forEach>
                </c:if>
                <%--Then, the user choose his own parameters for a new calculation --%>
                <tr>
                    <td>
                    	<select name="sexValue" id = "sexValue">
						    <option value="<c:out value="-1"/>">All</option> 
						    <option value="<c:out value="0"/>">Male</option> 
						    <option value="<c:out value="1"/>">Female</option> 
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
	                    <select name="day" id = "day">
	                    	<option value='<c:out value="-1"/>'>All</option> 
						<c:forEach var="day" begin="1" end="31">
						    <option value='<c:out value="${day}"/>'><c:out value="${day}"/></option> 
						</c:forEach>
						</select>
					</td>
					<td>
	                    <select name="hour" id = "hour">
	                    	<option value='<c:out value="-1"/>'>All</option> 
						<c:forEach var="hour" begin="0" end="23">
						    <option value='<c:out value="${hour}"/>'><c:out value="${hour}"/></option> 
						</c:forEach>
						</select>
					</td>
					<td>
	                    <select name="activity" id = "activity">
	                    	<option value='<c:out value="-1"/>'>All</option> 
						    <option value='<c:out value="0"/>'>Visited Pages</option> 
						    <option value='<c:out value="1"/>'>Performed Actions</option> 
						</select>
					</td>
					<td>
						<input type="submit" name="Calculate" value="Display by month">
					</td>
					
                </tr>
            </table>
        </form>
        <br>
        <br>
        <form method="post" action="<c:url value="/dataWarehouse"/>" >
            <%-- and the other for calculation through years, weeks and days of week  --%>
            <table >
            <CAPTION>Display by Year>Week>Day of Week </CAPTION>
            
                <tr>
                	<th>Sex</th>
                    <th>Group</th>
                    <th>Year</th>
                    <th>Week</th>
                    <th>Day of Week</th>
                    <th>Activity</th>
                    <th class="action">Count</th>
                </tr>
                <%--Previous calculation are displayed first --%>
                <%--For number attribute, if the value is -1 it means the "All" option --%>
                <c:if test="${ ! empty sessionScope.resultsWeek }">
                	<c:forEach items="${ sessionScope.resultsWeek }" var="listResult" varStatus="boucle">
               			<%--A test to put a different color every other time  --%>
                		<tr class="${boucle.index % 2 == 0 ? 'pair' : 'impair'}">
	                		
							<td>
	                    		<c:choose>
		                    		<c:when test="${ listResult.getSex()=='-1' }" >
		                    			<c:out value ="All"/>
		                    		</c:when>
		                    		<c:when test="${ listResult.getSex()=='0' }" >
		                    			<c:out value ="Male"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<c:out value="Female"/>
		                    		</c:otherwise>
	                    		</c:choose>
							</td>
							<td>
	                    		<c:out value="${listResult.getGroup()}"/>
							</td>
							<td>
	                    		<c:out value="${listResult.getYear()}"/>
							</td>
							<td>
	                    		<c:choose>
		                    		<c:when test="${ listResult.getWeek()=='-1' }" >
		                    			<c:out value ="All"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<c:out value="${listResult.getWeek()}"/>
		                    		</c:otherwise>
	                    		</c:choose>
							</td>
							<td>
	                    		<c:out value="${listResult.getDayOfWeek()}"/>
							</td>
							<td>
								<c:choose>
		                    		<c:when test="${ listResult.getActivity()=='-1' }" >
		                    			<c:out value ="All"/>
		                    		</c:when>
		                    		<c:when  test="${ listResult.getActivity()=='0'}" >
		                    			<c:out value="Visited Pages"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<c:out value="Performed Actions"/>
		                    		</c:otherwise>
		                    	</c:choose>
							</td>
							<td>
	                    		<c:out value="${listResult.getCount()}"/>
							</td>
	                    </tr>
                	</c:forEach>
                </c:if>
                
                <%--Then, the user choose his own parameters for a new calculation --%>
                <tr>
                    <td>
                    	<select name="sexValue" id = "sexValue">
						    <option value="<c:out value="-1"/>">All</option> 
						    <option value="<c:out value="0"/>">Male</option> 
						    <option value="<c:out value="1"/>">Female</option> 
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
	                    <select name="week" id = "week">
	                    	<option value='<c:out value="-1"/>'>All</option> 
						<c:forEach var="week" begin="1" end="52">
						    <option value='<c:out value="${week}"/>'><c:out value="${week}"/></option> 
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
	                    <select name="activity" id = "activity">
	                    	<option value='<c:out value="-1"/>'>All</option> 
						    <option value='<c:out value="0"/>'>Visited Pages </option> 
						    <option value='<c:out value="1"/>'>Performed Actions</option> 
						</select>
					</td>
					<td>
						<input type="submit" name="Calculate" value="Display by week"  >
					</td>
					
                </tr>
            </table>
        </form>
        <br>
        <%--User can also edit a pie chart, grouping together users according their serx or their group --%>
        <div id="normalForm">
        <form method="post" action="<c:url value="/chart"/>" >
           	<fieldset>
           	<LEGEND>Draw a Chart</LEGEND>
            	Please choose a category :
                   <select name="type" id = "type">																				
                   		<option value='Sex'>Sex</option>
                   		<option value='Group'>Group</option>                     
               	</select>
                <br>
                <%--The Chart could show either number of visited page, of performed action or both --%>
                Please choose a type of action :
                   <select name="action" id = "action">																				
                   		<option value='-1'>All</option>
                   		<option value='0'>Visited Pages </option>                     
                    		<option value='1'>Performed Actions</option>                     
               	</select>
               	<br>
               	<%--The Chart could show result of a precise year, or on all years group together  --%>
               	Please choose a Year : 
               	<select name="year" id = "year">
                    <c:forEach items="${ requestScope.years }" var="year" varStatus="boucle">
					    <option value='<c:out value="${year}"/>'><c:out value="${year}"/></option> 
					</c:forEach>
				</select>
				<br>
      			<input type="submit" name = "Draw" value="Draw a PieChart"  />
      			<input type="submit" name = "Draw" value="Draw a BarChart"  /> 			
       		</fieldset>
       	</form>
        </div>
    </body>
</html>