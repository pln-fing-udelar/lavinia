/*
 * Created on 08/08/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.jboss.util.file.Files;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponent;
import com.lavinia.gwt.user.client.datatypes.DTFeature;
import com.lavinia.gwt.user.client.datatypes.DTFeatureFS;
import com.lavinia.gwt.user.client.datatypes.DTParamType;
import com.lavinia.gwt.user.client.datatypes.DTParameter;
import com.lavinia.gwt.user.client.datatypes.DTResource;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;
import com.lavinia.gwt.user.client.datatypes.DTType;
import com.lavinia.gwt.user.client.datatypes.DTTypeF;
import com.lavinia.gwt.user.client.datatypes.DTTypeString;
import com.lavinia.gwt.user.client.datatypes.DTTypeValue;
import com.lavinia.gwt.user.client.exceptions.ExistsComponent;
import com.lavinia.gwt.user.client.exceptions.FileNotFoundException;
import com.lavinia.gwt.user.client.exceptions.ProcessingException;
import com.lavinia.gwt.user.client.exceptions.XMLErrorException;
import com.lavinia.gwt.user.client.interfaces.IComponent;
import com.lavinia.gwt.user.client.interfaces.IResources;


public class IComponentImp extends RemoteServiceServlet implements IComponent {

	private Namespace NS = Namespace
			.getNamespace("http://uima.apache.org/resourceSpecifier");
			
	private String barra = "/";

	static Logger log = Logger.getLogger(IComponentImp.class.getName());
	
	/* Agrega una AE simple al sistema */
	public void addSimpleComponent(DTComponent dt) throws Exception,
			FileNotFoundException, ExistsComponent {

		if (existsComponent(dt.getName())) {
			String msg = "El componente "+dt.getName()+" ya existe";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = dt.getName() + "already exists";
			}
			log.error(msg);
			throw new ExistsComponent(msg);
			
		}else{			
						
			// crea el archivo xml engines/component_name/component_name.xml
			createXML(dt);

			// agrega el nombre del componente a la lista global
			try{
				addComponent(dt.getName());
			}catch(Exception e){
				//si el componente no se pudo agregar a la lista global
				//elimina el descriptor que se habia creado
				removeXML(dt.getName());
			}
		}

	}
	
	public void addAggregateComponent(DTAggregateComponent dt) throws Exception,
	FileNotFoundException, ExistsComponent,ProcessingException {

		if (existsComponent(dt.getName())) {

			String msg = "El componente "+dt.getName()+" ya existe";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = dt.getName() + "already exists";
			}
			log.error(msg);
			throw new ExistsComponent(msg);
		
		}else{ 
			
			if (dt.getCompsParams().size()>0)
				createAggXML(dt);
			else{
				String msg = "Error: flujo vacio";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error: Empty Flow";
				}
				log.error(msg);
				throw new ProcessingException(msg);
			}			

			// agrega el nombre del componente a la lista global
			try{
				addAggToList(dt);
			}catch(Exception e){
				//si el componente no se pudo agregar a la lista global
				//elimina el descriptor que se habia creado
				removeXML(dt.getName());
			}
	
		}
		
	}

	
	/* Retorna true si y solo si el componente con el nombre "name" ya existe 
	 * en el sistema.
	 */
	public boolean existsComponent(String name) throws FileNotFoundException {
		SAXBuilder builder = new SAXBuilder();

		AppConfiguration apconf = AppConfiguration.getInstance();

		Document document;
		try {
			document = builder.build(apconf.getComponentsXMLPath());
		} catch (JDOMException e) {
			String msg = "Error en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in LaviniaComponents.xml";
			}
			log.error(msg);
			throw new FileNotFoundException(msg);
		} catch (IOException e) {
			String msg = "Error de I/O en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error in LaviniaComponents.xml";
			}
			log.error(msg);
			throw new FileNotFoundException(msg);
		}
		Element root = document.getRootElement();
		List comps = root.getChildren("component");
		for (int i = 0; i < comps.size(); i++) {
			Element comp = (Element) comps.get(i);
			if (comp.getText().equals(name)) {
				return true;
			}
		}
		return false;
	}

	private void addAggToList(DTAggregateComponent dt) throws FileNotFoundException{
		SAXBuilder builder = new SAXBuilder();
		AppConfiguration apconf = AppConfiguration.getInstance();
		
		Document document = null;
		try {
			document = builder.build(apconf.getComponentsXMLPath());
		} catch (JDOMException e) {		
			String msg = "Error en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in LaviniaComponents.xml";
			}
			log.error(msg);
		} catch (IOException e) {	
			String msg = "Error de I/O en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error in LaviniaComponents.xml";
			}
			log.error(msg);
		}
		Element root = document.getRootElement();
		Element comp = new Element("component").setText(dt.getName());
		
		comp.setAttribute("simple","false");
		
		root.addContent(comp);
		
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());	
		try {
			File file = new File(apconf.getComponentsXMLPath());
		
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter FileWriter = new OutputStreamWriter(fos,"UTF-8");
			outputter.output(document,FileWriter);
			
					
			fos.flush();
			fos.close();
		} catch (IOException e) {
			String msg = "Error de I/O en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error in LaviniaComponents.xml";
			}
			log.error(msg);
			throw new FileNotFoundException(msg);		
		}
	}
	
	/* Agrega el nombre de componente "name" a la lista global de componentes
	 * en el archivo laviniaComponents.xml
	 */
	private void addComponent(String name) throws FileNotFoundException {
		SAXBuilder builder = new SAXBuilder();
		AppConfiguration apconf = AppConfiguration.getInstance();
		
		Document document = null;
		try {
			document = builder.build(apconf.getComponentsXMLPath());
		} catch (JDOMException e) {		
			String msg = "Error en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in LaviniaComponents.xml";
			}
			log.error(msg);
			
		} catch (IOException e) {	
			String msg = "Error de I/O en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error in LaviniaComponents.xml";
			}
			log.error(msg);
			
		}
		Element root = document.getRootElement();
		Element comp = new Element("component").setText(name);
		
		comp.setAttribute("simple","true");
		
		root.addContent(comp);
			
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());	
		try {
			File file = new File(apconf.getComponentsXMLPath());
		
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter FileWriter = new OutputStreamWriter(fos,"UTF-8");
			outputter.output(document,FileWriter);
			
					
			fos.flush();
			fos.close();
		} catch (IOException e) {
			String msg = "No se encuentra XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "File not found: LaviniaComponents.xml";
			}
			log.error(msg);
			throw new FileNotFoundException(msg);
		}
	}
	
	/* Elimina el directorio que contiene el descriptor XML del componente "name"
	 * y los archivos que contiene
	 */
	private void removeXML(String name){
		File dir = new File(AppConfiguration.getInstance().getEnginesXMLsPath() 
				+ barra + name);
		if (dir!=null){
			if (!Files.delete(dir)){
				
				String msg = "No se pudo eliminar directorio con descriptores XML de componente "+name;
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Failed to delele folder with descriptors of "+name;
				}
				log.warn(msg);
			}
		}else{
			String msg = "El directorio con descriptores de "+name+" no existe";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Folder with descriptors of "+name+" does not exists";
			}
			log.warn(msg);
		}
	}

	
	/* Crea el descriptor en XML de un componente y si se tienen valores por defecto
	 * para algun parametro tambien se crea otro archivo XML con estos datos
	 */
	
	private void createAggXML(DTAggregateComponent dt)  throws Exception  {
			
		AppConfiguration apconf = AppConfiguration.getInstance();
		
		AnalysisEngineDescription ae = ((IComponentsAnalysisImp)(Factory.getInstance().getIComponentsAnalysis())).createAE(dt.getCompsParams());

		
		ae.getAnalysisEngineMetaData().setName(dt.getName());
		ae.getAnalysisEngineMetaData().setVendor(dt.getAutor());
		ae.getAnalysisEngineMetaData().setDescription(dt.getDescription());
		
		
		File file = new File(apconf.getEnginesXMLsPath() + barra + dt.getName());
		file.mkdir();
		
		
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(apconf
					.getEnginesXMLsPath()
					+ barra + dt.getName() + barra + dt.getName() + ".xml");
		} catch (java.io.FileNotFoundException e) {
			String msg = "No se encuentra archivo xml con AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "File not found: XML with AnalysisEngine";
			}
			log.error(msg);
			throw new FileNotFoundException(msg);
		
		}
		
		try {
			ae.toXML(fop);
		} catch (SAXException e) {
			String msg = "Error al obtener XML de AE creada";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error obtaining XML descriptor from AnalysisEngine";
			}
			log.error(msg);			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error de I/O al obtener XML de AE creada";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O Error obtaining XML descriptor from AnalysisEngine";
			}
			log.error(msg);			
			throw new XMLErrorException(msg);
		}
		
		try {
			fop.flush();
			fop.close();
		} catch (IOException e) {
			String msg = "Error al cerrar archivo XML con AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error closing XML descriptor of AnalysisEngine";
			}
			log.warn(msg);			
			
		}
		
	}
	private void createXML(DTComponent dt) throws FileNotFoundException  {
		AppConfiguration apconf = AppConfiguration.getInstance();

		String BASE = "com.engines.";

		Element root = new Element("analysisEngineDescription", NS);
		// *************************************************************
		Element item1 = new Element("frameworkImplementation", NS)
				.setText("org.apache.uima.java");
		root.addContent(item1);
		// *************************************************************
		Element item2 = new Element("primitive", NS).setText("true");
		root.addContent(item2);
		// *************************************************************
		Element item3;
		if (dt.getPack().lastIndexOf(".") == dt.getPack().length() - 1)
			item3 = new Element("annotatorImplementationName", NS).setText(dt
					.getPack()
					+ dt.getName());
		else
			item3 = new Element("annotatorImplementationName", NS).setText(dt
					.getPack()
					+ "." + dt.getName());

		root.addContent(item3);
		// *************************************************************
		Element item4 = new Element("analysisEngineMetaData", NS);
		Element item41 = new Element("name", NS).setText(BASE + dt.getName());
		item4.addContent(item41);
		Element item42 = new Element("description", NS).setText(dt
				.getDescription());
		item4.addContent(item42);
		Element item43 = new Element("version", NS).setText("1.0");
		item4.addContent(item43);
		Element item44 = new Element("vendor", NS).setText(dt.getAutor());
		item4.addContent(item44);
		Element item45 = new Element("configurationParameters", NS);
		item4.addContent(item45);
		// *************************************************************
		Collection<IsSerializable> params = dt.getParameters();
		Iterator<IsSerializable> it = params.iterator();
		DTParameter dtp;
		Vector paramFiles = new Vector();
		while (it.hasNext()) {
			dtp = (DTParameter) it.next();
			if (dtp.getType().equals("Archivo")||dtp.getType().equals("File")){
				paramFiles.add(dtp);
			}else{
				Element item451 = new Element("configurationParameter", NS);
				Element item4511 = new Element("name", NS).setText(dtp.getName());
				item451.addContent(item4511);
	
				Element item4512 = new Element("description", NS).setText(dtp
						.getDescription());
				item451.addContent(item4512);
	
				Element item4513 = new Element("type", NS).setText(dtp.getType());
				item451.addContent(item4513);
	
				Element item4514 = new Element("multiValued", NS).setText(dtp
						.getMultivalued());
	
				item451.addContent(item4514);
	
				Element item4515 = new Element("mandatory", NS).setText(dtp
						.getMandatory());
				item451.addContent(item4515);
				item45.addContent(item451);
			}
		}
		it = params.iterator();
		Element item46 = new Element("configurationParameterSettings", NS);
		Collection<Object> values;
		Iterator<Object> it2;
		Element item46121 = null;
		Element item461211 = null;
		Collection<DTParameter> paramsLavinia = new ArrayList<DTParameter>();
		while (it.hasNext()) {
			dtp = (DTParameter) it.next();
			if (!dtp.getType().equals("File") && !dtp.getType().equals("Archivo")){
				values = dtp.getValues();
				if (values.size() > 0) {
					if (dtp.getMultivalued().equals("true")) {
						Element item461 = new Element("nameValuePair", NS);
						Element item4611 = new Element("name", NS).setText(dtp
								.getName());
						item461.addContent(item4611);
						Element item4612 = new Element("value", NS);
						it2 = values.iterator();
						if (dtp.getType().equals("String")) {
							if (values.size() == 1) {
								item46121 = new Element("string", NS)
										.setText(((DTParamType) it2.next())
												.getValue());
							} else {
								item46121 = new Element("array", NS);
								while (it2.hasNext()) {
									item461211 = new Element("string", NS)
											.setText(((DTParamType) it2.next())
													.getValue());
									item46121.addContent(item461211);
								}
							}
						} else if (dtp.getType().equals("Integer")) {
							if (values.size() == 1) {
								item46121 = new Element("integer", NS)
										.setText((((DTParamType) it2.next())
												.getValue()).toString());
							} else {
								item46121 = new Element("array", NS);
								while (it2.hasNext()) {
									item461211 = new Element("integer", NS)
											.setText((((DTParamType) it2.next())
													.getValue()).toString());
									item46121.addContent(item461211);
								}
							}
						} else if (dtp.getType().equals("Float")) {
							if (values.size() == 1) {
								item46121 = new Element("float", NS)
										.setText((((DTParamType) it2.next())
												.getValue()).toString());
							} else {
								item46121 = new Element("array", NS);
								while (it2.hasNext()) {
									item461211 = new Element("float", NS)
											.setText((((DTParamType) it2.next())
													.getValue()).toString());
									item46121.addContent(item461211);
								}
							}
						} else if (dtp.getType().equals("Boolean")) {
							if (values.size() == 1) {
								item46121 = new Element("boolean", NS)
										.setText((((DTParamType) it2.next())
												.getValue()).toString());
							} else {
								item46121 = new Element("array", NS);
								while (it2.hasNext()) {
									item461211 = new Element("boolean", NS)
											.setText((((DTParamType) it2.next())
													.getValue()).toString());
									item46121.addContent(item461211);
								}
							}
						} else
							item46121 = new Element("ERROR", NS).setText(dtp
									.getType());
						item4612.addContent(item46121);
						item461.addContent(item4612);
						item46.addContent(item461);
					} else {
						paramsLavinia.add(dtp);
					}
	
				}
			}
		
		}
		item4.addContent(item46);

		// *************************************************************
		Element item47 = new Element("typeSystemDescription", NS);
		Element item471 = new Element("name", NS).setText(dt.getName()
				+ "." + "TypeSystem");
		item47.addContent(item471);
		Element item472 = new Element("description", NS).setText("TypeSystem");
		item47.addContent(item472);
		Element item473 = new Element("version", NS).setText("1.0");
		item47.addContent(item473);
		Element item474 = new Element("vendor", NS).setText(dt.getAutor());
		item47.addContent(item474);
		Element item475 = new Element("imports", NS);
		item47.addContent(item475);

		Element item401 = new Element("capabilities", NS);// requerido
		Element item4011 = new Element("capability", NS);
		// Entradas
		Element item40111 = new Element("inputs", NS);
		item4011.addContent(item40111);
		// Salidas
		Element item40112 = new Element("outputs", NS);

		Element item476 = new Element("types", NS);
		Collection<IsSerializable> types = dt.getTypes();
		it = types.iterator();
		DTType dtt;
		Element item47614 = null;
		while (it.hasNext()) {
			dtt = (DTType) it.next();
			Element item4761 = new Element("typeDescription", NS);
			Element item47611 = null;
						
			item47611 = new Element("name", NS).setText(dtt.getName());			
			
			item4761.addContent(item47611);
			Element item47612 = new Element("description", NS).setText(dtt
					.getDescription());
			item4761.addContent(item47612);
			
			Element item47613 = new Element("supertypeName", NS).setText(dtt
					.getSupertype());
			item4761.addContent(item47613);

			if (dtt.getSupertype().equals("String")) {
				item47614 = new Element("allowedValues", NS);
				Collection<Object> typeVal = ((DTTypeString) dtt).getValues();
				it2 = typeVal.iterator();
				while (it2.hasNext()) {
					DTTypeValue dv = (DTTypeValue) it2.next();
					Element item476141 = new Element("value", NS);
					Element item4761411 = new Element("string", NS).setText(dv
							.getValue());
					item476141.addContent(item4761411);
					Element item4761412 = new Element("description", NS)
							.setText(dv.getDescription());
					item476141.addContent(item4761412);
					item47614.addContent(item476141);
				}
			} else {
				item47614 = new Element("features", NS);
				Collection<Object> fts = ((DTTypeF) dtt).getFeatures();
				it2 = fts.iterator();
				while (it2.hasNext()) {
					DTFeature df = (DTFeature) it2.next();
					Element item476141 = new Element("featureDescription", NS);
					Element item4761411 = new Element("name", NS).setText(df
							.getName());
					item476141.addContent(item4761411);
					Element item4761412 = new Element("description", NS)
							.setText(df.getDescription());
					item476141.addContent(item4761412);

					// range type name
					String rangeName = df.getType();
					
					Element item4761413 = new Element("rangeTypeName", NS)
							.setText(rangeName);
					item476141.addContent(item4761413);
					if (df.hasElementType()) {
						//element type
						String elementName = ((DTFeatureFS) df)
								.getElementType();
						
						Element item4761414 = new Element("elementType", NS)
								.setText(elementName);
						item476141.addContent(item4761414);
					}
					Element item4761415 = new Element(
							"multipleReferencesAllowed", NS).setText("true");
					item476141.addContent(item4761415);

					item47614.addContent(item476141);
				}
			}
			if (dtt.isInput()) {
				Element item401111 = null;
			
				item401111 = new Element("type", NS).setText(dtt.getName());
				
				item401111.setAttribute("allAnnotatorFeatures", "true");
				item40111.addContent(item401111);

			}
			if (dtt.isOutput()) {
				Element item401121 = null;
				
				item401121 = new Element("type", NS).setText(dtt.getName());
				
				item401121.setAttribute("allAnnotatorFeatures", "true");
				item40112.addContent(item401121);

			}

			item4761.addContent(item47614);
			item476.addContent(item4761);
		
		}
		item47.addContent(item476);
		item4.addContent(item47);
		
		item4011.addContent(item40112);
		// ******
		Element item40113 = new Element("languagesSupported", NS);
		item4011.addContent(item40113);
		Element item40114 = new Element("inputSofas", NS);
		item4011.addContent(item40114);
		Element item40115 = new Element("outputSofas", NS);
		item4011.addContent(item40115); // OPCIONALES

		item401.addContent(item4011);
		item4.addContent(item401);
		// *************************************************************
		Element item402 = new Element("operationalProperties", NS);
		Element item4021 = new Element("modifiesCas", NS).setText("true");
		item402.addContent(item4021);
		Element item4022 = new Element("multipleDeploymentAllowed", NS)
				.setText("true");
		item402.addContent(item4022);
		Element item4023 = new Element("outputsNewCASes", NS).setText("false");
		item402.addContent(item4023);
		item4.addContent(item402);
		// *************************************************************

		root.addContent(item4);
		Element item5 = new Element("externalResourceDependencies", NS);
		Collection<DTResource> resources = dt.getResources();
		Iterator<DTResource> itr = resources.iterator();
		while (itr.hasNext()) {
			DTResource dtr = itr.next();
			//** externalResourceDependencies* 
			Element item51 = new Element("externalResourceDependency", NS);
			Element item511 = new Element("key", NS).setText(dtr.getName());
			Element item512 = new Element("description", NS).setText(dtr
					.getDescription());
			Element item513 = new Element("interfaceName", NS).setText(dtr
					.getInter());
			Element item514 = new Element("optional", NS).setText("false");
			item51.addContent(item511);
			item51.addContent(item512);
			item51.addContent(item513);
			item51.addContent(item514);
			item5.addContent(item51);
		}
		root.addContent(item5);
		Element item6 = new Element("resourceManagerConfiguration", NS);
		
		Element item61 = new Element("externalResources", NS);

		Element item62 = new Element("externalResourceBindings", NS);
		
		itr = resources.iterator();
		while (itr.hasNext()) {
			DTResource dtr = itr.next();

			//** externalResources* 
			Element item611 = new Element("externalResource", NS);
			Element item6111 = new Element("name", NS).setText(dtr
					.getName());
			Element item6112 = new Element("description", NS).setText(dtr
					.getDescription());
			Element item6113 = new Element("fileResourceSpecifier", NS);

			Element item61131 = null;
			if (dtr.isGlobal()) {
				if (dtr.isNuevo()) {
					IResources ir = Factory.getInstance().getIResources();

					try {
						ir.addResource(new DTResourceData(dtr.getName(), dtr
								.getDescription(), apconf
								.getGlobalResourcesPath()
								+ barra
								+ FilenameUtils.getName(dtr.getFilename()),
								"false"));
					} catch (Exception e) {
						
						String msg = "Error al agregar recurso "+dtr.getName();
						if (!AppConfiguration.getInstance().getLang().equals("es")){
							msg = "Error adding resource "+dtr.getName();
						}
						log.error(msg);						
												
						
					}

				}
				item61131 = new Element("fileUrl", NS).setText("file:"
						+ apconf.getPath(dtr.getName()));

			} else {
				if ((apconf.getLocalResourcesPath().startsWith("/"))
						|| (apconf.getLocalResourcesPath().startsWith("\\")))
					item61131 = new Element("fileUrl", NS).setText("file:"
							+ apconf.getLocalResourcesPath() + barra
							+ dt.getName() + barra
							+ FilenameUtils.getName(dtr.getFilename()));
				else
					item61131 = new Element("fileUrl", NS).setText("file:/"
							+ apconf.getLocalResourcesPath() + barra
							+ dt.getName() + barra
							+ FilenameUtils.getName(dtr.getFilename()));
			}

			Element item6114 = new Element("implementationName", NS)
					.setText(dtr.getImpl());

			item6113.addContent(item61131);

			item611.addContent(item6111);
			item611.addContent(item6112);
			item611.addContent(item6113);
			item611.addContent(item6114);
			item61.addContent(item611);

			//** externalResourceBindings* *
			Element item621 = new Element("externalResourceBinding", NS);
			Element item6211 = new Element("key", NS)
					.setText(dtr.getName());
			Element item6212 = new Element("resourceName", NS).setText(dtr
					.getName());

			item621.addContent(item6211);
			item621.addContent(item6212);
			item62.addContent(item621);

		}
		
	
		//***********************************************************************
		it = paramFiles.iterator();
		while (it.hasNext()) {
			dtp = (DTParameter)it.next();

			Element item611 = new Element("externalResource", NS);
			Element item6111 = new Element("name", NS).setText(dtp.getName());
			Element item6112 = new Element("description", NS).setText(dtp.getDescription());
			Element item6113 = new Element("fileResourceSpecifier", NS);

			Element item61131 = null;
			
			item61131 = new Element("fileUrl", NS).setText("DYNAMIC");
				
			
			Element item6114 = new Element("implementationName", NS)
					.setText("");

			item6113.addContent(item61131);

			item611.addContent(item6111);
			item611.addContent(item6112);
			item611.addContent(item6113);
			item611.addContent(item6114);
			item61.addContent(item611);

			//** externalResourceBindings* *
			Element item621 = new Element("externalResourceBinding", NS);
			Element item6211 = new Element("key", NS)
					.setText(dtp.getName());
			Element item6212 = new Element("resourceName", NS).setText(dtp
					.getName());

			item621.addContent(item6211);
			item621.addContent(item6212);
			item62.addContent(item621);

		}
		item6.addContent(item61);
		item6.addContent(item62);		

		root.addContent(item6);				
		
		//***********************************************************************		
		
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());

		File file = new File(apconf.getEnginesXMLsPath() + barra + dt.getName());

		file.mkdir();

		try {
			File f = new File(apconf
					.getEnginesXMLsPath()
					+ barra + dt.getName() + barra + dt.getName() + ".xml");
			
			FileOutputStream fos = new FileOutputStream(f);
			OutputStreamWriter FileWriter = new OutputStreamWriter(fos,"UTF-8");
			outputter.output(new Document(root),FileWriter);				
			fos.flush();
			fos.close();
			
			if (!paramsLavinia.isEmpty())
				saveParametersLavinia(dt.getName(), paramsLavinia);
		} catch (java.io.FileNotFoundException e) {
			String msg = "Error al guardar datos del componente";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error saving component data";
			}
			log.error(msg);			
			throw new FileNotFoundException(msg);
			
		} catch (IOException e) {
			String msg = "Error de I/O al guardar datos del componente";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error saving component data";
			}
			log.error(msg);			
			throw new FileNotFoundException(msg);
		}
		
		

	}

	
	/* Guarda en un XML los valores por defecto de los parametros del componente */
	private void saveParametersLavinia(String component, Collection<DTParameter> colparams){
		
		Element root = new Element("configParametersLavinia");

		Collection<Object> values;
		Iterator<Object> it2;
		Element item46121, item461211;

		Iterator<DTParameter> it = colparams.iterator();
		while (it.hasNext()) {
			DTParameter dtp = it.next();
			values = dtp.getValues();
			Element item461 = new Element("nameValuePair");
			Element item4611 = new Element("name").setText(dtp.getName());
			item461.addContent(item4611);
			Element item4612 = new Element("value");
			it2 = values.iterator();
			if (dtp.getType().equals("String")) {
				if (values.size() == 1) {
					item46121 = new Element("string")
							.setText(((DTParamType) it2.next()).getValue());
				} else {
					item46121 = new Element("array");
					while (it2.hasNext()) {
						item461211 = new Element("string")
								.setText(((DTParamType) it2.next()).getValue());
						item46121.addContent(item461211);
					}
				}
			} else if (dtp.getType().equals("Integer")) {
				if (values.size() == 1) {
					item46121 = new Element("integer")
							.setText((((DTParamType) it2.next()).getValue())
									.toString());
				} else {
					item46121 = new Element("array");
					while (it2.hasNext()) {
						item461211 = new Element("integer")
								.setText((((DTParamType) it2.next()).getValue())
										.toString());
						item46121.addContent(item461211);
					}
				}
			} else if (dtp.getType().equals("Float")) {
				if (values.size() == 1) {
					item46121 = new Element("float")
							.setText((((DTParamType) it2.next()).getValue())
									.toString());
				} else {
					item46121 = new Element("array");
					while (it2.hasNext()) {
						item461211 = new Element("float")
								.setText((((DTParamType) it2.next()).getValue())
										.toString());
						item46121.addContent(item461211);
					}
				}
			} else if (dtp.getType().equals("Boolean")) {
				if (values.size() == 1) {
					item46121 = new Element("boolean")
							.setText((((DTParamType) it2.next()).getValue())
									.toString());
				} else {
					item46121 = new Element("array");
					while (it2.hasNext()) {
						item461211 = new Element("boolean")
								.setText((((DTParamType) it2.next()).getValue())
										.toString());
						item46121.addContent(item461211);
					}
				}
			} else
				item46121 = new Element("ERROR").setText(dtp.getType());
			item4612.addContent(item46121);
			item461.addContent(item4612);
			root.addContent(item461);
		}

		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		
		AppConfiguration apconf = AppConfiguration.getInstance();

		try {
			File file = new File(apconf.getEnginesXMLsPath()
					+ barra + component + barra + "configParametersLavinia.xml");
		
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter FileWriter = new OutputStreamWriter(fos,"UTF-8");
			outputter.output(new Document(root),FileWriter);
			
					
			fos.flush();
			fos.close();
		} catch (IOException e) {
			String msg = "Error de I/O al escribir archivo de configuracion de parametros" +
					"para componente "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error saving file with parametters settings of "+component;
			}
			log.error(msg);		
		}
	}

	
	/* Elimina un componente del sistema junto con los archivos asociados,
	 * no se eliminan los archivos globales que utiliza.
	 */
	public void removeComponent(String component) throws XMLErrorException,	FileNotFoundException {

		SAXBuilder builder = new SAXBuilder();
		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document = null;
		try {
			document = builder.build(apconf.getComponentsXMLPath());
		} catch (JDOMException e) {
			String msg = "Error en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in LaviniaComponents.xml";
			}
			log.error(msg);			
			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error de I/O en XML LaviniaComponents";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error in LaviniaComponents.xml";
			}
			log.error(msg);			
			
			throw new XMLErrorException(msg);
		}

		Element root = document.getRootElement();
		List comps = root.getChildren("component");
		boolean fin = false;

		Iterator it = comps.iterator();
		Element comp = null;
		while ((!fin) && (it.hasNext())) {
			comp = (Element) it.next();
			if (comp.getText().equals(component))
				fin = true;
		}
		boolean simple=true;
		if (comp.getAttributeValue("simple").equals("false"))
			simple=false;
		
		String msg = "";
		if ((fin) && (root.removeContent(comp))) {
			try {

				XMLOutputter outputter = new XMLOutputter();
				outputter.setFormat(Format.getPrettyFormat());
				
				File file = new File(apconf.getComponentsXMLPath());
							
				
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter FileWriter = new OutputStreamWriter(fos,"UTF-8");
				outputter.output(document,FileWriter);
					
							
				fos.flush();
				fos.close();
				

			} catch (java.io.FileNotFoundException e) {
				String msg2 = "No se pudo eliminar " + component
						+ " de la lista";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Failed to delete component name from global list";
				}
				log.error(msg2);			
				
				throw new XMLErrorException(msg2);				
				
			} catch (IOException e) {
				String msg2 = "No se pudo eliminar " + component
				+ " de la lista";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Failed to delete component name from global list";
				}
				log.error(msg2);			
		
				throw new XMLErrorException(msg2);			
			}
		} else{
			
			String msg2="No se pudo eliminar de la lista";
			
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg2 = "Failed to delete component name from global list";
			}
			msg = msg + msg2+"\n";
			log.warn(msg2);
		}

		File dir = new File(apconf.getEnginesXMLsPath() + barra + component);
		if (dir != null) {
			if (!Files.delete(dir)){	
				String msg2="No se pudo eliminar directorio con descriptores";
				
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg2 = "Failed to delete folder with descriptors";
				}
				msg = msg + msg2+"\n";
				log.warn(msg2);
				
				
			}
		} else{			
			String msg2="No se encontro directorio con descriptores";
			
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg2 = "Failed to find folder with descriptors";
			}
			msg = msg + msg2+"\n";
			log.warn(msg2);			
			
		}
		if (simple){
			File jar = new File(apconf.getBinPath() + barra + component + ".jar");
			if (jar != null) {
				if (!Files.delete(jar)){
					String msg2="No se pudo eliminar "+component+".jar";
					
					if (!AppConfiguration.getInstance().getLang().equals("es")){
						msg2 = "Failed to delete "+component+".jar";
					}
					msg = msg + msg2+"\n";
					log.warn(msg2);
				}
			} else{
				String msg2="No se encontro archivo "+component+".jar";
				
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg2 = "Failed to find "+component+".jar";
				}
				msg = msg + msg2+"\n";
				log.warn(msg2);
			}
	
			
			// Eliminacion de Recursos locales
			File dirloc = new File(apconf.getLocalResourcesPath() + barra
					+ component);
			if (dirloc != null)
				Files.delete(dirloc);
		}	
		if (!msg.equals("")) {
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Warning: "+msg;
			}else
				msg = "Advertencia: " + msg;
			
			throw new FileNotFoundException(msg);
		}
		

	}

	
	/* Actualiza los datos de un componente, el vector que recibe como
	 * parametro son los archivos locales que no se utilizan mas.
	 */
	public void updateSimpleComponent(Vector removed, DTComponent dt)throws Exception {

		String component = dt.getName();
		SAXBuilder builder = new SAXBuilder();
		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document = null;
		try {
			document = builder.build(apconf.getEnginesXMLsPath() + barra
					+ component + barra + component + ".xml");
		} catch (JDOMException e) {
			String msg = "Error en descriptor XML del componente " + component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "No se encuentra descriptor XML del componente "+ component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);		
			throw new FileNotFoundException(msg);			
		}

		Element root = document.getRootElement();
		if (root!=null){
			Element resMan = root.getChild("resourceManagerConfiguration", NS);
			if (resMan!=null){
				Element extR = resMan.getChild("externalResources", NS);
					if (extR != null) {
						List res = extR.getChildren("externalResource", NS);
						Iterator it3 = res.iterator();
						while (it3.hasNext()) {
							Element er = (Element) it3.next();
							String nameR = er.getChildText("name", NS);
							
			
							if (removed.contains(nameR)) {
			
								Element fd = er.getChild("fileResourceSpecifier", NS);
								if (fd!=null){
									Element fdd = fd.getChild("fileUrl", NS);
									if (fdd!=null){
										String path = fdd.getText();
										if (path.indexOf(apconf.getLocalResourcesPath()) >= 0) {
											path = path.substring(5, path.length());
											if (!Files.delete(path)){
												String msg = "No se pudo eliminar recurso "+nameR+" del componente "+component;
												if (!AppConfiguration.getInstance().getLang().equals("es")){
													msg = "Failed to delete resource "+nameR+" of "+component;
												}
												log.warn(msg);		
											
												
											}
											File dir = new File(apconf.getLocalResourcesPath()+ barra + component);
											if ((dir!=null) && (dir.listFiles().length == 0))
												Files.delete(apconf.getLocalResourcesPath() + barra
														+ component);
										}
									}
								}
							}
						}
					}
				}
			}
		
		createXML(dt);
	}
	
	public void updateAggregateComponent(DTAggregateComponent dt)throws Exception {

		if (dt.getCompsParams().size()>0)
			createAggXML(dt);
		else{
			String msg = "Error: flujo vacio";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error: Empty Flow";
			}
			log.error(msg);
			throw new ProcessingException(msg);			
			
		}
		
	}

	
	/* Actualiza todas las referencias que hay en los componentes a un recurso
	 * que cambio para que las rutas sean las correctas
	 */	
	public void updateReferences(DTResourceData dtr) throws XMLErrorException {
	
		AppConfiguration apconf = AppConfiguration.getInstance();

		File dirxmls = new File(apconf.getEnginesXMLsPath());
		
		if (dirxmls!=null){

			File[] files = dirxmls.listFiles();
	
			if (files!=null){				
			
				for (int i = 0; i < files.length; i++) {
					File xml = new File(files[i], files[i].getName() + ".xml");
		
					SAXBuilder builder = new SAXBuilder();
		
					Document document = null;					
					try {
						document = builder.build(xml.getAbsolutePath());
					} catch (JDOMException e1) {
						String msg = "Error al escribir en archivo XML del componente "+files[i].getName();
						if (!AppConfiguration.getInstance().getLang().equals("es")){
							msg = "Error writing file "+files[i].getName();
						}
						log.error(msg);					
						throw new XMLErrorException(msg);
						
					} catch (IOException e1) {
						String msg = "Error de I/O al escribir en archivo XML del componente "+files[i].getName();
						if (!AppConfiguration.getInstance().getLang().equals("es")){
							msg = "I/O error writing file "+files[i].getName();
						}
						log.error(msg);					
						throw new XMLErrorException(msg);
					
					}	
					Element root = document.getRootElement();	
					if (root!=null){
						Element rmc = root.getChild("resourceManagerConfiguration", NS);
						if (rmc!=null){
							Element ers = rmc.getChild("externalResources", NS);
							if (ers!=null){
								List er = ers.getChildren("externalResource", NS);
		
								boolean updated = false;
								for (int j = 0; j < er.size(); j++) {
									String name = ((Element) er.get(j))
										.getChildText("name", NS);
									if (name.equals(dtr.getName())) {
										Element frs = ((Element) er.get(j)).getChild(
										"fileResourceSpecifier", NS);
										if (frs != null) {
											Element fu = frs.getChild("fileUrl", NS);
											if (fu!=null){
												if ((dtr.getURL().startsWith("/"))
														|| (dtr.getURL().startsWith("\\")))
													fu.setText("file:"
															+ dtr.getURL().replace("\\", "/"));
												else
													fu.setText("file:/"
															+ dtr.getURL().replace("\\", "/"));
											
												updated = true;
											}
										}
										
									}
								}
								if (updated) {
									try{
										XMLOutputter outputter = new XMLOutputter();
										outputter.setFormat(Format.getPrettyFormat());
										
										File file = new File(xml.getAbsolutePath());
													
										
										FileOutputStream fos = new FileOutputStream(file);
										OutputStreamWriter FileWriter = new OutputStreamWriter(fos,"UTF-8");
										outputter.output(document,FileWriter);
											
													
										fos.flush();
										fos.close();
									
							
									} catch (IOException e) {
										String msg = "Error al escribir en archivo XML";
										if (!AppConfiguration.getInstance().getLang().equals("es")){
											msg = "Error writing XML file";
										}
										log.error(msg +" "+ xml.getAbsolutePath());					
										throw new XMLErrorException(msg);									
									}
								}
							}
						}						
					}
				}
			}
		}		
	}
	
}
