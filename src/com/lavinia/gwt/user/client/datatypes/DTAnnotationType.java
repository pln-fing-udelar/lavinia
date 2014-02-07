/*
 * Created on 14/10/2006
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
public class DTAnnotationType  implements IsSerializable {
	private String type;
	/**
	   * This field is a Set that must always contain DTAnnotation.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTAnnotation>
	   */
	private Vector annotations;
	/**
	 * 
	 */
	public DTAnnotationType() {		
		type="";
		annotations = new Vector();		
	}
	
	public DTAnnotationType(String type){
		this.type=type;
		annotations = new Vector();
	}
	
	public void addAnnotation(DTAnnotation dt){
		annotations.add(dt);
	}
	
	public Vector getAnnotations(){
		return annotations;
	}
	
	public String getType(){
		return type;
	}
}
