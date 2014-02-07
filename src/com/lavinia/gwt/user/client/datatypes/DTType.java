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
public abstract class DTType implements IsSerializable {
	private String name;
	private String supertype;
	private String description;
	private boolean input;
	private boolean output;
	/**
	 * 
	 */
	public DTType() {
		super();
	}
	/**
	 * @param name
	 * @param supertype
	 * @param description
	 * @param input
	 * @param output
	 */
	public DTType(String name, String supertype, String description,
			boolean input, boolean output) {
		super();
		this.name = name;
		this.supertype = supertype;
		this.description = description;
		this.input = input;
		this.output = output;
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
	 * @return Returns the input.
	 */
	public boolean isInput() {
		return input;
	}
	/**
	 * @param input The input to set.
	 */
	public void setInput(boolean input) {
		this.input = input;
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
	 * @return Returns the output.
	 */
	public boolean isOutput() {
		return output;
	}
	/**
	 * @param output The output to set.
	 */
	public void setOutput(boolean output) {
		this.output = output;
	}
	/**
	 * @return Returns the supertype.
	 */
	public String getSupertype() {
		return supertype;
	}
	/**
	 * @param supertype The supertype to set.
	 */
	public void setSupertype(String supertype) {
		this.supertype = supertype;
	}
}
