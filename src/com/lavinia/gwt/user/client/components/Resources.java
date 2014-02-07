package com.lavinia.gwt.user.client.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.datatypes.DTResource;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;
import com.lavinia.gwt.user.client.interfaces.IResources;
import com.lavinia.gwt.user.client.interfaces.IResourcesAsync;

public class Resources extends Composite{
	
	private TableResources table;
	private boolean showingRec=false;
	private VerticalPanel newRec;
	private VerticalPanel main;
	private ArrayList infoRecs=new ArrayList();
	private IResourcesAsync ass;
	private ServiceDefTarget endpoint;

	private HorizontalPanel tablePanel;
	
	private Widget tname;
	private TextBox tinterface=new TextBox();
	private TextBox timp=new TextBox();
	private Hyperlink hlrem;
	
	private TextArea tdescription;
	
	private int count = 0;
	
	private Vector resources;
	
	private ListBox lb;
	
	private FormPanel form;
	
	private FileUpload fup= new FileUpload();
	
	private HashMap fups = new HashMap();
	private HashMap gfups = new HashMap();
	
	private boolean sub =false;
	
	private boolean checked=false;
	
	private Vector removedResources=new Vector();

	private String lang;
	
	public Resources(String l, boolean enabled){
		this.lang = l;
		ass = (IResourcesAsync)GWT.create(IResources.class);
		endpoint = (ServiceDefTarget)ass;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IResources");
		
		loadResources();
		
		table = new TableResources(lang);
		tablePanel = new HorizontalPanel();
		main = new VerticalPanel();
		
        if (enabled){
	        Hyperlink hladd;
	        if (lang.equals("es"))
	        	hladd = new Hyperlink("Agregar Recurso","AddResource");        
	        else
	        	hladd = new Hyperlink("Add Resource","AddResource");
			hladd.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			        if (!showingRec){
			        	addResource(true);		        	
			        	main.add(newRec);
			        	showingRec=true;
			        }
			        
			      }
			    });
			
			 Hyperlink hlimp;
			 if (lang.equals("es"))
				 hlimp = new Hyperlink("Importar Recurso","AddResource");
			 else
				 hlimp = new Hyperlink("Import Resource","AddResource");
				hlimp.addClickListener(new ClickListener() {
				      public void onClick(Widget sender) {
				        if (!showingRec){
				        	addResource(false);		        	
				        	main.add(newRec);
				        	showingRec=true;
				        }
				        
				      }
				    });
			if (lang.equals("es"))
				hlrem = new Hyperlink("Eliminar Recurso","RemResource");
			else
				hlrem = new Hyperlink("Delete Resource","RemResource");
	        hlrem.setVisible(false);
			hlrem.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			    	  if (table.getSelectedRow()>0){
			    		  String msg = "";
			    		  if (lang.equals("es"))
			    			  msg = "Seguro desea dejar de utilizar el recurso seleccionado?";
			    		  else
			    			  msg = "Release selected resource?";
				    	  if (Window.confirm(msg)){
						      	String name = table.getNameSelected();
						        removeRecInfo(name);
						        if (fups.containsKey(name))
						        	fups.remove(name);
						        else
						        	gfups.remove(name);			        	
						        if (table.removeSelectedRow()){
						        	hlrem.setVisible(false);
						        	count=0;
						        	tablePanel.remove(table);
						        	
						        }
						        if (!table.isGlobalSelected())
						        	removedResources.add(name);
				    	  }
			    	  }
			      }
			    });
	
			
	        
	        
	        VerticalPanel hpopt = new VerticalPanel();
	        hpopt.add(hladd);
	        hpopt.add(hlimp);
	        hpopt.add(hlrem);
	        hpopt.setSpacing(4);
	        
			main.add(hpopt);
        }
        
        
		main.add(tablePanel);		
        main.setCellHorizontalAlignment(tablePanel,HasHorizontalAlignment.ALIGN_CENTER);
		main.setSpacing(8);
		infoRecs = new ArrayList();
		
		
		
		
		/**************************************************/
		form = new FormPanel();		
	   	form.setAction(GWT.getModuleBaseURL()+"uploadResource");	   	
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);	    
	    
	    
	    form.setWidget(main);
	    main.setSize("100%", "100%");
	    
	    form.addFormHandler(new FormHandler() {
	        public void onSubmitComplete(FormSubmitCompleteEvent event) {
	        	
		        if (!sub){
		        	form.setAction(GWT.getModuleBaseURL()+"uploadGResource");
		    		
		    		Iterator it =fups.values().iterator();
		    		while (it.hasNext()){			
		    			main.remove((FileUpload)it.next());
		    		}
		    		
		    		it =gfups.values().iterator();
		    		while (it.hasNext()){
		    			FileUpload fup2 = (FileUpload)it.next();
		    			fup2.setVisible(false);
		    			main.add(fup2);
		    		}
		    		if (gfups.size()>0)
		    			form.submit();
		    		
		    		sub=true;
		        }
	        }

	        public void onSubmit(FormSubmitEvent event) {
	        
	        }
	        
	      });
	    
	    initWidget(form);
	}
	
	private void addResource(final boolean nuevo){
		
		/********LABELS***********************/
		Label lname, ldescription, linterface, limp;
		
		if (lang.equals("es")){
			lname = new Label("Nombre");
			lname.setWidth("160");
			
			ldescription = new Label("Descripción");
			ldescription.setWidth("160");
			
			linterface = new Label("Interface (opcional)");
			linterface.setWidth("160");
			
			limp = new Label("Implementación (opcional)");
			limp.setWidth("160");
		}else{
			lname = new Label("Name");
			lname.setWidth("160");
			
			ldescription = new Label("Description");
			ldescription.setWidth("160");
			
			linterface = new Label("Interface (optional)");
			linterface.setWidth("160");
			
			limp = new Label("Implementation (optional)");
			limp.setWidth("160");
		}
	
		
		
		/*******TEXTBOXS AND OTHERS***********/
	
		if (nuevo){
			tname = new TextBox();			
		}else{
			tname = lb;			
		}
		tname.setWidth("260");
		
		tdescription = new TextArea();
		tdescription.setWidth("260");	    
		tdescription.setVisibleLines(4);
		
		
		
		tinterface = new TextBox();
		tinterface.setWidth("260");
		
		timp = new TextBox();
		timp.setWidth("260");
		
		HorizontalPanel hp7=null,hp8=null;
	
		if (nuevo){
			Label lfile, lav;
			if (lang.equals("es")){
				lfile = new Label("Archivo");
				lfile.setWidth("160");
				
				lav = new Label("Disponible globalmente");
				lav.setWidth("156");
			}else{
				lfile = new Label("File");
				lfile.setWidth("160");
				
				lav = new Label("Globally Available");
				lav.setWidth("156");
			}
			
			fup = new FileUpload();
			fup.setWidth("260");
			fup.setName("fup");
			CheckBox cb = new CheckBox();
			checked=false;
			
			cb.addClickListener(new ClickListener(){
				
				public void onClick(Widget sender) {		
					checked=!checked;
				}
			});			
		    
		    hp7 = new HorizontalPanel();
		    hp7.add(lfile);
		    hp7.add(fup);
		    
		    hp8 = new HorizontalPanel();
		    hp8.add(lav);
		    hp8.add(cb);
		}else{
			if (lb.getItemCount()>0)
				tdescription.setText(lb.getValue(0));
			checked=true;
			fup= new FileUpload();
		}
			
		
		
		
		/********BUTTONS***********************/
		
		Button badd = new Button();
		Button bcancel = new Button();
		if (lang.equals("es")){
			badd.setText("Agregar");
			bcancel.setText("Cancelar");
		}else{
			badd.setText("Add");
			bcancel.setText("Cancel");
		}
		
		badd.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {	
		    	
		      	addRecInfo(nuevo);
		      	
		      }
		    });
		
		bcancel.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		        main.remove(newRec);
		        showingRec=false;
		      }
		    });
		
	    
	    /*********PANELS***********************/
	    
	    HorizontalPanel hp1 = new HorizontalPanel();
	    hp1.add(lname);
	    hp1.add(tname);
	   // hp1.add(hluse);	    
	    
	    HorizontalPanel hp3 = new HorizontalPanel();
	    hp3.add(ldescription);
	    hp3.add(tdescription);
	    
	    HorizontalPanel hp4 = new HorizontalPanel();
	    hp4.add(linterface);
	    hp4.add(tinterface);
	    
	    HorizontalPanel hp5 = new HorizontalPanel();
	    hp5.add(limp);
	    hp5.add(timp);
	    
	    HorizontalPanel hpb = new HorizontalPanel();
	    hpb.add(badd);
	    hpb.add(bcancel);
	    hpb.setSpacing(8);
	    
	    
	    
	    
	    
	    /**********MAIN PANEL**********************/
	    
	    newRec = new VerticalPanel();
	    newRec.add(hp1);	    
	    newRec.add(hp3);
	    newRec.add(hp4);
	    newRec.add(hp5);    
	    
	    
	    if (nuevo){
		    newRec.add(hp7);
		    newRec.add(hp8);
	    }
	    
	    newRec.add(hpb);
	    
	    newRec.setCellHorizontalAlignment(hpb,HasHorizontalAlignment.ALIGN_CENTER);
	    newRec.setSpacing(8);
	    newRec.setStyleName("lv-Sink");
	    
	    
		
	}
	private void loadResources(){
		
		ass.getAllResources(new AsyncCallback(){

			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}

			public void onSuccess(Object result) {
			
				resources = (Vector)result;
				lb = new ListBox();			
				
				
				Iterator it =resources.iterator();
				while (it.hasNext()){
					DTResourceData dtd = (DTResourceData)it.next();
					lb.addItem(dtd.getName(),dtd.getDescription());					
				}
				
				
				lb.addChangeListener(new ChangeListener(){
					public void onChange(Widget sender) {						
						tdescription.setText(lb.getValue(lb.getSelectedIndex()));					
					}					
				});
				
				
			}			
		});
	}
	
	private void addRecInfo(boolean nuevo){
		String name="";
		boolean error = false;
		
		if (nuevo){
			name = ((TextBox)tname).getText();
			if (fup.getFilename().equals("")){
				if (lang.equals("es"))
					Window.alert("Debe seleccionar un archivo");
				else
					Window.alert("Missing file");
				error=true;
			}
			
		}else
			name = ((ListBox)tname).getItemText(((ListBox)tname).getSelectedIndex());
		
		if ((!error)&&(name.length()>0)){
			if ((name.indexOf(" ")<0)&&(name.indexOf(".")<0)){
				if (tdescription.getText().length()>0){					
					if (((tinterface.getText().length()>0)&&(timp.getText().length()>0)) ||
							((tinterface.getText().length()<=0)&&(timp.getText().length()<=0))){
						
						infoRecs.add(new DTResource(name,tdescription.getText(),tinterface.getText(),timp.getText(),fup.getFilename(),checked,nuevo));
						
						if (count==0){
							tablePanel.add(table);		
							hlrem.setVisible(true);
						}
						
						table.addRow(name,checked,tinterface.getText(),timp.getText(),tdescription.getText());
				      	count++;
				      
				        main.remove(newRec);	
				        showingRec=false;
				        
				    	if (checked){
				    		
				      		gfups.put(name,fup);			      		
				      	}else{
				      		
				      		fups.put(name,fup);			      		
				      	}
					}else{
						if (lang.equals("es"))
							Window.alert("Debe ingresar el nombre de la interface y su implementación");
						else
							Window.alert("Missing interface and implementation");
					}
				}else{
					if (lang.equals("es"))
						Window.alert("Ingrese descripción del recurso");
					else
						Window.alert("Missing resource description");
				}
			}else{
				if (lang.equals("es"))
					Window.alert("Nombre de recurso inválido");
				else
					Window.alert("Invalid resource name");
			}
		}else{
			if (lang.equals("es"))
				Window.alert("Ingrese nombre del recurso");
			else
				Window.alert("Missing resource name");
			
		}
	}
	
	private void removeRecInfo(String name){
		Iterator it = infoRecs.iterator();
		DTResource dt;
		boolean end=false;
		while((!end)&&(it.hasNext())){
			dt = (DTResource)it.next();
			if (dt.getName().equals(name)){
				it.remove();
				end=true;
			}
		}
	}
	
	public Collection saveResources(String componentName){
		
		form.setAction(GWT.getModuleBaseURL()+"uploadResource?name="+componentName);
		
		Iterator it =fups.values().iterator();
		while (it.hasNext()){
			FileUpload fup2 = (FileUpload)it.next();
			fup2.setVisible(false);
			main.add(fup2);
		}
	
		if (fups.size()>0)
			form.submit();
		else{
			sub=true;
			
			form.setAction(GWT.getModuleBaseURL()+"uploadGResource");
    		
    		it =gfups.values().iterator();
    		while (it.hasNext()){
    			FileUpload fup2 = (FileUpload)it.next();
    			fup2.setVisible(false);
    			main.add(fup2);
    		}
    		if (gfups.size()>0)
    			form.submit();
    		
    		
		}
				
		return infoRecs;
	}
	
	public void loadResources(Collection p){		
		infoRecs.addAll(p);
		DTResource dtp;
			
		Iterator it1 = p.iterator();
		
		while (it1.hasNext()){
			dtp = (DTResource)it1.next();
			table.addRow(dtp.getName(),dtp.isGlobal(),dtp.getInter(),dtp.getImpl(),dtp.getDescription());
			count++;
		}
		if (count>0){
			tablePanel.add(table);
			if (hlrem!=null)
				hlrem.setVisible(true);
		}
	}
	
	public Vector getRemovedResources(){
		return removedResources;
	}
}

