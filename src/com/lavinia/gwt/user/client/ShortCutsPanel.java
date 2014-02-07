package com.lavinia.gwt.user.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ShortCutsPanel  extends Composite {

	private int links=0;
	private VerticalPanel vp2;
	private VerticalPanel temp;
	private String lang = "es";
	
	private static ShortCutsPanel instance = null;
	
	public static ShortCutsPanel getInstance() {
	      if (instance != null)
	        return instance;
	      return (instance = new ShortCutsPanel());
	}
	
	public void setLang(String l){
		lang = l;
	}
	
	private ShortCutsPanel(){
		
		vp2 = new VerticalPanel();
		vp2.setWidth("100%");		
		vp2.setSpacing(10);		
		initWidget(vp2);
		
	}

	public void clear(){
		vp2.clear();
		links=0;
		
	}

	public void addHyperLink(Hyperlink hl){
			removeHyperLink(hl.getTargetHistoryToken());
		
			if (links==0){
				VerticalPanel vp = new VerticalPanel();
				
				Label lbl; 
				if (lang.equals("es"))
					lbl = new Label("Opciones");
				else
					lbl = new Label("Options");
				lbl.setStyleName("lv-ShortCutTitle");
				vp.add(lbl);
				vp.setCellHorizontalAlignment(lbl,HasHorizontalAlignment.ALIGN_CENTER);
				vp.setStyleName("lv-ShortCuts");
				vp.setCellVerticalAlignment(lbl,HasVerticalAlignment.ALIGN_MIDDLE);			
				vp.setWidth("100%");		
				
				temp = new VerticalPanel();
							
				vp.add(temp);
				
				vp2.add(vp);				
			}
			links++;
			hl.setStyleName("lv-ShortCutHL");
			temp.add(hl);
		
	}
	
	public void removeHyperLink(String historyToken){
		if (temp!=null){
			for (int i=0; i<temp.getWidgetCount();i++){
				Hyperlink hl = (Hyperlink)temp.getWidget(i);
				if (hl.getTargetHistoryToken().equals(historyToken))
					temp.remove(hl);
			}
		}
	}

}
