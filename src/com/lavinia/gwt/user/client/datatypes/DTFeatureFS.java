/*
 * Created on 09/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.datatypes;


/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DTFeatureFS extends DTFeature {
	private String elementType;
	/**
	 * 
	 */
	public DTFeatureFS() {
		super();
	}
	/**
	 * @param name
	 * @param type
	 * @param description
	 */
	public DTFeatureFS(String name, String type, String description, String elementType) {
		super(name, type, description);
		this.elementType = elementType;
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the elementType.
	 */
	public String getElementType() {
		return elementType;
	}
	/**
	 * @param elementType The elementType to set.
	 */
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public boolean hasElementType(){
		return true;
	}
}
