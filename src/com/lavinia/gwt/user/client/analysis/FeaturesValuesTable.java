package com.lavinia.gwt.user.client.analysis;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lavinia.gwt.user.client.datatypes.DTTreeNode;

public class FeaturesValuesTable extends Composite{
	
	public Tree tree;
	public DTTreeNode dt;
	
	public VerticalPanel vp = new VerticalPanel();
	
	public FeaturesValuesTable(ArrayList dts){
		
		Iterator it =dts.iterator();
		while (it.hasNext()){
			DTTreeNode dt = (DTTreeNode)it.next();
			addTree(dt);
		}
		initWidget(vp);
		
	}		
	
	
	private void addTree(DTTreeNode dt){
		Tree tree = new Tree();
		String caption = dt.getCaption();
		TreeItem r = new TreeItem(caption);
		tree.addItem(r);
		ArrayList nodes = dt.getNodes();
		
		Iterator it =nodes.iterator();
		while (it.hasNext()){
			DTTreeNode n = (DTTreeNode)it.next();
			TreeItem ti = new TreeItem(n.getCaption());			
			addNodes(ti,n.getNodes());
			r.addItem(ti);
		}
		vp.add(tree);
	}
	private void addNodes(TreeItem ti,ArrayList nodes){
		Iterator it = nodes.iterator();
		while (it.hasNext()){
			DTTreeNode n = (DTTreeNode)it.next();
			TreeItem tih = new TreeItem();
			tih.setText(n.getCaption());
			addNodes(tih,n.getNodes());
		}
	}
		
}
