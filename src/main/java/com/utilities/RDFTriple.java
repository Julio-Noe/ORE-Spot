package com.utilities;

public class RDFTriple {
	
	private String subject;
	private String predicate;
	private String object;
	private Triple ore;
	@Override
	public String toString() {
		return subject  + "\t" + predicate +"\t" + object;
		
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}

	public Triple getOre() {
		return ore;
	}

	public void setOre(Triple ore) {
		this.ore = ore;
	}
	
	public boolean repeated(RDFTriple r2) {
		if(this.subject.equals(r2.subject))
			if(this.predicate.equals(r2.predicate))
				if(this.object.equals(r2.object))
					return true;
		return false;

	}
	
	

}
