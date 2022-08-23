package com.dave.codepower.codegen.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dave.codepower.codegen.models.Model;
import com.dave.codepower.codegen.models.TableInfo;

/**
 * 
 * @author MATU
 * 27-Mar-2022
 * 
 * This interface will help generate code, it has modules to generate the code in question
 * 
 */
public class CodegenAPI {
	
	/**
	 * Generate a Java Class model 
	 * @param modelName
	 * @return
	 * 
	 * Map<String, String> modelAttributeMap; 	//In the form <FieldName, DataType> e.g <"FirstName", "String">
	 */
	public static String createClassDefinition(String modelName, Map<String, String> modelAttributeMap) {
		String fin = "";	//Final String
		
		StringBuffer sb = new StringBuffer();
		sb.append("public class " + modelName +" implements Serializable {\n");
		//sb.append();	private static final long serialVersionUID = 1L;
		sb.append("\tprivate static final long serialVersionUID = 1L;\n");
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\tprivate " + pair.getValue() + " " + pair.getKey() + ";");
			sb.append("\n");
		}
		
		sb.append("\n\tpublic " + modelName + "() {\n"
				+ "\t\tsuper();\n"
				+ "\t}\n");
		
		//Generate Getters and Setters
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\n\tpublic " + pair.getValue() + " get" + CodeUtil.capitalize(pair.getKey()) + "() {\n" ); //e.g public String getFirstName() { ... }
			sb.append("\t\treturn " + pair.getKey() + ";\n");
			sb.append("\t}\n");
			
			sb.append("\n\tpublic void set" + CodeUtil.capitalize(pair.getKey()) + "(" + pair.getValue() + " " + pair.getKey() + ") {\n" ); //e.g public void setFirstName(String firstName) { ... }
			sb.append("\t\tthis." + pair.getKey() + " = " + pair.getKey() + ";\n"); //this.firstName = firstName;
			sb.append("\t}\n");
		}
		
		sb.append("\n");
		sb.append("}\n");
		
