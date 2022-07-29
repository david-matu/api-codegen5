package com.dave.codepower.codegen.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class DBConnection {
	private static DataSource ds;
	
	private static DataSource tempDs;	//Temporary datasource for external or contextual database connections
	
	static String URL = "jdbc:mysql://localhost:3306/codegen5?verifyServerCertificate=false&useSSL=true&requireSSL=true";
	static String lUserName = "root";
	static String lPassword = "root";
	
	static String rURL = "jdbc:mysql://" + "dbdestine.cbf3y03gliba.us-east-2.rds.amazonaws.com:3306/destinedb" + "?verifyServerCertificate=false"
			+ "&useSSL=true" +
			"&requireSSL=true";
	static String rUserName = "markyapp";
	static String rPassword = "markyapp$707";
	
	public static DataSource getDataSource() {
		if(ds == null) {
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setJdbcUrl(URL);
			config.setUsername(lUserName);
			config.setPassword(lPassword);
			
			config.setMaximumPoolSize(20);
			config.setAutoCommit(true);
			config.setConnectionTimeout(7000);
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			config.addDataSourceProperty("tcpKeepAlive", false);
			
			config.addDataSourceProperty("sslMode", "DISABLED");
			config.addDataSourceProperty("allowPublicKeyRetrieval", "false");
			
			ds = new HikariDataSource(config);
		}
		return ds;
	}
	
	public DBConnection() {
		ds = getDataSource();
	}
	
	public static Connection createConnection() {
        Connection con = null;
        try {
	        if(ds != null) {
	        	con = ds.getConnection();
	        } else {
	        	con = getDataSource().getConnection();
	        }			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        if(con == null) {
        	System.out.println("Critical Datasource search! Cannot find a connection to database");
        	try {
				con = DriverManager.getConnection(rURL, rUserName, rPassword);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        return con;
    }

	public static void closeConnection(Connection con) {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * For fetching info from other databases
	 * 
	 */
	public static Connection createConnection(String dbName, String dbUser, String dbPassword, boolean shortTerm) {
		String tempURL = "jdbc:mysql://localhost:3306/" + dbName + "?verifyServerCertificate=false&useSSL=true&requireSSL=true";
		
		Connection con = null;
		try {
			con = DriverManager.getConnection(tempURL, dbUser, dbPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
        return con;
    }
	
	public static Connection createConnection(String dbName, String dbUser, String dbPassword) {
		String tempURL = "jdbc:mysql://localhost:3306/" + dbName + "?verifyServerCertificate=false&useSSL=true&requireSSL=true";
		
		Connection con = null;
        
        try {
	        if(tempDs != null) {
	        	con = tempDs.getConnection();
	        } else {
	        	con = getDataSource(dbName, dbUser, dbPassword).getConnection();
	        }			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        if(con == null) {
        	System.out.println("Datasource is empty. Remedy by restarting the application...");
        	try {
				con = DriverManager.getConnection(tempURL, dbUser, dbPassword);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        return con;
    }
	
	public static DataSource getDataSource(String dbName, String dbUser, String dbPassword) {
		String tempURL = "jdbc:mysql://localhost:3306/" + dbName + "?verifyServerCertificate=false&useSSL=true&requireSSL=true";
		
		if(tempDs == null) {
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setJdbcUrl(tempURL);
			config.setUsername(dbUser);
			config.setPassword(dbPassword);
			
			config.setMaximumPoolSize(5);
			config.setAutoCommit(true);
			config.setConnectionTimeout(7000);
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			config.addDataSourceProperty("tcpKeepAlive", true);
			
			config.addDataSourceProperty("sslMode", "DISABLED");
			config.addDataSourceProperty("allowPublicKeyRetrieval", "false");
			
			tempDs = new HikariDataSource(config);
		}
		return tempDs;
	}
}
