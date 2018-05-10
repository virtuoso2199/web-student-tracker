package com.jonathanbowley.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Define datasource/connection pool for resource injection
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//setup PrintWriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
		//get a connection to the database
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
		//create a SQL statement
			myConn = dataSource.getConnection();
			String sql = "SELECT * FROM student";
			myStmt = myConn.createStatement();
			
		//execute SQL query
			
			myRs = myStmt.executeQuery(sql);
				
				
		//process the ResultSet
			while(myRs.next()) {
				String email = myRs.getString("email");
				out.println(email);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}



}
