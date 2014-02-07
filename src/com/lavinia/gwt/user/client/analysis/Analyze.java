/*
 * Created on 01/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.ShortCutsPanel;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponentData;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.interfaces.IComponentsAnalysis;
import com.lavinia.gwt.user.client.interfaces.IComponentsAnalysisAsync;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfoAsync;
import com.lavinia.gwt.user.client.interfaces.IUtils;
import com.lavinia.gwt.user.client.interfaces.IUtilsAsync;
import com.lavinia.gwt.user.client.utils.StatusPopup;


/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Analyze extends UseFlow{
	
	private String lang;
	private ListBox list;
	private ComponentsFlow fl;	
	private HorizontalPanel temp;
	private int step=-1;	
	private Button bant;
	private Button bsig;
	private VerticalPanel hvFirst;			//components data and flow	
	private VerticalPanel vpSecond;			//parameters settings
	

	private boolean load2=false;
	private boolean load3=false;
	
	private IComponentsInfoAsync assinfo;
	
	private ServiceDefTarget endpointinfo;
	
	private IComponentsAnalysisAsync assanalysis;
	
	private ServiceDefTarget endpointanalysis;	
	
	private Label tbdesc;
	
	private FlexTable lbinpp;
	
	private FlexTable lboutt;
	
	private Vector data;
	
	private Label title;	

	private StatusPopup statusPopup = StatusPopup.getInstance();
	
	private final FormPanel form;
	
	
	private VerticalPanel main;
	
	private VerticalPanel pfup = new VerticalPanel();
	
	private AnalyzeThirdStep thirdStep;
	
	
	private FileUpload fup;
	private FileUpload fupi;
	
	
	private String id;
	
	private AnalyzeSecondStep secondStep;
	
	private Vector compsParams;
	private boolean loaded=false;
	
	private boolean mimport = false;
	
	
	private HorizontalPanel hplabellink;
	
		
	private Hyperlink siguiente;
	private Hyperlink anterior;
	
	private boolean loading = false;
	
	private Hyperlink bsave;
	
	public Analyze(String l){
		this.lang=l;
		assinfo = (IComponentsInfoAsync)GWT.create(IComponentsInfo.class);
		endpointinfo = (ServiceDefTarget)assinfo;			
		endpointinfo.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsInfo");
		
		assanalysis = (IComponentsAnalysisAsync)GWT.create(IComponentsAnalysis.class);
		endpointanalysis = (ServiceDefTarget)assanalysis;			
		endpointanalysis.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsAnalysis");
		
		
	    main = new VerticalPanel();
        title = new Label("");
        title.setWidth("100%");
                
        main.add(title);
        main.setCellHorizontalAlignment(title,HasHorizontalAlignment.ALIGN_CENTER);
        main.setSpacing(10);
	    temp = new HorizontalPanel();        
	    
	    
	    hplabellink = new HorizontalPanel();
	    hplabellink.setStyleName("lv-Sink");
	    main.add(hplabellink);	    
	    main.setCellVerticalAlignment(hplabellink, HasVerticalAlignment.ALIGN_TOP);
	    
	    
		main.add(temp);
		temp.setHeight("300");
		main.setCellHorizontalAlignment(temp,HasHorizontalAlignment.ALIGN_CENTER);

		/********BUTTONS*************************************/
		if (lang.equals("es")){
			anterior = new Hyperlink("Anterior", "anterior");
			siguiente = new Hyperlink("Siguiente", "siguiente");
			bsig = new Button("Siguiente");
			bant = new Button("Anterior");
		
		}else{
			anterior = new Hyperlink("Previous", "anterior");
			siguiente = new Hyperlink("Next", "siguiente");
			bsig = new Button("Next");
			bant = new Button("Previous");
		}
			
		ShortCutsPanel.getInstance().addHyperLink(anterior);	
		ShortCutsPanel.getInstance().addHyperLink(siguiente);
		
		
						
		
		
		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(bant);
		buttons.add(bsig);		
		buttons.setSpacing(8);
		
		ClickListener clsig = new ClickListener() {
			 public void onClick(Widget sender) {
			 	if(step==0){
			 		if (fl.getCount()>0){				 		
				 		if (!load2){				 			
				 			checkFlow();
				 			setFlowChange(false);
				 		}else{
				 			if (!isFlowChange()){
				 				temp.clear();
				 				temp.add(vpSecond);
				 				step=1;
				 				bant.setEnabled(true);
				 				
				 			}else{
				 				checkFlow();				 				
				 			}				 			
				 			
				 		}
				 		if (lang.equals("es"))
				 			title.setText("Configuración de Parámetros");
				 		else
				 			title.setText("Parametters Settings");
			 		}else{
			 			if (lang.equals("es"))
			 				Window.alert("Flujo Vacío");
			 			else
			 				Window.alert("Empty Flow");
			 		}
			 	}else if (step==1){
			 		temp.clear();			 		
		 			if ((!load3)||(mimport)){
		 				ThirdStep();
		 				load3=true;
		 			}else{
		 				if (!isFlowChange()){
		 					thirdStep.setParams(secondStep.getParams());		 					
		 					temp.add(thirdStep);
		 				}else{
		 					ThirdStep();
		 					setFlowChange(false);
		 				}			 			
			 		}			 		
			 		step=2;
			 		bsig.setEnabled(false);	
			 		if (lang.equals("es"))
			 			title.setText("Análisis de Texto");
			 		else
			 			title.setText("Text Analysis");
			 	}
			 }	
		};
		
		bsig.addClickListener(clsig);
		siguiente.addClickListener(clsig);
		
		ClickListener clant = new ClickListener() {
			 public void onClick(Widget sender) {
			 	if (step==1){
			 		temp.clear();
			 		temp.add(hvFirst);
			 		step=0;
			 		bant.setEnabled(false);
			 		if (lang.equals("es"))
			 			title.setText("Selección de Componentes");
			 		else
			 			title.setText("Flow Selection");
			 		
			 	}if (step==2){
			 		temp.clear();
			 		temp.add(vpSecond);
			 		step=1;
			 		bsig.setEnabled(true);
			 		if (lang.equals("es"))
			 			title.setText("Configuración de Parámetros");
			 		else
			 			title.setText("Parametters Settings");
			 	}
			 }	
		};
		
		bant.addClickListener(clant);
		anterior.addClickListener(clant);
		
		
		FirstStep();		
 		step=0;
 		bant.setEnabled(false);
		
		/*****************************************************/
		main.add(buttons);
		buttons.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		main.setCellHorizontalAlignment(buttons,HasHorizontalAlignment.ALIGN_CENTER);
		
		
		main.setCellWidth(temp,"100%");
		main.setStyleName("lv-Sink");
		main.setWidth("100%");
		
		/********************************************************/
		form = new FormPanel();   	
	   	
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);	    
	    
	    form.setWidget(main);
	    
	    form.addFormHandler(new FormHandler() {
	        public void onSubmitComplete(FormSubmitCompleteEvent event) {        	
	        	
	        	if (loading){
	        		loading=false;
	        		pfup.setVisible(false);
		        	assanalysis.loadFlow(id, new AsyncCallback(){
		        		public void onFailure(Throwable caught) {
		        			Window.alert(caught.getMessage());
		        			
		        		}
		        		public void onSuccess(Object result) {
		        			Vector compsParams = (Vector) result;
		        			loadFlowFromData(compsParams);
		        			
		        		}
		        	});
	        	}
	        	if (main.getWidgetIndex(fup)>=0)
	        		main.remove(fup);
	        
	        	
	        }

	        public void onSubmit(FormSubmitEvent event) {
          
	        }
	      });
	    
		initWidget(form);
		
		
	}
	
	public void addLabelLink(){
		if (hplabellink.getWidgetCount()<=0){
			
			Label lblink;
			Hyperlink hlink = new Hyperlink();
			if (lang.equals("es")){
				lblink = new Label("Usted esta trabajando con resultados importados desde un archivo: ");			
				hlink.setText("cambiar a modo normal");
			}else{
				lblink = new Label("You are working with results imported of a file:  ");			
				hlink.setText("change to normal mode");
			}
				
			
			
			hlink.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					hplabellink.clear();
					mimport = false;
					main.remove(fupi);
					temp.clear();
			 		temp.add(hvFirst);
			 		step=0;
			 		bant.setEnabled(false);
			 		bsig.setEnabled(true);
			 		
			 		if (lang.equals("es"))
			 			title.setText("Selección de Componentes");
			 		else
			 			title.setText("Flow Selection");
			 		
			 		fl.clear();
				}
			});
			
			hplabellink.add(lblink);
			hplabellink.add(hlink);
			hplabellink.setSpacing(4);
				
			main.setCellVerticalAlignment(hplabellink,HasVerticalAlignment.ALIGN_BOTTOM);
		}
	}
	
	
	public void checkFlow(){
		
		Collection names = new ArrayList();
		for (int i=0;i<fl.getCount();i++){
			names.add(fl.getText(i));
		}
		if (!mimport){
			if (lang.equals("es"))
				statusPopup.setText("Chequeando Flujo...");
			else
				statusPopup.setText("Checking Flow...");
			statusPopup.show();
			assanalysis.isFlowOK(names,new AsyncCallback() {
		        public void onFailure(Throwable caught) {	        		        	
		        	Window.alert(caught.getMessage());
		        	statusPopup.hide();
		          }
	
		          public void onSuccess(Object result) {
		          	if (((Boolean)result).booleanValue()){
		          		temp.clear();
		          		SecondStep();
		          		load2=true;     
		          		step=1;
		 				bant.setEnabled(true);    		
		 				
		          	}else{
		          		if (lang.equals("es"))
		          			Window.alert("Flujo Incorrecto");
		          		else
		          			Window.alert("Incorrect Flow");
		          	}
		          	statusPopup.hide();
		          }
		    });	
		}else{
			temp.clear();
      		SecondStep();
      		load2=true;     
      		step=1;
				bant.setEnabled(true); 
		}
	}
	
	public void loadList(){		  
	    
	    assinfo.getComponents(new AsyncCallback() {
	        public void onFailure(Throwable caught) {	        		        	
	        	Window.alert(caught.getMessage());
	          }

	          public void onSuccess(Object result) {
	          	Collection col = (Collection)result;
	          	Iterator it = col.iterator();
	          	while (it.hasNext()){
	          		list.addItem((String)it.next());
	          	}
	          	int size = list.getItemCount();
	          	data = new Vector();
	          	data.setSize(size);
	          	if (size>0)
	          		loadData(list.getSelectedIndex());
	          }
	    });	    
	   
	}
	public void loadData(int index){
		
		String name = list.getItemText(index);
		
		
		assinfo.getComponentData(name,new AsyncCallback() {
		    public void onFailure(Throwable caught) {	        		        	
		    	Window.alert(caught.getMessage());
		      }
		      public void onSuccess(Object result) {
		      	DTComponentData dt = (DTComponentData)result;
		      	showData(dt);
		      	data.set(list.getSelectedIndex(),result);
		      }
		});
	    
	}
	
	public void showData(DTComponentData dt){
		tbdesc.setText(dt.getDescription());	          	
      	String[] inps = dt.getInputs();
      	String[] outs = dt.getOutputs();
      	
      	if (lbinpp.getRowCount()>0)
     		lbinpp.removeStyleName("lv-Sink");
      	if (lboutt.getRowCount()>0)
      		lboutt.removeStyleName("lv-Sink");
      	lbinpp.clear();      	
      	lboutt.clear();
      	      	
      	for(int i=0;i<inps.length;i++){
      		HTML t = new HTML(inps[i]);
      		t.setStyleName("gwt-TextBox");
      		lbinpp.setWidget(i,0,t);
      	}
      	if (inps.length>0){
      		lbinpp.setStyleName("lv-Sink");
      		lbinpp.setCellSpacing(0);
      	}
      	
      	for(int i=0;i<outs.length;i++){
      		HTML t = new HTML(outs[i]);      		
      		t.setStyleName("gwt-TextBox");
      		lboutt.setWidget(i,0,t);
      	}
      	if (outs.length>0){
      		lboutt.setStyleName("lv-Sink");
      		lboutt.setCellSpacing(0);
      	}
      

      
	}
	
	private void FirstStep(){
		
		if (lang.equals("es"))
			title.setText("Selección de Componentes");
		else
			title.setText("Flow Selection");
		
		/***** COMPONENTS DATA ************************/
		Label lbcomp;
		if (lang.equals("es"))
			lbcomp= new Label ("Componente");
		else
			lbcomp= new Label ("Component");
		lbcomp.setWidth("110");
		list = new ListBox();		
		list.setVisibleItemCount(1);
		list.setWidth("150");
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(lbcomp);
		hp1.add(list);
		
		
		Label ldesc;
		if (lang.equals("es"))
			ldesc = new Label ("Descripción");
		else
			ldesc = new Label ("Description");
		ldesc.setWidth("110");
		tbdesc = new Label();		
		tbdesc.setWidth("260");
		tbdesc.setStyleName("lv-Label");
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add(ldesc);
		vp1.add(tbdesc);
		
		
		Label linp=null;
		Label lout=null;
		if (lang.equals("es")){
			linp = new Label ("Entradas");
			lout = new Label ("Salidas");
		}else{
			linp = new Label ("Inputs");
			lout = new Label ("Outputs");
		}
		
		linp.setWidth("110");
		
		lbinpp = new FlexTable();
		
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(linp);
		hp2.add(lbinpp);
		lbinpp.setSize("150", "100%");
		
		 
		lout.setWidth("110");
		
		lboutt = new FlexTable();
		
		HorizontalPanel hp3 = new HorizontalPanel();
		hp3.add(lout);
		hp3.add(lboutt);
		lboutt.setSize("150", "100%");
		
		
		
		VerticalPanel vpcomp = new VerticalPanel();
		
		vpcomp.add(hp1);
		vpcomp.add(vp1);
		vpcomp.add(hp2);
		vpcomp.add(hp3);
		vpcomp.setSpacing(10);
		vpcomp.setStyleName("lv-sink");
		/******************************************************/
		
		loadList();
		
		list.addChangeListener(new ChangeListener() {
			public void onChange(Widget arg0) {
				int index = list.getSelectedIndex();
				if (data.get(index)!=null){
					showData((DTComponentData)data.get(index));
				}else{
					loadData(index);
				}
			}
		});
		
		
		/***** BUTTONS << AND >> *********************/
		Button badd = new Button(" >> ");
		badd.setWidth("50");
		Button brem = new Button(" << ");
		brem.setWidth("50");
		VerticalPanel vpbuttons = new VerticalPanel();
		vpbuttons.add(badd);
		vpbuttons.add(brem);
		vpbuttons.setSpacing(6);
		vpbuttons.setWidth("100");
		vpbuttons.setCellHorizontalAlignment(badd,HasHorizontalAlignment.ALIGN_CENTER);
		vpbuttons.setCellHorizontalAlignment(brem,HasHorizontalAlignment.ALIGN_CENTER);
		
		/***********ACCIONS << AND >> **************************/
		
		badd.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {
			 	final String name = list.getItemText(list.getSelectedIndex());
			 	assinfo.isSimpleComponent(name, new AsyncCallback(){
			 		public void onFailure(Throwable caught) {			 		
			 			Window.alert(caught.getMessage());
			 		}
			 		
			 		public void onSuccess(Object result) {			 		
			 			if (((Boolean)result).booleanValue()){
			 				fl.addRow(name);
			 				setFlowChange(true);
			 				loaded=false;
			 			}else{
			 				assinfo.getAggregateData(name, new AsyncCallback(){
			 					public void onFailure(Throwable caught) {
			 						Window.alert(caught.getMessage());
			 						
			 					}
			 					public void onSuccess(Object result) {
			 						DTAggregateComponent dta = (DTAggregateComponent)result;
			 						loadFlowFromData(dta.getCompsParams());
			 						
			 					}
			 				});
			 			}
			 		}
			 	});		 	
			 }	
		});
		brem.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {
			 	fl.removeRow();
			 	setFlowChange(true);
			 	loaded=false;
			 }	
		});
		/*********************************************************/
		
		/****** COMPONENTS FLOW **********************/
		fl = new ComponentsFlow(lang,true);		
		fl.setStyleName("lv-sink");		
		
		/*********************************************/
		String msg1="";
		if (lang.equals("es"))
			msg1 = "Cargar Flujo (XML)";
		else
			msg1 = "Load Flow (XML)";
		Hyperlink bload = new Hyperlink(msg1, "cargarFlujo");
		ShortCutsPanel.getInstance().addHyperLink(bload);
