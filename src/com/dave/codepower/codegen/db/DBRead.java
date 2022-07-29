package com.dave.codepower.codegen.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dave.codepower.codegen.models.Model;
import com.dave.codepower.codegen.models.Project;

public class DBRead {
	
	public static Project getProject(Connection con, int i) throws SQLException {
		String query = "SELECT * FROM PROJECT WHERE PROJECT_ID = ?";
		try (PreparedStatement pst = con.prepareStatement(query);) {
			pst.setInt(1, i);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				Project p = new Project();
				p.setDateCreated(rs.getTimestamp("DATE_CREATED"));
				p.setDbName(rs.getString("DB_NAME"));
				p.setName(rs.getString("PROJECT"));
				p.setDbUser(rs.getString("DB_USER"));
				p.setDescription(rs.getString("DESCRIPTION"));
				p.setProjectID(rs.getInt("PROJECT_ID"));
				p.setDbPassword(rs.getString("DB_PASSWORD"));
				return p;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Retrieve a project based on its name. This is ambiguous as of now (26 Mar, 2022), I assume projects will have distinct names
	 * The reason is that I could not retrieve the project ID at the time of creation and its crucial in the other phases of code generation
	 * 
	 * @param con
	 * @param projectName
	 * @return
	 * @throws SQLException
	 */
	public static Project getProject(Connection con, String projectName) throws SQLException {
		String query = "SELECT * FROM PROJECT WHERE PROJECT = ?";
		try (PreparedStatement pst = con.prepareStatement(query);) {
			pst.setString(1, projectName);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				Project p = new Project();
				p.setDateCreated(rs.getTimestamp("DATE_CREATED"));
				p.setDbName(rs.getString("DB_NAME"));
				p.setName(rs.getString("PROJECT"));
				p.setDbUser(rs.getString("DB_USER"));
				p.setDescription(rs.getString("DESCRIPTION"));
				p.setProjectID(rs.getInt("PROJECT_ID"));
				p.setDbPassword(rs.getString("DB_PASSWORD"));
				return p;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Project> getProjectList(Connection con) throws SQLException {
		String query = "SELECT * FROM PROJECT ORDER BY DATE_CREATED DESC";
		List<Project> pList = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();) {
			while(rs.next()) {
				Project p = new Project();
				p.setDateCreated(rs.getTimestamp("DATE_CREATED"));
				p.setDbName(rs.getString("DB_NAME"));
				p.setName(rs.getString("PROJECT"));
				p.setDbUser(rs.getString("DB_USER"));
				p.setDescription(rs.getString("DESCRIPTION"));
				p.setProjectID(rs.getInt("PROJECT_ID"));
				p.setDbPassword(rs.getString("DB_PASSWORD"));

				pList.add(p);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return pList;
	}
	
	
	public static Model getModel(Connection con, int i) throws SQLException {
		String query = "SELECT * FROM MODEL WHERE MODEL_ID = ?";
		try (PreparedStatement pst = con.prepareStatement(query);) {
			pst.setInt(1, i);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				Model m = new Model();
				m.setModelID(rs.getInt("MODEL_ID"));
				m.setProjectID(rs.getInt("PROJECT_ID"));
				m.setEntityName(rs.getString("ENTITY_NAME"));
				m.setTable(rs.getString("PAIRING_TABLE"));
				m.setDateCreated(rs.getTimestamp("DATE_CREATED"));
				m.setCustomAttributes(rs.getString("CUSTOM_ATTRIBUTES"));
				return m;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Model getModel(Connection con, String modelName) throws SQLException {
		String query = "SELECT * FROM MODEL WHERE ENTITY_NAME=?";
		try (PreparedStatement pst = con.prepareStatement(query);) {
			pst.setString(1, modelName);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				Model m = new Model();
				m.setModelID(rs.getInt("MODEL_ID"));
				m.setProjectID(rs.getInt("PROJECT_ID"));
				m.setEntityName(rs.getString("ENTITY_NAME"));
				m.setTable(rs.getString("PAIRING_TABLE"));
				m.setDateCreated(rs.getTimestamp("DATE_CREATED"));
				m.setCustomAttributes(rs.getString("CUSTOM_ATTRIBUTES"));
				return m;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Model> getModelList(Connection con) throws SQLException {
		String query = "SELECT * FROM MODEL";
		List<Model> mList = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();) {
			while(rs.next()) {
				Model m = new Model();
				m.setDateCreated(rs.getTimestamp("DATE_CREATED"));
				m.setModelID(rs.getInt("MODEL_ID"));
				m.setEntityName(rs.getString("ENTITY_NAME"));
				m.setProjectID(rs.getInt("PROJECT_ID"));
				m.setTable(rs.getString("PAIRING_TABLE"));

				mList.add(m);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return mList;
	}
	
	public static List<Model> getModelList(Connection con, int projectID) throws SQLException {
		String query = "SELECT * FROM MODEL WHERE PROJECT_ID=" + projectID;
		List<Model> mList = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(query);
			ResultSet rs = pst.executeQuery();) {
			while(rs.next()) {
				Model m = new Model();
				m.setDateCreated(rs.getTimestamp("DATE_CREATED"));
				m.setModelID(rs.getInt("MODEL_ID"));
				m.setEntityName(rs.getString("ENTITY_NAME"));
				m.setProjectID(rs.getInt("PROJECT_ID"));
				m.setTable(rs.getString("PAIRING_TABLE"));

				mList.add(m);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return mList;
	}
	
	/**
	 * Fetch Query Info: Table name, Columns and Data present in that particular table
	 * @param con
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public static SQLData getSQLData(Connection con, String query) throws SQLException {
		SQLData data = new SQLData();
		
		try (PreparedStatement pst = con.prepareStatement(query);
				ResultSet rs = pst.executeQuery();) {
				
				/** Show table name and print data **/
				System.out.println("Table name: " + rs.getMetaData().getTableName(1));
				
				data.setTable(rs.getMetaData().getTableName(1));
				
				int colCounts = rs.getMetaData().getColumnCount();				
				System.out.println("Translating Column Labels");
				
				//Add columns to the list and provision to the SQLData object				
				List<List<String>> dataList = new ArrayList<>();
				List<String> columnList = new ArrayList<>();
				
				for(int i=1; i <= colCounts; i++) {
					//System.out.println("Column " + i + ": " + rs.getMetaData().getColumnLabel(i));
					columnList.add(rs.getMetaData().getColumnLabel(i));
				}
				
				
				//Now inserting data 
				Map<String, String> colDataMap = new LinkedHashMap<>();
				
				for(Map.Entry<String, String> pair : colDataMap.entrySet()) {
					for(String col : columnList) {
						pair.setValue(col);
						colDataMap.put(col, "rs");
					}
				}
				
				/** End Printing table names **/
				/** Print Data */
				int index = 1;
				while(rs.next()) {
					//System.out.print(rs.getString(index) + ", ");
					//index++;
					
//					for(int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
//				        System.out.print(rs.getString(i) + " ");
//					}
//				    System.out.println();
					List<String> dataRow = new ArrayList<>();	//Add to dataList: List<List<String>>
					
				    for(int i=1; i <= colCounts; i++) {
				    	String colLabel = rs.getMetaData().getColumnLabel(i); 
				    	dataRow.add(rs.getString(colLabel));
				    }
				    dataList.add(dataRow);
				    System.out.println(Arrays.toString(dataRow.toArray()));
				    
					//colDataMap.put(rs.getCursorName(), rs.getString(index));					
				}
				
				data.setColumnList(columnList);
				data.setSqlDataMap(colDataMap);
				data.setDataList(dataList);
				
				System.out.println();
			} catch(SQLException e) {
				e.printStackTrace();
			}	
		
		
		return data;
	}
}
