package com.dave.codepower.codegen.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * @author MATU
 * 27-Mar-2022
 */
public class DBDelete {
	
	/*
	 * This is 
	 */
	public static boolean deleteResource(Connection con, long entityID, String tableName, String primaryCol) throws SQLException {		
		String sql = "DELETE FROM " + tableName + " WHERE " + primaryCol + "=" + entityID;
		
		try (PreparedStatement pst = con.prepareStatement(sql);){
			pst.executeUpdate();
			return true;
		} catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	/*
	public static boolean deleteResource(Connection con, String tableName, long entityID) throws SQLException {
		boolean isDeleted = deleteResource(con, entityID, tableName);
		return isDeleted;
	}
	*/
}
