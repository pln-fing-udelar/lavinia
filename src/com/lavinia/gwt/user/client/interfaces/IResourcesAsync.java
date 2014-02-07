package com.lavinia.gwt.user.client.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;

public interface IResourcesAsync {
	
		
	public void addResource(DTResourceData dtr,AsyncCallback callback);
	
	public void updateResource(DTResourceData dtr,AsyncCallback callback);
	
	public void updateLaviniaPath(DTResourceData dtr,AsyncCallback callback);
	
	public void removeResource(String name,AsyncCallback callback);
	
	public void getAllResources(AsyncCallback callback);
	
	public void getExtResources(AsyncCallback callback);
	
	//public void getResourcesGL(AsyncCallback callback);
	
	public void existsResource(String name,AsyncCallback callback);

}
