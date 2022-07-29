package com.dave.codepower.codegen.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SQLData {
	
	private String table;
	private List<String> columnList = new ArrayList<>();
	private Map<String, String> sqlDataMap = new LinkedHashMap<>();	//Store column and data
	
	private List<List<String>> dataList = new ArrayList<>();
	
	public SQLData() {
		super();
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * @return the columnList
	 */
	public List<String> getColumnList() {
		return columnList;
	}

	/**
	 * @param columnList the columnList to set
	 */
	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	/**
	 * @return the sqlDataMap
	 */
	public Map<String, String> getSqlDataMap() {
		return sqlDataMap;
	}

	/**
	 * @param sqlDataMap the sqlDataMap to set
	 */
	public void setSqlDataMap(Map<String, String> sqlDataMap) {
		this.sqlDataMap = sqlDataMap;
	}

	/**
	 * @return the dataList
	 */
	public List<List<String>> getDataList() {
		return dataList;
	}

	/**
	 * @param dataList the dataList to set
	 */
	public void setDataList(List<List<String>> dataList) {
		this.dataList = dataList;
	}	
}
