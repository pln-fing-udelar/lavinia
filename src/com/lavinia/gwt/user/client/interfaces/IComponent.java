package com.lavinia.gwt.user.client.interfaces;




import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponent;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;


public interface  IComponent extends RemoteService{
	
	void addSimpleComponent(DTComponent dt) throws Exception;
	void removeComponent(String component)throws Exception;
	void updateSimpleComponent(Vector removed, DTComponent dt) throws Exception;	
	void updateReferences(DTResourceData dtr)throws Exception;	
	void addAggregateComponent(DTAggregateComponent dt) throws Exception;
	void updateAggregateComponent(DTAggregateComponent dt) throws Exception;
		
	
}
