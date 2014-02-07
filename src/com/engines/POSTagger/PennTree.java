package com.engines.POSTagger;

import java.util.Vector;

public class PennTree {
	
	public Vector getFeatures(String lang, String label){
		
		Vector list= new Vector();
		if (lang.equals("Ingles")){
			if (label.equals("CC"))
				list.add("coordinating conjunction");
			else if (label.equals("CD"))
				list.add("cardinal number");
			else if (label.equals("DT"))
				list.add("determiner");
			else if (label.equals("EX"))
				list.add("existential there");
			else if (label.equals("FW"))
				list.add("foreign word");
			else if (label.equals("IN"))
				list.add("preposition/subordinating conjunction");
			else if (label.equals("JJ"))
				list.add("adjective");
			else if (label.equals("JJR")){
				list.add("adjective");
				list.add("comparative");
			}else if (label.equals("JJS")){
				list.add("adjective");
				list.add("superlative");
			}else if (label.equals("LS"))
				list.add("list marker");
			else if (label.equals("MD"))
				list.add("modal");	
			else if (label.equals("NN")){
				list.add("noun");
				list.add("singular or mass");
			}else if (label.equals("NNS")){
				list.add("noun plural");				
			}else if (label.equals("NNP")){
				list.add("proper noun");
				list.add("singular");
			}else if (label.equals("NNPS")){
				list.add("proper noun");
				list.add("plural");
			}else if (label.equals("PDT")){
				list.add("predeterminer");				
			}else if (label.equals("POS")){
				list.add("possessive ending");				
			}else if (label.equals("PRP")){
				list.add("personal pronoun");				
			}else if (label.equals("PRP$")){
				list.add("possessive pronoun");				
			}else if (label.equals("RB")){
				list.add("adverb");				
			}else if (label.equals("RBR")){
				list.add("adverb");				
				list.add("comparative");
			}else if (label.equals("RBS")){
				list.add("adverb");				
				list.add("superlative");
			}else if (label.equals("RP")){
				list.add("particle");		
			}else if (label.equals("TO")){
				list.add("to");			
			}else if (label.equals("UH")){
				list.add("interjection");				
			}else if (label.equals("VB")){
				list.add("verb");				
				list.add("base form");
			}else if (label.equals("VBD")){
				list.add("verb");				
				list.add("past tense");
			}else if (label.equals("VBG")){
				list.add("verb");				
				list.add("gerund/present participle");
			}else if (label.equals("VBN")){
				list.add("verb");				
				list.add("past participle");
			}else if (label.equals("VBP")){
				list.add("verb");				
				list.add("sing.");
				list.add("present");
				list.add("non-3d");
			}else if (label.equals("VBZ")){
				list.add("verb");				
				list.add("3d person");
				list.add("sing.");
				list.add("present");
			}else if (label.equals("WDT")){
				list.add("wh-determiner");			
			}else if (label.equals("WP")){
				list.add("wh-pronoun");				
			}else if (label.equals("WP$")){
				list.add("possessive wh-pronoun");				
			}else if (label.equals("WRB")){
				list.add("wh-abverb");				
			}
			
			
		}
		
		return list;
	}
}
