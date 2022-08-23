package com.dave.codepower.codegen.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dave.codepower.codegen.models.Model;
import com.dave.codepower.codegen.models.Project;

public class DBCreate {
	
	/**
	 * Create a Project 
	 * @param con	Connection
	 * @param p		Project
	 * @throws SQLException
	 */
	public static void createProject(Connection con, Project p) throws SQLException {
		String query = "INSERT INTO PROJECT(DATE_CREATED, DB_NAME, PROJECT, DB_USER, DESCRIPTION,  DB_PASSWORD)"
				+ "values(?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pst = con.prepareStatement(query);) {
			pst.setTimestamp(1, p.getDateCreated());
			pst.setString(2, p.getDbName());
			pst.setString(3, p.getName());
			pst.setString(4, p.getDbUser());
			pst.setString(5, p.getDescription());
			pst.setString(6, p.getDbPassword());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createModel(Connection con, Model m) throws SQLException {
		String query = "INSERT INTO MODEL(MODEL_ID, PROJECT_ID, ENTITY_NAME, PAIRING_TABLE, DATE_CREATED, CUSTOM_ATTRIBUTES )"
				+ " values(?, ?, ?, ?, ?, ? )";

		try (PreparedStatement pst = con.prepareStatement(query);) {
			pst.setInt(1, m.getModelID());
			pst.setInt(2, m.getProjectID());
			pst.setString(3, m.getEntityName());
			pst.setString(4, m.getTable());
			pst.setTimestamp(5, m.getDateCreated());
			pst.setString(6, m.getCustomAttributes());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
