package com.jonathanbowley.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDBUtil {

	private DataSource dataSource;
	
	public StudentDBUtil(DataSource theDataSource) {
		this.dataSource=theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		
		List<Student> students = new ArrayList<>();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
		//get a connection
		conn = dataSource.getConnection();
		
		//create a sql statement
		String sql = "SELECT * FROM student ORDER BY last_name";
		stmt = conn.createStatement();
		
		//execute query
		rs = stmt.executeQuery(sql);
		
		//process result set
		
		while(rs.next()) {
			//retrieve data from ResultSet row
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String email = rs.getString("email");
			
			//create a new student object
			Student tempStudent = new Student(id,firstName,lastName,email);
			
			//add to list of students
			students.add(tempStudent);
		}
		
		
			
			return students;
		} finally {
			//close JDBC objects
			if (conn != null) {
				conn.close(); //will close Statement and ResultSet as well
			}
		}
		
		
	}

	public void addStudent(Student theStudent) throws Exception{
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = dataSource.getConnection();
			//create sql for insert
			String sql = "INSERT INTO student (first_name, last_name, email) VALUES (?,?,?)";
			
			//set the param values for the student
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, theStudent.getFirstName());
			stmt.setString(2, theStudent.getLastName());
			stmt.setString(3, theStudent.getEmail());
			
			//execute sql insert
			stmt.execute();
			
		} finally {
			//clean up the JDBC objects
			conn.close();
		}
	}

	public Student getStudent(String theStudentID) throws Exception{
		
		Student theStudent = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int studentID;
		
		try {
			//convert student id to int
			studentID = Integer.parseInt(theStudentID);
			
			//get connection to database
			conn = dataSource.getConnection();
			
			//create sql to get selected student 
			String sql = "SELECT * FROM student WHERE id=?";
			
			//create prepared statement 
			stmt = conn.prepareStatement(sql);
			
			//set params
			stmt.setInt(1, studentID);
			
			//exeute statement
			rs = stmt.executeQuery();
			
			//retrieve data from ResultSet row
			if(rs.next()) {
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				
				//use the studentID during construction
				theStudent = new Student(studentID,firstName,lastName, email);
			} else {
				throw new Exception("Could not find student id: "+studentID);
			}
			
			return theStudent;
			
		} finally {
			
			//clean up JDBC objects
			conn.close();
		}
		
		
	}

	public void updateStudent(Student theStudent) throws Exception{
		
		//setup JDBC objects
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			//get db connection
			conn = dataSource.getConnection();
			
			//create SQL update statement
			String sql = "UPDATE student set first_name=?, last_name=?, email=? WHERE id=?";
			
			//prepare statement
			stmt=conn.prepareStatement(sql);
			
			//set parameters for statement
			stmt.setString(1, theStudent.getFirstName());
			stmt.setString(2, theStudent.getLastName());
			stmt.setString(3, theStudent.getEmail());
			stmt.setInt(4, theStudent.getId());
			
			//execute SQL statement
			stmt.execute();
		
		} finally {
			//close JDBC objects
			conn.close();
		}
		
	}

	public void deleteStudent(String studentID) throws Exception{
		//setup JDBC objects
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			//convert studentID to int
			int id = Integer.parseInt(studentID);
			
			//get connection to the database
			conn = dataSource.getConnection();
			
			//create sql to delete student
			String sql = "DELETE FROM student WHERE id=?";
			
			//prepare statement
			stmt = conn.prepareStatement(sql);
			
			//setup parameters
			stmt.setInt(1, id);
			
			//execute sql statement
			stmt.execute();
			
		} finally {
			//close JDBC objects
			conn.close();
		}
		
	}
	
}
