package com.oie;

import java.util.ArrayList;
import java.util.List;

import com.utilities.Triple;

import de.mpii.clausie.ClausIE;
import de.mpii.clausie.Proposition;

public class ClausIEImpl {
	
	public List<Triple> extractClausIETriples(String sentence, List<String> problematicSnts){
		List<Triple> listTriples = new ArrayList<Triple>();
		ClausIE clausIE = new ClausIE();
		clausIE.initParser();
		try {
			//logger.info("Sentence analyzed by ClausIE: " + sentence.getSentence());
			clausIE.parse(sentence);
			clausIE.detectClauses();
			clausIE.generatePropositions();
		} catch (StackOverflowError s) {
			problematicSnts.add(sentence + "********StackOverflowError");
			//logger.error("StackOverflowError.... in sentence: " + sentence);
			return null;
		} catch (NullPointerException e) {
			problematicSnts.add(sentence + "********NullPointerException");
			//logger.error("sentence :" + sentence + "--- give no clause");
			return null;
		} catch(Exception e) {
			problematicSnts.add(sentence + "********Exception");
			//logger.error("sentence :" + sentence + "--- give no clause");
			return null;
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
		
		return listTriples;
	}

}
