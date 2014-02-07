package com.lavinia.gwt.user.client.datatypes;


import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DTAggregateComponent implements IsSerializable{
	public String name;	
	public String autor;
	public String description;
	
	/**
	   * This field is a Set that must always contain DTParameter.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTComponentParams>
	   */
	public Vector compsParams;
	
	public DTAggregateComponent(String name, String autor, String description) {
		super();
		this.name = name;
		this.autor = autor;
		this.description = description;
	}
	public DTAggregateComponent(){
		name = "";
		autor = "";
		description="";
		compsParams = new Vector();
	}
	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public Vector getCompsParams() {
		return compsParams;
	}

	public void setCompsParams(Vector comps) {
		this.compsParams = comps;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
