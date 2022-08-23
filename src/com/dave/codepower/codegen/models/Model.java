package com.dave.codepower.codegen.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Timestamp dateCreated;
	private int modelID;
	private String entityName;
	private int projectID;
	private String table;
	private String customAttributes;

	public Model() {
		super();
	}

	public Timestamp getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
	}

	public int getModelID() {
		return modelID;
	}

	public void setModelID(int modelID) {
		this.modelID = modelID;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public String getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(String customAttributes) {
		this.customAttributes = customAttributes;
	}
}
