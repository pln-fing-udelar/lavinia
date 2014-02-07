package com.lavinia.gwt.user.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.TypeOrFeature;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
import org.apache.uima.analysis_engine.metadata.FlowConstraints;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.FileResourceSpecifier;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.ResourceManagerConfiguration;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lavinia.gwt.user.client.datatypes.DTAggregateComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponent;
import com.lavinia.gwt.user.client.datatypes.DTComponentData;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.datatypes.DTFeature;
import com.lavinia.gwt.user.client.datatypes.DTFeatureFS;
import com.lavinia.gwt.user.client.datatypes.DTParamType;
import com.lavinia.gwt.user.client.datatypes.DTParameter;
import com.lavinia.gwt.user.client.datatypes.DTResource;
import com.lavinia.gwt.user.client.datatypes.DTType;
import com.lavinia.gwt.user.client.datatypes.DTTypeF;
import com.lavinia.gwt.user.client.datatypes.DTTypeString;
import com.lavinia.gwt.user.client.datatypes.DTTypeValue;
import com.lavinia.gwt.user.client.exceptions.FileNotFoundException;
import com.lavinia.gwt.user.client.exceptions.XMLErrorException;
import com.lavinia.gwt.user.client.interfaces.IComponentsInfo;
import com.lavinia.gwt.user.client.interfaces.IResources;

