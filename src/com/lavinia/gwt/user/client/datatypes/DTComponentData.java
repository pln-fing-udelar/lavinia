/*
 * Created on 08/09/2006
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
public class DTComponentData implements IsSerializable{
	
	private String description;
	
	private String[] inputs;
	
	private String[] outputs;

	/**
	 * 
	 */
	public DTComponentData() {
		super();
	}
	/**
	 * @param description
	 * @param inputs
	 * @param outputs
	 */
	public DTComponentData(String description, String[] inputs, String[] outputs) {
		super();
		this.description = description;
		this.inputs = inputs;
		this.outputs = outputs;
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
	 * @return Returns the inputs.
	 */
	public String[] getInputs() {
		return inputs;
	}
	/**
	 * @param inputs The inputs to set.
	 */
	public void setInputs(String[] inputs) {
		this.inputs = inputs;
	}
	/**
	 * @return Returns the outputs.
	 */
	public String[] getOutputs() {
		return outputs;
	}
	/**
	 * @param outputs The outputs to set.
	 */
	public void setOutputs(String[] outputs) {
		this.outputs = outputs;
	}
}
