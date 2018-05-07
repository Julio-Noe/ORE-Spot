package com.main;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class Testing {

	public static void main(String[] args) {
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		String name = "volkswagen";
        System.out.println(l.distance(name, "volkswagen"));
        System.out.println(l.distance(name, "vw"));
        System.out.println(l.distance(name, "vws"));
        
        info.debatty.java.stringsimilarity.MetricLCS lcs = 
                new info.debatty.java.stringsimilarity.MetricLCS();

        String s1 = "ABCDEFG";   
        String s2 = "ABCDEFHJKL";
        // LCS: ABCDEF => length = 6
        // longest = s2 => length = 10
        // => 1 - 6/10 = 0.4
//        System.out.println(lcs.distance(s1, s2));
        System.out.println(lcs.distance(name, "israeli"));

        // LCS: ABDF => length = 4
        // longest = ABDEF => length = 5
        // => 1 - 4 / 5 = 0.2
        System.out.println(lcs.distance("ABDEF", "ABDIF"));

	}

}
