package com.lavinia.gwt.user.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.uima.ResourceSpecifierFactory;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.TypeOrFeature;
import org.apache.uima.analysis_engine.metadata.AnalysisEngineMetaData;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
import org.apache.uima.analysis_engine.metadata.FlowConstraints;
import org.apache.uima.cas.ArrayFS;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.FloatArrayFS;
import org.apache.uima.cas.IntArrayFS;
import org.apache.uima.cas.StringArrayFS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.FileResourceSpecifier;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Capability;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.OperationalProperties;
import org.apache.uima.resource.metadata.ResourceManagerConfiguration;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XmlCasDeserializer;
import org.apache.uima.util.XmlCasSerializer;
import org.jboss.util.file.Files;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.lavinia.gwt.user.client.datatypes.DTAnnotation;
import com.lavinia.gwt.user.client.datatypes.DTAnnotationType;
import com.lavinia.gwt.user.client.datatypes.DTComponentParams;
import com.lavinia.gwt.user.client.datatypes.DTParamType;
import com.lavinia.gwt.user.client.datatypes.DTParameter;
import com.lavinia.gwt.user.client.datatypes.DTTreeNode;
import com.lavinia.gwt.user.client.exceptions.FileNotFoundException;
import com.lavinia.gwt.user.client.exceptions.ProcessingException;
import com.lavinia.gwt.user.client.exceptions.XMLErrorException;
import com.lavinia.gwt.user.client.interfaces.IComponentsAnalysis;

