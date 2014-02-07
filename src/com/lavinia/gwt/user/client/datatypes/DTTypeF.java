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
public class DTTypeF extends DTType implements IsSerializable  {

	/**
	   * This field is a Set that must always contain DTFeature.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTFeature>
	   */

	private Collection features;
	
	/**
	 * 
	 */
	public DTTypeF() {
		super();
	}
	/**
	 * @param name
	 * @param supertype
	 * @param description
	 * @param input
	 * @param output
	 */
	public DTTypeF(String name, String supertype, String description,
			boolean input, boolean output, Collection features) {
		super(name, supertype, description, input, output);
		this.features = features;
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the features.
	 */
	public Collection getFeatures() {
		return features;
	}
	/**
	 * @param features The features to set.
	 */
	public void setFeatures(Collection features) {
		this.features = features;
	}
}
