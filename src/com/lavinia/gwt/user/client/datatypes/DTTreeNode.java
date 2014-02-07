package com.lavinia.gwt.user.client.datatypes;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;



public class DTTreeNode implements IsSerializable{
	
	private String caption;
	
	/**
	   * This field is a Set that must always contain DTTypeValue.
	   * 
	   * @gwt.typeArgs <com.lavinia.gwt.user.client.datatypes.DTFeature>
	   */
	private ArrayList nodes;

	public DTTreeNode(){
		super();
		caption="";
		nodes=new ArrayList();
	}
	
	public DTTreeNode(String string) {
		super();
		caption = string;
		nodes=new ArrayList();
	}


	public void add(DTTreeNode node) {
		nodes.add(node);
		
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public ArrayList getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList nodes) {
		this.nodes = nodes;
	}

}
