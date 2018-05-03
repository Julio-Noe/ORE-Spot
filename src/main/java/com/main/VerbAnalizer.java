package com.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class VerbAnalizer {
	/*
	 * args[0] main results directory name
	 * args[1] comma separated datasets name
	 * 
	 */

	Properties props = new Properties();
	StanfordCoreNLP pipeline = new StanfordCoreNLP();
	
	public VerbAnalizer() {
		this.props.setProperty("annotators", "tokenize, pos, lemma, depparse");
	}
	
	public static void main(String args[]) throws IOException{
		VerbAnalizer va = new VerbAnalizer();
		
		String[] datasets = args[1].split(",");
		for(String d: datasets) {
			File verbDir = new File(args[0]+"/"+d+"/verbLists");
			for(File v : verbDir.listFiles()) {
				File output = new File(verbDir+"/main-" + v.getName());
				List<String> listLines = FileUtils.readLines(v);
				List<String> mainVerbs = new ArrayList<String>();
				for(String ll : listLines) {
					mainVerbs.add(va.verbProcessing(ll));
				}
				FileUtils.writeLines(output, mainVerbs);
			}
		}
		
	}
	
	public String verbProcessing(String verb) {
		String mainVerb = "";
		Annotation document = new Annotation(verb);
		pipeline.annotate(document);

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class);
			System.out.println(dependencies.toString());
			System.out.println(dependencies.getRoots());
			for(IndexedWord iw : dependencies.getRoots()) {
				System.out.println(iw.lemma() + "--" + iw.tag());
				mainVerb = iw.lemma() + "---" + iw.tag();
			}
		}
		return mainVerb;
	}
}
