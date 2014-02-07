package com.lavinia.gwt.user.client.interfaces;

import java.util.Collection;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IComponentsAnalysisAsync {
	
	public void analyze(Vector compsParams, String text, String folder,AsyncCallback callback);
	public void analyzeFiles(Vector compsParams, Vector files,String folder,String folder2, AsyncCallback callback);
	public void analyzeImport(String path, Vector compsParams,boolean export,String folder, AsyncCallback callback);
	public void exportResult(Vector compsParams, String text, String folder,AsyncCallback callback);
	public void saveFlow(Vector compsParams,AsyncCallback callback);
	public void loadFlow(String id,AsyncCallback callback);
	public void isFlowOK(Collection names,AsyncCallback callback);
	public void isSubFlowOK(Collection names,AsyncCallback callback);
	
	
}
