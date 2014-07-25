<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="dropdownmenu">
	<ul class="step1">
	  	
		<c:forEach var="item" items="${sessionScope.userSessionAccess}">
		<c:if test="${item eq 'User'}">
	  	<li>
	    	User
	    		<ul class="step2">
	    			
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Display Users'}">
	      			<li><a href="<c:url value="/displayUsers"/>">Display Users</a></li>
	      			</c:if>
					</c:forEach>
	      			
	      			
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Add User'}">
	      			<li><a href="<c:url value="/userCreation"/>">Add User</a></li>
	      			</c:if>
					</c:forEach>
	      			
	      			
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Group'}">
	      			<li>
	      				Group
		      				<ul class="step3">
								<c:forEach var="item" items="${sessionScope.userSessionAccess}">
								<c:if test="${item eq 'Display Groups'}">
			      				<li><a href="<c:url value="/displayGroups"/>">Display groups</a></li>
			      				</c:if>
								</c:forEach>
			      				
								<c:forEach var="item" items="${sessionScope.userSessionAccess}">
								<c:if test="${item eq 'Add Group'}">
			      				<li><a href="<c:url value="/groupCreation"/>">Add Group</a></li>
			      				</c:if>
								</c:forEach>
			      				
		      				</ul>
	      			</li>
	      			</c:if>
					</c:forEach>
	      			
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Privilege'}">
	      			<li>
	      				Privilege
	      					<ul class="step3">

								<c:forEach var="item" items="${sessionScope.userSessionAccess}">
								<c:if test="${item eq 'Display Privileges'}">
			      				<li><a href="<c:url value="/displayPrivs"/>">Display Privileges</a></li>
			      				</c:if>
								</c:forEach>
								

								<c:forEach var="item" items="${sessionScope.userSessionAccess}">
								<c:if test="${item eq 'Add Privilege'}">
			      				<li><a href="<c:url value="/privCreation"/>">Add Privilege</a></li>
			      				</c:if>
								</c:forEach>
		      				</ul>
	      			</li>
	      			</c:if>
					</c:forEach>
	      			
	    		</ul>
	  	</li>
		</c:if>
		</c:forEach>
	  	

		<c:forEach var="item" items="${sessionScope.userSessionAccess}">
		<c:if test="${item eq 'Course'}">
	  	<li>
	    	Course
	    		<ul class="step2">
	    		
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Display Courses'}">
	      			<li><a href="<c:url value="/displayCourses"/>">Display Courses</a></li>
	      			</c:if>
					</c:forEach>
					
					
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Add Course'}">
	      			<li><a href="<c:url value="/courseCreation"/>">Add Course</a></li>
	      			</c:if>
					</c:forEach>
	    		</ul>
	  	</li>
	  	</c:if>
		</c:forEach>
	  	
	  	
		<c:forEach var="item" items="${sessionScope.userSessionAccess}">
		<c:if test="${item eq 'Teacher'}">
	  	<li>
	    	Teacher
	    		<ul class="step2">
	    			
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Display Teachers'}">
	      			<li><a href="<c:url value="/displayTeachers"/>">Display Teachers</a></li>
	      			</c:if>
					</c:forEach>
					
					
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Add Teacher'}">
	      				<li><a href="<c:url value="/teacherCreation"/>">Add Teacher</a></li>
					</c:if>
					</c:forEach>
	    		</ul>
	  	</li>
	  	</c:if>
		</c:forEach>
	  	
	  	
		<c:forEach var="item" items="${sessionScope.userSessionAccess}">
		<c:if test="${item eq 'Student'}">
	  	<li>
	    	Student
	    		<ul class="step2">
	    			
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Display Students'}">
	      			<li><a href="<c:url value="/displayStudents"/>">Display Students</a></li>
	      			</c:if>
					</c:forEach>
					
					
					<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Add Student'}">
						<li><a href="<c:url value="/studentCreation"/>">Add Student</a></li>
	      			</c:if>
					</c:forEach>
	    		</ul>
	  	</li>
	  	</c:if>
		</c:forEach>
		
	  	<c:forEach var="item" items="${sessionScope.userSessionAccess}">
		<c:if test="${item eq 'Data Warehouse'}">
		<li><a href="<c:url value="/dataWarehouse"/>">Data Warehouse</a></li>
		</c:if>
		</c:forEach>
	  	
		<c:forEach var="item" items="${sessionScope.userSessionAccess}">
		<c:if test="${item eq 'Data Analysis'}">
		<li>
			Data Analysis
			<ul class="step2">
	    			
				<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'Naive Bayes'}">
	     				<li><a href="<c:url value="/naiveBayes"/>">Naive Bayes</a></li>
	     			</c:if>
				</c:forEach>
				
				
				<c:forEach var="item" items="${sessionScope.userSessionAccess}">
					<c:if test="${item eq 'KMeans'}">
						<li><a href="<c:url value="/kMeans"/>">KMeans</a></li>
	     			</c:if>
				</c:forEach>
	    	</ul>
		</li>
		</c:if>
		</c:forEach>
		
		
		
		
		
		<c:forEach var="item" items="${sessionScope.userSessionAccess}">
		<c:if test="${item eq 'Profile'}">
		<li><a href="<c:url value="/profile"><c:param name="personnal" value="true" /></c:url>">Profile</a></li>	  
		</c:if>
		</c:forEach>
		
		
		<c:if test="${!empty sessionScope.userSession}">
		<li><a href="<c:url value="/logout"/>">Logout</a></li>	  
		</c:if>
		
	
	</ul>
</div>

