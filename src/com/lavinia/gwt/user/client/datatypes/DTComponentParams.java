/*
 * Created on 08/11/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.datatypes;

import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;
/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DTComponentParams implements IsSerializable {
	private String name;
	/**
	   * This field is a Set that must always contain DTParameter
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTParameter>
	   */
	private Vector params;

	/**
	 * @param name
	 * @param params
	 */
	public DTComponentParams(String name, Vector params) {
		super();
		this.name = name;
		this.params = params;
	}
	/**
	 * 
	 */
	public DTComponentParams() {
		super();
		name="";
		params = new Vector();
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
	 * @return Returns the params.
	 */
	public Vector getParams() {
		return params;
	}
	/**
	 * @param params The params to set.
	 */
	public void setParams(Vector params) {
		this.params = params;
	}
	
	public void addParam(DTParameter dt){
		params.add(dt);
	}
}
