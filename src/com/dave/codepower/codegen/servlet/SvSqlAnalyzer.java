package com.dave.codepower.codegen.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dave.codepower.codegen.db.DBConnection;
import com.dave.codepower.codegen.db.DBRead;
import com.dave.codepower.codegen.db.SQLData;

@WebServlet(
		asyncSupported = true, 
		description = "SQL Utility Tool", 
		urlPatterns = { "/util/sql", "/sql-analyzer"})
public class SvSqlAnalyzer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvSqlAnalyzer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/SQL_UTIL.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con = DBConnection.createConnection();
		String sqlStatement = request.getParameter("sqlStatement");
		
		SQLData data = new SQLData();
		
		try {
			data = DBRead.getSQLData(con, sqlStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		List<String> colData = new ArrayList<>();
		
		for(Map.Entry<String, String> pair : data.getSqlDataMap().entrySet()) {
			colData.add(pair.getValue());
		}
		
		request.setAttribute("sqlData", data);
		request.setAttribute("colDataList", colData);
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/SQL_REPORT.jsp");
		dispatcher.forward(request, response);
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());