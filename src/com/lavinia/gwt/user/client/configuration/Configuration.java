package com.lavinia.gwt.user.client.configuration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;
import com.lavinia.gwt.user.client.interfaces.IComponent;
import com.lavinia.gwt.user.client.interfaces.IComponentAsync;
import com.lavinia.gwt.user.client.interfaces.IResources;
import com.lavinia.gwt.user.client.interfaces.IResourcesAsync;

public class Configuration extends Composite implements TableListener, ClickListener{

	private VerticalPanel main;
	private FlexTable table;
	private VerticalPanel resourceDataPanel;
	
	private IResourcesAsync ass;
	private ServiceDefTarget endpoint;
	
	private IComponentAsync ass2;
	private ServiceDefTarget endpoint2;
	
	private int nextRow = 1;
	private int selectedRow=-1;
	
	private boolean showing=false;
	
	
	private HashSet globals = new HashSet();
	private String lang;
	
	public Configuration(String l){
		this.lang = l;
		
		/*******************************************************************/
		ass = (IResourcesAsync)GWT.create(IResources.class);
		endpoint = (ServiceDefTarget)ass;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IResources");
		
		
		ass2 = (IComponentAsync)GWT.create(IComponent.class);
		endpoint2 = (ServiceDefTarget)ass2;		
		endpoint2.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponent");
		
		main=new VerticalPanel();
		main.setStyleName("lv-Sink");
		initWidget(main);
		main.setSize("100%", "100%");
		main.setSpacing(8);

		String msg1 = "";
		if (lang.equals("es"))
			msg1 = "Configuración de Recursos";
		else
			msg1 = "Resources Configuration";
		final Label configuracionDeRecursosLabel = new Label(msg1);
		main.add(configuracionDeRecursosLabel);
		configuracionDeRecursosLabel.setHeight("20");
		main.setCellHorizontalAlignment(configuracionDeRecursosLabel, HasHorizontalAlignment.ALIGN_CENTER);

		final VerticalPanel verticalPanel = new VerticalPanel();
		main.add(verticalPanel);
		verticalPanel.setSpacing(10);
		verticalPanel.setSize("100%", "100%");
		main.setCellVerticalAlignment(verticalPanel, HasVerticalAlignment.ALIGN_TOP);
		main.setCellHeight(verticalPanel, "100%");
		main.setCellWidth(verticalPanel, "100%");

		final HorizontalPanel tableLinks = new HorizontalPanel();
		verticalPanel.add(tableLinks);
		verticalPanel.setCellHorizontalAlignment(tableLinks, HasHorizontalAlignment.ALIGN_CENTER);

		table = new FlexTable();
		
		tableLinks.add(table);
		table.setStyleName("lv-Table");
		table.setCellSpacing(2);
		table.addTableListener(this);		
		table.setWidth("300");
		if (lang.equals("es"))
				table.setText(0, 0, "Recurso");
		else
				table.setText(0, 0, "Resource");
		if (lang.equals("es"))
			table.setText(0, 1, "Ruta");
		else
			table.setText(0, 1, "Path");
		
		table.getRowFormatter().setStyleName(0, "lv-Table-Header");

		final VerticalPanel links = new VerticalPanel();
		tableLinks.add(links);
		links.setSpacing(8);

		if (lang.equals("es"))
			msg1 = "Nuevo";
		else
			msg1 = "New";
		final Hyperlink verHyperlink = new Hyperlink(msg1, "newResource");
		links.add(verHyperlink);
		verHyperlink.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if (!showing){					
					resourceDataPanel.add(resourcePanel());
					showing=true;
				}
				
			}
		});

		if (lang.equals("es"))
			msg1 = "Modificar";
		else
			msg1 = "Update";
		final Hyperlink modificarHyperlink = new Hyperlink(msg1, "updateResource");
		links.add(modificarHyperlink);
		modificarHyperlink.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {				
				if ((!showing)&&(selectedRow>0)){
					String name = ((Label)table.getWidget(selectedRow, 0)).getText();
					if (globals.contains(name)){
						if (lang.equals("es"))
							Window.alert("Los recursos globales no se pueden modificar");
						else
							Window.alert("Global resources can not be updated");
					}else{
						resourceDataPanel.add(resourcePanelUpdate());
						showing=true;
					}
				}
				
			}
		});
		if (lang.equals("es"))
			msg1 = "Eliminar";
		else
			msg1 = "Delete";
		final Hyperlink eliminarHyperlink = new Hyperlink(msg1, "remResource");
		links.add(eliminarHyperlink);
		eliminarHyperlink.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if (selectedRow>0){
					String msg1="";
					if (lang.equals("es"))
						msg1 = "Seguro desea elminar el recurso seleccionado?";
					else
						msg1 = "Delete selected resource?";
					
					if (Window.confirm(msg1)){
						removeResource();
					}
				}
			}
		});
		
		
		resourceDataPanel = new VerticalPanel();
		verticalPanel.add(resourceDataPanel);
		verticalPanel.setCellVerticalAlignment(resourceDataPanel, HasVerticalAlignment.ALIGN_TOP);
		resourceDataPanel.setWidth("100%");
		
		loadData();
	}
	
	public void loadData(){
		ass.getExtResources(new AsyncCallback(){
			public void onFailure(Throwable caught) {				
				Window.alert(caught.getMessage());
				
			}
			public void onSuccess(Object result) {
				Vector recs = (Vector)result;
				Iterator it=recs.iterator();
				while (it.hasNext()){
					DTResourceData dtr = (DTResourceData)it.next();
					Label name = new Label(dtr.getName());
					name.setTitle(dtr.getDescription());
					name.setStyleName("lv-Label");
					
					Label url = new Label(dtr.getURL());
					url.setTitle(dtr.getDescription());
					url.setStyleName("lv-Label");
					
					table.setWidget(nextRow, 0,name);
					table.setWidget(nextRow, 1,url);
					nextRow++;
					
					
					if (dtr.isExterno().equals("false")){
						globals.add(dtr.getName());
					}
				}
			};
		});
	}

	

	public void onClick(Widget sender) {
		// TODO Auto-generated method stub
		
	}
	
	public VerticalPanel resourcePanel(){
		
		Label lbname;
		if (lang.equals("es"))
			lbname = new Label("Nombre");
		else
			lbname = new Label("Name");
		lbname.setWidth("120");
		Label lbdescription;
		if (lang.equals("es"))
			lbdescription = new Label("Descripción");
		else
			lbdescription = new Label("Description");
		lbdescription.setWidth("120");
		
		Label lburl;
		if (lang.equals("es"))
			lburl = new Label("Ruta");
		else
			lburl = new Label("Path");
		lburl.setWidth("120");
		
		final TextBox tname = new TextBox();
		tname.setWidth("210");		
		
		final TextArea tdescription = new TextArea();
		tdescription.setWidth("210");	    
		tdescription.setVisibleLines(4);
		
		final FileUpload fup = new FileUpload();
		fup.setWidth("210");
		
		String msg1 = "";
		if (lang.equals("es"))
			msg1 = "Aceptar";
		else
			msg1 = "OK";
		Button baccept = new Button(msg1);
		baccept.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {		
				if ((tname.getText().length()>0)&&(tname.getText().indexOf(" ")<0)){
					if (tdescription.getText().length()>0){
						if (fup.getFilename().length()>0){
							DTResourceData dtr = new DTResourceData(tname.getText(),tdescription.getText(),fup.getFilename(),"true");
							addResource(dtr);
						}else
							if (lang.equals("es"))
								Window.alert("Debe ingresar una ruta");
							else
								Window.alert("You must type a path");
					}else
						if (lang.equals("es"))
							Window.alert("Debe ingresar una descripción");
						else
							Window.alert("You must type a description");
				}else
					if (lang.equals("es"))
						Window.alert("Ingrese un nombre de recurso válido");
					else
						Window.alert("Invalid resource name");
			}
		});
		if (lang.equals("es"))
			msg1 = "Cancelar";
		else
			msg1 = "Cancel";
		
		Button bcancel = new Button(msg1);
		bcancel.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {				
				resourceDataPanel.clear();
				showing=false;
			}
		});
		
		HorizontalPanel hp1=new HorizontalPanel();
		hp1.add(lbname);
		hp1.add(tname);
		
		HorizontalPanel hp2=new HorizontalPanel();
		hp2.add(lbdescription);
		hp2.add(tdescription);
		
		HorizontalPanel hp3=new HorizontalPanel();
		hp3.add(lburl);
		hp3.add(fup);
		
		HorizontalPanel hp4 = new HorizontalPanel();
		hp4.add(baccept);
		hp4.add(bcancel);
		hp4.setSpacing(4);
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(hp1);
		vp.add(hp2);
		vp.add(hp3);
		vp.add(hp4);		
		vp.setSpacing(4);
		vp.setStyleName("lv-Sink");
		vp.setCellHorizontalAlignment(hp4, HasHorizontalAlignment.ALIGN_CENTER);
		return vp;	
	}
	
	public VerticalPanel resourcePanelUpdate(){
	
		if (selectedRow>0){
		
			String name = ((Label)table.getWidget(selectedRow, 0)).getText();
			String desc = ((Label)table.getWidget(selectedRow, 0)).getTitle();
		
			Label lbname;
			if (lang.equals("es"))
				lbname = new Label("Nombre");
			else
				lbname = new Label("Name");
			lbname.setWidth("120");
			
			Label lbdescription;
			if (lang.equals("es"))
				lbdescription = new Label("Descripción");
			else
				lbdescription = new Label("Description");
			lbdescription.setWidth("120");
			
			Label lburl;
			if (lang.equals("es"))
				lburl = new Label("Ruta");
			else
				lburl = new Label("Path");
			lburl.setWidth("120");
			
			final TextBox tname = new TextBox();
			tname.setWidth("210");
			tname.setText(name);
			tname.setEnabled(false);
			
			final TextArea tdescription = new TextArea();
			tdescription.setWidth("210");	    
			tdescription.setVisibleLines(4);
			tdescription.setText(desc);
			
			final FileUpload fup = new FileUpload();
			fup.setWidth("210");
			
			
			
			
			String msg1 = "";
			if (lang.equals("es"))
				msg1 = "Aceptar";
			else
				msg1 = "OK";
			Button baccept = new Button(msg1);
			baccept.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {	
					if (tdescription.getText().length()>0){
						if (fup.getFilename().length()>0){
							DTResourceData dtr = new DTResourceData(tname.getText(),tdescription.getText(),fup.getFilename(),"true");
							updateResource(dtr);
						}else
							if (lang.equals("es"))
								Window.alert("Debe ingresar una ruta");
							else
								Window.alert("You must type a path");
					}else
						if (lang.equals("es"))
							Window.alert("Debe ingresar una descripción");
						else
							Window.alert("You must type a description");
					
				}
			});
			
			if (lang.equals("es"))
				msg1 = "Cancelar";
			else
				msg1 = "Cancel";
			
			Button bcancel = new Button(msg1);
			bcancel.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {				
					resourceDataPanel.clear();
					showing=false;
				}
			});
			
			HorizontalPanel hp1=new HorizontalPanel();
			hp1.add(lbname);
			hp1.add(tname);
			
			HorizontalPanel hp2=new HorizontalPanel();
			hp2.add(lbdescription);
			hp2.add(tdescription);
			
			HorizontalPanel hp3=new HorizontalPanel();
			hp3.add(lburl);
			hp3.add(fup);
			
			HorizontalPanel hp4 = new HorizontalPanel();
			hp4.add(baccept);
			hp4.add(bcancel);
			hp4.setSpacing(4);
			
			VerticalPanel vp = new VerticalPanel();
			vp.add(hp1);
			vp.add(hp2);
			vp.add(hp3);
			vp.add(hp4);		
			vp.setSpacing(4);
			vp.setStyleName("lv-Sink");
			vp.setCellHorizontalAlignment(hp4, HasHorizontalAlignment.ALIGN_CENTER);
			return vp;	
		}else
			return null;
	}
	
	public void addResource(final DTResourceData dtr){
		
		
		ass.addResource(dtr, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}
			public void onSuccess(Object result) {
				Label name = new Label(dtr.getName());
				name.setTitle(dtr.getDescription());
				name.setStyleName("lv-Label");
				
				Label url = new Label(dtr.getURL());
				url.setTitle(dtr.getDescription());
				url.setStyleName("lv-Label");
				
				table.setWidget(nextRow, 0,name);
				table.setWidget(nextRow, 1,url);
				
				nextRow++;
				
				resourceDataPanel.clear();
				showing=false;
			}
		});
	}
	
	public void updateResource(final DTResourceData dtr){		
		
		
		ass.updateResource(dtr, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}
			public void onSuccess(Object result) {
				
				updateReferences(dtr);
			}
		});
	}
	
	public void updateReferences(final DTResourceData dtr){
		
		ass2.updateReferences(dtr, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}
			public void onSuccess(Object result) {
				Label name = new Label(dtr.getName());
				name.setTitle(dtr.getDescription());
				name.setStyleName("lv-Label");
				
				Label url = new Label(dtr.getURL());
				url.setTitle(dtr.getDescription());
				url.setStyleName("lv-Label");
				
				table.setWidget(selectedRow, 0,name);
				table.setWidget(selectedRow, 1,url);
				
				resourceDataPanel.clear();
				showing=false;
				if (lang.equals("es"))
					Window.alert("El recurso ha sido actualizado");
				else
					Window.alert("Resource successfully updated");
			}
		});
	}
	
	public void removeResource(){
		
		final String name = ((Label)table.getWidget(selectedRow, 0)).getText();		
			
		ass.removeResource(name, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				
			}
			public void onSuccess(Object result) {
				table.removeRow(selectedRow);
				nextRow--;
				selectedRow=-1;
				if (globals.contains(name)){
					globals.remove(name);
				}
				if (lang.equals("es"))
					Window.alert("El recurso ha sido eliminado");
				else
					Window.alert("Resource deleted");
			};
		});
	}
	
	public void onCellClicked(SourcesTableEvents arg0, int row, int col) {
		if (row>0)
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
}
