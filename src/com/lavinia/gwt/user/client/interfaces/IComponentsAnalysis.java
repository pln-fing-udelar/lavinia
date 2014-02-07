package com.lavinia.gwt.user.client.interfaces;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IComponentsAnalysis extends RemoteService{
	
	/**   
	   * @gwt.typeArgs compsParams <com.lavinia.gwt.user.client.datatypes.DTComponentParams>
	   * @gwt.typeArgs <java.lang.String, com.lavinia.gwt.user.client.datatypes.DTAnnotationType>
	   */
	HashMap analyze(Vector compsParams, String text, String folder) throws Exception;
	
	Vector analyzeFiles(Vector compsParams, Vector files, String folder,String folder2) throws Exception;
	
	Vector analyzeImport(String path, Vector compsParams, boolean export,String folder) throws Exception;	
	
	boolean isFlowOK(Collection names) throws Exception;
	boolean isSubFlowOK(Collection names) throws Exception;
	String exportResult(Vector compsParams, String text, String folder) throws Exception;
	String saveFlow(Vector compsParams)throws Exception;
	
	/**   
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTComponentParams>
	   */
	Vector loadFlow(String id)throws Exception;
	

	
	
}
