<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="dropdownmenu">
	<ul class="step1">
	  	<li>
	    	User
	    		<ul class="step2">
	      			<li><a href="<c:url value="/displayUsers"/>">Display users</a></li>
	      			<li><a href="<c:url value="/addUser"/>">Add a user</a></li>
	      			<li>
	      				Group
		      				<ul class="step3">
			      				<li><a href="<c:url value="/displayGroups"/>">Display groups</a></li>
			      				<li><a href="<c:url value="/addGroups"/>">Add a group</a></li>
		      				</ul>
	      			</li>
	      			<li>
	      				Privilege
	      					<ul class="step3">
			      				<li><a href="<c:url value="/displayPrivileges"/>">Display privileges</a></li>
			      				<li><a href="<c:url value="/addPrivilege"/>">Add a privilege</a></li>
		      				</ul>
	      			</li>
	    		</ul>
	  	</li>
	  	<li>
	    	Course
	    		<ul class="step2">
	      			<li><a href="<c:url value="/displayCourses"/>">Display courses</a></li>
	      			<li><a href="<c:url value="/addCourse"/>">Add a course</a></li>
	    		</ul>
	  	</li>
	  	<li>
	    	Teacher
	    		<ul class="step2">
	      			<li><a href="<c:url value="/displayUsers"/>">Display teachers</a></li>
	      			<li><a href="<c:url value="/addUser"/>">Add a teacher</a></li>
	    		</ul>
	  	</li>
	  	<li>
	    	Student
	    		<ul class="step2">
	      			<li><a href="<c:url value="/displayUsers"/>">Display students</a></li>
	      			<li><a href="<c:url value="/addUser"/>">Add a student</a></li>
	    		</ul>
	  	</li>
		<li><a href="<c:url value="/warehouseMaintenance"/>">WarehouseMaintenance</a></li>
		<li><a href="<c:url value="/dataAnalysis"/>">Data Analysis</a></li>
		<li><a href="<c:url value="/profile"/>">Profile</a></li>	  
	</ul>
</div>