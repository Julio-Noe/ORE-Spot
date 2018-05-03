package com.oie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.utilities.Triple;

import edu.washington.cs.knowitall.extractor.ReVerbExtractor;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunctionException;
import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;
import edu.washington.cs.knowitall.nlp.ChunkedSentence;
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class ReverbImpl implements OIETools{
	
	public List<Triple> extractTriples(String sentStr) {
//		String sentStr = "Michael McGinn is the mayor of Seattle.";
		List<Triple> revTriples = new ArrayList<Triple>();
        // Looks on the classpath for the default model files.
        OpenNlpSentenceChunker chunker = null;
		try {
			chunker = new OpenNlpSentenceChunker();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ChunkedSentence sent = chunker.chunkSentence(sentStr);

        // Prints out the (token, tag, chunk-tag) for the sentence
//        System.out.println(sentStr);
        for (int i = 0; i < sent.getLength(); i++) {
            String token = sent.getToken(i);
            String posTag = sent.getPosTag(i);
            String chunkTag = sent.getChunkTag(i);
//            System.out.println(token + " " + posTag + " " + chunkTag);
        }

        // Prints out extractions from the sentence.
        ReVerbExtractor reverb = new ReVerbExtractor();
        ConfidenceFunction confFunc = null;
		try {
			confFunc = new ReVerbOpenNlpConfFunction();
		} catch (ConfidenceFunctionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<Triple> lt = new ArrayList<Triple>();
        for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
        		Triple t = new Triple();
        		
            double conf = confFunc.getConf(extr);
//            System.out.println("Arg1=" + extr.getArgument1());
//            System.out.println("Rel=" + extr.getRelation());
//            System.out.println("Arg2=" + extr.getArgument2());
//            System.out.println("Conf=" + conf);
            t.setExtractor("Reverb");
            t.setConfidence(conf);
            t.setSubject(extr.getArgument1().getText());
            t.setVerb(extr.getRelation().getText());
            t.setPredicate(extr.getArgument2().getText());
            revTriples.add(t);
//            System.out.println(t.toString());
        }
        return revTriples;

	}

}
