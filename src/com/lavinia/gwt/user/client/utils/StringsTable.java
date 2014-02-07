/*
 * Created on 17/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.utils;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StringsTable extends Composite implements TableListener, ClickListener{

	private FlexTable table = new FlexTable();
	private int nextRow=0;
	private int selectedRow=0;
	
	public StringsTable(){
		table.setStyleName("lv-Table");
		table.setCellSpacing(2);
		table.addTableListener(this);		
		initWidget(table);

	}
	
	public void clear(){
		for (int i = table.getRowCount()-1; i>=0;i--){
			table.removeRow(i);
		}
		table.clear();
		nextRow=0;
		selectedRow=0;
		
	}
	
	public void addRow(String value){
		
		table.setText(nextRow,0,value);		
		nextRow++;
	}
	
	public int getCount(){
		
		return table.getRowCount();
	}
	
	public String getText(int row){
		
		return table.getText(row,0);
	}
	
	
	public void onCellClicked(SourcesTableEvents arg0, int row, int col) {
		 selectRow(row);
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
	
	public void removeSelectedRow(){
		
		if (selectedRow>=0){
			
			table.removeRow(selectedRow);
			selectedRow=-1;
			
			nextRow--;
		}
		
	}
	
	public int getSelectedRow(){
		
		return selectedRow;
	}

	public void onClick(Widget arg0) {
		// TODO Auto-generated method stub
		
	}
	public void upSelectedRow(){
		if (selectedRow>0){
			String text = getText(selectedRow);			
			table.removeRow(selectedRow);
			table.insertRow(selectedRow-1);
			table.setText(selectedRow-1,0,text);
			selectRow(selectedRow-1);
		}
	}
	public void downSelectedRow(){
		if (selectedRow<table.getRowCount()-1){
			String text = getText(selectedRow);			
			table.removeRow(selectedRow);
			table.insertRow(selectedRow+1);
			table.setText(selectedRow+1,0,text);
			selectRow(selectedRow+1);
		}
	}
	
}
