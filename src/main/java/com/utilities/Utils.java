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

import com.main.VerbAnalizer;
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
	
	public String createOutput(String docName, String sentence, Sentence snt) {
		String output = docName + "\t" + sentence + "\n";
		
		for(RDFTriple r : snt.getRdf()) {
			output +="\t \t"+ r.getOre().toString() + "\t" +r.toString() + "\n";
		}
		
		return output;
	}
	
	public String createOutputOre(Sentence snt) {
		String output = "";
		
		for(RDFTriple r : snt.getRdf()) {
			output += r.toString() + "\n";
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
	
	public List<RDFTriple> extractRDFTriples(List<Triple> triples, List<NE> nes) {
		VerbAnalizer va = new VerbAnalizer();
		List<RDFTriple> rdf = new ArrayList<RDFTriple>();
		for(Triple t : triples) {
			NE o = null;
			RDFTriple trip = new RDFTriple();
			String v = "";
			NE s = lookForMention(t.getSubject(), nes);
			if(s != null) {
				o = lookForMention(t.getPredicate(),nes);
				if(o != null) {
					v = va.verbProcessing(t.getVerb());
					trip.setSubject(s.getMention());
					trip.setPredicate(v);
					trip.setObject(o.getMention());
					trip.setOre(t);
					rdf.add(trip);
				}
			}
		}
		return rdf;
		
	}
	
	public NE lookForMention(String part, List<NE> nes) {
		NE ne = null;
		for(NE n : nes) {
			if(part.contains(n.getMention())) {
				ne = n;
				break;
			}
		}
		return ne;
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
			int numRDFTriples = 0;
			pw.write("file \t numSentences \t numTriples \t num2Ne \t numNE \t numRDFTriples \n");
			for(NelReport r : rs) {
				numNe += r.getNumNe();
				numSentences += r.getNumSentences();
				num2Ne += r.getNum2Ne();
				numTriples += r.getNumTriples();
				numRDFTriples += r.getNumRDFTriples();
				pw.write(r.printReport() + "\n");
			}
			pw.write( "\t" + numSentences +  "\t" + numTriples +  "\t" + num2Ne +  "\t" + numNe + "\t"+ + numRDFTriples + "\n");
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
