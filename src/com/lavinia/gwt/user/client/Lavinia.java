package com.lavinia.gwt.user.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.analysis.Analyze;
import com.lavinia.gwt.user.client.components.ComponentsOptions;
import com.lavinia.gwt.user.client.configuration.Configuration;
import com.lavinia.gwt.user.client.interfaces.IUtils;
import com.lavinia.gwt.user.client.interfaces.IUtilsAsync;
import com.lavinia.gwt.user.client.utils.StatusPopup;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Lavinia implements EntryPoint,HistoryListener {

	private DockPanel dock;
	private VerticalPanel top;
	private HorizontalPanel down;
	private VerticalPanel left;
	private Composite center;
	private int LinkSelected;
	private Hyperlink link0;
	private Hyperlink link2;
	private Hyperlink link3;
	private Hyperlink link4;
	private Hyperlink hlesp;
	private Hyperlink hling;
	private Label lesp;
	private Label ling;
	private HorizontalPanel wesp;
	private HorizontalPanel wing;
	private String lang = "es";
	
	private StatusPopup statusPopup;
	
	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {

		IUtilsAsync ass = (IUtilsAsync)GWT.create(IUtils.class);
		ServiceDefTarget endpoint = (ServiceDefTarget)ass;			
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IUtils");
		ass.configure(new AsyncCallback() {
	        public void onFailure(Throwable caught) {	
	        	if (lang.equals("es"))
	        		Window.alert("ERROR al obtener rutas a archivos");
	        	else
	        		Window.alert("ERROR obtaining file paths");
	          }
	          public void onSuccess(Object result) {
	        	  if (((Boolean)result).booleanValue())
	        		  if (lang.equals("es"))
	        			  Window.alert("El servidor se ha actualizado");
	        		  else
	        			  Window.alert("The server has been updated");
	        	  load();
	          }	          
	    }); 
		
				
	}
	
	public void load(){		
		
		IUtilsAsync ass = (IUtilsAsync)GWT.create(IUtils.class);
		ServiceDefTarget endpoint = (ServiceDefTarget)ass;			
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()+"IUtils");
		
		ass.setLanguage(lang, new AsyncCallback(){
			public void onFailure(Throwable caught) {
				Window.alert("Error al cambiar idioma en servidor");
				
			}
			public void onSuccess(Object result) {
				
			}
		});
		
		RootPanel.get().clear();
		String msg ="";
		String version = "";
		if (lang.equals("es")){
			msg = "Ambiente Web para PLN";
			version = "Versión 1.5";
		}else{
			msg = "Web Environment for NLP";
			version = "Version 1.5";
		}
		HTML htmltitle = new HTML("<p align=\"left\"><font face=\"Book Antiqua\"><span style=\"font-size: 15pt\">"+
		"<img border=\"0\" src=\"transp.png\" width=\"161\" height=\"133\" align=\"left\"><b><font face=\"Book Antiqua\" style=\"font-size: 20pt\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
		"<br>"+
		"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
		"</font></b><font face=\"Book Antiqua\"><b><span style=\"font-size: 20pt\">"+
		"Lavinia</span><span style=\"font-size: 15pt\"> </span><font size=\"2\">"+
		version +"</font></b></font></p>"+
		"<p align=\"left\"><font face=\"Book Antiqua\">"+
		"<span style=\"font-weight: 700; font-size: 18pt\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+ 
		msg+"<br>"+
		"&nbsp;</span></font></p>");
		
		htmltitle.setWidth("900");
		htmltitle.setHeight("150");
		
		top = new VerticalPanel();	
		
		
		hlesp = new Hyperlink("Español","spanish");
		lesp = new Label ("Español");		
		wesp = new HorizontalPanel();
		
		
		
		hling = new Hyperlink("Inglés","english");
		ling = new Label ("Inglés");
		wing = new HorizontalPanel();
		
		if (lang.equals("es")){
			wesp.add(lesp);
			wing.add(hling);
		}else if (lang.equals("en")){
			wesp.add(hlesp);
			wing.add(ling);
		}
		
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		hp.add(wesp);
		hp.add(wing);
		hp.setWidth("130");
		
		top.add(hp);
		top.setCellHorizontalAlignment(hp,HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		top.add(htmltitle);
		top.setCellHorizontalAlignment(htmltitle,HasHorizontalAlignment.ALIGN_LEFT);
		
		
		hlesp.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				LinkSelected=0;
				wesp.clear();
				wesp.add(lesp);
				wing.clear();				
				wing.add(hling);
				lang = "es";
				ShortCutsPanel.getInstance().clear();
				ShortCutsPanel.getInstance().setLang("es");
				load();
			}
		});
		
		hling.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				LinkSelected=0;
				wesp.clear();
				wesp.add(hlesp);
				wing.clear();
				wing.add(ling);
				lang = "en";
				ShortCutsPanel.getInstance().clear();
				ShortCutsPanel.getInstance().setLang("en");
				load();
				
			}
		});
		
		down = new HorizontalPanel();		
		HTML foot = null;
		if (lang.equals("es"))
			foot = new HTML("<p><b>Por consultas o sugerencias enviar mail "+
				"a <a href=\"mailto:pgaptxt@fing.edu.uy?subject=Contacto desde Lavinia\">"+
				"[pgaptxt@fing.edu.uy]</a></b></p>");
		else
			foot = new HTML("<p><b> Comments or suggestions? E-mail <a href=\"mailto:pgaptxt@fing.edu.uy?subject=Contacto desde Lavinia\">"+
					"[pgaptxt@fing.edu.uy]</a></b></p>");
		down.add(foot);
		down.setSpacing(8);
		
		left = new VerticalPanel();
		
		if (lang.equals("es"))
			link0 = new Hyperlink("Bienvenida", "link0");
		else
			link0 = new Hyperlink("Welcome", "link0");
		Label l0 = new Label(" ");
		
		if (lang.equals("es"))
			link2 = new Hyperlink("Componentes", "link2");
		else
			link2 = new Hyperlink("Components", "link2");
		
	    Label l2 = new Label(" ");
	    
	    if (lang.equals("es"))
	    	link3 = new Hyperlink("Analizar Texto", "link3");
	    else
	    	link3 = new Hyperlink("Text Analysis", "link3");
	    
	    Label l3 = new Label(" ");
	    
	    if (lang.equals("es"))
	    	link4 = new Hyperlink("Configuración", "link4");	    
	    else
	    	link4 = new Hyperlink("Configuration", "link4");
	      
	    /*DESCOMENTAR EL CODIGO QUE SIGUE PARA LA VERSION INTERNET*/
	    /**********************************************************************/
	  /*	link4.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if (lang.equals("es"))
					Window.alert("Esta funcionalidad no esta disponible");
				else
					Window.alert("Not Available");
				
			}
		});*/
		/***********************************************************************/
		
		
	    left.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);	    
	    left.add(link0);
	    left.add(l0);
	    
	    left.add(link2);
	    left.add(l2);
	    
	    left.add(link3);
	    left.add(l3);
	    
	    left.add(link4);	   
	    
	    left.setCellWidth(link0,"100%");
	    left.setCellWidth(link2,"100%");
	    left.setCellWidth(link3,"100%");
	    left.setCellWidth(link4,"100%");	    
	   
	    left.setWidth("116");
	    
	    Welcome.getInstance().setLang(lang);
	    center = Welcome.getInstance();
    
	    dock = new DockPanel();
		dock.setHeight("100%");
		dock.setWidth("100%");
		
		dock.add(top, DockPanel.NORTH);
		dock.setCellHorizontalAlignment(top,HasHorizontalAlignment.ALIGN_CENTER);
		dock.setCellHeight(top,"4%");
		dock.setCellWidth(top,"700");
		
		dock.add(down,DockPanel.SOUTH);
		dock.setCellHorizontalAlignment(down,HasHorizontalAlignment.ALIGN_CENTER);
		dock.setCellHeight(down,"7%");
		
		dock.add(left,DockPanel.WEST);
		dock.setCellVerticalAlignment(left,DockPanel.ALIGN_TOP);		
		dock.setCellWidth(left,"12%");			
		
		dock.add(center,DockPanel.WEST);
		dock.setCellWidth(center,"72%");
		dock.setCellHorizontalAlignment(center,HasHorizontalAlignment.ALIGN_LEFT);
		
		ShortCutsPanel scp = ShortCutsPanel.getInstance();	
		dock.add(scp,DockPanel.EAST);		
		scp.setWidth("155");
				
		History.addHistoryListener(this);
		RootPanel.get().add(dock);	
		

		String historyToken = History.getToken();	        
		
		if ((historyToken.equals("link0"))||
				(historyToken.equals("link2"))||(historyToken.equals("link3"))||
				(historyToken.equals("link4"))){
			onHistoryChanged(historyToken);
		}else{
			LinkSelected=0;
			link0.removeStyleName("gwt-Hyperlink");
			link0.addStyleName("gwt-Hyperlink-Sel");
		}
		
		statusPopup = StatusPopup.getInstance();
		statusPopup.setPopupPosition(0,0);

	}	
	
	
	public void onHistoryChanged(String historyToken) {
		
	
		int temp;
		if (historyToken.equals("link2")){
			temp=2;
		}else if (historyToken.equals("link3")){
			temp=3;
		}else if (historyToken.equals("link4")){
			temp=4;
		}else
			temp=0;
		boolean load=false;
		/* COMENTAR LA LINEA QUE SIGUE PARA LA VERSION INTERNET */
		if (LinkSelected!=temp){
		
		/* DESCOMENTAR LA LINEA QUE SIGUE PARA LA VERSION INTERNET */ 
		//if ((LinkSelected!=temp)&&(temp!=4)){
			
			if (historyToken.equals("link0")){				//Welcome
				
				ShortCutsPanel.getInstance().clear();
				dock.remove(center);
		    	Welcome.getInstance().setLang(lang);
		    	
		    	center=Welcome.getInstance();
		    	dock.add(center,DockPanel.WEST);
				dock.setCellWidth(center,"72%");
				
				link0.removeStyleName("gwt-Hyperlink");
	    		link0.addStyleName("gwt-Hyperlink-Sel");
	    		temp=0;
	    		load=true;

		    }else if (historyToken.equals("link2")){		//Ver Componentes
		    	ShortCutsPanel.getInstance().clear();
		    	dock.remove(center);
		    	center= new ComponentsOptions(lang);
		    	dock.add(center,DockPanel.WEST);
				dock.setCellWidth(center,"72%");
				
		    	link2.removeStyleName("gwt-Hyperlink");
		    	link2.addStyleName("gwt-Hyperlink-Sel");
		    	temp=2;
		    	load=true;
			}else if (historyToken.equals("link3")){		//Analizar Texto
				ShortCutsPanel.getInstance().clear();
				dock.remove(center);
				center= new Analyze(lang);				
				dock.add(center,DockPanel.WEST);
				dock.setCellWidth(center,"72%");				
				
				link3.removeStyleName("gwt-Hyperlink");
		    	link3.addStyleName("gwt-Hyperlink-Sel");
		    	temp=3;
		    	load=true;
			}else if (historyToken.equals("link4")){		//Configuracion
				ShortCutsPanel.getInstance().clear();
				
				dock.remove(center);
				center= new Configuration(lang);	
				
				dock.add(center,DockPanel.WEST);
				dock.setCellWidth(center,"72%");				
				
				link4.removeStyleName("gwt-Hyperlink");
		    	link4.addStyleName("gwt-Hyperlink-Sel");
		    	temp=4;
		    	load=true;
				
			}
			
			if (load){
				if (LinkSelected==0){
					link0.removeStyleName("gwt-Hyperlink-Sel");
					link0.setStyleName("gwt-Hyperlink");			
				}else if (LinkSelected==2){
					link2.removeStyleName("gwt-Hyperlink-Sel");		
					link2.setStyleName("gwt-Hyperlink");
				}else if (LinkSelected==3){			
					link3.removeStyleName("gwt-Hyperlink-Sel");		
					link3.setStyleName("gwt-Hyperlink");
				}else if (LinkSelected==4){			
					link4.removeStyleName("gwt-Hyperlink-Sel");		
					link4.setStyleName("gwt-Hyperlink");
				}		
				LinkSelected=temp;
			}
		}
	  }
	}



