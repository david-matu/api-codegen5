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
		urlPatterns = { "/create-project-model"})
public class SvProjectCreateModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvProjectCreateModel() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		Project p = (Project) request.getSession().getAttribute("currentProject");
		
		List<String> tableNames = new ArrayList<>();
		
		List<Model> modelList = null;		
		Connection con = DBConnection.createConnection();
		
		try {
			tableNames = CodeUtil.getTableNames(p.getDbName(), p.getDbUser(), p.getDbPassword());
			modelList = DBRead.getModelList(con, p.getProjectID());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection(con);
		}
		
		
		List<String> nList = new ArrayList<>();
		List<String> eList = new ArrayList<>();
		
//		/*
//		 * Fetch non-existing 
//		 */		
		for(Model m : modelList) {	//Existing
			eList.add(m.getTable());
		}
//		
//		System.out.println("PRINTING TABLE NAMES");
//		for(String s : tableNames) {
//			System.out.println(s);
//		}
//		
//		System.out.println("");
//		System.out.println("PRINTING EXISTING");
//		for(String s : eList) {
//			System.out.println(s);
//		}
		
		for(String s : tableNames) {
			if(!eList.contains(s.toUpperCase())) {
				//System.out.println("NOT CONTAIN::::::: " + s);
				nList.add(s);
			}
		}
		
		/*1
		for(String e : eList) {
			System.out.println("Existing: " + e);
			nList = new ArrayList<>();
			
			for(String s : tableNames) {
				if(s.equalsIgnoreCase(e)) {
					System.out.println("::::::: " + s);
					nList.add(s);
				}
			}
		}
		*/
		
		request.setAttribute("nList", nList);
		request.setAttribute("modelList", modelList);
		request.setAttribute("models", tableNames);		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/PROJECT_CREATE_MODEL.jsp");
		dispatcher.forward(request, response);
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