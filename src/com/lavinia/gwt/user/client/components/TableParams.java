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
public class TableParams extends Composite implements TableListener, ClickListener{

	private FlexTable table = new FlexTable();
	private int nextRow=1;
	private int selectedRow=-1;
	private TreeItem root;
	private Tree t;

	private String lang;
	
	public TableParams(String l){
		this.lang = l;
		table.setCellSpacing(0);
	    table.setCellPadding(5);	   
	    
	    table.addTableListener(this);
	    if (lang.equals("es")){
		    table.setText(0, 0, "Parámetro");	    	    
		    table.setText(0, 1, "Requerido");
		    table.setText(0, 2, "Multivaluado");
	    }else{
	    	table.setText(0, 0, "Parametter");	    	    
		    table.setText(0, 1, "Mandatory");
		    table.setText(0, 2, "Multivalued");
	    }
	    
	    table.getRowFormatter().setStyleName(0, "lv-Table-Header");
	    
	    initWidget(table);
	    setStyleName("lv-Table");
	    	    
	}
	

	public void onCellClicked(SourcesTableEvents sender, int row, int cell) {

		if (row > 0)
			selectRow(row);
		
	}

	public void onClick(Widget arg0) {	}
	
	public void addRow(String name, String type, String req, String mult){
		
		root = new TreeItem(name+" : "+type);
		t = new Tree();
		t.addItem(root);
		table.setWidget(nextRow,0,t);
		
		
		CheckBox creq = new CheckBox();
		creq.setEnabled(false);
		CheckBox cmult = new CheckBox();
		cmult.setEnabled(false);
		if (req.equals("true")){			
			creq.setChecked(true);						
		}else{
			creq.setChecked(false);
		}
		if (mult.equals("true")){			
			cmult.setChecked(true);						
		}else{
			cmult.setChecked(false);
		}
		
		table.setWidget(nextRow,1,creq);
		table.setWidget(nextRow,2,cmult);
		table.getCellFormatter().setVerticalAlignment(nextRow,1,HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setHorizontalAlignment(nextRow,1,HasHorizontalAlignment.ALIGN_CENTER);
		table.getCellFormatter().setVerticalAlignment(nextRow,2,HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setHorizontalAlignment(nextRow,2,HasHorizontalAlignment.ALIGN_CENTER);
		
		nextRow++;
	}
	
	public void addValue(String value){
		if (lang.equals("es"))
			root.addItem("Valor: "+value);	
		else
			root.addItem("Value: "+value);
		
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
			nextRow--;
		}
		
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

}
