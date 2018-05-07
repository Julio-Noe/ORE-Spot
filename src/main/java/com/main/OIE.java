package com.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.nel.NEL;
import com.oie.ClausIEImpl;
import com.oie.OIETools;
import com.oie.OllieImpl;
import com.oie.ReverbImpl;
import com.oie.StanfordOIEImpl;
import com.utilities.NelReport;
import com.utilities.Report;
import com.utilities.Sentence;
import com.utilities.Triple;
import com.utilities.Utils;

public class OIE {
	private static Logger logger = Logger.getLogger(OIE.class);
	/*
	 * args[0] - dataset folder
	 * args[1] - output folder
	 * args[3] - Extractor stanford,ollie,clausie,reverb
	 */

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File input = new File(args[0]);
		String outputFolder = args[1];
		String opt = args[2];
		OIE oie = new OIE();
		
//		switch(opt) {
//		case "stanford": oie.runStanford(input.listFiles(), outputFolder); 
//			break;
//		case "ollie": oie.runOllie(input.listFiles(), outputFolder);
//			break;
//		case "reverb": oie.runReverb(input.listFiles(), outputFolder);
//			break;
//		case "clausie": oie.runClausie(input.listFiles(), outputFolder);
//			break;
//			default: logger.error("No option: stanford, ollie, clausie or reverb");
//		}
		
		
		switch(opt) {
		case "stanford": oie.runOIE(input.listFiles(), outputFolder,new StanfordOIEImpl(), opt); 
			break;
		case "ollie": oie.runOIE(input.listFiles(), outputFolder, new OllieImpl(), opt);
			break;
		case "reverb": oie.runOIE(input.listFiles(), outputFolder, new ReverbImpl(), opt);
			break;
		case "clausie": oie.runOIE(input.listFiles(), outputFolder, new ClausIEImpl(), opt);
			break;
			default: logger.error("No option: stanford, ollie, clausie or reverb");
		}
		
//		String sentence = "In European competitions, Jamie Carragher holds the club record for the most appearances, with 139, and Steven Gerrard is the club's record goalscorer, with 38 goals.";
		
//		cl.extractClausIETriples(sentence, problematicSnts);
//		soie.extractTSL(sentence);
//		reverb.reverb(sentence);
//		o.tripleExtractor(sentence);
		
	}
	
	public void runOIE(File[] listFiles, String outputFolder, OIETools o, String oieToolName) throws Exception {
		long initialGlobalTime = System.currentTimeMillis();
		Utils u = new Utils();
		List<NelReport> lr = new ArrayList<NelReport>();
		List<String> globalOutput = new ArrayList<String>();
		for(File f : listFiles) {
			if(f.getName().endsWith(".txt")) {
				NelReport r = new NelReport();
				r.setFile(f.getName());
				logger.info("Processing file: " + f.getName());
				List<String> l = FileUtils.readLines(f);
				
				r.setNumSentences(l.size());
				List<String> list = new ArrayList<String>();
				
				long initialTime = System.currentTimeMillis();
				
				//The block
				int numNes = 0;
				int num2Ne = 0;
				int numRdfTrip = 0;
				int numTrip = 0;
				NEL nel = new NEL();
				for(String s : l) {
					Sentence snt = new Sentence();
					
					snt.setTriples(o.extractTriples(s));
					snt.setNes(nel.extractEntitiesSpotlight(s, "0.7"));
					if(snt.getNes().size() > 1) {
						num2Ne += snt.getNes().size();
						snt.setRdf(u.extractRDFTriples(snt.getTriples(), snt.getNes()));
					}
					
					String oOutput = u.createOutput(f.getName(),s, snt);
					list.add(oOutput);
					numTrip += snt.getTriples().size();
					numNes += snt.getNes().size();
					numRdfTrip += snt.getRdf().size();
					
				}
				
				r.setNumNe(numNes);
				r.setNum2Ne(num2Ne);
				r.setNumTriples(numTrip);
				r.setNumRDFTriples(numRdfTrip);
				r.setNumTriples(numTrip);
				long endTime = System.currentTimeMillis() - initialTime;
				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
						TimeUnit.MILLISECONDS.toSeconds(endTime)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
				logger.info(oieToolName + " - "+timeElapsed);
				list.add(timeElapsed);
				u.writeDocumentTriples(new File(outputFolder+"/"+oieToolName+ "/" + f.getName() + ".tsv"), list);
				globalOutput.addAll(list);
				lr.add(r);
			}
		}
		long endTime = System.currentTimeMillis() - initialGlobalTime;
		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
				TimeUnit.MILLISECONDS.toSeconds(endTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
		logger.info("ClausIE - "+timeElapsed);
		u.writeDocumentTriples(new File(outputFolder+"/Global-"+oieToolName + ".tsv"), globalOutput);
		u.writeNelReport(new File(outputFolder+"report/" + oieToolName + "-Report.tsv"),lr, timeElapsed);
		
	}
	
//	public void runOllie(File[] listFiles, String outputFolder) throws IOException {
//		Utils u = new Utils();
//		List<Report> lr = new ArrayList<Report>();
//		OllieImpl o = new OllieImpl();
//		
//		long initialGlobalTime = System.currentTimeMillis();
//		for(File f : listFiles) {
//			if(f.getName().endsWith(".txt")) {
//				Report r = new Report();
//				r.setFile(f.getName());
//				logger.info("Processing file: " + f.getName());
//				List<String> l = FileUtils.readLines(f);
//				
//				r.setNumSentences(l.size());
//				List<String> list = new ArrayList<String>();
//				
//				int counter = 0;
//				long initialTime = System.currentTimeMillis();
//				for(String s : l) {
//					List<Triple> t = new ArrayList<Triple> (o.extractTriples(s));
//					String oOutput = u.createOutput(s, t);
//					list.add(oOutput);
//					counter += t.size();
//				}
//				long endTime = System.currentTimeMillis() - initialTime;
//				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
//						TimeUnit.MILLISECONDS.toSeconds(endTime)
//								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//				logger.info("Ollie - "+timeElapsed);
//				list.add(timeElapsed);
//				u.writeDocumentTriples(new File(outputFolder+"ollie/" + f.getName() + ".tsv"), list);
//				r.setNumTriples(counter);
//				lr.add(r);
//			}
//			
//		}
//		
//		long endTime = System.currentTimeMillis() - initialGlobalTime;
//		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
//				TimeUnit.MILLISECONDS.toSeconds(endTime)
//						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//		logger.info("ClausIE - "+timeElapsed);
//		u.writeReport(new File(outputFolder+"report/Ollie-Report.tsv"),lr, timeElapsed);
//	}
//	
//	public void runStanford(File[] listFiles, String outputFolder) throws IOException {
//		Utils u = new Utils();
//		List<Report> lr = new ArrayList<Report>();
//		StanfordOIEImpl soie = new StanfordOIEImpl();
//		
//		long initialGlobalTime = System.currentTimeMillis();
//		for(File f : listFiles) {
//			if(f.getName().endsWith(".txt")) {
//				Report r = new Report();
//				r.setFile(f.getName());
//				logger.info("Processing file: " + f.getName());
//				List<String> l = FileUtils.readLines(f);
//				
//				r.setNumSentences(l.size());
//				List<String> list = new ArrayList<String>();
//				
//				int counter = 0;
//				long initialTime = System.currentTimeMillis();
//				for(String s : l) {
//					List<Triple> t = new ArrayList<Triple>(soie.extractTriples(s));
//					String stanfordOutput = u.createOutput(s, t);
//					list.add(stanfordOutput);
//					
//					counter += t.size();
//				}
//				long endTime = System.currentTimeMillis() - initialTime;
//				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
//						TimeUnit.MILLISECONDS.toSeconds(endTime)
//								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//				logger.info("Stanford - "+timeElapsed);
//				list.add(timeElapsed);
//
//				u.writeDocumentTriples(new File(outputFolder+"stanford/" + f.getName() + ".tsv"), list);
//				r.setNumTriples(counter);
//				lr.add(r);
//			}
//			
//		}
//		long endTime = System.currentTimeMillis() - initialGlobalTime;
//		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
//				TimeUnit.MILLISECONDS.toSeconds(endTime)
//						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//		logger.info("ClausIE - "+timeElapsed);
//		u.writeReport(new File(outputFolder+"report/Stanford-Report.tsv"),lr, timeElapsed);
//	}
//	
//	public void runReverb(File[] listFiles, String outputFolder) throws IOException {
//		Utils u = new Utils();
//		List<Report> lr = new ArrayList<Report>();
//		ReverbImpl reverb = new ReverbImpl();
//		
//		long initialGlobalTime = System.currentTimeMillis();
//		for(File f : listFiles) {
//			if(f.getName().endsWith(".txt")) {
//				Report r = new Report();
//				r.setFile(f.getName());
//				logger.info("Processing file: " + f.getName());
//				List<String> l = FileUtils.readLines(f);
//				
//				r.setNumSentences(l.size());
//				List<String> list = new ArrayList<String>();
//				
//				int counter = 0;
//				long initialTime = System.currentTimeMillis();
//				for(String s : l) {
//					List<Triple> t = new ArrayList<Triple> (reverb.extractTriples(s));
//					String reverbOutput = u.createOutput(s, t);
//					list.add(reverbOutput);
//					
//					counter += t.size();
//				}
//				
//				long endTime = System.currentTimeMillis() - initialTime;
//				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
//						TimeUnit.MILLISECONDS.toSeconds(endTime)
//								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//				logger.info("Reverb - "+timeElapsed);
//				list.add(timeElapsed);
//				
//				u.writeDocumentTriples(new File(outputFolder+"reverb/" + f.getName() + ".tsv"), list);
//				r.setNumTriples(counter);
//				lr.add(r);
//			}
//			
//			
//		}
//		long endTime = System.currentTimeMillis() - initialGlobalTime;
//		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
//				TimeUnit.MILLISECONDS.toSeconds(endTime)
//						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//		logger.info("ClausIE - "+timeElapsed);
//		u.writeReport(new File(outputFolder+"report/Reverb-Report.tsv"),lr, timeElapsed);
//	}
//	
//	public void runClausie(File[] listFiles, String outputFolder) throws IOException {
//		Utils u = new Utils();
//		List<Report> lr = new ArrayList<Report>();
//		ClausIEImpl cl = new ClausIEImpl();
////		List<String> problematicSnts = new ArrayList<String>();
//		
//		long initialGlobalTime = System.currentTimeMillis();
//		for(File f : listFiles) {
//			if(f.getName().endsWith(".txt")) {
//				Report r = new Report();
//				r.setFile(f.getName());
//				logger.info("Processing file: " + f.getName());
//				List<String> l = FileUtils.readLines(f);
//				
//				r.setNumSentences(l.size());
//				List<String> list = new ArrayList<String>();
//				
//				int counter = 0;
//				long initialTime = System.currentTimeMillis();
//				for(String s : l) {
//					List<Triple> t = new ArrayList<Triple>(cl.extractTriples(s));
//					String clOutput = u.createOutput(s, t);
//					list.add(clOutput);
//					
//					counter += t.size();
//				}
//				
//				long endTime = System.currentTimeMillis() - initialTime;
//				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
//						TimeUnit.MILLISECONDS.toSeconds(endTime)
//								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//				logger.info("ClausIE - "+timeElapsed);
//				list.add(timeElapsed);
//				
//				u.writeDocumentTriples(new File(outputFolder+"clausie/" + f.getName() + ".tsv"), list);
//				r.setNumTriples(counter);
//				lr.add(r);
//			}
//			
//		}
//		long endTime = System.currentTimeMillis() - initialGlobalTime;
//		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime),
//				TimeUnit.MILLISECONDS.toSeconds(endTime)
//						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//		logger.info("ClausIE - "+timeElapsed);
//		u.writeReport(new File(outputFolder+"report/Clausie-Report.tsv"),lr, timeElapsed);
//	}
	

}
