/*
 * Created on 15/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.components;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.datatypes.DTComponent;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComponentData extends Composite{
	
	
	private Vector panels;
	private Integer index;
	private TextBox tname; 
	private TextBox tautor;
	private TextArea tdescription;
	private TextBox filePackage; 
	private FileUpload mainFile;
	private VerticalPanel panel= new VerticalPanel();
	final FormPanel form;
	
	private boolean adding;
	private boolean enabled;
	
	private String lang;
	
	public ComponentData(String l,boolean a, boolean e){
		this.lang=l;
		this.adding = a;
		this.enabled = e;
	
		/********LABELS**************************/
		Label lbname, lbautor,lbdescription,lbpackage;
		
		if (lang.equals("es")){
			lbname = new Label("Nombre");
			lbname.setWidth("140");
			lbautor = new Label("Autor");
			lbautor.setWidth("140");
			lbdescription = new Label("Descripción");
			lbdescription.setWidth("140");
			lbpackage = new Label ("Paquete");
			lbpackage.setWidth("140");
		}else{
			lbname = new Label("Name");
			lbname.setWidth("140");
			lbautor = new Label("Autor");
			lbautor.setWidth("140");
			lbdescription = new Label("Description");
			lbdescription.setWidth("140");
			lbpackage = new Label ("Package");
			lbpackage.setWidth("140");
		}
		
		
		/*******TEXTBOXS AND TEXTAREAS***********/
		tname = new TextBox();
		tname.setName("componentName");
		tname.setWidth("260");
		tname.setEnabled(enabled);
		if (!adding)
			tname.setEnabled(false);
		
		tautor = new TextBox();
		tautor.setWidth("260");
		tautor.setEnabled(enabled);
		
		tdescription = new TextArea();
		tdescription.setWidth("260");	    
		tdescription.setVisibleLines(4);
		tdescription.setEnabled(enabled);
		
		filePackage = new TextBox();
		filePackage.setWidth("260");
		filePackage.setEnabled(enabled);
		
		/*******HORIZONTAL PANELS****************/
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(lbname);
		hp1.add(tname);
		
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(lbautor);
		hp2.add(tautor);
		
		HorizontalPanel hp3 = new HorizontalPanel();
		hp3.add(lbdescription);
		hp3.add(tdescription);
		
		HorizontalPanel hp4 = new HorizontalPanel();
		hp4.add(lbpackage);
		hp4.add(filePackage);
		
		/***FORM TO UPLOAD****************************/
	
		form = new FormPanel();
		
	   	form.setAction(GWT.getModuleBaseURL()+"upload");
	   	
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);	    
	    
	    
	    panel = new VerticalPanel();
	    form.setWidget(panel);
	    panel.setHeight("100%");
	
	    
	    /******MAIN PANEL***************************/		
		panel.add(hp1);
	    panel.add(hp2);
	    panel.add(hp3);
	    panel.add(hp4);
	    
	    form.addFormHandler(new FormHandler() {
	        public void onSubmitComplete(FormSubmitCompleteEvent event) {
	         
	        }

	        public void onSubmit(FormSubmitEvent event) {
          
	        }
	      });

	    
		/*********FILE**************************/
	    	if (enabled){
	    		mainFile = new FileUpload();
	    		mainFile.setWidth("360");
	    		mainFile.setName("uploadFormElement");
	    		HorizontalPanel hpfile = new HorizontalPanel();
	    		Label lbfile;
	    		if (lang.equals("es"))
	    			lbfile = new Label("Archivo Principal");
	    		else
	    			lbfile = new Label("Main File");
	    		lbfile.setWidth("140");
	    		hpfile.add(lbfile);		
	    		hpfile.add(mainFile);
	    		panel.add(hpfile);
	    	}
			
			
			
		    /**********ADITIONAL FILES**************/
			
	    	if (enabled){
			    Hyperlink hadfile;
			    if (lang.equals("es"))
			    	hadfile= new Hyperlink("Agregar Archivo","addfile");
			    else
			    	hadfile= new Hyperlink("Add File","addfile");
			    hadfile.setWidth("140");
			    
			    panels = new Vector();
			   		
		        panel.add(hadfile);
				
				index=new Integer(0);
				
			    hadfile.addClickListener( new ClickListener() {
			    	public void onClick(Widget sender) {
			    		HorizontalPanel hpfile = new HorizontalPanel();
			    		panels.add(hpfile);
			    		
			    		FileUpload fup = new FileUpload();
			    		fup.setName("uploadFormElement"+index);
			    		
			    		Label lbfile = new Label(" ");
			    		Hyperlink hyp;
			    		if (lang.equals("es"))
			    			hyp= new Hyperlink("eliminar", index.toString());
			    		else
			    			hyp= new Hyperlink("remove", index.toString());
			    		index = new Integer(index.intValue()+1);
			    			    		
			    		hyp.addClickListener(new ClickListener(){
			    			public void onClick(Widget sender){
			    				panel.remove((HorizontalPanel)(panels.get((new Integer((((Hyperlink)sender).getTargetHistoryToken()))).intValue())));
			    					    				
							}
			    		});
			    		
			    		lbfile.setWidth("132");
			    		fup.setWidth("360");
			    		hpfile.add(lbfile);
			    		hpfile.add(fup);
			    		hpfile.add(hyp);
			    		hpfile.setSpacing(4);
			    		
			    		panel.add(hpfile);	    		
			    	}
					});
	    	}
		      
		
	    panel.setSpacing(8);
		VerticalPanel vp = new VerticalPanel();
		vp.add(form);
		
		initWidget(form);
		
		
	}
	
	public boolean SubmitFiles(){
		boolean error=false;
		if (mainFile.getFilename().length()>0){			
			int b1 = mainFile.getFilename().lastIndexOf("\\");
			int b2 = mainFile.getFilename().lastIndexOf("/");			
			if (b1>b2){
				if (!(mainFile.getFilename().substring(b1+1, mainFile.getFilename().length())).equalsIgnoreCase(tname.getText()+".jar")){
					if (lang.equals("es"))
						Window.alert("El archivo principal debe ser "+tname.getText()+".jar");
					else
						Window.alert("The main file name must be "+tname.getText()+".jar");
					error=true;	
				}
			}else if (b2>b1){
				if (!(mainFile.getFilename().substring(b2+1, mainFile.getFilename().length())).equalsIgnoreCase(tname.getText()+".jar")){
					if (lang.equals("es"))
						Window.alert("El archivo principal debe ser "+tname.getText()+".jar");
					else
						Window.alert("The main file name must be "+tname.getText()+".jar");
					error=true;	
				}
			}
			
		}else if (adding){
			if (lang.equals("es"))
				Window.alert("Archivo Principal Requerido");
			else
				Window.alert("Missing main file");
			error=true;
		}
			
		if (!error){
			form.submit();
		}
		return error;
	}
	
	public DTComponent saveData() throws Exception{
		DTComponent dt = new DTComponent();		
		if (tname.getText().length()>0){			
			if ((tname.getText().indexOf(" ")==-1)||(tname.getText().indexOf(".")==-1)){
				
				if (tautor.getText().length()>0){
					
					if (tdescription.getText().length()>0){
						if ((filePackage.getText().indexOf(" ")<=0)&&(filePackage.getText().length()>0)&&(!filePackage.getText().endsWith("."))){
							dt.setName(tname.getText());
							dt.setAutor(tautor.getText());
							dt.setDescription(tdescription.getText());
							
							dt.setPack(filePackage.getText());
							
							
						}else{
							String msg="";
							if (lang.equals("es"))
								msg = "Nombre de paquete inválido";
							else
								msg = "Invalid package";
							throw new Exception (msg);
						}						
					}else{
						String msg="";
						if (lang.equals("es"))
							msg = "Ingrese una descripción";
						else
							msg = "Missing description";
						throw new Exception (msg);
					}
				}else{
					String msg="";
					if (lang.equals("es"))
						msg = "Ingrese nombre del autor";
					else
						msg = "Missing autor name";
					throw new Exception (msg);
				}
			}else{
				String msg="";
				if (lang.equals("es"))
					msg = "Nombre de componente inválido";
				else
					msg = "Invalid component name";
				throw new Exception (msg);
			}
		}else{
			String msg="";
			if (lang.equals("es"))
				msg = "Ingrese nombre del componente";
			else
				msg = "Missing component name";
			throw new Exception (msg);
		}
		
		return dt;
		
	}
	
	public void loadData(DTComponent dt){
		if (dt!=null){
			tname.setText(dt.getName());		
			
			tautor.setText(dt.getAutor());
			
			tdescription.setText(dt.getDescription());
			
			filePackage.setText(dt.getPack());
		}
	}
	
}
