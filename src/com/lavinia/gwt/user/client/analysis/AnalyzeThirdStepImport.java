package com.lavinia.gwt.user.client.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.ShortCutsPanel;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.interfaces.IComponentsAnalysis;
import com.lavinia.gwt.user.client.interfaces.IComponentsAnalysisAsync;
import com.lavinia.gwt.user.client.interfaces.IUtils;
import com.lavinia.gwt.user.client.interfaces.IUtilsAsync;
import com.lavinia.gwt.user.client.utils.StatusPopup;


public class AnalyzeThirdStepImport  extends AnalyzeThirdStep{
	
	private HorizontalPanel hp = new HorizontalPanel();
	private VerticalPanel result = new VerticalPanel();
	private VerticalPanel vpResult;
	
	private Pages pages;
	private Label resultadoDelAnalisisLabel;
	private FileUpload fupi;
	
	private VerticalPanel tablePanel = new VerticalPanel();	
	private boolean export = false;
	
	private String lang;
	
	private FormPanel form;
	
	private Vector hm = new Vector();
	
	private String folder;
	
	private String folder2;
	
	private boolean mimport=false;
	
	private StatusPopup statusPopup = StatusPopup.getInstance();
	
	public AnalyzeThirdStepImport(String l,final Vector typesNames2, FileUpload a,Vector params){
		super(params,typesNames2);
		this.lang = l;
		this.fupi=a;
		
		
		export=false;
		
		String msg1 = "";
		if (lang.equals("es"))
			msg1 = "Desea exportar los resultados a un archivo?";
		else
			msg1 = "Export results to a file?";
		if (Window.confirm(msg1)){
			export = true;
		}
				
		
		form = new FormPanel();
		
	   	form.setAction(GWT.getModuleBaseURL()+"uploadParams");	   	
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);	  
	    	    
	    form.setWidget(hp);
	    
	    		
	    form.addFormHandler(new FormHandler() {
	    	public void onSubmit(FormSubmitEvent event) {
	    		// TODO Auto-generated method stub
	    		
	    	}
	    	public void onSubmitComplete(FormSubmitCompleteEvent event) {
	    		if (!mimport){
		    		Iterator it = hm.iterator();
	        		while (it.hasNext()){
	        			Vector v = (Vector)it.next();
	        			FileUpload fup = (FileUpload)v.get(0);
	        			fup.setVisible(true);
	        			Panel p = (Panel)v.get(1);
	        			p.add(fup);
	        			
	        		}
	        		uploadResults();
	    		}else{
	    			IComponentsAnalysisAsync assanalysis = (IComponentsAnalysisAsync)GWT.create(IComponentsAnalysis.class);
	    			ServiceDefTarget endpointanalysis = (ServiceDefTarget)assanalysis;			
	    			endpointanalysis.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsAnalysis");
	    		
	    			Vector params = getParams();
					setCompsParams(new Vector());
					for(int i=0;i<getTypesNames().size();i++){
						getCompsParams().add(new DTComponentParams((String)getTypesNames().get(i),((ParamsSettings)params.get(i)).getValues()));					
					}
					
	    			assanalysis.analyzeImport(folder2,getCompsParams(), export, folder, new AsyncCallback() {
				        public void onFailure(Throwable caught) {	        		
				        	Window.alert(caught.getMessage());			
				        	statusPopup.hide();
				        	
				          }
			
				          public void onSuccess(Object result) {
				        	 
				        	statusPopup.hide();
				        	  
				        	showResultText(((String)((Vector)result).get(0)),((HashMap)((Vector)result).get(1)),((ArrayList)((Vector)result).get(2)));
				        	if (export){
				        	  export2(((String)((Vector)result).get(3)));
				        	  export=false;
				        	}
				        	 
				        	 	      			
				        	  
				          }	          
				    });
	    		}
	    		
	    	}
	    });
	    initWidget(form);
				
		vpResult = new VerticalPanel();			
		
		if (lang.equals("es"))
			resultadoDelAnalisisLabel = new Label("Resultado del Analisis: ");
		else			
			resultadoDelAnalisisLabel = new Label("Analysis Results: ");
		resultadoDelAnalisisLabel.setVisible(false);
		
		vpResult.setCellHorizontalAlignment(resultadoDelAnalisisLabel,HasHorizontalAlignment.ALIGN_CENTER);
		
