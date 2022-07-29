package com.dave.codepower.codegen.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dave.codepower.codegen.db.DBConnection;
import com.dave.codepower.codegen.db.DBCreate;
import com.dave.codepower.codegen.db.DBDelete;
import com.dave.codepower.codegen.db.DBRead;
import com.dave.codepower.codegen.models.Model;
import com.dave.codepower.codegen.models.Project;

@WebServlet(
		asyncSupported = true, 
		description = "Delete an Item: Should be used with caution. With this application in production facing the public, "
				+ "this servlet should be served under filters to control access", 
		urlPatterns = { "/delete"})
public class SvDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvDelete() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		request.setAttribute("error", "");
//		
//		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/PROJECT_CREATE_MODEL.jsp");
//		dispatcher.forward(request, response);
		response.sendRedirect(this.getServletContext().getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long entityID = Long.parseLong(request.getParameter("eid"));
		String targetTable = request.getParameter("tab");
		String primaryKey = request.getParameter("key");
		
		Connection con = DBConnection.createConnection();
		
		boolean isDeleted = false;
		
		try {
			isDeleted = DBDelete.deleteResource(con, entityID, targetTable, primaryKey);
			isDeleted = true;
		} catch (SQLException e) {
			e.printStackTrace();
			isDeleted = false;
		}
		
		/*
		 * Assuming that this request came in form of AJAX
		 * We return text/plain response
		 */
		response.setContentType("text/plain");
		if(isDeleted) {	//true
			response.getWriter().write("DELETE_OKAY");
		} else {
			response.getWriter().write("DELETE_FAILED");
		}
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());