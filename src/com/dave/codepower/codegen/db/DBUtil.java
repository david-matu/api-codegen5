package com.dave.codepower.codegen.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBUtil {
	
	/* 
	 * UNIQUE ENTITY COMPARISON USING COMMON VIEW FOR ALL ENTITIES henceforth
	 * 
	 *  The 3 methods below (isUniqueEntityID) - overloaded
	 *  Cases where the value needed is either an String, Integer or a Long 
	 * 
	 */
	public static boolean isUniqueEntityID(Connection con, String uID) throws SQLException {
		String sql = "SELECT ENTITY_ID FROM E_UNIQUE"
				+ " where ENTITY_ID=?";
		
		boolean unique = true;
		
		try (PreparedStatement pst = con.prepareStatement(sql);){
			pst.setString(1, uID);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				unique = false;
				return unique;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return unique;
	}
	
	/**
	 * Generate a Unique ID in the existing tables specified by @param tableName
	 * @param con
	 * @param tableName
	 * @param primaryKey
	 * @param uID
	 * @return
	 * @throws SQLException
	 */
	public static boolean isUniqueEntityID(Connection con, String tableName, String primaryKey, String uID) throws SQLException {
		String sql = "SELECT " + primaryKey + " FROM " + tableName
				+ " where " + primaryKey + "=" + uID;
		
		boolean unique = true;
		
		try (PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();) {		
			if(rs.next()) {
				unique = false;
				return unique;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return unique;
	}
	
	public static boolean isUniqueEntityID(Connection con, long uNumber) throws SQLException {
		String sql = "SELECT ENTITY_ID FROM E_UNIQUE"
				+ " WHERE ENTITY_ID=?";
		
		boolean unique = true;
		
		try (PreparedStatement pst = con.prepareStatement(sql);){
			pst.setLong(1, uNumber);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				unique = false;
				return unique;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return unique;
	}
	
	public static boolean isUniqueEntityID(Connection con, int uID) throws SQLException {
		String sql = "SELECT ENTITY_ID FROM E_UNIQUE"
				+ " WHERE ENTITY_ID=?";
		
		boolean unique = true;
		
		try (PreparedStatement pst = con.prepareStatement(sql);){
			pst.setInt(1, uID);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				unique = false;
				return unique;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return unique;
	}
	
	//For Phones
	public static boolean isUniqueContact(Connection con, int uID) throws SQLException {
		String sql = "SELECT CONTACT FROM EXISTING_CONTACTS"
				+ " WHERE CONTACT=?";
		
		boolean unique = true;
		
		try (PreparedStatement pst = con.prepareStatement(sql);){
			pst.setInt(1, uID);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				unique = false;
				return unique;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return unique;
	}
	
	//For Emails
	public static boolean isUniqueContact(Connection con, String uID) throws SQLException {
		String sql = "SELECT CONTACT FROM EXISTING_CONTACTS"
				+ " WHERE CONTACT=?";
		
		boolean unique = true;
		
		try (PreparedStatement pst = con.prepareStatement(sql);){
			pst.setString(1, uID);
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				unique = false;
				return unique;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return unique;
	}
	
	/*
	public static boolean isValidLogin(Connection con, Logins l) throws SQLException {
		String sql = "SELECT * FROM LOGINS"
				+ " WHERE CONTACT = ? AND PASSECRET = SHA2(?, 256)";
		
		boolean valid = false;
		
		try (PreparedStatement pst = con.prepareStatement(sql);){
			pst.setString(1, l.getUserContact());
			pst.setString(2, l.getPassword());
			
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()) {
				valid = true;
				return valid;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return valid;
	}
	*/

	public static boolean businessExists(Connection con, long uid) throws SQLException {
		String sql = "SELECT * FROM BUSINESS"
				+ " WHERE BUSINESS_ID = ? OR OWNER_ID = ?";
		
		boolean valid = false;
		
		try (PreparedStatement pst = con.prepareStatement(sql);){
			pst.setLong(1, uid);
			pst.setLong(2, uid);			
			ResultSet rs = pst.executeQuery();			
			if(rs.next()) {
				valid = true;
				return valid;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return valid;
	}
	
}
