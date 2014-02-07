/*
 * Created on 04/09/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lavinia.gwt.user.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lavinia.gwt.user.client.exceptions.XMLErrorException;
import com.lavinia.gwt.user.client.interfaces.ITypes;

public class ITypesImp extends RemoteServiceServlet implements ITypes {

	static Logger log = Logger.getLogger(ITypesImp.class.getName());

	/*
	 * Obtiene los tipos que se pueden usar como supertipos, esto no incluye los
	 * tipos que se declararon en los componentes agregados Si feature=true se
	 * retornan los tipos que se pueden usar para los atributos.
	 */
	public Collection<String> getTypes(boolean feature) throws Exception {

		Collection<String> col = new ArrayList<String>();
		SAXBuilder builder = new SAXBuilder();

		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document = null;
		try {
			document = builder.build(apconf.getTypesXMLPath());
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con tipos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with types";
			}
			log.error(msg+apconf.getTypesXMLPath());			
			throw new XMLErrorException(msg);
			
		} catch (IOException e) {
			String msg = "Error al leer archivo con tipos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with types";
			}
			log.error(msg+apconf.getTypesXMLPath());			
			throw new XMLErrorException(msg);
		}
		Element root = document.getRootElement();
		List types = root.getChildren("type");
		for (int i = 0; i < types.size(); i++) {
			Element type = (Element) types.get(i);
			if (feature)
				col.add(type.getText());
			else if (!feature
					&& type.getAttributeValue("feature").equals("false"))
				col.add(type.getText());
		}

		return col;
	}

	/* Obtiene los tipos que representan colecciones, listas, arrays de objetos */
	public Collection<String> getMultiTypes() throws Exception {
		Collection<String> col = new ArrayList<String>();
		SAXBuilder builder = new SAXBuilder();

		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document;
		try {
			document = builder.build(apconf.getTypesXMLPath());
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con tipos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with types";
			}
			log.error(msg+apconf.getTypesXMLPath());			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error al leer archivo con tipos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with types";
			}
			log.error(msg+apconf.getTypesXMLPath());			
			throw new XMLErrorException(msg);
		}
		Element root = document.getRootElement();
		List types = root.getChildren("type");
		for (int i = 0; i < types.size(); i++) {
			Element type = (Element) types.get(i);
			if (type.getAttributeValue("multivalued").equals("true"))
				col.add(type.getText());
		}

		return col;

	}

}
