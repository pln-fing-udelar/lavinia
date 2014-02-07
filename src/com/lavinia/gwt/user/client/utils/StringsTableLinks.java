/*
 * Created on 07/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.utils;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StringsTableLinks extends Composite{

	private StringsTable table;
	private Hyperlink hladd;
	private Hyperlink hlrem;
	private int count=0;
	private VerticalPanel hp;
	private Widget tvalue;
	private VerticalPanel hv = new VerticalPanel();
	private HorizontalPanel main;
	private String type = "";
	private boolean enabled;

	private String lang;
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public StringsTableLinks(String l,boolean enabled){
		this.enabled = enabled;
		this.lang = l;
		
		table = new StringsTable();		
		if (enabled){
			
			if (lang.equals("es")){
				hladd=new Hyperlink("Agregar","addValue");
				hlrem=new Hyperlink("Eliminar","removeValue");
			}else{
				hladd=new Hyperlink("Add","addValue");
				hlrem=new Hyperlink("Remove","removeValue");
			}
			hlrem.setVisible(false);
			HorizontalPanel options = new HorizontalPanel();
			options.add(hladd);
			options.add(hlrem);
			options.setSpacing(8);
			hv = new VerticalPanel();
			hv.add(options);
		
		    hladd.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			      	if (count==0){
			      		hp = new VerticalPanel();
			      		tvalue = new TextBox();
			      		tvalue.setWidth("110");
			      		Button baccept = new Button();
			      		if (lang.equals("es"))
			      			baccept.setText("Aceptar");
			      		else
			      			baccept.setText("OK");
			      		baccept.addClickListener(new ClickListener(){
		    			public void onClick(Widget sender){
		    				if (((TextBox)tvalue).getText()!=""){	    					
		    					table.addRow(((TextBox)tvalue).getText());	    					
		    					hv.remove(hp);
		    					count=0;
		    					hlrem.setVisible(true);
		    				}else{
		    					hv.remove(hp);
		    					count=0;
		    				}
						}
		    		    });
			      		
			      		hp.add(tvalue);
			      		hp.add(baccept);
			      		hp.setSpacing(8);
			      		hv.add(hp);
			      		count++;
			      	}
			      	
			      }
			    });	    
		    hlrem.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			      	table.removeSelectedRow();		      	
			      	if (table.getCount()==0){
			      		hlrem.setVisible(false);
			      	}else
			      		table.selectRow(0);
			      }
			      
			    });
		}
		main = new HorizontalPanel();
	    main.add(table);
	    main.add(hv);
	    initWidget(main);
	}
	
	public StringsTableLinks(String l, String rtype, boolean enabled){
		
		this.type=rtype;
		this.lang = l;
		table = new StringsTable();		
		if (enabled){
			if (lang.equals("es")){
				hladd=new Hyperlink("Agregar","addValue");
				hlrem=new Hyperlink("Eliminar","removeValue");
			}else{
				hladd=new Hyperlink("Add","addValue");
				hlrem=new Hyperlink("Remove","removeValue");
			}
			hlrem.setVisible(false);
			HorizontalPanel options = new HorizontalPanel();
			options.add(hladd);
			options.add(hlrem);
			options.setSpacing(8);
			hv = new VerticalPanel();
			hv.add(options);
		
		    hladd.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			      	if (count==0){
			      		hp = new VerticalPanel();
			      		if (type.equals("Boolean")){
			      			tvalue= new ListBox();
			      			if (lang.equals("es")){
			      				((ListBox)tvalue).addItem("Verdadero","true");
			      				((ListBox)tvalue).addItem("Falso","false");
			      			}else{
			      				((ListBox)tvalue).addItem("True","true");
			      				((ListBox)tvalue).addItem("False","false");
			      			}
			      		}else{
			      			tvalue = new TextBox();
			      		}
			      		tvalue.setWidth("110");
			      		
			      		Button baccept = new Button();
			      		if (lang.equals("es"))
			      			baccept.setText("Aceptar");
			      		else
			      			baccept.setText("OK");
			      		
			      		baccept.addClickListener(new ClickListener(){
		    			public void onClick(Widget sender){
		    				if (type.equals("Boolean")){
		    					table.addRow(((ListBox)tvalue).getItemText(((ListBox)tvalue).getSelectedIndex()));	    					
		    					hv.remove(hp);
		    					count=0;
		    					hlrem.setVisible(true);
		    				}else if (type.equals("Integer")){
		    					if (Auxiliar.isInteger(((TextBox)tvalue).getText())) {
		    						if (((TextBox)tvalue).getText()!=""){
		    							table.addRow(((TextBox)tvalue).getText());	    					
				    					hv.remove(hp);
				    					count=0;
				    					hlrem.setVisible(true);
		    						}else{
		    							hv.remove(hp);
				    					count=0;
		    						}
			    				}else{
			    					if (lang.equals("es"))
			    						Window.alert("Valor Incorrecto");
			    					else
			    						Window.alert("Invalid Value");
			    				}	    					
		    				}else if (type.equals("Float")){
		    					if (Auxiliar.isFloat(((TextBox)tvalue).getText())) {
		    						if (((TextBox)tvalue).getText()!=""){
			    						table.addRow(((TextBox)tvalue).getText());	    					
				    					hv.remove(hp);
				    					count=0;
				    					hlrem.setVisible(true);
		    						}else{
		    							hv.remove(hp);
				    					count=0;
		    						}
			    				}else{
			    					if (lang.equals("es"))
			    						Window.alert("Valor Incorrecto");
			    					else
			    						Window.alert("Invalid Value");
			    				}
		    				}else{
		    					if (((TextBox)tvalue).getText()!=""){	    					
			    					table.addRow(((TextBox)tvalue).getText());	    					
			    					hv.remove(hp);
			    					count=0;
			    					hlrem.setVisible(true);
			    				}else{
			    					hv.remove(hp);
			    					count=0;
			    				}
		    				}
						}
		    		    });
			      		
			      		hp.add(tvalue);
			      		hp.add(baccept);
			      		hp.setSpacing(8);
			      		hv.add(hp);
			      		count++;
			      	}
			      	
			      }
			    });	    
		    hlrem.addClickListener(new ClickListener() {
			      public void onClick(Widget sender) {
			      	table.removeSelectedRow();		      	
			      	if (table.getCount()==0){
			      		hlrem.setVisible(false);
			      	}else
			      		table.selectRow(0);
			      }
			      
			    });	    
		}
	    main = new HorizontalPanel();
	    main.add(table);
	    main.add(hv);
	    initWidget(main);
	}
	
	public void addRow(String value){
		
		if (table.getCount()==0)		
			hlrem.setVisible(true);
		table.addRow(value);
	}
	public Collection getValues(){
		Collection col = new ArrayList();
		for (int i=0; i<table.getCount();i++){
			col.add(table.getText(i));
		}
		return col;
	}
	
	public int getCount(){
		return table.getCount();
	}
	
	public String getText(int row){
		if (type.equals("Boolean")){
			if (lang.equals("es"))
				if (table.getText(row).equals("Verdadero")){
					return "true";
				}else
					return "false";
			else
				if (table.getText(row).equals("True")){
					return "true";
				}else
					return "false";
		}
		return table.getText(row);
	}
}
