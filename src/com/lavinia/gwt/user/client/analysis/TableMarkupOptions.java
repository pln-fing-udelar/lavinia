/*
 * Created on 18/10/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.analysis;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.utils.ColorsList;
/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableMarkupOptions extends Composite implements TableListener, ClickListener {

	private FlexTable table;	
	private int selectedRow=0;
	
	private HorizontalPanel hp;
	
	private String lang;
	
	public TableMarkupOptions(){
		
	}
	
	public void refreshTypes(ArrayList res){
		
		selectedRow=0;
		
		hp.clear();
		hp.add(createTable(res));		
	}
	
	
	public TableMarkupOptions(String l, ArrayList types){
		this.lang = l;
						
		/*********************************************************/
		hp = new HorizontalPanel();
		hp.add(createTable(types));
		
	    initWidget(hp);
	    
	}
	
	public HorizontalPanel createTable(ArrayList types){
		table = new FlexTable();
		table.setCellSpacing(0);
	    table.setCellPadding(5);	   
	    
	    table.addTableListener(this);
	    
	    final CheckBox cball = new CheckBox();
	    cball.setChecked(false);
	    cball.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				if (cball.isChecked())
					marcarTodas();
				else
					desmarcarTodas();				
			}	    	
	    });	    	  
	    
	    table.setText(0, 0, "");
	    table.setWidget(0, 0, cball);
	    if (lang.equals("es")){
		    table.setText(0, 1, "Tipo");    
		    table.setText(0, 2, "Fondo");
		    table.setText(0, 3, "Fuente");
	    }else{
	    	table.setText(0, 1, "Type");    
		    table.setText(0, 2, "Fill");
		    table.setText(0, 3, "Font");
	    }
	    	    	    
	    table.getRowFormatter().setStyleName(0, "lv-Table-Header");
	    	    
	    int row=1;
	   
	    Iterator it  = types.iterator();
	    while (it.hasNext()){
	    	
	    	CheckBox cb = new CheckBox();	    
			table.setWidget(row,0,cb);			
			
	    	String name = (String)it.next();	   
	    	table.setText(row,1,name);    	
	    	
			HTML cl1 = new HTML(		
			        "<div style='padding:1; width: 25; border-style: solid; border-width: 1; height:5'>"+
		              "<span style='background-color: rgb(255,120,120)'><font size='1'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
		              "</font>"+
		              "</span>"+
		        "</div>");
			cl1.setTitle("rgb(255,120,120)");
			
			table.setWidget(row,2,cl1);
			
			HTML cl2 = new HTML(		
			        "<div style='padding:1; width: 25; border-style: solid; border-width: 1; height:5'>"+
		              "<span style='background-color: rgb(0,0,0)'><font size='1'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
		              "</font>"+
		              "</span>"+
		        "</div>");
			cl2.setTitle("rgb(0,0,0)");
			table.setWidget(row,3,cl2);
			
			row++;
	    }
	    Button bup, bdown;
	    if (lang.equals("es")){
			bup = new Button("Subir");
			bdown = new Button("Bajar");
	    }else{
	    	bup = new Button("Up");
			bdown = new Button("Down");
	    }
		bup.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {
			 	upSelectedRow();			 	
			 }	
		});
		bdown.addClickListener(new ClickListener() {
			 public void onClick(Widget sender) {
			 	downSelectedRow();			 	
			 }	
		});
			
		VerticalPanel vpbuttons = new VerticalPanel();
		vpbuttons.add(bup);
		vpbuttons.add(bdown);
		vpbuttons.setSpacing(4);
	
		HorizontalPanel hp = new HorizontalPanel();		
		hp.add(table);
		hp.add(vpbuttons);
		hp.setSpacing(4);	
		table.setStyleName("lv-Table");
		
		return hp;
	}
	
	
	public void selectRow(int row){
		
		styleRow(selectedRow, false);
	    styleRow(row, true);
	    
	    selectedRow = row;
	    
	}
	
	private void styleRow(int row, boolean selected) {
		if (row >= 0) {
			if (selected)
				table.getRowFormatter().addStyleName(row, "lv-Table-SelectedRow");
			else{
				table.getRowFormatter().removeStyleName(row, "lv-Table-SelectedRow");				
			}
	    }
	}
	
	void upSelectedRow(){
		if (selectedRow>1){
			Widget w0 = table.getWidget(selectedRow,0);
			String w1 = table.getText(selectedRow,1);
			Widget w2 = table.getWidget(selectedRow,2);
			Widget w3 = table.getWidget(selectedRow,3);
			table.removeRow(selectedRow);
			table.insertRow(selectedRow-1);
			table.setWidget(selectedRow-1,0,w0);
			table.setText(selectedRow-1,1,w1);		
			table.setWidget(selectedRow-1,2,w2);
			table.setWidget(selectedRow-1,3,w3);
			
			selectRow(selectedRow-1);
		}
	}
	void downSelectedRow(){
		if ((selectedRow>0)&&(selectedRow<table.getRowCount()-1)){
			Widget w0 = table.getWidget(selectedRow,0);
			String w1 = table.getText(selectedRow,1);
			Widget w2 = table.getWidget(selectedRow,2);
			Widget w3 = table.getWidget(selectedRow,3);
			table.removeRow(selectedRow);
			table.insertRow(selectedRow+1);
			table.setWidget(selectedRow+1,0,w0);
			table.setText(selectedRow+1,1,w1);			
			table.setWidget(selectedRow+1,2,w2);
			table.setWidget(selectedRow+1,3,w3);
			
			selectRow(selectedRow+1);

		}
	}
	
	public void onCellClicked(SourcesTableEvents arg0, int arg1, int arg2) {
		if (arg1>0){
			if (arg2==2){			
				Popup p = new Popup(arg1,1);		
				int left = table.getWidget(arg1,arg2).getAbsoluteLeft();
				int top = table.getWidget(arg1,arg2).getAbsoluteTop();		    
			    p.setPopupPosition(left, top);
			 	p.show();			
			}else if (arg2==3){			
				Popup p = new Popup(arg1,2);		
				int left = table.getWidget(arg1,arg2).getAbsoluteLeft();
				int top = table.getWidget(arg1,arg2).getAbsoluteTop();		    
			    p.setPopupPosition(left, top);
			 	p.show();			
			}else{
				selectRow(arg1);
			}
		}
	
	}
	public int getCount(){
		return table.getRowCount()-1;
	}
	
	public void onClick(Widget arg0) {}
	
	public String getType(int row){
		return table.getText(row,1);
	}

	public String getForeground(int row){
		return ((HTML)table.getWidget(row,3)).getTitle();
	}
	public String getBackground(int row){
		return ((HTML)table.getWidget(row,2)).getTitle();
	}
	public boolean getShow(int row){
		return (((CheckBox)table.getWidget(row,0)).isChecked());			
	}
	
	public void marcarTodas(){
		for (int i=1;i<table.getRowCount();i++){
			CheckBox cb = (CheckBox)table.getWidget(i,0);
			cb.setChecked(true);
		}
	}
	
	public void desmarcarTodas(){
		for (int i=1;i<table.getRowCount();i++){
			CheckBox cb = (CheckBox)table.getWidget(i,0);
			cb.setChecked(false);
		}
	}
	
	public class Popup extends PopupPanel implements PopupListener{
		private ColorsList cl;
		private int row;
		private int type;
		
		public Popup(int row, int type){
			super(true);
			this.row=row;
			this.type=type;
			
			cl = new ColorsList(this);
			add(cl);
			setStyleName("lv-Popup");
			addPopupListener(this);
			
	    }
		
		public void onPopupClosed(PopupPanel arg0, boolean arg1) {		
		
			if (!arg1){

				String newColor = cl.getSelectedColor();			
				HTML ht = new HTML(		
				        "<div style='padding:1; width: 25; border-style: solid; border-width: 1; height:5'>"+
			              "<span style='background-color:"+newColor+"'><font size='1'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
			              "</font>"+
			              "</span>"+
			        "</div>");
				ht.setTitle(newColor);
				if (type==1){
					table.setWidget(row,2,ht);				
				}else if (type==2){
					table.setWidget(row,3,ht);				
				}
			}
		}

		public void hide(){
			super.hide();
		}

	
	}
	
}
