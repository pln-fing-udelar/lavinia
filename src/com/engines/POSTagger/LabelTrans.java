package com.engines.POSTagger;

import java.util.Vector;

public class LabelTrans {
	
	
	
	public String getType(String label, String lang){
		
		if (lang.equals("Ingles")){
			if (label.charAt(0)=='J')
				return "Adjetivo";
			else if (label.charAt(0)=='N')
				return "Nombre";
			else if (label.charAt(0)=='P')				
				return "Pronombre";
			else if (label.charAt(0)=='R')			
				return "Adverbio";
			else if (label.charAt(0)=='V')			
				return "Verbo";
			else if (label.charAt(0)=='D')				
				return "Determinante";
			else if ((label.equals("UH"))||(label.equals("INTJ")))
				return "Interjeccion";
			else if (label.equals("WDT"))
				return "Determinante";
			else if ((label.equals("WP"))||(label.equals("WP$")))
				return "Pronombre";
			else if (label.equals("WRB"))
				return "Adverbio";
			else if (label.equals("IN"))
				return "Preposicion";
			else if (label.equals("CC"))
				return "Conjuncion";
			else if ((label.equals("CD"))||(label.charAt(0)=='Z'))
				return "Numero";
			else if (label.charAt(0)=='M')
				return "Modal";			
			else if (label.charAt(0)=='W')				
				return "FechaHora";
			else 
				return "Abreviacion";
			
			
				
				
				
		}else{
			if (label.charAt(0)=='A'){
				//adjetivo
				return "Adjetivo";
			}else if (label.charAt(0)=='R'){
				//adverbio
				return "Adverbio";
			}else if (label.charAt(0)=='D'){
				//determinante
				return "Determinante";
			}else if (label.charAt(0)=='N'){
				//nombre
				return "Nombre";
			}else if (label.charAt(0)=='V'){
				//verbo
				return "Verbo";
			}else if (label.charAt(0)=='P'){
				//pronombre
				return "Pronombre";
			}else if (label.charAt(0)=='C'){
				//conjuncion
				return "Conjuncion";
			}else if (label.charAt(0)=='I'){
				//interjeccion		
				return "Interjeccion";
			}else if (label.charAt(0)=='S'){
				//preposicion
				return "Preposicion";
			}else if (label.charAt(0)=='F'){
				//puntuacion		
				return "Puntuacion";
			}else if (label.charAt(0)=='Z'){
				//numero
				return "Numero";
			}else if (label.charAt(0)=='W'){
				//fechahora
				return "FechaHora";
			}else{
				//abreviacion
				return "Abreviacion";
			}
		}
		
	}
	
	public Vector getList(String label, String lang){
		
		if (lang.equals("Ingles"))
			return new PennTree().getFeatures(lang, label);
		else
			return new Eagles().getFeatures(lang, label);
		
	}
}
