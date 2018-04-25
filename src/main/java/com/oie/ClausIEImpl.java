package com.oie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.utilities.Triple;
import com.utilities.Utils;

import de.mpii.clausie.ClausIE;
import de.mpii.clausie.Proposition;

public class ClausIEImpl {
	
	
	private ClausIE clausIE;
	public ClausIEImpl() {
		clausIE = new ClausIE();
		clausIE.initParser();
	}
	
	public List<Triple> extractClausIETriples(String sentence, List<String> problematicSnts){
		List<String> outputLines = new ArrayList<String>();
		List<Triple> listTriples = new ArrayList<Triple>();
		try {
			//logger.info("Sentence analyzed by ClausIE: " + sentence.getSentence());
			clausIE.parse(sentence);
			clausIE.detectClauses();
			clausIE.generatePropositions();
		} catch (StackOverflowError s) {
			problematicSnts.add(sentence + "********StackOverflowError");
			System.out.println("StackOverflowError");
			//logger.error("StackOverflowError.... in sentence: " + sentence);
			return listTriples;
		} catch (NullPointerException e) {
			problematicSnts.add(sentence + "********NullPointerException");
			System.out.println("NullPointerException");
			//logger.error("sentence :" + sentence + "--- give no clause");
			return listTriples;
		} catch(Exception e) {
			
			problematicSnts.add(sentence + "********Exception");
			System.out.println("Exception");
			//logger.error("sentence :" + sentence + "--- give no clause");
			return listTriples;
		}
		//logger.info("Extracting ClausIE triples");
		for(Proposition proposition: clausIE.getPropositions()) {
			if(proposition.noArguments() > 0) {
				Triple triple = new Triple();
				triple.setExtractor("clausie");
				triple.setSubject(proposition.subject());
				triple.setVerb(proposition.relation());
				triple.setPredicate(proposition.argument(0));
				//triple.setConfidence(clausIE.lpq.getPCFGScore());
				//logger.info(triple.toString());
				listTriples.add(triple);
//				System.out.println(proposition.toString());
			}
		}
		System.out.println("[ClausIE] - Triples returned = " + listTriples.size());
		return listTriples;
	}

}
