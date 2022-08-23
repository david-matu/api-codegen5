package com.dave.codepower.codegen.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
		description = "Create a Project", 
		urlPatterns = { "/create-project"})
public class SvProjectCreate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvProjectCreate() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		request.setAttribute("errorMsg", "");
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/PROJECT_CREATE.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String projectName = request.getParameter("project-name");
		String description = request.getParameter("description");
		String dbName = request.getParameter("db-name");
		String dbUser = request.getParameter("db-username");
		String dbPass = request.getParameter("db-password");
		
		Project p = new Project();
		p.setName(projectName);
		p.setDescription(description);
		p.setDbName(dbName);
		p.setDbUser(dbUser);
		p.setDbPassword(dbPass);
		p.setDateCreated(new Timestamp(System.currentTimeMillis()));
		
		Connection con = DBConnection.createConnection();
		
		String error = "";
		try {
			DBCreate.createProject(con, p);
		} catch (SQLException e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		} finally {
			DBConnection.closeConnection(con);
		}
		
		
		if(!error.equals("")) {
			request.setAttribute("error", error);		
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/PROJECT_CREATE.jsp");
			dispatcher.forward(request, response);
		} else {
			HttpSession session = request.getSession();
			con = DBConnection.createConnection();
			
			Project project = new Project();
			try {
				project = DBRead.getProject(con, projectName);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			/*
			 * CREATE THE MODELS AUTOMATICALLY
			 * 
			 */
			List<String> tableNames = new ArrayList<>();
			
			try {
				tableNames = CodeUtil.getTableNames(p.getDbName(), p.getDbUser(), p.getDbPassword());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			con = DBConnection.createConnection();
			for(String s : tableNames) {				
				Model m = new Model();
				m.setProjectID(project.getProjectID());
				m.setEntityName(CodeUtil.getConventionalName(s));
				m.setTable(s.toUpperCase());
				m.setDateCreated(new Timestamp(System.currentTimeMillis()));
				
				//con = DBConnection.createConnection();
				
				try {
					DBCreate.createModel(con, m);
				} catch (SQLException e) {
					e.printStackTrace();
					//err = e.getLocalizedMessage();
				}
			}
			DBConnection.closeConnection(con);
			
			session.setAttribute("currentProject", project);			
			response.sendRedirect(request.getServletContext().getContextPath() + "/create-project-model");
		}
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());