public class IComponentsAnalysisImp extends RemoteServiceServlet implements
		IComponentsAnalysis {

	private String barra = "/";

	private Namespace NS = Namespace
			.getNamespace("http://uima.apache.org/resourceSpecifier");

	static Logger log = Logger
			.getLogger(IComponentsAnalysisImp.class.getName());

	/*
	 * Analiza un texto tomando como entrada un flujo de componentes y sus
	 * parametros
	 */
	public HashMap<String, DTAnnotationType> analyze(Vector compsParams,
			String text, String folder2) throws FileNotFoundException,
			XMLErrorException, ProcessingException {

		HashMap<String, DTAnnotationType> allAnnots2 = new HashMap<String, DTAnnotationType>();

		AnalysisEngine nueva = null;
		try {
			AnalysisEngineDescription aed = createAE(compsParams, folder2);

			nueva = UIMAFramework.produceAnalysisEngine(aed);

			/** **************************************************** */

		} catch (ResourceInitializationException e) {
			e.printStackTrace();
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			String msg = "Error al inicializar recursos del flujo";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error initializing resources";
			}
			log.error(msg);			
			throw new ProcessingException(msg+e.getCause());
			
		} catch (InvalidXMLException e) {
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			String msg = "Error en archivos XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML files";
			}
			log.error(msg);		
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			String msg = "Error de I/O al crear AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error creating AnalysisEngine";
			}
			log.error(msg);		
			throw new FileNotFoundException(msg);
		}

		CAS cas = null;
		try {
			cas = nueva.newCAS();
		} catch (ResourceInitializationException e) {
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			String msg = "Error al inicializar recursos del flujo";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error initializing resources";
			}
			log.error(msg);			
			throw new ProcessingException(msg+e.getCause());
		}
		cas.setDocumentText(text);

		try {
			nueva.process(cas);
		} catch (AnalysisEngineProcessException e) {
			String msg = "Error al realizar procesamiento";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Flow processing error";
			}
			log.error(msg,e);			
			throw new ProcessingException(msg);			
			
		} finally{
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
		}

		FSIterator iter = cas.getAnnotationIndex().iterator();
		while (iter.isValid()) {
			FeatureStructure fs = iter.get();
			if (fs instanceof AnnotationFS) {

				AnnotationFS annot = (AnnotationFS) fs;

				String tt = annot.getType().getShortName();
				if (!tt.equals("DocumentAnnotation")) {

					DTAnnotation dtannot = new DTAnnotation(annot.getBegin(),
							annot.getEnd());

					DTTreeNode root = new DTTreeNode(annot.getType()
							.getShortName());
					addFeatureTreeNodes(cas, root, fs);
					dtannot.setNode(root);

					if (!allAnnots2.containsKey(tt)) {

						allAnnots2.put(tt, new DTAnnotationType(tt));

					}

					allAnnots2.get(tt).addAnnotation(dtannot);

				}
			}
			iter.moveToNext();
		}

		return allAnnots2;
	}

	
	private HashMap<String, DTAnnotationType> analyzeSingleFile(Vector compsParams,
			String text, String folder, String folder2) throws FileNotFoundException,
			XMLErrorException, ProcessingException {

		AppConfiguration apconf = AppConfiguration.getInstance();
		HashMap<String, DTAnnotationType> allAnnots2 = new HashMap<String, DTAnnotationType>();

		AnalysisEngine nueva = null;
		try {
			AnalysisEngineDescription aed = createAE(compsParams, folder2);

			nueva = UIMAFramework.produceAnalysisEngine(aed);

			/** **************************************************** */

		} catch (ResourceInitializationException e) {		
			limpiarDirectorio(apconf.getParams() + barra + folder2);
			limpiarDirectorio(apconf.getCorpus() + barra + "folder"	+ folder);
			
			String msg = "Error al inicializar recursos del flujo";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error initializing resources";
			}
			log.error(msg);			
			throw new ProcessingException(msg+e.getCause());
			
		} catch (InvalidXMLException e) {
			limpiarDirectorio(apconf.getParams() + barra + folder2);
			limpiarDirectorio(apconf.getCorpus() + barra + "folder"	+ folder);
			
			String msg = "Error en archivos XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML files";
			}
			log.error(msg);		
			throw new XMLErrorException(msg);
		} catch (IOException e) {
			limpiarDirectorio(apconf.getParams() + barra + folder2);
			limpiarDirectorio(apconf.getCorpus() + barra + "folder"	+ folder);
			String msg = "Error de I/O al crear AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error creating AnalysisEngine";
			}
			log.error(msg);					
			throw new FileNotFoundException(msg);
		}

		CAS cas = null;
		try {
			cas = nueva.newCAS();
		} catch (ResourceInitializationException e) {
			limpiarDirectorio(apconf.getParams() + barra + folder2);
			limpiarDirectorio(apconf.getCorpus() + barra + "folder"	+ folder);
			String msg = "Error al inicializar recursos del flujo";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error initializing resources";
			}
			log.error(msg);			
			throw new ProcessingException(msg+e.getCause());
		}
		cas.setDocumentText(text);

		try {
			nueva.process(cas);
		} catch (AnalysisEngineProcessException e) {
			limpiarDirectorio(apconf.getParams() + barra + folder2);
			limpiarDirectorio(apconf.getCorpus() + barra + "folder"	+ folder);
			String msg = "Error al realizar procesamiento";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Flow processing error";
			}
			log.error(msg,e);			
			throw new ProcessingException(msg);			
		}

		FSIterator iter = cas.getAnnotationIndex().iterator();
		while (iter.isValid()) {
			FeatureStructure fs = iter.get();
			if (fs instanceof AnnotationFS) {

				AnnotationFS annot = (AnnotationFS) fs;

				String tt = annot.getType().getShortName();
				if (!tt.equals("DocumentAnnotation")) {

					DTAnnotation dtannot = new DTAnnotation(annot.getBegin(),
							annot.getEnd());

					DTTreeNode root = new DTTreeNode(annot.getType()
							.getShortName());
					addFeatureTreeNodes(cas, root, fs);
					dtannot.setNode(root);

					if (!allAnnots2.containsKey(tt)) {

						allAnnots2.put(tt, new DTAnnotationType(tt));

					}

					allAnnots2.get(tt).addAnnotation(dtannot);

				}
			}
			iter.moveToNext();
		}

		return allAnnots2;
	}
	
	public Vector analyzeImport(String path, Vector compsParams,
			boolean export,String folder2) throws FileNotFoundException, XMLErrorException,
			ProcessingException {
		Vector v = new Vector();
		HashMap<String, DTAnnotationType> allAnnots2 = new HashMap<String, DTAnnotationType>();

		AnalysisEngine nueva = null;
		try {
			nueva = UIMAFramework.produceAnalysisEngine(createAE(compsParams,folder2));
		} catch (ResourceInitializationException e) {
			Files.delete(AppConfiguration.getInstance().getEnginesXMLsPath()+barra+path+".xml");
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+barra+folder2);		
			e.printStackTrace();
			String msg = "Error al inicializar recursos del flujo";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error initializing resources";
			}
			log.error(msg);			
			throw new ProcessingException(msg+e.getCause());
		} catch (InvalidXMLException e) {
			Files.delete(AppConfiguration.getInstance().getEnginesXMLsPath()+barra+path+".xml");
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			String msg = "Error en archivos XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML files";
			}
			log.error(msg);		
			throw new XMLErrorException(msg);
			
		} catch (IOException e) {
			Files.delete(AppConfiguration.getInstance().getEnginesXMLsPath()+barra+path+".xml");
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			String msg = "Error de I/O al crear AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error creating AnalysisEngine";
			}
			log.error(msg);					
			throw new FileNotFoundException(msg);
			
		} catch (Exception e) {
			Files.delete(AppConfiguration.getInstance().getEnginesXMLsPath()+barra+path+".xml");
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			String msg = "Error al crear AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error creating AnalysisEngine";
			}
			log.error(msg);					
			throw new FileNotFoundException(msg);
		}

		CAS cas = null;
		try {
			cas = nueva.newCAS();
		} catch (ResourceInitializationException e) {
			Files.delete(AppConfiguration.getInstance().getEnginesXMLsPath()+barra+path+".xml");
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			String msg = "Error al inicializar recursos del flujo";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error initializing resources";
			}
			log.error(msg);			
			throw new ProcessingException(msg+e.getCause());
		}

		try {
			cas = importResult(cas, path);

		} catch (Exception e) {
			Files.delete(AppConfiguration.getInstance().getEnginesXMLsPath()+barra+path+".xml");
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			e.printStackTrace();
			String msg = "Error al importar CAS";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error importing CAS";
			}
			log.error(msg);			
			throw new ProcessingException(msg+e.getCause());			
		}

		try {
			nueva.process(cas);
		} catch (AnalysisEngineProcessException e) {
			String msg = "Error al realizar procesamiento";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Flow processing error";
			}
			log.error(msg,e);			
			throw new ProcessingException(msg);			
		} finally{
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder2);
			Files.delete(AppConfiguration.getInstance().getEnginesXMLsPath()+barra+path+".xml");
		}

		ArrayList outs = new ArrayList();
		FSIterator iter = cas.getAnnotationIndex().iterator();
		while (iter.isValid()) {
			FeatureStructure fs = iter.get();
			if (fs instanceof AnnotationFS) {

				AnnotationFS annot = (AnnotationFS) fs;

				String tt = annot.getType().getShortName();
				if (!tt.equals("DocumentAnnotation")) {

					DTAnnotation dtannot = new DTAnnotation(annot
							.getBegin(), annot.getEnd());

					DTTreeNode root = new DTTreeNode(annot.getType()
							.getShortName());
					addFeatureTreeNodes(cas, root, fs);
					dtannot.setNode(root);

					if (!allAnnots2.containsKey(tt)) {

						allAnnots2.put(tt, new DTAnnotationType(tt));
						outs.add(tt);

					}

					allAnnots2.get(tt).addAnnotation(dtannot);

				}
			}
			iter.moveToNext();
		}
		
		Object[] outputs = (Object[])outs.toArray();
		
		Arrays.sort(outputs);
		
		ArrayList third = new ArrayList();
		for (int i=0; i<outputs.length;i++){
			third.add(outputs[i]);
		}

		
		v.add(cas.getDocumentText());
		v.add(allAnnots2);
		v.add(third);

		if (export) {

			String id = Factory.getInstance().getIUtils().getNanoTime();

			AppConfiguration apconf = AppConfiguration.getInstance();
			path = apconf.getEnginesXMLsPath() + barra + id + ".xml";

			File file = new File(path);
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(file);
			} catch (java.io.FileNotFoundException e) {
				String msg = "Error en archivo XML donde se guardara resultado";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error saving results in XML file";
				}
				log.error(msg+" "+path);			
				throw new FileNotFoundException(msg);
			}

			try {

				XmlCasSerializer.serialize(cas, os);

			} catch (SAXException e) {
				String msg = "Error al serializar CAS a XML";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error serializing CAS";
				}
				log.error(msg);				
				throw new XMLErrorException(msg);
			} catch (IOException e) {
				String msg = "Error al serializar CAS a XML";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error serializing CAS";
				}
				log.error(msg);				
				throw new XMLErrorException(msg);
			}

			try {
				os.flush();
				os.close();
			} catch (IOException e) {
				String msg = "Error al cerrar archivo XML con resultados de analisis";
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Error closing XML file with results";
				}
				log.warn(msg);			
			}

			v.add(id);
		}
		
		
		
		return v;
	}

	/*
	 * Crea una AE compuesta a partir de la especificacion del flujo y los
	 * parametros
	 */
	
	public AnalysisEngineDescription createAE(Vector compsParams)
			throws Exception {

		AppConfiguration apconf = AppConfiguration.getInstance();

		ResourceSpecifierFactory factory = UIMAFramework
				.getResourceSpecifierFactory();
		AnalysisEngineMetaData metaData = factory
				.createAnalysisEngineMetaData();

	
		/** ** se carga la lista de nombres de componentes ** */
		String ss[] = new String[compsParams.size()];
		Iterator it = compsParams.iterator();
		int i = 0;		
		while (it.hasNext()) {
			DTComponentParams dtp = (DTComponentParams) it.next();
			ss[i] = dtp.getName()+"."+i;
			i++;
		}
		FixedFlow ff = factory.createFixedFlow();
		ff.setFixedFlow(ss);
		/** ************************************************** */

		/** ***************************************************** */

		metaData.setFlowConstraints(ff);
		metaData.setAsynchronousModeSupported(false);
		metaData.setName("AggregateAE");
		metaData.setDescription("");
		metaData.setVendor("Lavinia");
		metaData.setVersion("1.0");

	
		Capability cap = factory.createCapability();
		HashMap hm1 = getFullNameOutputs(ss);
		
		Iterator iter = hm1.keySet().iterator();
		while (iter.hasNext()){
			cap.addOutputType((String)iter.next(), true);
		}
		
		HashMap hm2 = getFullNameInputs(ss);
		
		iter = hm2.keySet().iterator();
		while (iter.hasNext()){
			cap.addOutputType((String)iter.next(), true);
		}
	
		Capability caps[] = new Capability[1];
		caps[0] = cap;
		metaData.setCapabilities(caps);

		
		
		/** **** operational properties********************** */
		OperationalProperties opp = factory.createOperationalProperties();
		opp.setModifiesCas(true);
		opp.setMultipleDeploymentAllowed(true);
		opp.setOutputsNewCASes(false);
		metaData.setOperationalProperties(opp);
		/** ************************************************* */

		/** ***** parametros ****************** */
		it = compsParams.iterator();
		ConfigurationParameterDeclarations cpd = factory
				.createConfigurationParameterDeclarations();
		ConfigurationParameterSettings cps = factory
				.createConfigurationParameterSettings();
		int component=0;
		while (it.hasNext()) {
			DTComponentParams dtp = (DTComponentParams) it.next();
			Collection<DTParameter> col = dtp.getParams();
			Iterator<DTParameter> it2 = col.iterator();
			while (it2.hasNext()) {
				DTParameter dt = it2.next();
				if ((dt.getType().equals("File"))
						|| (dt.getType().equals("Archivo"))) {

				} else {
					ConfigurationParameter cp = factory
							.createConfigurationParameter();
					cp.setName((dtp.getName() + "." + dt.getName()+"."+component));
					/** ******************** */
					cp.setDescription(dt.getDescription());
					if (dt.getMandatory().equals("true"))
						cp.setMandatory(true);
					else
						cp.setMandatory(false);
					if (dt.getMultivalued().equals("true"))
						cp.setMultiValued(true);
					else
						cp.setMultiValued(false);
					cp.setType(dt.getType());
					cp.addOverride(dtp.getName() +"."+component+ "/" + dt.getName());

					cpd.addConfigurationParameter(cp);

					// valores

					Iterator<Object> it3 = dt.getValues().iterator();

					if (dt.getMultivalued().equals("true")) {

						if ((dt.getMandatory().equals("true"))
								&& (!it3.hasNext())) {
							
							String msg = "El Parametro " + dt.getName() + " de "
							+ dtp.getName() + " es Requerido";
							if (!AppConfiguration.getInstance().getLang().equals("es")){
								msg = "The parametter "+ dt.getName() + " of "+ dtp.getName() +" is mandatory";
							}
										
							log.error(msg);
							throw new ProcessingException(msg);
						}

						if (dt.getType().equals("String")) {
							ArrayList<String> array = new ArrayList<String>();
							while (it3.hasNext()) {
								DTParamType dtpt = (DTParamType) it3.next();
								array.add(dtpt.getValue());

							}
							String ar[] = {};
							cps.setParameterValue(dtp.getName() + "."
									+ dt.getName()+"."+component, array.toArray(ar));
						} else if (dt.getType().equals("Integer")) {
							ArrayList<Integer> array = new ArrayList<Integer>();
							while (it3.hasNext()) {
								DTParamType dtpt = (DTParamType) it3.next();
								array.add(new Integer(dtpt.getValue()));

							}
							Integer ar[] = {};
							cps.setParameterValue(dtp.getName() + "."
									+ dt.getName()+"."+component, array.toArray(ar));
						} else if (dt.getType().equals("Float")) {
							ArrayList<Float> array = new ArrayList<Float>();
							while (it3.hasNext()) {
								DTParamType dtpt = (DTParamType) it3.next();
								array.add(new Float(dtpt.getValue()));

							}
							Float ar[] = {};
							cps.setParameterValue(dtp.getName() + "."
									+ dt.getName()+"."+component, array.toArray(ar));
						} else if (dt.getType().equals("Boolean")) {
							ArrayList<Boolean> array = new ArrayList<Boolean>();
							while (it3.hasNext()) {
								DTParamType dtpt = (DTParamType) it3.next();
								array.add(new Boolean(dtpt.getValue()));

							}
							Boolean ar[] = {};
							cps.setParameterValue(dtp.getName() + "."
									+ dt.getName()+"."+component, array.toArray(ar));
						}

					} else {
						if (it3.hasNext()) {
							DTParamType dtpt = (DTParamType) it3.next();
							if (dt.getType().equals("String")) {
								cps.setParameterValue(dtp.getName() + "."
										+ dt.getName()+"."+component, (dtpt).getValue());
							} else if (dt.getType().equals("Integer")) {
								cps.setParameterValue(dtp.getName() + "."
										+ dt.getName()+"."+component, new Integer(dtpt
										.getValue()));
							} else if (dt.getType().equals("Float")) {
								cps.setParameterValue(dtp.getName() + "."
										+ dt.getName()+"."+component, new Float(dtpt
										.getValue()));
							} else if (dt.getType().equals("Boolean")) {
								cps.setParameterValue(dtp.getName() + "."
										+ dt.getName()+"."+component, new Boolean(dtpt
										.getValue()));
							}
						} else if (dt.getMandatory().equals("true")) {
							String msg = "El Parametro " + dt.getName() + " de "
							+ dtp.getName() + " es Requerido";
							if (!AppConfiguration.getInstance().getLang().equals("es")){
								msg = "The parametter "+ dt.getName() + " of "+ dtp.getName() +" is mandatory";
							}										
							log.error(msg);
							throw new ProcessingException(msg);
						}
					}
				}
				
			}
			component++;
		}
		metaData.setConfigurationParameterDeclarations(cpd);
		metaData.setConfigurationParameterSettings(cps);

		/** *********************************** */
		AnalysisEngineDescription desc = factory
				.createAnalysisEngineDescription();
		desc.setMetaData(metaData);
		desc.setFrameworkImplementation("org.apache.uima.java");
		desc.setPrimitive(false);

		/** ******rutas a los componentes**** */
		Map<String, Import> mapf = desc
				.getDelegateAnalysisEngineSpecifiersWithImports();
		mapf.clear();
		it = compsParams.iterator();
		component=0;
		while (it.hasNext()) {
			DTComponentParams dtp = (DTComponentParams) it.next();
			Import imp = factory.createImport();
			imp.setLocation("file:" + apconf.getEnginesXMLsPath() + barra
					+ dtp.getName() + barra + dtp.getName() + ".xml");
			mapf.put(dtp.getName()+"."+component, imp);
			component++;
			
		}
		
		return (desc);

	}

	private HashMap<String, String> getFullNameInputs(String flow[]) throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		HashMap<String, String> col = new HashMap<String, String>();

		
		if (flow.length>0){
			String name = flow[0];
			int index = name.lastIndexOf(".");
			if (index>=0){
				name = name.substring(0,index);
				XMLInputSource in = null;
				try {
					in = new XMLInputSource(apconf.getEnginesXMLsPath() + barra
							+ name + barra + name + ".xml");
				} catch (IOException e) {
					String msg = "Error al leer descriptor de "+ name;
					if (!AppConfiguration.getInstance().getLang().equals("es")){
						msg = "Error reading XML descriptor of "+name;
					}
								
					log.error(msg + apconf.getEnginesXMLsPath() + barra + name + barra + name + ".xml");
					throw new XMLErrorException(msg);
				}
				
				AnalysisEngineDescription aed = null;
				try {
					aed = UIMAFramework.getXMLParser()
							.parseAnalysisEngineDescription(in);
				} catch (InvalidXMLException e) {
					
					String msg = "Error al crear AE a partir de descriptor de " + name;
					if (!AppConfiguration.getInstance().getLang().equals("es")){
						msg = "Error creating AnalysisEngine from descriptor "+name;
					}
								
					log.error(msg + apconf.getEnginesXMLsPath() + barra + name + barra
							+ name + ".xml");
					throw new XMLErrorException(msg);
					
				}
				AnalysisEngineMetaData aem = aed.getAnalysisEngineMetaData();
				Capability[] cs = aem.getCapabilities();
				if (cs.length > 0) {
					TypeOrFeature[] tof = cs[0].getInputs();
					for (int j = 0; j < tof.length; j++) {
						if (tof[j].isType()) 
							col.put(tof[j].getName(), tof[j].getName());					
					}
				}
			}

		}
		return col;
	}
	
	private HashMap<String, String> getFullNameOutputs(String flow[]) throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		HashMap<String, String> col = new HashMap<String, String>();

		
		for (int i = 0; i<flow.length;i++){
			String name = flow[i];
			int index = name.lastIndexOf(".");
			if (index>=0){
				name = name.substring(0,index);
			
				XMLInputSource in = null;
				try {
					in = new XMLInputSource(apconf.getEnginesXMLsPath() + barra
							+ name + barra + name + ".xml");
				} catch (IOException e) {
					
					
					String msg = "Error al leer descriptor de "
						+ name;
					if (!AppConfiguration.getInstance().getLang().equals("es")){
						msg = "Error reading descriptor of "+name;
					}
					log.error(msg
							+ apconf.getEnginesXMLsPath() + barra + name + barra
							+ name + ".xml");
					throw new XMLErrorException(msg);
				}
				AnalysisEngineDescription aed = null;
				try {
					aed = UIMAFramework.getXMLParser()
							.parseAnalysisEngineDescription(in);
				} catch (InvalidXMLException e) {
					String msg = "Error al crear AE a partir de descriptor de " + name;
					if (!AppConfiguration.getInstance().getLang().equals("es")){
						msg = "Error creating AnalysisEngine from descriptor "+name;
					}
								
					log.error(msg + apconf.getEnginesXMLsPath() + barra + name + barra
							+ name + ".xml");
					throw new XMLErrorException(msg);
					
				}
				AnalysisEngineMetaData aem = aed.getAnalysisEngineMetaData();
				Capability[] cs = aem.getCapabilities();
				if (cs.length > 0) {
					TypeOrFeature[] tof = cs[0].getOutputs();
					for (int j = 0; j < tof.length; j++) {
						if (tof[j].isType()) 
							col.put(tof[j].getName(), tof[j].getName());					
					}
				}
			}

		}
		return col;
	}
	
	
	public AnalysisEngineDescription createAE(Vector compsParams,
			String folder2) throws IOException, InvalidXMLException,
			ProcessingException, ResourceInitializationException {

		AppConfiguration apconf = AppConfiguration.getInstance();

		ResourceSpecifierFactory factory = UIMAFramework
				.getResourceSpecifierFactory();
		AnalysisEngineMetaData metaData = factory
				.createAnalysisEngineMetaData();

	
		String ss[] = new String[compsParams.size()];
		Iterator it = compsParams.iterator();
		int i = 0;
		while (it.hasNext()) {
			DTComponentParams dtp = (DTComponentParams) it.next();
			ss[i] = dtp.getName()+"."+i;
		
			i++;
		}
		FixedFlow ff = factory.createFixedFlow();
		ff.setFixedFlow(ss);
		/** ************************************************** */

		/** ***************************************************** */

		metaData.setFlowConstraints(ff);
		metaData.setAsynchronousModeSupported(false);
		metaData.setName("AggregateAE");
		metaData.setDescription("");
		metaData.setVendor("Lavinia");
		metaData.setVersion("1.0");

		/** ***** tipos de entrada y tipos de salida********* */


		/** **** operational properties********************** */
		OperationalProperties opp = factory.createOperationalProperties();
		opp.setModifiesCas(true);
		opp.setMultipleDeploymentAllowed(true);
		opp.setOutputsNewCASes(false);
		metaData.setOperationalProperties(opp);
		/** ************************************************* */

		/** ***** parametros ****************** */
		it = compsParams.iterator();
		ConfigurationParameterDeclarations cpd = factory
				.createConfigurationParameterDeclarations();
		ConfigurationParameterSettings cps = factory
				.createConfigurationParameterSettings();
		int component=0;
		while (it.hasNext()) {
			DTComponentParams dtp = (DTComponentParams) it.next();
			Collection<DTParameter> col = dtp.getParams();
			Iterator<DTParameter> it2 = col.iterator();
			while (it2.hasNext()) {
				DTParameter dt = it2.next();
				if ((dt.getType().equals("File"))
						|| (dt.getType().equals("Archivo"))) {

				} else {
					ConfigurationParameter cp = factory
							.createConfigurationParameter();
					cp.setName((dtp.getName() + "." + dt.getName()+"."+component));
					/** ******************** */
					cp.setDescription(dt.getDescription());
					if (dt.getMandatory().equals("true"))
						cp.setMandatory(true);
					else
						cp.setMandatory(false);
					if (dt.getMultivalued().equals("true"))
						cp.setMultiValued(true);
					else
						cp.setMultiValued(false);
					cp.setType(dt.getType());
					cp.addOverride(dtp.getName()+"."+component + "/" + dt.getName());

					cpd.addConfigurationParameter(cp);

					// valores

					Iterator<Object> it3 = dt.getValues().iterator();

					if (dt.getMultivalued().equals("true")) {

						if ((dt.getMandatory().equals("true"))
								&& (!it3.hasNext())) {
							String msg = "El Parametro " + dt.getName() + " de "
							+ dtp.getName() + " es Requerido";
							if (!AppConfiguration.getInstance().getLang().equals("es")){
								msg = "The parametter "+ dt.getName() + " of "+ dtp.getName() +" is mandatory";
							}
										
							log.error(msg);
							throw new ProcessingException(msg);
							
						
						}

						if (dt.getType().equals("String")) {
							ArrayList<String> array = new ArrayList<String>();
							while (it3.hasNext()) {
								DTParamType dtpt = (DTParamType) it3.next();
								array.add(dtpt.getValue());

							}
							String ar[] = {};
							cps.setParameterValue(dtp.getName() + "."
									+ dt.getName()+"."+component, array.toArray(ar));
						} else if (dt.getType().equals("Integer")) {
							ArrayList<Integer> array = new ArrayList<Integer>();
							while (it3.hasNext()) {
								DTParamType dtpt = (DTParamType) it3.next();
								array.add(new Integer(dtpt.getValue()));

							}
							Integer ar[] = {};
							cps.setParameterValue(dtp.getName() + "."
									+ dt.getName()+"."+component, array.toArray(ar));
						} else if (dt.getType().equals("Float")) {
							ArrayList<Float> array = new ArrayList<Float>();
							while (it3.hasNext()) {
								DTParamType dtpt = (DTParamType) it3.next();
								array.add(new Float(dtpt.getValue()));

							}
							Float ar[] = {};
							cps.setParameterValue(dtp.getName() + "."
									+ dt.getName()+"."+component, array.toArray(ar));
						} else if (dt.getType().equals("Boolean")) {
							ArrayList<Boolean> array = new ArrayList<Boolean>();
							while (it3.hasNext()) {
								DTParamType dtpt = (DTParamType) it3.next();
								array.add(new Boolean(dtpt.getValue()));

							}
							Boolean ar[] = {};
							cps.setParameterValue(dtp.getName() + "."
									+ dt.getName()+"."+component, array.toArray(ar));
						}

					} else {
						if (it3.hasNext()) {
							DTParamType dtpt = (DTParamType) it3.next();
							if (dt.getType().equals("String")) {
								cps.setParameterValue(dtp.getName() + "."
										+ dt.getName()+"."+component, (dtpt).getValue());
							} else if (dt.getType().equals("Integer")) {
								cps.setParameterValue(dtp.getName() + "."
										+ dt.getName()+"."+component, new Integer(dtpt
										.getValue()));
							} else if (dt.getType().equals("Float")) {
								cps.setParameterValue(dtp.getName() + "."
										+ dt.getName()+"."+component, new Float(dtpt
										.getValue()));
							} else if (dt.getType().equals("Boolean")) {
								cps.setParameterValue(dtp.getName() + "."
										+ dt.getName()+"."+component, new Boolean(dtpt
										.getValue()));
							}
						} else if (dt.getMandatory().equals("true")) {
							String msg = "El Parametro " + dt.getName() + " de "
							+ dtp.getName() + " es Requerido";
							if (!AppConfiguration.getInstance().getLang().equals("es")){
								msg = "The parametter "+ dt.getName() + " of "+ dtp.getName() +" is mandatory";
							}
										
							log.error(msg);
							throw new ProcessingException(msg);
						}
					}
				}
			}
			component++;
		}
		metaData.setConfigurationParameterDeclarations(cpd);
		metaData.setConfigurationParameterSettings(cps);

		/** *********************************** */
		AnalysisEngineDescription desc = factory
				.createAnalysisEngineDescription();
		desc.setMetaData(metaData);
		desc.setFrameworkImplementation("org.apache.uima.java");
		desc.setPrimitive(false);

		/** ******rutas a los componentes**** */
		
		Map<String, Import> mapf = desc
				.getDelegateAnalysisEngineSpecifiersWithImports();
		mapf.clear();
		it = compsParams.iterator();
		int index=0;
		while (it.hasNext()){
			DTComponentParams dtp = (DTComponentParams) it.next();
			Import imp = factory.createImport();
			imp.setLocation("file:" + apconf.getEnginesXMLsPath() + barra
					+ dtp.getName() + barra + dtp.getName() + ".xml");
			mapf.put(dtp.getName()+"."+index, imp);
			index++;
		}
		
		
		/** *************** ******************************************** */
		
	//	Map m = desc.getDelegateAnalysisEngineSpecifiers();
		
		
		//Collection c = m.values();
		//it = c.iterator();
		index=0;
		it = compsParams.iterator();
		while (it.hasNext()) {
			DTComponentParams dtp = (DTComponentParams) it.next();
			String key = dtp.getName()+"."+index;
			
			
			ResourceSpecifier rs = desc.getComponentSpecifier(key);
			//ResourceSpecifier rs = (ResourceSpecifier) it.next();
			if (rs instanceof AnalysisEngineDescription) {
				AnalysisEngineDescription aae = (AnalysisEngineDescription) rs;

				ResourceManagerConfiguration res = aae
						.getResourceManagerConfiguration();
				
				if (res != null) {

					ExternalResourceDescription[] ers = res
							.getExternalResources();
					for (int j = 0; j < ers.length; j++) {
						if (ers[j].getResourceSpecifier() instanceof FileResourceSpecifier) {
							if (((FileResourceSpecifier) ers[j]
									.getResourceSpecifier()).getFileUrl()
									.equals("DYNAMIC")) {									
								
								String name = aae.getAnalysisEngineMetaData().getName();
								int ind = name.lastIndexOf(".");
								if (ind>=0){
									name = name.substring(ind+1,name.length());
									String param = ers[j].getName();
									   									
									File f = new File(apconf.getParams()+"/"+folder2+"/"+name+"."+index+"/"+param);										
									if ((f.exists()) && (f.isDirectory())){
										File fs[]= f.listFiles();
										if (fs[0]!=null)
											((FileResourceSpecifier) ers[j].getResourceSpecifier())
												.setFileUrl(fs[0].getAbsolutePath());
									}
								}else{
									String param = ers[j].getName();
   									
									File f = new File(apconf.getParams()+"/"+folder2+"/"+name+"."+index+"/"+param);
									
									if ((f.exists()) && (f.isDirectory())){
										File fs[]= f.listFiles();
										if (fs[0]!=null)
											((FileResourceSpecifier) ers[j].getResourceSpecifier())
												.setFileUrl(fs[0].getAbsolutePath());
									}
								}
							}
						}
					}
				}				
			}
			index++;
		}
		

		/** ************************************************************* */
		return (desc);

	}

	/*
	 * Crea un arbol (aParentNode) con los tipos de anotaciones y los valores de
	 * sus atributos Utiliza mCAS para saber el sistema de tipos y aFS para
	 * saber los valores de los atributos de la anotacion
	 */
	private void addFeatureTreeNodes(CAS mCAS, DTTreeNode aParentNode,
			FeatureStructure aFS) {

		Type mStringType = mCAS.getTypeSystem().getType("uima.cas.String");
		java.util.List aFeatures = aFS.getType().getFeatures();
		Iterator iter = aFeatures.iterator();
		do {
			if (!iter.hasNext()) {
				break;
			}
			Feature feat = (Feature) iter.next();
			String featName = feat.getShortName();

			String featVal = "null";
			Type rangeType = feat.getRange();
			String rangeTypeName = rangeType.getShortName();

			if (!rangeTypeName.equals("Sofa")) {

				if (mCAS.getTypeSystem().subsumes(mStringType, rangeType)) {
					featVal = aFS.getStringValue(feat);
					if (featVal == null) {
						featVal = "null";
					} else if (featVal.length() > 64) {
						featVal = featVal.substring(0, 64) + "...";
					}

				} else if ("Integer".equals(rangeTypeName)) {
					int intVal = aFS.getIntValue(feat);
					featVal = Integer.toString(intVal);

				} else if ("Float".equals(rangeTypeName)) {
					float floatVal = aFS.getFloatValue(feat);
					featVal = Float.toString(floatVal);

				} else if ("StringArray".equals(rangeTypeName)) {
					StringArrayFS arrayFS = (StringArrayFS) aFS
							.getFeatureValue(feat);
					StringBuffer displayVal = new StringBuffer();
					if (arrayFS == null) {
						displayVal.append("null");
					} else {
						displayVal.append('[');
						String vals[] = arrayFS.toArray();
						for (int i = 0; i < vals.length - 1; i++) {
							displayVal.append(vals[i]);
							displayVal.append(',');
						}

						if (vals.length > 0) {
							displayVal.append(vals[vals.length - 1]);
						}
						displayVal.append(']');
					}
					featVal = displayVal.toString();

				} else if ("IntegerArray".equals(rangeTypeName)) {
					IntArrayFS arrayFS = (IntArrayFS) aFS.getFeatureValue(feat);
					StringBuffer displayVal = new StringBuffer();
					if (arrayFS == null) {
						displayVal.append("null");
					} else {
						displayVal.append('[');
						int vals[] = arrayFS.toArray();
						for (int i = 0; i < vals.length - 1; i++) {
							displayVal.append(vals[i]);
							displayVal.append(',');
						}

						if (vals.length > 0) {
							displayVal.append(vals[vals.length - 1]);
						}
						displayVal.append(']');
					}
					featVal = displayVal.toString();

				} else if ("FloatArray".equals(rangeTypeName)) {
					FloatArrayFS arrayFS = (FloatArrayFS) aFS
							.getFeatureValue(feat);
					StringBuffer displayVal = new StringBuffer();
					if (arrayFS == null) {
						displayVal.append("null");
					} else {
						displayVal.append('[');
						float vals[] = arrayFS.toArray();
						for (int i = 0; i < vals.length - 1; i++) {
							displayVal.append(vals[i]);
							displayVal.append(',');
						}

						if (vals.length > 0) {
							displayVal.append(vals[vals.length - 1]);
						}
						displayVal.append(']');
					}
					featVal = displayVal.toString();

				} else if ("FSArray".equals(rangeTypeName)) {
					ArrayFS arrayFS = (ArrayFS) aFS.getFeatureValue(feat);
					if (arrayFS != null) {
						DTTreeNode arrayNode = new DTTreeNode(featName
								+ " = FSArray");
						for (int i = 0; i < arrayFS.size(); i++) {
							FeatureStructure fsVal = arrayFS.get(i);
							if (fsVal != null) {
								DTTreeNode fsValNode = new DTTreeNode(
										FsTreeNodeObject(fsVal, featName));

								if (!fsVal.getType().getFeatures().isEmpty()) {
									fsValNode.add(new DTTreeNode(null));
								}
								arrayNode.add(fsValNode);
							} else {
								arrayNode.add(new DTTreeNode("null"));
							}
						}

						aParentNode.add(arrayNode);
						continue;
					}
				} else {
					FeatureStructure fsVal = aFS.getFeatureValue(feat);
					if (fsVal != null) {
						DTTreeNode fsValNode = new DTTreeNode(FsTreeNodeObject(
								fsVal, featName));
						if (!fsVal.getType().getFeatures().isEmpty()) {
							fsValNode.add(new DTTreeNode(null));
						}
						aParentNode.add(fsValNode);
						continue;
					}
				}
				aParentNode.add(new DTTreeNode(featName + " = " + featVal));
			}
		} while (true);

	}

	/*
	 * Crea un String que contiene el valor del atributo (mFeatureName) en la
	 * anotacion mFS
	 */
	private String FsTreeNodeObject(FeatureStructure mFS, String mFeatureName) {

		String mCaption = mFS.getType().getShortName();
		if (mFS instanceof AnnotationFS) {
			String coveredText = ((AnnotationFS) mFS).getCoveredText();
			if (coveredText.length() > 64) {
				coveredText = coveredText.substring(0, 64) + "...";
			}
			mCaption += " (\"" + coveredText + "\")";
		}
		if (mFeatureName != null) {
			mCaption = mFeatureName + " = " + mCaption;
		}
		return mCaption;

	}

	/*
	 * Analiza un corpus obteniendo los archivos de la carpeta de nombre folder,
	 * los nombres de los mismos estan en el vector files. Tiene como entrada el
	 * flujo de componentes y los valores de sus parametros
	 */
	public Vector analyzeFiles(Vector compsParams, Vector files,
			String folderCorpus, String folder2) throws Exception{
		AppConfiguration apconf = AppConfiguration.getInstance();

		
		try {

			Vector results = new Vector();
			for (int i = 0; i < files.size(); i++) {
				String path = apconf.getCorpus() + barra + "folder"
						+ folderCorpus + barra + files.get(i);
						
				HashMap<String, DTAnnotationType> result = new HashMap<String, DTAnnotationType>();
				String text = "";
				int len;
				char[] buf = new char[1024];
				
				FileInputStream ffile = null;
				InputStreamReader in = null;
				StringWriter out = null;
				try{
					ffile = new FileInputStream(path);				
					in = new InputStreamReader(ffile);
					out = new StringWriter();				
					while ((len = in.read(buf)) > 0)
						out.write(buf, 0, len);

					text = out.toString();
				}catch(Exception e){
					throw e;
				}finally{
					
					ffile.close();
					in.close();
					out.flush();
					out.close();
				}
				
				

				if (!text.equals(""))
					result = analyzeSingleFile(compsParams, text,folderCorpus,folder2);

				Vector<Serializable> v = new Vector<Serializable>();
				v.add(text);
				v.add(result);
				results.add(v);

			}

			return results;
		} catch (java.io.FileNotFoundException e) {
			String msg = "No se encuentran archivos del corpus";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Corpus files missing";
			}
						
			log.error(msg);			
			throw new FileNotFoundException(msg);
		} catch (IOException e2) {
			String msg = "Error al abrir archivos del corpus";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error opening corpus files";
			}
						
			log.error(msg);			
			throw new ProcessingException(msg);
			
		} finally {			
			
			limpiarDirectorio(apconf.getParams() + barra + folder2);
			limpiarDirectorio(apconf.getCorpus() + barra + "folder"	+ folderCorpus);			
			

		}

	}

	/* Limpia el directorio de la ruta folder eliminando todos sus archivos */
	private void limpiarDirectorio(String folder) {

		File dir = new File(folder);
		if (dir.exists()) {
			if (!Files.delete(dir)){
				String msg = "No se pudo eliminar directorio " + folder;
				if (!AppConfiguration.getInstance().getLang().equals("es")){
					msg = "Failed to delete folder "+folder;
				}
				
				log.warn(msg);
			}
		}

	}

	/*
	 * Crea una AE compuesta segun la especificacion de flujo y parametros de
	 * entrada y la guarda en un XML. Retorna un String con el nombre del
	 * archivo donde se guardo.
	 */
	public String saveFlow(Vector compsParams) throws Exception {
		AppConfiguration apconf = AppConfiguration.getInstance();
		String id = Factory.getInstance().getIUtils().getNanoTime();
		AnalysisEngineDescription ae = createAE(compsParams);

		File temp = new File(apconf.getEnginesXMLsPath() + "/" + id + ".xml");
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(temp);
		} catch (java.io.FileNotFoundException e) {
			String msg = "No se encuentra archivo xml con AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Missing XML descriptor";
			}
			log.error(msg);	
			
			
			throw new FileNotFoundException(msg);
		}

		try {
			ae.toXML(fop);
		} catch (SAXException e) {
			String msg = "Error al obtener XML de AE creada";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error obtaining XML from AnalysisEngine";
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
			
		} catch (IOException e) {
			String msg = "Error de I/O al obtener XML de AE creada";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O Error obtaining XML from AnalysisEngine";
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		}

		try {
			fop.flush();
			fop.close();
		} catch (IOException e) {
			String msg = "Error al cerrar XML con AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error closing XML with AnalysisEngine";
			}
			log.warn(msg);				
			
			
		}

		return id;
	}

	/*
	 * Exporta a un XML los resultados del analisis del texto de entrada
	 * utilizando el flujo seleccionado con los valores de los parametros.
	 * Retorna un string con el nombre del archivo donde se guardo el resultado.
	 */
	public String exportResult(Vector compsParams, String text,String folder)
			throws Exception {

		String id = Factory.getInstance().getIUtils().getNanoTime();

		AppConfiguration apconf = AppConfiguration.getInstance();
		String path = apconf.getEnginesXMLsPath() + barra + id + ".xml";

		File file = new File(path);
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (java.io.FileNotFoundException e) {
			String msg = "Error en archivo XML donde se guardara resultado";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error in XML descriptor where the results should be saved";
			}
			log.error(msg+path);				
			
			throw new FileNotFoundException(msg);
		}

		HashMap<String, DTAnnotationType> allAnnots2 = new HashMap<String, DTAnnotationType>();

		AnalysisEngineDescription aed = createAE(compsParams,folder);

		AnalysisEngine nueva = null;
		CAS cas = null;
		try {
			nueva = UIMAFramework.produceAnalysisEngine(aed);
			cas = nueva.newCAS();
			cas.setDocumentText(text);
		} catch (ResourceInitializationException e) {
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder);
			String msg = "Error al inicializar recursos del flujo";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error initializing resources";
			}
			log.error(msg);			
			throw new ProcessingException(msg+e.getCause());
		}

		try {
			nueva.process(cas);
		} catch (AnalysisEngineProcessException e) {
			String msg = "Error al realizar procesamiento";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Flow processing error";
			}
			log.error(msg,e);			
			throw new ProcessingException(msg);			
		}finally{
			limpiarDirectorio(AppConfiguration.getInstance().getParams()+"/"+folder);
		}

		try {

			XmlCasSerializer.serialize(cas, os);

		} catch (SAXException e) {
			String msg = "Error al serializar CAS a XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error serializing CAS";
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
			
		} catch (IOException e) {
			String msg = "Error al serializar CAS a XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error serializing CAS";
			}
			log.error(msg);				
			throw new XMLErrorException(msg);
		}

		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			log.warn("Error al cerrar archivo XML con resultados de analisis");
		}

		return id;
	}

	/*
	 * Carga un flujo de componentes a partir del descriptor XML que se
	 * encuentra en el archivo de nombre id. Retorna un vector con el flujo y
	 * los valores de los parametros
	 */
	public Vector loadFlow(String id) throws Exception{
		AppConfiguration apconf = AppConfiguration.getInstance();
		String path = apconf.getEnginesXMLsPath() + barra + id + ".xml";
	
		Vector compsParams = new Vector();
			
		HashMap hm = new HashMap();

		XMLInputSource xml;
		try {
			xml = new XMLInputSource(path);
		} catch (IOException e) {
			limpiarDirectorio(path);
			String msg = "Error al abrir archivo descriptor XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error serializing CAS";
			}
			log.error(msg+path);				
			throw new XMLErrorException(msg);
			
		}

		AnalysisEngineDescription AEdesc = null;
		try {
			AEdesc = UIMAFramework.getXMLParser()
					.parseAnalysisEngineDescription(xml);
		} catch (InvalidXMLException e) {
			limpiarDirectorio(path);
			String msg = "Error al crear AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error creating AnalysisEngine";
			}
			log.error(msg+path);					
			throw new XMLErrorException(msg);
			
		}

		try{
			
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
						String component = engine.substring(0,ind);		
						if (hm.containsKey(engine))
							((DTComponentParams) hm.get(engine))
								.addParam(getParamInfoWithValue(component, param, cps
										.getParameterValue(name)));
					}
				}
			}
			/** *************************** */
	
			//Map m = AEdesc.getDelegateAnalysisEngineSpecifiers();
			//Collection c = m.values();
			//Iterator it = c.iterator();
	
			
			//while (it.hasNext()) {
				//ResourceSpecifier rs = (ResourceSpecifier) it.next();
			
			int index=0;
			Iterator it = compsParams.iterator();
			while (it.hasNext()) {
				DTComponentParams dtc = (DTComponentParams) it.next();
				String key = dtc.getName()+"."+index;
					
				ResourceSpecifier rs = AEdesc.getComponentSpecifier(key);
				
				if (rs instanceof AnalysisEngineDescription) {
					AnalysisEngineDescription aae = (AnalysisEngineDescription) rs;
	
					ResourceManagerConfiguration res = aae
							.getResourceManagerConfiguration();
					if (res != null) {
	
						ExternalResourceDescription[] ers = res
								.getExternalResources();
						for (int j = 0; j < ers.length; j++) {
							if (((FileResourceSpecifier) ers[j]
									.getResourceSpecifier()).getFileUrl().equals(
									"DYNAMIC")) {
								DTParameter dtp = new DTParameter(ers[j].getName(),
										"File", ers[j].getDescription(), "true",
										"false", new ArrayList());
								String name = aae.getAnalysisEngineMetaData()
										.getName();
								
								int ind = name.lastIndexOf(".");
								if (ind >= 0)
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
			return compsParams;
		}catch(Exception e){
			e.printStackTrace();
			String msg = "Error al crear AE";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error creating AnalysisEngine";
			}
			log.error(msg+path);					
			throw new XMLErrorException(msg);
			
			
		}finally{
			limpiarDirectorio(path);
		}

		
	}		
				
	

	/*
	 * Obtiene toda la informacion del parametro "param" del componente "engine"
	 * value contiene el o los valores que se asignaran al parametro
	 */
	public DTParameter getParamInfoWithValue(String engine, String param,
			Object value) throws XMLErrorException, FileNotFoundException {

		SAXBuilder builder = new SAXBuilder();

		AppConfiguration apconf = AppConfiguration.getInstance();
		Document document = null;
		try {
			document = builder.build(apconf.getEnginesXMLsPath() + barra
					+ engine + barra + engine + ".xml");
		} catch (JDOMException e) {
			String msg = "Error al leer descriptor de "
				+ engine;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error reading descriptor of "+engine;
			}
			log.error(msg);
			throw new XMLErrorException(msg);
			
			
		} catch (IOException e) {
			String msg = "Error de I/O al leer descriptor de "
				+ engine;
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "I/O error reading descriptor of "+engine;
			}
			log.error(msg);
			throw new XMLErrorException(msg);
		}
		
		Element root = document.getRootElement();

		Element metadata = root.getChild("analysisEngineMetaData", NS);
		Element confparams = metadata.getChild("configurationParameters", NS);
		List lparams = confparams.getChildren("configurationParameter", NS);

		String name = "", description, type, multivalued, mandatory;
		Element p, v, v2;
		boolean found = false;
		for (int i = 0; i < lparams.size(); i++) {
			p = (Element) lparams.get(i);

			name = p.getChildText("name", NS);

			if (name.equals(param)) {
				found = true;
				description = p.getChildText("description", NS);
				type = p.getChildText("type", NS);
				multivalued = p.getChildText("multiValued", NS);
				mandatory = p.getChildText("mandatory", NS);

				/** real values*********************** */
				Collection realvalues = new ArrayList();
				if (multivalued.equals("true")) {
					Object obv[] = (Object[]) value;
					for (int k = 0; k < obv.length; k++) {
						realvalues.add(new DTParamType(obv[k].toString()));
					}
				} else {
					realvalues.add(new DTParamType(value.toString()));
				}
				/** *values**** */
				Collection<Object> values = new ArrayList<Object>();

				Element confparamsset = metadata.getChild(
						"configurationParameterSettings", NS);
				List lvaluesPairs = confparamsset.getChildren("nameValuePair",
						NS);
				for (int j = 0; j < lvaluesPairs.size(); j++) {
					v = (Element) lvaluesPairs.get(j);
					String nameP = v.getChildText("name", NS);

					if (nameP.equals(name)) {
						Element val = v.getChild("value", NS);
						if (multivalued.equals("true")) {
							Element arr = val.getChild("array", NS);
							List vs = arr.getChildren(type.toLowerCase(), NS);
							for (int k = 0; k < vs.size(); k++) {
								Element evs = (Element) vs.get(k);
								values.add(new DTParamType(evs.getText()));
							}

						} else {
							Element vs = val.getChild(type.toLowerCase(), NS);
							values.add(new DTParamType(vs.getText()));

						}
					}
				}

				if (multivalued.equals("false")) {
					SAXBuilder builder2 = new SAXBuilder();
					Document configLavinia = null;
					try {
						configLavinia = builder2.build(apconf
								.getEnginesXMLsPath()
								+ barra
								+ engine
								+ barra
								+ "configParametersLavinia.xml");
						Element root2 = configLavinia.getRootElement();
						List lvaluesPairsLav = root2
								.getChildren("nameValuePair");
						for (int m = 0; m < lvaluesPairsLav.size(); m++) {
							v2 = (Element) lvaluesPairsLav.get(m);
							String nameP = v2.getChildText("name");
							if (nameP.equals(name)) {
								Element val = v2.getChild("value");
								if (multivalued.equals("false")) {
									Element arr = val.getChild("array");
									List vs = arr.getChildren(type
											.toLowerCase());
									for (int k = 0; k < vs.size(); k++) {
										Element evs = (Element) vs.get(k);
										values.add(new DTParamType(evs
												.getText()));
									}
								}
							}
						}
					} catch (JDOMException e) {
						String msg = "Error en descriptor de parametros de " + engine;
						if (!AppConfiguration.getInstance().getLang().equals("es")){
							msg = "Error reading parametters descriptor of "+engine;
						}
						log.error(msg);
						throw new XMLErrorException(msg);
						
					} catch (IOException e) {
						// Si no se encuentra el archivo es porque no hay
						// opciones guardadas para el parametro
					}
				}

				/** ************ */
				DTParameter dt = new DTParameter(name, type, description,
						mandatory, multivalued, values, realvalues);

				return dt;

			}
		}
		if (!found) {
			Element resMan = root.getChild("resourceManagerConfiguration", NS);
			if (resMan != null) {
				Element extR = resMan.getChild("externalResources", NS);
				if (extR != null) {
					List res = extR.getChildren("externalResource", NS);
					Iterator it3 = res.iterator();
					while (it3.hasNext()) {
						Element er = (Element) it3.next();
						String nameR = er.getChildText("name", NS);
						if (nameR.equals(param)) {
							String descR = er.getChildText("description", NS);
							String url = er.getChild("fileResourceSpecifier",
									NS).getChildText("fileUrl", NS);
							if (url.equals("DYNAMIC")) {
								DTParameter dtp = new DTParameter(nameR,
										"File", descR, "true", "false",
										new ArrayList());
								return dtp;
							}

						}
					}
				}
			}
		}

		return null;
	}

	/*
	 * Retorna true sii el flujo de componentes es correcto teniendo en cuenta
	 * los tipos de entrada y salida
	 */
	public boolean isFlowOK(Collection names) throws Exception {
		Iterator it = names.iterator();
		String name;
		Collection<String> outputs = new ArrayList<String>();

		if (it.hasNext()) {
			name = (String) it.next();
			Collection<String> t = getComponentInputs(name);
			if (t.size() > 0)
				return false;
			outputs = getComponentOutputs(name);
		} else
			return false;
		while (it.hasNext()) {
			name = (String) it.next();
			Collection<String> t = getComponentInputs(name);
			if (!outputs.containsAll(t))
				return false;
			outputs.addAll(getComponentOutputs(name));
		}
		return true;

	}

	public boolean isSubFlowOK(Collection names) throws Exception {

		return true;

	}

	/* Retorna los tipos de entrada de un componente */
	private Collection<String> getComponentInputs(String name)
			throws XMLErrorException {
		Collection<String> col = new ArrayList<String>();
		AppConfiguration apconf = AppConfiguration.getInstance();

		XMLInputSource in = null;
		try {
			in = new XMLInputSource(apconf.getEnginesXMLsPath() + barra + name
					+ barra + name + ".xml");
		} catch (IOException e) {
			String msg = "Error al abrir archivo XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error opening XML descriptor";
			}
			log.error(msg+ apconf.getEnginesXMLsPath()
					+ barra + name + barra + name + ".xml");
			throw new XMLErrorException(msg);
			
		}

		AnalysisEngineDescription aed = null;
		try {
			aed = UIMAFramework.getXMLParser().parseAnalysisEngineDescription(
					in);
		} catch (InvalidXMLException e) {
			
			String msg = "Error al crear AE a partir de XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error creating AnalysisEnginer";
			}
			
			log.error(msg
					+ apconf.getEnginesXMLsPath() + barra + name + barra + name
					+ ".xml");
			throw new XMLErrorException(msg);
		}

		AnalysisEngineMetaData aem = aed.getAnalysisEngineMetaData();
		Capability[] cs = aem.getCapabilities();
		if (cs.length > 0) {
			TypeOrFeature[] tof = cs[0].getInputs();
			for (int i = 0; i < tof.length; i++) {
				if (tof[i].isType()) {
					String tt = tof[i].getName();
					col.add(tt);
				}
			}
		}
		return col;
	}

	/* Obtiene los tipos de salida de un componente */
	private Collection<String> getComponentOutputs(String name)
			throws XMLErrorException {
		Collection<String> col = new ArrayList<String>();
		AppConfiguration apconf = AppConfiguration.getInstance();

		XMLInputSource in = null;
		try {
			in = new XMLInputSource(apconf.getEnginesXMLsPath() + barra + name
					+ barra + name + ".xml");
		} catch (IOException e) {
			String msg = "Error al abrir archivo XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error opening XML descriptor";
			}
			log.error(msg+ apconf.getEnginesXMLsPath()
					+ barra + name + barra + name + ".xml");
			throw new XMLErrorException(msg);
			
		}
		AnalysisEngineDescription aed = null;
		try {
			aed = UIMAFramework.getXMLParser().parseAnalysisEngineDescription(
					in);
		} catch (InvalidXMLException e) {
			
			String msg = "Error al crear AE a partir de XML";
			if (!AppConfiguration.getInstance().getLang().equals("es")){
				msg = "Error creating AnalysisEnginer";
			}
			
			log.error(msg
					+ apconf.getEnginesXMLsPath() + barra + name + barra + name
					+ ".xml");
			throw new XMLErrorException(msg);
		}
		AnalysisEngineMetaData aem = aed.getAnalysisEngineMetaData();
		Capability[] cs = aem.getCapabilities();
		if (cs.length > 0) {
			TypeOrFeature[] tof = cs[0].getOutputs();
			for (int i = 0; i < tof.length; i++) {
				if (tof[i].isType()) {
					String tt = tof[i].getName();
					col.add(tt);
				}
			}
		}
		return col;
	}

	private CAS importResult(CAS cas, String num) throws Exception {

		AppConfiguration apconf = AppConfiguration.getInstance();
		String path = apconf.getEnginesXMLsPath();
		String barra = "/";

		File file = new File(path + barra + num + ".xml");
		
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		}

		XmlCasDeserializer.deserialize(is, cas, true);

		is.close();
		
		return cas;

	}

}
