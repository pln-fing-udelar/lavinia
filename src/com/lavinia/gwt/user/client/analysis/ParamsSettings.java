/*
 * Created on 07/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.datatypes.DTParamType;
import com.lavinia.gwt.user.client.datatypes.DTParameter;
import com.lavinia.gwt.user.client.utils.Auxiliar;
import com.lavinia.gwt.user.client.utils.StringsTableLinks;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ParamsSettings extends Composite{

	private VerticalPanel vp = new VerticalPanel();
	private Vector widgets;
	private Vector params;
	private String component;
	
	private String lang;
	private boolean files;
		
	public ParamsSettings(String l, int index, String name,Vector params,boolean values, boolean enabled,boolean files){
		this.params = params;
		this.lang = l;
		this.files=files;
		Iterator it = params.iterator();
		Iterator it2;
		DTParameter dt;		
		
		component = name;
		Label lb;
		if (lang.equals("es"))
			lb = new Label("Componente: "+name);
		else
			lb = new Label("Component: "+name);
		vp.add(lb);
		vp.setCellHorizontalAlignment(lb,HasHorizontalAlignment.ALIGN_CENTER);
		
		widgets = new Vector();
		while (it.hasNext()){
			dt = (DTParameter)it.next();
			Label lbname = new Label (dt.getName());
			lbname.setWidth("150");
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(lbname);
			hp.setSpacing(4);
			
			if (dt.getType().equals("File")){
				if (files){
					FileUpload fup = new FileUpload();
					fup.setName(component+"%"+dt.getName()+"%"+index);
					fup.setWidth("200");
					hp.add(fup);
					widgets.add(fup);
					vp.add(hp);
				}else{
					
					Label lbfile = new Label("File");
					if (lang.equals("es"))
						lbfile.setText("Archivo");
					else
						lbfile.setText("File");
					
					lbfile.setWidth("150");
					hp.add(lbfile);
					widgets.add(lbfile);
					vp.add(hp);
					
				}
			
			}else if (dt.getMultivalued().equals("false")){
				it2 = dt.getValues().iterator();
				if (it2.hasNext()){
					String value="";
					if (values){
						
						value = ((DTParamType)(dt.getRealvalues().iterator().next())).getValue();
					}
					ListBox lbvalues = new ListBox();
					lbvalues.setVisibleItemCount(1);
					lbvalues.setWidth("150");
					lbvalues.setEnabled(enabled);
					
					int k=0,n=0;
					while (it2.hasNext()){
						DTParamType dtp = (DTParamType)it2.next();
						lbvalues.addItem(dtp.getValue());
						if ((values) && (dtp.getValue().equals(value))){
							n=k;
						}
						k++;
					}
					if (values)
						lbvalues.setSelectedIndex(n);
					
					hp.add(lbvalues);
					widgets.add(lbvalues);
					vp.add(hp);
				}else{
					if (dt.getType().equals(("Boolean"))){
						 
						ListBox cbvalue = new ListBox();
						if (lang.equals("es")){
							cbvalue.addItem("Verdadero");
							cbvalue.addItem("Falso");
						}else{
							cbvalue.addItem("True");
							cbvalue.addItem("False");
						}
						cbvalue.setWidth("100");
						
						if (values){
							String value = (String)((DTParamType)dt.getRealvalues().iterator().next()).getValue();
							cbvalue.setSelectedIndex(0);
							if (value.equals("false"))
								cbvalue.setSelectedIndex(1);
						}
						
						hp.add(cbvalue);
						cbvalue.setEnabled(enabled);
						widgets.add(cbvalue);
						vp.add(hp);
					}else{
						TextBox tbvalue = new TextBox();
						
						if (values){
							String value = (String)((DTParamType)dt.getRealvalues().iterator().next()).getValue();
							tbvalue.setText(value);
						}
						tbvalue.setWidth("150");
						hp.add(tbvalue);
						tbvalue.setEnabled(enabled);
						widgets.add(tbvalue);
						vp.add(hp);
					}
				}
			}else if (dt.getMultivalued().equals("true")){
				StringsTableLinks st = new StringsTableLinks(lang,dt.getType(),enabled);
				if (values)
					it2 = dt.getRealvalues().iterator();
				else
					it2 = dt.getValues().iterator();
				while (it2.hasNext()){
					DTParamType dtp = (DTParamType)it2.next();
					if (dt.getType().equals("Boolean")){
						if (lang.equals("es"))
							if (dtp.getValue().equals("true")){
								st.addRow("Verdadero");
							}else{
								st.addRow("Falso");
							}
						else
							if (dtp.getValue().equals("true")){
								st.addRow("True");
							}else{
								st.addRow("False");
							}
					}else{
						st.addRow(dtp.getValue());
					}
				}
				hp.add(st);
				st.setEnabled(enabled);
				widgets.add(st);
				vp.add(hp);
			}
		}
		vp.setSpacing(8);
		vp.setStyleName("lv-Sink2");
		initWidget(vp);
	}
	
	public Vector getValues(){
		Vector values = new Vector();
		Iterator it = params.iterator();
		Iterator it2;
		DTParameter dt;
		int i=0;
		while(it.hasNext()){
			dt = (DTParameter)it.next();			
			Collection v = new ArrayList();
			
			if (dt.getType().equals("File") || (dt.getType().equals("Archivo"))){
				if (files){
					FileUpload fup = (FileUpload) widgets.get(i);
					v.add(new DTParamType(fup.getFilename()));
				}
				
			}else if (dt.getMultivalued().equals("false")){
				it2 = dt.getValues().iterator();
				DTParamType newValue;
				if (it2.hasNext()){
					ListBox lbvalues = (ListBox)widgets.get(i);
					String val =lbvalues.getItemText(lbvalues.getSelectedIndex());					
					newValue = new DTParamType(val);
					
				}else{
					if (dt.getType().equals(("Boolean"))){
						ListBox cbvalue = (ListBox)widgets.get(i);
						if (cbvalue.getItemText(cbvalue.getSelectedIndex()).equals("Verdadero"))
							newValue = new DTParamType("true");
						else
							newValue = new DTParamType("false");
						
					}else{
						TextBox tbvalue = (TextBox)widgets.get(i);
						if (dt.getType().equals("Integer")){
							if (!Auxiliar.isInteger(tbvalue.getText()))
								if (lang.equals("es"))
									Window.alert("Error en Parámetro "+dt.getName()+" del Componente "+tbvalue.getText());
								else
									Window.alert("Error in parametter "+dt.getName()+" of Component "+tbvalue.getText());
								
						}else if (dt.getType().equals("Float")){
							if (!Auxiliar.isFloat(tbvalue.getText()))
								if (lang.equals("es"))
									Window.alert("Error en Parámetro "+dt.getName()+" del Componente "+tbvalue.getText());
								else
									Window.alert("Error in parametter "+dt.getName()+" of Component "+tbvalue.getText());							
						}						
						newValue = new DTParamType(tbvalue.getText());
					}					
				}								
				v.add(newValue);
				
			}else if (dt.getMultivalued().equals("true")){
				StringsTableLinks st = (StringsTableLinks)widgets.get(i);
				Collection tableValues = st.getValues();
				it2 = tableValues.iterator();
				
				while (it2.hasNext()){
					String s = (String)it2.next();
					if (dt.getType().equals(("Boolean"))){
						if (lang.equals("es"))
							if (s.equals("Verdadero")){
								v.add(new DTParamType("true"));
							}else{
								v.add(new DTParamType("false"));
							}
						else
							if (s.equals("True")){
								v.add(new DTParamType("true"));
							}else{
								v.add(new DTParamType("false"));
							}
					}else{
						v.add(new DTParamType(s));
					}
					
				}
				
			}
			i++;
			DTParameter dt2 = new DTParameter(dt.getName(),dt.getType(),dt.getDescription(),dt.getMandatory(),dt.getMultivalued(),new ArrayList());
			dt2.setValues(v);
			values.add(dt2);
			
		}
		
		return values;
	}
	
	public DTParameter getValue(int j){
		Vector values = new Vector();
		
		Iterator it2;
		
		int i=0;
		
			DTParameter dt = (DTParameter)params.get(j);	
			Collection v = new ArrayList();
			
			if (dt.getType().equals("File") || (dt.getType().equals("Archivo"))){
				FileUpload fup = (FileUpload) widgets.get(i);
				v.add(new DTParamType(fup.getFilename()));
				
			}else if (dt.getMultivalued().equals("false")){
				it2 = dt.getValues().iterator();
				DTParamType newValue;
				if (it2.hasNext()){
					ListBox lbvalues = (ListBox)widgets.get(i);
					String val =lbvalues.getItemText(lbvalues.getSelectedIndex());					
					newValue = new DTParamType(val);
					
				}else{
					if (dt.getType().equals(("Boolean"))){
						ListBox cbvalue = (ListBox)widgets.get(i);
						if (cbvalue.getItemText(cbvalue.getSelectedIndex()).equals("Verdadero"))
							newValue = new DTParamType("true");
						else
							newValue = new DTParamType("false");
						
					}else{
						TextBox tbvalue = (TextBox)widgets.get(i);
						if (dt.getType().equals("Integer")){
							if (!Auxiliar.isInteger(tbvalue.getText()))
								if (lang.equals("es"))
									Window.alert("Error en Parámetro "+dt.getName()+" del Componente "+tbvalue.getText());
								else
									Window.alert("Error in parametter "+dt.getName()+" of Component "+tbvalue.getText());
								
						}else if (dt.getType().equals("Float")){
							if (!Auxiliar.isFloat(tbvalue.getText()))
								if (lang.equals("es"))
									Window.alert("Error en Parámetro "+dt.getName()+" del Componente "+tbvalue.getText());
								else
									Window.alert("Error in parametter "+dt.getName()+" of Component "+tbvalue.getText());							
						}						
						newValue = new DTParamType(tbvalue.getText());
					}					
				}								
				v.add(newValue);
				
			}else if (dt.getMultivalued().equals("true")){
				StringsTableLinks st = (StringsTableLinks)widgets.get(i);
				Collection tableValues = st.getValues();
				it2 = tableValues.iterator();
				
				while (it2.hasNext()){
					String s = (String)it2.next();
					if (dt.getType().equals(("Boolean"))){
						if (lang.equals("es"))
							if (s.equals("Verdadero")){
								v.add(new DTParamType("true"));
							}else{
								v.add(new DTParamType("false"));
							}
						else
							if (s.equals("True")){
								v.add(new DTParamType("true"));
							}else{
								v.add(new DTParamType("false"));
							}
					}else{
						v.add(new DTParamType(s));
					}
					
				}
				
			}
			i++;
			DTParameter dt2 = new DTParameter(dt.getName(),dt.getType(),dt.getDescription(),dt.getMandatory(),dt.getMultivalued(),new ArrayList());
			dt2.setValues(v);
			values.add(dt2);
			
		return dt2;
		
		
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}
	
	public String getType(int i){
		DTParameter dt = (DTParameter)params.get(i);
		return dt.getType();
	}
	public Widget getWidget(int i){
		return (Widget)widgets.get(i);
	}
	
	public int getCant(){
		
		return params.size();
	}
	

	
}
