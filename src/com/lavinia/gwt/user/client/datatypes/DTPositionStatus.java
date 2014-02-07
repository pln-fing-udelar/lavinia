/*
 * Created on 25/11/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.datatypes;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;



/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DTPositionStatus implements IsSerializable{

	private boolean borderRight;
	private boolean borderLeft;
	private boolean borderTop;	
	private boolean used;
	private int row1;
	private int row2;
	private String titles;
	private boolean third;

	
	/**
	   * This field is a Set that must always contain DTTypeValue.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTTreeNode>
	   */
	private ArrayList trees;
	
	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public DTPositionStatus(){
		borderRight=false;
		borderLeft=false;
		borderTop=false;	
		used=false;			
		row1=0;
		row2=0;
		titles="";
		trees=new ArrayList();
	}
	
	
	/**
	 * @return Returns the borderLeft.
	 */
	public boolean isBorderLeft() {
		return borderLeft;
	}
	/**
	 * @param borderLeft The borderLeft to set.
	 */
	public void setBorderLeft(boolean borderLeft) {
		this.borderLeft = borderLeft;
	}
	/**
	 * @return Returns the borderRight.
	 */
	public boolean isBorderRight() {
		return borderRight;
	}
	/**
	 * @param borderRight The borderRight to set.
	 */
	public void setBorderRight(boolean borderRight) {
		this.borderRight = borderRight;
	}
	/**
	 * @return Returns the borderTop.
	 */
	public boolean isBorderTop() {
		return borderTop;
	}
	/**
	 * @param borderTop The borderTop to set.
	 */
	public void setBorderTop(boolean borderTop) {
		this.borderTop = borderTop;
	}
	/**
	 * @return Returns the used.
	 */
	public boolean isUsed() {
		return used;
	}
	/**
	 * @param used The used to set.
	 */
	public void setUsed(boolean used) {
		this.used = used;
	}
	/**
	 * @return Returns the row.
	 */
	public int getRow1() {
		return row1;
	}
	/**
	 * @param row The row to set.
	 */
	public void setRow1(int row) {
		this.row1 = row;
	}
	/**
	 * @return Returns the row.
	 */
	public int getRow2() {
		return row2;
	}
	/**
	 * @param row The row to set.
	 */
	public void setRow2(int row) {
		this.row2 = row;
	}
	
	public boolean equals(DTPositionStatus dt){
		if ((dt.isUsed()) && (!used))			
			return false;
		if (dt.isBorderLeft() && !borderLeft)
			return false;
		if (dt.isBorderRight() && !borderRight)
			return false;
		if (dt.isBorderTop() && !borderTop)
			return false;	
		return true;
	}
	public String print(){
		String s="";
		if (!used)
			s=s+"no usado ";
		if (!borderLeft)
			s=s+"sin borde izq ";
		if (!borderRight)
			s=s+"sin borde der ";
		if (!borderTop)
			s=s+"sin borde top ";	
		s=s+"row1="+row1;
		s=s+"row2="+row2;
		s=s+titles;
			
		return s;
	}


	public void addTree(DTTreeNode dt) {
		trees.add(dt);
	}

	public ArrayList getTrees() {
		return trees;
	}

	public boolean isThird() {
		return third;
	}

	public void setThird(boolean third) {
		this.third = third;
	}

	
}
