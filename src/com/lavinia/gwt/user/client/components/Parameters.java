/*
 * Created on 15/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.components;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.Window;
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
import com.lavinia.gwt.user.client.datatypes.DTParamType;
import com.lavinia.gwt.user.client.datatypes.DTParameter;
import com.lavinia.gwt.user.client.utils.StringsTableLinks;
/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Parameters extends Composite{
	
	private VerticalPanel newParam;
	private VerticalPanel main;	
	private HorizontalPanel hp5;
	private HorizontalPanel tablePanel;
	private StringsTableLinks tvalues = new StringsTableLinks("es",true);
		
	private TableParams table;
	private TextBox tname;
	private ListBox ttype;
	private Hyperlink hlrem;
	
	private boolean showingParam=false;
	private Collection infoParams=new ArrayList();;
	private TextArea tdescription;
	private CheckBox treq;
	private CheckBox tmult;
	private int count = 0;
	
	private String lang;
	
	public Parameters(String l, boolean enabled){
		this.lang = l;
		table = new TableParams(lang);
		tablePanel = new HorizontalPanel();
		main = new VerticalPanel();
		
        if (enabled){
        	
	        Hyperlink hladd;
	        if (lang.equals("es")){
	        	hladd = new Hyperlink("Agregar Parámetro","AddParameter");
	        	hlrem = new Hyperlink("Eliminar Parámetro","RemoveParameter");
	        }else{
	        	hladd = new Hyperlink("Add Parametter","AddParameter");
	        	hlrem = new Hyperlink("Delete Parametter","RemoveParameter");
	        }
	        hlrem.setVisible(false);
			hladd.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			        if (!showingParam){
			        	addParameter();		        	
			        	main.add(newParam);
			        	showingParam=true;
			        }
			        
			      }
			    });
			
			hlrem.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			      	String name = table.getNameSelected();
			        removeParamInfo(name);
			        if (table.removeSelectedRow()){
			        	hlrem.setVisible(false);
			        	count=0;
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
		
		infoParams = new ArrayList();
		
		main.setHeight("100%");
		initWidget(main);
		
	}
	
	private void addParameter(){
		
		/********LABELS***********************/
		Label lname, ltype, ldescription, lreq, lmult, lvalues;
		
		if (lang.equals("es")){
			lname = new Label("Nombre");
			lname.setWidth("140");
			ltype = new Label("Tipo");
			ltype.setWidth("140");
			ldescription = new Label("Descripción");
			ldescription.setWidth("140");
			lreq = new Label("Requerido");
			lreq.setWidth("140");
			lmult = new Label("Multivaluado");
			lmult.setWidth("140");
			lvalues = new Label("Valores");
			lvalues.setWidth("140");
		}else{
			lname = new Label("Name");
			lname.setWidth("140");
			ltype = new Label("Type");
			ltype.setWidth("140");
			ldescription = new Label("Description");
			ldescription.setWidth("140");
			lreq = new Label("Mandatory");
			lreq.setWidth("140");
			lmult = new Label("Multivalued");
			lmult.setWidth("140");
			lvalues = new Label("Values");
			lvalues.setWidth("140");
		}
		
		
		
		/*******TEXTBOXS AND OTHERS***********/
		tname = new TextBox();
		tname.setWidth("260");
		
		ttype = new ListBox();
		ttype.addItem("String");
		ttype.addItem("Float");
		ttype.addItem("Integer");
		ttype.addItem("Boolean");
		if (lang.equals("es"))
			ttype.addItem("Archivo");
		else
			ttype.addItem("File");
		ttype.setVisibleItemCount(1);
		
		ttype.addChangeListener(new ChangeListener(){

			public void onChange(Widget arg0) {
				
				if (hp5.getWidgetIndex(tvalues)>=0)
					hp5.remove(tvalues);
				tvalues=null;
				if (lang.equals("es")){
					if (!ttype.getItemText(ttype.getSelectedIndex()).equals("Archivo")){
						tvalues = new StringsTableLinks(lang,ttype.getItemText(ttype.getSelectedIndex()),true);
						hp5.add(tvalues);
					}
				}else{
					if (!ttype.getItemText(ttype.getSelectedIndex()).equals("File")){
						tvalues = new StringsTableLinks(lang,ttype.getItemText(ttype.getSelectedIndex()),true);
						hp5.add(tvalues);
					}
				}
				
				
			}});
		
		treq = new CheckBox();		
	    treq.setChecked(true);
	    
	    tmult = new CheckBox();		
	    tmult.setChecked(false);
	    
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
		      		
		      	addParamInfo();
		      	
		      	
		      }
		    });
		
		bcancel.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		        main.remove(newParam);
		        showingParam=false;
		      }
		    });
		
	    
	    /*********PANELS***********************/
	    
	    HorizontalPanel hp1 = new HorizontalPanel();
	    hp1.add(lname);
	    hp1.add(tname);
	    
	    HorizontalPanel hp2 = new HorizontalPanel();
	    hp2.add(ltype);
	    hp2.add(ttype);
	    
	    HorizontalPanel hp3 = new HorizontalPanel();
	    hp3.add(ldescription);
	    hp3.add(tdescription);
	    
	    HorizontalPanel hp4 = new HorizontalPanel();
	    hp4.add(lreq);
	    hp4.add(treq);
	    
	    HorizontalPanel hp7 = new HorizontalPanel();
	    hp7.add(lmult);
	    hp7.add(tmult);
	    
	    HorizontalPanel hp6 = new HorizontalPanel();
	    hp6.add(badd);
	    hp6.add(bcancel);
	    hp6.setSpacing(8);
	    
	    
	    
	    /**********ALLOWED VALUES******************/
	    hp5 = new HorizontalPanel();
	    
	    tvalues = new StringsTableLinks(lang,"String",true);    
	    
	    hp5.add(tvalues);	    

	    
	    /**********MAIN PANEL**********************/
	    
	    newParam = new VerticalPanel();
	    newParam.add(hp1);
	    newParam.add(hp2);
	    newParam.add(hp3);
	    newParam.add(hp4);
	    newParam.add(hp7);
	    newParam.add(lvalues);
	    newParam.add(hp5);
	    newParam.add(hp6);
	    newParam.setCellHorizontalAlignment(hp6,HasHorizontalAlignment.ALIGN_CENTER);
	    newParam.setSpacing(8);
	    newParam.setStyleName("lv-Sink");
		
	}
	
	private void addParamInfo(){
		
		if (tname.getText().length()>0){
			if ((tname.getText().indexOf(" ")==-1)&&(tname.getText().indexOf(".")==-1)){
				if (tdescription.getText().length()>0){					
					
					
					Collection values = new ArrayList();	
					if (tvalues!=null){
						for(int i=0;i<tvalues.getCount();i++)
							values.add(new DTParamType(tvalues.getText(i)));
					}
					
					String req = "false";
					if (treq.isChecked())
						req = "true";
					
					String mult = "false";
					if (tmult.isChecked())
						mult = "true";
					infoParams.add(new DTParameter(tname.getText(),ttype.getItemText(ttype.getSelectedIndex()),tdescription.getText(),req,mult,values));
					
					if (count==0){
						tablePanel.add(table);		
						hlrem.setVisible(true);
					}
					String s = ttype.getItemText(ttype.getSelectedIndex());
					table.addRow(tname.getText(),s, req, mult);
			      	count++;
			      	if (tvalues!=null){
			      		for(int i=0;i<tvalues.getCount();i++)		      			
			      			table.addValue(tvalues.getText(i));
			      	}
			      	
			        main.remove(newParam);	
			        showingParam=false;
			        
				}else{
					if (lang.equals("es"))
						Window.alert("Ingrese descripción del parámetro");
					else
						Window.alert("Missing parametter description");
				}
			}else{
				if (lang.equals("es"))
					Window.alert("Nombre de parámetro inválido");
				else
					Window.alert("Invalid name");
			}
		}else{
			if (lang.equals("es"))
				Window.alert("Ingrese nombre del parámetro");
			else
				Window.alert("Missing parametter name");
			
		}
	}
	
	private void removeParamInfo(String name){
		Iterator it = infoParams.iterator();
		DTParameter dt;
		boolean end=false;
		while((!end)&&(it.hasNext())){
			dt = (DTParameter)it.next();
			if (dt.getName().equals(name)){
				it.remove();
				end=true;
			}
		}
	}
	
	public Collection saveParameters(){
		
		return infoParams;
	}
	
	public void loadParameters(Collection p){		
		infoParams.addAll(p);
		DTParameter dtp;
		DTParamType val;	
		Iterator it1 = p.iterator();
		Iterator it2;
		while (it1.hasNext()){
			dtp = (DTParameter)it1.next();
			table.addRow(dtp.getName(), dtp.getType(), dtp.getMandatory(), dtp.getMultivalued());
			it2=dtp.getValues().iterator();
			while(it2.hasNext()){
				val = (DTParamType)it2.next();
				table.addValue(val.getValue());
			}
			count++;
		}
		if (count>0){
			tablePanel.add(table);
			if (hlrem!=null)
				hlrem.setVisible(true);
		}
	}
}
