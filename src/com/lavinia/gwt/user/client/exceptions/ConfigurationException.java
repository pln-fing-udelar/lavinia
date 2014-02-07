/*
 * Created on 26/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConfigurationException extends Exception implements IsSerializable{

	  private String msg; 

	  public String getMessage(){ 
              return this.msg; 
      } 
	
	  public ConfigurationException(){
	  		super();
	  }
	
	  public ConfigurationException(String arg0) {	  	
	  	super(arg0);
	  	this.msg = arg0;
		
	}
}