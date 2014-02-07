package com.lavinia.gwt.user.client.analysis;

import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfoAsync;
import com.lavinia.gwt.user.client.utils.StatusPopup;
import com.lavinia.gwt.user.client.utils.StringsTableListener;

public class AnalyzeSecondStep  extends Composite{

	private IComponentsInfoAsync ass;
	private ServiceDefTarget endpoint;
	private StringsTableListener table;
	private HorizontalPanel hpSecond;
	
	private Vector params;
	
	private StatusPopup statusPopup = StatusPopup.getInstance();
	
	private Vector names;
	
	private HorizontalPanel parametters;
	
	private boolean loadvalues = false;
	
	private Vector compsParams;
	
	private String lang;
	
	
	
	public AnalyzeSecondStep(String l, Vector names){
		this.lang = l;
		hpSecond = new HorizontalPanel();
		this.names=names;
		SecondStep();

		initWidget(hpSecond);
		
	}
	
	public AnalyzeSecondStep(String l, Vector names,Vector cp){
		this.lang = l;
		hpSecond = new HorizontalPanel();
		hpSecond.setWidth("100%");
		
		this.names=names;
		compsParams=cp;
		loadvalues=true;
		
		SecondStep();
		
		
		initWidget(hpSecond);
	}
	
	
	private void SecondStep(){
		
		if (lang.equals("es"))
			statusPopup.setText("Cargando Parametros...");
		else
			statusPopup.setText("Loading Parametters...");
		statusPopup.show();
		
		ass = (IComponentsInfoAsync)GWT.create(IComponentsInfo.class);
		endpoint = (ServiceDefTarget)ass;	
			
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsInfo");
		
		
		
		/*******COMPONENTS LIST******************************/
		VerticalPanel hv1 = new VerticalPanel();
		
		Label lb1;
		if (lang.equals("es"))
			lb1 = new Label ("Componentes");
		else
			lb1 = new Label ("Components");
		table = new StringsTableListener(this);
		for (int i=0; i<names.size();i++)
			table.addRow((String)names.get(i));
		
		hv1.add(lb1);
		hv1.add(table);
		hv1.setStyleName("lv-Sink2");
		hv1.setSpacing(4);
		hpSecond.add(hv1);
		
		params = new Vector();
		params.setSize(table.getCount());
		
		/*******BUTTON****************************************/
		
		
		parametters = new HorizontalPanel();
		hpSecond.add(parametters);
		hpSecond.setCellHorizontalAlignment(parametters, HasHorizontalAlignment.ALIGN_RIGHT);
		hpSecond.setCellWidth(parametters, "100%");
		
		
		for (int i=0;i<table.getCount();i++){
			loadParams(i);
		}
		
		
		
		/*************************************************************/
		hpSecond.setSpacing(10);
		statusPopup.hide();
		
		
	}
	public void rowSelected(int row){
		if (row>=0) {
			showParams((ParamsSettings)params.get(row));
		}
	}
	private void loadParams(final int index){
		
		if (loadvalues){
			DTComponentParams dt = (DTComponentParams)compsParams.get(index);
			ParamsSettings ps = new ParamsSettings(lang,index,table.getText(index),dt.getParams(),true,true,true);
			params.set(index,ps);
			if (index==0)
          		showParams(ps); 
		}else{
		
			ass.getParameters(table.getText(index),new AsyncCallback() {
			
				public void onFailure(Throwable caught) {	        		        	
					Window.alert(caught.getMessage());
					statusPopup.hide();
				}
		        public void onSuccess(Object result) {
		        	
		        	Vector col = (Vector)result;
		        	
		        	ParamsSettings ps = new ParamsSettings(lang,index,table.getText(index),col,false,true,true);
		        	
		        	if (ps!=null){
		        		params.set(index,ps);	          	
		          	
		        		if (index==0)
		        			showParams(ps);
		        	}
		        }
		    });	 
		}
	}
	
	private void showParams(ParamsSettings ps){
		parametters.clear();
		parametters.add(ps);
	}
	public Vector getParams(){
		return params;
	}

	public Vector getCompsParams() {
		return compsParams;
	}

	public void setCompsParams(Vector compsParams) {
		this.compsParams = compsParams;
	}

	public Vector getNames() {
		return names;
	}

	public void setNames(Vector names) {
		this.names = names;
	}
	public void refreshPanel(){
		loadvalues=true;
		table.clear();
		for (int i=0; i<names.size();i++)
			table.addRow((String)names.get(i));
		
		params = new Vector();
		params.setSize(table.getCount());
		for (int i=0;i<table.getCount();i++){
			loadParams(i);
		}		
	}
	
}
