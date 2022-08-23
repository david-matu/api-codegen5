package com.dave.codepower.codegen.servlet;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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

@WebServlet(
		asyncSupported = true, 
		description = "Generates an Angular Project with fundamental code with the conventional directory structure. By David 17-Aug-2022", 
		urlPatterns = { "/generate-angular-project/*"})
public class SvGenerateAngularCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SvGenerateAngularCode() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		//request.setAttribute("errorMsg", "");
		int projectID = Integer.parseInt(CodeUtil.getResourceID(request));		
		
		request.setAttribute("projectID", projectID);
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/NG_PROJECT_GENERATE_FORM.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * This method will receive the details of the project to create:
	 * 		#Name of project (#ArtifactId)
	 * 		#Source Folder
	 * 		#Package Name (#GroupId)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int projectID = Integer.parseInt(CodeUtil.getResourceID(request));
		String userHome = System.getProperty("user.home");
		
		Project project = null;
		
		Connection con = DBConnection.createConnection();
		try {
			project = DBRead.getProject(con, projectID);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		/**
		 * Fetch models and write them to respective directories
		 * Objective: For each model, write content to respective directories
		 * How to:
		 * 1. List models
		 * 2. Iterate and Write it's entire content to file (entire: Model, Mapper, DAO, Service and Resource)
		 * 
		 */
		List<Model> modelList = new ArrayList<>();		
		try {
			modelList = DBRead.getModelList(con, projectID);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection(con);
		}
		
		String projectName = request.getParameter("projectName");
		String sourceDirectory = request.getParameter("sourceFolder");	// src
		
		//Now resolve the slashes and dots
		String sourceFolder = sourceDirectory;	//.replaceAll("/", "\\"+File.separator);		
		
		/**
		 * Initialize a List of directories to create
		 */
		String basePath = userHome + File.separator + "Documents" + File.separator + "CodeGen-Angular-Projects" + File.separator + projectName;
		
		File appDir = new File(basePath + File.separator + "src" + File.separator  + "app");
		
		boolean directoriesMade = appDir.mkdirs();
		
		if(directoriesMade) {
			System.out.println("Angular Project Directory Created Successfully::: ->" + basePath);
			System.out.println("App Folder Created (" + directoriesMade + ")::: ->" + appDir.getAbsolutePath());
			//System.out.println("App Path:::::::::::::::::::::::::::: ->" + file.getAbsolutePath());
		}
		
		String[] packages = {"models", "services", "components"};
		
		// Make the packages under the base App Path
		for(String s : packages) {
			File f = new File(appDir + File.separator + s);
			boolean isCreated = f.mkdir();
			if(isCreated == true) {
				System.out.println("Package created: " + f.getAbsolutePath());
			}
		}
		
		/**
		 * Refer to source files
		 */
		
		
//		String appBanner = CodegenAPI.generateBanner(projectName);
//		String configYML = CodegenAPI.generateConfigFile(projectName, project.getDbName(), groupID, 12345);
//		String pomXML = CodegenAPI.generatePOM(projectName, groupID);
//		String appModel = CodegenAPI.generateAppModel(groupID);
//		String appMapper = CodegenAPI.generateAppMapper(groupID);
//		String appDAO = CodegenAPI.generateAppDAO(groupID);
//		String appService = CodegenAPI.generateAppService(groupID);
//		String appConstants = CodegenAPI.generateAppConstants(groupID);
//		String appHealth = CodegenAPI.generateAppHealthCheck(groupID);
//		
//		String configClass = CodegenAPI.generateConfigurationClass(projectName, groupID);
//		String mainClass = CodegenAPI.generateMainClass(projectName, groupID, modelList);		
//		
//		String basePackagePath = file.getAbsolutePath();
//		
//		CodegenAPI.createFile(new File(resourceFolder.getAbsolutePath()), "banner.txt", appBanner);
//		CodegenAPI.createFile(new File(resourceFolder.getAbsolutePath()), "SQL_Functions_Generic_by_Dave.txt", CodegenAPI.getGenericSQLFunctions());
//		CodegenAPI.createFile(new File(basePath), "config.yml", configYML);
//		CodegenAPI.createFile(new File(basePath), "pom.xml", pomXML);
//		//+ File.separator + "core"
//		//CodeUtil.capitalize(projectName)
//		
//		CodegenAPI.createFile(new File(basePackagePath), CodeUtil.capitalize(projectName) + "Application.java", mainClass);
//		CodegenAPI.createFile(new File(basePackagePath), CodeUtil.capitalize(projectName) + "Configuration.java", configClass);
//		
//		CodegenAPI.createFile(new File(basePackagePath + File.separator + "health"), "AppHealthCheck.java", appHealth);
//		CodegenAPI.createFile(new File(basePackagePath + File.separator + "util"), "AppConstants.java", appConstants);
//		
//		CodegenAPI.createFile(new File(basePackagePath + File.separator + "core"), "App.java", appModel);
//		CodegenAPI.createFile(new File(basePackagePath + File.separator + "mappers"), "AppMapper.java", appMapper);
//		CodegenAPI.createFile(new File(basePackagePath + File.separator + "dao"), "AppDAO.java", appDAO);
//		CodegenAPI.createFile(new File(basePackagePath + File.separator + "service"), "AppService.java", appService);
//		
		//Write the files appropriately
		for(Model model : modelList) {
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
			TableInfo tableInfo = new TableInfo();
			try {
				tableInfo = CodeUtil.getTableInfo(dbName, userName, password, tableName);
				tableInfo.setModelName(model.getEntityName());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			List<String> attributeList = new ArrayList<>();
			Map<String, String> modelAttributeMap = new LinkedHashMap<String, String>(); 	//In the form <FieldName, DataType> e.g <"FirstName", "String">
			Map<String, String> tableMapping = new LinkedHashMap<String, String>();	//Maps model attributes(variable names) to corresponding table columns <FieldName, Column>
			
			/**
			 * Populate the data structures with appropriate data
			 */
			List<String> variables = new ArrayList<>();
			
			int index = 0;
			/**
			 * Use Custom Attributes as saved from DB Table column (CUSTOM_ATTRIBUTES)
			 * 
			 * BUT
			 * 	ONLY IF THAT FIELD CONTAINS CUSTOM ATTRIBUTES
			 * 	OTHERWISE USE EXISTING COLUMNS AS FROM TABLE DEFINITION
			 */
			String attr = model.getCustomAttributes() == null ? "" : model.getCustomAttributes();
			index = 0;
			if(attr != "") {
				//modelId:int, projectId:int, ...
				String[] attSplit = attr.split(",");
				 for(String s : attSplit) {
					 //Split by colon (:)
					 //modelId:int
					 String[] dSplit = s.split(":");
					 
					 String col = dSplit[0].trim();
					 String type =  dSplit[1].trim();
					 variables.add(col + ":" + type);
						modelAttributeMap.put(col, type);
						attributeList.add(col);
						tableMapping.put(col, tableInfo.getColumnList().get(index));
						index++;
				 }
			} else {		
				for(String s : tableInfo.getColumnList()) {
					String col = CodeUtil.getConventionalVariableName(s);
					String type = CodeUtil.getTypescriptType(tableInfo.getDataTypeList().get(index));
					variables.add(col + ":" + type);
					modelAttributeMap.put(col, type);
					attributeList.add(col);
					tableMapping.put(col, s);
					index++;
				}
			}
			
//			String genEntity =  CodegenAPI.createAPIEntity(model.getEntityName(), groupID, modelAttributeMap);
//			String genMapper =  CodegenAPI.createAPIMapper(model.getEntityName(), groupID, modelAttributeMap, tableMapping);
//			String genDAO= CodegenAPI.createAPIEntityDAO(model.getEntityName(), groupID, tableName, attributeList, modelAttributeMap, tableMapping);
//			String genService = CodegenAPI.createAPIEntityService(model.getEntityName(), groupID, attributeList, modelAttributeMap);
//			String genResource = CodegenAPI.createAPIEntityResource(model.getEntityName(), groupID, attributeList, modelAttributeMap, tableMapping);
//			
			String modelName = model.getEntityName();
			String genModel =  CodegenAPI.createNgModel(modelName, modelAttributeMap);

			
			CodegenAPI.createFile(new File(appDir + File.separator + "models"), modelName + ".ts", genModel);
			
//			CodegenAPI.createFile(new File(basePackagePath + File.separator + "core"), modelName + ".java", genEntity);
//			CodegenAPI.createFile(new File(basePackagePath + File.separator + "mappers"), modelName  + "Mapper.java", genMapper);
//			CodegenAPI.createFile(new File(basePackagePath + File.separator + "dao"), modelName + "DAO.java", genDAO);
//			CodegenAPI.createFile(new File(basePackagePath + File.separator + "service"), modelName + "Service.java", genService);
//			CodegenAPI.createFile(new File(basePackagePath + File.separator + "resources"), modelName + "Resource.java", genResource);
		} //End of iterations for Models
		
//		Runtime rt = Runtime.getRuntime();
//		
//		try {
//			System.out.println("Opening System Command-line...");
//			rt.exec(new String[] {"cmd.exe", "start"});
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
		
		//Opening File Explorer the Project Directory
		try {
			System.out.println("Opening System Command-line...");
			Desktop.getDesktop().open(appDir);
		} catch (IOException ex) {
			System.out.println("(!) Could not open directory in your File Explorer. Reason:\n" + ex.getLocalizedMessage());
		}		
		
		request.setAttribute("projectPath", basePath);
		request.setAttribute("baseAppPath", appDir.getAbsolutePath());
		request.setAttribute("projectName", projectName);
		
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/pub/NG_PROJECT_GENERATE_RESULT.jsp");
		dispatcher.forward(request, response);		
	}

}


/**
 * String[] sd = sourceDirectory.split("/");
		for(String s : sd) {
			if(s != "/") {
				
			}
		}
 */

//response.getWriter().append("Served at: ").append(request.getContextPath());