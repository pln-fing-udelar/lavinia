package com.lavinia.gwt.user.server;

import com.lavinia.gwt.user.client.interfaces.IComponent;
import com.lavinia.gwt.user.client.interfaces.IComponentsAnalysis;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IResources;
import com.lavinia.gwt.user.client.interfaces.ITypes;
import com.lavinia.gwt.user.client.interfaces.IUtils;

public class Factory {
	private static Factory instance = null;
	private IComponent iComponent;
	private IComponentsInfo iComponentsInfo;
	private IComponentsAnalysis iComponentsAnalysis;
	private IResources iResources;
	private ITypes iTypes;
	private IUtils iUtils;	
	
	private Factory(){
		iComponent = new IComponentImp();
		iComponentsInfo = new IComponentsInfoImp();
		iComponentsAnalysis = new IComponentsAnalysisImp();
		iResources = new IResourcesImp();
		iTypes = new ITypesImp();
		iUtils = new IUtilsImp();
	}
	
	public static Factory getInstance(){
		if (instance==null)
			instance = new Factory();
		return instance;
	}

	public IComponent getIComponent() {
		return iComponent;
	}
	
	public IComponentsAnalysis getIComponentsAnalysis() {
		return iComponentsAnalysis;
	}

	public IComponentsInfo getIComponentsInfo() {
		return iComponentsInfo;
	}

	public IResources getIResources() {
		return iResources;
	}
	
	public ITypes getITypes() {
		return iTypes;
	}

	public IUtils getIUtils() {
		return iUtils;
	}

}
