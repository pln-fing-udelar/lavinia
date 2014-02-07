package com.engines.ReglasContextuales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
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
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.StringArrayFS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.jboss.util.file.Files;



public class ReglasContextuales  extends CasAnnotator_ImplBase{
	
	private String editorRC_path;
	
	private String rules_path;
	
	private String prolog_path;
	
	private String condiciones_path;
	
	
	
	private Type FLTag;
	
	private static Logger logger;	
	
	private Type Etiqueta;
	
	@Override
	public void initialize(UimaContext arg0) throws ResourceInitializationException {		
		super.initialize(arg0);
		logger = getContext().getLogger();	    
		
		 try{
		    	
		    	editorRC_path = getContext().getResourceURL("EditorRC").getPath();
		    	
		    	prolog_path = getContext().getResourceURL("PrologExe").getPath();
		    	
		      	rules_path = getContext().getResourceURL("Reglas").getPath();
		    	
		    	condiciones_path = getContext().getResourceURL("Condiciones").getPath();
		    	
		    }catch(ResourceAccessException e){
		    	
		    	throw new ResourceInitializationException(
		    			AnnotatorInitializationException.STANDARD_MESSAGE_CATALOG,
		    			new Object[]{getClass().getName(), "No se encuentran rutas"});		    			   
		    }
	}
	
	@Override
	public void typeSystemInit(TypeSystem aTypeSystem) throws AnalysisEngineProcessException {
		super.typeSystemInit(aTypeSystem);
		
		FLTag = aTypeSystem.getType("com.engines.FLTag");
		Etiqueta = aTypeSystem.getType("com.engines.RCTag");
		
	    if (FLTag == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo FLTag");
	    	throw new AnalysisEngineProcessException(
	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	        new Object[]{getClass().getName(), "com.engines.FLTag"});
	    }else if (Etiqueta == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Etiqueta");
	    	throw new AnalysisEngineProcessException(
	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	        new Object[]{getClass().getName(), "com.engines.RCTag"});
	    }
	}
	
