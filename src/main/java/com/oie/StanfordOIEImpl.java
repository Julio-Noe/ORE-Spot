package com.oie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.utilities.Report;
import com.utilities.Triple;
import com.utilities.Utils;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class StanfordOIEImpl {
	
	private Properties props;
	private StanfordCoreNLP pipeline;
	
	public StanfordOIEImpl() {
		this.props  = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, depparse,natlog,openie");
		pipeline = new StanfordCoreNLP(props);
	}
	
	public void extractTSL(String doc, File output, Report r) throws IOException{
		Utils u = new Utils();
		List<String> listSentences = new ArrayList<String>();
		Annotation document = new Annotation(doc);
		pipeline.annotate(document);
		List<String> outputLines = new ArrayList<String>();
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		int i = 0;
		int counterTriples = 0;
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
//				System.out.println(triple.toString());
			}
			counterTriples += listTriples.size();
			String toWrite = u.createOutput(sentence.toString(),listTriples);
			outputLines.add(toWrite);
		}
		r.setStanfordTriples(counterTriples);
		u.writeDocumentTriples(output, outputLines);
	}
	
	public List<Triple> extractTSL(String s) throws IOException{
		Utils u = new Utils();
		Annotation document = new Annotation(s);
		pipeline.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		List<Triple> listTriples = new ArrayList<Triple>();
		for (CoreMap sentence : sentences) {
//			docSentences.add(sentence.toString());
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
//				System.out.println(triple.toString());
			}
		}
		return listTriples;
	}

}
