/*
 * Created on 06/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.datatypes;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DTAnnotation implements IsSerializable {
	private int begin;
	private int end;
	
	private DTTreeNode node;
	
	/**
	   * This field is a Set that must always contain DTTypeValue.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTFeature>
	   */
//	private Collection features;
	/**
	 * 
	 */
	
	public DTAnnotation() {
		begin=0;
		end=0;
	}
	public DTAnnotation(int begin, int end) {
		this.begin=begin;
		this.end=end;
		//features = new ArrayList();
		node = new DTTreeNode();
	}
	/**
	 * @return Returns the begin.
	 */
	public int getBegin() {
		return begin;
	}
	/**
	 * @param begin The begin to set.
	 */
	public void setBegin(int begin) {
		this.begin = begin;
	}
	/**
	 * @return Returns the end.
	 */
	public int getEnd() {
		return end;
	}
	/**
	 * @param end The end to set.
	 */
	public void setEnd(int end) {
		this.end = end;
	}
	
	/**
	 * @return Returns the type.
	 */
	public DTTreeNode getNode() {
		return node;
	}
	public void setNode(DTTreeNode node) {
		this.node = node;
	}
	
}
