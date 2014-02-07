package com.engines.ShallowParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.CasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

public class ShallowParser  extends CasAnnotator_ImplBase{
	
	private static Logger logger;
	
	private String language="";
	
	private String freeling_conf_esp;
	private String freeling_conf_it;
	private String freeling_conf_eng;
	private String freeling_conf_cat;
	private String freeling_exe;
	
	private Type FLTag;
	
	private Type Nivel_0;
	private Type Nivel_1;
	private Type Nivel_2;
	private Type Nivel_3;
	private Type Nivel_4;
	private Type Nivel_5;
	private Type Nivel_6;
	private Type Nivel_7;
	private Type Nivel_8mas;
	
	
	public void initialize(UimaContext arg0) throws ResourceInitializationException {
		super.initialize(arg0);
		
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
		super.typeSystemInit(aTypeSystem);
		
		FLTag = aTypeSystem.getType("com.engines.FLTag");
		Nivel_0 = aTypeSystem.getType("com.engines.Nivel_0");
		Nivel_1 = aTypeSystem.getType("com.engines.Nivel_1");
		Nivel_2 = aTypeSystem.getType("com.engines.Nivel_2");
		Nivel_3 = aTypeSystem.getType("com.engines.Nivel_3");
		Nivel_4 = aTypeSystem.getType("com.engines.Nivel_4");
		Nivel_5 = aTypeSystem.getType("com.engines.Nivel_5");
		Nivel_6 = aTypeSystem.getType("com.engines.Nivel_6");
		Nivel_7 = aTypeSystem.getType("com.engines.Nivel_7");
		Nivel_8mas = aTypeSystem.getType("com.engines.Nivel_8mas");
		
		  if (FLTag == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo FLTag");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.FLTag"});
		  }else if (Nivel_0 == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_0");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_0"});
		  }else if (Nivel_1 == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_1");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_1"});
		  }else if (Nivel_2 == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_2");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_2"});
		  }else if (Nivel_3 == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_3");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_3"});
		  }else if (Nivel_4 == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_4");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_4"});
		  }else if (Nivel_5 == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_5");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_5"});
		  }else if (Nivel_6 == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_6");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_6"});
		  }else if (Nivel_7 == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_7");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_7"});
		  }else if (Nivel_8mas == null){
		    	logger.log(Level.SEVERE, "No se encuentra el tipo Nivel_8mas");
		    	throw new AnalysisEngineProcessException(
		        AnnotatorInitializationException.TYPE_NOT_FOUND,
		        new Object[]{getClass().getName(), "com.engines.Nivel_8mas"});
		  }
	}
	
	
	public void process(CAS aCAS) throws AnalysisEngineProcessException {
	
		String text = aCAS.getDocumentText();
		
		HashMap hm = new HashMap();
		
		AnnotationIndex it = aCAS.getAnnotationIndex(FLTag);
		FSIterator fs = it.iterator();
		int i=0;
		
		while (fs.hasNext()){
			AnnotationFS a = (AnnotationFS)fs.next();
			hm.put(i, a);
			i++;					
		}
		
		
		
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
							"--outf", "parsed"},
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
		
		 			
		Pattern p = Pattern.compile("([^\\s]+_\\[)|(\\])|(\\((.*)[^\\((.+)\\)]\\))");
		Matcher m = p.matcher(result);
		
		Stack stack = new Stack();
		int anotaciones=0;
		
		int nivel = 0;
		
		boolean especial=false;
		while (m.find()){
			if (m.group().matches("([^\\s]+_\\[)")){
		
				Vector ve =new Vector();
				ve.add(m.group());
				ve.add(new Vector());
				ve.add(nivel);
				stack.push(ve); 
		
				nivel++;
				
			}else if (m.group().matches("\\((.*)[^\\((.+)\\)]\\)")){
				
				if ((!especial)&&(hm.containsKey(new Integer(anotaciones)))){
					AnnotationFS a = (AnnotationFS)hm.get(new Integer(anotaciones));
		
					for (int j=0; j<stack.size();j++){
						Vector v = (Vector)stack.get(j);
						((Vector)v.get(1)).add(a);						
					}
					
					AnnotationFS annot=null;
					Feature f1=null;
					int n=nivel;
					if (n==0){
						annot = aCAS.createAnnotation(Nivel_0, a.getBegin(), a.getEnd());
						f1 = Nivel_0.getFeatureByBaseName("Etiqueta");
					}else if (n==1){
						annot = aCAS.createAnnotation(Nivel_1, a.getBegin(), a.getEnd());
						f1 = Nivel_1.getFeatureByBaseName("Etiqueta");
					}else if (n==2){
						annot = aCAS.createAnnotation(Nivel_2, a.getBegin(), a.getEnd());
						f1 = Nivel_2.getFeatureByBaseName("Etiqueta");
					}else if (n==3){
						annot = aCAS.createAnnotation(Nivel_3, a.getBegin(), a.getEnd());
						f1 = Nivel_3.getFeatureByBaseName("Etiqueta");
					}else if (n==4){
						annot = aCAS.createAnnotation(Nivel_4, a.getBegin(), a.getEnd());
						f1 = Nivel_4.getFeatureByBaseName("Etiqueta");
					}else if (n==5){
						annot = aCAS.createAnnotation(Nivel_5, a.getBegin(), a.getEnd());
						f1 = Nivel_5.getFeatureByBaseName("Etiqueta");
					}else if (n==6){
						annot = aCAS.createAnnotation(Nivel_6, a.getBegin(), a.getEnd());
						f1 = Nivel_6.getFeatureByBaseName("Etiqueta");
					}else if (n==7){
						annot = aCAS.createAnnotation(Nivel_7, a.getBegin(), a.getEnd());
						f1 = Nivel_7.getFeatureByBaseName("Etiqueta");
					}else if (n>=8){
						annot = aCAS.createAnnotation(Nivel_8mas, a.getBegin(), a.getEnd());
						f1 = Nivel_8mas.getFeatureByBaseName("Etiqueta");
					}
										
					annot.setFeatureValueFromString(f1,"Hoja");
					
					aCAS.getIndexRepository().addFS(annot);				
					
					if (language.equals("Español")){
						if (a.getCoveredText().equals("del")){
							int ind2 = m.group().indexOf(" "); 
							String palabra = m.group().substring(1,ind2);
							if (palabra.equals("de"))
								especial=true;
						}else if (a.getCoveredText().equals("al")){
							int ind2 = m.group().indexOf(" "); 
							String palabra = m.group().substring(1,ind2);
							if (palabra.equals("a"))
								especial=true;
						}
					}
					anotaciones++;
				}else if (especial)
					especial=false;
				
			}else if (m.group().matches("(\\])")){
								
					
				Vector v = (Vector)stack.pop();
		
				Vector v2 = (Vector)v.get(1);
						
				int n = ((Integer)(((Vector)v).get(2))).intValue();

				if (v2.size()>0){
					AnnotationFS a1 = (AnnotationFS)v2.get(0);
					AnnotationFS a2 = (AnnotationFS)v2.get(v2.size()-1);
					AnnotationFS annot=null;
					Feature f1=null;
					
					if (n==0){
						annot = aCAS.createAnnotation(Nivel_0, a1.getBegin(), a2.getEnd());
						f1 = Nivel_0.getFeatureByBaseName("Etiqueta");
					}else if (n==1){
						annot = aCAS.createAnnotation(Nivel_1, a1.getBegin(), a2.getEnd());
						f1 = Nivel_1.getFeatureByBaseName("Etiqueta");
					}else if (n==2){
						annot = aCAS.createAnnotation(Nivel_2, a1.getBegin(), a2.getEnd());
						f1 = Nivel_2.getFeatureByBaseName("Etiqueta");
					}else if (n==3){
						annot = aCAS.createAnnotation(Nivel_3, a1.getBegin(), a2.getEnd());
						f1 = Nivel_3.getFeatureByBaseName("Etiqueta");
					}else if (n==4){
						annot = aCAS.createAnnotation(Nivel_4, a1.getBegin(), a2.getEnd());
						f1 = Nivel_4.getFeatureByBaseName("Etiqueta");
					}else if (n==5){
						annot = aCAS.createAnnotation(Nivel_5, a1.getBegin(), a2.getEnd());
						f1 = Nivel_5.getFeatureByBaseName("Etiqueta");
					}else if (n==6){
						annot = aCAS.createAnnotation(Nivel_6, a1.getBegin(), a2.getEnd());
						f1 = Nivel_6.getFeatureByBaseName("Etiqueta");
					}else if (n==7){
						annot = aCAS.createAnnotation(Nivel_7, a1.getBegin(), a2.getEnd());
						f1 = Nivel_7.getFeatureByBaseName("Etiqueta");
					}else if (n>=8){
						annot = aCAS.createAnnotation(Nivel_8mas, a1.getBegin(), a2.getEnd());
						f1 = Nivel_8mas.getFeatureByBaseName("Etiqueta");
					}
					
					String etiqueta = (String)v.get(0);
					etiqueta = etiqueta.substring(0, etiqueta.length()-2);
					annot.setFeatureValueFromString(f1,etiqueta);
					
					aCAS.getIndexRepository().addFS(annot);					
				
				}
				nivel--;
			}
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
			logger.log(Level.SEVERE, "El proceso no termino normalmente\nCodigo = "+process.exitValue());
			throw new RuntimeException("El proceso no termino normalmente\nCodigo = "+process.exitValue());
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
}
