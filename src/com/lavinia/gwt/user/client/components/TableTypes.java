/*
 * Created on 16/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.components;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableTypes extends Composite implements TableListener, ClickListener{

	private FlexTable table = new FlexTable();
	private int selectedRow=-1;
	private TreeItem root;
	private Tree t;
	private int nextRow=1;
	
	private String lang;
	
	public TableTypes(String l){
		this.lang=l;
		table.setCellSpacing(0);
	    table.setCellPadding(5);	   
	    
	    table.addTableListener(this);
	    
	    if (lang.equals("es")){
		    table.setText(0, 0, "Tipo");
		    table.setText(0, 1, "Entrada");
		    table.setText(0, 2, "Salida");
	    }else{
	    	table.setText(0, 0, "Type");
		    table.setText(0, 1, "Input");
		    table.setText(0, 2, "Output");
	    }
	    
	    table.getRowFormatter().setStyleName(0, "lv-Table-Header");	    
	    
	    initWidget(table);
	    setStyleName("lv-Table");    
	    
	}
	
	public void addType(String name, String supertype, boolean inp, boolean out){
		root = new TreeItem(name+" : "+supertype);
		t = new Tree();
		t.addItem(root);
		table.setWidget(nextRow,0,t);
		
		CheckBox cinp = new CheckBox();
		cinp.setEnabled(false);
		CheckBox cout = new CheckBox();
		cout.setEnabled(false);
		if (inp){			
			cinp.setChecked(true);						
		}else{
			cinp.setChecked(false);
		}
		if (out){			
			cout.setChecked(true);						
		}else{
			cout.setChecked(false);
		}
		
		table.setWidget(nextRow,1,cinp);
		table.setWidget(nextRow,2,cout);
		table.getCellFormatter().setVerticalAlignment(nextRow,1,HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setHorizontalAlignment(nextRow,1,HasHorizontalAlignment.ALIGN_CENTER);
		table.getCellFormatter().setVerticalAlignment(nextRow,2,HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setHorizontalAlignment(nextRow,2,HasHorizontalAlignment.ALIGN_CENTER);
		
		nextRow++;
	}
	
	public void addFeature(String value){
		if (lang.equals("es"))
			root.addItem("Atributo: "+value);
		else
			root.addItem("Feature: "+value);
						
	}
	
	public void addValue(String value){
		if (lang.equals("es"))
			root.addItem("Valor Permitido: "+value);				
		else
			root.addItem("Allowed Value: "+value);
	}
	
	public void onCellClicked(SourcesTableEvents arg0, int row, int arg2) {
		if (row > 0)
			selectRow(row);
		
	}
	private void selectRow(int row){
		
		styleRow(selectedRow, false);
	    styleRow(row, true);
	    
	    selectedRow = row;
	    
	}
	
	private void styleRow(int row, boolean selected) {
		if (row != -1) {
			if (selected)
				table.getRowFormatter().addStyleName(row, "lv-Table-SelectedRow");
			else
				table.getRowFormatter().removeStyleName(row, "lv-Table-SelectedRow");
	    }
	}
	
	public boolean removeSelectedRow(){
		
		if (selectedRow>0){
			
			table.removeRow(selectedRow);
			selectedRow=-1;
		}
		nextRow--;
		if (table.getRowCount()==1)			
			return true;
		else
			return false;
		
	}
	public String getNameSelected(){
		if (selectedRow>0){
			Tree t = (Tree) table.getWidget(selectedRow,0);
			TreeItem root = t.getItem(0);
			String s = root.getText();
			int pos = s.indexOf(":");
			return (s.substring(0,pos-1));
		}
		return "";
	}
	
	public void onClick(Widget arg0) {
		// TODO Auto-generated method stub
		
	}

}
