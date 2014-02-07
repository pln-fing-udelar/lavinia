package com.lavinia.gwt.user.client.components;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;

public class AggregateData  extends Composite{
	
	private TextBox tname;
	private TextArea tdescription;
	private TextBox tautor;
	private String lang;
	
	public AggregateData(String l, boolean adding, boolean enabled){
		
		this.lang = l;
		Label lbname, lbautor, lbdescription;
		
		if (lang.equals("es")){
			lbname = new Label("Nombre");
			lbname.setWidth("140");
			lbautor = new Label("Autor");
			lbautor.setWidth("140");
			lbdescription = new Label("Descripcion");
			lbdescription.setWidth("140");
		}else{
			lbname = new Label("Name");
			lbname.setWidth("140");
			lbautor = new Label("Autor");
			lbautor.setWidth("140");
			lbdescription = new Label("Description");
			lbdescription.setWidth("140");
		}
		
		tname = new TextBox();
		tname.setName("componentName");
		tname.setWidth("260");
		tname.setEnabled(enabled);
		if (!adding)
			tname.setEnabled(false);
		
		tautor = new TextBox();
		tautor.setWidth("260");
		tautor.setEnabled(enabled);
	
		tdescription = new TextArea();
		tdescription.setWidth("260");	    
		tdescription.setVisibleLines(4);
		tdescription.setEnabled(enabled);
		
		HorizontalPanel hpp1 = new HorizontalPanel();
		hpp1.add(lbname);
		hpp1.add(tname);
		
		HorizontalPanel hpp2 = new HorizontalPanel();
		hpp2.add(lbautor);
		hpp2.add(tautor);
		
		HorizontalPanel hpp3 = new HorizontalPanel();
		hpp3.add(lbdescription);
		hpp3.add(tdescription);
		
		VerticalPanel vpData = new VerticalPanel();
		vpData.setSpacing(8);
		vpData.add(hpp1);
		vpData.add(hpp2);
		vpData.add(hpp3);
		
		VerticalPanel vpData2 = new VerticalPanel();
		vpData2.add(vpData);
		
		vpData2.setSpacing(40);
		initWidget(vpData2);	
	}
	
	public DTAggregateComponent getData(){
		if (tname.getText().equals(""))
			if (lang.equals("es"))
				Window.alert("Ingrese un nombre");
			else
				Window.alert("Missing component name");
		else if (tname.getText().indexOf(" ")>=0)
			if (lang.equals("es"))
				Window.alert("Nombre de flujo inválido");
			else
				Window.alert("Invalid flow name");
		else
			return new DTAggregateComponent(tname.getText(),tautor.getText(),tdescription.getText());
		return null;
	}
	public void loadData(DTAggregateComponent dt){
		tname.setText(dt.getName());
		tautor.setText(dt.getAutor());
		tdescription.setText(dt.getDescription());
	}

}
