package com.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.utilities.Utils;

public class SplitSentences {
	
	/*
	 * args[0] - Input directory
	 * args[1] - output directory
	 * 
	 */

	public SplitSentences() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		Utils u = new Utils();
		File[] in = new File(args[0]).listFiles();
		String out = args[1];
		
		for(File f : in) {
			List<String> s = u.extractSentences(FileUtils.readFileToString(f));
			FileUtils.writeLines(new File(out+f.getName()), s);
		}

	}

}
