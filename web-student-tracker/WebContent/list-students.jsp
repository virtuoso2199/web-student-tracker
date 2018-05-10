<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Student Tracker App</title>

	<link type="text/css" rel="stylesheet" href="css/style.css" />
</head>
<body>



<div id="wrapper">
	<div id="header">
		<h2>JBO University</h2>
	</div>
</div>

<div id="container">
	<div id="content">
		<!-- New Student Button -->
		<input type="button" value="Add Student" onclick="window.location.href='add-student-form.jsp'; return false;" class="add-student-button" />
		<table>
			<tr><th>First Name</th><th>Last Name</th><th>E-mail</th><th>Action</th></tr>
				<c:forEach var="tempStudent" items="${student_list}">
				
					<!-- Setup a link to update each student -->
					<c:url var="updLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD" />
						<c:param name="studentId" value="${tempStudent.id}" />
					</c:url>
					
					<!-- Setup a link to delete each student -->
					<c:url var="delLink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE" />
						<c:param name="studentId" value="${tempStudent.id}" />
					</c:url>
					
					<tr><td>${tempStudent.firstName}</td><td>${tempStudent.lastName}</td><td>${tempStudent.email}</td><td><a href="${updLink}">Update</a> | <a href="${delLink}" onclick="if(!(confirm('Are you sure you want to delete this student?'))) return false" >Delete</a></td></tr>
				</c:forEach>
				
		</table>
	</div>
</div>

</body>
</html>