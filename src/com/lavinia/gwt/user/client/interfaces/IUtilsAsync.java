package com.lavinia.gwt.user.client.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IUtilsAsync {
	public void configure(AsyncCallback callback);
	public void getNanoTime(AsyncCallback callback);	
	public void importTextFromURL(String url,AsyncCallback callback);
	public void setLanguage(String lang,AsyncCallback callback);
}
