/*
 * Created on 15/11/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.utils;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StatusPopup extends PopupPanel{
	private HorizontalPanel panel = new HorizontalPanel();
	private static StatusPopup instance=null;
	
	public static StatusPopup getInstance(){
		if (instance==null)
			instance=new StatusPopup();
		return instance;
	}
	
	public StatusPopup(){
		super(false);
		panel.setSpacing(10);
		add(panel);
		setStyleName("lv-StatusPopup");
    }
	
	public void setText(String text){	
		panel.clear();
		panel.add(new Label(text));		
		show();
	}	
	
	
	public void show() {		
		super.show();
		int left = (Window.getClientWidth() - this.getOffsetWidth()) / 2; 
		int top = (Window.getClientHeight() - this.getOffsetHeight()) / 2; 
		setPopupPosition(left,top);
	}
	

}

