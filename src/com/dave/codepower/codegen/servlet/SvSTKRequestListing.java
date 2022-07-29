package com.dave.codepower.codegen.servlet;

import java.io.BufferedReader;
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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dave.codepower.codegen.db.DBConnection;
import com.dave.codepower.codegen.db.DBCreate;
import com.dave.codepower.codegen.db.DBRead;

@WebServlet(
		asyncSupported = true, 
		description = "Serves STK", 
		urlPatterns = { "/stk/requests"})
public class SvSTKRequestListing extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvSTKRequestListing() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/STK_REQUESTS_LIST.jsp");
		dispatcher.forward(request, response);
	}

	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MPesa Responded to this POST servlet");
		
		
		 String data = "";   
		 StringBuilder builder = new StringBuilder();
		 BufferedReader reader = request.getReader();
		 String line;
		 
		 while ((line = reader.readLine()) != null) {
			 builder.append(line);
		 }
		 
		 data = builder.toString();
		 System.out.println("JSON Received from MPesa callback: \n" + data);
		 
//		 Object obj;
//		try {
//			obj = new JSONParser().parse(line);
//			JSONObject jo = (JSONObject) obj;
//			
//			
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		 
//		System.out.println("JSON Received from MPesa callback: \n" + line);
//		
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());