package com.engines.POSTagger;

import java.util.Vector;

public class Eagles {

	
	public Vector getFeatures(String lang, String label){
		Vector list= new Vector();
		
		if (label.length()>=1){
			char codigo = label.charAt(0);
		
		
			if (codigo=='A'){
				
				if (label.length()>=2){
					char tipo = label.charAt(1);
					
					if (tipo=='Q'){
						list.add("calificativo");
					}else if (tipo=='O'){
						list.add("ordinal");
					}
				}
				
				if (label.length()>=3){
					char grado = label.charAt(2);
					if (grado=='A')
						list.add("apreciativo");
				}
					
				if (label.length()>=4){
					char genero = label.charAt(3);
					
					if (genero=='M')
						list.add("masculino");
					else if (genero=='F')
						list.add("femenino");				
					else if (genero=='C')
						list.add("comun");
				}
				
				if (label.length()>=5){
					char numero = label.charAt(4);
					
					if (numero=='S')
						list.add("singular");
					else if (numero=='P')
						list.add("plural");
					else if (numero=='N')
						list.add("invariable");
				}
				
				if (label.length()>=6){
					char funcion = label.charAt(5);
					
					if (funcion=='P')
						list.add("participio");
				}
				
			}else if (codigo=='C'){
				
				if (label.length()>=2){
					char tipo = label.charAt(1);
					
					if (tipo=='C')
						list.add("coordinada");
					else if (tipo=='S')
						list.add("subordinada");
				}
				
			}else if (codigo=='D'){
				if (label.length()>=2){
					char tipo=label.charAt(1);
					if (tipo=='D')
						list.add("demostrativo");
					else if (tipo=='P')
						list.add("posesivo");
					else if (tipo=='T')
						list.add("interrogativo");
					else if (tipo=='E')
						list.add("exclamativo");
					else if (tipo=='I')
						list.add("indefinido");
					else if (tipo=='A')
						list.add("articulo");
					else if (tipo=='N')
						list.add("numeral");
				
			
					if (lang.equals("Catalan"))
						if (tipo=='R')
							list.add("relativo");
				}
				
				if (label.length()>=3){
					char persona = label.charAt(2);
					
					if (persona=='1')
						list.add("primera persona");
					else if (persona=='2')
						list.add("segunda persona");
					else if (persona=='3')
						list.add("tercera persona");
				}
				
				if (label.length()>=4){
					char genero=label.charAt(3);
					
					if (genero=='M')
						list.add("masculino");
					else if (genero=='F')
						list.add("femenino");
					else if (genero=='C')
						list.add("comun");
					else if (genero=='N')
						list.add("neutro");
					
				}
				
				if (label.length()>=5){
					char numero = label.charAt(4);
					
					if (numero=='S')
						list.add("num=singular");
					else if (numero=='P')
						list.add("num=plural");
					else if (numero=='N')
						list.add("num=invariable");
				}
				
				if (label.length()>=6){
					char poseedor = label.charAt(5);
					
					if (poseedor=='S')
						list.add("poseedor=singular");
					else if (poseedor=='P')
						list.add("poseedor=plural");				
				}				
				
				
			}else if (codigo=='E'){
				
			}else if (codigo=='F'){
				//puntuacion
			}else if (codigo=='I'){
				
			}else if (codigo=='J'){
				
			}else if (codigo=='L'){
				
			}else if (codigo=='M'){
				
			}else if (codigo=='N'){
				
				if (label.length()>=2){
					char tipo = label.charAt(1);
					
					if (tipo=='C')
						list.add("comun");
					else if (tipo=='P')
						list.add("propio");
				}
				
				if (label.length()>=3){
					char genero = label.charAt(2);
					
					if (genero=='M')
						list.add("masculino");
					else if (genero=='F')
						list.add("femenino");
					else if (genero=='C')
						list.add("comun");
				}
				
				if (label.length()>=4){
					char numero=label.charAt(3);
					
					if (numero=='S')
						list.add("singular");
					else if (numero=='P')
						list.add("plural");
					else if (numero=='N')
						list.add("invariable");
					
				}
				
				if (label.length()>=6){
					if (lang.equals("Catalan"))
						if ((label.charAt(4)=='S')&&(label.charAt(5)=='P'))
							list.add("esser-persona");
						else if ((label.charAt(4)=='O')&&(label.charAt(5)=='0'))
							list.add("organitazio");
						else if ((label.charAt(4)=='G')&&(label.charAt(5)=='0'))
							list.add("Lloc");
				}
				
				if (label.length()>7){
					char grado=label.charAt(6);
					
					if (grado=='A')
						list.add("apreciativo");
				}
				
			}else if (codigo=='P'){
				
				if (label.length()>=2){
					char tipo = label.charAt(1);
					
					if (tipo=='P')
						list.add("personal");
					else if (tipo=='D')
						list.add("demostrativo");
					else if (tipo=='X')
						list.add("posesivo");
					else if (tipo=='I')
						list.add("indefinido");
					else if (tipo=='T')
						list.add("interrogativo");
					else if (tipo=='R')
						list.add("relativo");
					else if (tipo=='N')
						list.add("numeral");
					else if (tipo=='E')
						list.add("exclamativo");
				}
				
				if (label.length()>=3){
					char persona = label.charAt(2);
					
					if (persona=='1')
						list.add("primera persona");
					else if (persona=='2')
						list.add("segunda persona");
					else if (persona=='3')
						list.add("tercera persona");
				}
				
				if (label.length()>=4){
					char genero = label.charAt(3);
					
					if (genero=='M')
						list.add("masculino");
					else if (genero=='F')
						list.add("femenino");
					else if (genero=='C')
						list.add("comun");
					else if (genero=='N')
						list.add("neutro");
				}
				
				if (label.length()>=5){
					char numero = label.charAt(4);
					
					if (numero=='S')
						list.add("singular");
					else if (numero=='P')
						list.add("plural");
					else if (numero=='N')
						list.add("invariable");
				}
				
				if (label.length()>=6){
					char caso = label.charAt(5);
					
					if (caso=='N')
						list.add("nominativo");
					else if (caso=='A')
						list.add("acusativo");
					else if (caso=='D')
						list.add("dativo");
					else if (caso=='O')
						list.add("oblicuo");
				}
				
				if (label.length()>=7){
					char poseedor = label.charAt(6);
					
					if (poseedor=='S')
						list.add("poseedor=singular");
					else if (poseedor=='P')
						list.add("poseedor=plural");	
				}
				
				if (label.length()>=8){
					char politeness = label.charAt(7);
					
					if (politeness=='P')
						list.add("polite");
				}
				
				
				
				
			}else if (codigo=='R'){
				if (label.length()>=2){
					char tipo = label.charAt(1);
					if (tipo=='G')
						list.add("general");
					else if (tipo=='N')
						list.add("negativo");				
				}
				
			}else if (codigo=='S'){
				
				if (label.length()>=2){
					char tipo = label.charAt(1);
					
					if (tipo=='P')
						list.add("preposicion");
				}
				
				if (label.length()>=3){
					char forma = label.charAt(2);
					
					if (forma=='S')
						list.add("simple");
					else if (forma=='C')
						list.add("contraida");
				}
				
				if (label.length()>=4){
					char genero=label.charAt(3);
					
					if (genero=='M')
						list.add("masculino");
				}
				
				if (label.length()>=5){
					char numero=label.charAt(4);
					
					if (numero=='S')
						list.add("singular");
					
					if (lang.equals("Catalan"))
						if (numero=='P')
							list.add("plural");
				}
				
			}else if (codigo=='T'){
				
			}else if (codigo=='U'){
				
			}else if (codigo=='V'){
				
				if (label.length()>=2){
					char tipo = label.charAt(1);
					
					if (tipo=='M')
						list.add("principal");
					else if (tipo=='A')
						list.add("auxiliar");
					else if (tipo=='S')
						list.add("semiauxiliar");
				}
				
				if (label.length()>=3){
					char modo = label.charAt(2);
					
					if (modo=='I')
						list.add("indicativo");
					else if (modo=='S')
						list.add("subjuntivo");
					else if (modo=='M')
						list.add("imperativo");
					else if (modo=='N')
						list.add("infinitivo");
					else if (modo=='G')
						list.add("gerundio");
					else if (modo=='P')
						list.add("participio");
				}
				
				if (label.length()>=4){
					char tiempo = label.charAt(3);
					
					if (tiempo=='P')
						list.add("presente");
					else if (tiempo=='I')
						list.add("imperfecto");
					else if (tiempo=='F')
						list.add("futuro");
					else if (tiempo=='S')
						list.add("pasado");
					else if (tiempo=='C')
						list.add("condicional");
				}
				
				if (label.length()>=5){
					char persona = label.charAt(4);
					if (persona=='1')
						list.add("primera persona");
					else if (persona=='2')
						list.add("segunda persona");
					else if (persona=='3')
						list.add("tercera persona");
				}
				
				if (label.length()>=6){
					char numero = label.charAt(5);
					
					if (numero=='S')
						list.add("singular");
					else if (numero=='P')
						list.add("plural");
				}
				
				if (label.length()>=7){
					char genero = label.charAt(6);
					
					if (genero=='M')
						list.add("masculino");
					else if (genero=='F')
						list.add("femenino");
				}
				
			}else if (codigo=='W'){
				
			}else if (codigo=='X'){
				
			}else if (codigo=='Y'){
				
			}else if (codigo=='Z'){
				
				if ((label.length()>=2)&&(label.charAt(1)=='m'))
					list.add("moneda");
				
			}else if (codigo=='x'){
				
			}			
		}
		
		return list;
		
	}
	
}