	@Override
	public void process(CAS cas) throws AnalysisEngineProcessException {	
			
    		String temp_path = editorRC_path + System.nanoTime()+".txt";   		
    		
    		File f = new File (temp_path);
    		
    		
    	try{
    		Writer writer = new FileWriter(f);
    			try {
    				writer.write(getInputText(cas));
    			} finally {
    				try {
    					writer.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		    		
    		String entrada ="'"+condiciones_path+"'.\n"+ 
    		                "'"+rules_path+"'.\n"+
    		                "'"+temp_path+"'.\n";
    	
    		String procesada = editorRC(entrada);
    	    		
    		crearAnotaciones(cas,procesada);		    		
    		    		   		
		
		}catch (Exception e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		} finally{
			Files.delete(f);
		}
	}
	
	private void crearAnotaciones(CAS cas,String procesada) throws AnalysisEngineProcessException{
		
		try{
			String result = "";
			BufferedReader reader = new BufferedReader(	new StringReader(procesada));		
			String line = reader.readLine();
			for (; line != null; line = reader.readLine()) {
				result = result+line;
			}

				HashMap hm = new HashMap();
				
				AnnotationIndex it = cas.getAnnotationIndex(FLTag);
				FSIterator fs = it.iterator();
				int i=0;
				
				while (fs.hasNext()){
					AnnotationFS a = (AnnotationFS)fs.next();
					hm.put(i, a);
					i++;					
				}
				
				Pattern p = Pattern.compile("<[^>]*>");
				Matcher m = p.matcher(result);
				
				Pattern p2 = Pattern.compile("\\d+ ");
				Pattern p3 = Pattern.compile("atributo\\d=");
				Matcher m2,m3;
				Vector vals;
				String tipo;
				while (m.find()){						
					
					m2 = p2.matcher(m.group());
					
					if (m2.find()){
						String num1 = m2.group().replaceAll(" ", "");
						
						if (m2.find()){
							
							String num2 = m2.group().replaceAll(" ", "");
						
							
							if (hm.containsKey(new Integer(num1).intValue()-1)){
								
								if (hm.containsKey(new Integer(num2).intValue()-1)){
																		
									AnnotationFS a2 = (AnnotationFS)hm.get(new Integer(num1).intValue()-1);
									AnnotationFS a1 = (AnnotationFS)hm.get(new Integer(num2).intValue()-1);
									
									tipo = m.group().substring(1,m.group().indexOf(" "));
									
									m3 = p3.matcher(m.group());							
									vals = new Vector();
									while (m3.find()){
										String valor = m.group().substring(m3.end(),m.group().indexOf(" ", m3.end()));
										vals.add(valor);
									}
																
									createAnnotation(cas, a1.getBegin(),a2.getEnd(),tipo , vals);
								}
							}
						}
					}
				}
				
		}catch (Exception e) {
			e.printStackTrace();
			throw new AnalysisEngineProcessException(e);
		}
	}
	
	
	private String editorRC(String entrada) throws Exception {
		
		Process process = Runtime.getRuntime().exec(
				new String[] {
                    prolog_path,
					"-f", editorRC_path,
					"-t", "halt",
					"-g", "principal"},
				null,
				null);

		return interact(process, entrada);
	}
	
	private static void sendInParallel(final Writer writer, final String entrada) {
		
		new Thread() {
			public void run() {
				try {
					try {
						writer.write(entrada);
					} finally {
						writer.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}		
			}
		}.start();
	}
	
	private static void capture(final Reader reader) {
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
					e.printStackTrace();
				}		
			}
		}.start();
	}
	private static String interact(final Process process, final String entrada) throws Exception {

		capture(new InputStreamReader(process.getErrorStream()));
		
		sendInParallel(new OutputStreamWriter(process.getOutputStream()), entrada);
		String salida = readAll(new InputStreamReader(process.getInputStream()));
		process.getInputStream().close();
		process.waitFor();
		if (process.exitValue() != 0)
			throw new RuntimeException("El proceso no termino normalmente\nCodigo = "+process.exitValue());
		return salida;
	}
	
	private static String readAll(Reader reader) throws IOException {
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
	
	private String getInputText(CAS aCAS){
		String inputText="";
        FSIterator iter = aCAS.getAnnotationIndex().iterator();
        
		while (iter.isValid()){
			FeatureStructure fs = iter.get();
			if (fs instanceof AnnotationFS){
				AnnotationFS annot = (AnnotationFS)fs;
				String tt = annot.getType().getName();
				int ind = tt.lastIndexOf(".");
				String typeAbrev = tt.substring(ind+1,tt.length());
				if (typeAbrev.equals("FLTag")){
					
					inputText=inputText+annot.getCoveredText()+" "+
					annot.getFeatureValueAsString(annot.getType().getFeatureByBaseName("Lema"))+" "+
					annot.getFeatureValueAsString(annot.getType().getFeatureByBaseName("Etiqueta"))+"\n";
				}
				
			}		
			iter.moveToNext();
	    }	
		return inputText;
	}
	
	private void createAnnotation(CAS aCAS, int aBeginPos, int aEndPos, String tipo, Vector atributos) throws AnalysisEngineProcessException {
		
			
		AnnotationFS annot = aCAS.createAnnotation(Etiqueta, aBeginPos, aEndPos);
		
		Feature f1 = Etiqueta.getFeatureByBaseName("Tipo");
		Feature f2 = Etiqueta.getFeatureByBaseName("Atributos");	
		
		annot.setFeatureValueFromString(f1,tipo);				
		
		StringArrayFS sl = aCAS.createStringArrayFS(atributos.size());
		
		for (int i=0; i<atributos.size();i++)
			sl.set(i,(String)(atributos.get(i)));
		
		annot.setFeatureValue(f2,sl);		
		
		aCAS.getIndexRepository().addFS(annot);
		
	}

}
