package com.dave.codepower.codegen.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.dave.codepower.codegen.models.TableInfo;
import com.dave.codepower.codegen.util.CodeUtil;
import com.dave.codepower.codegen.util.CodegenAPI;
/**
 * 
 * @author MATU
 *	After defining the model, we specify its attributes (variable names in Java conventions)
 *	We should be able to fetch the database table details from the given project's DB_NAME and the model's PAIRING_TABLE 
 */
@WebServlet(
		asyncSupported = true, 
		description = "Show plain code in TXT", 
		urlPatterns = { "/view/code/plain/*"})
public class SvCodeView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvCodeView() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		HttpSession session = request.getSession();
		Project project  = (Project) session.getAttribute("currentProject");
		
		int modelID = Integer.parseInt(CodeUtil.getResourceID(request));
		
		Model model = null;
		
		Connection con = DBConnection.createConnection();
		
		try {
			model = DBRead.getModel(con, modelID);
			if(project == null) {
				project = DBRead.getProject(con, model.getProjectID());
				session.setAttribute("currentProject", project);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection(con);
		}
		
		String dbName = project.getDbName();
		String userName = project.getDbUser();
		String password = project.getDbPassword();
		String tableName = model.getTable();
		
		/**
		 * Do what?
		 * 	Fetch information about the table using the above information
		 * 	1. Create temporary connection to db
		 * 	2. Fetch TableInfo 
		 */
		TableInfo tableInfo = null;
		try {
			tableInfo = CodeUtil.getTableInfo(dbName, userName, password, tableName);
			tableInfo.setModelName(model.getEntityName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Special code
		List<String> attributeList = new ArrayList<>();
		Map<String, String> modelAttributeMap = new LinkedHashMap<String, String>(); 	//In the form <FieldName, DataType> e.g <"FirstName", "String">
		Map<String, String> tableMapping = new LinkedHashMap<String, String>();	//Maps model attributes(variable names) to corresponding table columns <FieldName, Column>
		//Special code
		
		List<String> variables = new ArrayList<>();
		
		int index = 0;
		for(String s : tableInfo.getColumnList()) {
			String col = CodeUtil.getConventionalVariableName(s);
			String type = CodeUtil.getDataType(tableInfo.getDataTypeList().get(index));
			variables.add(col + ":" + type);
			modelAttributeMap.put(col, type);
			attributeList.add(col);
			tableMapping.put(col, s);
			index++;
		}
		
		/*
		 * *************************************************************************************************************************
		 * 	BUILD CODE IN MODULES
		 * *************************************************************************************************************************
		 */
		String servletCreate = CodegenAPI.createServletCreate(model.getEntityName(), tableInfo, modelAttributeMap, tableMapping);
		String formCreate = CodegenAPI.createHTML_Form_Create(tableInfo, modelAttributeMap);
		
//		response.setContentType("text/plain");
//		response.getWriter().write("FORM: CREATE \n\n\n\n");
//		response.getWriter().write(formCreate);
//		response.getWriter().write("");
		
//		formCreate = formCreate.replaceAll("<", "&lt;");
//		formCreate = formCreate.replaceAll(">", "&gt;");
//		formCreate = formCreate.replaceAll("&", "&amp;");
		
//		request.setAttribute("error", "");
//		request.setAttribute("table", tableInfo);
//		request.setAttribute("model", model);
//		request.setAttribute("variables", variables);
//		
//		
//		
//		//From generated code
//		request.setAttribute("genClass", CodegenAPI.createClassDefinition(model.getEntityName(), modelAttributeMap));
//		request.setAttribute("genCRUD_Create", CodegenAPI.createCreateOperation(model.getEntityName(), tableName, modelAttributeMap, tableMapping));
//		request.setAttribute("genCRUD_Read_Single", CodegenAPI.createReadOperation(model.getEntityName(), tableName, attributeList, modelAttributeMap, tableMapping));
//		request.setAttribute("genCRUD_Read_List", CodegenAPI.createReadListOperation(model.getEntityName(), tableName, attributeList, modelAttributeMap, tableMapping));
//		request.setAttribute("genCRUD_Update", CodegenAPI.createUpdateOperation(model.getEntityName(), tableName, attributeList, modelAttributeMap, tableMapping));
//		
//		request.setAttribute("servletCreate", servletCreate);
		request.setAttribute("code", formCreate);
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/CODE_VIEW.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}


//response.getWriter().append("Served at: ").append(request.getContextPath());