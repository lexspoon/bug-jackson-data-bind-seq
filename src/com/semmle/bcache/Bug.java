package com.semmle.bcache;

import java.io.File;

public class Bug {
	public static void main(String[] args) {
		BuildGraph buildGraph = BuildGraph.load(new File("brules.json"));
		System.out.println("Loaded graph with " + buildGraph.numRules() + " rules.");
	}
}