public class IComponentsInfoImp extends RemoteServiceServlet implements
		IComponentsInfo {

	private String barra = "/";

	private Namespace NS = Namespace
			.getNamespace("http://uima.apache.org/resourceSpecifier");

	static Logger log = Logger.getLogger(IComponentsInfoImp.class.getName());

	/*
	 * Obtiene los tipos de salida de un flujo de componentes haciendo la union
	 * de los tipos de salida de estos (no hay repetidos)
	 */
	public ArrayList getOutputs(Collection flow) throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		
		
		
		HashMap<String, String> col = new HashMap<String, String>();

		Iterator it = flow.iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			XMLInputSource in = null;
			try {
				in = new XMLInputSource(apconf.getEnginesXMLsPath() + barra
						+ name + barra + name + ".xml");
			} catch (IOException e) {
				String msg = "Error al leer descriptor de "+name;
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error reading XML descriptor of "+name;
				}
				log.error(msg);
				
				throw new XMLErrorException(msg);
			}
			AnalysisEngineDescription aed = null;
			try {
				aed = UIMAFramework.getXMLParser()
						.parseAnalysisEngineDescription(in);
			} catch (InvalidXMLException e) {
				String msg = "Error al crear AE a partir de descriptor";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error creating AnalysisEngine form XML descriptor";
				}
				log.error(msg);				
				throw new XMLErrorException(msg);
				
				
			}
			AnalysisEngineMetaData aem = aed.getAnalysisEngineMetaData();
			Capability[] cs = aem.getCapabilities();
			if (cs.length > 0) {
				TypeOrFeature[] tof = cs[0].getOutputs();
				for (int i = 0; i < tof.length; i++) {
					if (tof[i].isType()) {
						String tt = tof[i].getName();
						int ind = tt.lastIndexOf(".");
						String typeAbrev = tt.substring(ind + 1, tt.length());
						col.put(typeAbrev, typeAbrev);
					}
				}
			}

		}
		String[] words= new String[col.size()];
		Set s = col.keySet();
		Iterator itt = s.iterator();
		int i=0;
		while (itt.hasNext()){
			String type = (String)itt.next();
			words[i]=type;
			i++;
			
		}
		
		Arrays.sort(words);
		
		ArrayList al = new ArrayList();
		for (int k=0; k< words.length; k++){
			al.add(words[k]);
		}
			
		return al;
	}

	/*
	 * Obtiene los datos de un componente y los retorna en un datatype
	 * DTComponentData
	 */
	public DTComponentData getComponentData(String component) throws Exception {

		
		String description = "";
		String[] out = new String[1];
		String[] inp = new String[1];
		DTComponentData dt = new DTComponentData();

		SAXBuilder builder = new SAXBuilder();
		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document = null;
		try {
			document = builder.build(apconf.getEnginesXMLsPath() + barra
					+ component + barra + component + ".xml");
		} catch (JDOMException e) {
			String msg = "Error en descriptor XML de "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
			
		} catch (IOException e) {
			String msg = "Error de I/O en descriptor XML de "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error in XML descriptor of "+component;
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		}
		
		
		
		Element root = document.getRootElement();
		if (root!=null){
			Element metadata = root.getChild("analysisEngineMetaData", NS);
			if (metadata!=null){
				Element desc = metadata.getChild("description", NS);
				if (desc!=null){
					description = desc.getText();

				Element caps = metadata.getChild("capabilities", NS);
				if (caps!=null){
					Element cap = caps.getChild("capability", NS);
					if (cap!=null){
						Element inputs = cap.getChild("inputs", NS);
						Element outputs = cap.getChild("outputs", NS);

						
						if (inputs!=null){
							List inps = inputs.getChildren("type", NS);
							inp = new String[inps.size()];
							for (int i = 0; i < inps.size(); i++) {
								String is = ((Element) inps.get(i)).getText();
								int index = is.lastIndexOf(".");
								inp[i] = is.substring(index + 1, is.length());

							}
						}
						
						if (outputs!=null){
							List outs = outputs.getChildren("type", NS);
							out = new String[outs.size()];
							for (int i = 0; i < outs.size(); i++) {
								String is = ((Element) outs.get(i)).getText();
								int index = is.lastIndexOf(".");
								out[i] = is.substring(index + 1, is.length());

							}

						}

					}
				}
				}
			}
		}
		Arrays.sort(inp);
		Arrays.sort(out);
		dt = new DTComponentData(description, inp, out);
				
		
		return dt;
	}

	/* Si el componente es simple, retorna solo el nombre del mismo en el vector.
	 * Si el componente es complejo, retorna el flujo que lo conforma
	 */
	public Vector getFlow(String component) throws XMLErrorException, FileNotFoundException{
		Vector res = new Vector();
		
				
		
		AppConfiguration apconf = AppConfiguration.getInstance();
		
		String path = apconf.getEnginesXMLsPath() + barra + component + barra + component + ".xml";

		

		XMLInputSource xml;
		try {
			xml = new XMLInputSource(path);
		} catch (IOException e) {
			String msg = "Error al abrir descriptor XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error opening XML descriptor";
			}
			log.error(msg+path);				
			throw new XMLErrorException(msg);
			
		}

		AnalysisEngineDescription AEdesc = null;
		try {
			AEdesc = UIMAFramework.getXMLParser()
					.parseAnalysisEngineDescription(xml);
		} catch (InvalidXMLException e) {
			String msg = "Error al crear AE a partir de descriptor";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error creating AnalysisEngine form XML descriptor";
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		}

		AnalysisEngineMetaData AEmeta = AEdesc.getAnalysisEngineMetaData();

		FlowConstraints fc = AEmeta.getFlowConstraints();
		if (fc instanceof FixedFlow) {
			FixedFlow ff = (FixedFlow) fc;
			String flow[] = ff.getFixedFlow();
			for (int i = 0; i < flow.length; i++) {
				int ind = flow[i].lastIndexOf(".");
				String name = flow[i].substring(ind + 1, flow[i].length());
				res.add(name);
			}
		}
		
		
		
				
		return res;
	}
	
	/*
	 * Obtiene todos los datos de un componente, esto incluye parametros y sus
	 * valores
	 */
	public DTComponent getComponentFullData(String component) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document = null;
		try {
			document = builder.build(apconf.getEnginesXMLsPath() + barra
					+ component + barra + component + ".xml");
		} catch (JDOMException e) {
			String msg = "Error en descriptor XML de "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error en descriptor XML de "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		}
		try{
			Element root = document.getRootElement();
	
			if (root!=null){
				Element metadata = root.getChild("analysisEngineMetaData", NS);
				if (metadata!=null){
	
					/** nombre * */
					String nameC = metadata.getChildText("name", NS);
					nameC = (nameC.substring(nameC.lastIndexOf(".") + 1, nameC.length()));
		
					/** autor * */
					String autor = (metadata.getChildText("vendor", NS));
		
					/** descripcion * */
					String desc = (metadata.getChildText("description", NS));
		
					/** paquete * */
					String file = root.getChildText("annotatorImplementationName", NS);
					file = (file.substring(0, file.lastIndexOf(".")));
		
					DTComponent dt = new DTComponent(nameC, autor, desc, file,
							new Vector<IsSerializable>(), new Vector<IsSerializable>(),
							new Vector<DTResource>());
		
					/** parametros * */
					Vector<IsSerializable> params = new Vector<IsSerializable>();
					String name, description, type, multivalued, mandatory;
					Element p, v, v2;
			
					Element confparams = metadata.getChild("configurationParameters", NS);
					List lparams = confparams.getChildren("configurationParameter", NS);
	
					if (lparams!=null){
						for (int i = 0; i < lparams.size(); i++) {
							p = (Element) lparams.get(i);
							name = p.getChildText("name", NS);
							description = p.getChildText("description", NS);
							type = p.getChildText("type", NS);
							multivalued = p.getChildText("multiValued", NS);
							mandatory = p.getChildText("mandatory", NS);
				
							/** *values**** */
							Collection<Object> values = new ArrayList<Object>();
							Element confparamsset = metadata.getChild(
									"configurationParameterSettings", NS);
							if (confparamsset!=null){
								List lvaluesPairs = confparamsset.getChildren("nameValuePair", NS);
								for (int j = 0; j < lvaluesPairs.size(); j++) {
									v = (Element) lvaluesPairs.get(j);
									String nameP = v.getChildText("name", NS);
									if (nameP.equals(name)) {
										Element val = v.getChild("value", NS);
										if ((val!=null)&&(multivalued.equals("true"))) {
											Element arr = val.getChild("array", NS);
											if (arr!=null){
												List vs = arr.getChildren(type.toLowerCase(), NS);
												for (int k = 0; k < vs.size(); k++) {
													Element evs = (Element) vs.get(k);
													if (evs!=null)
														values.add(new DTParamType(evs.getText()));
												}
											}
										} else {
											Element vs = val.getChild(type.toLowerCase(), NS);
											if (vs!=null)
												values.add(new DTParamType(vs.getText()));
										}
									}
								}
							}
				
							if (multivalued.equals("false")) {
								SAXBuilder builder2 = new SAXBuilder();
								Document configLavinia = null;
								try {
									configLavinia = builder2.build(apconf.getEnginesXMLsPath()
											+ barra + component + barra
											+ "configParametersLavinia.xml");
									Element root2 = configLavinia.getRootElement();
									if (root2!=null){
										List lvaluesPairsLav = root2.getChildren("nameValuePair");
										for (int m = 0; m < lvaluesPairsLav.size(); m++) {
											v2 = (Element) lvaluesPairsLav.get(m);
											if (v2!=null){
												String nameP = v2.getChildText("name");
												if (nameP.equals(name)) {
													Element val = v2.getChild("value");
													if (val!=null){
														if (multivalued.equals("false")) {
															Element arr = val.getChild("array");
															if (arr!=null){
																List vs = arr.getChildren(type.toLowerCase());
																for (int k = 0; k < vs.size(); k++) {
																	Element evs = (Element) vs.get(k);
																	if (evs!=null)
																		values.add(new DTParamType(evs.getText()));
																}
															}
														}
													}
												}
											}
										}
									}
								} catch (JDOMException e) {
									String msg = "Error en descriptor de parametros de "+component;
									if (!AppConfiguration.getInstance().getLang().equals("es")){
										msg = "Error in XML parametters descriptor of "+component;
									}
									log.error(msg);				
									throw new XMLErrorException(msg);
								} catch (IOException e) {
									// Si no se encuentra el archivo es porque no hay opciones
									// guardadas para el parametro
								}
				
							}
				
							/** ************ */
							DTParameter dtp = new DTParameter(name, type, description,
									mandatory, multivalued, values);
							params.add(dtp);
						}
					}
						
			
					/** ** tipos **** */
					Vector<IsSerializable> types = new Vector<IsSerializable>();
					DTType dtt = new DTTypeF();
			
					String nameT="", descriptionT="", supertype="";
			
					Element tsd = metadata.getChild("typeSystemDescription", NS);
					if (tsd!=null){
						Element ts = tsd.getChild("types", NS);
						if (ts!=null){
							List t = ts.getChildren("typeDescription", NS);
							Iterator it = t.iterator();
							while (it.hasNext()) {
								Element typed = (Element) it.next();
								if (typed!=null){
									nameT = typed.getChildText("name", NS);
									descriptionT = typed.getChildText("description", NS);
									supertype = typed.getChildText("supertypeName", NS);
								}
								Vector<String> outs = new Vector<String>(), inps = new Vector<String>();
								Element caps = metadata.getChild("capabilities", NS);
								if (caps!=null){
									Element cap = caps.getChild("capability", NS);
									if (cap!=null){
										Element inputs = cap.getChild("inputs", NS);
										Element outputs = cap.getChild("outputs", NS);
							
										if (inputs!=null){
											List linps = inputs.getChildren("type", NS);
											inps = new Vector<String>();
											for (int i = 0; i < linps.size(); i++) {
												inps.add(((Element) linps.get(i)).getText());
											}
										}
										if (outputs!=null){
											List louts = outputs.getChildren("type", NS);			
											
											outs = new Vector<String>();
											
											for (int i = 0; i < louts.size(); i++) {
												outs.add(((Element) louts.get(i)).getText());
											}
										}
									}
								}
									if ((typed!=null)&&(supertype.equals("String"))) {
										Vector<Object> values = new Vector<Object>();
										Element av = typed.getChild("allowedValues", NS);
										if (av!=null){
											List vals = av.getChildren("value", NS);
											Iterator it2 = vals.iterator();
											while (it2.hasNext()) {
												Element val = (Element) it2.next();
												
												Element sst = val.getChild("string", NS);
												Element sdesc = val.getChild("description", NS);
												DTTypeValue dtv = new DTTypeValue(sst.getText(), sdesc
														.getText());
												values.add(dtv);
												
											}
											boolean inp = false, out = false;
											if ((inps!=null)&&(inps.contains(nameT))) {
												inp = true;
											}
											if ((outs!=null)&&(outs.contains(nameT))) {
												out = true;
											}
							
											dtt = new DTTypeString(nameT, supertype, descriptionT, inp,
													out, values);
										}
									} else if (typed!=null){
										Vector<Object> features = new Vector<Object>();
										Element fs = typed.getChild("features", NS);
										if (fs != null) {
											List fds = fs.getChildren("featureDescription", NS);
											Iterator it2 = fds.iterator();
											while (it2.hasNext()) {
												Element fd = (Element) it2.next();
												String namef = fd.getChildText("name", NS);
												String descf = fd.getChildText("description", NS);
												String range = fd.getChildText("rangeTypeName", NS);
						
												Element et = fd.getChild("elementType", NS);
												DTFeature dtf = new DTFeature();
												if (et != null) {
													String ett = et.getText();
													dtf = new DTFeatureFS(namef, range, descf, ett);
												} else {
													dtf = new DTFeature(namef, range, descf);
						
												}
												features.add(dtf);
						
											}
										}
										boolean inp = false, out = false;
										if ((inps!=null)&&(inps.contains(nameT))) {
											inp = true;
										}
										if ((outs!=null)&&(outs.contains(nameT))) {
											out = true;
										}
										dtt = new DTTypeF(nameT, supertype, descriptionT, inp, out,
												features);
						
									}
									types.add(dtt);
								}
							
					
							}
								
						
					}
					dt.setTypes(types);
			
					Vector<DTResource> resources = new Vector<DTResource>();
					DTResource dtr = new DTResource();
			
					Element resMan = root.getChild("resourceManagerConfiguration", NS);
					if (resMan!=null){
						Element extR = resMan.getChild("externalResources", NS);
						if (extR != null) {
							List res = extR.getChildren("externalResource", NS);
							Iterator it3 = res.iterator();
							while (it3.hasNext()) {
								Element er = (Element) it3.next();
								String nameR = er.getChildText("name", NS);
								String descR = er.getChildText("description", NS);
								String imp = er.getChildText("implementationName", NS);
								String url = er.getChild("fileResourceSpecifier",NS).getChildText("fileUrl",NS);
								if (url.equals("DYNAMIC")){
									DTParameter dtp = new DTParameter(nameR,"File",descR,"true", "false", new ArrayList());
									params.add(dtp);
								}else{
									if (!((imp == null) || (imp.equals("")))) {
										String inter = "";
										Element extRD = root.getChild(
												"externalResourceDependencies", NS);
										if (extRD!=null){
											List extrds = extRD.getChildren(
													"externalResourceDependency", NS);
											Iterator it4 = extrds.iterator();
											boolean fin = false;
											while ((!fin) && (it4.hasNext())) {
												Element dep = (Element) it4.next();
												String key = dep.getChildText("key", NS);
												if (nameR.equals(key)) {
													inter = dep.getChildText("interfaceName", NS);
													fin = true;
												}
											}
						
											dtr = new DTResource(nameR, descR, inter, imp, "", false,
													false);
											}	
										} else
											dtr = new DTResource(nameR, descR, "", "", "", false, false);
								
				
									IResources ir = Factory.getInstance().getIResources();
									if (ir.existsResource(nameR)) {
										dtr.setGlobal(true);
					
									}
									Element fd = er.getChild("fileResourceSpecifier", NS);
									if (fd!=null){
										Element fdd = fd.getChild("fileUrl", NS);
										if (fdd!=null)
											dtr.setFilename(fdd.getText());
									}
					
									resources.add(dtr);
								}
							}
						}
					dt.setResources(resources);
					dt.setParameters(params);
					}
					return dt;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;

	}

	/* Obtiene los nombres de los componentes existentes en el sistema */
	public Collection<String> getComponents() throws Exception {

		Collection<String> col = new ArrayList<String>();

		AppConfiguration apconf = AppConfiguration.getInstance();
		SAXBuilder builder = new SAXBuilder();
		Document document;
		try {
			document = builder.build(apconf.getComponentsXMLPath());
		} catch (JDOMException e) {
			String msg = "Error al leer LaviniaComponents.xml";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading LaviniaComponents.xml";
			}
			log.error(msg+apconf.getComponentsXMLPath());				
			throw new XMLErrorException(msg);
			
			
		} catch (IOException e) {
			String msg = "Error al leer LaviniaComponents.xml";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading LaviniaComponents.xml";
			}
			log.error(msg+apconf.getComponentsXMLPath());				
			throw new XMLErrorException(msg);
		}
		Element root = document.getRootElement();
		List comps = root.getChildren("component");
		for (int i = 0; i < comps.size(); i++) {
			Element comp = (Element) comps.get(i);
			col.add(comp.getText());
		}

		return col;
	}

	/*
	 * Obtiene toda la informacion de los parametros de un componente dado,
	 * incluyendo los valores por defecto
	 */
	public Vector<DTParameter> getParameters(String component)
			throws Exception {
		Vector<DTParameter> params = new Vector<DTParameter>();

		String name="", description="", type="", multivalued="", mandatory="";
		Element p, v, v2;

		SAXBuilder builder = new SAXBuilder();

		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document = null;
		
		try {
			document = builder.build(apconf.getEnginesXMLsPath() + barra
					+ component + barra + component + ".xml");
		} catch (JDOMException e) {
			String msg = "Error en descriptor XML de "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
			
			
		} catch (IOException e) {
			String msg = "Error en descriptor XML de "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		}
		Element root = document.getRootElement();

		if (root!=null){
			Element metadata = root.getChild("analysisEngineMetaData", NS);
			if (metadata!=null){
				Element confparams = metadata.getChild("configurationParameters", NS);
				if (confparams!=null){
					List lparams = confparams.getChildren("configurationParameter", NS);

					for (int i = 0; i < lparams.size(); i++) {
						p = (Element) lparams.get(i);
						if (p!=null){
							name = p.getChildText("name", NS);
							description = p.getChildText("description", NS);
							type = p.getChildText("type", NS);
							multivalued = p.getChildText("multiValued", NS);
							mandatory = p.getChildText("mandatory", NS);
						}
						
						
						/** *values**** */
						Collection values = new ArrayList();
						Element confparamsset = metadata.getChild(
								"configurationParameterSettings", NS);
						if (confparamsset!=null){
							List lvaluesPairs = confparamsset.getChildren("nameValuePair", NS);
							for (int j = 0; j < lvaluesPairs.size(); j++) {
								v = (Element) lvaluesPairs.get(j);
								if (v!=null){
									String nameP = v.getChildText("name", NS);
									if (nameP.equals(name)) {
										Element val = v.getChild("value", NS);
										if (multivalued.equals("true")) {
											Element arr = val.getChild("array", NS);
											if (arr!=null){
												List vs = arr.getChildren(type.toLowerCase(), NS);
												for (int k = 0; k < vs.size(); k++) {
													Element evs = (Element) vs.get(k);
													if (evs!=null)
														values.add(new DTParamType(evs.getText()));
												}
											}
										}else{
											Element vs = val.getChild(type.toLowerCase(), NS);
											if (vs!=null)
												values.add(new DTParamType(vs.getText()));
										}
									}
								}
							}
							}
					
						/******************************************************************************/
							if (multivalued.equals("false")) {
								SAXBuilder builder2 = new SAXBuilder();
								
								Document configLavinia = null;
								try {
				
									configLavinia = builder2.build(apconf.getEnginesXMLsPath()
											+ barra + component + barra
											+ "configParametersLavinia.xml");
									Element root2 = configLavinia.getRootElement();
									if (root2!=null){
										List lvaluesPairsLav = root2.getChildren("nameValuePair");
										for (int m = 0; m < lvaluesPairsLav.size(); m++) {
											v2 = (Element) lvaluesPairsLav.get(m);											
											String nameP = v2.getChildText("name");
											
											if (nameP.equals(name)) {
												Element val = v2.getChild("value");
												if (val!=null)
													if (multivalued.equals("false")) {
														Element arr = val.getChild("array");
														if (arr!=null){
															List vs = arr.getChildren(type.toLowerCase());
															for (int k = 0; k < vs.size(); k++) {
																Element evs = (Element) vs.get(k);
																values.add(new DTParamType(evs.getText()));
															}
														}
													}
												}
										}
									}
								} catch (JDOMException e) {
									String msg = "Error en descriptor de parametros de "+component;
									if (!AppConfiguration.getInstance().getLang().equals("es")){
										msg = "Error in XML parametters descriptor of "+component;
									}
									log.error(msg);				
									throw new XMLErrorException(msg);
								} catch (IOException e) {
									// Si no se encuentra el archivo es porque no hay opciones
									// guardadas para el parametro
				
								}catch(Exception e){
									e.printStackTrace();
								}
								
							}
								
					DTParameter dt = new DTParameter(name, type, description,
							mandatory, multivalued, values);
					params.add(dt);
					}
				}
			}
			/******************************/
			
			Element resMan = root.getChild("resourceManagerConfiguration", NS);
			if (resMan!=null){
				Element extR = resMan.getChild("externalResources", NS);
				if (extR != null) {
					List res = extR.getChildren("externalResource", NS);
					Iterator it3 = res.iterator();
					while (it3.hasNext()) {
						Element er = (Element) it3.next();
						String nameR = er.getChildText("name", NS);
						String descR = er.getChildText("description", NS);						
						String url = er.getChild("fileResourceSpecifier",NS).getChildText("fileUrl",NS);
						if (url.equals("DYNAMIC")){
							DTParameter dtp = new DTParameter(nameR,"File",descR,"true", "false", new ArrayList());
							params.add(dtp);
							
						}
						
					}
				}	
			
			}
			
			
			/*******************************/
		}

		return params;
	}

	public Boolean isSimpleComponent(String component) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document = null;
		try {
			document = builder.build(apconf.getComponentsXMLPath());
		} catch (JDOMException e) {
			String msg = "Error en descriptor XML de "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			String msg = "Error en descriptor XML de "+component;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor of "+component;
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		}
		
		Element root = document.getRootElement();
	
		List comps = root.getChildren("component");
		for (int i = 0; i < comps.size(); i++) {
			Element comp = (Element) comps.get(i);
			if (comp.getText().equals(component))
					if (comp.getAttributeValue("simple").equals("false"))
						return new Boolean(false);
					else
						return new Boolean(true);
		}
		return new Boolean(false);
		
	}

	public DTAggregateComponent getAggregateData(String component) throws Exception {
		
		IComponentsAnalysisImp iai = (IComponentsAnalysisImp)Factory.getInstance().getIComponentsAnalysis();
		
		Vector compsParams = new Vector();
		AppConfiguration apconf = AppConfiguration.getInstance();
		
		String path = apconf.getEnginesXMLsPath() + barra + component + barra + component + ".xml";

		HashMap hm = new HashMap();

		XMLInputSource xml;
		try {
			xml = new XMLInputSource(path);
		} catch (IOException e) {
			String msg = "Error al abrir descriptor XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error opening XML descriptor";
			}
			log.error(msg+path);				
			throw new XMLErrorException(msg);
			
		}

		AnalysisEngineDescription AEdesc = null;
		try {
			AEdesc = UIMAFramework.getXMLParser()
					.parseAnalysisEngineDescription(xml);
		} catch (InvalidXMLException e) {
			String msg = "Error al crear AE a partir de descriptor";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error creating AnalysisEngine form XML descriptor";
			}
			log.error(msg+path);				
			throw new XMLErrorException(msg);
			
			
		}

		AnalysisEngineMetaData AEmeta = AEdesc.getAnalysisEngineMetaData();

		FlowConstraints fc = AEmeta.getFlowConstraints();
		if (fc instanceof FixedFlow) {
			FixedFlow ff = (FixedFlow) fc;
			String flow[] = ff.getFixedFlow();
			for (int i = 0; i < flow.length; i++) {
				int ind = flow[i].lastIndexOf(".");
				String name = flow[i].substring(0, ind);
				DTComponentParams dtc = new DTComponentParams(name,
						new Vector());
				compsParams.add(dtc);
				hm.put(name+"."+i, dtc);
			}
		}

		ConfigurationParameterDeclarations cpd = AEmeta
				.getConfigurationParameterDeclarations();
		ConfigurationParameterSettings cps = AEmeta
				.getConfigurationParameterSettings();

		ConfigurationParameter cp[] = cpd.getConfigurationParameters();
		for (int i = 0; i < cp.length; i++) {
			String name = cp[i].getName();
			String ovs[] = cp[i].getOverrides();
			for (int j = 0; j < ovs.length; j++) {
				String override = ovs[j];

				String engine = override.substring(0, override.indexOf("/"));
				String param = override.substring(override.indexOf("/") + 1,
						override.length());
				int ind = engine.lastIndexOf(".");
				if (ind>=0){
					String componentt = engine.substring(0,ind);	
					if (hm.containsKey(engine))
					((DTComponentParams) hm.get(engine))
						.addParam(iai.getParamInfoWithValue(componentt, param, cps
								.getParameterValue(name)));
				}
			}
		}
		/**********************************************************/
		
		int index=0;
		Iterator it = compsParams.iterator();
		while (it.hasNext()) {
			DTComponentParams dtc = (DTComponentParams) it.next();
			String key = dtc.getName()+"."+index;
				
			ResourceSpecifier rs = AEdesc.getComponentSpecifier(key);
			
			if (rs instanceof AnalysisEngineDescription){
				AnalysisEngineDescription aae = (AnalysisEngineDescription)rs;					
				
				ResourceManagerConfiguration rmc = aae.getResourceManagerConfiguration();
				ExternalResourceDescription[] ers =rmc.getExternalResources();
				for (int j=0;j<ers.length;j++){
					if (ers[j].getResourceSpecifier() instanceof FileResourceSpecifier){
						if (((FileResourceSpecifier)ers[j].getResourceSpecifier()).getFileUrl().equals("DYNAMIC")){										
							DTParameter dtp = new DTParameter(ers[j].getName(),"File",ers[j].getDescription(),"true","false",new ArrayList());
							String name = aae.getAnalysisEngineMetaData().getName();	
							int ind = name.lastIndexOf(".");
							if (ind>=0)
								if (hm.containsKey(name.substring(ind + 1, name
										.length())+"."+index))
									((DTComponentParams) hm.get(name.substring(
											ind + 1, name.length())+"."+index))
											.addParam(dtp);
						}
					}							
				}
			}
			index++;
		}
		/**********************************************************/

		DTAggregateComponent dta = new DTAggregateComponent(AEmeta.getName(),AEmeta.getVendor(),AEmeta.getDescription());
		dta.setCompsParams(compsParams);
		return dta;
		
	}
}
