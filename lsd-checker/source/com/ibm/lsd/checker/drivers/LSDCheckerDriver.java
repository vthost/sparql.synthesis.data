package com.ibm.lsd.checker.drivers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;

import org.apache.jena.riot.RiotNotFoundException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.ibm.research.rdf.store.sparql11.semantics.BasicUniverse;
import com.ibm.research.rdf.store.sparql11.semantics.DatasetUniverse;
import com.ibm.research.rdf.store.sparql11.semantics.Drivers;
import com.ibm.research.rdf.store.sparql11.semantics.JenaTranslator;
import com.ibm.research.rdf.store.sparql11.semantics.JenaUtil;
import com.ibm.research.rdf.store.sparql11.semantics.SolutionRelation;
import com.ibm.research.rdf.store.utilities.io.SparqlRdfResultReader;
import com.ibm.research.rdf.store.utilities.io.SparqlSelectResult;
import com.ibm.wala.util.collections.Pair;

import kodkod.ast.Formula;
import kodkod.instance.TupleSet;

public class LSDCheckerDriver extends DriverBase {

	public static void main(String[] args) throws Exception {
		main(args[0], (String s) -> { mainLoop(s); });
	}
	
	public static void mainLoop(String queryFile) throws URISyntaxException, IOException {
		Query ast = JenaUtil.parse(queryFile);
		Op query = JenaUtil.compile(ast);
		
		String stem = queryFile.substring(0, queryFile.length()-QUERY_FILE_EXT.length());
	
		BasicUniverse U;
		try {
			U = new DatasetUniverse(new URL(stem + QUERY_DATA_FILE_EXT));
		} catch (RiotNotFoundException e) {
			return;
		}
		SparqlSelectResult answer = new SparqlRdfResultReader(stem + QUERY_RESULT_FILE_EXT);
		SolutionRelation s = new SolutionRelation(answer, ast.getProjectVars(), Collections.<String,Object>emptyMap());
		s.init(U);
		JenaTranslator xlator = JenaTranslator.make(ast.getProjectVars(), Collections.singleton(query), U, s);
		Pair<Formula, Pair<Formula, Formula>> xlation = xlator.translateSingle(Collections.<String,Object>emptyMap(), false).iterator().next();
		TupleSet x = Drivers.check(U, xlation, "solution");
		
		assert x.size() > 0;
	}
}