		uploadParams();
	}
		

	
	public void showResultText(String text, HashMap res, ArrayList outs){
		
			if (lang.equals("es"))
				getStatusPopup().setText("Actualizando Vista...");
			else
				getStatusPopup().setText("Updating View...");

			getStatusPopup().show();
					
			setTableTypes(new TableMarkupOptions(lang,outs));
			
			 
      	  Hyperlink hlupdate;
      	  if (lang.equals("es"))
      		hlupdate = new Hyperlink("Actualizar Vista","actualizarVista");
      	  else
      		hlupdate = new Hyperlink("Update View","actualizarVista");
      	  
      	  ShortCutsPanel.getInstance().addHyperLink(hlupdate);
      	  
      	 
      	 ClickListener click = new ClickListener() {
      			 public void onClick(Widget sender) {
      				 if (pages!=null){	       				
		       			updateResultText();	       				
      				 }
      			 }	
      		};
      		
      		Hyperlink bupdate;
       		if (lang.equals("es")){
       			bupdate= new Hyperlink("Actualizar Vista","");
       			bupdate.addClickListener(click);
       		}else{
       			bupdate= new Hyperlink("Update View","");
       			bupdate.addClickListener(click);
       		}
			hlupdate.addClickListener(click);
		   	 
			  tablePanel.add(getTableTypes());
			  tablePanel.add(bupdate);
			  tablePanel.setCellHorizontalAlignment(bupdate,HasHorizontalAlignment.ALIGN_RIGHT);
			  tablePanel.setSpacing(4);	   
			  tablePanel.setStyleName("lv-Sink");							
			
			
			
			pages = new Pages(lang,text,res,getTableTypes());
			
			if (vpResult.getWidgetIndex(result)>=0)
				vpResult.remove(result);
			result = new VerticalPanel();		
			
			result.add(pages);					
						
			result.setSpacing(4);
			vpResult.add(result);
			
			resultadoDelAnalisisLabel.setVisible(true);
			hp.add(tablePanel);
			hp.add(vpResult);
			getStatusPopup().hide();		
		
	}
	
	
	public void updateResultText(){
		if (lang.equals("es"))
			getStatusPopup().setText("Actualizando Vista...");
		else
			getStatusPopup().setText("Updating View...");
		getStatusPopup().show();
		
		pages.updateView();
		
		if (vpResult.getWidgetIndex(result)>=0)
				vpResult.remove(result);
		result = new VerticalPanel();				       					
		result.add(pages);		
			
							
		result.setSpacing(4);
		vpResult.add(result);
		
		getStatusPopup().hide();
		
		
	}
	private void uploadParams(){
		
		
		if (lang.equals("es"))		
			getStatusPopup().setText("Analizando...");			
		else
			getStatusPopup().setText("Analyzing...");
		getStatusPopup().show();
		
		IUtilsAsync ass2 = (IUtilsAsync)GWT.create(IUtils.class);
		ServiceDefTarget endpoint2 = (ServiceDefTarget)ass2;	
			
		endpoint2.setServiceEntryPoint(GWT.getModuleBaseURL()+"IUtils");
		
			ass2.getNanoTime(new AsyncCallback() {
		        public void onFailure(Throwable caught) {	   
		        	if (lang.equals("es"))
		        		Window.alert("Error al obtener NanoTime: "+caught.getMessage());	        	
		        	else
		        		Window.alert("Error obtaining NanoTime: "+caught.getMessage());
		          }
	
		          public void onSuccess(Object result) {	
		        	 
		        	  	folder = (String)result;			        			
						
						Vector params = getParams();						
		   			 	for(int i=0;i<getTypesNames().size();i++){
		   			 		for (int j =0;j<((ParamsSettings)params.get(i)).getCant();j++){
		   			 			if ((((ParamsSettings)params.get(i)).getType(j).equals("File")) ||
		   			 				(((ParamsSettings)params.get(i)).getType(j).equals("Archivo"))){
		   			 			FileUpload fup = (FileUpload)((ParamsSettings)params.get(i)).getWidget(j);
		   			 			fup.setVisible(false);
		   			 			Vector v = new Vector();
		   			 			v.add(fup);
		   			 			v.add(fup.getParent());
		   			 			hm.add(v);
		   			 			hp.add(fup);
		   			 			}
		   			 		}
		   			 	}
		   			 	
						form.setAction(GWT.getModuleBaseURL()+"uploadParams?id="+folder);
						form.submit();							
		          }	          
		    });
		
	}
	
	private void uploadResults(){
		mimport=true;
		IUtilsAsync ass2 = (IUtilsAsync)GWT.create(IUtils.class);
		ServiceDefTarget endpoint2 = (ServiceDefTarget)ass2;	
			
		endpoint2.setServiceEntryPoint(GWT.getModuleBaseURL()+"IUtils");
		
		ass2.getNanoTime(new AsyncCallback() {
	        public void onFailure(Throwable caught) {	   
	        	if (lang.equals("es"))
	        		Window.alert("Error al obtener NanoTime: "+caught.getMessage());	        	
	        	else
	        		Window.alert("Error obtaining NanoTime: "+caught.getMessage());
	          }

	          public void onSuccess(Object result) {	
	        	  	
	        	  folder2 = (String)result;
	        	  
	        	  fupi.setVisible(false);
	        	  hp.add(fupi);
	        	  form.setAction(GWT.getModuleBaseURL()+"uploadImport?id="+folder2);
	        	  form.submit();
	        	 					
	          }	          
	    });
		
	}
	
	public void refeshPanel() {	
		super.refeshPanel();
		if (lang.equals("es"))
			Window.alert("Flujo cargado, regrese al paso anterior.");
		else
			Window.alert("Flow loaded, go back to previous step.");
	}
	
}
