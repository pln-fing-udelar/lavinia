/*
 * Created on 26/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.server;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AppConfiguration {
	
	private String configurationPaths;		//ruta donde se guardan las rutas de configuracion
	private String componentsXMLPath;		//ruta donde se guardan los componentes
	private String typesXMLPath;			//ruta donde se guardan los tipos
	private String enginesXMLsPath;			//ruta donde se guardan los descriptores
	private String binPath;					//ruta donde se guardan los JARS
	private String resources;
	private String corpus;
	private String params;
	
	private String local;
	private String global;
	private String lang="es";
	
	
	private static String barra ="/";
	
	private static AppConfiguration instance = null;
	
	public static void setRealPath(String path){
		
		path.replace("\\", "/");
			instance = new AppConfiguration(path);
		
	}
	
	public static AppConfiguration getInstance(){		
		return instance;
	}

	private AppConfiguration(String path){		
		
		resources = path+"WEB-INF"+barra+"resources";
		configurationPaths = resources+barra+"LaviniaPaths.xml";
		componentsXMLPath = resources+barra+"LaviniaComponents.xml";
		typesXMLPath = resources+barra+"LaviniaTypes.xml";
		enginesXMLsPath = resources+barra+"engines";		

		binPath = path+barra+"WEB-INF"+barra+"lib";
		
		corpus = resources+barra+"corpus";
		params = resources+barra+"params";
		
		local = resources+barra+"local";
		global = resources+barra+"global";
	}
	
	public String getResourcesEngine(String name){
		return resources+barra+"engines"+barra+name;
	}
	
	//ruta al archivo con los nombres de los componentes existentes
	public String getComponentsXMLPath() {
		return componentsXMLPath;
	}
	
	//ruta al directorio donde estan los descriptores XML
	public String getEnginesXMLsPath() {
		return enginesXMLsPath;
	}
	//ruta al archivo con los nombres de los tipos
	public String getTypesXMLPath() {
		return typesXMLPath;
	}
	
	//ruta al directorio donde subir las implementaciones de componentes
	public String getBinPath() {
		return binPath;
	}
	//obtiene la ruta del elemento correspondiente en el XML
	public String getPath(String element){
		
		SAXBuilder builder = new SAXBuilder();		
		Document document;		
		try {
			document = builder.build(configurationPaths);
			Element root = document.getRootElement();
			if ((!root.getChildText(element).startsWith("/"))&&(!root.getChildText(element).startsWith("\\")))
					return ("/"+root.getChildText(element));
			return root.getChildText(element);
			
		}catch (Exception e) {
			return "ERROR";
		}
		
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getCorpus() {
		return corpus;
	}
	public String getParams() {
		return params;
	}
	public void setCorpus(String corpus) {
		this.corpus = corpus;
	}

	public String getConfigurationPaths() {
		return configurationPaths;
	}

	public void setConfigurationPaths(String configurationPaths) {
		this.configurationPaths = configurationPaths;
	}

	public String getGlobalResourcesPath() {
		return global;
	}


	public String getLocalResourcesPath() {
		return local;
	}
	
	public String getGlobal(String root){
		return (root+"WEB-INF"+barra+"resources"+barra+"global");
	}
	
	public String getLocal(String root){
		return (root+"WEB-INF"+barra+"resources"+barra+"local");
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
