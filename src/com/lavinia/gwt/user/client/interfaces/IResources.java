package com.lavinia.gwt.user.client.interfaces;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;
import com.lavinia.gwt.user.client.exceptions.XMLErrorException;

public interface IResources extends RemoteService{

	void addResource(DTResourceData dtr)throws Exception;	
	Boolean updateResource(DTResourceData dtr)throws Exception;	
	Boolean updateLaviniaPath(DTResourceData dtr)throws Exception;	
	Boolean removeResource(String name)throws Exception;	
	 /**
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTResourceData>
	   */
	Collection getAllResources()throws Exception;
	/**
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTResourceData>
	   */
	Collection getExtResources()throws Exception;	
	Boolean existsResource(String name) throws XMLErrorException;
}