		fin = sb.toString();
		return fin;
	}
	
	
	//CREATE A GENERAL CREATOR CODE that enables writing data from model to the database
	public static String createCreateOperation(String modelName, String tableName, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {
		String fin = "";	//Final String
		
		String firstLetter = modelName.substring(0, 1).toLowerCase();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\tpublic static void create" + CodeUtil.capitalize(modelName) + "(Connection con, " + modelName + " " + firstLetter
				+ ") throws SQLException {\n");
		sb.append("\t\tString query = \"INSERT INTO " + tableName + "(");
		
		int index = 0;
		for(Map.Entry<String, String> pair : tableMapping.entrySet()) {
			sb.append(pair.getValue());
			if(index != tableMapping.entrySet().size() - 1) {
				sb.append(", ");
			} else {
				sb.append(" ");
			}
			index++;
		}
		sb.append(")\"\n"); //By now we have an SQL like -> String query = "INSERT INTO PERSON (FIRST_NAME, LAST_NAME, AGE)"
		
		sb.append("\t\t\t\t+ \" values(");
		//Re-use the variable index
		index = 0;
		for(Map.Entry<String, String> pair : tableMapping.entrySet()) {
			sb.append("?");
			if(index != tableMapping.entrySet().size() - 1) {
				sb.append(", ");
			} else {
				sb.append(" ");
			}
			index++;
		} //IT should produce -> values
		//sb.replace(sb.length()-1, sb.length(), );	//Remove the last
		//sb.toString().substring(sb.length()-1);
		sb.append(")\";\n");	//
		sb.append("\n\t\ttry (PreparedStatement pst = con.prepareStatement(query);) {\n");
		
		//Counter to refer to Map entry being iterated
		int counter = 0;
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t\t\tpst.set" + CodeUtil.capitalize(pair.getValue()) + "(" + (counter + 1) + ", " + firstLetter + ".get" + CodeUtil.capitalize(pair.getKey()) + "());\n");
			counter++;
		}
		
		sb.append("\t\t\tpst.executeUpdate();\n");
		
		sb.append("\t\t} catch(SQLException e) {\n"
				+ "\t\t\te.printStackTrace();\n"
				+ "\t\t}\n");
		
		sb.append("\t}");
		
		fin = sb.toString();
		return fin;
	}
	
	public static String createReadOperation(String modelName, String tableName, List<String> attributeList, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {
		String fin = "";	//Final String
		
		String firstLetter = modelAttributeMap.get(attributeList.get(0)).substring(0, 1).toLowerCase(); //This gets String -> s, int -> i
		//modelAttributeMap.keySet().stream().findFirst().get().substring(0, 1).toLowerCase();//modelAttributeMap.entrySet().iterator().next().getKey().substring(0, 1).toLowerCase();
		
		//System.out.println("First Letter: " + firstLetter);
		
		StringBuffer sb = new StringBuffer();
		
		/*
		 * Searching through for a probable ID
		 * Most times the ID is usually the first Attribute
		 */
		
		sb.append("\tpublic static " + modelName + " get" + CodeUtil.capitalize(modelName) + "(Connection con, " + modelAttributeMap.get(attributeList.get(0)).toString() + " " + firstLetter
				+ ") throws SQLException {\n");
		sb.append("\t\tString query = \"SELECT * FROM " + tableName + " WHERE " + tableMapping.get(attributeList.get(0)) + ""
				+ " = ?\";");
		//modelAttributeMap.get(attributeList.get(0))
		//modelAttributeMap.values().stream().findFirst().get().toString()
		sb.append("\n\t\ttry (PreparedStatement pst = con.prepareStatement(query);) {\n");
		sb.append("\t\t\tpst.set" + CodeUtil.capitalize(modelAttributeMap.get(attributeList.get(0))) + "(1, " + firstLetter + ");\n"
				+ "\t\t\tResultSet rs = pst.executeQuery();\n"
				+ "\t\t\tif(rs.next()) {\n");
		
		String fLetter = modelName.substring(0,1).toLowerCase();
		sb.append("\t\t\t\t" + modelName + " " + fLetter + " = new " + modelName + "();\n"
				+ "");
		
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t\t\t\t" + fLetter +".set" + CodeUtil.capitalize(pair.getKey()) + "(rs.get" + CodeUtil.capitalize(pair.getValue()) + "(\"" + tableMapping.get(pair.getKey().trim()) + "\"));\n");
		}
		
		sb.append("\t\t\t\treturn " + fLetter + ";\n"
				+ "\t\t\t}\n");
		
		sb.append("\t\t} catch(SQLException e) {\n"
				+ "\t\t\te.printStackTrace();\n"
				+ "\t\t}\n"
				+ "\t\treturn null;\n");
		
		sb.append("\t}");
		
		fin = sb.toString();
		return fin;
	}
	
	public static String createReadListOperation(String modelName, String tableName, List<String> attributeList, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {
		String fin = "";	//Final String
		
		String firstLetter = modelAttributeMap.get(attributeList.get(0)).substring(0, 1).toLowerCase(); //This gets String -> s, int -> i
		//modelAttributeMap.keySet().stream().findFirst().get().substring(0, 1).toLowerCase();//modelAttributeMap.entrySet().iterator().next().getKey().substring(0, 1).toLowerCase();
		
		//System.out.println("First Letter: " + firstLetter);
		
		StringBuffer sb = new StringBuffer();
		
		/*
		 * Searching through for a probable ID
		 * Most times the ID is usually the first Attribute
		 */
		
		sb.append("\tpublic static List<" + modelName + "> get" + CodeUtil.capitalize(modelName) + "List(Connection con) throws SQLException {\n");
		sb.append("\t\tString query = \"SELECT * FROM " + tableName + "\";\n");
		
		//List<Member> mList = new ArrayList<>();
		sb.append("\t\tList<" + modelName +"> " + modelName.substring(0, 1).toLowerCase() + "List = new ArrayList<>();\n");
		sb.append("\n\t\ttry (PreparedStatement pst = con.prepareStatement(query);\n"
				+ "\t\t\tResultSet rs = pst.executeQuery();) {\n");
		
		sb.append("\t\t\twhile(rs.next()) {\n");
		String fLetter = modelName.substring(0,1).toLowerCase();
		
		//Member m = new Member();
		sb.append("\t\t\t\t" + modelName + " " + fLetter + " = new " + modelName + "();\n");
		
		//m.setFirstName(rs.getString("FIRST_NAME_COLUMN"));
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t\t\t\t" + fLetter +".set" + CodeUtil.capitalize(pair.getKey()) + "(rs.get" + CodeUtil.capitalize(pair.getValue()) + "(\"" + tableMapping.get(pair.getKey().toString()) + "\"));\n");
		}
		sb.append("\n\t\t\t\t" + modelName.substring(0, 1).toLowerCase() + "List.add(" + fLetter + ");\n"
				+ "\t\t\t}\n");
		
		sb.append("\t\t} catch(SQLException e) {\n"
				+ "\t\t\te.printStackTrace();\n"
				+ "\t\t}\n"
				+ "\t\treturn " + modelName.substring(0, 1).toLowerCase() + "List;\n\t}"); //return mList
		
		fin = sb.toString();
		return fin;
	}
	
	/*
	 * UPDATE OPERATION 
	 * Updates Entities in the the Database for a given ID of the entity
	 */
	public static String createUpdateOperation(String modelName, String tableName, List<String> attributeList, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {
		String fin = "";	//Final String
		
		//Reverse Order the <JavaField, TableColumn> Map to vice versa
		Map<String, String> rMap = new HashMap<>(); //<TableColumn, JavaField>
		for(Map.Entry<String, String> pair : tableMapping.entrySet())
			rMap.put(pair.getValue().toUpperCase(), pair.getKey());			
		
		String firstLetter = modelName.substring(0, 1).toLowerCase();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\tpublic static void update" + CodeUtil.capitalize(modelName) + "(Connection con, " + CodeUtil.capitalize(modelName) + " " + firstLetter
				+ ") throws SQLException {\n");
		//String sql = "UPDATE MEMBER SET TITLE=?, DESCRIPTION=? WHERE THEME_ID=?";
		sb.append("\t\tString query = \"UPDATE " + tableName + " SET ");
		
		int index = 0;
		for(Map.Entry<String, String> pair : tableMapping.entrySet()) {
			sb.append(pair.getValue().toUpperCase() + "=?");		
			if(index != tableMapping.entrySet().size() - 1) {
				sb.append(", ");
			} else {
				sb.append(" ");
			}
			index++;
		}
		sb.append("WHERE " + tableMapping.get(attributeList.get(0)) + "=?\";\n");
		
		sb.append("\n\t\ttry (PreparedStatement pst = con.prepareStatement(query);) {\n");
		
		//Counter to refer to Map entry being iterated
		int counter = 0;
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t\t\tpst.set" + CodeUtil.capitalize(pair.getValue()) + "(" + (counter + 1) + ", " + firstLetter + ".get" + CodeUtil.capitalize(pair.getKey()) + "());\n");
			counter++;
		}
		sb.append("\t\t\tpst.set" + CodeUtil.capitalize(modelAttributeMap.get(attributeList.get(0))) + "(" + (counter + 1) + 
				", " + firstLetter + ".get" +  CodeUtil.capitalize(attributeList.get(0)) + "());\n");
				//", " + firstLetter + ".get" +  CodeUtil.capitalize(rMap.get(attributeList.get(0)) + "());\n"));
		sb.append("\t\t\tpst.executeUpdate();\n");
		
		sb.append("\t\t} catch(SQLException e) {\n"
				+ "\t\t\te.printStackTrace();\n"
				+ "\t\t}\n");
		
		sb.append("\t}");
		
		fin = sb.toString();
		return fin;
	}
	
	
	/**
	 * CodeGen5 Additions 
	 * 28-Mar-2022
	 */
	
	/**
	 * Generates Servlet which returns a CREATE form and POSTs data to database as retrieved from the form 
	 * @param modelName
	 * @param tableName
	 * @param modelAttributeMap
	 * @param tableMapping
	 * @return
	 */
	public static String createServletCreate(String modelName, String tableName, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {
		String fin = "";	//Final String
		
		String firstLetter = modelName.substring(0, 1).toLowerCase();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("package com.dave.codepower.project.servlet;\r\n\n");
		sb.append("import java.io.IOException;\r\n"
				+ "import javax.servlet.ServletException;\r\n"
				+ "import javax.servlet.annotation.WebServlet;\r\n"
				+ "import javax.servlet.http.HttpServlet;\r\n"
				+ "import javax.servlet.http.HttpServletRequest;\r\n"
				+ "import javax.servlet.http.HttpServletResponse;\r\n\n");
		
		sb.append("@WebServlet(\r\n"
				+ "		asyncSupported = true, \r\n"
				+ "		description = \"Serves Entity\", \r\n"
				+ "		urlPatterns = { \"/create/" + modelName + "\"})\r\n");
		
		String servletName = "Sv" +modelName + "Create";
		
		sb.append("public class " + servletName + " extends HttpServlet {\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;\n\n");
		sb.append("\tpublic " + servletName + "() {\r\n"
				+ "\t\tsuper();\r\n"
				+ "\t}\n\n");
		
		sb.append("\tprotected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n\n"
				+ "\t\tRequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(\"/WEB-INF/pub/CREATE_" + modelName.toUpperCase() + ".jsp\");\r\n"
				+ "\t\tdispatcher.forward(request, response);\r\n"
				+ "\t}\n\n");
		
		sb.append("\tprotected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\n");
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String parsion = "request.getParameter(\"" + pair.getKey() + "\");";
			
			if(pair.getValue().equalsIgnoreCase("int")) {
				parsion = "Integer.parseInt(request.getParameter(\"" + pair.getKey() + "\"));";
			} else if(pair.getValue().equalsIgnoreCase("long")) {
				parsion = "Long.parseLong(request.getParameter(\"" + pair.getKey() + "\"));";
			} else if(pair.getValue().equalsIgnoreCase("Timestamp")) {
				parsion = "Timestamp.valueOf(request.getParameter(\"" + pair.getKey() + "\"));";
			}
			
			sb.append("\t\t" + pair.getValue() + " " + pair.getKey() + " = " + parsion + "\n");
		} //IT should produce -> values
		//sb.replace(sb.length()-1, sb.length(), );	//Remove the last
		//sb.toString().substring(sb.length()-1);
		
		sb.append("\n\n");
		
		String primaryKey = tableMapping.get(0);
		
		sb.append("\t\tString primaryKey = \" " + primaryKey + "\"; //Get from model attributes\r\n"
				+ "\t\tString tableName = \" " + tableName + "\";\r\n"
				+ "\t\tString format = \" " + modelAttributeMap.get(primaryKey) + "\";			//data type of the key\r\n"
				+ "\t\tint length = 8;			//According to Developer's primary key format policy ");
		
		return sb.toString();
	}


	public static String createServletCreate(String entityName, TableInfo tableInfo, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {		
		StringBuffer sb = new StringBuffer();
		
		sb.append("package com.dave.codepower.project.servlet;\r\n\n");
		sb.append("import java.io.IOException;\r\n"
				+ "import javax.servlet.ServletException;\r\n"
				+ "import javax.servlet.annotation.WebServlet;\r\n"
				+ "import javax.servlet.http.HttpServlet;\r\n"
				+ "import javax.servlet.http.HttpServletRequest;\r\n"
				+ "import javax.servlet.http.HttpServletResponse;\r\n\n");
		
		sb.append("@WebServlet(\r\n"
				+ "		asyncSupported = true, \r\n"
				+ "		description = \"Creates a new " + entityName + "\", \r\n"
				+ "		urlPatterns = { \"/create/" + tableInfo.getModelName().toLowerCase() + "\"})\r\n");
		
		String servletName = "Sv" + tableInfo.getModelName() + "Create";
		
		sb.append("public class " + servletName + " extends HttpServlet {\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;\n\n");
		sb.append("\tpublic " + servletName + "() {\r\n"
				+ "\t\tsuper();\r\n"
				+ "\t}\n\n");
		
		sb.append("\tprotected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n\n"
				+ "\t\tRequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(\"/WEB-INF/pub/CREATE_" + tableInfo.getModelName().toUpperCase() + ".jsp\");\r\n"
				+ "\t\tdispatcher.forward(request, response);\r\n"
				+ "\t}\n\n");
		
		sb.append("\tprotected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\n");
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String parsion = "request.getParameter(\"" + pair.getKey() + "\");";
			
			if(pair.getValue().equalsIgnoreCase("int")) {
				parsion = "Integer.parseInt(request.getParameter(\"" + pair.getKey() + "\"));";
			} else if(pair.getValue().equalsIgnoreCase("long")) {
				parsion = "Long.parseLong(request.getParameter(\"" + pair.getKey() + "\"));";
			} else if(pair.getValue().equalsIgnoreCase("Timestamp")) {
				parsion = "Timestamp.valueOf(request.getParameter(\"" + pair.getKey() + "\"));";
			}
			
			sb.append("\t\t" + pair.getValue() + " " + pair.getKey() + " = " + parsion + "\n");
		} //IT should produce -> values
		//sb.replace(sb.length()-1, sb.length(), );	//Remove the last
		//sb.toString().substring(sb.length()-1);
		
		sb.append("\n\n");
		
		sb.append("\t\tString primaryKey = \"" + tableInfo.getColumnList().get(0) + "\"; //Get from model attributes\r\n"
				+ "\t\tString tableName = \"" + tableInfo.getTableName() + "\";\r\n"
				+ "\t\tString format = \"" + modelAttributeMap.entrySet().iterator().next().getValue() + "\";			//data type of the key\r\n"
				+ "\t\tint length = 8;			//According to Developer's primary key format policy \n\n");
		
		sb.append("\t\tConnection con = DBConnection.createConnection();\n");
		
		String shortVar = tableInfo.getModelName().substring(0,1).toLowerCase();
		
		sb.append("\t\t" + tableInfo.getModelName() + " " + shortVar + " = new " + tableInfo.getModelName() + "();\n");
		sb.append("\t\t" + shortVar + ".set" + CodeUtil.capitalize((modelAttributeMap.entrySet().iterator().next().getKey()) + "(CodeUtil.generateUniqueID(con, tableName, primaryKey, format, length));\n"));
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t\t" + shortVar + ".set" + CodeUtil.capitalize(pair.getKey()) + "(" + pair.getKey() + ");\n");
		}
		
		String primaryKeyMethod = CodeUtil.capitalize((modelAttributeMap.entrySet().iterator().next().getKey()));
		
		sb.append("\t\tcon = DBConnection.createConnection();	//Since connection is closed by above CodeUtil\n\n");
		sb.append("\t\tString error = \"\";\n\n");
		sb.append("\t\ttry {\r\n"
				+ "			DBCreate.create" + tableInfo.getModelName() + "(con, " + shortVar + ");\r\n"
				+ "		} catch (SQLException e) {\r\n"
				+ "			e.printStackTrace();\r\n"
				+ "			error = e.getLocalizedMessage();\r\n"
				+ "		} finally {\r\n"
				+ "			DBConnection.closeConnection(con);\r\n"
				+ "		}\n\n");
		sb.append("\t\tif(!error.equals(\"\")) {\r\n"
				+ "\t\t\trequest.setAttribute(\"error\", error);		\r\n"
				+ "\t\t\tRequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(\"/WEB-INF/pub/CREATE_" + tableInfo.getModelName().toUpperCase() + ".jsp\");\r\n"
				+ "\t\t\tdispatcher.forward(request, response);\r\n"
				+ "\t\t} else {\r\n"
				+ "\t\t\tresponse.sendRedirect(request.getServletContext().getContextPath() + \"/" + tableInfo.getModelName().toLowerCase() +"/\" + " + shortVar + ".get" + primaryKeyMethod +"());\r\n"
				+ "\t\t}\n");
		sb.append("\t}\n");
		sb.append("}\n");
		sb.append("");
		sb.append("");
		sb.append("");
		sb.append("");
		sb.append("");
		
		return sb.toString();
	}
	
	public static String createServletUpdate(String entityName, TableInfo tableInfo, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {		
		StringBuffer sb = new StringBuffer();
		
		sb.append("package com.dave.codepower.project.servlet;\r\n\n");
		sb.append("import java.io.IOException;\r\n"
				+ "import javax.servlet.ServletException;\r\n"
				+ "import javax.servlet.annotation.WebServlet;\r\n"
				+ "import javax.servlet.http.HttpServlet;\r\n"
				+ "import javax.servlet.http.HttpServletRequest;\r\n"
				+ "import javax.servlet.http.HttpServletResponse;\r\n\n");
		
		sb.append("@WebServlet(\r\n"
				+ "		asyncSupported = true, \r\n"
				+ "		description = \"For Updating " + entityName + "\", \r\n"
				+ "		urlPatterns = { \"/update/" + tableInfo.getModelName().toLowerCase() + "\"})\r\n");
		
		String servletName = "Sv" + tableInfo.getModelName() + "Update";
		
		sb.append("public class " + servletName + " extends HttpServlet {\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;\n\n");
		sb.append("\tpublic " + servletName + "() {\r\n"
				+ "\t\tsuper();\r\n"
				+ "\t}\n\n");
		
		sb.append("\tprotected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n\n"
				+ "\t\tRequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(\"/WEB-INF/pub/UPDATE_" + tableInfo.getModelName().toUpperCase() + ".jsp\");\r\n"
				+ "\t\tdispatcher.forward(request, response);\r\n"
				+ "\t}\n\n");
		
		sb.append("\tprotected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\n");
		
		if(tableInfo.getDataTypeList().get(0).equalsIgnoreCase("int")) {
			sb.append("\t\tint entityID = Integer.parseInt(CodeUtil.getResourceID(request));\n\n");
		} else if(tableInfo.getDataTypeList().get(0).equalsIgnoreCase("bigint")) {
			sb.append("\t\tlong entityID = Long.parseLong(CodeUtil.getResourceID(request));\n\n");
		} else {
			sb.append("\t\tString entityID = CodeUtil.getResourceID(request);\n\n");
		}
		
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String parsion = "request.getParameter(\"" + pair.getKey() + "\");";
			
			if(pair.getValue().equalsIgnoreCase("int")) {
				parsion = "Integer.parseInt(request.getParameter(\"" + pair.getKey() + "\"));";
			} else if(pair.getValue().equalsIgnoreCase("long")) {
				parsion = "Long.parseLong(request.getParameter(\"" + pair.getKey() + "\"));";
			} else if(pair.getValue().equalsIgnoreCase("Timestamp")) {
				parsion = "Timestamp.valueOf(request.getParameter(\"" + pair.getKey() + "\"));";
			}
			
			sb.append("\t\t" + pair.getValue() + " " + pair.getKey() + " = " + parsion + "\n");
		} //IT should produce -> values
		//sb.replace(sb.length()-1, sb.length(), );	//Remove the last
		//sb.toString().substring(sb.length()-1);
		
		sb.append("\n\n");
		
		sb.append("\t\tString primaryKey = \"" + tableInfo.getColumnList().get(0) + "\"; //Get from model attributes\r\n"
				+ "\t\tString tableName = \"" + tableInfo.getTableName() + "\";\r\n"
				+ "\t\tString format = \"" + modelAttributeMap.entrySet().iterator().next().getValue() + "\";			//data type of the key\r\n"
				+ "\t\tint length = 8;			//According to Developer's primary key format policy \n\n");
		
		sb.append("\t\tConnection con = DBConnection.createConnection();\n");
		
		String shortVar = tableInfo.getModelName().substring(0,1).toLowerCase();
		
		//sb.append("\t\t" + tableInfo.getModelName() + " " + shortVar + " = new " + tableInfo.getModelName() + "();\n");
		sb.append("\t\t" + entityName +  " " + shortVar + " = null;\r\n\n"
				+ "\t\ttry {\r\n"
				+ "\t\t\t" + shortVar + " = DBRead.get" + entityName + "(con, entityID);\r\n"
				+ "\t\t} catch (SQLException e) {\r\n"
				+ "\t\t\te.printStackTrace();\r\n"
				+ "\t\t}\n\n");
		
		sb.append("\t\t" + shortVar + ".set" + CodeUtil.capitalize((modelAttributeMap.entrySet().iterator().next().getKey()) + "(CodeUtil.generateUniqueID(con, tableName, primaryKey, format, length));\n"));
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t\t" + shortVar + ".set" + CodeUtil.capitalize(pair.getKey()) + "(" + pair.getKey() + ");\n");
		}
		
		String primaryKeyMethod = CodeUtil.capitalize((modelAttributeMap.entrySet().iterator().next().getKey()));
		
		sb.append("\t\tcon = DBConnection.createConnection();	//Since connection is closed by above CodeUtil\n\n");
		sb.append("\t\tString error = \"\";\n\n");
		sb.append("\t\ttry {\r\n"
				+ "			DBUpdate.update" + tableInfo.getModelName() + "(con, " + shortVar + ");\r\n"
				+ "		} catch (SQLException e) {\r\n"
				+ "			e.printStackTrace();\r\n"
				+ "			error = e.getLocalizedMessage();\r\n"
				+ "		} finally {\r\n"
				+ "			DBConnection.closeConnection(con);\r\n"
				+ "		}\n\n");
		sb.append("\t\tif(!error.equals(\"\")) {\r\n"
				+ "\t\t\trequest.setAttribute(\"error\", error);		\r\n"
				+ "\t\t\tRequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(\"/WEB-INF/pub/UPDATE_" + tableInfo.getModelName().toUpperCase() + ".jsp\");\r\n"
				+ "\t\t\tdispatcher.forward(request, response);\r\n"
				+ "\t\t} else {\r\n"
				+ "\t\t\tresponse.sendRedirect(request.getServletContext().getContextPath() + \"/" + tableInfo.getModelName().toLowerCase() +"/\" + " + shortVar + ".get" + primaryKeyMethod +"());\r\n"
				+ "\t\t}\n");
		sb.append("\t}\n");
		sb.append("}\n");
		sb.append("");
		sb.append("");
		sb.append("");
		sb.append("");
		sb.append("");
		
		return sb.toString();
	}
	
	public static String createServletReadSingle(String entityName, TableInfo tableInfo, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {		
		StringBuffer sb = new StringBuffer();
		
		sb.append("package com.dave.codepower.project.servlet;\r\n\n");
		sb.append("import java.io.IOException;\r\n"
				+ "import javax.servlet.RequestDispatcher;\r\n"
				+ "import javax.servlet.ServletException;\r\n"
				+ "import javax.servlet.annotation.WebServlet;\r\n"
				+ "import javax.servlet.http.HttpServlet;\r\n"
				+ "import javax.servlet.http.HttpServletRequest;\r\n"
				+ "import javax.servlet.http.HttpServletResponse;\r\n\n");
		
		sb.append("@WebServlet(\r\n"
				+ "		asyncSupported = true, \r\n"
				+ "		description = \"Read single " + entityName + "\", \r\n"
				+ "		urlPatterns = { \"/show/" + tableInfo.getModelName().toLowerCase() + "/*\"})\n");
		
		String servletName = "Sv" + tableInfo.getModelName() + "Show";
		
		sb.append("public class " + servletName + " extends HttpServlet {\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;\n\n");
		sb.append("\tpublic " + servletName + "() {\r\n"
				+ "\t\tsuper();\r\n"
				+ "\t}\n\n");
		
		sb.append("\tprotected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n\n");
		
		if(tableInfo.getDataTypeList().get(0).equalsIgnoreCase("int")) {
			sb.append("\t\tint entityID = Integer.parseInt(CodeUtil.getResourceID(request));\n\n");
		} else if(tableInfo.getDataTypeList().get(0).equalsIgnoreCase("bigint")) {
			sb.append("\t\tlong entityID = Long.parseLong(CodeUtil.getResourceID(request));\n\n");
		} else {
			sb.append("\t\tString entityID = CodeUtil.getResourceID(request);\n\n");
		}
		
		String shortVar = tableInfo.getModelName().substring(0,1).toLowerCase();
		sb.append("\t\t" + tableInfo.getModelName() + " " + shortVar + " = null;\n\n"); //" + tableInfo.getModelName() + "();\n");
		
		sb.append("\t\tConnection con = DBConnection.createConnection();\r\n"
				+ "\t\tString error = \"\";\n\n");
		
		sb.append("\t\ttry {\r\n"
				+ "\t\t\t" + shortVar + " = DBRead.get" + tableInfo.getModelName() + "(con, entityID);\r\n"
				+ "\t\t} catch (SQLException e) {\r\n"
				+ "\t\t\te.printStackTrace();\r\n"
				+ "\t\t\terror = e.getLocalizedMessage();\r\n"
				+ "\t\t} finally {\r\n"
				+ "\t\t\tDBConnection.closeConnection(con);\r\n"
				+ "\t\t}\n\n");
		
		sb.append("\t\trequest.setAttribute(\"error\", error);\r\n"
				+ "\t\trequest.setAttribute(\"" + entityName + "\", " + shortVar + ");\n\n");
		
		sb.append("\t\tRequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(\"/WEB-INF/pub/" + entityName.toUpperCase() + "_VIEW.jsp\");\r\n"
				+ "\t\tdispatcher.forward(request, response);\n\n");
		sb.append("\t}\n");
		
		sb.append("\n"
				+ "\tprotected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n"
				+ "\t}\n");
		
		sb.append("}\n");
		//sb.append("int entityID = Integer.parseInt(CodeUtil.getResourceID(request));");
		//int entityID = Integer.parseInt(CodeUtil.getResourceID(request));
		
		
		return sb.toString();
	}
	
	public static String createServletReadList(String entityName, TableInfo tableInfo, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {		
		StringBuffer sb = new StringBuffer();
		
		sb.append("package com.dave.codepower.project.servlet;\r\n\n");
		sb.append("import java.io.IOException;\r\n"
				+ "import java.sql.Connection;\r\n"
				+ "import java.sql.SQLException;\r\n"
				+ "import java.util.ArrayList;\r\n"
				+ "import java.util.List;\r\n"
				+ "\r\n"
				+ "import javax.servlet.RequestDispatcher;\r\n"
				+ "import javax.servlet.ServletException;\r\n"
				+ "import javax.servlet.annotation.WebServlet;\r\n"
				+ "import javax.servlet.http.HttpServlet;\r\n"
				+ "import javax.servlet.http.HttpServletRequest;\r\n"
				+ "import javax.servlet.http.HttpServletResponse;\r\n\n");
		
		sb.append("@WebServlet(\r\n"
				+ "		asyncSupported = true, \r\n"
				+ "		description = \"Read List of " + entityName + "\", \r\n"
				+ "		urlPatterns = { \"/listing/" + tableInfo.getModelName().toLowerCase() + "/*\"})\n");
		
		String servletName = "Sv" + tableInfo.getModelName() + "Listing";
		
		sb.append("public class " + servletName + " extends HttpServlet {\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;\n\n");
		sb.append("\tpublic " + servletName + "() {\r\n"
				+ "\t\tsuper();\r\n"
				+ "\t}\n\n");
		
		sb.append("\tprotected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n\n");
		
		if(tableInfo.getDataTypeList().get(0).equalsIgnoreCase("int")) {
			sb.append("\t\tint entityID = Integer.parseInt(CodeUtil.getResourceID(request));\n\n");
		} else if(tableInfo.getDataTypeList().get(0).equalsIgnoreCase("bigint")) {
			sb.append("\t\tlong entityID = Long.parseLong(CodeUtil.getResourceID(request));\n\n");
		} else {
			sb.append("\t\tString entityID = CodeUtil.getResourceID(request);\n\n");
		}
		
		String shortVar = tableInfo.getModelName().substring(0,1).toLowerCase();
		sb.append("\t\tList<" + entityName + "> " + shortVar + "List = new ArrayList<>();\n\n"); //" + tableInfo.getModelName() + "();\n");
		
		sb.append("\t\tConnection con = DBConnection.createConnection();\r\n"
				+ "\t\tString error = \"\";\n\n");
		
		sb.append("\t\ttry {\r\n"
				+ "\t\t\t" + shortVar + "List = DBRead.get" + tableInfo.getModelName() + "List(con, entityID);\r\n"
				+ "\t\t} catch (SQLException e) {\r\n"
				+ "\t\t\te.printStackTrace();\r\n"
				+ "\t\t\terror = e.getLocalizedMessage();\r\n"
				+ "\t\t} finally {\r\n"
				+ "\t\t\tDBConnection.closeConnection(con);\r\n"
				+ "\t\t}\n\n");
		
		sb.append("\t\trequest.setAttribute(\"error\", error);\r\n"
				+ "\t\trequest.setAttribute(\"" + shortVar + "List\", " + shortVar + "List);\n\n");
		
		sb.append("\t\tRequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(\"/WEB-INF/pub/" + entityName.toUpperCase() + "_VIEW.jsp\");\r\n"
				+ "\t\tdispatcher.forward(request, response);\n\n");
		sb.append("\t}\n");
		
		sb.append("\n"
				+ "\tprotected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\r\n"
				+ "\t}\n");
		
		sb.append("}\n");
		//sb.append("int entityID = Integer.parseInt(CodeUtil.getResourceID(request));");
		//int entityID = Integer.parseInt(CodeUtil.getResourceID(request));
		
		
		return sb.toString();
	}
	
	public static String createHTML_Form_Create(TableInfo tableInfo, Map<String, String> modelAttributeMap) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"bg-light d-none card border-danger p-2 font-weight-bold\" id=\"feedback-card\">${feedback}</div>\n");
		sb.append("<form class=\"form\" id=\"" + tableInfo.getModelName().toLowerCase() +"-form\" method=\"POST\" action=\"${pageContext.request.contextPath}/create/" + tableInfo.getModelName().toLowerCase() + "\">\n");
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String varName = CodeUtil.getConventionalVariableName(pair.getKey());
			String dType= CodeUtil.getConventionalVariableName(pair.getValue());
			
			if(dType.equalsIgnoreCase("int") || dType.equalsIgnoreCase("long")) {
				sb.append("\t<div class='form-group'>\n"
						+ "\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n"
						+ "\t\t<input type=\"number\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"\" min=\"\" required/>\n"
						+ "\t</div>\n\n");
			} else if(dType.equalsIgnoreCase("String")) {
				sb.append("\t<div class='form-group'>\n"
						+ "\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n"
						+ "\t\t<input type=\"text\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"\" required/>\n"
						+ "\t</div>\n\n");
			} else if(dType.equalsIgnoreCase("Timestamp") || dType.equalsIgnoreCase("Date")) {
				sb.append("\t<div class='form-group'>\n"
						+ "\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n"
						+ "\t\t<input type=\"date\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"\" required/>\n"
						+ "\t</div>\n\n");
			}
		}
		sb.append("\t<input type=\"hidden\" id=\"nextURL\" name=\"nextURL\" value=\"${pageContext.request.contextPath}/\"/>\n"); 
		sb.append("\t<button class=\"btn btn-md btn-primary\" id=\"btn-register\" type=\"submit\">SUBMIT</button>\n");
		sb.append("</form>\n\n");
		sb.append("");
		
		//JavaScript
		sb.append("<script>\n");
		sb.append("\t$(document).ready(function() {\n");
		sb.append("\t\t$(\"#" + tableInfo.getModelName().toLowerCase() + "-form\").on(\"submit\", function(e){\n");
		sb.append("\t\t\te.preventDefault();\r\n"
				+ "\t\t\t$(\"#feedback-card\").removeClass(\"d-none\").empty().html(\"<strong>Sending request to server</strong>\");		//Animate loading... here\r\n"
				+ "\t\t\tvar formURL = $(\"#admina-form\").attr(\"action\")\n\n");
		sb.append("\t\t$.post(formURL,\r\n"
				+ "\t\t\t{\n");
		
		int total = modelAttributeMap.entrySet().size();
		int count = 1;
		
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String varName = CodeUtil.getConventionalVariableName(pair.getKey());
			sb.append("\t\t\t" + varName + " : $(\"#" + varName + "\")");
			if(count < total) {
				sb.append(",\n");
			}
			count++;
		}
		
		sb.append("\n\t\t\t},\n");
		sb.append("\t\t\tfunction(data) {\r\n"
				+ "\t\t\tif(data == 'FAILED'){\r\n"
				+ "\t\t\t\tscrollTopFunc();\r\n"
				+ "\t\t\t\t$('#feedback-card').empty().html(\"<strong>An error occurred</strong>\")\r\n"
				+ "\t\t\t}\r\n"
				+ "\t\t\telse if(data == 'SUCCESS'){\r\n"
				+ "\t\t\t\t$('#feedback-card').empty().html(\"<strong class='text-success'>Successful operation!</strong>\")\r\n"
				+ "\t\t\t\t$(location).attr('href', $('#nextURL').val());\r\n"
				+ "\t\t\t}\r\n"
				+ "\t\t});\n"); 	//Close post method
		sb.append("\t});\n");	//Close form submit
		sb.append("\t});\n");	//Close document.ready()
		sb.append("</script>");
		sb.append("");
		sb.append("");
		
		return sb.toString();
	}
	
	
	public static String createHTML_Form_Create_V2(TableInfo tableInfo, Map<String, String> modelAttributeMap) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"bg-light d-none card border-danger p-2 font-weight-bold\" id=\"feedback-card\">${feedback}</div>\n\t");
		sb.append("<form class=\"form\" id=\"admina-form\" method=\"POST\" action=\"${pageContext.request.contextPath}/create/" + tableInfo.getModelName().toLowerCase() + "\">\n\t");
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String varName = pair.getKey(); //CodeUtil.getConventionalVariableName(pair.getKey());
			String dType= pair.getValue();	//CodeUtil.getConventionalVariableName(pair.getValue());
			
			if(dType.equalsIgnoreCase("int") || dType.equalsIgnoreCase("long")) {
				sb.append("\t<div class=form-group>\n"
						+ "\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n\t"
						+ "\t\t<input type=\"number\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"\" min=\"\" required/>\n"
						+ "\t</div>\n\t");
			} else if(dType.equalsIgnoreCase("String")) {
				sb.append("\t<div class=form-group>\n"
						+ "\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n"
						+ "\t\t<input type=\"text\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"\" required/>\n"
						+ "\t</div>\n\t");
			} else if(dType.equalsIgnoreCase("Timestamp") || dType.equalsIgnoreCase("Date")) {
				sb.append("\t<div class=form-group>\n"
						+ "\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n"
						+ "\t\t<input type=\"date\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"\" required/>\n"
						+ "\t</div>\n\t");
			}
		}
		sb.append("\t<input type=\"hidden\" id=\"nextURL\" name=\"nextURL\" value=\"${pageContext.request.contextPath}/\"/>\n\t"); 
		sb.append("\t<button class=\"btn btn-md btn-primary\" id=\"btn-register\" type=\"submit\">SUBMIT</button>\n\t");
		sb.append("</form>\n\n\t");
		sb.append("\n\t");
		
		//JavaScript
		sb.append("<script>\n\t");
		sb.append("\t$(document).ready(function() {\n\t");
		sb.append("\t\t$(\"#admina-form\").on(\"submit\", function(e){\n\t");
		sb.append("\t\te.preventDefault();\r\n"
				+ "\t\t$(\"#feedback-card\").removeClass(\"d-none\").empty().html(\"<strong>Sending request to server</strong>\n\t);\n"		//Animate loading... here\r\n \n\t"
				+ "\t\tvar formURL = $(\"#admina-form\").attr(\"action\")\n\t");
		sb.append("\t\t$.post(formURL,\r\n"
				+ "\t\t\t{\n\t");
		
		int total = modelAttributeMap.entrySet().size();
		int count = 1;
		
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String varName = CodeUtil.getConventionalVariableName(pair.getKey());
			sb.append("\t\t\t\t" + varName + " : $(\"#" + varName + "\")\n");
			if(count < total) {
				sb.append(",\n");
			}
			count++;
		}
		
		sb.append("\n\t");
		sb.append("\n\t");
		sb.append("\n\t");
		sb.append("\n\t");
		sb.append("\n\t");
		sb.append("");
		sb.append("");
		
		return sb.toString();
	}
	
	public static String createHTML_Form_Update(TableInfo tableInfo, Map<String, String> modelAttributeMap) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("\t<div class=\"bg-light d-none card border-danger p-2 font-weight-bold\" id=\"feedback-card\">${feedback}</div>\n");
		sb.append("\t<form class=\"form\" id=\"" + tableInfo.getModelName() + "-form\" method=\"POST\" action=\"${pageContext.request.contextPath}/update/" + tableInfo.getModelName().toLowerCase() + "\">\n");
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String varName = pair.getKey(); //CodeUtil.getConventionalVariableName(pair.getKey());
			String dType= pair.getValue();	//CodeUtil.getConventionalVariableName(pair.getValue());
			
			if(dType.equalsIgnoreCase("int") || dType.equalsIgnoreCase("long")) {
				sb.append("\t\t<div class=form-group>\n"
						+ "\t\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n"
						+ "\t\t\t<input type=\"number\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"${" + tableInfo.getModelName() + "." + varName + "}\" min=\"\" required/>\n"
						+ "\t\t</div>\n");
			} else if(dType.equalsIgnoreCase("String")) {
				sb.append("\t\t<div class=form-group>\n"
						+ "\t\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n"
						+ "\t\t\t<input type=\"text\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"${" + tableInfo.getModelName() + "." + varName + "}\" required/>\n"
						+ "\t\t</div>\n");
			} else if(dType.equalsIgnoreCase("Timestamp") || dType.equalsIgnoreCase("Date")) {
				sb.append("\t\t<div class=form-group>\n"
						+ "\t\t\t<label for=\"" + varName + "\" class=\"font-weight-bold\">" + CodeUtil.getCompleteNameOnCamel(varName).toUpperCase() + "</label>\n"
						+ "\t\t\t<input type=\"date\" name=\"" + varName + "\" id=\"" + varName + "\" class=\"form-control\" value=\"${" + tableInfo.getModelName() + "." + varName + "}\" required/>\n"
						+ "\t\t</div>\n\n");
			}
		}
		sb.append("\t\t<input type=\"hidden\" id=\"nextURL\" name=\"nextURL\" value=\"${pageContext.request.contextPath}/" + tableInfo.getModelName() + "/${" + tableInfo.getModelName() + "." + CodeUtil.getConventionalVariableName(tableInfo.getColumnList().get(0)) + "}\"/>\n"); 
		sb.append("\t\t<button class=\"btn btn-md btn-primary\" id=\"btn-register\" type=\"submit\">UPDATE</button>\n");
		sb.append("\t</form>\n\n");
		sb.append("\n");
		
		//JavaScript
		sb.append("<script>\n");
		sb.append("\t$(document).ready(function() {\n");
		sb.append("\t\t$(\"#" + tableInfo.getModelName() + "-form\").on(\"submit\", function(e){\n");
		sb.append("\t\te.preventDefault();\r\n"
				+ "\t\t$(\"#feedback-card\").removeClass(\"d-none\").empty().html(\"<strong>Sending request to server</strong>\");\n"		//Animate loading... here\r\n \n\t"
				+ "\t\tvar formURL = $(\"#" + tableInfo.getModelName() + "-form\").attr(\"action\");\n");
		sb.append("\t\t$.post(formURL,\r\n"
				+ "\t\t\t{\n");
		
		int total = modelAttributeMap.entrySet().size();
		int count = 1;
		
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String varName = CodeUtil.getConventionalVariableName(pair.getKey());
			sb.append("\t\t\t\t" + varName + " : $(\"#" + varName + "\")");
			if(count < total) {
				sb.append(",\n");
			}
			count++;
		}
		
		sb.append("\n\t\t\t},\n");
		sb.append("\t\t\tfunction(data) {\n");
		sb.append("\t\t\t\tscrollTopFunc();\r\n"
				+ "\t\t\t\t$('#feedback-card').empty().html(\"<strong>An error occurred</strong>\");\n"
				+ "\t\t\t}\n"
				+ "\t\t\telse if(data == 'SUCCESS'){\r\n"
				+ "\t\t\t\t$('#feedback-card').empty().html(\"<strong class='text-success'>Successful operation!</strong>\");\r\n"
				+ "\t\t\t\t$(location).attr('href', $('#nextURL').val());\r\n"
				+ "\t\t\t}\r\n");
		sb.append("\t\t});\n");
		sb.append("\t});\n");
		sb.append("});\n");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	public static String createHTML_Read(TableInfo tableInfo, Map<String, String> modelAttributeMap) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"card shadow border-success\" id=\"profile-overview\">\n");
		sb.append("\t<div class=\"card-header\">\n"); 
		sb.append("\t\t<h5 class=\"card-title\">" + tableInfo.getModelName() + "</h5>\n");
		sb.append("\t\t<h5 class=\"card-title\">${" + tableInfo.getModelName() + "." + CodeUtil.getConventionalVariableName(tableInfo.getColumnList().get(0)) + "}</h5>\n");
		sb.append("\t</div>\n"); 
		sb.append("\t<div class=\"card-body\">\n"); 
		sb.append(""); 
		
		String modelName = tableInfo.getModelName();
		
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String varName = pair.getKey(); //CodeUtil.getConventionalVariableName(pair.getKey());
			String dType= pair.getValue();	//CodeUtil.getConventionalVariableName(pair.getValue());
			
			int index = 0;
			
			sb.append("\t\t<div class=\"row\">\r\n"
					+ "\t\t\t<div class=\"col-lg-3 col-md-4 label \">" + tableInfo.getColumnList().get(index) + "</div>\r\n"
					+ "\t\t\t<div class=\"col-lg-9 col-md-8\">${" + modelName + "." + varName +  "}</div>\r\n"
					+ "\t\t</div>\n");
			index++;
		}		
		sb.append("\t</div>\n");
		sb.append("\t<div class=\"card-footer\">\n");
		sb.append("\t\t\t\t<a class=\"btn btn-sm btn-primary\" href=\"${pageContext.request.contextPath}/edit/" + tableInfo.getModelName() + "/${" + tableInfo.getModelName() + "." + CodeUtil.getConventionalVariableName(tableInfo.getColumnList().get(0)) + " }\">Edit</a>\n");
		sb.append("\t\t\t\t<a class=\"btn btn-sm btn-danger\" href=\"${pageContext.request.contextPath}/delete/" + tableInfo.getModelName() + "/${" + tableInfo.getModelName() + "." + CodeUtil.getConventionalVariableName(tableInfo.getColumnList().get(0)) + " }\">Remove</a>\n");
		sb.append("\t</div>\n");
		sb.append("</div>");
		return sb.toString();
	}
	
	public static String createHTML_ReadList(TableInfo tableInfo, Map<String, String> modelAttributeMap) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<table class=\"table datatable\">\n"); 
		sb.append("\t<thead>\n");
		sb.append("\t\t<tr>\n");
		
		int index = 0;
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {			
			sb.append("\t\t\t<th scope=\"col\">" + tableInfo.getColumnList().get(index)  + "</th>\n");
			index++;
		}
		sb.append("\t\t\t<td>ACTION></td>\n");
		sb.append("\t\t</tr>\n");
		sb.append("\t</thead>\n");
		sb.append("\t<tbody>\r\n");
		sb.append("\t\t<c:forEach items=\"${" + CodeUtil.getConventionalVariableName(tableInfo.getModelName()) + "List}\" var=\"" + tableInfo.getModelName() + "\">\n");
		sb.append("\t\t\t<tr>\r\n");
		
		index = 0;
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String varName = pair.getKey(); //CodeUtil.getConventionalVariableName(pair.getKey());
			String dType= pair.getValue();	//CodeUtil.getConventionalVariableName(pair.getValue());
			
			sb.append("\t\t\t\t<td>${" + tableInfo.getModelName() + "." + varName + "}</td>\n");
			index++;
		}
		
		sb.append("\t\t\t\t<td class=\"btn-group\">\n"
				+ "\t\t\t\t\t<a class=\"btn btn-sm btn-success\" href=\"${pageContext.request.contextPath}/" + tableInfo.getModelName() + "/${" + tableInfo.getModelName() + "." + CodeUtil.getConventionalVariableName(tableInfo.getColumnList().get(0)) + " }\">View</a>\n");
		sb.append("\t\t\t\t\t<a class=\"btn btn-sm btn-primary\" href=\"${pageContext.request.contextPath}/edit/" + tableInfo.getModelName() + "/${" + tableInfo.getModelName() + "." + CodeUtil.getConventionalVariableName(tableInfo.getColumnList().get(0)) + " }\">Edit</a>\n");
		sb.append("\t\t\t\t\t<a class=\"btn btn-sm btn-danger\" href=\"${pageContext.request.contextPath}/delete/" + tableInfo.getModelName() + "/${" + tableInfo.getModelName() + "." + CodeUtil.getConventionalVariableName(tableInfo.getColumnList().get(0)) + " }\">Remove</a>\n");
		sb.append("\t\t\t\t</td>\n");
		sb.append(""
				+ "\t\t\t</tr>\r\n"
				+ "\t\t</c:forEach>\n"
				+ "\t</tbody>\r\n"
				+ "</table>\n");
		sb.append("");
		return sb.toString();
	}
	
	/* ************************************************************************************************************************
	 * 19-Jul-2022 | DAVID MATU
	 * 
	 * The Methods below will create code that is Dropwizard-centric
	 * 
	 * ************************************************************************************************************************
	 */
	
	/**
	 * Create a model class that has Swagger annotations
	 * Though not complete as at 19-Jul-2022 (I started recently on Swagger, and still not successful for Swagger integration
	 * Aim: to produce a swagger.json file
	 *   
	 * @param modelName
	 * @param groupID (a complete name of the base package e.g. com.dave.apis.<<project_name>>
	 * @param modelAttributeMap
	 * @return
	 */
	public static String createAPIEntity(String modelName, String groupID, Map<String, String> modelAttributeMap) {
		String fin = "";	//Final String
		
		String timestampImport  = "";
		if(modelAttributeMap.containsValue("Timestamp")) {
			timestampImport = "import java.sql.Timestamp;\n";  
		}
		
		if(modelAttributeMap.containsValue("Date")) {
			timestampImport += "import java.util.Date;\n";  
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("package " + groupID + ".core;\n\n");
		sb.append("import java.io.Serializable;\n\n"
				+ timestampImport + "\n"
				+ "import com.fasterxml.jackson.annotation.JsonProperty;\r\n"
				+ "\r\n"
				+ "import io.swagger.annotations.ApiModel;\r\n"
				+ "import io.swagger.annotations.ApiModelProperty;\r\n\r\n");
		
		sb.append("");
		sb.append("@ApiModel(value=\"" + modelName + "\")\n");
		sb.append("public class " + modelName +" implements Serializable {\n");
		//sb.append();	private static final long serialVersionUID = 1L;
		sb.append("\tprivate static final long serialVersionUID = 1L;\n\n");
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t@ApiModelProperty(value=\"" + CodeUtil.capitalize(pair.getKey()) + " of the " + modelName + "\", required = true, name = \"" + pair.getKey() + "\")\n"
					+ "\t@JsonProperty\n");
			sb.append("\tprivate " + pair.getValue() + " " + pair.getKey() + ";");
			sb.append("\n\n");
		}
		
		sb.append("\n\tpublic " + modelName + "() {\n"
				+ "\t\tsuper();\n"
				+ "\t}\n");
		
		//Generate Getters and Setters
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\n\tpublic " + pair.getValue() + " get" + CodeUtil.capitalize(pair.getKey()) + "() {\n" ); //e.g public String getFirstName() { ... }
			sb.append("\t\treturn " + pair.getKey() + ";\n");
			sb.append("\t}\n");
			
			sb.append("\n\tpublic void set" + CodeUtil.capitalize(pair.getKey()) + "(" + pair.getValue() + " " + pair.getKey() + ") {\n" ); //e.g public void setFirstName(String firstName) { ... }
			sb.append("\t\tthis." + pair.getKey() + " = " + pair.getKey() + ";\n"); //this.firstName = firstName;
			sb.append("\t}\n");
		}
		
		sb.append("\n");
		sb.append("}\n");
		
		fin = sb.toString();
		return fin;
	}
	
	/**
	 * Create a Mapper,  that maps SQL resultset to the domain model
	 * 
	 * @param modelName
	 * @param modelAttributeMap
	 * @return
	 */
	public static String createAPIMapper(String modelName, String groupID, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {
		String fin = "";	//Final String
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("");
		sb.append("package " + groupID + ".mappers;\n\n");
		sb.append("import java.sql.ResultSet;\r\n"
				+ "import java.sql.SQLException;\r\n"
				+ "\r\n"
				+ "import org.skife.jdbi.v2.StatementContext;\r\n"
				+ "import org.skife.jdbi.v2.tweak.ResultSetMapper;\r\n"
				+ "\r\n"
				+ "import " + groupID + ".core." + modelName + ";\n\n");
		
		sb.append("public class " + modelName + "Mapper implements ResultSetMapper<" + modelName + "> {\n\n");
		sb.append("\t@Override\r\n"
				+ "\tpublic " + modelName + " map(int index, ResultSet rs, StatementContext ctx) throws SQLException {\n\n");
		
		String fLetter = modelName.substring(0,1).toLowerCase();
		sb.append("\t\t" + modelName + " " + fLetter + " = new " + modelName + "();\n");
		
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t\t" + fLetter +".set" + CodeUtil.capitalize(pair.getKey()) + "(rs.get" + CodeUtil.capitalize(pair.getValue()) + "(\"" + tableMapping.get(pair.getKey().trim()) + "\"));\n");
		}
		sb.append("\t\treturn " + fLetter + ";\n");
		sb.append("\t}\n");
		sb.append("}");
		//sb.append("");
		fin = sb.toString();
		
		return fin;
	}
	
	public static String createAPIEntityDAO(String modelName, String groupID, String tableName, List<String> attributeList, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {
		String fin = "";	//Final String
		StringBuffer sb = new StringBuffer();
		
		sb.append("package " + groupID + ".dao;\n\n");
		sb.append("import java.util.List;\r\n"
				+ "\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.Bind;\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.BindBean;\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.SqlQuery;\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.SqlUpdate;\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;\n\n");
		sb.append("import " + groupID + ".core." + modelName + ";\n");
		sb.append("import " + groupID + ".mappers." + modelName + "Mapper;\n\n");
		sb.append("@RegisterMapper(" + modelName + "Mapper.class)\n");
		sb.append("public interface " + modelName + "DAO {\n\n");
		sb.append("\t@SqlQuery(\"SELECT * FROM " + tableName + "\")\n");
		sb.append("\tabstract List<" + modelName + "> get" + modelName + "s();\n\n");
		
		String supplyID = attributeList.get(0);
		String pkDataType = modelAttributeMap.get(attributeList.get(0));	//primaryKeyDataType 
		
		sb.append("\t@SqlQuery(\"SELECT * FROM " + tableName + " WHERE " + tableMapping.get(attributeList.get(0)).toString() + " = :" + supplyID +"\")\n");
		sb.append("\tpublic " + modelName + " get" + modelName + "(@Bind(\"" + supplyID + "\") final " + pkDataType + " " + supplyID + ");\n\n");
		

		/** **************************************************************************************************************************
		 * Code for Create
		 */
		StringBuilder sbc = new StringBuilder();
		sbc.append("\t@SqlUpdate(\"INSERT INTO " + tableName + "(");
		
		int index = 0;
		for(Map.Entry<String, String> pair : tableMapping.entrySet()) {
			sbc.append(pair.getValue());
			if(index != tableMapping.entrySet().size() - 1) {
				sbc.append(", ");
			} else {
				sbc.append(" ");
			}
			index++;
		}
		
		sbc.append(") values(");
		
		index = 0;
		for(Map.Entry<String, String> pair : tableMapping.entrySet()) {
			sbc.append(":" + CodeUtil.getConventionalVariableName(pair.getValue()));
			if(index != tableMapping.entrySet().size() - 1) {
				sbc.append(", ");
			} else {
				sbc.append(" ");
			}
			index++;
		}
		sbc.append(")\")\n");
		
		sb.append(sbc.toString());
		sb.append("\tvoid create" + modelName + "(@BindBean final " + modelName + " " + CodeUtil.getConventionalVariableName(modelName) + ");\n\n");
		
		/** **************************************************************************************************************************
		 * Code for SQLUpdate
		 */
		StringBuilder sbu = new StringBuilder();
		sbu.append("\t@SqlUpdate(\"UPDATE " + tableName + " SET ");
		
		index = 0;	//Reused variable
		for(Map.Entry<String, String> pair : tableMapping.entrySet()) {
			sbu.append(pair.getValue().toUpperCase() + " = coalesce(:" + CodeUtil.getConventionalVariableName(pair.getValue()) + ", " + pair.getValue().toUpperCase() + ")");		
			if(index != tableMapping.entrySet().size() - 1) {
				sbu.append(", ");
			} else {
				sbu.append(" ");
			}
			index++;
		}
		sbu.append("WHERE " + tableMapping.get(attributeList.get(0)) + " = :" + supplyID + " \")\n");
		
		sb.append(sbu.toString());
		sb.append("\tvoid update" + modelName + "(@BindBean final " + modelName + " " + CodeUtil.getConventionalVariableName(modelName) + ");\n\n");
		
		/** **************************************************************************************************************************
		 * Code for Delete
		 */
		sb.append("\t@SqlUpdate(\"DELETE FROM " + tableName + " WHERE " + tableMapping.get(attributeList.get(0)).toString() + " = :" + supplyID + "\")\n");
		sb.append("\tint delete" + modelName + "(@Bind(\"" + supplyID + "\") final " + pkDataType + " " + supplyID + ");\n\n");
		sb.append("\t/*\r\n"
				+ "\t* Custom SQL Functions to generate unique  IDs\r\n"
				+ "\t*/\n");
		sb.append("\t@SqlQuery(\"select genProductID();\")\r\n"
				+ "\tpublic long getUniqueProductId();\r\n"
				+ "\n"
				+ "\t@SqlQuery(\"select genUserID();\")\r\n"
				+ "\tpublic long getUniqueUserId();\n"
				+ "\n"
				+ "\t@SqlQuery(\"select genUserID();\")\r\n"
				+ "\t\tpublic int getUniqueItemID();\n");
		sb.append("\t}");
		sb.append("");
		
		fin = sb.toString();		
		return fin;
	}
	
	/**
	 * Create an EntityService
	 * 
	 * @param modelName
	 * @param attributeList
	 * @param modelAttributeMap
	 * @return the generated code
	 */
	public static String createAPIEntityService(String modelName, String groupID, List<String> attributeList, Map<String, String> modelAttributeMap) {
		String fin = "";
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + groupID + ".service;\n\n");
		sb.append("import java.util.List;\r\n"
				+ "import java.util.Objects;\r\n"
				+ "\r\n"
				+ "import javax.ws.rs.WebApplicationException;\r\n"
				+ "import javax.ws.rs.core.Response.Status;\r\n"
				+ "\r\n"
				+ "import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;\r\n"
				+ "import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.CreateSqlObject;\r\n"
				+ "\r\n"
				+ "import org.slf4j.Logger;\r\n"
				+ "import org.slf4j.LoggerFactory;\r\n"
				+ "\r\n"
				+ "import " + groupID + ".core." + modelName + ";\r\n"
				+ "import " + groupID + ".dao." + modelName + "DAO;\r\n"
				+ "\r\n"
				+ "import " + groupID + ".util.AppConstants;\r\n");
		
		sb.append("public abstract class " + modelName + "Service {\n");
		sb.append("\tprivate static final Logger LOGGER = LoggerFactory.getLogger(" + modelName +  "Service.class);\n\n");
		sb.append("\t@CreateSqlObject\n"
				+ "\tabstract " + modelName + "DAO " + CodeUtil.getConventionalVariableName(modelName) + "DAO();\n\n");
		
		/*******************************************************
		 * CodeBlock: Create an Entity
		 */
		String entityAsVariable = CodeUtil.getConventionalVariableName(modelName);
		String fLetter = modelName.substring(0,1).toLowerCase();
		String supplyID = attributeList.get(0);
		String pkDataType = modelAttributeMap.get(attributeList.get(0));	//primaryKeyDataType 
		
		sb.append("\tpublic " + modelName + " create" + modelName + "(" + modelName + " " + entityAsVariable + ") {\n");
		sb.append("\t\tLOGGER.info(\"Creating " + CodeUtil.capitalize(modelName) + " ::::: \\n\" + " + entityAsVariable + ".toString());\n\n");		
		sb.append("\t\t" + modelName + " " + fLetter + " = " + entityAsVariable + ";\n\n");
		sb.append("\t\t" + fLetter + ".set" + CodeUtil.capitalize(attributeList.get(0)) + "(" + CodeUtil.getConventionalVariableName(modelName) + "DAO().getUniqueItemID());\n");	//There's a SQL function to get unique IDs, change as appropriate
		sb.append("\t\t" + CodeUtil.getConventionalVariableName(modelName) + "DAO().create" + modelName + "(" + fLetter + ");\n");
		sb.append("\t\treturn " + CodeUtil.getConventionalVariableName(modelName) + "DAO().get" + modelName + "(" + fLetter + ".get" + CodeUtil.capitalize(attributeList.get(0)) + "());\n"); //return employeeDAO().getEmployee(e.getId());
		sb.append("\t}\n\n");
		
		sb.append("\t//Read a Single " + modelName + "\n");		
		/*******************************************************
		 * CodeBlock: READ an Entity
		 */
		sb.append("\tpublic " + modelName + " get" + modelName + "(" + pkDataType + " " + supplyID + ") {\n");
		sb.append("\t\tLOGGER.info(\"Retrieving " + CodeUtil.capitalize(modelName) + " ::::: supplied ID: \" + " + supplyID + ");\n\n");		
		sb.append("\t\t" + modelName + " " + fLetter + " = " + entityAsVariable + "DAO().get" + modelName + "(" + supplyID + ");\n\n");
		sb.append("\t\tif(Objects.isNull(" + fLetter + ")) {\n");
		sb.append("\t\t\tthrow new WebApplicationException(String.format(AppConstants.ENTITY_NOT_FOUND.toString(), " + supplyID + "), Status.NOT_FOUND);\n");
		sb.append("\t\t}\n\n");
		sb.append("\t\treturn " + fLetter + ";\n");
		sb.append("\t}\n\n");
		sb.append("");
		
		
		/*******************************************************
		 * CodeBlock: FETCH LIST of Entity(ies)
		 */
		sb.append("\tpublic List<" + CodeUtil.capitalize(modelName) + "> get" + CodeUtil.capitalize(modelName) + "s() {\n");
		sb.append("\t\tLOGGER.info(\"Retrieving all " + CodeUtil.capitalize(modelName) + "s\");\n\n");
		sb.append("\t\treturn " + entityAsVariable + "DAO().get" + CodeUtil.capitalize(modelName) + "s();\n");
		sb.append("\t}\n\n");
		
		/*******************************************************
		 * CodeBlock: UPDATE (EDIT) ENTITY
		 */
		sb.append("\t//Update " + modelName + "\n");
		sb.append("\tpublic " + CodeUtil.capitalize(modelName) + " edit" + CodeUtil.capitalize(modelName) + "(" +  CodeUtil.capitalize(modelName) + " " + CodeUtil.getConventionalVariableName(modelName) + " ) {\n");	//public Employee editEmployee(Employee e) {
		sb.append("\t\tif(Objects.isNull(" + CodeUtil.getConventionalVariableName(modelName) + "DAO().get" + CodeUtil.capitalize(modelName) + "(" + entityAsVariable + ".get" + CodeUtil.capitalize(supplyID) + "()))) {\n");	//if(Objects.isNull(employeeDAO().getEmployee(e.getId()))) {
		sb.append("\t\t\tthrow new WebApplicationException(String.format(AppConstants.ENTITY_NOT_FOUND.toString(), Status.NOT_FOUND));\n");
		sb.append("\t\t}\n");
		sb.append("\t\t" + CodeUtil.getConventionalVariableName(modelName) +"DAO().update" + CodeUtil.capitalize(modelName) + "(" + CodeUtil.getConventionalVariableName(modelName) + ");\n");
		sb.append("\t\treturn " + CodeUtil.getConventionalVariableName(modelName) + "DAO().get" + CodeUtil.capitalize(modelName) + "(" + CodeUtil.getConventionalVariableName(modelName) + ".get" + CodeUtil.capitalize(supplyID) + "());\n");
		sb.append("\t}\n\n");
		sb.append("");
		
		
		/*******************************************************
		 * CodeBlock: DELETE ENTITY
		 */
		sb.append("\t//DELETE " + modelName + "\n");
		sb.append("\tpublic String delete" + CodeUtil.capitalize(modelName) + "(final " + pkDataType + " " + supplyID + ") {\n");
		sb.append("\t\tint result = " + entityAsVariable + "DAO().delete" + modelName + "(" + supplyID + ");\n");
		sb.append("\t\tLOGGER.info(\"Results after attempting delete via " + modelName + "Service.delete" + modelName + "(): \", result);\n\n");
		sb.append("\t\tswitch(result) {\r\n"
				+ "\t\tcase 1:\r\n"
				+ "\t\t\treturn AppConstants.SUCCESS.toString();\r\n"
				+ "\t\tcase 0:\r\n"
				+ "\t\t\tthrow new WebApplicationException(String.format(AppConstants.ENTITY_NOT_FOUND.toString(), Status.NOT_FOUND));\r\n"
				+ "    		\r\n"
				+ "\t\t\tdefault:\r\n"
				+ "\t\t\t\tthrow new WebApplicationException(String.format(AppConstants.UNEXPECTED_DELETE_ERROR.toString(), Status.INTERNAL_SERVER_ERROR));\r\n"
				+ "\t\t}\n");
		sb.append("\t}\n\n");
		
		/*******************************************************
		 * CodeBlock: CHECK PERFORMANCE
		 * Copied as it is!
		 */
		sb.append("\tpublic String performHealthCheck() {\r\n"
				+ "    	try {\r\n"
				+ "    		" + entityAsVariable + "DAO().get" + modelName + "s();\r\n"
				+ "    	} catch(UnableToObtainConnectionException ex) {\r\n"
				+ "    		return AppService.checkUnableToObtainConnectionException(ex);\r\n"
				+ "    	} catch(UnableToExecuteStatementException ex) {\r\n"
				+ "    		return AppService.checkUnableToExecuteStatementException(ex);\r\n"
				+ "    	}\r\n"
				+ "    	return null;\r\n"
				+ "    }\n\n");
		sb.append("}");
		//sb.append("");
		
		fin = sb.toString();		
		return fin;
	}


	public static String createAPIEntityResource(String modelName, String groupID, List<String> attributeList, Map<String, String> modelAttributeMap, Map<String, String> tableMapping) {
		String fin = "";
		StringBuilder sb = new StringBuilder();
		
		//Context Variables
		String entityAsVariable = CodeUtil.getConventionalVariableName(modelName);
		String fLetter = modelName.substring(0,1).toLowerCase();
		String supplyID = attributeList.get(0);
		String pkDataType = modelAttributeMap.get(attributeList.get(0));	//primaryKeyDataType 
		//End of Context variables
		
		sb.append("package " + groupID + ".resources;\r\n"
				+ "\r\n"
				+ "import java.util.HashMap;\r\n"
				+ "import java.util.Map;\r\n"
				+ "\r\n"
				+ "import javax.validation.Valid;\r\n"
				+ "import javax.validation.constraints.NotNull;\r\n"
				+ "import javax.ws.rs.DELETE;\r\n"
				+ "import javax.ws.rs.GET;\r\n"
				+ "import javax.ws.rs.POST;\r\n"
				+ "import javax.ws.rs.PUT;\r\n"
				+ "import javax.ws.rs.Path;\r\n"
				+ "import javax.ws.rs.PathParam;\r\n"
				+ "import javax.ws.rs.Produces;\r\n"
				+ "import javax.ws.rs.core.MediaType;\r\n"
				+ "import javax.ws.rs.core.Response;\r\n"
				+ "\r\n"
				+ "import com.codahale.metrics.annotation.Timed;\r\n"
				+ "import " + groupID + ".core." + modelName + ";\r\n"
				+ "import " + groupID + ".service." + modelName + "Service;\r\n"
				+ "\r\n"
				+ "import io.swagger.annotations.Api;\n\n");
		sb.append("@Path(\"/" + CodeUtil.getConventionalVariableName(modelName) + "\")\r\n"
				+ "@Produces(MediaType.APPLICATION_JSON)\r\n"
				+ "@Api(value=\"" + modelName + " endpoint\")\n");
		sb.append("public class " + modelName + "Resource {\n\n");
		sb.append("\tprivate final " + modelName + "Service " + entityAsVariable + "Service;\n\n");
		sb.append("\tpublic " + modelName + "Resource(" + modelName + "Service " + CodeUtil.getConventionalVariableName(modelName) +  "Service) {\r\n"	//public EmployeeResource(EmployeeService es) {
				+ "\t\tthis." + entityAsVariable + "Service = " + entityAsVariable + "Service;\r\n"
				+ "\t}\n\n");	//Constructor
		
		/** ***************
		 * Create an Entity
		 */
		sb.append("\t@POST\r\n"
				+ "\t@Timed\n");
		sb.append("\tpublic Response create" + modelName + "(@NotNull @Valid final " + modelName + " " + entityAsVariable + ") {\n");	//public Response createEmployee(@NotNull @Valid final Employee employee) {
		sb.append("\t\t" + modelName + " " + fLetter + " = new " + modelName + "();\n");
		//m.setFirstName(rs.getString("FIRST_NAME_COLUMN"));
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t\t" + fLetter +".set" + CodeUtil.capitalize(pair.getKey()) + "(" + entityAsVariable + ".get" + CodeUtil.capitalize(pair.getKey()) + "());\n");
		}
		sb.append("\n");
		sb.append("\t\treturn Response.ok(" + entityAsVariable + "Service.create" + modelName + "(" + fLetter + ")).build();\n");
		sb.append("\t}\n\n");
		
		/** ***************
		 * GET A SINGLE ENTITY
		 */
		sb.append("\t@GET\r\n"
				+ "\t@Timed\r\n"
				+ "\t@Path(\"{id}\")\n");
		sb.append("\tpublic Response get" + modelName + "(@PathParam(\"id\") final " + pkDataType + " id) {\n");
		sb.append("\t\treturn Response.ok(" + entityAsVariable + "Service.get" + modelName + "(id)).build();\n");
		sb.append("\t}\n\n");
		
		/** ***************
		 * GET A LIST OF ENTITY
		 */
		sb.append("\t@GET\r\n"
				+ "\t@Timed\n");
		sb.append("\tpublic Response get" + modelName + "s() {\r\n"
				+ "\t\treturn Response.ok(" + entityAsVariable + "Service.get" + modelName + "s()).build();\r\n"
				+ "	\t}\n\n");
		
		/** ***************
		 * UPDATE ENTITY
		 */
		sb.append("\t@PUT\r\n"
				+ "\t@Timed\r\n"
				+ "\t@Path(\"{id}\")\n");
		sb.append("\tpublic Response edit" + modelName + "(@NotNull @Valid final " + modelName + " " + entityAsVariable + " , @PathParam(\"id\") final " + pkDataType + " id) {\n");	//public Response editEmployee(@NotNull @Valid final Employee employee, @PathParam("id") final int id) {
		sb.append("\t\t" + entityAsVariable +  ".set" + CodeUtil.capitalize(supplyID) + "(id);\n\n");
		sb.append("\t\treturn Response.ok(" + entityAsVariable + "Service.edit" + modelName + "(" + entityAsVariable + ")).build();\n\n");
		sb.append("\t}\n\n");
		
		/** ***************
		 * DELETE ENTITY
		 */
		sb.append("\t@DELETE\r\n"
				+ "\t@Timed\r\n"
				+ "\t@Path(\"{id}\")\n");
		sb.append("\tpublic Response delete" + modelName + "(@PathParam(\"id\") final int id) {\n");
		sb.append("\t\tMap<String, String> response = new HashMap<>();\r\n"
				+ "\t\tresponse.put(\"status\", " + entityAsVariable + "Service.delete" + modelName + "(id));\n");
		sb.append("\t\treturn Response.ok(response).build();\n");
		sb.append("\t}\n");
		sb.append("}");
		
		fin = sb.toString();		
		return fin;
	}
	
	/**
	 * This can be done better by reading files instead of pasting entire lines of Strings here
	 * @param projectName
	 * @param projectDB
	 * @param groupID
	 * @param port
	 * @return config.yml content
	 */
	public static String generateConfigFile(String projectName, String projectDB, String groupID, int port) {
		String fin = "";
		StringBuilder sb = new StringBuilder();
		
		sb.append("logging:\r\n"
				+ "  level: INFO\r\n"
				+ "  \r\n"
				+ "  appenders:\r\n"
				+ "    - type: console\r\n"
				+ "      threshold: ALL\r\n"
				+ "      timeZone: EAT\r\n"
				+ "#  loggers:\r\n"
				+ "#    " + groupID + ": DEBUG\r\n"
				+ "\r\n"
				+ "server:\r\n"
				+ "  type: simple\r\n"
				+ "  applicationContextPath: /" + projectName + "\r\n"
				+ "  adminContextPath: /" + projectName + "/admin\r\n"
				+ "  connector: \r\n"
				+ "    port: " + port + "\r\n"
				+ "    type: http\r\n"
				+ "    \r\n"
				+ "database:\r\n"
				+ "  driverClass: com.mysql.cj.jdbc.Driver\r\n"
				+ "  url: jdbc:mysql://localhost:3306/" + projectDB + "?autoReconnect=true&useSSL=false\r\n"
				+ "  user: root\r\n"
				+ "  password: root \r\n"
				+ "  maxWaitForConnection: 1s\r\n"
				+ "  validationQuery: \"SELECT 1\"\r\n"
				+ "  validationQueryTimeout: 2s\r\n"
				+ "  minSize: 8\r\n"
				+ "  maxSize: 32\r\n"
				+ "  checkConnectionWhileIdle: false\r\n"
				+ "  evictionInterval: 10s\r\n"
				+ "  minIdleTime: 1 minute\r\n"
				+ "  checkConnectionOnBorrow: true\r\n"
				+ "  \r\n"
				+ "swagger:\r\n"
				+ "  resourcePackage: " + groupID + "." + "resources");
		
		fin = sb.toString();		
		return fin;
	}
	
	public static String generatePOM(String projectName, String groupID) {
		String fin = "";
		StringBuilder sb = new StringBuilder();
		
		String projectTitle = CodeUtil.capitalize(projectName);
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<project\r\n"
				+ "        xmlns=\"http://maven.apache.org/POM/4.0.0\"\r\n"
				+ "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
				+ "        xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\r\n"
				+ "\r\n"
				+ "    <modelVersion>4.0.0</modelVersion>\r\n"
				+ "\r\n"
				+ "    <groupId>" + groupID + "</groupId>\r\n"
				+ "    <artifactId>" + projectName + "</artifactId>\r\n"
				+ "    <version>0.0.1-SNAPSHOT</version>\r\n"
				+ "    <packaging>jar</packaging>\r\n"
				+ "\r\n"
				+ "    <name>" + projectTitle + "API</name>\r\n"
				+ "    <description>" + projectTitle + " project. "
						+ "Auto-Generated on " + new Date(System.currentTimeMillis()) 
						+ " by Dave's CodeGen for Dropwizard API Apps (https://github.com/david-matu/)</description>\r\n"
				+ "\r\n"
				+ "    <properties>\r\n"
				+ "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\r\n"
				+ "        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>\r\n"
				+ "        <dropwizard.version>2.1.0</dropwizard.version>\r\n"
				+ "        <dropwizard-jdbi.version>2.0.0-rc9</dropwizard-jdbi.version>\r\n"
				+ "        <mysql-connector.version>8.0.29</mysql-connector.version>\r\n"
				+ "        <smoketurner-swagger.version>2.0.12-1</smoketurner-swagger.version>\r\n"
				+ "        <mainClass>" + groupID + "." + projectTitle + "Application</mainClass>\r\n"
				+ "    </properties>\r\n"
				+ "\r\n"
				+ "    <dependencyManagement>\r\n"
				+ "        <dependencies>\r\n"
				+ "            <dependency>\r\n"
				+ "                <groupId>io.dropwizard</groupId>\r\n"
				+ "                <artifactId>dropwizard-dependencies</artifactId>\r\n"
				+ "                <version>${dropwizard.version}</version>\r\n"
				+ "                <type>pom</type>\r\n"
				+ "                <scope>import</scope>\r\n"
				+ "            </dependency>\r\n"
				+ "        </dependencies>\r\n"
				+ "    </dependencyManagement>\r\n"
				+ "\r\n"
				+ "    <dependencies>\r\n"
				+ "        <dependency>\r\n"
				+ "            <groupId>io.dropwizard</groupId>\r\n"
				+ "            <artifactId>dropwizard-core</artifactId>\r\n"
				+ "        </dependency>\r\n"
				+ "        <dependency>\r\n"
				+ "		    <groupId>io.dropwizard</groupId>\r\n"
				+ "		    <artifactId>dropwizard-jdbi</artifactId>\r\n"
				+ "		    <version>${dropwizard-jdbi.version}</version>\r\n"
				+ "		</dependency>\r\n"
				+ "        <dependency>\r\n"
				+ "            <groupId>com.fasterxml.jackson.core</groupId>\r\n"
				+ "            <artifactId>jackson-annotations</artifactId>\r\n"
				+ "        </dependency>\r\n"
				+ "        <dependency>\r\n"
				+ "            <groupId>jakarta.validation</groupId>\r\n"
				+ "            <artifactId>jakarta.validation-api</artifactId>\r\n"
				+ "        </dependency>\r\n"
				+ "        <dependency>\r\n"
				+ "            <groupId>org.hibernate.validator</groupId>\r\n"
				+ "            <artifactId>hibernate-validator</artifactId>\r\n"
				+ "        </dependency>\r\n"
				+ "        \r\n"
				+ "        <dependency>\r\n"
				+ "		    <groupId>mysql</groupId>\r\n"
				+ "		    <artifactId>mysql-connector-java</artifactId>\r\n"
				+ "		    <version>${mysql-connector.version}</version>\r\n"
				+ "		</dependency>\r\n"
				+ "		\r\n"
				+ "		<dependency>\r\n"
				+ "		    <groupId>com.smoketurner</groupId>\r\n"
				+ "		    <artifactId>dropwizard-swagger</artifactId>\r\n"
				+ "		    <version>${smoketurner-swagger.version}</version>\r\n"
				+ "		</dependency>\r\n"
				+ "		\r\n"
				+ "		<!-- <dependency>\r\n"
				+ "		    <groupId>org.springframework.batch</groupId>\r\n"
				+ "		    <artifactId>spring-batch-core</artifactId>\r\n"
				+ "		    <version>4.3.6</version>\r\n"
				+ "		    <exclusions>\r\n"
				+ "		        <exclusion>\r\n"
				+ "		            <groupId>org.springframework</groupId>\r\n"
				+ "		            <artifactId>spring-beans</artifactId>\r\n"
				+ "		        </exclusion>\r\n"
				+ "		    </exclusions>\r\n"
				+ "		</dependency> -->\r\n"
				+ "\r\n"
				+ "    </dependencies>\r\n"
				+ "\r\n"
				+ "    <build>\r\n"
				+ "        <plugins>\r\n"
				+ "            <plugin>\r\n"
				+ "                <artifactId>maven-shade-plugin</artifactId>\r\n"
				+ "                <version>3.3.0</version>\r\n"
				+ "                <configuration>\r\n"
				+ "                    <createDependencyReducedPom>true</createDependencyReducedPom>\r\n"
				+ "                    <transformers>\r\n"
				+ "                        <transformer implementation=\"org.apache.maven.plugins.shade.resource.ServicesResourceTransformer\"/>\r\n"
				+ "                        <transformer implementation=\"org.apache.maven.plugins.shade.resource.ManifestResourceTransformer\">\r\n"
				+ "                            <mainClass>${mainClass}</mainClass>\r\n"
				+ "                        </transformer>\r\n"
				+ "                    </transformers>\r\n"
				+ "                    <!-- exclude signed Manifests -->\r\n"
				+ "                    <filters>\r\n"
				+ "                        <filter>\r\n"
				+ "                            <artifact>*:*</artifact>\r\n"
				+ "                            <excludes>\r\n"
				+ "                                <exclude>META-INF/*.SF</exclude>\r\n"
				+ "                                <exclude>META-INF/*.DSA</exclude>\r\n"
				+ "                                <exclude>META-INF/*.RSA</exclude>\r\n"
				+ "                            </excludes>\r\n"
				+ "                        </filter>\r\n"
				+ "                    </filters>\r\n"
				+ "                </configuration>\r\n"
				+ "                <executions>\r\n"
				+ "                    <execution>\r\n"
				+ "                        <phase>package</phase>\r\n"
				+ "                        <goals>\r\n"
				+ "                            <goal>shade</goal>\r\n"
				+ "                        </goals>\r\n"
				+ "                    </execution>\r\n"
				+ "                </executions>\r\n"
				+ "            </plugin>\r\n"
				+ "            <plugin>\r\n"
				+ "                <artifactId>maven-jar-plugin</artifactId>\r\n"
				+ "                <version>3.2.2</version>\r\n"
				+ "                <configuration>\r\n"
				+ "                    <archive>\r\n"
				+ "                        <manifest>\r\n"
				+ "                            <addClasspath>true</addClasspath>\r\n"
				+ "                            <mainClass>${mainClass}</mainClass>\r\n"
				+ "                        </manifest>\r\n"
				+ "                    </archive>\r\n"
				+ "                </configuration>\r\n"
				+ "            </plugin>\r\n"
				+ "            <plugin>\r\n"
				+ "                <artifactId>maven-compiler-plugin</artifactId>\r\n"
				+ "                <version>3.10.1</version>\r\n"
				+ "                <configuration>\r\n"
				+ "                    <source>1.8</source>\r\n"
				+ "                    <target>1.8</target>\r\n"
				+ "                </configuration>\r\n"
				+ "            </plugin>\r\n"
				+ "            <plugin>\r\n"
				+ "                <artifactId>maven-surefire-plugin</artifactId>\r\n"
				+ "                <version>2.22.2</version>\r\n"
				+ "            </plugin>\r\n"
				+ "            <plugin>\r\n"
				+ "                <artifactId>maven-source-plugin</artifactId>\r\n"
				+ "                <version>3.2.1</version>\r\n"
				+ "                <executions>\r\n"
				+ "                    <execution>\r\n"
				+ "                        <id>attach-sources</id>\r\n"
				+ "                        <goals>\r\n"
				+ "                            <goal>jar</goal>\r\n"
				+ "                        </goals>\r\n"
				+ "                    </execution>\r\n"
				+ "                </executions>\r\n"
				+ "            </plugin>\r\n"
				+ "            <plugin>\r\n"
				+ "                <artifactId>maven-javadoc-plugin</artifactId>\r\n"
				+ "                <version>3.4.0</version>\r\n"
				+ "                <executions>\r\n"
				+ "                    <execution>\r\n"
				+ "                        <id>attach-javadocs</id>\r\n"
				+ "                        <goals>\r\n"
				+ "                            <goal>jar</goal>\r\n"
				+ "                        </goals>\r\n"
				+ "                    </execution>\r\n"
				+ "                </executions>\r\n"
				+ "            </plugin>\r\n"
				+ "            \r\n"
				+ "            <!-- It is said that this will generate a swagger.json file  | | Source: https://copyprogramming.com/howto/swagger-integration-into-dropwizard\r\n"
				+ "            <plugin>\r\n"
				+ "            			    <groupId>com.github.kongchen</groupId>\r\n"
				+ "            			    <artifactId>swagger-maven-plugin</artifactId>\r\n"
				+ "                <version>3.1.8</version>\r\n"
				+ "                <configuration>\r\n"
				+ "                    <apiSources>\r\n"
				+ "                        <apiSource>\r\n"
				+ "                            <springmvc>false</springmvc>\r\n"
				+ "                            <locations>\r\n"
				+ "                                <location>me.pabloestrada.api.*</location>\r\n"
				+ "                            </locations>\r\n"
				+ "                            <schemes>http,https</schemes>\r\n"
				+ "                            <outputFormats>json</outputFormats>\r\n"
				+ "                            <basePath>/</basePath>\r\n"
				+ "                            <host>127.0.0.1:8000</host>\r\n"
				+ "                            <info>\r\n"
				+ "                                <title>APIs</title>\r\n"
				+ "                                <version>1.0.0</version>\r\n"
				+ "                                <description>APIs</description>\r\n"
				+ "                                <contact>\r\n"
				+ "                                    <email>support@stackoverflow.com</email>\r\n"
				+ "                                    <name>Support</name>\r\n"
				+ "                                </contact>\r\n"
				+ "                                <license>\r\n"
				+ "                                    <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>\r\n"
				+ "                                    <name>Apache 2.0</name>\r\n"
				+ "                                </license>\r\n"
				+ "                            </info>\r\n"
				+ "                            <swaggerDirectory>swagger</swaggerDirectory>\r\n"
				+ "                            <swaggerFileName>swaggerspec</swaggerFileName>\r\n"
				+ "                        </apiSource>\r\n"
				+ "                    </apiSources>\r\n"
				+ "                </configuration>\r\n"
				+ "                <executions>\r\n"
				+ "                    <execution>\r\n"
				+ "                        <phase>compile</phase>\r\n"
				+ "                        <goals>\r\n"
				+ "                            <goal>generate</goal>\r\n"
				+ "                        </goals>\r\n"
				+ "                    </execution>\r\n"
				+ "                </executions>\r\n"
				+ "            </plugin> -->\r\n"
				+ "        </plugins>\r\n"
				+ "    </build>\r\n"
				+ "\r\n"
				+ "    <reporting>\r\n"
				+ "        <plugins>\r\n"
				+ "            <plugin>\r\n"
				+ "                <artifactId>maven-project-info-reports-plugin</artifactId>\r\n"
				+ "                <version>3.3.0</version>\r\n"
				+ "                <configuration>\r\n"
				+ "                    <dependencyLocationsEnabled>false</dependencyLocationsEnabled>\r\n"
				+ "                    <dependencyDetailsEnabled>false</dependencyDetailsEnabled>\r\n"
				+ "                </configuration>\r\n"
				+ "            </plugin>\r\n"
				+ "            <plugin>\r\n"
				+ "                <artifactId>maven-javadoc-plugin</artifactId>\r\n"
				+ "                <version>3.4.0</version>\r\n"
				+ "            </plugin>\r\n"
				+ "        </plugins>\r\n"
				+ "    </reporting>\r\n"
				+ "    <profiles>\r\n"
				+ "        <profile>\r\n"
				+ "            <id>java11+</id>\r\n"
				+ "            <activation>\r\n"
				+ "                <jdk>[11,)</jdk>\r\n"
				+ "            </activation>\r\n"
				+ "            <properties>\r\n"
				+ "                <!--\r\n"
				+ "                Workaround for \"javadoc: error - The code being documented uses modules but the packages\r\n"
				+ "                defined in https://docs.oracle.com/javase/8/docs/api/ are in the unnamed module.\"\r\n"
				+ "                -->\r\n"
				+ "                <maven.javadoc.skip>true</maven.javadoc.skip>\r\n"
				+ "            </properties>\r\n"
				+ "        </profile>\r\n"
				+ "    </profiles>\r\n"
				+ "</project>\r\n"
				+ "");
		
		fin = sb.toString();		
		return fin;
	}
	
	public static String generateMainClass(String projectName, String groupID, List<Model> models) {
		String fin = "";
		StringBuilder sb = new StringBuilder();
		
		/**
		 * String for Register Resources 
		 * 	 env.jersey().register(new CustomerResource(dbi.onDemand(CustomerService.class)));
		 */
		StringBuilder rsList = new StringBuilder();
		
		rsList.append("\n");
		for(Model m : models) {
			rsList.append("\t\tenv.jersey().register(new " + m.getEntityName() + "Resource(dbi.onDemand(" + m.getEntityName() + "Service.class)));\n");
		}
		
		StringBuilder importList = new StringBuilder();
		importList.append("\n");
		for(Model m : models) {
			importList.append("import " + groupID +".resources." + m.getEntityName() + "Resource;\n");
		}
		
		importList.append("\nimport " + groupID +".service.AppService;\n");
		for(Model m : models) {
			importList.append("import " + groupID +".service." + m.getEntityName() + "Service;\n");
		}
		
		String projectTitle = CodeUtil.capitalize(projectName);
		
		sb.append("package " + groupID + ";\r\n"
				+ "\r\n"
				+ "import javax.sql.DataSource;\r\n"
				+ "\r\n"
				+ "import org.skife.jdbi.v2.DBI;\r\n"
				+ "\r\n"
				+ "import " + groupID + ".health.AppHealthCheck;\r\n"
				+ importList
				+ "\r\n"
				+ "import io.dropwizard.Application;\r\n"
				+ "import io.dropwizard.setup.Bootstrap;\r\n"
				+ "import io.dropwizard.setup.Environment;\r\n"
				+ "import io.federecio.dropwizard.swagger.SwaggerBundle;\r\n"
				+ "import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;\r\n"
				+ "\r\n"
				+ "public class " + projectTitle + "Application extends Application<" + projectTitle + "Configuration> {\r\n"
				+ "\r\n"
				+ "    public static void main(final String[] args) throws Exception {\r\n"
				+ "        new " + projectTitle + "Application().run(args);\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    @Override\r\n"
				+ "    public String getName() {\r\n"
				+ "        return \"" + projectTitle + "API\";\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    @Override\r\n"
				+ "    public void initialize(final Bootstrap<" + projectTitle + "Configuration> bootstrap) {\r\n"
				+ "    	bootstrap.addBundle(new SwaggerBundle<" + projectTitle + "Configuration>() {\r\n"
				+ "			@Override\r\n"
				+ "			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(" + projectTitle + "Configuration config) {\r\n"
				+ "				return config.swaggerBundleConfiguration;\r\n"
				+ "			}\r\n"
				+ "        });\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    @Override\r\n"
				+ "    public void run(final " + projectTitle + "Configuration config, final Environment env) {\r\n"
				+ "        final DataSource dataSource = config.getDataSourceFactory().build(env.metrics(), getName());\r\n"
				+ "        \r\n"
				+ "        DBI dbi = new DBI(dataSource);\r\n"
				+ "        \r\n"
				+ "        AppHealthCheck healthCheck = new AppHealthCheck(dbi.onDemand(AppService.class));\r\n"
				+ "        env.healthChecks().register(getName(), healthCheck);\r\n"
				+ "\r\n"
				+ "\t\tenv.jersey().register(getConfigurationClass());\r\n"
				+ "\t\t" + rsList + "\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "}\r\n"
				+ "");
		
		fin = sb.toString();		
		return fin;
	}
	
	/*
	 * Generate Configuration Class
	 */
	public static String generateConfigurationClass(String projectName, String groupID) {
		String fin = "";
		StringBuilder sb = new StringBuilder();
		
		String projectTitle = CodeUtil.capitalize(projectName);
		
		sb.append("package " + groupID + ";\r\n"
				+ "\r\n"
				+ "import io.dropwizard.Configuration;\r\n"
				+ "import io.dropwizard.db.DataSourceFactory;\r\n"
				+ "import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;\r\n"
				+ "\r\n"
				+ "import com.fasterxml.jackson.annotation.JsonProperty;\r\n"
				+ "import org.hibernate.validator.constraints.*;\r\n"
				+ "\r\n"
				+ "import javax.validation.Valid;\r\n"
				+ "import javax.validation.constraints.*;\r\n"
				+ "\r\n"
				+ "public class " + projectTitle + "Configuration extends Configuration {\r\n"
				+ "    @Valid\r\n"
				+ "    @NotNull\r\n"
				+ "    @JsonProperty(\"database\")\r\n"
				+ "    private DataSourceFactory dataSourceFactory = new DataSourceFactory();\r\n"
				+ "    \r\n"
				+ "    @JsonProperty(\"swagger\")\r\n"
				+ "    public SwaggerBundleConfiguration swaggerBundleConfiguration;\r\n"
				+ "\r\n"
				+ "    @JsonProperty(\"database\")\r\n"
				+ "    public DataSourceFactory getDataSourceFactory() {\r\n"
				+ "		return dataSourceFactory;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "    @JsonProperty(\"database\")\r\n"
				+ "	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {\r\n"
				+ "		this.dataSourceFactory = dataSourceFactory;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "    @JsonProperty(\"swagger\")\r\n"
				+ "	public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {\r\n"
				+ "		return swaggerBundleConfiguration;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "    @JsonProperty(\"swagger\")\r\n"
				+ "	public void setSwaggerBundleConfiguration(SwaggerBundleConfiguration swaggerBundleConfiguration) {\r\n"
				+ "		this.swaggerBundleConfiguration = swaggerBundleConfiguration;\r\n"
				+ "	}   \r\n"
				+ "}");
		
		fin = sb.toString();		
		return fin;
	}
	
	public static String generateAppModel(String basePackage) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + basePackage + ".core;\r\n"
				+ "\r\n"
				+ "import com.fasterxml.jackson.annotation.JsonProperty;\r\n"
				+ "\r\n"
				+ "import io.swagger.annotations.ApiModel;\r\n"
				+ "import io.swagger.annotations.ApiModelProperty;\r\n"
				+ "\r\n"
				+ "@ApiModel(value=\"App\")\r\n"
				+ "public class App {\r\n"
				+ "	\r\n"
				+ "	@ApiModelProperty(value=\"This is a result of database check\", required = false, name = \"db_result\")\r\n"
				+ "	@JsonProperty\r\n"
				+ "	private int databaseResult;\r\n"
				+ "\r\n"
				+ "	public App() {\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	public int getDatabaseResult() {\r\n"
				+ "		return databaseResult;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	public void setDatabaseResult(int databaseResult) {\r\n"
				+ "		this.databaseResult = databaseResult;\r\n"
				+ "	}	\r\n"
				+ "}\r\n"
				+ "");
		
		return sb.toString();
	}
	
	public static String generateAppMapper(String basePackage) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + basePackage + ".mappers;\r\n"
				+ "\r\n"
				+ "import java.sql.ResultSet;\r\n"
				+ "import java.sql.SQLException;\r\n"
				+ "\r\n"
				+ "import org.skife.jdbi.v2.StatementContext;\r\n"
				+ "import org.skife.jdbi.v2.tweak.ResultSetMapper;\r\n"
				+ "\r\n"
				+ "import " + basePackage + ".core.App;\r\n"
				+ "\r\n"
				+ "public class AppMapper implements ResultSetMapper<App> {\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	public App map(int index, ResultSet rs, StatementContext ctx) throws SQLException {\r\n"
				+ "		App c = new App();		\r\n"
				+ "		c.setDatabaseResult(rs.getInt(1));		\r\n"
				+ "		return c;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "");
		
		return sb.toString();
	}
	
	public static String generateAppDAO(String basePackage) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + basePackage + ".dao;\r\n"
				+ "\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.SqlQuery;\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;\r\n"
				+ "\r\n"
				+ "import " + basePackage + ".mappers.AppMapper;\r\n"
				+ "\r\n"
				+ "@RegisterMapper(AppMapper.class)\r\n"
				+ "public interface AppDAO {\r\n"
				+ "\r\n"
				+ "	@SqlQuery(\"SELECT 1\")\r\n"
				+ "	int testDBHealth();\r\n"
				+ "}");
		
		return sb.toString();
	}
	
	/**
	 * Reside in .service
	 * @return
	 */
	public static String generateAppService(String basePackage) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + basePackage + ".service;\r\n"
				+ "\r\n"
				+ "import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;\r\n"
				+ "import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;\r\n"
				+ "import org.skife.jdbi.v2.sqlobject.CreateSqlObject;\r\n"
				+ "\r\n"
				+ "import " + basePackage + ".dao.AppDAO;\r\n"
				+ "import " + basePackage + ".util.AppConstants;\r\n"
				+ "\r\n"
				+ "public abstract class AppService {\r\n"
				+ "	\r\n"
				+ "	@CreateSqlObject\r\n"
				+ "    abstract AppDAO appDAO();\r\n"
				+ "	\r\n"
				+ "	public String performHealthCheck() {\r\n"
				+ "    	try {\r\n"
				+ "    		appDAO().testDBHealth();\r\n"
				+ "    	} catch(UnableToObtainConnectionException ex) {\r\n"
				+ "    		return checkUnableToObtainConnectionException(ex);\r\n"
				+ "    	} catch(UnableToExecuteStatementException ex) {\r\n"
				+ "    		return checkUnableToExecuteStatementException(ex);\r\n"
				+ "    	}\r\n"
				+ "    	return null;\r\n"
				+ "    }\r\n"
				+ "	\r\n"
				+ "	public static String checkUnableToObtainConnectionException(UnableToObtainConnectionException ex) {\r\n"
				+ "    	if(ex.getCause() instanceof java.sql.SQLNonTransientConnectionException) {\r\n"
				+ "    		return AppConstants.DATABASE_ACCESS_ERROR + ex.getCause().getLocalizedMessage();\r\n"
				+ "    	} else if(ex.getCause() instanceof java.sql.SQLException) {\r\n"
				+ "    		return AppConstants.DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();\r\n"
				+ "    	} else {\r\n"
				+ "    		return AppConstants.UNEXPECTED_DATABASE_ERROR + ex.getCause().getLocalizedMessage();\r\n"
				+ "    	}\r\n"
				+ "    }\r\n"
				+ "    \r\n"
				+ "    public static String checkUnableToExecuteStatementException(UnableToExecuteStatementException ex) {\r\n"
				+ "    	if(ex.getCause() instanceof java.sql.SQLSyntaxErrorException) {\r\n"
				+ "    		return AppConstants.DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();\r\n"
				+ "    	} else {\r\n"
				+ "    		return AppConstants.UNEXPECTED_DATABASE_ERROR + ex.getCause().getLocalizedMessage();\r\n"
				+ "    	}\r\n"
				+ "    }\r\n"
				+ "}\r\n"
				+ "");
		
		return sb.toString();
	}
	
	/**
	 * To reside in .util
	 * @return
	 */
	/**
	 * 
	 * @param basePackage	(package name such as " + basePackage + ")
	 * @return
	 */
	public static String generateAppConstants(String basePackage) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + basePackage + ".util;\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "public enum AppConstants {\r\n"
				+ "	DATABASE_ACCESS_ERROR(\"Could not reach the database. The database may be down or there may be network connectivity issues. Details: \"),\r\n"
				+ "	DATABASE_CONNECTION_ERROR(\"Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: \"),\r\n"
				+ "    UNEXPECTED_DATABASE_ERROR(\"Unexpected error occurred while attempting to reach the database. Details: \"),\r\n"
				+ "    SUCCESS(\"Success\"),\r\n"
				+ "    UNEXPECTED_DELETE_ERROR(\"An unexpected error occurred while deleting entity.\"),\r\n"
				+ "    ENTITY_NOT_FOUND(\"Entity with id %s not found.\");\r\n"
				+ "	\r\n"
				+ "	private final String text;\r\n"
				+ "	\r\n"
				+ "	AppConstants(final String text) {\r\n"
				+ "		this.text = text;\r\n"
				+ "	}\r\n"
				+ "    \r\n"
				+ "	@Override\r\n"
				+ "	public String toString() {\r\n"
				+ "		return text;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "");
		
		return sb.toString();
	}
	
	public static String generateAppHealthCheck(String basePackage) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package " + basePackage + ".health;\r\n"
				+ "\r\n"
				+ "import com.codahale.metrics.health.HealthCheck;\r\n"
				+ "import " + basePackage + ".service.AppService;\r\n"
				+ "\r\n"
				+ "public class AppHealthCheck extends HealthCheck {\r\n"
				+ "	\r\n"
				+ "	private static final String HEALTHY_MSG = \"Application is healthy\";\r\n"
				+ "	private static final String UNHEALTHY_MSG = \"(!)-(!) Warning: Problems with Application Health (!)\";\r\n"
				+ "	\r\n"
				+ "	final AppService appService;\r\n"
				+ "	\r\n"
				+ "	public AppHealthCheck(AppService appService) {\r\n"
				+ "		this.appService = appService;\r\n"
				+ "	}\r\n"
				+ "\r\n"
				+ "	@Override\r\n"
				+ "	protected Result check() throws Exception {\r\n"
				+ "		String dbHealthStatus = appService.performHealthCheck();\r\n"
				+ "		\r\n"
				+ "		if(dbHealthStatus == null) {\r\n"
				+ "			return Result.healthy(HEALTHY_MSG);\r\n"
				+ "		} else {\r\n"
				+ "			return Result.unhealthy(UNHEALTHY_MSG, dbHealthStatus);\r\n"
				+ "		}\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "");
		
		return sb.toString();
	}
	
	public static String generateBanner(String projectName) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		sb.append( CodeUtil.capitalize(projectName).toUpperCase() + "\n"
				+ "\nCreated on: " + new Date(System.currentTimeMillis()).toLocaleString()
				+ "\n"
				+ "Credits ~ Dave's CodeGen6 (https://github.com/david-matu/ , davidmatu817@gmail.com)\n");
		sb.append("%DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD%\r\n"
				+ "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
		return sb.toString();
	}
	
	public static String getGenericSQLFunctions() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("MYSQL SQL UTILITY FUNCTIONS (DAVID MATU, 20-Jul-2022)\r\n"
				+ "________________________________________________________________________________________________________________________________________________________________________\r\n"
				+ "#GENERATE UNIQUE USER ID FUNCTION\r\n"
				+ "This code will generate unique user IDs with the first 10 digits representing timestamp and the last six are random\r\n"
				+ "________________________________________________________________________________________________________________________________________________________________________\r\n"
				+ "DELIMITER //\r\n"
				+ "CREATE FUNCTION genUserID()	RETURNS BIGINT\r\n"
				+ "READS SQL DATA\r\n"
				+ "DETERMINISTIC\r\n"
				+ "BEGIN\r\n"
				+ "	DECLARE UID BIGINT DEFAULT 0;\r\n"
				+ "	DECLARE TIME_NOW VARCHAR(20);\r\n"
				+ "	DECLARE STRING_1 VARCHAR(20);\r\n"
				+ "	DECLARE STRING_2 VARCHAR(20);\r\n"
				+ "\r\n"
				+ "	SET TIME_NOW = UNIX_TIMESTAMP(now());\r\n"
				+ "\r\n"
				+ "	SET UID = FLOOR(RAND() * 999999);\r\n"
				+ "\r\n"
				+ "	SET STRING_1 = '' + TIME_NOW;\r\n"
				+ "	SET STRING_2 = '' + UID;\r\n"
				+ "\r\n"
				+ "	SET STRING_1 = CONCAT(STRING_1, STRING_2);\r\n"
				+ "\r\n"
				+ "	RETURN CAST(STRING_1 AS UNSIGNED INTEGER);\r\n"
				+ "\r\n"
				+ "END; //\r\n"
				+ "________________________________________________________________________________________________________________________________________________________________________\r\n"
				+ "#RESULT\r\n"
				+ "mysql> select genUserId() //\r\n"
				+ "+------------------+\r\n"
				+ "| genUserId()      |\r\n"
				+ "+------------------+\r\n"
				+ "| 1658308675792585 |\r\n"
				+ "+------------------+\r\n"
				+ "\r\n"
				+ "________________________________________________________________________________________________________________________________________________________________________\r\n"
				+ "#GENERATE A UNIQUE 9-DIGIT PRODUCT ID\r\n"
				+ "\r\n"
				+ "What I considered: 9-Digits are 900,000,000 or 999,999,999 :: These are like 999K products with IDs, I think we can increase to like 14-digits\r\n"
				+ "________________________________________________________________________________________________________________________________________________________________________\r\n"
				+ "DELIMITER //\r\n"
				+ "DROP FUNCTION IF EXISTS genProductID //\r\n"
				+ "CREATE FUNCTION genProductID()	RETURNS BIGINT\r\n"
				+ "READS SQL DATA\r\n"
				+ "DETERMINISTIC\r\n"
				+ "BEGIN\r\n"
				+ "	DECLARE UID BIGINT DEFAULT 0;\r\n"
				+ "\r\n"
				+ "	SET UID = FLOOR(RAND() * 99999999999999);\r\n"
				+ "\r\n"
				+ "	RETURN UID;\r\n"
				+ "\r\n"
				+ "END; //\r\n"
				+ "\r\n"
				+ "DELIMITER; //\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "___________\r\n"
				+ "#ALT\r\n"
				+ "___________\r\n"
				+ "DELIMITER //\r\n"
				+ "DROP FUNCTION IF EXISTS genNewProductID //\r\n"
				+ "CREATE FUNCTION genNewProductID()	RETURNS BIGINT\r\n"
				+ "READS SQL DATA\r\n"
				+ "DETERMINISTIC\r\n"
				+ "BEGIN\r\n"
				+ "	DECLARE UID BIGINT DEFAULT 0;\r\n"
				+ "	DECLARE EXISTING_ID BIGINT DEFAULT 0;\r\n"
				+ "\r\n"
				+ "	SET UID = FLOOR(RAND() * 9999999999999);\r\n"
				+ "\r\n"
				+ "	SET EXISTING_ID = (SELECT PRODUCT_ID FROM PRODUCT WHERE PRODUCT_ID = UID);\r\n"
				+ "\r\n"
				+ "	IF EXISTING_ID = UID THEN\r\n"
				+ "		SET UID = FLOOR(RAND() * 9999999999999);\r\n"
				+ "	END IF;\r\n"
				+ "\r\n"
				+ "	RETURN UID;\r\n"
				+ "\r\n"
				+ "END; //\r\n"
				+ "\r\n"
				+ "DELIMITER ; //\r\n"
				+ "________________________________________________________________________________________________________________________________________________________________________\r\n"
				+ "#RESULT\r\n"
				+ "\r\n"
				+ "mysql> select genUserID();\r\n"
				+ "    -> //\r\n"
				+ "+-----------------+\r\n"
				+ "| genUserID()     |\r\n"
				+ "+-----------------+\r\n"
				+ "| 165830926897513 |\r\n"
				+ "+-----------------+\r\n"
				+ "\r\n"
				+ "#COMMENT: This is PRONE TO DUPLICATES\r\n"
				+ "________________________________________________________________________________________________________________________________________________________________________\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "");
		
		return sb.toString();
	}
	
	/**
	 * Create a File at the specified Path
	 * @param file (Destination path)
	 * @param modelName	(Filename, containing extension)
	 * @param content (File content to write)
	 * @throws IOException
	 */
	public static void createFile(File file, String modelName, String content) throws IOException {		
		Formatter output = null;
		String filePath = file.getAbsolutePath() + File.separator + modelName;
		try {
	      output = new Formatter(filePath); // open the file
	      
	      PrintStream ps = new PrintStream(filePath);
	      ps.println(content);
	      ps.close();	      
	    } catch (SecurityException securityException) {
	      System.err.println("Write permission denied. Terminating.");
	      //System.exit(1); // terminate the program
	    } catch (FileNotFoundException fileNotFoundException) {
	      System.err.println("Error opening file at " + filePath  + ". Terminating.\nMessage: " + fileNotFoundException.getLocalizedMessage());
	      //System.exit(1); // terminate the program
	    } finally {
	    	output.close();
	    }
		
	}

	/**
	 * Generate Angular Code (8/18/2022)
	 */
	/**
	 * Generate a Typescript model
	 * @param modelName
	 * @param modelAttributeMap
	 * @return an exported Typescript model
	 */
	public static String createNgModel(String modelName, Map<String, String> modelAttributeMap) {
		String fin = "";	//Final String
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("export class " + modelName + " {\n");
		
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			String defaultVal = "";
			if(pair.getValue().equals("string")) {
				defaultVal = " = ''";
			} else if(pair.getKey().equals("number")) {
				defaultVal = " = 0";
			}
			sb.append("\t" + pair.getKey() + ": " + pair.getValue() + defaultVal + ";\n");
		}
		
		sb.append("}");
		
		fin = sb.toString();
		return fin;
	}
	
	public static String createNgService(String modelName, Map<String, String> modelAttributeMap) {
		String fin = "";	//Final String
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("import { Injectable } from '@angular/core';\r\n"
				+ "\r\n"
				+ "import { HttpClient } from '@angular/common/http';\r\n"
				+ "\r\n"
				+ "import { GlobalVariable } from '../util/global';\r\n"
				+ "\r\n"
				+ "let " + CodeUtil.getConventionalName(modelName) + "List : Array<" + modelName + "Interface> = [];\r\n"
				+ "let service = GlobalVariable.BASE_API_URL;\r\n"
				+ "\n");
		sb.append("export interface " + modelName + "Interface {\n");
		for(Map.Entry<String, String> pair : modelAttributeMap.entrySet()) {
			sb.append("\t" + pair.getKey() + ": " + pair.getValue() + ";\n");
		}
		
		sb.append("}");
		
		fin = sb.toString();
		return fin;
	}
}
