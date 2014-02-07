/*
 * Created on 25/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.client.utils;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Auxiliar {

	public static boolean isInteger(String s){
		try{
			int value = Integer.parseInt(s); 
			if (!s.equals(String.valueOf(value))) throw new Exception();
			return true;
		}catch(Exception e){
			return false;
		}		
	}
	
	public static boolean isFloat(String s){
		try{
			float value = Float.parseFloat(s); 
			if (!s.equals(String.valueOf(value))) throw new Exception();
			return true;
		}catch(Exception e){
			return false;
		}		
	}
	
}
