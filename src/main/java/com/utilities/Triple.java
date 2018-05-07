package com.utilities;

import com.main.VerbAnalizer;

public class Triple {
	
	private String extractor;
	private String subject;
	private String verb;
	private String mainVerb;
	private String predicate;
	
	private double confidence;
	
	public Triple() {
		
	}
	
	@Override
    public String toString() {
        return "\""+subject+" , "+verb+" , "+predicate + "\"";
    }

	@Override
	public boolean equals(Object other){
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof Triple)) return false;
		Triple otherTriple = (Triple) other;
		if (otherTriple.getConfidence() == this.getConfidence() && 
				otherTriple.getSubject().equalsIgnoreCase(this.getSubject()) &&
				otherTriple.getPredicate().equals(this.getPredicate()) &&
				otherTriple.getVerb().equals(this.getVerb()))
			return true;
		return false;
	}
	
	public int compareTo(Triple o) {
		int returnValue = 0;
		if(this.getConfidence() > o.getConfidence()){
			returnValue = -1;
		}else if(this.getConfidence() < o.getConfidence()){
			returnValue = 1;
		}else if(this.getConfidence() == o.getConfidence()){
			returnValue = 0;
		}
		return returnValue;
	}

	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		VerbAnalizer v = new VerbAnalizer();
		this.verb = verb;
		this.setMainVerb(v.verbProcessing(this.verb));
		
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	
	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public String getExtractor() {
		return extractor;
	}

	public void setExtractor(String extractor) {
		this.extractor = extractor;
	}

	public String getMainVerb() {
		return mainVerb;
	}

	public void setMainVerb(String mainVerb) {
		this.mainVerb = mainVerb;
	}

}
