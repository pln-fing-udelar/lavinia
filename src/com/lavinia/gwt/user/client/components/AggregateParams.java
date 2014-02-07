package com.lavinia.gwt.user.client.components;

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
import com.lavinia.gwt.user.client.analysis.ParamsSettings;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfoAsync;
import com.lavinia.gwt.user.client.utils.StringsTableListener;

public class AggregateParams  extends Composite{

	
	private StringsTableListener table;
	private Vector names = new Vector();
	private HorizontalPanel hpSecond;
	private Vector params;
	private HorizontalPanel parametters;
	
	private Vector compsParams;
	
	private boolean loadvalues = false;
	private IComponentsInfoAsync ass;
	private ServiceDefTarget endpoint;
	private boolean enabled;
	
	private String lang;
	
	public AggregateParams(String l, boolean enabled){
		this.enabled = enabled;
		this.lang = l;
		hpSecond = new HorizontalPanel();
		Params();
		hpSecond.setSpacing(20);
		initWidget(hpSecond);
	}
	
	
	
	public void setFlow(Vector flow){
		loadvalues =false;
		names=flow;
		table.clear();
		
		for (int i=0; i<names.size();i++)
			table.addRow((String)names.get(i));
		
		params = new Vector();
		params.setSize(table.getCount());
		
		for (int i=0;i<table.getCount();i++){
			loadParams(i);
		}
	}
	
		
	private void Params(){
		
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
	}
	
	
	private void loadParams(final int index){
		
		if (loadvalues){
			DTComponentParams dt = (DTComponentParams)compsParams.get(index);
			ParamsSettings ps = new ParamsSettings(lang, index, table.getText(index),dt.getParams(),true,enabled,false);
			params.set(index,ps);
			if (index==0)
          		showParams(ps);  
		}else{
		
			ass.getParameters(table.getText(index),new AsyncCallback() {
			
				public void onFailure(Throwable caught) {	        		        	
					Window.alert(caught.getMessage());
					
				}
				
		        public void onSuccess(Object result) {
		        	Vector col = (Vector)result;
		        	ParamsSettings ps = new ParamsSettings(lang,index, table.getText(index),col,false,enabled,false);
		        		          	
		          	params.set(index,ps);	          	
		          	
		          	if (index==0)
		          		showParams(ps);       	          	
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
	
	public void rowSelected(int row){
		if (row>=0) {
			showParams((ParamsSettings)params.get(row));
		}
	}
	
	public void loadData(DTAggregateComponent dt){
		loadvalues=true;
		
		compsParams = dt.getCompsParams();
		
		table.clear();
		
		names = new Vector();
		for (int i=0; i<dt.getCompsParams().size();i++){
			DTComponentParams dtp = (DTComponentParams)dt.getCompsParams().get(i);
			names.add(dtp.getName());
			table.addRow(dtp.getName());
		}
		
		params = new Vector();
		params.setSize(table.getCount());
		
		for (int i=0;i<table.getCount();i++){
			loadParams(i);
		}
		
	}
	
}
