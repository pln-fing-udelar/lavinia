/*
 * Created on 04/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.interfaces;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ITypes extends RemoteService{

	 /**
	   * @gwt.typeArgs <java.lang.String>
	   */

	Collection getTypes(boolean feature) throws Exception;
	 /**
	   * @gwt.typeArgs <java.lang.String>
	   */
	Collection getMultiTypes() throws Exception;
	
}