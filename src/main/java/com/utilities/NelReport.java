package com.utilities;

public class NelReport {

	private String file;
	private int numTriples;
	private int numNe;
	private int num2Ne;
	private int numSentences;
	private int numRDFTriples;

	public NelReport() {
		// TODO Auto-generated constructor stub
	}

	public String printReport() {
		return file + "\t" + numSentences + "\t" + numTriples + "\t" + num2Ne + "\t" + numNe + "\t" + numRDFTriples;
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

	public int getNumNe() {
		return numNe;
	}

	public void setNumNe(int numNe) {
		this.numNe = numNe;
	}

	public int getNum2Ne() {
		return num2Ne;
	}

	public void setNum2Ne(int num2Ne) {
		this.num2Ne = num2Ne;
	}

	public int getNumRDFTriples() {
		return numRDFTriples;
	}

	public void setNumRDFTriples(int numRDFTriples) {
		this.numRDFTriples = numRDFTriples;
	}

}
