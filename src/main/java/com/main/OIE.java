package com.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.oie.ClausIEImpl;
import com.oie.OllieImpl;
import com.oie.ReverbImpl;
import com.oie.StanfordOIEImpl;
import com.utilities.Report;
import com.utilities.Triple;
import com.utilities.Utils;

public class OIE {
	private static Logger logger = Logger.getLogger(OIE.class);
	/*
	 * args[0] - dataset folder
	 * args[1] - output folder
	 * args[3] - Extractor stanford,ollie,clausie,reverb
	 */

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File input = new File(args[0]);
		String outputFolder = args[1];
		String opt = args[2];
		OIE oie = new OIE();
		
		switch(opt) {
		case "stanford": oie.runStanford(input.listFiles(), outputFolder); 
			break;
		case "ollie": oie.runOllie(input.listFiles(), outputFolder);
			break;
		case "reverb": oie.runReverb(input.listFiles(), outputFolder);
			break;
		case "clausie": oie.runClausie(input.listFiles(), outputFolder);
			break;
			default: logger.error("No option: stanford, ollie, clausie or reverb");
		}
		
//		String sentence = "In European competitions, Jamie Carragher holds the club record for the most appearances, with 139, and Steven Gerrard is the club's record goalscorer, with 38 goals.";
		
//		cl.extractClausIETriples(sentence, problematicSnts);
//		soie.extractTSL(sentence);
//		reverb.reverb(sentence);
//		o.tripleExtractor(sentence);
		
	}
	
	public void runOllie(File[] listFiles, String outputFolder) throws IOException {
		Utils u = new Utils();
		List<Report> lr = new ArrayList<Report>();
		OllieImpl o = new OllieImpl();
		
		long initialGlobalTime = System.currentTimeMillis();
		for(File f : listFiles) {
			if(f.getName().endsWith(".txt")) {
				Report r = new Report();
				r.setFile(f.getName());
				logger.info("Processing file: " + f.getName());
				List<String> l = FileUtils.readLines(f);
				
				r.setNumSentences(l.size());
				List<String> list = new ArrayList<String>();
				
				int counter = 0;
				long initialTime = System.currentTimeMillis();
				for(String s : l) {
					List<Triple> t = new ArrayList<Triple> (o.extractTriples(s));
					String oOutput = u.createOutput(s, t);
					list.add(oOutput);
					counter += t.size();
				}
				long endTime = System.currentTimeMillis() - initialTime;
				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
						TimeUnit.MILLISECONDS.toSeconds(endTime)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
				logger.info("Ollie - "+timeElapsed);
				list.add(timeElapsed);
				u.writeDocumentTriples(new File(outputFolder+"ollie/" + f.getName() + ".tsv"), list);
				r.setNumTriples(counter);
				lr.add(r);
			}
			
		}
		
		long endTime = System.currentTimeMillis() - initialGlobalTime;
		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
				TimeUnit.MILLISECONDS.toSeconds(endTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
		logger.info("ClausIE - "+timeElapsed);
		u.writeReport(new File(outputFolder+"report/Ollie-Report.tsv"),lr, timeElapsed);
	}
	
	public void runStanford(File[] listFiles, String outputFolder) throws IOException {
		Utils u = new Utils();
		List<Report> lr = new ArrayList<Report>();
		StanfordOIEImpl soie = new StanfordOIEImpl();
		
		long initialGlobalTime = System.currentTimeMillis();
		for(File f : listFiles) {
			if(f.getName().endsWith(".txt")) {
				Report r = new Report();
				r.setFile(f.getName());
				logger.info("Processing file: " + f.getName());
				List<String> l = FileUtils.readLines(f);
				
				r.setNumSentences(l.size());
				List<String> list = new ArrayList<String>();
				
				int counter = 0;
				long initialTime = System.currentTimeMillis();
				for(String s : l) {
					List<Triple> t = new ArrayList<Triple>(soie.extractTriples(s));
					String stanfordOutput = u.createOutput(s, t);
					list.add(stanfordOutput);
					
					counter += t.size();
				}
				long endTime = System.currentTimeMillis() - initialTime;
				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
						TimeUnit.MILLISECONDS.toSeconds(endTime)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
				logger.info("Stanford - "+timeElapsed);
				list.add(timeElapsed);

				u.writeDocumentTriples(new File(outputFolder+"stanford/" + f.getName() + ".tsv"), list);
				r.setNumTriples(counter);
				lr.add(r);
			}
			
		}
		long endTime = System.currentTimeMillis() - initialGlobalTime;
		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
				TimeUnit.MILLISECONDS.toSeconds(endTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
		logger.info("ClausIE - "+timeElapsed);
		u.writeReport(new File(outputFolder+"report/Stanford-Report.tsv"),lr, timeElapsed);
	}
	
	public void runReverb(File[] listFiles, String outputFolder) throws IOException {
		Utils u = new Utils();
		List<Report> lr = new ArrayList<Report>();
		ReverbImpl reverb = new ReverbImpl();
		
		long initialGlobalTime = System.currentTimeMillis();
		for(File f : listFiles) {
			if(f.getName().endsWith(".txt")) {
				Report r = new Report();
				r.setFile(f.getName());
				logger.info("Processing file: " + f.getName());
				List<String> l = FileUtils.readLines(f);
				
				r.setNumSentences(l.size());
				List<String> list = new ArrayList<String>();
				
				int counter = 0;
				long initialTime = System.currentTimeMillis();
				for(String s : l) {
					List<Triple> t = new ArrayList<Triple> (reverb.extractTriples(s));
					String reverbOutput = u.createOutput(s, t);
					list.add(reverbOutput);
					
					counter += t.size();
				}
				
				long endTime = System.currentTimeMillis() - initialTime;
				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
						TimeUnit.MILLISECONDS.toSeconds(endTime)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
				logger.info("Reverb - "+timeElapsed);
				list.add(timeElapsed);
				
				u.writeDocumentTriples(new File(outputFolder+"reverb/" + f.getName() + ".tsv"), list);
				r.setNumTriples(counter);
				lr.add(r);
			}
			
			
		}
		long endTime = System.currentTimeMillis() - initialGlobalTime;
		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
				TimeUnit.MILLISECONDS.toSeconds(endTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
		logger.info("ClausIE - "+timeElapsed);
		u.writeReport(new File(outputFolder+"report/Reverb-Report.tsv"),lr, timeElapsed);
	}
	
	public void runClausie(File[] listFiles, String outputFolder) throws IOException {
		Utils u = new Utils();
		List<Report> lr = new ArrayList<Report>();
		ClausIEImpl cl = new ClausIEImpl();
//		List<String> problematicSnts = new ArrayList<String>();
		
		long initialGlobalTime = System.currentTimeMillis();
		for(File f : listFiles) {
			if(f.getName().endsWith(".txt")) {
				Report r = new Report();
				r.setFile(f.getName());
				logger.info("Processing file: " + f.getName());
				List<String> l = FileUtils.readLines(f);
				
				r.setNumSentences(l.size());
				List<String> list = new ArrayList<String>();
				
				int counter = 0;
				long initialTime = System.currentTimeMillis();
				for(String s : l) {
					List<Triple> t = new ArrayList<Triple>(cl.extractTriples(s));
					String clOutput = u.createOutput(s, t);
					list.add(clOutput);
					
					counter += t.size();
				}
				
				long endTime = System.currentTimeMillis() - initialTime;
				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
						TimeUnit.MILLISECONDS.toSeconds(endTime)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
				logger.info("ClausIE - "+timeElapsed);
				list.add(timeElapsed);
				
				u.writeDocumentTriples(new File(outputFolder+"clausie/" + f.getName() + ".tsv"), list);
				r.setNumTriples(counter);
				lr.add(r);
			}
			
		}
		long endTime = System.currentTimeMillis() - initialGlobalTime;
		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
				TimeUnit.MILLISECONDS.toSeconds(endTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
		logger.info("ClausIE - "+timeElapsed);
		u.writeReport(new File(outputFolder+"report/Clausie-Report.tsv"),lr, timeElapsed);
	}
	

}
