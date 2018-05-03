package com.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.nel.NE;
import com.nel.NEL;
import com.oie.ClausIEImpl;
import com.oie.OIETools;
import com.oie.OllieImpl;
import com.oie.ReverbImpl;
import com.oie.StanfordOIEImpl;
import com.utilities.NelReport;
import com.utilities.Triple;
import com.utilities.Utils;

public class OIENEL {
	private static Logger logger = Logger.getLogger(OIENEL.class);
	/*
	 * args[0] - dataset folder args[1] - output folder args[3] - Extractor
	 * stanford,ollie,clausie,reverb
	 */

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		File input = new File(args[0]);
		String outputFolder = args[1];
		String opt = args[2];
		OIENEL oie = new OIENEL();

//		switch (opt) {
//		case "stanford":
//			oie.runStanford(input.listFiles(), outputFolder);
//			break;
//		case "ollie":
//			oie.runOllie(input.listFiles(), outputFolder, new OllieImpl(), opt);
//			break;
//		case "reverb":
//			oie.runReverb(input.listFiles(), outputFolder);
//			break;
//		case "clausie":
//			oie.runClausie(input.listFiles(), outputFolder);
//			break;
//		default:
//			logger.error("No option: stanford, ollie, clausie or reverb");
//		}
		
		switch (opt) {
		case "stanford":
			oie.runGeneric(input.listFiles(), outputFolder, new StanfordOIEImpl(), opt);
			break;
		case "ollie":
			oie.runGeneric(input.listFiles(), outputFolder, new OllieImpl(), opt);
			break;
		case "reverb":
			oie.runGeneric(input.listFiles(), outputFolder, new ReverbImpl(), opt);
			break;
		case "clausie":
			oie.runGeneric(input.listFiles(), outputFolder, new ClausIEImpl(), opt);
			break;
		default:
			logger.error("No option: stanford, ollie, clausie or reverb");
		}

		// String sentence = "In European competitions, Jamie Carragher holds the club
		// record for the most appearances, with 139, and Steven Gerrard is the club's
		// record goalscorer, with 38 goals.";

		// cl.extractClausIETriples(sentence, problematicSnts);
		// soie.extractTSL(sentence);
		// reverb.reverb(sentence);
		// o.tripleExtractor(sentence);

	}
	
	public void runGeneric(File[] listFiles, String outputFolder, OIETools o, String oieToolName) throws Exception {
		Utils u = new Utils();
		List<NelReport> lr = new ArrayList<NelReport>();
		NEL nel = new NEL();
		long initialGlobalTime = System.currentTimeMillis();
		for (File f : listFiles) {
			
			if (f.getName().endsWith(".txt")) {
				NelReport r = new NelReport();
				r.setFile(f.getName());
				logger.info("Processing file: " + f.getName());
				List<String> l = FileUtils.readLines(f);

				r.setNumSentences(l.size());
				List<String> listT = new ArrayList<String>();
				List<String> listV = new ArrayList<String>();

				int counter = 0;
				int numNe = 0;
				int num2Ne = 0;
				long initialTime = System.currentTimeMillis();
				for (String s : l) {
					List<NE> nes = nel.extractEntitiesSpotlight(s, "0.7");
					if(nes != null) {
						numNe += nes.size();
						if(nes.size() > 1){
							num2Ne += nes.size();
							List<Triple> t = new ArrayList<Triple>(o.extractTriples(s));
							String oOutput = u.createOutput(s, t);
							String nOutput = u.createNelOutput(s, nes);
							listV.add(oOutput);
							listT.add(nOutput);
							counter += t.size();
						}
					}
					
				}
				long endTime = System.currentTimeMillis() - initialTime;
				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
						TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
				logger.info(oieToolName + " - " + timeElapsed);
				listT.add(timeElapsed);
				listV.add(timeElapsed);
				u.writeDocumentTriples(new File(outputFolder + oieToolName +"T/" + f.getName() + ".tsv"), listT);
				u.writeDocumentTriples(new File(outputFolder + oieToolName + "V/" + f.getName() + ".tsv"), listV);
				r.setNumTriples(counter);
				r.setNum2Ne(num2Ne);
				r.setNumNe(numNe);
				lr.add(r);
			}

		}
		
		long endTime = System.currentTimeMillis() - initialGlobalTime;
		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
		logger.info(oieToolName+" - " + timeElapsed);
		u.writeNelReport(new File(outputFolder + "report/" + oieToolName + "-Report.tsv"), lr, timeElapsed);
	}

