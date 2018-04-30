package com.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.nel.NE;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	
	public void writeDocumentTriples(File output, List<String> lines) throws IOException {
		FileUtils.writeLines(output, lines, true);
	}
	
	public void writeAllDocumentTriples() {
		
	}
	
	public String createOutput(String sentence, List<Triple> triples) {
		String output = sentence + "\t";
		
		for(Triple t : triples) {
			output += t.toString() + "\n\t";
		}
		return output;
	}
	
	public String createNelOutput(String sentence, List<NE> nes) {
		String output = sentence + "\t";
		
		for(int i = 0; i < nes.size() ; i++) {
			int next = i + 1;
			if(next < nes.size() && nes.get(i).getEnd() < nes.get(next).getBegin())
				output += nes.get(i).getMention() + "," +sentence.substring(nes.get(i).getEnd(),nes.get(++i).getBegin()) + "," +nes.get(i).getMention() + "\n\t";
		}
		return output;
	}
	
	
	public void writeReport(File output, List<Report> rs, String timeElapsed) {
		try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output)))){
			for(Report r : rs) {
				pw.write(r.printReport() + "\n");
			}
			pw.write(timeElapsed);
			pw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeNelReport(File output, List<NelReport> rs, String timeElapsed) {
		try(PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output)))){
			int numNe = 0;
			int numSentences = 0;
			int num2Ne = 0;
			int numTriples = 0;
			for(NelReport r : rs) {
				numNe += r.getNumNe();
				numSentences += r.getNumSentences();
				num2Ne += r.getNum2Ne();
				numTriples += r.getNumTriples();
				pw.write(r.printReport() + "\n");
			}
			pw.write( "\t" + numSentences +  "\t" + numTriples +  "\t" + num2Ne +  "\t" + numNe + "\n");
			pw.write(timeElapsed);
			pw.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> extractSentences(String doc){
		List<String> listSentences = new ArrayList<String>();
		Annotation document = new Annotation(doc);
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit");
		StanfordCoreNLP pipeline = new StanfordCoreNLP();
		pipeline.annotate(document);
		
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		for (CoreMap sentence : sentences) {
			listSentences.add(sentence.toString());
		}
		return listSentences;
	}

}
