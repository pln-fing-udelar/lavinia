package com.lavinia.gwt.user.client.analysis;

import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Pages  extends Composite{

	private int LIMIT = 700;
	private VerticalPanel vp = new VerticalPanel();
	private Vector analyzed=new Vector();
	private Vector loaded=new Vector();
	private HorizontalPanel temp = new HorizontalPanel();
	private HashMap annotations;
	private TableMarkupOptions colors;
	private String text;
	
	private int actual=1;
	
	private int cant;
	private String lang;
	
	private Hyperlink anterior;
	
	private HorizontalPanel hplinks;
	
	public Pages(String l, String text,HashMap annotations, TableMarkupOptions colors){
		this.annotations=annotations;
		this.colors=colors;
		this.text=text;
		this.lang = l;
		
		
		
		cant = getCantPages(text);
		
		
		hplinks = new HorizontalPanel();
				
		hplinks.setWidth("346");
		
		Hyperlink hlant, hlsig;
		if (lang.equals("es")){
			hlant = new Hyperlink("Anterior","ant");
			hlsig = new Hyperlink("Siguiente","sig");
		}else{
			hlant = new Hyperlink("Previous","ant");
			hlsig = new Hyperlink("Next","sig");
		}
			
		hplinks.add(hlant);			
		hplinks.add(hlsig);
		
		hlant.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					if (actual>1){
						show(actual-1);
						((Hyperlink)(hplinks.getWidget(actual-1+2))).setStyleName("lv-Hyperlink-Sel");
						((Hyperlink)(hplinks.getWidget(actual+2))).setStyleName("lv-Hyperlink");
					}
				}
			});
		
		hlsig.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				if ((actual+1)<=cant){
					show(actual+1);
					((Hyperlink)(hplinks.getWidget(actual+1))).setStyleName("lv-Hyperlink-Sel");
					((Hyperlink)(hplinks.getWidget(actual))).setStyleName("lv-Hyperlink");
				}
			}
		});
		hplinks.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		for (int i=1;i<=cant;i++){
			Hyperlink hl = new Hyperlink(i+"",i+"");
			hl.setStyleName("lv-Hyperlink");
			if (i==1){
				anterior=hl;
				anterior.setStyleName("lv-Hyperlink-Sel");
			}
			hl.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					String token = ((Hyperlink)sender).getTargetHistoryToken();
					if (anterior!=null)
						anterior.setStyleName("lv-Hyperlink");
					anterior = (Hyperlink)sender;
					anterior.setStyleName("lv-Hyperlink-Sel");
					show((new Integer(token)).intValue());
				}
			});
			hplinks.add(hl);
			loaded.add(new Boolean(false));
			analyzed.add(null);
		}
		
		
		String subtext = getSubText(1,text);
		AnalyzedText at = new AnalyzedText(getFirstIndex(1),subtext,annotations,colors);
		temp.add(at);
		vp.add(temp);
		if (cant>1)
			vp.add(hplinks);
		loaded.set(0,new Boolean(true));
		analyzed.set(0,at);
		initWidget(vp);
	}
	
	private int getCantPages(String text){
		double cant = ((double)text.length()/(double)LIMIT);		
		return ((new Double((Math.ceil(cant)))).intValue());				
	}
	
	private String getSubText(int ind, String text){
		if (ind==1)
			if (LIMIT<text.length())
				return text.substring(0, LIMIT);
			else
				return text.substring(0, text.length());
		
		if (ind*LIMIT<text.length())
			return text.substring((ind-1)*LIMIT,ind*LIMIT);
		else
			return text.substring((ind-1)*LIMIT,text.length());
		
	}
	private int getFirstIndex(int ind){
		if (ind==1)
			return 0;
		else 
			return ((ind-1)*LIMIT);
	}
	
	private void show(int i){
		if (actual!=i){
			if (((Boolean)loaded.get(i-1)).booleanValue()){
				AnalyzedText at = (AnalyzedText)analyzed.get(i-1);
				temp.clear();
				temp.add(at);
			}else{
				String subtext = getSubText(i,text);
				AnalyzedText at = new AnalyzedText(getFirstIndex(i),subtext,annotations,colors);
				temp.clear();
				temp.add(at);
				loaded.set(i-1,new Boolean(true));
				analyzed.set(i-1,at);
			}
			actual=i;
		}
	}
	public void updateView(){
		String subtext = getSubText(actual,text);
		AnalyzedText at = new AnalyzedText(getFirstIndex(actual),subtext,annotations,colors);
		temp.clear();
		temp.add(at);
		
		for (int i=1;i<=cant;i++){			
			loaded.set(i-1,new Boolean(false));			
		}
		
		loaded.set(actual-1,new Boolean(true));
		analyzed.set(actual-1,at);
	}
}
