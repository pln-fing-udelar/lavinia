/*
 * Created on 04/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ITypesAsync {
	
	public void getTypes(boolean feature,AsyncCallback callback);	
	public void getMultiTypes(AsyncCallback callback);

}
