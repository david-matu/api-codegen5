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
import com.dave.codepower.codegen.db.DBUpdate;
import com.dave.codepower.codegen.models.Model;
import com.dave.codepower.codegen.models.Project;
import com.dave.codepower.codegen.util.CodeUtil;

@WebServlet(
		asyncSupported = true, 
		description = "Create a Project", 
		urlPatterns = { "/edit/model/*"})
public class SvEditModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvEditModel() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		Project p = (Project) request.getSession().getAttribute("currentProject");
		
		int modelID = Integer.parseInt(CodeUtil.getResourceID(request));
		
		Connection con = DBConnection.createConnection();
		
		Model m = null;
		
		try {
			m = DBRead.getModel(con, modelID);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection(con);
		}
				
		request.setAttribute("model", m);
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/EDIT_MODEL.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Project p = (Project) request.getSession().getAttribute("currentProject");
		int modelID = Integer.parseInt(CodeUtil.getResourceID(request));//Integer.parseInt(request.getParameter("model-id"));
		
		System.out.println("Model ID: " + modelID + "\nParam: " + Integer.parseInt(request.getParameter("model-id")));
		
		String entity = request.getParameter("entity-name");
		String table = request.getParameter("pair-table");
		
		
		Connection con = DBConnection.createConnection();
		
		Model m = new Model();
		
		//REad the model using the ID
		try {
			m = DBRead.getModel(con, modelID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/**
		 * 
		 */
		m.setModelID(modelID);
		m.setProjectID(p.getProjectID());
		m.setEntityName(entity);
		m.setTable(table);
		
		
		String error = "";
		try {
			DBUpdate.updateModel(con, m);
		} catch (SQLException e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
		} finally {
			DBConnection.closeConnection(con);
		}		
		
		if(error.equals("")) {
			request.setAttribute("error", error);
			response.sendRedirect(this.getServletContext().getContextPath() + "/create-project-model-attributes/" + modelID);
		} else {
			request.setAttribute("error", error);
			
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/ERROR.jsp");
			dispatcher.forward(request, response);
		}
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());