//	public void runOllie(File[] listFiles, String outputFolder, OllieImpl o, String oieToolName) throws Exception {
//		Utils u = new Utils();
//		List<NelReport> lr = new ArrayList<NelReport>();
////		OllieImpl o = new OllieImpl();
//		NEL nel = new NEL();
//		long initialGlobalTime = System.currentTimeMillis();
//		for (File f : listFiles) {
//			
//			if (f.getName().endsWith(".txt")) {
//				NelReport r = new NelReport();
//				r.setFile(f.getName());
//				logger.info("Processing file: " + f.getName());
//				List<String> l = FileUtils.readLines(f);
//
//				r.setNumSentences(l.size());
//				List<String> listT = new ArrayList<String>();
//				List<String> listV = new ArrayList<String>();
//
//				int counter = 0;
//				int numNe = 0;
//				int num2Ne = 0;
//				long initialTime = System.currentTimeMillis();
//				for (String s : l) {
//					List<NE> nes = nel.extractEntitiesSpotlight(s, "0.7");
//					if(nes != null) {
//						numNe += nes.size();
//						if(nes.size() > 1){
//							num2Ne += nes.size();
//							List<Triple> t = new ArrayList<Triple>(o.tripleExtractor(s));
//							String oOutput = u.createOutput(s, t);
//							String nOutput = u.createNelOutput(s, nes);
//							listV.add(oOutput);
//							listT.add(nOutput);
//							counter += t.size();
//						}
//					}
//					
//				}
//				long endTime = System.currentTimeMillis() - initialTime;
//				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
//						TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
//								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//				logger.info(oieToolName + " - " + timeElapsed);
//				listT.add(timeElapsed);
//				listV.add(timeElapsed);
//				u.writeDocumentTriples(new File(outputFolder + oieToolName +"T/" + f.getName() + ".tsv"), listT);
//				u.writeDocumentTriples(new File(outputFolder + oieToolName + "V/" + f.getName() + ".tsv"), listV);
//				r.setNumTriples(counter);
//				r.setNum2Ne(num2Ne);
//				r.setNumNe(numNe);
//				lr.add(r);
//			}
//
//		}
//		
//		long endTime = System.currentTimeMillis() - initialGlobalTime;
//		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
//				TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
//						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//		logger.info(oieToolName+" - " + timeElapsed);
//		u.writeNelReport(new File(outputFolder + "report/" + oieToolName + "-Report.tsv"), lr, timeElapsed);
//	}
//
//	public void runStanford(File[] listFiles, String outputFolder) throws Exception {
//		Utils u = new Utils();
//		List<NelReport> lr = new ArrayList<NelReport>();
//		StanfordOIEImpl soie = new StanfordOIEImpl();
//		NEL nel = new NEL();
//		long initialGlobalTime = System.currentTimeMillis();
//		for (File f : listFiles) {
//			if (f.getName().endsWith(".txt")) {
//				NelReport r = new NelReport();
//				r.setFile(f.getName());
//				logger.info("Processing file: " + f.getName());
//				List<String> l = FileUtils.readLines(f);
//
//				r.setNumSentences(l.size());
//				List<String> list = new ArrayList<String>();
//
//				int counter = 0;
//				int numNe = 0;
//				int num2Ne = 0;
//				long initialTime = System.currentTimeMillis();
//				for (String s : l) {
//					List<NE> nes = nel.extractEntitiesSpotlight(s, "0.7");
//					if(nes !=null) {
//						numNe += nes.size();
//						if(nes.size() > 1){
//							num2Ne += nes.size();
//							List<Triple> t = new ArrayList<Triple>(soie.extractTSL(s));
//							String stanfordOutput = u.createOutput(s, t);
//							String nOutput = u.createNelOutput(s, nes);
//							list.add(stanfordOutput);
//							list.add("=========");
//							list.add(nOutput);
//							list.add("=========");
//							counter += t.size();
//						}
//					}
//					
//					
//				}
//				long endTime = System.currentTimeMillis() - initialTime;
//				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
//						TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
//								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//				logger.info("Stanford - " + timeElapsed);
//				list.add(timeElapsed);
//
//				u.writeDocumentTriples(new File(outputFolder + "stanford/" + f.getName() + ".tsv"), list);
//				r.setNumTriples(counter);
//				r.setNum2Ne(num2Ne);
//				r.setNumNe(numNe);
//				lr.add(r);
//			}
//
//		}
//		long endTime = System.currentTimeMillis() - initialGlobalTime;
//		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
//				TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
//						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//		logger.info("Stanford - " + timeElapsed);
//		u.writeNelReport(new File(outputFolder + "report/Stanford-Report.tsv"), lr, timeElapsed);
//	}
//
//	public void runReverb(File[] listFiles, String outputFolder) throws Exception {
//		Utils u = new Utils();
//		List<NelReport> lr = new ArrayList<NelReport>();
//		ReverbImpl reverb = new ReverbImpl();
//		NEL nel = new NEL();
//		
//		long initialGlobalTime = System.currentTimeMillis();
//		for (File f : listFiles) {
//			if (f.getName().endsWith(".txt")) {
//				NelReport r = new NelReport();
//				r.setFile(f.getName());
//				logger.info("Processing file: " + f.getName());
//				List<String> l = FileUtils.readLines(f);
//
//				r.setNumSentences(l.size());
//				List<String> list = new ArrayList<String>();
//
//				int counter = 0;
//				int numNe = 0;
//				int num2Ne = 0;
//				long initialTime = System.currentTimeMillis();
//				for (String s : l) {
//					List<NE> nes = nel.extractEntitiesSpotlight(s, "0.7");
//					if(nes != null){
//						numNe += nes.size();
//						if(nes.size() > 1){
//							num2Ne += nes.size();
//							List<Triple> t = new ArrayList<Triple>(reverb.reverb(s));
//							String reverbOutput = u.createOutput(s, t);
//							String nOutput = u.createNelOutput(s, nes);
//							list.add(reverbOutput);
//							list.add("=========");
//							list.add(nOutput);
//							list.add("=========");
//							counter += t.size();
//						}
//					}
//					
//				}
//				long endTime = System.currentTimeMillis() - initialTime;
//				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
//						TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
//								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//				logger.info("Stanford - " + timeElapsed);
//				list.add(timeElapsed);
//
//				u.writeDocumentTriples(new File(outputFolder + "reverb/" + f.getName() + ".tsv"), list);
//				r.setNumTriples(counter);
//				r.setNum2Ne(num2Ne);
//				r.setNumNe(numNe);
//				lr.add(r);
//			
//			}
//
//		}
//		long endTime = System.currentTimeMillis() - initialGlobalTime;
//		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
//				TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
//						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//		logger.info("Reverb - " + timeElapsed);
//		u.writeNelReport(new File(outputFolder + "report/Reverb-Report.tsv"), lr, timeElapsed);
//	}
//
//	public void runClausie(File[] listFiles, String outputFolder) throws Exception {
//		Utils u = new Utils();
//		List<NelReport> lr = new ArrayList<NelReport>();
//		ClausIEImpl cl = new ClausIEImpl();
//		List<String> problematicSnts = new ArrayList<String>();
//		NEL nel = new NEL();
//		
//		long initialGlobalTime = System.currentTimeMillis();
//		for (File f : listFiles) {
//			if (f.getName().endsWith(".txt")) {
//				NelReport r = new NelReport();
//				r.setFile(f.getName());
//				logger.info("Processing file: " + f.getName());
//				List<String> l = FileUtils.readLines(f);
//
//				r.setNumSentences(l.size());
//				List<String> list = new ArrayList<String>();
//
//				int counter = 0;
//				int numNe = 0;
//				int num2Ne = 0;
//				long initialTime = System.currentTimeMillis();
//				for (String s : l) {
//					List<NE> nes = nel.extractEntitiesSpotlight(s, "0.7");
//					if(nes != null) {
//						numNe += nes.size();
//						if(nes.size() > 1){
//							num2Ne += nes.size();
//							List<Triple> t = new ArrayList<Triple>(cl.extractClausIETriples(s, problematicSnts));
//							String clOutput = u.createOutput(s, t);
//							String nOutput = u.createNelOutput(s, nes);
//							list.add(clOutput);
//							list.add("=========");
//							list.add(nOutput);
//							list.add("=========");
//							counter += t.size();
//						}
//					}
//					
//				}
//				
//				long endTime = System.currentTimeMillis() - initialTime;
//				String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
//						TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
//								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//				logger.info("Stanford - " + timeElapsed);
//				list.add(timeElapsed);
//				
//				u.writeDocumentTriples(new File(outputFolder + "clausie/" + f.getName() + ".tsv"), list);
//				r.setNumTriples(counter);
//				r.setNum2Ne(num2Ne);
//				r.setNumNe(numNe);
//				lr.add(r);
//			}
//
//		}
//		long endTime = System.currentTimeMillis() - initialGlobalTime;
//		String timeElapsed = String.format("TOTAL TIME = %d min, %d sec",
//				TimeUnit.MILLISECONDS.toMinutes(endTime), TimeUnit.MILLISECONDS.toSeconds(endTime)
//						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)));
//		logger.info("ClausIE - " + timeElapsed);
//		u.writeNelReport(new File(outputFolder + "report/Clausie-Report.tsv"), lr, timeElapsed);
//	}

}
