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
public class DTParamType implements IsSerializable {

	private String value;
	/**
	 * 
	 */
	public DTParamType() {
		super();
		value="";
	}
	/**
	 * @param value
	 */
	public DTParamType(String value) {
		super();
		this.value = value;

	}
	/*public DTParamType(String value) {
		super();
		this.value = value;
	}*/
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
