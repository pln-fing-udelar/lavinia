
package com.lavinia.gwt.user.client.interfaces;



import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponent;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;


public interface IComponentAsync {

	public void addSimpleComponent(DTComponent dt, AsyncCallback callback);
	public void removeComponent(String component,AsyncCallback callback);
	public void updateSimpleComponent(Vector removed, DTComponent dt, AsyncCallback callback);	
	public void updateReferences(DTResourceData dtr, AsyncCallback callback);
	public void addAggregateComponent(DTAggregateComponent dt, AsyncCallback callback);
	public void updateAggregateComponent(DTAggregateComponent dt, AsyncCallback callback);
}
