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
public class DTTypeString extends DTType implements IsSerializable {
	/**
	   * This field is a Set that must always contain DTTypeValue.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTTypeValue>
	   */

	private Collection values;
	/**
	 * 
	 */
	public DTTypeString() {
		super();
	}
	/**
	 * @param name
	 * @param supertype
	 * @param description
	 * @param input
	 * @param output
	 */
	public DTTypeString(String name, String supertype, String description,
			boolean input, boolean output, Collection values) {
		super(name, supertype, description, input, output);
		this.values = values;
		// TODO Auto-generated constructor stub
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
}
