package com.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.oie.ClausIEImpl;
import com.oie.OllieImpl;
import com.oie.ReverbImpl;
import com.oie.StanfordOIEImpl;

public class OIE {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ClausIEImpl cl = new ClausIEImpl();
		StanfordOIEImpl soie = new StanfordOIEImpl();
		ReverbImpl reverb = new ReverbImpl();
		OllieImpl o = new OllieImpl();
		String sentence = "In European competitions, Jamie Carragher holds the club record for the most appearances, with 139, and Steven Gerrard is the club's record goalscorer, with 38 goals.";
		List<String> problematicSnts = new ArrayList<String>();

		
//		cl.extractClausIETriples(sentence, problematicSnts);
//		soie.extractTSL(sentence);
//		reverb.reverb(sentence);
		o.tripleExtractor(sentence);
	}

}
