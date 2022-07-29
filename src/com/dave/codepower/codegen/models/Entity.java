package com.dave.codepower.codegen.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dataTypes;
	private Timestamp dateCreated;
	private int modelID;
	private String columns;
	private int entityID;
	private String attributes;

	public Entity() {
		super();
	}

	public String getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(String dataTypes) {
		this.dataTypes = dataTypes;
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

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public int getEntityID() {
		return entityID;
	}

	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

}
