/*
 * Created on 14/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Welcome extends Composite{

	private static Welcome instance;
	private String lang="es";
	private Label lbl;
	
	private VerticalPanel vp = new VerticalPanel();;
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
		vp.clear();
		if (lang.equals("es"))
			lbl = new Label("Bienvenido a la página web del proyecto Lavinia. El proyecto Lavinia consiste en el desarrollo de un ambiente web para el procesamiento de lenguaje natural. Esperamos que este ambiente sea de su utilidad.");
		else
			lbl = new Label("Welcome to the web page of the Lavinia project. The Lavinia Project consists in the development of a web environment for natural language processing. We hope that this environment helps you.");
		vp.add(lbl);
		vp.setStyleName("lv-Sink");
		vp.setCellVerticalAlignment(lbl,HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setSpacing(10);
	}

	private Welcome(){
		
				
		if (lang.equals("es"))
				lbl = new Label("Bienvenido a la página web del proyecto Lavinia. El proyecto Lavinia consiste en el desarrollo de un ambiente web para el procesamiento de lenguaje natural. Esperamos que este ambiente sea de su utilidad.");
		else
			lbl = new Label("Welcome to the web page of the Lavinia project. The Lavinia Project consists in the development of a web environment for natural language processing. We hope that this environment helps you.");
		vp.add(lbl);
		vp.setStyleName("lv-Sink");
		vp.setCellVerticalAlignment(lbl,HasVerticalAlignment.ALIGN_MIDDLE);
		vp.setSpacing(10);
		initWidget(vp);
		
	}
	
	public static Welcome getInstance() {
	      if (instance != null)
	        return instance;
	      return (instance = new Welcome());
	}
	
}
