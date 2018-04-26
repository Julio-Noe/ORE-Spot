package com.utilities;

public class Report {
	private String file;
	private int numTriples;
	private int numSentences;
	
	public Report() {
		// TODO Auto-generated constructor stub
	}
	
	public String printReport() {
		return file +"\t" + numSentences + "\t" + numTriples;
	}


	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	public int getNumTriples() {
		return numTriples;
	}


	public void setNumTriples(int numTriples) {
		this.numTriples = numTriples;
	}


	public int getNumSentences() {
		return numSentences;
	}


	public void setNumSentences(int numSentences) {
		this.numSentences = numSentences;
	}

}
