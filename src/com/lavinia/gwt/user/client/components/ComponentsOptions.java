package com.lavinia.gwt.user.client.components;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponent;
import com.lavinia.gwt.user.client.interfaces.IComponent;
import com.lavinia.gwt.user.client.interfaces.IComponentAsync;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfoAsync;

public class ComponentsOptions extends Composite{
	
	private IComponentsInfoAsync ass;
	
	private ServiceDefTarget endpoint;
	
	
	
	private Label seleccioneElComponenteLabel;
	private VerticalPanel links;
	private VerticalPanel vp1;
	private ListBox listBox;
	private String lang;
	
	
	
	public ComponentsOptions(String l){
		this.lang = l;
		
		ass = (IComponentsInfoAsync)GWT.create(IComponentsInfo.class);
		endpoint = (ServiceDefTarget)ass;			
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsInfo");
		
		
		
		vp1 = new VerticalPanel();
		
		vp1.setStyleName("lv-Sink");		
		
		initWidget(vp1);
		vp1.setSize("100%", "100%");
		
		

		firstStep();
	}
	
	public void firstStep(){
		
		VerticalPanel vp2 = new VerticalPanel();
		vp2.setSpacing(10);
		
		if (lang.equals("es"))
			seleccioneElComponenteLabel = new Label("Seleccione el componente:");
		else
			seleccioneElComponenteLabel = new Label("Select a component:");
		
		vp2.add(seleccioneElComponenteLabel);
		vp2.setCellHeight(seleccioneElComponenteLabel, "50");

		listBox = new ListBox();		
		listBox.setSelectedIndex(0);		
		listBox.setWidth("150");
		listBox.setVisibleItemCount(15);
		loadList();

		/****** links ******/
		
		Hyperlink agregarSimple, agregarCompuesto, ver, modificar, eliminar;
		if (lang.equals("es"))
			agregarSimple = new Hyperlink("Agregar Componente","addSimple");
		else
			agregarSimple = new Hyperlink("Add Component","addSimple");
		if (lang.equals("es"))
			agregarCompuesto = new Hyperlink("Agregar Flujo", "addAggregate");
		else
			agregarCompuesto = new Hyperlink("Add Flow", "addAggregate");
		if (lang.equals("es"))
			ver = new Hyperlink("Ver","show");
		else
			ver = new Hyperlink("Show","show");
		if (lang.equals("es"))
			modificar = new Hyperlink("Modificar","update");
		else
			modificar = new Hyperlink("Update","update");
		if (lang.equals("es"))
			eliminar = new Hyperlink("Eliminar","remove");
		else
			eliminar = new Hyperlink("Delete","remove");
		
		links = new VerticalPanel();
		if (lang.equals("es"))
			links.add(new Label("Opciones:"));
		else
			links.add(new Label("Options:"));
		links.add(agregarSimple);
		links.add(agregarCompuesto);
		links.add(ver);
		links.add(modificar);
		links.add(eliminar);
		links.setSpacing(12);
		links.setStyleName("lv-Sink");
		
		agregarSimple.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				agregarSimple();
				
				// DESCOMENTAR ESTAS LINEAS PARA LA VERSION INTERNET
				/*******************************************************/
			/*	if (lang.equals("es"))
						Window.alert("Esta funcionalidad no esta disponible");
				else
					Window.alert("Not Available");*/
				/********************************************************/
			}
		});
		
		agregarCompuesto.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				agregarCompuesto();
				
				// DESCOMENTAR ESTAS LINEAS PARA LA VERSION INTERNET
				/*******************************************************/
				/*if (lang.equals("es"))
					Window.alert("Esta funcionalidad no esta disponible");
				else
					Window.alert("Not Available");*/
				/*******************************************************/
				
				
			}
		});
		
		ver.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {				
				if (listBox.getSelectedIndex()>=0){
					String name = listBox.getItemText(listBox.getSelectedIndex());
					ass.isSimpleComponent(name, new AsyncCallback() {
				        public void onFailure(Throwable caught) {	        		        	
			        		Window.alert(caught.getMessage());	        	
			          }

			          public void onSuccess(Object result) {
			        	  String name = listBox.getItemText(listBox.getSelectedIndex());
			        	  if (((Boolean)result).booleanValue())
			        		  ver(name);
			        	  else
			        		  verCompuesto(name);
			          }
			    });
					
					
				}else
					if (lang.equals("es"))
						Window.alert("Debe seleccionar un componente");
					else
						Window.alert("You must select a component");
			}
		});
		
		modificar.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {	
				/* COMENTAR EL CODIGO QUE SIGUE PARA LA VERSION INTERNET */
				/********************************************************************/
				if (listBox.getSelectedIndex()>=0){
					String name = listBox.getItemText(listBox.getSelectedIndex());
					ass.isSimpleComponent(name, new AsyncCallback() {
				        public void onFailure(Throwable caught) {	        		        	
			        		Window.alert(caught.getMessage());	        	
			          }

			          public void onSuccess(Object result) {
			        	  String name = listBox.getItemText(listBox.getSelectedIndex());
			        	  if (((Boolean)result).booleanValue())
			        		  modificar(name);
			        	  else
			        		  modificarCompuesto(name);
			          }
					});
				}else
					if (lang.equals("es"))
						Window.alert("Debe seleccionar un componente");
					else
						Window.alert("You must select a component");
				
				/********************************************************************/
				
				/* DESCOMENTAR LA LINEA QUE SIGUE PARA LA VERSION INTERNET */
				/*if (lang.equals("es"))
					Window.alert("Esta funcionalidad no esta disponible");
				else
					Window.alert("Not Available");*/
				 
			}
		});
		
		eliminar.addClickListener(new ClickListener(){			
			public void onClick(Widget sender) {		
				/* COMENTAR EL CODIGO QUE SIGUE PARA LA VERSION INTERNET */
				/********************************************************************/
				if (listBox.getSelectedIndex()>=0){
					if (listBox.getSelectedIndex()>=0){					
						String name = listBox.getItemText(listBox.getSelectedIndex());
						String msg1 = "";
						if (lang.equals("es"))
							msg1 = "Seguro desea eliminar componente?\n"+
							"ADVERTENCIA: los recursos locales se eliminan";
						else
							msg1 = "Delete the selected component?\n"+
							"WARNING: local resources will be deleted";
						if (Window.confirm(msg1))
							remove(name,listBox.getSelectedIndex());
					}else
						if (lang.equals("es"))
							Window.alert("Debe seleccionar un componente");
						else
							Window.alert("You must select a component");
				}else
					if (lang.equals("es"))
						Window.alert("Debe seleccionar un componente");
					else
						Window.alert("You must select a component");
				
				/********************************************************************/
				/* DESCOMENTAR LA LINEA QUE SIGUE PARA LA VERSION INTERNET */
				/*if (lang.equals("es"))
					Window.alert("Esta funcionalidad no esta disponible");
				else
					Window.alert("Not Available");*/
				 
			}
		});
		
		vp1.add(vp2);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(listBox);
		hp.add(links);
		hp.setSpacing(20);
		vp1.add(hp);
		vp1.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
		
		
	}
	
	private void ver(final String name){
		
		
		ass.getComponentFullData(name, new AsyncCallback() {
	        public void onFailure(Throwable caught) {	        		        	
	        		Window.alert(caught.getMessage());	        	
	          }

	          public void onSuccess(Object result) {
	        	  	loadDataShow(name, (DTComponent)result);		      		
	          }
	    });	 
			
	}
	
	private void modificar(final String name){
		ass.getComponentFullData(name, new AsyncCallback() {
	        public void onFailure(Throwable caught) {	        		        	
	        	Window.alert(caught.getMessage());
	        	
	          }

	          public void onSuccess(Object result) {
	        	  loadData(name, (DTComponent)result);
	        	  	 	          	
		      		
	          }
	    });	 
	}
	private void verCompuesto(String name){
		
		
		final AddAggregate addc = new AddAggregate(lang,this,false,false);
		
		ass.getAggregateData(name, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}
			public void onSuccess(Object result) {
				
				addc.loadData((DTAggregateComponent)result);
				vp1.clear();
		  		vp1.removeStyleName("lv-Sink");
		  		
		  		addc.setWidth("100%");
		  		addc.setHeight("100%");
		  		
		  		vp1.add(addc);
		  		vp1.setCellWidth(addc, "100%");
		  		vp1.setCellHeight(addc, "100%");
		  		vp1.setWidth("100%");
		  		vp1.setHeight("100%");;
				
			}
		});
				
									
	}
	
	private void modificarCompuesto(String name){
		
		final AddAggregate addc = new AddAggregate(lang,this,true,false);
		
		ass.getAggregateData(name, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}
			public void onSuccess(Object result) {
				addc.loadData((DTAggregateComponent)result);
				vp1.clear();
		  		vp1.removeStyleName("lv-Sink");
		  		
		  		addc.setWidth("100%");
		  		addc.setHeight("100%");
		  		
		  		vp1.add(addc);
		  		vp1.setCellWidth(addc, "100%");
		  		vp1.setCellHeight(addc, "100%");
		  		vp1.setWidth("100%");
		  		vp1.setHeight("100%");;
				
			}
		});
		
		
	}
	
	private void agregarSimple(){
		vp1.clear();
		 
		AddComponent addc = new AddComponent(lang,this,true,true);
		
		vp1.clear();
  		vp1.removeStyleName("lv-Sink");
  		
  		addc.setWidth("100%");
  		addc.setHeight("100%");
  		
  		vp1.add(addc);
  		vp1.setCellWidth(addc, "100%");
  		vp1.setCellHeight(addc, "100%");
  		vp1.setWidth("100%");
  		vp1.setHeight("100%");;		
	}
	
	private void agregarCompuesto(){
		vp1.clear();
		 
		AddAggregate addc = new AddAggregate(lang,this,true,true);
		
		vp1.clear();
  		vp1.removeStyleName("lv-Sink");
  		
  		addc.setWidth("100%");
  		addc.setHeight("100%");
  		
  		vp1.add(addc);
  		vp1.setCellWidth(addc, "100%");
  		vp1.setCellHeight(addc, "100%");
  		vp1.setWidth("100%");
  		vp1.setHeight("100%");;		
	}
	
	public void loadData(String name, DTComponent dt){
		vp1.clear();
		AddComponent addc = new AddComponent(lang,this,false,true); 		
  		addc.loadComponent(dt);     		
  		vp1.clear();
  		vp1.removeStyleName("lv-Sink");
  		
  		addc.setWidth("100%");
  		addc.setHeight("100%");
  		
  		vp1.add(addc);
  		vp1.setCellWidth(addc, "100%");
  		vp1.setCellHeight(addc, "100%");
  		vp1.setWidth("100%");
  		vp1.setHeight("100%");;		
	}
	public void loadDataShow(String name, DTComponent dt){
		vp1.clear();
		AddComponent addc = new AddComponent(lang,this,false,false); 		
  		addc.loadComponent(dt);     		
  		vp1.clear();
  		vp1.removeStyleName("lv-Sink");
  		addc.setWidth("100%");
  		addc.setHeight("100%");
  		
  		vp1.add(addc);
  		vp1.setCellWidth(addc, "100%");
  		vp1.setCellHeight(addc, "100%");
  		vp1.setWidth("100%");
  		vp1.setHeight("100%");
	}
	
	public void loadList(){

		ass.getComponents(new AsyncCallback() {
	        public void onFailure(Throwable caught) {	        		        	
	        	Window.alert(caught.getMessage());
	          }

	          public void onSuccess(Object result) {
	        	  listBox.clear();
		          	Collection col = (Collection)result;
		          	Iterator it = col.iterator();
		          	while (it.hasNext()){
		          		listBox.addItem((String)it.next());
		          	}  	          	
	          }
	    });	    
	}
	
	private void remove(String name, final int index){
		IComponentAsync ass2 = (IComponentAsync)GWT.create(IComponent.class);
		ServiceDefTarget endpoint2 = (ServiceDefTarget)ass2;			
		endpoint2.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponent");
		ass2.removeComponent(name, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				listBox.removeItem(index);
				Window.alert(caught.getMessage());
			}
			public void onSuccess(Object result) {	
				listBox.removeItem(index);
				if (lang.equals("es"))
					Window.alert("Componente eliminado!");
				else
					Window.alert("Component successfully deleted!");
				
			}
		});
	}
	
	public void loadfirstStep(){
		ass.getComponents(new AsyncCallback() {
	        public void onFailure(Throwable caught) {	        		        	
	        	Window.alert(caught.getMessage());
	          }

	          public void onSuccess(Object result) {
	        	  	listBox.clear();
		          	Collection col = (Collection)result;
		          	Iterator it = col.iterator();
		          	while (it.hasNext()){
		          		listBox.addItem((String)it.next());
		          	}  	   
		        	vp1.clear();
		    		VerticalPanel vp2 = new VerticalPanel();
		    		vp2.setSpacing(10);
		    		vp2.add(seleccioneElComponenteLabel);
		    		vp2.setCellHeight(seleccioneElComponenteLabel, "50");
		    		
		    		vp1.add(vp2);
		    		HorizontalPanel hp = new HorizontalPanel();
		    		hp.add(listBox);
		    		hp.add(links);
		    		hp.setSpacing(20);
		    		
		    		vp1.add(hp);
		    		vp1.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_CENTER);
		    		
		    	
		    		vp1.setStyleName("lv-Sink");
	          }
	    });	    
	
	}
}
