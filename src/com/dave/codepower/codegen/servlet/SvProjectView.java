package com.dave.codepower.codegen.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import com.dave.codepower.codegen.db.DBRead;
import com.dave.codepower.codegen.models.Model;
import com.dave.codepower.codegen.models.Project;
import com.dave.codepower.codegen.util.CodeUtil;

@WebServlet(
		asyncSupported = true, 
		description = "Serves Projects", 
		urlPatterns = { "/projects/*"})
public class SvProjectView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvProjectView() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		int projectID = Integer.parseInt(CodeUtil.getResourceID(request));
		
		Project project = null;
		List<Model> modelList = null;
		
		Connection con = DBConnection.createConnection();
		String error = "";
		try {
			project = DBRead.getProject(con, projectID);
			modelList = DBRead.getModelList(con, projectID);
		} catch (SQLException e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		} finally {
			DBConnection.closeConnection(con);
		}
		
		
		request.setAttribute("error", error);
		request.setAttribute("modelList", modelList);
		
		session.setAttribute("currentProject", project);
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/PROJECT_VIEW.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());