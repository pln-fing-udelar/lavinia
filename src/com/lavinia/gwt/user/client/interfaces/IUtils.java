package com.lavinia.gwt.user.client.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IUtils extends RemoteService{
	Boolean configure() throws Exception;
	String getNanoTime();	
	String importTextFromURL(String url)throws Exception;
	void setLanguage(String lang);
}
