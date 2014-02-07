package com.lavinia.gwt.user.client.analysis;

import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.lavinia.gwt.user.client.ShortCutsPanel;
import com.lavinia.gwt.user.client.interfaces.IComponentsAnalysis;
import com.lavinia.gwt.user.client.interfaces.IComponentsAnalysisAsync;
import com.lavinia.gwt.user.client.utils.StatusPopup;

public class AnalyzeThirdStep extends Composite{

	private IComponentsAnalysisAsync ass;
	private ServiceDefTarget endpoint;
	
	private Vector compsParams;
	private TableMarkupOptions tableTypes=null;
	
	private Vector typesNames;
	
	private Vector params;
	
	public Vector getParams() {
		return params;
	}

	public void setParams(Vector params) {
		this.params = params;
	}

	public Vector getTypesNames() {
		return typesNames;
	}

	public void setTypesNames(Vector typesNames) {
		this.typesNames = typesNames;
	}

	protected void onUnload() {	
		super.onUnload();
		ShortCutsPanel.getInstance().removeHyperLink("analizar");
		ShortCutsPanel.getInstance().removeHyperLink("actualizarVista");
		ShortCutsPanel.getInstance().removeHyperLink("export");
	}
	
	private StatusPopup statusPopup = StatusPopup.getInstance();
	
	public Vector getCompsParams() {
		return compsParams;
	}

	public void setCompsParams(Vector compsParams) {
		this.compsParams = compsParams;
	}

	public StatusPopup getStatusPopup() {
		return statusPopup;
	}

	public void setStatusPopup(StatusPopup statusPopup) {
		this.statusPopup = statusPopup;
	}

	public AnalyzeThirdStep(Vector params, Vector tn){
		this.params=params;
		typesNames = tn;
		ass = (IComponentsAnalysisAsync)GWT.create(IComponentsAnalysis.class);
		endpoint = (ServiceDefTarget)ass;	
			
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsAnalysis");
	}
	
	
	
	
	public IComponentsAnalysisAsync getAas(){
		return ass;
	}	
	
	
	public void export2(String id){	
		Window.open(GWT.getModuleBaseURL()+"exportXML?id="+id, "_self", "");
		statusPopup.hide();	
	}

	public void showResultText(String text, HashMap res){}
	
	public void refeshPanel(){
		
	}

	public TableMarkupOptions getTableTypes() {
		return tableTypes;
	}

	public void setTableTypes(TableMarkupOptions tableTypes) {
		this.tableTypes = tableTypes;
	}
		
}
