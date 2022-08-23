package com.dave.codepower.codegen.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.dave.codepower.codegen.db.DBConnection;
import com.dave.codepower.codegen.db.DBUtil;
import com.dave.codepower.codegen.models.TableInfo;

/**
 * 
 * @author MATU
 *	
 *	General purpose utility such as 
 *		* getting the resource id in a request string
 */
public class CodeUtil {

	public static String getResourceID(HttpServletRequest request) {		
		String assetID = request.getPathInfo().replace("/", "");
//		System.out.println("Resource ID in request: " + assetID);
		
		return assetID;
	}
	
	/**
	 * 
	 * @param con
	 * @param tableName
	 * @param primaryKey
	 * @param format
	 * @param length
	 * @return
	 */
	public static String generateUniqueID(Connection con, String tableName, String primaryKey, String format, int length) {		
		
		if(format != null) {
			format.toLowerCase();		//String -> string | Int -> int
		}
		
		switch(format) {
		case "string":
			String raw = "1234AC56789BCDEFGHJKLMNPQRSTVWXYZ";
			StringBuilder sb = new StringBuilder();
			Random random = new Random();
			
			while(sb.length() < length) {
				int index = (int) (random.nextFloat() * raw.length());
				sb.append(raw.charAt(index));
			}
			
			//Ensure that this id does not match any other number in the database table
			String uID = sb.toString();
			boolean unique = true;
			try {
				unique = DBUtil.isUniqueEntityID(con, tableName, primaryKey, uID);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			//Recursive call to re-generate the code for a unique one
			if(unique == false) {
				//con, tableName, primaryKey, uID
				generateUniqueID(con, tableName, primaryKey, format, length);
			}	
			//else return our already unique code
			return sb.toString();
			
		case "int":
			raw = "1234567890";
			sb = new StringBuilder();
			random = new Random();
			
			while(sb.length() < length) {
				int index = (int) (random.nextFloat() * raw.length());
				sb.append(raw.charAt(index));
			}
			
			int uNumber = Integer.parseInt(sb.toString());
			unique = true;
			try {
				unique = DBUtil.isUniqueEntityID(con, tableName, primaryKey, "" + uNumber);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			//Recursive call to re-generate the code for a unique one
			if(unique == false) {
				generateUniqueID(con, format, length);
			}	
			//else return our already unique code
			return "" + Integer.parseInt(sb.toString());
			
		case "long":
			raw = "1234567890";
			sb = new StringBuilder();
			random = new Random();
			
			while(sb.length() < length) {
				int index = (int) (random.nextFloat() * raw.length());
				sb.append(raw.charAt(index));
			}
			
			//Ensure that this id does not match any other number in the database table
			long unID = Long.parseLong(sb.toString());
			unique = true;
			try {
				unique = DBUtil.isUniqueEntityID(con, tableName, primaryKey, "" + unID);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			//Recursive call to re-generate the code for a unique one
			if(unique == false) {
				generateUniqueID(con, format, length);
			}	
			//else return our already unique code
			return "" + Long.parseLong(sb.toString());
		} //END CASE
		return "";
	}
	
	
	/**
	 * GENERIC UTILITY FOR UNIQUE ENTITY ID 
	 * @param con
	 * @return
	 * 
	 */
	public static String generateUniqueID(Connection con, String format, int length) {		
		
		if(format != null) {
			format.toLowerCase();		//String -> string | Int -> int
		}
		
		switch(format) {
		case "string":
			String raw = "1234AC56789BCDEFGHJKLMNPQRSTVWXYZ";
			StringBuilder sb = new StringBuilder();
			Random random = new Random();
			
			while(sb.length() < length) {
				int index = (int) (random.nextFloat() * raw.length());
				sb.append(raw.charAt(index));
			}
			
			//Ensure that this id does not match any other number in the database table
			String uID = sb.toString();
			boolean unique = true;
			try {
				unique = DBUtil.isUniqueEntityID(con, uID);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			//Recursive call to re-generate the code for a unique one
			if(unique == false) {
				generateUniqueID(con, format, length);
			}	
			//else return our already unique code
			return sb.toString();
			
		case "int":
			raw = "1234567890";
			sb = new StringBuilder();
			random = new Random();
			
			while(sb.length() < length) {
				int index = (int) (random.nextFloat() * raw.length());
				sb.append(raw.charAt(index));
			}
			
			int uNumber = Integer.parseInt(sb.toString());
			unique = true;
			try {
				unique = DBUtil.isUniqueEntityID(con, uNumber);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			//Recursive call to re-generate the code for a unique one
			if(unique == false) {
				generateUniqueID(con, format, length);
			}	
			//else return our already unique code
			return "" + Integer.parseInt(sb.toString());
			
		case "long":
			raw = "1234567890";
			sb = new StringBuilder();
			random = new Random();
			
			while(sb.length() < length) {
				int index = (int) (random.nextFloat() * raw.length());
				sb.append(raw.charAt(index));
			}
			
			//Ensure that this id does not match any other number in the database table
			long unID = Long.parseLong(sb.toString());
			unique = true;
			try {
				unique = DBUtil.isUniqueEntityID(con, unID);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			//Recursive call to re-generate the code for a unique one
			if(unique == false) {
				generateUniqueID(con, format, length);
			}	
			//else return our already unique code
			return "" + Long.parseLong(sb.toString());
		} //END CASE
		return "";
	}
	
	
	/**
	 * Fetch names of existing tables
	 * @param dbName
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getTableNames(String dbName, String userName, String password) throws SQLException {
		List<String> tList = new ArrayList<>();
		
		Connection con = DBConnection.createConnection(dbName, userName, password, true);
		
		String[] types = {"TABLE"};
		
		try {
			DatabaseMetaData md = con.getMetaData();	//con.getMetaData(); 
			ResultSet rs = md.getTables(dbName, null, "%", types);	
			
			while(rs.next()) {
				tList.add(rs.getString(3));
				//System.out.println("Table: " + rs.getString(3));
			}
		} catch(SQLException ex) {
			System.out.print("Fetching table names caused exception!");
			ex.printStackTrace();
		} finally {
			DBConnection.closeConnection(con);
		}
		
		return tList;
	}	
	
	/**
	 * Fetch information about table
	 * @param tableName 
	 * @param password 
	 * @param userName 
	 * @param dbName 
	 * @throws SQLException 
	 */
	public static TableInfo getTableInfo(String dbName, String userName, String password, String tableName) throws SQLException {
		TableInfo t = new TableInfo();
		List<String> colNames = new ArrayList<>();
		List<String> dataTypes = new ArrayList<>();
		
		Connection con = DBConnection.createConnection(dbName, userName, password, true);
		
		String query = "SELECT * FROM " + tableName;
		try (PreparedStatement pst = con.prepareStatement(query);) {
			ResultSet rs = pst.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			//System.out.println("Data types:::::::");
			// The column count starts from 1
			for (int i = 1; i <= columnCount; i++ ) {
			  String name = rsmd.getColumnName(i);
			  colNames.add(name);
			  
			  String d = rsmd.getColumnTypeName(i);
			  dataTypes.add(d);
			  //System.out.println(d);
			}
		} finally {
			DBConnection.closeConnection(con);
		}
		
		t.setTableName(tableName);
		t.setColumnList(colNames);
		t.setDataTypeList(dataTypes);
		
		return t;
	}
	
	/**
	 * Get a suitable Java data type based on SQL datatypes
	 * 	INT BIGINT VARCHAR TEXT DATETIME BLOB LONGBLOB MEDIUM_BLOB
	 */
	public static String getDataType(String sqlType) {
		String d = "";
		
		if(sqlType.equalsIgnoreCase("INT") || sqlType.equalsIgnoreCase("INT UNSIGNED")) {
			return "int";
		} else if(sqlType.equalsIgnoreCase("BIGINT") || sqlType.equalsIgnoreCase("BIGINT UNSIGNED")) {
			return "long";
		} else if(sqlType.equalsIgnoreCase("VARCHAR") || sqlType.equalsIgnoreCase("TEXT")) {
			return "String";
		} else if(sqlType.equalsIgnoreCase("DECIMAL")) {
			return "Double";
		} else if(sqlType.equalsIgnoreCase("DATE")) {
			return "Date";
		} else if(sqlType.equalsIgnoreCase("DATETIME")) {
			return "Timestamp";
		} else if(sqlType.contains("BLOB")) {
			return "InputStream";
		}
		
		return d;
	}
	
	/**
	 * 	Get an appropriate Typescript data type based on SQL data types
	 * 	INT BIGINT VARCHAR TEXT DATETIME BLOB LONGBLOB MEDIUM_BLOB
	 *  8/18/2022 12:30AM
	 */
	public static String getTypescriptType(String sqlType) {
		String d = "";
		
		if(sqlType.equalsIgnoreCase("INT") || sqlType.equalsIgnoreCase("INT UNSIGNED")) {
			return "number";
		} else if(sqlType.equalsIgnoreCase("BIGINT") || sqlType.equalsIgnoreCase("BIGINT UNSIGNED")) {
			return "number";
		} else if(sqlType.equalsIgnoreCase("VARCHAR") || sqlType.equalsIgnoreCase("TEXT")) {
			return "string";
		} else if(sqlType.equalsIgnoreCase("DECIMAL")) {
			return "number";
		} else if(sqlType.equalsIgnoreCase("DATE")) {
			return "number";
		} else if(sqlType.equalsIgnoreCase("DATETIME")) {
			return "number";
		} else if(sqlType.contains("BLOB")) {
			return "formData";
		}
		
		return d;
	}
	
	/**
	 * 
	 * Assume we want to transform 'TABLE_NAME' to 'TableName'
	 * 	1. toLowerCase()
	 *  2. Capitalize first word
	 *  3. Capitalize first word after underscore _
	 *  4. Remove Underscore and Concatenate string
	 */	
	
	/**
	 * For capitalizing a name after underscore. 'PRODUCT_NAME' to 'productName'
	 * @param name
	 * @return
	 */
	public static String getConventionalName(String name) {
		String out = "";
		
		out = name.toLowerCase();
		out = capitalize(out);
		out = capitalizeAfterUnderScore(out);
		
		return out;
	}
	
	/**
	 * For capitalizing variable names e.g. 'PRODUCT_NAME' to 'productName'
	 * 
	 * @param name
	 * @return
	 */
	public static String getConventionalVariableName(String name) {
		String out = "";
		
		out = name.toLowerCase();
		//out = capitalize(out);
		out = capitalizeAfterUnderScore(out);
		
		return out;
	}
	
	/**
	 * Produce a camel-cased name on finding an underscore
	 * @param name
	 * @return
	 */
	public static String capitalizeAfterUnderScore(String name) {
		if(name.contains("_")) {
			//Split string where underscore exists
			String[] split = name.split("_");
			String partA = split[0];
			String partB = split[1];
			
			return partA + capitalize(partB);
		} else {
			return name;
		}
	}
	
	/**
	 * Concatenates name on underscore e.g. 'FIRST_NAME' to 'FIRSTNAME'
	 * 
	 * @param name
	 * @return
	 */
	public static String getCompleteName(String name) {
		if(name.contains("_")) {
			//Split string where underscore exists
			String[] split = name.split("_");
			String partA = split[0];
			String partB = split[1];
			
			return partA + "" + partB;
		} else {
			return name;
		}
	}	
	
	/**
	 * 
	 * @param name e.g. 'productId'
	 * @return a separate set of name if on Camel e.g 'product Id'
	 */
	public static String getCompleteNameOnCamel(String name) {
		String[] splits = name.split("(?=\\p{Upper})");
		
		if(splits != null) {
			String finalString = "";
			
			for(String s : splits) {
				finalString += " " + s;
			}
			
			return finalString.trim();
		}
		return "";
	}

	//Sourced from www.attacomsian.com/blog/capitalize-first-letter-of-string-java
	public static String capitalize(String s) {
		if(s == null || s.isEmpty()) {
			return s;
		}
		
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
}
