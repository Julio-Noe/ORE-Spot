package com.nel;

import java.util.ArrayList;
import java.util.List;

public class NE {

	private String mention;
	private String neClass;
	private int begin;
	private int end;
	private double taConfidence;
	private String taAnnotatorRef;
	private String taIdentRef; 
	private List<String> taClassRef;

	public NE() {
		this.taClassRef = new ArrayList<String>();
	}
	
	public String printNE() {
		return mention + "\t" + begin + "-" + end + "\t" + taConfidence + "\t" + taIdentRef + "\t" + taClassRef;
	}
	
	public String getMention() {
		return mention;
	}

	public void setMention(String mention) {
		this.mention = mention;
	}

	public String getNeClass() {
		return neClass;
	}

	public void setNeClass(String neClass) {
		this.neClass = neClass;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public double getTaConfidence() {
		return taConfidence;
	}

	public void setTaConfidence(double taConfidence) {
		this.taConfidence = taConfidence;
	}

	public String getTaAnnotatorRef() {
		return taAnnotatorRef;
	}

	public void setTaAnnotatorRef(String taAnnotatorRef) {
		this.taAnnotatorRef = taAnnotatorRef;
	}

	public String getTaIdentRef() {
		return taIdentRef;
	}

	public void setTaIdentRef(String taIdentRef) {
		this.taIdentRef = taIdentRef;
	}

	public List<String> getTaClassRef() {
		return taClassRef;
	}

	public void setTaClassRef(List<String> taClassRef) {
		this.taClassRef.addAll(taClassRef);
	}

}