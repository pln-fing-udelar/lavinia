package com.lavinia.gwt.user.client.components;

import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.analysis.ParamsSettings;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.interfaces.IComponent;
import com.lavinia.gwt.user.client.interfaces.IComponentAsync;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfoAsync;
import com.lavinia.gwt.user.client.utils.StatusPopup;

public class AddAggregate extends Composite {
	
	private ComponentsOptions c;
	
	private StatusPopup statusPopup = StatusPopup.getInstance();
	
	private IComponentsInfoAsync assinfo;	
	

	private boolean adding;
	
	private TabPanel tab;
	
	private AggregateData ad;
	private AggregateFlow af;
	private AggregateParams ap;

	private VerticalPanel vp;
	
	private String lang;
	
	public AddAggregate(String l, ComponentsOptions p, boolean enabled, boolean add){		
		this.lang = l;
		this.adding = add;
		c=p;
		
		assinfo = (IComponentsInfoAsync)GWT.create(IComponentsInfo.class);
		ServiceDefTarget endpointinfo = (ServiceDefTarget)assinfo;			
		endpointinfo.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsInfo");
		
		tab = new TabPanel();
		
		ad = new AggregateData(lang,add,enabled);
		af = new AggregateFlow(lang,enabled);
		ap = new AggregateParams(lang,enabled);
		
		if (lang.equals("es")){
			tab.add(ad,"Datos");
			
			tab.add(af,"Flujo");
			
			tab.add(ap,"Parámetros");
		}else{
			tab.add(ad,"Info");
			
			tab.add(af,"Flow");
			
			tab.add(ap,"Parametters");
		}
		
		tab.selectTab(0);
		
		tab.addTabListener(new TabListener(){
			public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
				if ((tabIndex==2)&&(af.isFlowChange())){
					ap.setFlow(af.getFlow());			
					af.setFlowChange(false);
					
				}
			}

			public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
					return true;
			}
		});
		
		HorizontalPanel buttons = new HorizontalPanel();
		
		if (enabled){
			String msg="";
			if (lang.equals("es"))
				msg="Guardar";
			else
				msg = "Save";
			Button baccept = new Button(msg, new ClickListener() {
				      public void onClick(Widget sender) {
				    	  
				    	  String msg="";
				    	  if ((adding)&&(lang.equals("es")))
				    		  msg="Seguro desea agregar el flujo?";
				    	  else if ((!adding)&&(lang.equals("es")))
				    		  msg="Seguro desea modificar el flujo?";
				    	  else if ((adding)&&(!lang.equals("es")))
				    		  msg="Add flow?";
				    	  else if ((!adding)&&(!lang.equals("es")))
				    		  msg="Update flow?";
				    	  if (Window.confirm(msg)){
					    	if (adding){
					    		if (lang.equals("es"))
					    			statusPopup.setText("Agregando flujo...");
					    		else
					    			statusPopup.setText("Adding flow...");
					    		statusPopup.show();
					    	}else{
					    		if (lang.equals("es"))
					    			statusPopup.setText("Actualizando flujo...");
					    		else
					    			statusPopup.setText("Updating flow...");
					    		statusPopup.show();
					    	}
					        saveComponent();
				    	  }    	
				       		    	  
				      }
				    });
			
			if (lang.equals("es"))
				msg="Cancelar";
			else
				msg = "Cancel";
			Button bcancel = new Button(msg, new ClickListener() {
				      public void onClick(Widget sender) {
				    	  String msg="";
				    	  if (lang.equals("es"))
				    		  msg = "Cancel operation?";
				    	  else
				    		  msg = "Desea cancelar la operación?";
				    			  
				    	  if (Window.confirm(msg))
				    		  c.loadfirstStep();	      	
				    	 
				      }
				    });
			buttons.add(baccept);
			buttons.add(bcancel);
		
		}else{
			String msg="";
			if (lang.equals("es"))
				msg="Atras";
			else
				msg = "Previous";
			Button bAtras = new Button(msg, new ClickListener() {
			      public void onClick(Widget sender) {			    		     
				      c.loadfirstStep();	      				    	 
			      }
			    });
			
			buttons.add(bAtras);
		}
						
		
		buttons.setSpacing(8);		
		
		
		vp = new VerticalPanel();
		vp.add(tab);
		
		vp.add(buttons);
		vp.setCellHorizontalAlignment(buttons,HasHorizontalAlignment.ALIGN_CENTER);
		vp.setStyleName("lv-Sink");
		vp.setCellWidth(tab,"100%");
		vp.setCellHeight(tab,"100%");
		tab.setWidth("100%");
		
		vp.setWidth("100%");
		vp.setHeight("100%");
		
		initWidget(vp);
	}
	
	private void saveComponent(){
		
		
		IComponentAsync ass = (IComponentAsync)GWT.create(IComponent.class);
		ServiceDefTarget endpoint = (ServiceDefTarget)ass;				
	    endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponent");
	    
	    DTAggregateComponent dt = ad.getData();
	    if (dt!=null){
		    Vector flow = af.getFlow();
		    if (flow.size()>0){		    	
			    Vector params = ap.getParams();
			    if (params.size()==flow.size()){
				    Vector compParams = new Vector();
				    for(int i=0;i<flow.size();i++){
					 		compParams.add(new DTComponentParams((String)flow.get(i),((ParamsSettings)params.get(i)).getValues()));					
					}
				    
				    dt.setCompsParams(compParams);
				    
				    if (adding){
					    ass.addAggregateComponent(dt, new AsyncCallback(){
					    	public void onFailure(Throwable caught) {
					    		statusPopup.hide();
					    		Window.alert(caught.getMessage());
					    		
					    	}
					    	
					    	public void onSuccess(Object result) {
					    		statusPopup.hide();
					    		if (lang.equals("es"))
					    			Window.alert("Flujo agregado con éxito!");
					    		else
					    			Window.alert("Flow successfully added!");
					    		c.loadfirstStep();
					    		
					    	}
					    });    
				    }else{
				    	ass.updateAggregateComponent(dt, new AsyncCallback(){
					    	public void onFailure(Throwable caught) {
					    		statusPopup.hide();
					    		Window.alert(caught.getMessage());
					    		
					    	}
					    	
					    	public void onSuccess(Object result) {
					    		statusPopup.hide();
					    		if (lang.equals("es"))
					    			Window.alert("Flujo modificado con éxito!");
					    		else
					    			Window.alert("Flow successfully updated!");
					    		c.loadfirstStep();
					    		
					    	}
					    });
				    }
			    }else{
			    	statusPopup.hide();
			    	if (lang.equals("es"))
			    		Window.alert("Falta seleccion de parámetros");
			    	else
			    		Window.alert("Missing parametters selection");
			    }
		    }else{
		    	statusPopup.hide();
		    	if (lang.equals("es"))
		    		Window.alert("Error: Flujo Vacío");
		    	else
		    		Window.alert("Error: Empty Flow");
		    }
	    }else
	    	statusPopup.hide();
  	}
	
	public void loadData(DTAggregateComponent dt){
		if (dt!=null){
			ad.loadData(dt);
			af.loadData(dt);
			ap.loadData(dt);			
		}else
			if (lang.equals("es"))
				Window.alert("No se pudo cargar datos");
			else
				Window.alert("Error loading data");
	}

}
