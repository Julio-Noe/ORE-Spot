package com.utilities;

import java.util.ArrayList;
import java.util.List;

import com.nel.NE;

public class Sentence {
	
	private String sentence;
	private List<Triple> triples;
	private List<NE> nes;
	private List<RDFTriple> rdf;
	
	public Sentence() {
		this.triples = new ArrayList<Triple>();
		this.nes = new ArrayList<NE>();
		this.rdf = new ArrayList<RDFTriple>();
	}
	
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public List<Triple> getTriples() {
		return triples;
	}
	public void setTriples(List<Triple> triples) {
		this.triples = triples;
	}
	public List<NE> getNes() {
		return nes;
	}
	public void setNes(List<NE> nes) {
		this.nes = nes;
	}
	public List<RDFTriple> getRdf() {
		return rdf;
	}
	public void setRdf(List<RDFTriple> rdf) {
		this.rdf = rdf;
	}
	

}
