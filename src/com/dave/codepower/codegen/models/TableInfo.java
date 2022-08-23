package com.dave.codepower.codegen.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String modelName;	//Optional
	private String tableName;
	
	private List<String> columnList;
	private List<String> dataTypeList;

	public TableInfo() {
		super();
		columnList = new ArrayList<>();
		dataTypeList = new ArrayList<>();
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public List<String> getDataTypeList() {
		return dataTypeList;
	}

	public void setDataTypeList(List<String> dataTypeList) {
		this.dataTypeList = dataTypeList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
