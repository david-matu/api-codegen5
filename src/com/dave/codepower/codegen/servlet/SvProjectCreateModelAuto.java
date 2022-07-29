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
		urlPatterns = { "/create-project-model/auto/*"})
public class SvProjectCreateModelAuto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvProjectCreateModelAuto() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		Project p = (Project) request.getSession().getAttribute("currentProject");
		/*
		 * CREATE THE MODELS AUTOMATICALLY
		 * 1. First check for Existing models
		 * 2. Then add  
		 */
		
		List<Model> existingModelList = null;
		List<String> tableNames = new ArrayList<>();	
		
		Connection con = DBConnection.createConnection();
		
		try {
			tableNames = CodeUtil.getTableNames(p.getDbName(), p.getDbUser(), p.getDbPassword());
			existingModelList = DBRead.getModelList(con, p.getProjectID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(Model me : existingModelList) {
			tableNames.remove(me.getTable().toLowerCase());
		}
		
		for(String s : tableNames) {
			//con = DBConnection.createConnection();
			Model m = new Model();
			m.setProjectID(p.getProjectID());
			m.setEntityName(CodeUtil.getConventionalName(s));
			m.setTable(s.toUpperCase());
			m.setDateCreated(new Timestamp(System.currentTimeMillis()));
			
			try {
				DBCreate.createModel(con, m);
			} catch (SQLException e) {
				e.printStackTrace();
				//err = e.getLocalizedMessage();
			}
		}
		DBConnection.closeConnection(con);
		
		
		response.sendRedirect(this.getServletContext().getContextPath() + "/projects/" + p.getProjectID());
//		request.setAttribute("models", tableNames);		
//		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/PROJECT_CREATE_MODEL.jsp");
//		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Project p = (Project) request.getSession().getAttribute("currentProject");
		String entity = request.getParameter("entity-name");
		String table = request.getParameter("pair-table");
		
		Model m = new Model();
		m.setProjectID(p.getProjectID());
		m.setEntityName(entity);
		m.setTable(table);
		m.setDateCreated(new Timestamp(System.currentTimeMillis()));
		Connection con = DBConnection.createConnection();
		
		String error = "";
		try {
			DBCreate.createModel(con, m);
		} catch (SQLException e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		} finally {
			DBConnection.closeConnection(con);
		}
		
		
		if(error.equals("")) {
			con = DBConnection.createConnection();
			try {
				m = DBRead.getModel(con, entity);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBConnection.closeConnection(con);
			}			
			
			request.setAttribute("error", error);
			request.getSession().setAttribute("model", m);
//			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/PROJECT_CREATE_MODEL_DATA_TYPES.jsp");
//			dispatcher.forward(request, response);
			response.sendRedirect(this.getServletContext().getContextPath() + "/create-project-model-attributes/" + m.getModelID());
		} else {
			request.setAttribute("error", error);
			
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/ERROR.jsp");
			dispatcher.forward(request, response);
		}
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());