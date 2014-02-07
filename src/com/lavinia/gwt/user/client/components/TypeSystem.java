/*
 * Created on 16/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.components;
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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.datatypes.DTFeature;
import com.lavinia.gwt.user.client.datatypes.DTFeatureFS;
import com.lavinia.gwt.user.client.datatypes.DTType;
import com.lavinia.gwt.user.client.datatypes.DTTypeF;
import com.lavinia.gwt.user.client.datatypes.DTTypeString;
import com.lavinia.gwt.user.client.datatypes.DTTypeValue;
import com.lavinia.gwt.user.client.interfaces.ITypes;
import com.lavinia.gwt.user.client.interfaces.ITypesAsync;
import com.lavinia.gwt.user.client.utils.StringsTable;
/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TypeSystem  extends Composite{

	private TableTypes table;
	private VerticalPanel main;
	private VerticalPanel newType;
	private VerticalPanel extras;
	private ListBox tsupertype = new ListBox();
	private int count;
	private VerticalPanel right;
	private TextBox tvalue;

	private StringsTable listvalues = new StringsTable();
	private StringsTable listfeatures = new StringsTable();
	private ListBox ttypef = new ListBox();
	
	private VerticalPanel newValue;
	private VerticalPanel newFeature;
	private boolean showingStringValue=false;
	private HorizontalPanel valueStringPanel;
	private HorizontalPanel featuresPanel;
	private HorizontalPanel tablePanel;
	private boolean showingFeatures=false;
	private TextBox tnameF;
	private HorizontalPanel hpElemType;
	private HorizontalPanel hpanelE;
	private boolean showingType=false;
	private TextBox tname;
	private ListBox ttypel=new ListBox();
	private Hyperlink hlremValue;
	private Hyperlink hlrem;
	private Hyperlink hlremFeature;
	private Collection infoTypes= new ArrayList();
	private Collection infoValues;
	private Collection infoFeatures;
	private CheckBox tinput;
	private CheckBox toutput;
	private TextArea tdescription; 
	private TextArea tdescriptionV;
	private TextArea tdescriptionF;
	
	private Collection supertypes = new Vector();
	private Collection rangetypes = new Vector();
	private Collection multitypes = new Vector();
	
	private ITypesAsync ass;
	private ServiceDefTarget endpoint;

	private boolean emptyTable=true;
	
	private String lang;
	
	public TypeSystem(String l, boolean enabled){
		this.lang = l;
		table = new TableTypes(lang);
		tablePanel = new HorizontalPanel();
		main = new VerticalPanel();
		
		if (enabled){
		
			Hyperlink hladd;
			if (lang.equals("es")){
				hladd = new Hyperlink("Agregar Tipo","AddType");	
				hlrem = new Hyperlink("Eliminar Tipo","RemType");
			}else{				
				hladd = new Hyperlink("Add Type","AddType");	
				hlrem = new Hyperlink("Delete Type","RemType");
			}
			hlrem.setVisible(false);
			hladd.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			      	if (!showingType){
			      		addType();		        
			      		main.add(newType);
			      		showingType=true;
			      	}
			      }
			    });
			
			hlrem.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			        String name = table.getNameSelected();
			        removeTypeInfo(name);
			        if (table.removeSelectedRow()){
			        	hlrem.setVisible(false);
			        	emptyTable=true;
			        	tablePanel.remove(table);
			        }
			      }
			    });
			
			
			
			VerticalPanel hpopt = new VerticalPanel();
	        hpopt.add(hladd);     
	        hpopt.add(hlrem);
	        hpopt.setSpacing(4);	        
			main.add(hpopt);
		}
		
		main.add(tablePanel);       
        main.setCellHorizontalAlignment(tablePanel,HasHorizontalAlignment.ALIGN_CENTER);
		main.setSpacing(8);
		
		infoTypes = new ArrayList();
		
		
		ass = (ITypesAsync)GWT.create(ITypes.class);    
	    endpoint = (ServiceDefTarget)ass;	       
	 
	    endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"ITypes");
	 	    
		ass.getTypes(false,new AsyncCallback() {
	        public void onFailure(Throwable caught) {	        	        	
	        	Window.alert("ERROR: " +caught.getMessage());
	          }
	          public void onSuccess(Object result) {
	    	    supertypes = (Collection)result;	    	    
	          }
	    });
		ass.getTypes(true,new AsyncCallback() {
	        public void onFailure(Throwable caught) {	        	   	
	        	Window.alert("ERROR: " +caught.getMessage());
	          }
	          public void onSuccess(Object result) {
	    	    rangetypes = (Collection)result;	    	    
	          }
	    });
		
		ass.getMultiTypes(new AsyncCallback() {
	        public void onFailure(Throwable caught) {	        	        	
	        	Window.alert("ERROR: " +caught.getMessage());
	          }
	          public void onSuccess(Object result) {
	    	    multitypes = (Collection)result;	    	    
	          }
	    });

	    
		initWidget(main);		
		main.setHeight("100%");
	}
	
	private void addType(){
		
		
		
		/********LABELS********************************/
		Label lname, lsupertype,ldescription,linput,loutput;
		
		if (lang.equals("es")){
			lname = new Label("Nombre");
			lname.setWidth("140");
			lsupertype= new Label("SuperTipo");
			lsupertype.setWidth("140");
			ldescription = new Label("Descripción");
			ldescription.setWidth("140");
			linput = new Label("Entrada");
			linput.setWidth("140");
			loutput = new Label("Salida");
			loutput.setWidth("140");
		}else{
			lname = new Label("Name");
			lname.setWidth("140");
			lsupertype= new Label("SuperType");
			lsupertype.setWidth("140");
			ldescription = new Label("Description");
			ldescription.setWidth("140");
			linput = new Label("Input");
			linput.setWidth("140");
			loutput = new Label("Output");
			loutput.setWidth("140");
		}
		
		
		/********TEXTBOX, TEXTAREA, CHECKBOX, LISTBOX****/
		tname = new TextBox();
		tname.setWidth("260");
		
		updateSupertypes();
		
		tsupertype.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender){
				if ((tsupertype.getItemText(tsupertype.getSelectedIndex())).equals("String")){
					extrasString();
				
				}else{
					extrasNoString();
				}				
			}
		});		
		
		tinput = new CheckBox();		
		tinput.setChecked(false);
		toutput = new CheckBox();		
		toutput.setChecked(false);
		
		tdescription = new TextArea();
		tdescription.setWidth("260");	    
		tdescription.setVisibleLines(4);
		
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
		      	addTypeInfo();		      	
		      	      	
		      	
		      }
		    });
		
		bcancel.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		      	main.remove(newType);
		        showingType=false;
		      }
		    });
		
		/*********PANELS***********************************/
		
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(lname);
		hp1.add(tname);
		
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(lsupertype);
		hp2.add(tsupertype);
		
		HorizontalPanel hp3 = new HorizontalPanel();
		hp3.add(ldescription);
		hp3.add(tdescription);
		
		HorizontalPanel hp4 = new HorizontalPanel();
		hp4.add(linput);
		hp4.add(tinput);
		
		HorizontalPanel hp5 = new HorizontalPanel();
		hp5.add(loutput);
		hp5.add(toutput);
		
		HorizontalPanel hp6 = new HorizontalPanel();
		hp6.add(badd);
		hp6.add(bcancel);
		hp6.setSpacing(8);
		
		extras = new VerticalPanel();
		extrasNoString();
		/**********MAIN PANEL*****************************/	
		
		newType = new VerticalPanel();
		newType.add(hp1);
		newType.add(hp2);
		newType.add(hp3);
		newType.add(hp4);
		newType.add(hp5);
		newType.add(extras);
		newType.add(hp6);
		newType.setSpacing(8);
		newType.setStyleName("lv-Sink");
		newType.setCellHorizontalAlignment(hp6,HasHorizontalAlignment.ALIGN_CENTER);	
		
	}
	
	private void updateTable(){
		
		if (emptyTable){
			tablePanel.add(table);
			emptyTable=false;
			hlrem.setVisible(true);
		}
		
		table.addType(tname.getText(),tsupertype.getItemText(tsupertype.getSelectedIndex()),tinput.isChecked(),toutput.isChecked());
		
		if ((tsupertype.getItemText(tsupertype.getSelectedIndex())).equals("String")){
			for (int i=0;i<listvalues.getCount();i++){
				table.addValue(listvalues.getText(i));				
			}
		}else{
			
			for (int i=0;i<listfeatures.getCount();i++){
				table.addFeature(listfeatures.getText(i));
			}
		}
		supertypes.add(tname.getText());
		tsupertype.addItem(tname.getText());
		
		rangetypes.add(tname.getText());
		ttypef.addItem(tname.getText());
		
		multitypes.add(tname.getText());
		ttypel.addItem(tname.getText());
		
	}
	
	private void updateSupertypes(){
			
		Iterator it = supertypes.iterator();
		while (it.hasNext()){
			String s=(String)it.next();
			tsupertype.addItem(s,s);
		}	
		
		tsupertype.setVisibleItemCount(1);				
	}
	
	private void updateRangetypes(){
				
		Iterator it = rangetypes.iterator();
		while (it.hasNext()){
			String s=(String)it.next();
			ttypef.addItem(s,s);
		}	
				
		ttypef.setVisibleItemCount(1);
				
	}
	private void updateElementtypes(){
	
		Iterator it2 = multitypes.iterator();
		while (it2.hasNext()){
			String s=(String)it2.next();
			ttypel.addItem(s,s);
			
		}		
		ttypel.setVisibleItemCount(1);			
	}
	
	
	/****LINK TO ADD VALUES***********************/
	private void extrasString(){
		
		Hyperlink hladdValue;
		if (lang.equals("es"))
			hladdValue = new Hyperlink("Valores Permitidos", "addValue");
		else
			hladdValue = new Hyperlink("Allowed Values", "addValue");
		valueStringPanel = valueStringPanel();
		extras.clear();
		hladdValue.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {		        
		      	if (!showingStringValue){
		      		
		      		extras.add(valueStringPanel);
		      		showingStringValue=true;
		      	}else{
		      		extras.remove(valueStringPanel);
		      		showingStringValue=false;
		      	}
		      }
		    });
		extras.add(hladdValue);
	}
	
	/****LINK TO ADD FEATURES********************/
	private void extrasNoString(){
		
		Hyperlink hladdFeature;
		if (lang.equals("es"))
			hladdFeature = new Hyperlink("Atributos", "addFeatures");
		else
			hladdFeature = new Hyperlink("Features", "addFeatures");
		featuresPanel = featuresPanel();
		extras.clear();
		
		hladdFeature.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {		        
		      	if (!showingFeatures){
		      		
		      		extras.add(featuresPanel);
		      		showingFeatures=true;
		      	}else{
		      		extras.remove(featuresPanel);
		      		showingFeatures=false;
		      	}
		      }
		    });
		extras.add(hladdFeature);
		
	}
	
	private HorizontalPanel featuresPanel(){
		HorizontalPanel result = new HorizontalPanel();
		
		listfeatures = new StringsTable();
		infoFeatures = new ArrayList();
		
		result.add(listfeatures);
		result.setSpacing(4);
		
		right = new VerticalPanel();
		
		Hyperlink hladdFeature;
		if (lang.equals("es")){
			hladdFeature = new Hyperlink("Agregar","addFeature");
			hlremFeature = new Hyperlink("Eliminar","remFeature");
		}else{
			hladdFeature = new Hyperlink("Add","addFeature");
			hlremFeature = new Hyperlink("Delete","remFeature");
		}
		hlremFeature.setVisible(false);
		
		HorizontalPanel hoptions = new HorizontalPanel();
		hoptions.add(hladdFeature);
		hoptions.add(hlremFeature);
		hoptions.setSpacing(4);
		
		right.add(hoptions);		
		right.setSpacing(4);
		result.add(right);
		
		hladdFeature.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		      	if (count==0){
		      		newFeature = newFeaturePanel();
		      		right.add(newFeature);
		      		count++;
		      	}
		      	
		      }
		    });
		hlremFeature.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {
			 	infoFeatures.remove(infoFeatures.toArray()[listfeatures.getSelectedRow()]);
			 	listfeatures.removeSelectedRow();			 	
		      	if (listfeatures.getCount()==0){
		      		hlremFeature.setVisible(false);
		      	}else
		      		listfeatures.selectRow(0);
				
		      }
		    });
		
		result.setStyleName("lv-Sink");
		return result;
	}	
		
	
	/******LIST OF VALUES AND RIGHT PANEL WITH OPTIONS*************/
	private HorizontalPanel valueStringPanel(){
		HorizontalPanel result = new HorizontalPanel();
		
		listvalues = new StringsTable();
		listvalues.setStyleName("lv-Table");	
		
		infoValues = new ArrayList();
		
		result.add(listvalues);
		result.setSpacing(4);
	
		right = new VerticalPanel();
	
		Hyperlink hladdValue;
		if (lang.equals("es")){
			hladdValue = new Hyperlink ("Agregar","addSValue");
			hlremValue = new Hyperlink ("Eliminar","remSValue");
		}else{
			hladdValue = new Hyperlink ("Add","addSValue");
			hlremValue = new Hyperlink ("Delete","remSValue");
		}
		
		hlremValue.setVisible(false);
		HorizontalPanel hoptions = new HorizontalPanel();
		hoptions.add(hladdValue);
		hoptions.add(hlremValue);
		hoptions.setSpacing(4);
		
		right.add(hoptions);
		right.setSpacing(4);
		result.add(right);
		
		hladdValue.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		      	if (count==0){
		      		newValue = newValuePanel();
		      		right.add(newValue);
		      		count++;
		      	}
		      	
		      }
		    });
		hlremValue.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {
			 	infoValues.remove(infoValues.toArray()[listvalues.getSelectedRow()]);
			 	listvalues.removeSelectedRow();		      	
		      	if (listvalues.getCount()==0){
		      		hlremValue.setVisible(false);
		      	}else
		      		listvalues.selectRow(0);
				
		      }
		    });
		
		result.setStyleName("lv-Sink");
		return result;		
	}
	
	private VerticalPanel newFeaturePanel(){
		
		VerticalPanel result = new VerticalPanel();
		
		Label lname, ltype, ldesc, lelemtype;
		
		if (lang.equals("es")){
			lname = new Label("Nombre");
			lname.setWidth("100");
			ltype = new Label("Tipo");
			ltype.setWidth("100");
			ldesc = new Label ("Descripción");
			ldesc.setWidth("100");
			lelemtype = new Label ("Tipo de Elementos");
			lelemtype.setWidth("100");
		}else{
			lname = new Label("Name");
			lname.setWidth("100");
			ltype = new Label("Type");
			ltype.setWidth("100");
			ldesc = new Label ("Description");
			ldesc.setWidth("100");
			lelemtype = new Label ("Element's Type");
			lelemtype.setWidth("100");
		}
		
		tnameF = new TextBox();
		tnameF.setWidth("140");
		
		updateRangetypes();
		
		ttypef.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender){
				String elem=ttypef.getItemText(ttypef.getSelectedIndex());
				if ((elem.endsWith("FSArray"))||(elem.endsWith("FSList"))){					
					if (hpanelE.getWidgetCount()==0)
						hpanelE.add(hpElemType);
				}else{
					hpanelE.clear();
				}
				
			}
		});
		
		updateElementtypes();
		
		tdescriptionF = new TextArea();
		tdescriptionF.setWidth("140");	    
		tdescriptionF.setVisibleLines(4);
		
		Button accept = new Button();
		Button cancel = new Button ();
		if (lang.equals("es")){
			accept.setText("Aceptar");
			cancel.setText("Cancelar");
		}else{
			accept.setText("OK");
			cancel.setText("Cancel");
		}
		
		/***********************************************/
		
		HorizontalPanel hpanel1 = new HorizontalPanel();
		hpanel1.add(lname);
		hpanel1.add(tnameF);
		
		HorizontalPanel hpanel2 = new HorizontalPanel();
		hpanel2.add(ltype);
		hpanel2.add(ttypef);
		
		HorizontalPanel hpanel3 = new HorizontalPanel();
		hpanel3.add(ldesc);
		hpanel3.add(tdescriptionF);
		
		hpElemType = new HorizontalPanel();
		hpElemType.add(lelemtype);
		hpElemType.add(ttypel);
		
		hpanelE = new HorizontalPanel();
		
		HorizontalPanel hpanel5 = new HorizontalPanel();
		hpanel5.add(accept);
		hpanel5.add(cancel);	
		hpanel5.setSpacing(4);
		
		/************************************************/
		accept.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				if (tnameF.getText()!=""){	    	
					if ((tnameF.getText().indexOf(" ")==-1)&&(tnameF.getText().indexOf(".")==-1)){
						String s = ttypef.getItemText(ttypef.getSelectedIndex());
						String sv = ttypef.getValue(ttypef.getSelectedIndex());
						if ((s.endsWith("FSArray"))||(s.endsWith("FSList"))){
							String s2 = ttypel.getItemText(ttypel.getSelectedIndex());
							String s2v = ttypel.getValue(ttypel.getSelectedIndex());
							listfeatures.addRow(tnameF.getText()+" : "+ s+"("+s2+")");
							infoFeatures.add(new DTFeatureFS(tnameF.getText(),sv,tdescriptionF.getText(),s2v));
							
						}else{
							listfeatures.addRow(tnameF.getText()+" : "+ s);
							infoFeatures.add(new DTFeature(tnameF.getText(),sv,tdescriptionF.getText()));
							
						}
						right.remove(newFeature);
						count=0;
						hlremFeature.setVisible(true);
					}else{
						if (lang.equals("es"))
							Window.alert("Nombre de atributo inválido");
						else
							Window.alert("Invalid feature name");
					}
				}else{
					right.remove(newFeature);
					count=0;
				}
			}
		    });
		cancel.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				right.remove(newFeature);
				count=0;
				
			}
		    });		
	   
		/**************************************************/
		result.add(hpanel1);
		result.add(hpanel2);
		result.add(hpanel3);
		result.add(hpanelE);		
		result.add(hpanel5);
		result.setCellHorizontalAlignment(hpanel5,HasHorizontalAlignment.ALIGN_CENTER);
		result.setSpacing(4);
		
		return result;
	}
	
	/**** VALUE, DESCRIPTION, AND ACCEPT BUTTON ***********/
	private VerticalPanel newValuePanel(){
		VerticalPanel result = new VerticalPanel();
		
		Label lvalue, ldesc;
		if (lang.equals("es")){
			lvalue = new Label("Valor");
			lvalue.setWidth("100");
			ldesc = new Label ("Descripción");
			ldesc.setWidth("100");
		}else{
			
			lvalue = new Label("Value");
			lvalue.setWidth("100");
			ldesc = new Label ("Description");
			ldesc.setWidth("100");
		}
		
		tvalue = new TextBox();
		tvalue.setWidth("140");
		
		Button accept = new Button();
		Button cancel = new Button ();
		if (lang.equals("es")){
			accept.setText("Aceptar");
			cancel.setText("Cancelar");
		}else{
			accept.setText("OK");
			cancel.setText("Cancel");
		}
		
		tdescriptionV = new TextArea();
		tdescriptionV.setWidth("140");	    
		tdescriptionV.setVisibleLines(4);
		
		/********ACCEPT AND CANCEL ACTIONS****************/
		
		accept.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				if (tvalue.getText()!=""){	    					
					listvalues.addRow(tvalue.getText());	
					infoValues.add(new DTTypeValue(tvalue.getText(),tdescriptionV.getText()));
					right.remove(newValue);
					count=0;
					hlremValue.setVisible(true);
				}else{
					right.remove(newValue);
					count=0;
				}
			}
		    });
		cancel.addClickListener(new ClickListener(){
			public void onClick(Widget sender){
				right.remove(newValue);
				count=0;
				
			}
		    });		
	    /****************************************************/
		
		
		
		HorizontalPanel hpanel1 = new HorizontalPanel();
		hpanel1.add(lvalue);
		hpanel1.add(tvalue);
		
		HorizontalPanel hpanel2 = new HorizontalPanel();
		hpanel2.add(ldesc);
		hpanel2.add(tdescriptionV);
				
		HorizontalPanel hpanel3 = new HorizontalPanel();
		hpanel3.add(accept);
		hpanel3.add(cancel);	
		hpanel3.setSpacing(4);
		
		result.add(hpanel1);
		result.add(hpanel2);
		result.add(hpanel3);
		result.setCellHorizontalAlignment(hpanel3,HasHorizontalAlignment.ALIGN_CENTER);
		result.setSpacing(4);
		
		return result;
		
	}
	
	private void addTypeInfo(){
		if (tname.getText().length()>0){
			if ((tname.getText().indexOf(" ")==-1)){
				if (tinput.isChecked()||toutput.isChecked()){
					DTType dt=new DTTypeF();
					
					String sup = tsupertype.getValue(tsupertype.getSelectedIndex());
					if (sup.equals("String")){			
						dt = new DTTypeString(tname.getText(),sup,tdescription.getText(),tinput.isChecked(),toutput.isChecked(),infoValues);
					}else{			
						dt = new DTTypeF(tname.getText(),sup,tdescription.getText(),tinput.isChecked(),toutput.isChecked(),infoFeatures);
					}
					infoTypes.add(dt);
					updateTable();
					main.remove(newType);
			      	showingType=false;							
				}else{
					if (lang.equals("es"))
						Window.alert("Tipo debe ser Entrada y/o Salida");
					else
						Window.alert("A type should be input and/or output");
				}
			}else{
				if (lang.equals("es"))
					Window.alert("Nombre de tipo inválido");
				else
					Window.alert("Invalid type name");
			}
		}else{
			if (lang.equals("es"))
				Window.alert("Ingrese nombre del tipo");			
			else
				Window.alert("Missing type name");
		}
	}
				
	private void removeTypeInfo(String name){
		Iterator it = infoTypes.iterator();
		DTType dt;
		boolean end=false;
		while((!end)&&(it.hasNext())){
			dt = (DTType)it.next();
			if (dt.getName().equals(name)){
				it.remove();
				end=true;
			}
		}
	}

	public Collection saveTypes(){	
		
		return infoTypes;		
		
	}

	public void loadTypes(Collection t){
		infoTypes.addAll(t);
		DTType dt;
		Iterator it = t.iterator();
		Iterator it2;
		
		while (it.hasNext()){
			dt = (DTType)it.next();		
			
			table.addType(dt.getName(),dt.getSupertype(),dt.isInput(),dt.isOutput());
			
			if (dt.getSupertype().equals("String")){				
				DTTypeString dts = (DTTypeString)dt;
				it2=dts.getValues().iterator();
				while (it2.hasNext()){
					DTTypeValue dtv = (DTTypeValue)it2.next();
					table.addValue((String)dtv.getValue());
				}			
			}else{
				DTTypeF dtf = (DTTypeF)dt;
				it2=dtf.getFeatures().iterator();
				while (it2.hasNext()){
					DTFeature df = (DTFeature)it2.next();
					
					table.addFeature(df.getName()+" : "+ df.getType());
				}
				
			}
			
			supertypes.add(dt.getName());					
			tsupertype.addItem(dt.getName(),dt.getName());			
				
			
			rangetypes.add(dt.getName());			
			ttypef.addItem(dt.getName(),dt.getName());
			
			multitypes.add(dt.getName());			
			ttypel.addItem(dt.getName(),dt.getName());
			
		}
		if (t.size()>0){
			tablePanel.add(table);
			emptyTable=false;
			if (hlrem!=null)
				hlrem.setVisible(true);
		}
	}
	
}
