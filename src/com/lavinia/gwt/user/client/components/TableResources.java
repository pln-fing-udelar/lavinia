package com.lavinia.gwt.user.client.components;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;

public class TableResources extends Composite implements TableListener, ClickListener{

	private FlexTable table = new FlexTable();
	private int nextRow=1;
	private int selectedRow=-1;
	
	private String lang;

	public TableResources(String l){
		this.lang=l;
		table.setCellSpacing(0);
	    table.setCellPadding(5);	   
	    
	    table.addTableListener(this);
	    
	    if (lang.equals("es")){
		    table.setText(0, 0, "Recurso");
		    table.setText(0, 1, "Global");	    	    
		    table.setText(0, 2, "Interface");
		    table.setText(0, 3, "Implementación");
	    }else{
	    	table.setText(0, 0, "Resource");
		    table.setText(0, 1, "Global");	    	    
		    table.setText(0, 2, "Interface");
		    table.setText(0, 3, "Implementation");
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
	
	public void addRow(String name, boolean global, String interf, String impl,String desc){
		
		Label tname = new Label();
		tname.setText(name);
		tname.setTitle(desc);
		tname.setStyleName("lv-Label");
		table.setWidget(nextRow,0,tname);
		
		if (global){
			Label lg;
			if (lang.equals("es"))
				lg = new Label("SI");
			else
				lg = new Label("YES");
			lg.setTitle(desc);
			lg.setStyleName("lv-Label");
			table.setWidget(nextRow,1,lg);
		}else{
			Label lg = new Label("NO");			
			lg.setTitle(desc);
			lg.setStyleName("lv-Label");
			table.setWidget(nextRow,1,lg);
		}		
		
		Label tinterf = new Label();
		if ((interf==null)||(interf.equals("")))
				interf="N/A";
		tinterf.setText(interf);		
		tinterf.setTitle(desc);
		tinterf.setStyleName("lv-Label");
		table.setWidget(nextRow,2,tinterf);
		
		
		Label timpl = new Label();
		if ((impl==null)||(impl.equals("")))
				impl="N/A";
		timpl.setText(impl);
		timpl.setTitle(desc);
		timpl.setStyleName("lv-Label");
		table.setWidget(nextRow,3,timpl);
		
		
		nextRow++;
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
			return ((Label)table.getWidget(selectedRow, 0)).getText();			
		}
		return "";
	}
	
	public int getSelectedRow(){
		return selectedRow;
	}
	
	public boolean isGlobalSelected(){
		if (selectedRow>0){
			Label l = (Label)table.getWidget(selectedRow, 1);
			if (l.getText().equals("SI"))
				return true;
		}
		return false;		
	}

}

