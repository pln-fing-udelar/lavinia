/*
 * Created on 15/11/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.analysis;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PopupAnnotation extends DialogBox{
	
	public PopupAnnotation(FeaturesValuesTable dt, int left, int top){
		super(true);
		setPopupPosition(left,top);		
		setWidget(dt);
		setStyleName("lv-Popup");
		
    }
	 

}