;		bload.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {					 
				 DialogBox db = new DialogBox(true);
				 db.setPopupPosition(sender.getAbsoluteLeft()-200, sender.getAbsoluteTop()-10);
				 db.setStyleName("lv-Popup");
				 loadFlow(db);
				 
			 }	
		});
		
		/***********************************************/
		if (lang.equals("es"))
			msg1 = "Importar Resultados";
		else
			msg1 = "Import Results";
		Hyperlink bimport = new Hyperlink(msg1, "importRes");
		ShortCutsPanel.getInstance().addHyperLink(bimport);
		bimport.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				 DialogBox db = new DialogBox(true);
				 db.setPopupPosition(sender.getAbsoluteLeft()-200, sender.getAbsoluteTop()-10);
				 db.setStyleName("lv-Popup");				 
				 importRes(db);				
			}
		});
		
		
		
		/***********************************************/
		HorizontalPanel hpFirst = new HorizontalPanel();
		hpFirst.add(vpcomp);
		hpFirst.add(vpbuttons);
		hpFirst.add(fl);
		
		hvFirst = new VerticalPanel();
				
				
		hvFirst.add(pfup);	
		hvFirst.setCellHorizontalAlignment(pfup, HasHorizontalAlignment.ALIGN_RIGHT);
		//hvFirst.setSpacing(5);
		hvFirst.add(hpFirst);		
		temp.add(hvFirst);
		//temp.setSpacing(6);
		
		
		
		 
	}
	
	private void SecondStep(){
		
		step=1;
		
		Vector names = new Vector();
		
		for (int i=0;i<fl.getCount();i++){
			names.add(fl.getText(i));
		}
		
		
		/*************************************************************/
		if (lang.equals("es"))
			bsave = new Hyperlink("Guardar Flujo (XML)", "guardarFlujo");
		else
			bsave = new Hyperlink("Save Flow (XML)", "guardarFlujo");
		ShortCutsPanel.getInstance().addHyperLink(bsave);
		
		bsave.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				String msg1="";
				if (lang.equals("es"))
					msg1 = "Desea guardar el flujo y sus parámetros?";
				else
					msg1 = "Save flow and parametters settings?";
				if (Window.confirm(msg1)){
					saveFlow();
				}				
			}
		});
		
		/*************************************************************/
		
		vpSecond = new VerticalPanel();
		
		if (loaded)
			secondStep = new AnalyzeSecondStep(lang,names,compsParams);
		else
			secondStep = new AnalyzeSecondStep(lang,names);
		
		secondStep.setWidth("100%");		
		vpSecond.add(secondStep);
		vpSecond.setWidth("100%");
		
		
		temp.add(vpSecond);
		temp.setWidth("100%");
		temp.setCellHorizontalAlignment(vpSecond,HasHorizontalAlignment.ALIGN_LEFT);
		temp.setSpacing(10);
		
	}
	
	private void ThirdStep(){
		step=2;
		temp.setHeight("100%");
		main.setCellHeight(temp,"100%");
		main.setHeight("100%");
		
		Vector names = new Vector();
		
		for (int i=0;i<fl.getCount();i++){
			names.add(fl.getText(i));
		}	
		if (!mimport)
			thirdStep = new AnalyzeThirdStepSimple(lang,names,secondStep.getParams());
		else
			thirdStep = new AnalyzeThirdStepImport(lang,names,fupi,secondStep.getParams());
		
		thirdStep.setWidth("100%");
		
		temp.add(thirdStep);		
		temp.setWidth("100%");
		
		
	}
	
	private void saveFlow(){
		Vector compsParams = new Vector();
		for(int i=0;i<fl.getCount();i++){
			compsParams.add(new DTComponentParams(fl.getText(i),((ParamsSettings)(secondStep.getParams()).get(i)).getValues()));					
		}
		assanalysis.saveFlow(compsParams, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}
			public void onSuccess(Object result) {
				String id = (String) result;
				Window.open(GWT.getModuleBaseURL()+"downloadAE?id="+id, "_self", "");
				
			}
		});
		
	}
	
	private void loadFlow(final DialogBox db){
		
	if (main.getWidgetIndex(fup)>=0)
		main.remove(fup);
	
	fup = new FileUpload();	
	fup.setName("fileUpload");
	fup.setWidth("300");	
	Button baccept;
	if (lang.equals("es"))
		baccept = new Button("Aceptar");
	else
		baccept = new Button("OK");
	VerticalPanel vdb = new VerticalPanel();
	vdb.add(fup);
	vdb.add(baccept);
	db.setWidget(vdb);
	db.show();
	baccept.addClickListener(new ClickListener(){
		public void onClick(Widget sender) {
			if (fup.getFilename()!=""){
				IUtilsAsync assutil = (IUtilsAsync)GWT.create(IUtils.class);
				ServiceDefTarget endpointutils = (ServiceDefTarget)assutil;			
				endpointutils.setServiceEntryPoint(GWT.getModuleBaseURL()+"IUtils");
				
				assutil.getNanoTime(new AsyncCallback(){
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
						
					}
					public void onSuccess(Object result) {
						id = (String)result;						
						db.hide();						
						fup.setVisible(false);
						main.add(fup);						
						form.setAction(GWT.getModuleBaseURL()+"uploadAE?id="+id);
						loading=true;
						form.submit();
					}
				});
			}else
				db.hide();
			
		}
	});
	
	
		
	}
	
	public FileUpload getFileUploadImport(){
		return fupi;
	}
	
	private void importRes(final DialogBox db){
		
		if (main.getWidgetIndex(fupi)>=0)
			main.remove(fupi);
		
		fupi = new FileUpload();	
		fupi.setName("importFile");
		fupi.setWidth("300");	
		Button baccept;
		if (lang.equals("es"))
			baccept = new Button("Aceptar");
		else
			baccept = new Button("OK");
		VerticalPanel vdb = new VerticalPanel();
		vdb.add(fupi);
		vdb.add(baccept);
		db.setWidget(vdb);
		db.show();		
		
		
		baccept.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if (fupi.getFilename()!=""){				
												
							db.hide();	
							mimport=true;
							
							addLabelLink();
							
							if (step==2){
								temp.clear();
				 				temp.add(vpSecond);
				 				step=1;
				 				bsig.setEnabled(true);
							}
												
				}else
					db.hide();				
			}
		});
		
	}

	public void loadFlowFromData(Vector compsParams){
		loaded=true;
		fl.clear();
		Iterator it = compsParams.iterator();
		Vector names = new Vector();
		while (it.hasNext()){
			DTComponentParams dt = (DTComponentParams)it.next();
			fl.addRow(dt.getName());
			names.add(dt.getName());
			
		}
		this.compsParams=compsParams;
		setFlowChange(true);
		if (secondStep!=null){
			secondStep.setNames(names);
			secondStep.setCompsParams(compsParams);
			secondStep.refreshPanel();			
		
			if ((thirdStep!=null)&&(!mimport)){
				thirdStep.setTypesNames(names);
				thirdStep.setParams(secondStep.getParams());
				
				if (step==2)
					thirdStep.refeshPanel();
				
				
				
				
			}else if (mimport){
				thirdStep = new AnalyzeThirdStepImport(lang,names,fupi,secondStep.getParams());			
				thirdStep.setWidth("100%");
				temp.clear();
				temp.add(thirdStep);		
				temp.setWidth("100%");
			}
		}
		
	}
	
	public void setFlowChange(boolean f) {	
		super.setFlowChange(f);
		if ((f)&&(bsave!=null))
			ShortCutsPanel.getInstance().removeHyperLink(bsave.getTargetHistoryToken());
	}
	

}
