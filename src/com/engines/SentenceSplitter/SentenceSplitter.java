/*
 * Created on 14/11/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.engines.SentenceSplitter;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;


/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SentenceSplitter extends CasAnnotator_ImplBase{
	
	private String language="";
	
	private Type Oracion;
	
	private static Logger logger;	
	
	private String freeling_conf_esp;
	private String freeling_conf_it;
	private String freeling_conf_eng;
	private String freeling_conf_cat;
	private String freeling_exe;
	
	public void initialize(UimaContext aContext) throws ResourceInitializationException {		
	    super.initialize(aContext);
	    language = (String)getContext().getConfigParameterValue("Lenguaje");
	    logger = getContext().getLogger();	
	    try{
	    	freeling_conf_esp = getContext().getResourceURL("FreeLingConfEsp").getPath();
	    	freeling_conf_it = getContext().getResourceURL("FreeLingConfIt").getPath();
	    	freeling_conf_eng = getContext().getResourceURL("FreeLingConfEng").getPath();
	    	freeling_conf_cat = getContext().getResourceURL("FreeLingConfCat").getPath();
	    	freeling_exe = getContext().getResourceURL("FreeLingExe").getPath();
	    	if ((freeling_exe.startsWith("/"))&&(freeling_exe.contains(":")))
	    		freeling_exe=freeling_exe.substring(1);
	    }catch(ResourceAccessException e){
	    	throw new ResourceInitializationException(
	    			AnnotatorInitializationException.STANDARD_MESSAGE_CATALOG,
	    			new Object[]{getClass().getName(), "No se encuentran rutas"});
	    			
	    }
	}
	
	public void typeSystemInit(TypeSystem aTypeSystem) throws AnalysisEngineProcessException {	    
		Oracion = aTypeSystem.getType("com.engines.Oracion");
	    if (Oracion == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Oracion");
	    	throw new AnalysisEngineProcessException(
	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	        new Object[]{getClass().getName(), "com.engines.Oracion"});
	    }
	}

	static void capture(final Reader reader) {
		new Thread() {
			public void run() {
				try {
					try {
						String str = readAll(reader);
						str.length();
					} finally {
						reader.close();
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Error al leer del proceso");				
				}		
			}
		}.start();
	}
	
	public static void sendInParallel(final Writer writer, final String entrada) {
		// Hay que tener una thread para escribir porque cae en deadlock de otra manera
		new Thread() {
			public void run() {
				try {
					try {
						writer.write(entrada);
					} finally {
						writer.close();
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Error al escribir en el proceso");					
				}		
			}
		}.start();
	}
	
	public static String interact(final Process process, final String entrada) throws Exception {
		
		capture(new InputStreamReader(process.getErrorStream()));		
		sendInParallel(new OutputStreamWriter(process.getOutputStream()), entrada);
		String salida = null;
		try{ 
			salida = readAll(new InputStreamReader(process.getInputStream()));
			process.getInputStream().close();
			process.waitFor();
		}catch(IOException e1){
			logger.log(Level.SEVERE, "Error al leer el stream de entrada");
			throw new RuntimeException("Error al leer el stream de entrada");
		}catch(InterruptedException e2){
			logger.log(Level.SEVERE, "Error al esperar por el proceso");
			throw new RuntimeException("Error al esperar por el proceso");
		}
		if (process.exitValue() != 0){
			logger.log(Level.SEVERE, "El proceso no termin� normalmente\nC�digo = "+process.exitValue());
			throw new RuntimeException("El proceso no termin� normalmente\nC�digo = "+process.exitValue());
		}
		return salida;
	}
	
	public static String readAll(Reader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		char[] buffer = new char[1024];
		while (true) {
			int len = -1;
			len = reader.read(buffer);
			if (len < 0) {
				break;
			}
			builder.append(buffer, 0, len);
		}
		return builder.toString();
	}
	
	public void process(CAS aCAS) throws AnalysisEngineProcessException {
		
		
			String text = aCAS.getDocumentText();
			
			String freeling_conf="";
			if (language.equals("Español")){
				freeling_conf=freeling_conf_esp;
			}else if (language.equals("Inglés")){
				freeling_conf=freeling_conf_eng;
			}else if (language.equals("Catalán")){
				freeling_conf=freeling_conf_cat;
			}else if (language.equals("Italiano")){
				freeling_conf=freeling_conf_it;
			}
			
			if ((freeling_conf.startsWith("/"))&&(freeling_conf.contains(":")))
	    		freeling_conf=freeling_conf.substring(1);
			
			
			Process process = null;
			try{ 
			process = Runtime.getRuntime().exec(
							new String[] {
								freeling_exe,
								"-f", freeling_conf,
								"--outf", "splitted"},
							null);
			}catch(IOException e){
				logger.log(Level.SEVERE, "Error al ejecutar proceso");
				throw new AnalysisEngineProcessException();
			}
			
			String result=null;
			try{
				result= interact(process, text);
			}catch(Exception e){
				throw new AnalysisEngineProcessException();
			}
			
			String endline = "\n";
			
			int fin = result.indexOf(endline);			
			int indice=0, temp;
			String line;
			int sentencia_ini=0;
			
	        while (fin>=0) {	        		
	        	
	        	line = result.substring(0, fin);        	
	        	temp=text.indexOf(line,indice);	        	
	        	if (line.equals("")){
	        		createAnnotationS(aCAS,sentencia_ini,temp+line.length());
	        		sentencia_ini=temp+line.length();
	        	}
	        	if (temp>=0){	        		
	        		indice=temp+line.length();
	        	}        			        	
	        	
	        	if ((fin+1)<result.length()){
	        		result=result.substring(fin+1,result.length());
	        		fin = result.indexOf(endline);
	        	}else
	        		fin=-1;
	        	
	        }	     
			
	}

	private void createAnnotationS(CAS aCAS, int aBeginPos, int aEndPos) {	
		AnnotationFS annot = aCAS.createAnnotation(Oracion, aBeginPos, aEndPos);
		aCAS.getIndexRepository().addFS(annot);
	}


}
