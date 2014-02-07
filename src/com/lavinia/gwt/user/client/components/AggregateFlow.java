package com.lavinia.gwt.user.client.components;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.analysis.ComponentsFlow;
import com.lavinia.gwt.user.client.analysis.UseFlow;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponentData;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfoAsync;

public class AggregateFlow  extends UseFlow{

	private ListBox list;
	private Label tbdesc;
	private FlexTable lbinpp;
	private FlexTable lboutt;
	private ComponentsFlow fl;
	private HorizontalPanel hpFirst = new HorizontalPanel();
	private IComponentsInfoAsync assinfo;
	private Vector data = new Vector();
	
	private String lang;

	public AggregateFlow(String l, boolean enabled){
		this.lang = l;
		assinfo = (IComponentsInfoAsync)GWT.create(IComponentsInfo.class);
		ServiceDefTarget endpointinfo = (ServiceDefTarget)assinfo;			
		endpointinfo.setServiceEntryPoint(GWT.getModuleBaseURL()+"IComponentsInfo");
		
		
		if (enabled){
			Label lbcomp, ldesc,linp,lout;
			if (lang.equals("es")){
				lbcomp = new Label ("Componente");
				ldesc = new Label ("Descripción");
				linp = new Label ("Entradas");
				lout = new Label ("Salidas");
			}else{
				lbcomp = new Label ("Component");
				ldesc = new Label ("Description");
				linp = new Label ("Inputs");
				lout = new Label ("Outputs");
			}
			
			lbcomp.setWidth("110");
			list = new ListBox();		
			list.setVisibleItemCount(1);
			list.setWidth("150");
			HorizontalPanel hp1 = new HorizontalPanel();
			hp1.add(lbcomp);
			hp1.add(list);
			
			
			ldesc.setWidth("110");
			tbdesc = new Label();		
			tbdesc.setWidth("260");
			tbdesc.setStyleName("lv-Label");
			VerticalPanel vp1 = new VerticalPanel();
			vp1.add(ldesc);
			vp1.add(tbdesc);
			
			
			linp.setWidth("110");
			
			lbinpp = new FlexTable();
			
			HorizontalPanel hp2 = new HorizontalPanel();
			hp2.add(linp);
			hp2.add(lbinpp);
			lbinpp.setSize("150", "100%");
			
			
			 
			lout.setWidth("110");
			
			lboutt = new FlexTable();
			
			HorizontalPanel hp3 = new HorizontalPanel();
			hp3.add(lout);
			hp3.add(lboutt);
			lboutt.setSize("150", "100%");
			
			
			
			VerticalPanel vpcomp = new VerticalPanel();
			
			vpcomp.add(hp1);
			vpcomp.add(vp1);
			vpcomp.add(hp2);
			vpcomp.add(hp3);
			vpcomp.setSpacing(10);
			vpcomp.setStyleName("lv-sink");
			/******************************************************/
			
			loadList();
			
			list.addChangeListener(new ChangeListener() {
				
				public void onChange(Widget arg0) {
					int index = list.getSelectedIndex();
					if (data.get(index)!=null){
						showData((DTComponentData)data.get(index));
					}else{
						loadData(index);
					}
				}
			});
			
		
			
			
			/***** BUTTONS << AND >> *********************/
			Button badd = new Button(" >> ");
			badd.setWidth("50");
			badd.setEnabled(enabled);
			Button brem = new Button(" << ");
			brem.setWidth("50");
			brem.setEnabled(enabled);
			VerticalPanel vpbuttons = new VerticalPanel();
			vpbuttons.add(badd);
			vpbuttons.add(brem);
			vpbuttons.setSpacing(8);
			vpbuttons.setWidth("100");
			vpbuttons.setCellHorizontalAlignment(badd,HasHorizontalAlignment.ALIGN_CENTER);
			vpbuttons.setCellHorizontalAlignment(brem,HasHorizontalAlignment.ALIGN_CENTER);
			
			/***********ACCIONS << AND >> **************************/
			
			badd.addClickListener(new ClickListener() {
				 public void onClick(Widget sender) {
				 	final String name = list.getItemText(list.getSelectedIndex());
				 	assinfo.isSimpleComponent(name, new AsyncCallback(){
				 		public void onFailure(Throwable caught) {				 			
				 			
				 		}
				 		public void onSuccess(Object result) {
				 			
				 			if (!((Boolean)result).booleanValue())
					 			assinfo.getFlow(name, new AsyncCallback(){
							 		public void onFailure(Throwable caught) {
							 			if (lang.equals("es"))
							 				Window.alert("Error al obtener datos");
							 			else
							 				Window.alert("Error obtaining data");
							 			
							 		}
							 		public void onSuccess(Object result) {
							 			Vector res = (Vector)result;
							 			Iterator it = res.iterator();
							 			while (it.hasNext()){
							 				fl.addRow((String)it.next());
							 			}		
							 			setFlowChange(true);
							 		}
							 	});
				 			else{
				 				fl.addRow(name);
				 				setFlowChange(true);
				 			}
				 				
				 			
				 		}
				 	});
				 	
				 				 	
				 	
				 }	
			});
			brem.addClickListener(new ClickListener() {
				 public void onClick(Widget sender) {
				 	fl.removeRow();
				 	setFlowChange(true);
				 	
				 }	
			});
			
			hpFirst.add(vpcomp);
			hpFirst.add(vpbuttons);
		}
		/*********************************************************/
		
		/****** COMPONENTS FLOW **********************/
		fl = new ComponentsFlow(lang,enabled);		
		fl.setStyleName("lv-sink");		
		HorizontalPanel hpt = new HorizontalPanel();
		hpt.add(fl);
		hpFirst.add(hpt);
		hpFirst.setHeight("100%");
		hpFirst.setSpacing(10);
		initWidget(hpFirst);
		
	}
	
