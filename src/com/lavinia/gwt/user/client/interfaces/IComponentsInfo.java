package com.lavinia.gwt.user.client.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponentData;

public interface IComponentsInfo extends RemoteService{

	/**   	   
	   * @gwt.typeArgs <java.lang.String>
	   */
	Collection getComponents() throws Exception;
	
	/**   	   
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTParameter>
	   */	
	Vector getParameters(String component)throws Exception;
	
	DTComponentData getComponentData(String name)throws Exception;
	/**   
	   * @gwt.typeArgs flow <java.lang.String>
	   * @gwt.typeArgs <java.lang.String, java.lang.String>
	   */
	ArrayList getOutputs(Collection flow) throws Exception;
	DTComponent getComponentFullData(String component)throws Exception;
	/**   
	   * @gwt.typeArgs <java.lang.String, java.lang.String>
	   */
	Vector getFlow(String component)throws Exception;
	
	Boolean isSimpleComponent(String name) throws Exception;
	
	DTAggregateComponent getAggregateData(String name) throws Exception;
}
