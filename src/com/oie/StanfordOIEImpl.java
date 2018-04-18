package com.oie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.utilities.Triple;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordOIEImpl {
	
	public void extractTSL(String doc) throws IOException{
		Annotation document = new Annotation(doc);
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, depparse,natlog,openie");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		pipeline.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		int i = 0;
		for (CoreMap sentence : sentences) {
//			docSentences.add(sentence.toString());
			List<Triple> listTriples = new ArrayList<Triple>();
			Collection<RelationTriple> triplesOIE = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

			for (RelationTriple tripleOIE : triplesOIE) {
				Triple triple = new Triple();
				triple.setExtractor("openie");
				triple.setSubject(tripleOIE.subjectLemmaGloss());
				triple.setVerb(tripleOIE.relationLemmaGloss());
				triple.setPredicate(tripleOIE.objectLemmaGloss());
				triple.setConfidence(tripleOIE.confidence);
				// logger.info(triple.toString());
				listTriples.add(triple);
				System.out.println(triple.toString());
			}
		}
	}

}
