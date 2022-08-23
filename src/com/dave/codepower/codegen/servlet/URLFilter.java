package com.dave.codepower.codegen.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dave.codepower.codegen.db.DBConnection;
import com.dave.codepower.codegen.db.DBRead;
import com.dave.codepower.codegen.models.Project;

@WebFilter(asyncSupported = true, urlPatterns = {"/*"})
public class URLFilter implements Filter {
	private static String IGNORE_PATH;
    public URLFilter() { }

	
	public void destroy() { }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		//HttpServletResponse res = (HttpServletResponse)response;
		HttpSession session = req.getSession();
		
		if(session.getAttribute("projectList") == null) {
			Connection con = DBConnection.createConnection();
			List<Project> pList = new ArrayList<>();
			try {
				pList = DBRead.getProjectList(con);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBConnection.closeConnection(con);
			}
			session.setAttribute("projectList", pList);
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException { 
		IGNORE_PATH = fConfig.getInitParameter("/assets");
	}

}
