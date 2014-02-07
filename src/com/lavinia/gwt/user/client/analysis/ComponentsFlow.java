/*
 * Created on 07/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.analysis;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.utils.StringsTable;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComponentsFlow extends Composite{
	
	VerticalPanel vp = new VerticalPanel();
	StringsTable table;
	private String lang;
	
	public ComponentsFlow(String l, boolean enabled){
		this.lang = l;	
		
		/*** BUTTONS ******************/
		VerticalPanel buttons = new VerticalPanel();
		Button bup;
		if (lang.equals("es"))
			bup = new Button ("Subir");
		else
			bup = new Button ("Up");
		bup.setEnabled(enabled);
		Button bdown;
		if (lang.equals("es"))
			bdown = new Button ("Bajar");
		else
			bdown = new Button ("Down");
		bdown.setEnabled(enabled);
		buttons.add(bup);
		buttons.add(bdown);
		buttons.setSpacing(8);
		
		/*** TABLE*********************/
		table = new StringsTable();
		table.setWidth("150");
		
		/**** TABLE AND BUTTONS *******/
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(table);
		hp.add(buttons);
		
		/***** MAIN PANEL*************/
		Label lbflow;
		if (lang.equals("es"))
		 lbflow = new Label ("Flujo");
		else
			lbflow = new Label ("Flow");
		vp.add(lbflow);		
		vp.add(hp);
		vp.setSpacing(4);
		initWidget(vp);
		
		/***** LISTENERS**************/
		bup.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {
			 	table.upSelectedRow();
			 	
			 }	
		});
		bdown.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {
			 	table.downSelectedRow();
			 	
			 }	
		});
		
	}
	
	public void addRow(String name){
		table.addRow(name);		
	}
	public void removeRow(){
		table.removeSelectedRow();
	}
	public String getText(int i){
		return table.getText(i);
	}
	public int getCount(){
		return table.getCount();
	}
	public void clear(){
		table.clear();
	}
	
}
