package com.lavinia.gwt.user.client.interfaces;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IComponentsInfoAsync {

	public void getParameters(String component,AsyncCallback callback);	
	public void getComponentData(String name,AsyncCallback callback);
	public void getOutputs(Collection flow,AsyncCallback callback);
	public void getComponentFullData(String component,AsyncCallback callback);
	public void getComponents(AsyncCallback callback);
	public void getFlow(String component,AsyncCallback callback);
	public void isSimpleComponent(String name,AsyncCallback callback);
	public void getAggregateData(String component,AsyncCallback callback);
}
