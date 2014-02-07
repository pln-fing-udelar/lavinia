package com.lavinia.gwt.user.client.analysis;

import com.google.gwt.user.client.ui.Composite;

public class UseFlow extends Composite{

	private boolean flowChange;
	
	public void setFlowChange(boolean f){
		flowChange = f;
	}
	
	public boolean isFlowChange(){
		return flowChange;
	}
}
