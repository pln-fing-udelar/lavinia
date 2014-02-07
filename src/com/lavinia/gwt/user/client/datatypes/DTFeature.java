/*
 * Created on 09/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.datatypes;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DTFeature implements IsSerializable{
	private String name;
	private String type;
	private String description;
	private String value;
	/**
	 * 
	 */
	public DTFeature() {
		super();
	}
	/**
	 * @param name
	 * @param type
	 * @param description
	 */
	public DTFeature(String name, String type, String description) {
		super();
		this.name = name;
		this.type = type;
		this.description = description;
		this.value="";
	}
	public DTFeature(String name, String type, String description, String value) {
		super();
		this.name = name;
		this.type = type;
		this.description = description;
		this.value=value;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String v) {
		this.value = v;
	}
	public boolean hasElementType(){
		return false;
	}
}
