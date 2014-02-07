/*
 * Created on 08/11/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.engines.POSTagger;

/**
 * @author Cecilia Techera
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Vector;

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
public class POSTagger extends CasAnnotator_ImplBase{
	
	private String language="";
	private String taggertype="";
	
	private boolean Multiword;
	private boolean Numbers;
	private boolean Punctuation;
	private boolean Dates;
	private boolean Quantities;	
	
		
	private Type Adjetivo;
	private Type Adverbio;
	private Type Determinante;
	private Type Nombre;
	private Type Verbo;
	private Type Pronombre;
	private Type Conjuncion;
	private Type Interjeccion;
	private Type Preposicion;
	private Type Puntuacion;
	private Type Numero;
	private Type FechaHora;
	private Type Abreviacion;
	private Type Modal;
	
	private static Logger logger;	
	
	private String freeling_conf_esp;
	private String freeling_conf_it;
	private String freeling_conf_eng;
	private String freeling_conf_cat;
	private String freeling_exe;
	
	public void initialize(UimaContext aContext) throws ResourceInitializationException {		
	    super.initialize(aContext);
	    logger = getContext().getLogger();	    
	    language = (String)getContext().getConfigParameterValue("Lenguaje");
	    taggertype = (String)getContext().getConfigParameterValue("Tagger");
	    
	    Multiword = ((Boolean)getContext().getConfigParameterValue(("PalabrasCompuestas"))).booleanValue();
	    Numbers = ((Boolean)getContext().getConfigParameterValue(("DetectarNumeros"))).booleanValue();
	    Punctuation = ((Boolean)getContext().getConfigParameterValue(("DetectarPuntuacion"))).booleanValue();
	    Dates = ((Boolean)getContext().getConfigParameterValue(("DetectarFechas"))).booleanValue();
	    Quantities = ((Boolean)getContext().getConfigParameterValue(("DetectarCantidades"))).booleanValue();
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
		Adjetivo = aTypeSystem.getType("com.engines.Adjetivo");
		Adverbio = aTypeSystem.getType("com.engines.Adverbio");
		Determinante = aTypeSystem.getType("com.engines.Determinante");
		Nombre = aTypeSystem.getType("com.engines.Nombre");
		Verbo = aTypeSystem.getType("com.engines.Verbo");
		Pronombre = aTypeSystem.getType("com.engines.Pronombre");
		Conjuncion = aTypeSystem.getType("com.engines.Conjuncion");
		Interjeccion = aTypeSystem.getType("com.engines.Interjeccion");
		Preposicion = aTypeSystem.getType("com.engines.Preposicion");
		Puntuacion = aTypeSystem.getType("com.engines.Puntuacion");
		Numero = aTypeSystem.getType("com.engines.Numero");
		FechaHora = aTypeSystem.getType("com.engines.FechaHora");
		Abreviacion = aTypeSystem.getType("com.engines.Abreviacion");
		Modal = aTypeSystem.getType("com.engines.Modal");
		
	    if (Adjetivo == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Adjetivo");
	    	throw new AnalysisEngineProcessException(
	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	        new Object[]{getClass().getName(), "com.engines.Adjetivo"});
	    }else if (Adverbio == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Adverbio");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Adverbio"});
	    }else if (Determinante == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Determinante");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Determinante"});
	    }else if (Nombre == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Nombre");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Nombre"});
	    }else if (Verbo == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Verbo");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Verbo"});
	    }else if (Pronombre == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Pronombre");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Pronombre"});
	    }else if (Conjuncion == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Conjuncion");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Conjuncion"});
	    }else if (Interjeccion == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Interjeccion");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Interjeccion"});
	    }else if (Preposicion == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Preposicion");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Preposicion"});
	    }else if (Puntuacion == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Puntuacion");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Puntuacion"});
	    }else if (Numero == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Numero");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Numero"});
	    }else if (FechaHora == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo FechaHora");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.FechaHora"});
	    }else if (Abreviacion == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Abreviacion");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Abreviacion"});
	    }else if (Modal == null){
	    	logger.log(Level.SEVERE, "No se encuentra el tipo Modal");
	    	throw new AnalysisEngineProcessException(
	    	        AnnotatorInitializationException.TYPE_NOT_FOUND,
	    	        new Object[]{getClass().getName(), "com.engines.Modal"});
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
			
						
			String tag;
			
			if (taggertype.equals("HMM"))
				tag = "hmm";
			else
				tag = "relax";
			
			String multiword;
			if (Multiword)
				multiword="--loc";
			else
				multiword="--noloc";
			
			String numbers;
			if (Numbers)
				numbers="--numb";
			else
				numbers="--nonumb";
			
			String punctuation;
			if (Punctuation)
				punctuation="--punt";
			else
				punctuation="--nopunt";
			
			String dates;
			if (Dates){
				dates="--date";
				numbers="--numb";
			}else
				dates="--nodate";
			
			String quantities;
			if (Quantities){
				quantities="--quant";
				numbers="--numb";
			}else
				quantities="--noquant";
			
			String inputText="";
	        FSIterator iter = aCAS.getAnnotationIndex().iterator();
	        
			while (iter.isValid()){
				FeatureStructure fs = iter.get();
				if (fs instanceof AnnotationFS){
					AnnotationFS annot = (AnnotationFS)fs;
					String tt = annot.getType().getName();
					int ind = tt.lastIndexOf(".");
					String typeAbrev = tt.substring(ind+1,tt.length());
					if (typeAbrev.equals("Token")){
						inputText=inputText+annot.getCoveredText()+"\n";
					}
					
				}		
				iter.moveToNext();
		    }	
			
			Process process = null;
			try{
				process = Runtime.getRuntime().exec(
						new String[] {
							freeling_exe,
							"-f", freeling_conf,
							"--inpf", "token",
							"--outf", "tagged",		
							"--tag", tag, 
							multiword,
							numbers,
							punctuation,
							dates,
							quantities},
						null);
			}catch(IOException e){
				logger.log(Level.SEVERE, "Error al ejecutar proceso");
				throw new AnalysisEngineProcessException();
			}
		

			String result=null;
			try{
				result = interact(process,inputText);
			}catch(Exception e){
				throw new AnalysisEngineProcessException();
			}
			
			String endline = "\n";
			
			int fin = result.indexOf(endline);
			
			String aux=result;
			if (fin>=0)
				aux = result.substring(0, fin);
			
			
	      	String  aux2, aux22, aux3, aux4, aux5, aux6, t=null;
	        int indice=0;
	        int temp;
	        int firstspace,secondspace;
	        boolean f=false;
	        
	        while (fin>=0) {	        	
	        	firstspace=aux.indexOf(" ");
	        	if (firstspace>0){
		        	secondspace=aux.lastIndexOf(" ");
		        	aux2=aux.substring(0,firstspace);
		        	aux3=aux.substring(firstspace+1,secondspace);
		        	aux4=aux.substring(secondspace+1,aux.length());
		        	f=false;
		        	if (aux2.endsWith("_de")){
		        		aux22=aux2.replaceAll("_de","_del");
		        		aux22=aux22.replaceAll("_"," ");
		        		temp =text.indexOf(aux22,indice);
		        		if (temp>=0){
		        			
		        			result=result.substring(fin+1,result.length());
			        		fin = result.indexOf(endline);
			        		aux = result.substring(0, fin);
			        		

			        		firstspace=aux.indexOf(" ");
			        		secondspace=aux.lastIndexOf(" ");

				        	aux5=aux.substring(firstspace+1,secondspace);
				        	aux6=aux.substring(secondspace+1,aux.length());
			        		createAnnotation(aCAS,temp,temp+aux22.length(),aux3+"-"+aux5,aux4+"-"+aux6);
			        		indice=temp+aux22.length();
			        		f=true;
		        			
		        		}
		        	}else if (aux2.endsWith("_a")){
		        		aux22=aux2.replaceAll("_a","_al");
		        		aux22=aux22.replaceAll("_"," ");
		        		temp =text.indexOf(aux22,indice);
		        		if (temp>=0){
		        			
		        			result=result.substring(fin+1,result.length());
			        		fin = result.indexOf(endline);
			        		aux = result.substring(0, fin);
		        			
		        			

			        		firstspace=aux.indexOf(" ");
			        		secondspace=aux.lastIndexOf(" ");

				        	aux5=aux.substring(firstspace+1,secondspace);
				        	aux6=aux.substring(secondspace+1,aux.length());
			        		createAnnotation(aCAS,temp,temp+aux22.length(),aux3+"-"+aux5,aux4+"-"+aux6);
			        		indice=temp+aux22.length();
			        		f=true;
			        		
		        		}
		        	}
		        	boolean esp = false;
		        	if (!f){
			        	int guion =aux2.indexOf("_");
			        	int porc =aux2.indexOf("%");
			        	if (guion>=0 && porc>=0 && Quantities){
			        		t=aux2.replaceAll("_"," ");
			        		temp=text.indexOf(t,indice);			        	
				        	if (temp<0){
				        		aux2=aux2.replaceAll("_","");
				        	}else
				        		aux2=t;
			        	}else if (guion>=0){
			        		aux2=aux2.replaceAll("_"," ");
			        		if (aux2.contains(" de el ")){
			        			aux2=aux2.replaceAll(" de el ", " del ");
			        			esp=true;
			        		}if (aux2.contains(" a el ")){
			        			aux2= aux2.replaceAll(" a el ", " al ");
			        			esp=true;
			        		}
			        		
		        		}
			        	if (aux2.equals("a")){
			        		temp=text.indexOf("a",indice);
			        		int temp2 = text.indexOf("A",indice);
			        		if ((temp2>=0)&&(temp2<temp))
			        			temp=temp2;
			        		if ((temp>=0)&&(text.substring(temp,temp+2).equalsIgnoreCase("al"))){
			        			
			        			result=result.substring(fin+1,result.length());
				        		fin = result.indexOf(endline);
				        		aux = result.substring(0, fin);
			        			

				        		firstspace=aux.indexOf(" ");
				        		secondspace=aux.lastIndexOf(" ");
				        		aux2=aux.substring(0,firstspace);
					        	aux5=aux.substring(firstspace+1,secondspace);
					        	aux6=aux.substring(secondspace+1,aux.length());
				        		createAnnotation(aCAS,temp,temp+2,aux3+"-"+aux5,aux4+"-"+aux6);
				        		indice=temp+2;
				        		f=true;
				        		
			        		}
			        	}else if (aux2.equals("de")){
			        		temp=text.indexOf("d",indice);
			        		int temp2 = text.indexOf("D",indice);
			        		if ((temp2>=0)&&(temp2<temp))
			        			temp=temp2;
			        		if ((temp>=0)&&(text.substring(temp,temp+3).equalsIgnoreCase("del"))){
			        			
			        			
			        			result=result.substring(fin+1,result.length());
				        		fin = result.indexOf(endline);
				        		aux = result.substring(0, fin);
			        	
				        		firstspace=aux.indexOf(" ");
				        		secondspace=aux.lastIndexOf(" ");
				        		aux2=aux.substring(0,firstspace);
					        	aux5=aux.substring(firstspace+1,secondspace);
					        	aux6=aux.substring(secondspace+1,aux.length());
				        		createAnnotation(aCAS,temp,temp+3,aux3+"-"+aux5,aux4+"-"+aux6);
				        		indice=temp+3;
				        		f=true;
				        		
			        		}
			        	}		        	
			        	if (!f){
			        		temp=text.indexOf(aux2,indice);
				        	
				        	if (temp>=0){
				        		createAnnotation(aCAS,temp,temp+aux2.length(),aux3,aux4);			        	
				        		if (!esp)
				        			indice=temp+aux2.length();
				        		else
				        			indice=temp+aux2.length()-2;
				        	}else if (esp){
				        		aux2.replaceAll(" al ", " a el ");
				        		aux2.replaceAll(" del ", "de el");
				        		temp=text.indexOf(aux2,indice);
					        	
					        	if (temp>=0){
					        		createAnnotation(aCAS,temp,temp+aux2.length(),aux3,aux4);
					        		indice=temp+aux2.length();
					        	}
				        	}
			        	}  
		        	}
	        	}
	        	if ((fin+1)<result.length()){
	        		result=result.substring(fin+1,result.length());
	        		fin = result.indexOf(endline);
	        		if (fin>=0)
	        			aux = result.substring(0, fin);
	        	}else
	        		fin=-1;        		
	        }  			
	}

	private void createAnnotation(CAS aCAS, int aBeginPos, int aEndPos, String lemma, String label) throws AnalysisEngineProcessException {
			
		LabelTrans lt = new LabelTrans();
		String type = lt.getType(label, language);
		
		Type t = getType(type);
		AnnotationFS annot = aCAS.createAnnotation(t, aBeginPos, aEndPos);
		
		Feature f1 = t.getFeatureByBaseName("Etiqueta");
		Feature f2 = t.getFeatureByBaseName("Lema");
		Feature fl = t.getFeatureByBaseName("Lista");
		
		annot.setFeatureValueFromString(f1,label);
		annot.setFeatureValueFromString(f2,lemma);
		
		Vector list = lt.getList(label, language);
		StringArrayFS sl = aCAS.createStringArrayFS(list.size());
		
		for (int i=0; i<list.size();i++)
			sl.set(i,(String)(list.get(i)));
		
		if (fl!=null)
			annot.setFeatureValue(fl,sl);		
		
		aCAS.getIndexRepository().addFS(annot);
		
	}

	private Type getType(String type){
			
		if (type.equals("Adjetivo"))
			return Adjetivo;
		else if (type.equals("Adverbio"))
			return Adverbio;
		else if (type.equals("Determinante"))
			return Determinante;
		else if (type.equals("Nombre"))
			return Nombre;
		else if (type.equals("Verbo"))
			return Verbo;
		else if (type.equals("Pronombre"))
			return Pronombre;
		else if (type.equals("Conjuncion"))
			return Conjuncion;
		else if (type.equals("Interjeccion"))
			return Interjeccion;
		else if (type.equals("Preposicion"))
			return Preposicion;
		else if (type.equals("Puntuacion"))
			return Puntuacion;
		else if (type.equals("Numero"))
			return Numero;
		else if (type.equals("FechaHora"))
			return FechaHora;
		else if (type.equals("Abreviacion"))
			return Abreviacion;
		else if (type.equals("Modal"))
			return Modal;
		
		return null;
	}
	
}
