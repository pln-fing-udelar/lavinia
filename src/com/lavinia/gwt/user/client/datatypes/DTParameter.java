/*
 * Created on 09/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.datatypes;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DTParameter implements IsSerializable {
	private String name;
	private String type;
	private String description;
	private String mandatory;
	private String multivalued;
	/**
	   * This field is a Set that must always contain dtparamtype
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTParamType>
	   */
	private Collection values;
	/**
	   * This field is a Set that must always contain dtparamtype
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTParamType>
	   */
	private Collection realvalues;
	
	public Collection getRealvalues() {
		return realvalues;
	}
	public void setRealvalues(Collection realvalues) {
		this.realvalues = realvalues;
	}
	/**
	 * 
	 */
	public DTParameter() {
		super();
	}
	/**
	 * @param name
	 * @param type
	 * @param description
	 * @param mandatory
	 * @param values
	 */
	public DTParameter(String name, String type, String description,
			String mandatory, String multivalued, Collection values) {
		super();
		this.name = name;
		this.type = type;
		this.description = description;
		this.mandatory = mandatory;
		this.multivalued = multivalued;
		this.values = values;
		
	}
	
	public DTParameter(String name, String type, String description,
			String mandatory, String multivalued, Collection values,Collection realvalues) {
		super();
		this.name = name;
		this.type = type;
		this.description = description;
		this.mandatory = mandatory;
		this.multivalued = multivalued;
		this.values = values;
		this.realvalues = realvalues;
		
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
	 * @return Returns the mandatory.
	 */
	public String getMandatory() {
		return mandatory;
	}
	/**
	 * @param mandatory The mandatory to set.
	 */
	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
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
	/**
	 * @return Returns the values.
	 */
	public Collection getValues() {
		return values;
	}
	/**
	 * @param values The values to set.
	 */
	public void setValues(Collection values) {
		this.values = values;
	}
	/**
	 * @return Returns the multivalued.
	 */
	public String getMultivalued() {
		return multivalued;
	}
	/**
	 * @param multivalued The multivalued to set.
	 */
	public void setMultivalued(String multivalued) {
		this.multivalued = multivalued;
	}
}
