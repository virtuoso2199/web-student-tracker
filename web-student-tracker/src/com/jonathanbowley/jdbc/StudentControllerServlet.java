package com.jonathanbowley.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentDBUtil studentDBUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {

		super.init();
		try {
			studentDBUtil = new StudentDBUtil(dataSource);
			
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//list the students
		try {
			//read "command" parameter & route to the appropriate code
			
			String theCommand = request.getParameter("command");
			if(theCommand==null) { //if nothing provide, default to list
				theCommand="LIST";
			}
			
			switch(theCommand) {
				case "LIST":
					listStudents(request,response);
					break;
				case "ADD":
					addStudent(request,response);
					break;
				case "LOAD":
					loadStudent(request,response);
					break;
				case "UPDATE":
					updateStudent(request,response);
					break;
				case "DELETE":
					deleteStudent(request,response);
					break;
				default:
					listStudents(request,response);
					break;
			}
			
			
		} catch(Exception ex) {
			throw new ServletException(ex); //pass back to browser
		}
		
		
	}

	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read student id from form data
		String studentID = request.getParameter("studentId");
		
		//delete student from database
		studentDBUtil.deleteStudent(studentID);
		
		//send user back to list of students
		listStudents(request,response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read student info from form data
		int studentID = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create a new student object
		Student theStudent = new Student(studentID,firstName,lastName,email);
		
		//perform update on database
		studentDBUtil.updateStudent(theStudent);
		
		//send user back to student list
		listStudents(request,response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read student id from form data
		String theStudentID = request.getParameter("studentId");
		
		//get student from database (dbutil)
		Student theStudent = studentDBUtil.getStudent(theStudentID);
		
		//place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//sent to update-student-form.jsp (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// read student info from form data
		String firstName= request.getParameter("firstName");
		String lastName= request.getParameter("lastName");
		String email= request.getParameter("email");

		//create a new student object
		Student theStudent = new Student(firstName,lastName,email);
		
		//add the student to the database
		studentDBUtil.addStudent(theStudent);
		
		//send back to main page (i.e. show student list)
		listStudents(request,response);
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//get students from db
		List<Student> students = studentDBUtil.getStudents();
		
		//add students to the request
		request.setAttribute("student_list", students);
		
		//send to the JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
	}

}
