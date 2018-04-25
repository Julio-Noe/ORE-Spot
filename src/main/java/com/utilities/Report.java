package com.utilities;

public class Report {
	private String file;
	private int stanfordTriples;
	private int clausieTriples;
	private int reverbTriples;
	private int ollieTriples;
	private int numSentences;
	
	public Report() {
		// TODO Auto-generated constructor stub
	}
	
	public String printReport() {
		return file +"\t" + numSentences + "\t" + stanfordTriples + "\t" + clausieTriples + "\t" + reverbTriples + "\t" + ollieTriples;
	}


	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	public int getStanfordTriples() {
		return stanfordTriples;
	}


	public void setStanfordTriples(int stanfordTriples) {
		this.stanfordTriples = stanfordTriples;
	}


	public int getClausieTriples() {
		return clausieTriples;
	}


	public void setClausieTriples(int clausieTriples) {
		this.clausieTriples = clausieTriples;
	}


	public int getReverbTriples() {
		return reverbTriples;
	}


	public void setReverbTriples(int reverbTriples) {
		this.reverbTriples = reverbTriples;
	}


	public int getOllieTriples() {
		return ollieTriples;
	}


	public void setOllieTriples(int ollieTriples) {
		this.ollieTriples = ollieTriples;
	}


	public int getNumSentences() {
		return numSentences;
	}


	public void setNumSentences(int numSentences) {
		this.numSentences = numSentences;
	}

}
