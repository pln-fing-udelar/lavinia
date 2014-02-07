/*
 * Created on 18/10/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.utils;


import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.lavinia.gwt.user.client.analysis.TableMarkupOptions;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ColorsList extends Composite implements TableListener, ClickListener{

	private int rowselected=0;
	private int colselected=0;
//	private int cant=13;
	
	private TableMarkupOptions.Popup pop;
	private String[][] colors=new String[4][4];/* = {"rgb(153,204,155)",
			                    "rgb(255,255,255)",								
								"rgb(255,0,0)",
								"rgb(254,191,0)",
								"rgb(249,255,0)",
								"rgb(121,255,0)",								
								"rgb(0,242,255)",
								"rgb(210,0,255)",
								"rgb(155,155,155)",
								"rgb(0,0,255)",
								"rgb(217,140,142)",
								"rgb(0,153,51)",
								"rgb(0,0,0)"};*/
	
	public ColorsList(TableMarkupOptions.Popup p){		
		pop = p;
		
		colors[0][0]="rgb(255,120,120)"; colors[0][1]="rgb(254,201,122)"; colors[0][2]="rgb(249,255,121)"; colors[0][3]="rgb(188,254,122)"; 
		colors[1][0]="rgb(122,254,254)"; colors[1][1]="rgb(121,205,255)"; colors[1][2]="rgb(128,121,255)"; colors[1][3]="rgb(194,121,255)";
		colors[2][0]="rgb(255,121,255)"; colors[2][1]="rgb(255,121,179)"; colors[2][2]="rgb(255,0,0)"; colors[2][3]="rgb(56,251,0)";
		colors[3][0]="rgb(0,0,255)"; colors[3][1]="rgb(153,204,155)"; colors[3][2]="rgb(255,255,255)"; colors[3][3]="rgb(0,0,0)";
		
		
		
		FlexTable ft = new FlexTable();
		ft.addTableListener(this);
		for (int i=0;i<4;i++){
			for (int j=0;j<4;j++){
				ft.setWidget(i,j,
				new HTML(		
		        "<div style='padding:1; width: 25; border-style: solid; border-width: 1; height:5'>"+
		              "<span style='background-color: "+colors[i][j]+"'><font size='1'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
		              "</font>"+
		              "</span>"+
		        "</div>"));
			}
		}
		initWidget(ft);
	}
	
/*	public int getSelectedColor(){		
		return rowselected;
	}*/

	public void onCellClicked(SourcesTableEvents arg0, int arg1, int arg2) {
		rowselected = arg1;
		colselected = arg2;
		pop.hide();
		
	}
	
	public String getSelectedColor(){
		
			return colors[rowselected][colselected];
		
	}

	public void onClick(Widget arg0) {}
}
