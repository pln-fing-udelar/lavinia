/*
 * Created on 06/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.analysis;

import java.util.HashMap;
import java.util.Vector;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.datatypes.DTAnnotation;
import com.lavinia.gwt.user.client.datatypes.DTAnnotationType;
import com.lavinia.gwt.user.client.datatypes.DTPositionStatus;

public class AnalyzedText extends Composite{

	private HashMap annotationst;
	
	private AbsolutePanel vp= new AbsolutePanel();	
	private TableMarkupOptions colors;
	private String spantext;
	private String text;
	
	private Vector statusCells;
	
	private FeaturesValuesTable tablefeatures;
	
	private HashMap statuslabel = new HashMap();
	
	private int inicio;

	public AnalyzedText(int inicio, String thetext,HashMap theannotations, TableMarkupOptions thecolors){
		
		this.inicio=inicio;
		text=thetext;	
		colors=thecolors;		
		annotationst=theannotations;	
			
		updateView();
		
		initWidget(vp);
	}
		
	
	public void updateView(){
		
		statusCells=new Vector();
		for (int i=0;i<text.length();i++){
			statusCells.add(new DTPositionStatus());			
		}
	
		for (int i=1;i<=colors.getCount();i++){
			if (colors.getShow(i))
				showAnnotation(i);
		}
		
			
		paint();
	}
	private void showAnnotation(int row){
		try{
			String type = colors.getType(row);
			DTAnnotationType dtt =(DTAnnotationType)annotationst.get(type);
			if (dtt!=null){
				Vector annotations = dtt.getAnnotations();				
				DTPositionStatus status;
				DTAnnotation dt;
				boolean obs=false;
				int k;
				
				for (int i=0;i<annotations.size();i++){
					dt = (DTAnnotation)annotations.get(i);
					
					obs=false;
					k=0;
					for (int j=dt.getBegin()-inicio;j<=dt.getEnd()-inicio-1;j++){
						
						if ((j>=0)&&(j<text.length())){
						
								status = (DTPositionStatus)statusCells.get(j);
								
								status.addTree(dt.getNode());
							
															
								if (!(status).isUsed() && !obs){
									status.setUsed(true);						
									status.setRow1(row);													
									
								}else if (!(status).isUsed() && obs){
									status.setUsed(true);						
									status.setRow1(row);
									status.setRow2(row);									
									status.setBorderTop(true);					
									if (j==(dt.getEnd()-inicio-1))
										status.setBorderRight(true);
									
								}else if ((status).isUsed()&&(!status.isThird())){
									if (j==dt.getBegin()-inicio){
		
										if (status.isBorderLeft())
											status.setThird(true);
										status.setBorderLeft(true);
										status.setRow2(row);
									
										
									}
									
									if (j==(dt.getEnd()-inicio-1)){
										
										if (status.isBorderRight())
											status.setThird(true);
										status.setBorderRight(true);
										status.setRow2(row);
									
										
									}
									
									if (!status.isBorderTop()){
										status.setBorderTop(true);
										status.setRow2(row);
									}
									if (!obs){
										obs=true;
										k=j;
									}
								}
								 
								String lab= type+": ("+(dt.getBegin())+", "+(dt.getEnd())+")";				
								if (status.getTitles().equals(""))
									status.setTitles(lab);
								else
									status.setTitles(status.getTitles()+"\n"+lab);				
								
								
							}
					}
					if (obs){
						for (int j=dt.getBegin()-inicio;j<k;j++){
							if ((j>=0)&&(j<text.length())){
								status = (DTPositionStatus)statusCells.get(j);
								if (j==dt.getBegin()-inicio){
									if (!status.isBorderLeft()){
										status.setBorderLeft(true);
										status.setRow2(row);
									}
								}
								
								if (!status.isBorderTop()){
									status.setBorderTop(true);
									status.setRow2(row);
								}								
							}
						}
					}					
				}	
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	private void paint(){
		
		Label label=null;		
		DTPositionStatus status, status_ant=null;
		
		int id_act = 0;
		spantext="";				
		Vector labels = new Vector();
				
		for (int i=0;i<text.length();i++){
			status = (DTPositionStatus)statusCells.get(i);
			
			if (i>0)
				status_ant = (DTPositionStatus)statusCells.get(i-1);			
			
			if (i==0){				
				spantext = spantext+"<span id=id"+id_act+"></span>";	
									
				label=new Label(text.substring(i,i+1));
				
				label.setTitle(status.getTitles());
				
			
			}else if (status.print().equals(status_ant.print())){				
				String h = text.substring(i,i+1);				
				String s = label.getText();
				
				if ((h.equals("\n"))&&(s.endsWith(" "))){
					label.setText(s+"\n");
					
				}else if (h.equals("\n")){
					label.setText(s+" ");
				}else
					label.setText(s+h);
			}else if (!status.print().equals(status_ant.print())){
				
				paintDOM(label,status_ant);			
				
				labels.add(label);				
				
				id_act++;
				spantext = spantext+"<span id=id"+id_act+"></span>";	
				
				label=new Label(text.substring(i,i+1));					
				label.setTitle(status.getTitles());
				
				
					
			}						
			statuslabel.put(label,status);
			if (i==text.length()-1){	
				paintDOM(label, status);
				
				labels.add(label);
				
			}
		}
		
		HTMLPanel htpanel = new HTMLPanel(spantext);	
		htpanel.setWidth("346");
					
		for (int i=0;i<labels.size();i++){			
			htpanel.add((Label)labels.get(i),"id"+i);	
			Label l = ((Label)labels.get(i));
			if (((DTPositionStatus)statuslabel.get(l)).isUsed()){
				
				l.addClickListener(new ClickListener(){
					public void onClick(Widget sender) {
						
						tablefeatures = new FeaturesValuesTable(((DTPositionStatus)statuslabel.get((Label)sender)).getTrees());
						PopupAnnotation pop = new PopupAnnotation(tablefeatures,sender.getAbsoluteLeft(),sender.getAbsoluteTop()+sender.getOffsetHeight());
						pop.show();					
					}				
				});
			}
		}
		DOM.setStyleAttribute(htpanel.getElement(),"wordWrap","break-word");
		
		AbsolutePanel vp2 = new AbsolutePanel();
		vp.clear();
		
		vp2.add(htpanel);
		vp2.setStyleName("lv-white");			
		vp2.setWidth("346");
		
		vp.add(vp2);
		vp.setWidth("346");

	}
	
	
	private void paintDOM(Label label, DTPositionStatus status){
		
		label.setStyleName("lv-Annotation");
		if (status.isUsed()){
			
			String font = colors.getForeground(status.getRow1());
			String back = colors.getBackground(status.getRow1());
						
			DOM.setStyleAttribute(label.getElement(),"backgroundColor",back); 
			DOM.setStyleAttribute(label.getElement(),"color",font);
			
			if (status.isBorderLeft()){
				back = colors.getBackground(status.getRow2());
				DOM.setStyleAttribute(label.getElement(),"borderLeft","solid "+back+" 2px");
			}
			if (status.isBorderRight()){
				back = colors.getBackground(status.getRow2());
				DOM.setStyleAttribute(label.getElement(),"borderRight","solid "+back+" 2px");
			}
			if (status.isBorderTop()){
				back = colors.getBackground(status.getRow2());
				DOM.setStyleAttribute(label.getElement(),"borderTop","solid "+back+" 2px");
				DOM.setStyleAttribute(label.getElement(),"borderBottom","solid "+back+" 2px");
			}							
		
			
		}
	}
}

