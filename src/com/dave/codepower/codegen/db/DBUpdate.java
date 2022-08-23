package com.dave.codepower.codegen.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.dave.codepower.codegen.models.Model;
import com.dave.codepower.codegen.models.Project;

public class DBUpdate {
	public static void updateProject(Connection con, Project p) throws SQLException {
		String query = "UPDATE PROJECT SET DATE_CREATED=?, DB_NAME=?, PROJECT=?, DB_USER=?, DESCRIPTION=?, PROJECT_ID=?, DB_PASSWORD=?, WHERE PROJECT_ID=?";

		try (PreparedStatement pst = con.prepareStatement(query);) {
			pst.setTimestamp(1, p.getDateCreated());
			pst.setString(2, p.getDbName());
			pst.setString(3, p.getName());
			pst.setString(4, p.getDbUser());
			pst.setString(5, p.getDescription());
			pst.setInt(6, p.getProjectID());
			pst.setString(7, p.getDbPassword());
			pst.setInt(8, p.getProjectID());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateModel(Connection con, Model m) throws SQLException {
		String query = "UPDATE MODEL SET MODEL_ID=?, PROJECT_ID=?, ENTITY_NAME=?, PAIRING_TABLE=?, DATE_CREATED=?, CUSTOM_ATTRIBUTES=? WHERE MODEL_ID=?";

		try (PreparedStatement pst = con.prepareStatement(query);) {
			pst.setInt(1, m.getModelID());
			pst.setInt(2, m.getProjectID());
			pst.setString(3, m.getEntityName());
			pst.setString(4, m.getTable());
			pst.setTimestamp(5, m.getDateCreated());
			pst.setString(6, m.getCustomAttributes());
			pst.setInt(7, m.getModelID());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
