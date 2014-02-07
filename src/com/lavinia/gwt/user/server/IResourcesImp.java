package com.lavinia.gwt.user.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lavinia.gwt.user.client.datatypes.DTResourceData;
import com.lavinia.gwt.user.client.exceptions.ConfigurationException;
import com.lavinia.gwt.user.client.exceptions.XMLErrorException;
import com.lavinia.gwt.user.client.interfaces.IResources;

public class IResourcesImp extends RemoteServiceServlet implements IResources {

	static Logger log = Logger.getLogger(IResourcesImp.class.getName());

	/* Agrega un recurso al sistema */
	public void addResource(DTResourceData dtr) throws Exception {

		AppConfiguration apconf = AppConfiguration.getInstance();
		String paths = apconf.getConfigurationPaths();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;

		try {
			document = builder.build(paths);
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		}

		Element root = document.getRootElement();
		if (root.getChild(dtr.getName()) == null) {
			Element nuevo = new Element(dtr.getName());
			nuevo.setAttribute("description", dtr.getDescription());

			nuevo.setAttribute("externo", dtr.isExterno());
			
			nuevo.setText(dtr.getURL().replace("\\", "/"));
			root.addContent(nuevo);
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(paths);
				outputter.output(document, fos);
				fos.flush();
				fos.close();
			} catch (IOException e) {
				String msg = "Error al escribir en archivo con recursos";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error saving file with resources";
				}
				log.error(msg);
				
				throw new XMLErrorException(msg);
			}
		} else {
			String msg = "El recurso " + dtr.getName()
			+ "ya existe en el sistema";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Resource "+dtr.getName()+" already exists";
			}
			log.warn(msg);			
			throw new ConfigurationException(msg);
		}
	}

	/* Obtiene la informacion de todos los recursos existentes en el sistema */
	public Collection getAllResources() throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		String paths = apconf.getConfigurationPaths();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;

		try {
			document = builder.build(paths);
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		}

		Element root = document.getRootElement();

		List children = root.getChildren();
		Iterator it = children.iterator();
		Vector recs = new Vector();
		while (it.hasNext()) {
			Element p = (Element) it.next();
			if (!p.getName().equals("LAVINIA_PATH")) {
				if (p.getAttributeValue("externo").equals("true"))
					recs.add(new DTResourceData(p.getName(), p
							.getAttributeValue("description"), p.getText(),
							"true"));
				else
					recs.add(new DTResourceData(p.getName(), p
							.getAttributeValue("description"), p.getText(),
							"false"));
			}
		}

		return recs;
	}

	/*
	 * Obtiene la informacion de todos los recursos externos existentes, esto
	 * es, los que no son recursos globales agregados por los componentes
	 */
	public Collection getExtResources() throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		String paths = apconf.getConfigurationPaths();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;

		try {
			document = builder.build(paths);
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		}

		Element root = document.getRootElement();

		List children = root.getChildren();
		Iterator it = children.iterator();
		Vector recs = new Vector();
		while (it.hasNext()) {
			Element p = (Element) it.next();
			if (!p.getName().equals("LAVINIA_PATH")) {
				if (p.getAttributeValue("externo").equals("true"))
					recs.add(new DTResourceData(p.getName(), p
							.getAttributeValue("description"), p.getText(),
							"true"));
				else {
					String path = p.getText();
					int ind = path.indexOf(apconf.getGlobalResourcesPath());
					path = "GLOBAL_PATH"
							+ path.substring(ind
									+ apconf.getGlobalResourcesPath().length(),
									path.length());
					recs.add(new DTResourceData(p.getName(), p
							.getAttributeValue("description"), path, "false"));
				}
			}
		}

		return recs;
	}

	/* Elimina un recurso de la lista de recursos que mantiene el sistema */
	public Boolean removeResource(String name) throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		String paths = apconf.getConfigurationPaths();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;

		try {
			document = builder.build(paths);
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		}

		Element root = document.getRootElement();
		if (root.removeChild(name)) {
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(paths);
				outputter.output(document, fos);
				fos.flush();
				fos.close();
				return new Boolean(true);
			} catch (IOException e) {
				String msg = "Error al escribir archivo con recursos";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error writing file with resources";
				}
				log.error(msg+paths);
				
				throw new XMLErrorException(msg);
			}
		} else
			return new Boolean(false);
	}

	/*
	 * Actualiza el recurso de nombre LAVINIA_PATH, el cual apunta a la ruta
	 * donde se tiene instalada la aplicacion
	 */
	public Boolean updateLaviniaPath(DTResourceData dtr) throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		String paths = apconf.getConfigurationPaths();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;

		try {
			document = builder.build(paths);
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		}

		Element root = document.getRootElement();
		Element laviniapath = root.getChild(dtr.getName());
		String oldpath = laviniapath.getText();
		laviniapath.setText(dtr.getURL().replace("\\", "/"));
		laviniapath.setAttribute("description", dtr.getDescription());
		laviniapath.setAttribute("externo", dtr.isExterno());

		List children = root.getChildren();
		Iterator it = children.iterator();

		while (it.hasNext()) {
			Element p = (Element) it.next();
			if (p.getText().contains(oldpath)) {
				p.setText(p.getText().replace(oldpath,
						dtr.getURL().replace("\\", "/")));
			}
		}
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(paths);
			outputter.output(document, fos);
			fos.flush();
			fos.close();
			return new Boolean(true);
		} catch (IOException e) {
			String msg = "Error al escribir archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error writing file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		}

	}

	/*
	 * Actualiza los datos de un recurso dado, retorna true sii la actualizacion
	 * se realizo con exito
	 */
	public Boolean updateResource(DTResourceData dtr) throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		String paths = apconf.getConfigurationPaths();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;

		try {
			document = builder.build(paths);
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		}

		Element root = document.getRootElement();
		Element rec = root.getChild(dtr.getName());
		if (rec != null) {
			rec.setText(dtr.getURL().replace("\\", "/"));
			rec.setAttribute("description", dtr.getDescription());

			rec.setAttribute("externo", dtr.isExterno());
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(paths);
				outputter.output(document, fos);
				fos.flush();
				fos.close();
				return new Boolean(true);
			} catch (IOException e) {
				String msg = "Error al escribir archivo con recursos";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error writing file with resources";
				}
				log.error(msg+paths);
				
				throw new XMLErrorException(msg);
			}
		} else
			return new Boolean(false);

	}

	/* Retorna true sii el recurso con el nombre "name" ya existe en el sistema */
	public Boolean existsResource(String name) throws XMLErrorException {
		AppConfiguration apconf = AppConfiguration.getInstance();
		String paths = apconf.getConfigurationPaths();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;

		try {
			document = builder.build(paths);
		} catch (JDOMException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error al leer archivo con recursos";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading file with resources";
			}
			log.error(msg+paths);
			
			throw new XMLErrorException(msg);
		}

		Element root = document.getRootElement();

		if (root.getChild(name) == null)
			return new Boolean(false);
		return new Boolean(true);

	}
}
