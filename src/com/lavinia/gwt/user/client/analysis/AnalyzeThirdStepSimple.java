package com.lavinia.gwt.user.client.analysis;

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
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.ShortCutsPanel;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfoAsync;
import com.lavinia.gwt.user.client.interfaces.IUtils;
import com.lavinia.gwt.user.client.interfaces.IUtilsAsync;

public class AnalyzeThirdStepSimple extends AnalyzeThirdStep{	
	
	
	
	private HorizontalPanel PanelTypes;
	private VerticalPanel tablePanel = new VerticalPanel();	
	private FileUpload fileUpload;
	private ListBox listBox=new ListBox();
	private VerticalPanel vpResult;
	private VerticalPanel result = new VerticalPanel();	
	
	private FormPanel form;
	private HorizontalPanel panelH = new HorizontalPanel();
	private VerticalPanel panelV = new VerticalPanel();
	private boolean visible = false;
	private Integer index;
	private Button verButton;
	private HorizontalPanel temp = new HorizontalPanel();
	
	private Vector resultados;
	private Vector analyzedtext;
	private Vector loadAnalyzedText;
	private Vector updatedView;
	
	private Label nombreArchivo;
	private HorizontalPanel resLabels;
	private String folder;
	private String folder2;
	private Button accept;
	private VerticalPanel vpUploadAccept;
	
	private Label resultadoDelAnalisisLabel;
	
	
	
	private HashMap fileIndex = new HashMap();
	
	private boolean modalidad_Texto=true;
	
	
	
	private Pages pages;
	
	private StackPanel stackPanel;
		
	
	
	private String text;
	
	private TextBox timport;
	private TextArea textArea;
	
	
	
	private Hyperlink hlanalizar;
	private Hyperlink hlupdate;
	private Hyperlink hlexport;
	
	private String lang;
	
	
	private Vector hm = new Vector();
	
	private boolean exporting=false;
	
	private DialogBox db;
	
