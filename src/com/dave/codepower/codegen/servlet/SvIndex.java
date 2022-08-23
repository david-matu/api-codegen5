package com.dave.codepower.codegen.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

@WebServlet(
		asyncSupported = true, 
		description = "Serves Home", 
		urlPatterns = { "/", "/home", "/index"})
public class SvIndex extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvIndex() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		
		request.setAttribute("errorMsg", "");
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/INDEX.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());