	private void loadList(){		  
		
		
	    assinfo.getComponents(new AsyncCallback() {
	       

			public void onFailure(Throwable caught) {	        		        	
	        	Window.alert(caught.getMessage());
	          }

	          public void onSuccess(Object result) {
	          	Collection col = (Collection)result;
	          	Iterator it = col.iterator();
	          	while (it.hasNext()){
	          		list.addItem((String)it.next());
	          	}
	          	int size = list.getItemCount();
	          	data = new Vector();
	          	data.setSize(size);
	          	
	          	if (size>0)
	          		loadData(0);
	          }
	    });	    
	   
	}
	
	public Vector getFlow(){
		Vector f = new Vector();
		for (int i=0; i<fl.getCount();i++)
			f.add(fl.getText(i));
		
		return f;
	}
	
	private void loadData(int index){
		
		if (index>=0){
			String name = list.getItemText(index);
			
			
			assinfo.getComponentData(name,new AsyncCallback() {
			    public void onFailure(Throwable caught) {	        		        	
			    	Window.alert(caught.getMessage());
			      }
			      public void onSuccess(Object result) {
			      	DTComponentData dt = (DTComponentData)result;
			      	showData(dt);
			      	data.set(list.getSelectedIndex(),result);
			      }
			});
		}
	    
	}
	
	private void showData(DTComponentData dt){
		tbdesc.setText(dt.getDescription());	          	
      	String[] inps = dt.getInputs();
      	String[] outs = dt.getOutputs();
      	
      	if (lbinpp.getRowCount()>0)
     		lbinpp.removeStyleName("lv-Sink");
      	if (lboutt.getRowCount()>0)
      		lboutt.removeStyleName("lv-Sink");
      	lbinpp.clear();      	
      	lboutt.clear();
      	      	
      	for(int i=0;i<inps.length;i++){
      		HTML t = new HTML(inps[i]);
      		t.setStyleName("gwt-TextBox");
      		lbinpp.setWidget(i,0,t);
      	}
      	if (inps.length>0){
      		lbinpp.setStyleName("lv-Sink");
      		lbinpp.setCellSpacing(0);
      	}
      	
      	for(int i=0;i<outs.length;i++){
      		HTML t = new HTML(outs[i]);      		
      		t.setStyleName("gwt-TextBox");
      		lboutt.setWidget(i,0,t);
      	}
      	if (outs.length>0){
      		lboutt.setStyleName("lv-Sink");
      		lboutt.setCellSpacing(0);
      	}
      
      	     
	}
	
	public void loadData(DTAggregateComponent dt){
		Vector v = dt.getCompsParams();
		Iterator it =v.iterator();
		while (it.hasNext()){
			DTComponentParams dtp = (DTComponentParams) it.next();
			fl.addRow(dtp.getName());
		}
	}
	
	
}
