/*
 * Created on 15/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.components;
import java.util.Collection;
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
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.datatypes.DTComponent;
import com.lavinia.gwt.user.client.interfaces.IComponent;
import com.lavinia.gwt.user.client.interfaces.IComponentAsync;
import com.lavinia.gwt.user.client.utils.StatusPopup;


/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AddComponent extends Composite {
	
	private TabPanel tab;
	private Button baccept;
	private Button bcancel;
	private HorizontalPanel buttons;
	private VerticalPanel vp;
	
	private ComponentData data;
	
	private Parameters params;
	private TypeSystem types;
	private Resources recs;
	
	private boolean adding=false;
	private boolean enabled=false;
	
	
	private ComponentsOptions c;
	
	private StatusPopup statusPopup = StatusPopup.getInstance();
	
	private String lang;
	
	public AddComponent(String l, ComponentsOptions component,boolean add, boolean enabled){
		this.adding=add;
		this.enabled = enabled;
		this.c=component;
		this.lang = l;
		
	    tab = new TabPanel();
		
	 
	   	data = new ComponentData(lang,adding,enabled);
	   	
	   	
	 
	    
	    params = new Parameters(lang,enabled);
	    types = new TypeSystem(lang,enabled);
	    recs = new Resources(lang,enabled);
		
	    if (lang.equals("es")){
		    tab.add(data,"Componente");		
			tab.add(params,"Parámetros");						
			tab.add(types,"Tipos");		
			tab.add(recs,"Recursos");
	    }else{
	    	tab.add(data,"Component");		
			tab.add(params,"Parametters");						
			tab.add(types,"Types");		
			tab.add(recs,"Resources");
	    }
		
		tab.selectTab(0);
		
		
		/********* BUTTONS**************/
		buttons = new HorizontalPanel();
		if (enabled){
			String msg ="";
			if (lang.equals("es"))
				msg = "Guardar";
			else
				msg = "Save";
			baccept = new Button(msg, new ClickListener() {
			      public void onClick(Widget sender) {
			    	  String msg="";
			    	  if ((adding)&&(lang.equals("es")))
			    		  msg="Seguro desea agregar el componente?";
			    	  else if ((!adding)&&(lang.equals("es")))
			    		  msg="Seguro desea modificar el componente?";
			    	  else if ((adding)&&(!lang.equals("es")))
			    		  msg="Add component?";
			    	  else if ((!adding)&&(!lang.equals("es")))
			    		  msg="Update component?";
			    	  if (Window.confirm(msg)){
				    	if (adding){
				    		if (lang.equals("es"))
				    			statusPopup.setText("Agregando componente...");
				    		else
				    			statusPopup.setText("Adding component...");
				    		statusPopup.show();
				    	}else{
				    		if (lang.equals("es"))
				    			statusPopup.setText("Actualizando componente...");
				    		else
				    			statusPopup.setText("Updating component...");
				    		statusPopup.show();
				    	}
				        saveComponent();
				        
				        
			    	  
			    	  }
			       		    	  
			      }
			    });
			if (lang.equals("es"))
				msg = "Cancelar";
			else
				msg = "Cancel";
			bcancel = new Button(msg, new ClickListener() {
			      public void onClick(Widget sender) {
			    	  String msg ="";
			    	  if (lang.equals("es"))
			    		  msg = "Desea cancelar la operación?";
			    	  else
			    		  msg = "Cancel operation?";
			    			  
			    	  if (Window.confirm(msg))
			    		  c.loadfirstStep();
				     
			      }
			    });
			
			
			buttons.add(baccept);
			buttons.add(bcancel);
		}else{
			String msg = "";
			if (lang.equals("es"))
				msg = "Atras";
			else
				msg = "Previous";
			Button bAtras =new Button(msg, new ClickListener() {
			      public void onClick(Widget sender) {			      	
				      		c.loadfirstStep();	      
				      }
				    });
			buttons.add(bAtras);
		}
		
		/*********************************/
		
		vp = new VerticalPanel();
		vp.add(tab);
		buttons.setSpacing(8);
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
	
	public void saveComponent(){
		Collection p = params.saveParameters();
		Collection t = types.saveTypes();
		
		DTComponent dt = new DTComponent();
		
		
		try{			
			boolean error=false;
			if ((adding)||(enabled)){
				dt =data.saveData();
				error=data.SubmitFiles();
		    }else{
		    	dt =data.saveData();
		    }				
			
			Collection r = recs.saveResources(dt.getName());
			dt.setResources(r);
			dt.setParameters(p);
			dt.setTypes(t);
			
			if (!error){			
				IComponentAsync ass = (IComponentAsync)GWT.create(IComponent.class);
				ServiceDefTarget endpoint = (ServiceDefTarget)ass;				
			    endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponent");
			    
			    if (adding){
					ass.addSimpleComponent(dt, new AsyncCallback() {
					        public void onFailure(Throwable caught) {  	
					        	statusPopup.hide();
					        	Window.alert(caught.getMessage());					        	
					          }
			
					          public void onSuccess(Object result) {
					        	  statusPopup.hide();
					        	  if (lang.equals("es"))
					        		  Window.alert("Componente Agregado!");
					        	  else
					        		  Window.alert("Component successfully added!");
					        		  
					          }
					});
			    }else{
			    	Vector removed = recs.getRemovedResources();
			    	ass.updateSimpleComponent(removed,dt, new AsyncCallback() {
				        public void onFailure(Throwable caught) {  	
				        	statusPopup.hide();
				        	Window.alert(caught.getMessage());			        	
				          }
		
				          public void onSuccess(Object result) {
				        	  statusPopup.hide();
				        	  if (lang.equals("es"))
				        		  Window.alert("Componente Modificado!");
				        	  else
				        		  Window.alert("Component successfully updated!");
				        	  c.loadfirstStep();				    	    
				          }
				});
			    }
			}else
				statusPopup.hide();
				
		}catch (Exception e){
			statusPopup.hide();
			Window.alert(e.getMessage());
		}
	}
	
	public void loadComponent(DTComponent dt){
		if (dt!=null){
			data.loadData(dt);
			params.loadParameters(dt.getParameters());
			types.loadTypes(dt.getTypes());		
			recs.loadResources(dt.getResources());
			statusPopup.hide();
		}else
			if (lang.equals("es"))
				Window.alert("No se pudo cargar datos");
			else
				Window.alert("Error loading data");
	}
	



}
