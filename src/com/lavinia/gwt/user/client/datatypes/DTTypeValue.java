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
public class DTTypeValue implements IsSerializable{
	private String value;
	private String description;
	/**
	 * 
	 */
	public DTTypeValue() {
		super();
	}
	/**
	 * @param value
	 * @param description
	 */
	public DTTypeValue(String value, String description) {
		super();
		this.value = value;
		this.description = description;
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
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