	public AnalyzeThirdStepSimple(String l, final Vector typesNames2, Vector params){
		super(params,typesNames2);		
		lang = l;	
	
					
		/****************Modalidad *************************************/
		final HorizontalPanel modalidadPanel = new HorizontalPanel();
		panelV.add(modalidadPanel);
		panelV.setCellVerticalAlignment(modalidadPanel, HasVerticalAlignment.ALIGN_TOP);
		modalidadPanel.setSpacing(8);

		final Label modalidadLabel;
		if (lang.equals("es"))
			modalidadLabel = new Label("Modalidad:");
		else
			modalidadLabel = new Label("Mode:");
		modalidadPanel.add(modalidadLabel);

		final RadioButton textoRadioButton = new RadioButton("new-group");
		modalidadPanel.add(textoRadioButton);
		if (lang.equals("es"))
			textoRadioButton.setText("Texto");
		else
			textoRadioButton.setText("Text");
		textoRadioButton.setChecked(true);
				
		final RadioButton corpusRadioButton = new RadioButton("new-group");
		modalidadPanel.add(corpusRadioButton);
		if (lang.equals("es"))
			corpusRadioButton.setText("Corpus de Texto");
		else
			corpusRadioButton.setText("Text Corpus");
		
		
		textoRadioButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if (!modalidad_Texto){
					texto();
					modalidad_Texto=true;
				}
				
			}
		});
		
		corpusRadioButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if (modalidad_Texto){
					corpus();						
					modalidad_Texto=false;
					if (result.getWidgetCount()<=0)
						ShortCutsPanel.getInstance().removeHyperLink("export");
				}
				
			}
		});
		/****************Corpus ****************************************/		
		
		form = new FormPanel();
		
	   	form.setAction(GWT.getModuleBaseURL()+"uploadCorpus");	   	
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
	    form.setMethod(FormPanel.METHOD_POST);	  
	    	    
	    form.setWidget(panelV);
	    panelV.setWidth("350");
	    		
	    form.addFormHandler(new FormHandler() {
	        public void onSubmitComplete(FormSubmitCompleteEvent event){
	        	if ((!modalidad_Texto)&&(form.getAction().indexOf("uploadParams")<0)){
		        	getStatusPopup().hide();
		        	if (lang.equals("es"))
		        		getStatusPopup().setText("Analizando...");			
		        	else
		        		getStatusPopup().setText("Analyzing...");
		        	getStatusPopup().show();   			 
		        	
					
					resultados = new Vector();
					analyzedtext = new Vector();
					Vector corpus = new Vector();
					loadAnalyzedText=new Vector();
					updatedView = new Vector();
					for (int i=0;i<listBox.getItemCount();i++){					
						corpus.add(listBox.getItemText(i));
						loadAnalyzedText.add(new Boolean(false));
						updatedView.add(new Boolean(true));
						analyzedtext.add(null);
					}
					Vector params = getParams();
					setCompsParams(new Vector());
					for(int i=0;i<getTypesNames().size();i++){
						getCompsParams().add(new DTComponentParams((String)getTypesNames().get(i),((ParamsSettings)params.get(i)).getValues()));					
					}
					
					getAas().analyzeFiles(getCompsParams(), corpus, folder, folder2, new AsyncCallback() {
				        public void onFailure(Throwable caught) {	        		
				        	Window.alert(caught.getMessage());			
				        	getStatusPopup().hide();
				        	
				          }
			
				          public void onSuccess(Object result) {
				        	 
				        	  getStatusPopup().hide();
				        	  resultados=(Vector)result;
				        	  fileIndex = new HashMap();
				        	  for (int i = 0;i<listBox.getItemCount();i++){
				        		  fileIndex.put(listBox.getItemText(i), new Integer(i));
				        	  }
				          		if (resultados.size()>0){
				          			showResult(0);
				          			verButton.setVisible(true);
				          		}
				          }	          
				    });
	        	}else if ((!modalidad_Texto)&&(form.getAction().indexOf("uploadParams")>=0)){
	        		Iterator it = hm.iterator();
	        		while (it.hasNext()){
	        			Vector v = (Vector)it.next();
	        			FileUpload fup = (FileUpload)v.get(0);
	        			fup.setVisible(true);
	        			Panel p = (Panel)v.get(1);
	        			p.add(fup);
	        			
	        		}
	        		if (exporting)
	        			exportar(text);
	        		else
	        			analyzeAll();
	        	}else if (modalidad_Texto){
		        		Iterator it = hm.iterator();
		        		while (it.hasNext()){
		        			Vector v = (Vector)it.next();
		        			FileUpload fup = (FileUpload)v.get(0);
		        			fup.setVisible(true);
		        			Panel p = (Panel)v.get(1);
		        			p.add(fup);
		        			
		        		}
		        			if (textArea.getText().length()>0){
		        				
		        				if (!exporting){
			        				if (lang.equals("es"))
			        					getStatusPopup().setText("Analizando...");			
			        				else
			        					getStatusPopup().setText("Analizing...");
			        				
			        				getStatusPopup().show();
			   			 	
			        				setCompsParams(new Vector());
			        				Vector params = getParams();
			        				for(int i=0;i<getTypesNames().size();i++){
			        					getCompsParams().add(new DTComponentParams((String)getTypesNames().get(i),((ParamsSettings)params.get(i)).getValues()));					
			        				}
							
			        				getAas().analyze(getCompsParams(),textArea.getText(),folder2, new AsyncCallback() {
			        					public void onFailure(Throwable caught) {	        		
			        						Window.alert(caught.getMessage());	   
			        						getStatusPopup().hide();
			        					}
			
			        					public void onSuccess(Object result) {			
			        						showResultText(textArea.getText(),(HashMap)result);
			        						getStatusPopup().hide();
										
			        					}	          
			        				});
		        				}else{
		        					text = textArea.getText();
		        					setCompsParams(new Vector());
			        				Vector params = getParams();
			        				for(int i=0;i<getTypesNames().size();i++){
			        					getCompsParams().add(new DTComponentParams((String)getTypesNames().get(i),((ParamsSettings)params.get(i)).getValues()));					
			        				}
			        				exportar(text);
		        				}
		        			}else{
		        				if (lang.equals("es"))
		        					Window.alert("Ingrese el texto a analizar");
		        				else
		        					Window.alert("There is no text to analyze");
		        				getStatusPopup().hide();
		        				exporting=false;
		        			}
	        		}
					
	        	
				
	        }

	        public void onSubmit(FormSubmitEvent event){
	        	if (!modalidad_Texto){
		        	if (listBox.getItemCount()==0){
		        		event.setCancelled(true);
		        		if (lang.equals("es"))
		        			Window.alert("No hay documentos en el corpus");
		        		else
		        			Window.alert("Empty Corpus!");
		        		
		        		Iterator it = hm.iterator();
		        		while (it.hasNext()){
		        			Vector v = (Vector)it.next();
		        			FileUpload fup = (FileUpload)v.get(0);
		        			fup.setVisible(true);
		        			Panel p = (Panel)v.get(1);
		        			p.add(fup);
		        			
		        		}
		        		getStatusPopup().hide();
		        	}else{
		        		if (lang.equals("es"))
		        			getStatusPopup().setText("Cargando Archivos...");			
		        		else
		        			getStatusPopup().setText("Loading files...");
	   			 		getStatusPopup().show();
		        	}
	        	}//else
	        		//Window.alert("1");
	        }});				
		
		loadTableColors(getTypesNames());
		
				
		/*************************************************************/
		panelH.setWidth("100%");
		
		panelV.add(panelH);
		panelV.setCellVerticalAlignment(panelH, HasVerticalAlignment.ALIGN_TOP);
		panelV.setCellHorizontalAlignment(panelH,HasHorizontalAlignment.ALIGN_LEFT);
		vpResult = new VerticalPanel();	
		stackPanel=new StackPanel();
		texto();
		
		if (lang.equals("es"))
			resultadoDelAnalisisLabel = new Label("Resultado del Analisis: ");
		else
			resultadoDelAnalisisLabel = new Label("Analysis Results: ");
		resultadoDelAnalisisLabel.setVisible(false);
		
		resLabels = new HorizontalPanel();
		resLabels.add(resultadoDelAnalisisLabel);
		resLabels.setSpacing(4);
		vpResult.add(resLabels);
		vpResult.setCellHorizontalAlignment(resLabels,HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(form);
	}
	
	
	
	public void texto(){
		
		panelH.clear();
		
		VerticalPanel vpStack = new VerticalPanel();
				
		stackPanel.clear();
		if (lang.equals("es"))
			stackPanel.add(vpStack, "Anotaciones");
		else
			stackPanel.add(vpStack, "Annotations");
		
		vpStack.setWidth("266");
				
		vpStack.add(tablePanel);
		
		panelH.add(stackPanel);
			
		final VerticalPanel verticalPanel = new VerticalPanel();
		panelH.add(verticalPanel);
		verticalPanel.setSpacing(4);
		
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			
		/****************************************************************/
		Hyperlink hlimportURL;
		if (lang.equals("es"))
			hlimportURL= new Hyperlink("Obtener texto desde URL","importURL");
		else
			hlimportURL = new Hyperlink("Extract text from URL","importURL");
		

		hlimportURL.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				 db = new DialogBox(true);
				 db.setPopupPosition(sender.getAbsoluteLeft()-200, sender.getAbsoluteTop()-10);
				 db.setStyleName("lv-Popup");	
				 
				 timport = new TextBox();
				 timport.setWidth("340");
				 timport.setText("http://");				 
				 Label limport = new Label("URL");
				 Button bimport = new Button();
				 if (lang.equals("es"))
					 bimport.setText("Aceptar");
				 else
					 bimport.setText("OK");
				
				 HorizontalPanel hpi = new HorizontalPanel();
				 hpi.add(limport);
				 hpi.add(timport);
				 hpi.setSpacing(4);
				
				 VerticalPanel vpi = new VerticalPanel();
				
				 vpi.add(hpi);
				 vpi.add(bimport);
				 vpi.setCellHorizontalAlignment(bimport, HasHorizontalAlignment.ALIGN_RIGHT);
				 vpi.setSpacing(4);
				
				
				 bimport.addClickListener(new ClickListener(){
					 public void onClick(Widget sender) {
						 db.hide();
						 importTextFromURL();
					 }
				 });
				 
				 db.setWidget(vpi);
				 db.show();
			}
		});
		
		Hyperlink limpiar;
	
		if (lang.equals("es"))
			limpiar = new Hyperlink("Limpiar","");
		else
			limpiar = new Hyperlink("Clear","");
		
		limpiar.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
			textArea.setText("");				
			}
		});

		HorizontalPanel links = new HorizontalPanel();
		links.setSpacing(2);
		links.add(hlimportURL);
		links.add(limpiar);
		links.setWidth("350");
		links.setCellHorizontalAlignment(limpiar, HasHorizontalAlignment.ALIGN_RIGHT);
		links.setCellHorizontalAlignment(hlimportURL, HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(links);		
		
		
		/*********** Text Area ******************************************/	
		textArea = new TextArea();
		textArea.setWidth("350");
		verticalPanel.add(textArea);		
		textArea.setVisibleLines(5);
		
		
		final Button analizarButton;
		if (lang.equals("es")){
			analizarButton = new Button("Analizar");
			hlanalizar = new Hyperlink("Analizar","analizar");
		}else{
			analizarButton = new Button("Analyze");
			hlanalizar = new Hyperlink("Analyze","analizar");
		}
		verticalPanel.add(analizarButton);
		
		
		ShortCutsPanel.getInstance().addHyperLink(hlanalizar);
		
		
		ClickListener click = new ClickListener(){
			public void onClick(Widget sender) {
				/********************************************************/
				uploadParams();  			 	
			
			}
		};
		
		
		if (lang.equals("es")){
			
			hlexport = new Hyperlink("Exportar a XML","export");
		}else{
			
			hlexport = new Hyperlink("Export to XML","export");
		}
		
		ClickListener clickExp =new ClickListener(){
			public void onClick(Widget sender) {
				if (lang.equals("es"))
					getStatusPopup().setText("Exportando...");
				else
					getStatusPopup().setText("Exporting...");
				getStatusPopup().show();
				exporting=true;
				uploadParams();
				
			}
		};
		hlexport.addClickListener(clickExp);
		
		ShortCutsPanel.getInstance().addHyperLink(hlexport);
		
		analizarButton.addClickListener(click);
		hlanalizar.addClickListener(click);
		verticalPanel.add(vpResult);
		
		
	}
	
	private void importTextFromURL(){
		IUtilsAsync ass2 = (IUtilsAsync)GWT.create(IUtils.class);
		ServiceDefTarget endpoint2 = (ServiceDefTarget)ass2;	
			
		endpoint2.setServiceEntryPoint(GWT.getModuleBaseURL()+"IUtils");
		
			ass2.importTextFromURL(timport.getText(), new AsyncCallback() {
		        public void onFailure(Throwable caught) {	        		
		        	Window.alert(caught.getMessage());	        	
		        }
	
		        public void onSuccess(Object result) {	
		        	textArea.setText((String)result);
				}	          
		    });
	}
	private void uploadParams(){
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
		   			 			panelV.add(fup);
		   			 			}
		   			 		}
		   			 	}
		   			 	
						form.setAction(GWT.getModuleBaseURL()+"uploadParams?id="+folder2);
						form.submit();							
		          }	          
		    });
		
	}
	public void corpus(){
		panelH.clear();
		
		stackPanel.clear();
		
		final VerticalPanel vp1Stack = new VerticalPanel();
		stackPanel.add(vp1Stack, "Corpus");		
		vp1Stack.setSize("286", "150");
		vp1Stack.setSpacing(4);

		final VerticalPanel vp2Stack = new VerticalPanel();	
		vp2Stack.setWidth("266");				
		vp2Stack.add(tablePanel);
		if (lang.equals("es"))
			stackPanel.add(vp2Stack, "Anotaciones");
		else
			stackPanel.add(vp2Stack, "Annotations");
		
		final HorizontalPanel hpfiles = new HorizontalPanel();
		vp1Stack.add(hpfiles);


		hpfiles.add(listBox);		
		listBox.setWidth("180");		
		listBox.setVisibleItemCount(5);
	
		final VerticalPanel vpButtons = new VerticalPanel();
		hpfiles.add(vpButtons);
		vpButtons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vpButtons.setSpacing(4);

		if (lang.equals("es"))
			hlanalizar = new Hyperlink("Analizar Todo","analizar");
		else
			hlanalizar = new Hyperlink("Analyze All","analizar");
		ShortCutsPanel.getInstance().addHyperLink(hlanalizar);
		
		
		final Button analizarTodoButton = new Button();
		if (lang.equals("es"))
			analizarTodoButton.setText("Analizar");
		else
			analizarTodoButton.setText("Analyze");
		vpButtons.add(analizarTodoButton);
		analizarTodoButton.setWidth("57");
		ClickListener click = new ClickListener() {
			public void onClick(Widget sender) {
				//analyzeAll();
				uploadParams();
			}
		};
		analizarTodoButton.addClickListener(click);
		hlanalizar.addClickListener(click);
		
		verButton = new Button();
		vpButtons.add(verButton);
		verButton.setWidth("57");
		if (lang.equals("es"))
			verButton.setText("Ver >>");
		else
			verButton.setText("Show >>");
		verButton.addClickListener(new ClickListener() {			
			public void onClick(Widget sender) {
				if (fileIndex.containsKey(listBox.getItemText(listBox.getSelectedIndex()))){
					int ind = ((Integer)fileIndex.get(listBox.getItemText(listBox.getSelectedIndex()))).intValue();
					showResult(ind);
				}
			}		
		});
		verButton.setVisible(false);
		
		final HorizontalPanel hpButtons = new HorizontalPanel();
		vp1Stack.add(hpButtons);
		vp1Stack.add(temp);
		
		hpButtons.setSpacing(4);
		Hyperlink agregarButton;
		if (lang.equals("es")){
			agregarButton = new Hyperlink("Agregar","addValue");
			
		}else
			agregarButton = new Hyperlink("Add","addValue");
		hpButtons.add(agregarButton);
		agregarButton.setWidth("57");
		
		
		
		index=new Integer(0);		
		
		agregarButton.addClickListener(new ClickListener() {
		
			public void onClick(Widget sender) {
				if (visible){
				
				}else{
					fileUpload = new FileUpload();
					fileUpload.setName(""+index);
					index = new Integer(index.intValue()+1);
					vpUploadAccept = new VerticalPanel();
					if (lang.equals("es"))
						accept = new Button("Aceptar");					
					else
						accept = new Button("OK");
					accept.addClickListener(new ClickListener(){
						public void onClick(Widget sender) {							
							vpUploadAccept.setVisible(false);
							visible=false;
							if ((fileUpload.getFilename()==null)||(fileUpload.getFilename().equals(""))){
								temp.remove(vpUploadAccept);
							}
							else{
								int ind1 = fileUpload.getFilename().lastIndexOf("/");
								String fileName=fileUpload.getFilename();
								if (ind1>=0)
									fileName=fileName.substring(ind1+1, fileName.length());
								else{
									int ind2 = fileUpload.getFilename().lastIndexOf("\\");
									if (ind2>=0)
										fileName=fileName.substring(ind2+1, fileName.length());
								}						
									
								listBox.addItem(fileName);
								if (verButton.isVisible())
									verButton.setVisible(false);						
							}							
						}
					});
					vpUploadAccept.add(fileUpload);
					vpUploadAccept.add(accept);
					vpUploadAccept.setSpacing(4);
					vpUploadAccept.setStyleName("lv-Upload");
					vpUploadAccept.setCellHorizontalAlignment(accept,HasHorizontalAlignment.ALIGN_RIGHT);
					temp.add(vpUploadAccept);
					visible=true;					
				}
			}		
		});		
		
		
		Hyperlink quitarButton;
		if (lang.equals("es"))
			quitarButton = new Hyperlink("Quitar","removeValue");
		else
			quitarButton = new Hyperlink("Remove","removeValue");
		hpButtons.add(quitarButton);
		quitarButton.setWidth("57");
		
		quitarButton.addClickListener(new ClickListener() {
			
			public void onClick(Widget sender) {
				if (listBox.getItemCount()>0){
					if (fileIndex.containsKey(listBox.getItemText(listBox.getSelectedIndex()))){
						int ind = ((Integer)fileIndex.get(listBox.getItemText(listBox.getSelectedIndex()))).intValue();
						resultados.set(ind, null);
					}
					
					int sel = listBox.getSelectedIndex();
					if (sel>=0){
						temp.remove(sel); /**************************************/
						listBox.removeItem(sel);		
						
					}
					if (listBox.getItemCount()==0){
						if (verButton.isVisible())
							verButton.setVisible(false);
					}
				}
			}
		
		});
		

		
		/*********** Annotations Colors ****************************/
		
		
		
		panelH.add(stackPanel);
		panelH.add(vpResult);
	}
	
	public void loadTableColors(Collection names){
		if (lang.equals("es"))
			getStatusPopup().setText("Cargando Tipos...");
		else
			getStatusPopup().setText("Loading Types...");
		
		IComponentsInfoAsync ass2 = (IComponentsInfoAsync)GWT.create(IComponentsInfo.class);
		ServiceDefTarget endpoint2 = (ServiceDefTarget)ass2;	
			
		endpoint2.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsInfo");
		
		ass2.getOutputs(names, new AsyncCallback() {
	        public void onFailure(Throwable caught) {	  
	        	if (lang.equals("es"))
	        		Window.alert("Error al obtener tipos de salida: "+caught.getMessage());
	        	else
	        		Window.alert("Error obtaining output types: "+caught.getMessage());
	        	getStatusPopup().hide();
	          }

	          public void onSuccess(Object res) {
	        	  setTableTypes(new TableMarkupOptions(lang,(ArrayList)res));
	        	 
	        	  if (lang.equals("es"))
	        		  hlupdate = new Hyperlink("Actualizar Vista","actualizarVista");
	        	  else
	        		  hlupdate = new Hyperlink("Update View","actualizarVista");
	        	  ShortCutsPanel.getInstance().addHyperLink(hlupdate);
	        	  
	        	   ClickListener click = new ClickListener() {
		       			 public void onClick(Widget sender) {
		       				 if (pages!=null){
			       				 if ((modalidad_Texto)||(resultados==null)||(listBox.getItemCount()==0)){
				       					updateResultText();
			       					
			       				 }else{
				       				 for (int i=0;i<listBox.getItemCount();i++){
				       					 updatedView.set(i,new Boolean(false));
				       				 }
				       				 if ((listBox.getSelectedIndex()<0)&&(listBox.getItemCount()>0))
				       					 showResult(0);
				       				 else if (listBox.getItemCount()>0)
				       					 showResult(listBox.getSelectedIndex()); 			 	
			       				 }
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
	        	  PanelTypes = new HorizontalPanel();
	        	  PanelTypes.add(getTableTypes());
	        	  tablePanel.clear();
	        	  tablePanel.add(PanelTypes);
	        	  tablePanel.add(bupdate);
	        	  tablePanel.setCellHorizontalAlignment(bupdate,HasHorizontalAlignment.ALIGN_RIGHT);
	        	  tablePanel.setSpacing(4);	        	  
	        		  
	        	  getStatusPopup().hide();
	        	  
	          }
		});
	}
	
	public void analyzeAll(){
		
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
		        		form.setAction(GWT.getModuleBaseURL()+"uploadCorpus?id="+folder);	
						form.submit();
					
						
		          }	          
		    });
		
		  	
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
			
		Button bexport;
		if (lang.equals("es")){
			hlexport = new Hyperlink("Exportar a XML", "export");
			bexport= new Button("Exportar...");
		}else{
			hlexport = new Hyperlink("Export to XML", "export");
			bexport= new Button("Export...");
		}
		ShortCutsPanel.getInstance().addHyperLink(hlexport);
		
		
		 
		result.add(bexport);
		result.setCellHorizontalAlignment(bexport,HasHorizontalAlignment.ALIGN_RIGHT);
		ClickListener click =new ClickListener(){
			public void onClick(Widget sender) {
				if (lang.equals("es"))
					getStatusPopup().setText("Exportando...");
				else
					getStatusPopup().setText("Exporting...");
				getStatusPopup().show();
				exporting=true;
				uploadParams();
			}
		};
		bexport.addClickListener(click);
		hlexport.addClickListener(click);
				
		result.setSpacing(4);
		vpResult.add(result);
		
		getStatusPopup().hide();
		
		
	}
	public void showResultText(String ttext, HashMap res){
		this.text=ttext;
		if (lang.equals("es"))
			getStatusPopup().setText("Actualizando Vista...");
		else
			getStatusPopup().setText("Updating View...");
		getStatusPopup().show();
		
		
		
		pages = new Pages(lang,text,res,getTableTypes());
		
		
		if (vpResult.getWidgetIndex(result)>=0)
			vpResult.remove(result);
		result = new VerticalPanel();		
		
		
		result.add(pages);		
		Button bexport;
		if (lang.equals("es")){
			bexport = new Button("Exportar...");
			hlexport = new Hyperlink("Exportar a XML", "export");
		}else{
			bexport = new Button("Export...");
			hlexport = new Hyperlink("Export to XML", "export");
		}
			
		
		
		ShortCutsPanel.getInstance().addHyperLink(hlexport);
		
		result.add(bexport);
		result.setCellHorizontalAlignment(bexport,HasHorizontalAlignment.ALIGN_RIGHT);
		ClickListener click =new ClickListener(){
			public void onClick(Widget sender) {
				if (lang.equals("es"))
					getStatusPopup().setText("Exportando...");
				else
					getStatusPopup().setText("Exporting...");
				getStatusPopup().show();
				exporting=true;
				uploadParams();
				
			}
		};
		bexport.addClickListener(click);
		hlexport.addClickListener(click);
					
		result.setSpacing(4);
		vpResult.add(result);
		if (resLabels.getWidgetIndex(nombreArchivo)>=0)
			resLabels.remove(nombreArchivo);
		resultadoDelAnalisisLabel.setVisible(true);
		getStatusPopup().hide();
		
		
	}
	
	public void showResult(int index){
		
		if (index>=0){
		
			if (lang.equals("es"))
				getStatusPopup().setText("Actualizando Vista...");
			else
				getStatusPopup().setText("Updating View...");
			getStatusPopup().show();
			if (resultados.get(index)!=null){
				
				Vector v = (Vector)resultados.get(index);
				
				if (!((Boolean)loadAnalyzedText.get(index)).booleanValue()){
					
					pages = new Pages(lang,(String)v.get(0),(HashMap)v.get(1),getTableTypes());
					loadAnalyzedText.set(index,new Boolean(true));
					analyzedtext.set(index,pages);
					
				}else if (!((Boolean)updatedView.get(index)).booleanValue()){				
					pages = (Pages)analyzedtext.get(index);
					pages.updateView();
					updatedView.set(index, new Boolean(true));
				}else{				
					pages = (Pages)analyzedtext.get(index);
				}
				
				if (resLabels.getWidgetIndex(nombreArchivo)>=0)
					resLabels.remove(nombreArchivo);
				
				if (listBox.getSelectedIndex()>=0)
					nombreArchivo = new Label(listBox.getItemText(listBox.getSelectedIndex()));
				else
					nombreArchivo = new Label(listBox.getItemText(0));
				
				nombreArchivo.setStyleName("lv-FileNameLabel");
				resLabels.add(nombreArchivo);
				
				
				if (vpResult.getWidgetIndex(result)>=0)
					vpResult.remove(result);
				result = new VerticalPanel();		
				result.setSpacing(4);
				result.add(pages);
				
				
				Button bexport;
				if (lang.equals("es")){
					bexport = new Button("Exportar...");
					hlexport = new Hyperlink("Exportar a XML", "export");
				}else{
					bexport = new Button("Export...");
					hlexport = new Hyperlink("Export to XML", "export");
				}
				
				
				ShortCutsPanel.getInstance().addHyperLink(hlexport);
				
				
				result.add(bexport);
				result.setCellHorizontalAlignment(bexport, HasHorizontalAlignment.ALIGN_RIGHT);
				ClickListener click =new ClickListener(){
					public void onClick(Widget sender) {
						if (lang.equals("es"))
							getStatusPopup().setText("Exportando...");
						else
							getStatusPopup().setText("Exporting...");
						getStatusPopup().show();
						exporting=true;
						uploadParams();
					}
				};		
				bexport.addClickListener(click);
				hlexport.addClickListener(click);
				vpResult.add(result);
				resultadoDelAnalisisLabel.setVisible(true);
				
				text = (String)v.get(0);
				
			}
			getStatusPopup().hide();
		}
	}	
	
	
	protected void onLoad() {	
		super.onLoad();		
		
		if (hlanalizar!=null)
			ShortCutsPanel.getInstance().addHyperLink(hlanalizar);
		
		if (hlupdate!=null)
			ShortCutsPanel.getInstance().addHyperLink(hlupdate);
		
		if (hlexport!=null)
			ShortCutsPanel.getInstance().addHyperLink(hlexport);
		
	}
	
	
	public void refeshPanel() {	
		super.refeshPanel();
		loadTableColors(getTypesNames());
		
	}
	
	
	private void exportar(String text){
		if (lang.equals("es"))
			getStatusPopup().setText("Exportando...");			
		else
			getStatusPopup().setText("Exporting...");
		
		getStatusPopup().show();
		
		exporting=false;
		
		getAas().exportResult(getCompsParams(), text, folder2,new AsyncCallback(){
			public void onFailure(Throwable caught) {
				getStatusPopup().getInstance().hide();
				Window.alert(caught.getMessage());
				
			}
			
			
			public void onSuccess(Object result) {
				getStatusPopup().getInstance().hide();
				String id = (String) result;
				Window.open(GWT.getModuleBaseURL()+"exportXML?id="+id, "_self", "");
				
			}
		});
		
	}
	
	
}
