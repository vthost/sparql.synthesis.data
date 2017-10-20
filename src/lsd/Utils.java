package lsd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;

public class Utils {
	
	public static String DATA_DIR = "data/";	
	public static String QUERY_FILE_EXT = ".txt";
	public static String CONSTRUCT_QUERIES_FILE_EXT = "-cqs.txt";
	public static String QUERY_DATA_FILE_EXT = "-data.xml";
	public static String QUERY_RESULT_FILE_EXT = "-result.xml";
	
	public static String getQueryId(String lsqIdUrl) {
		return lsqIdUrl.substring(lsqIdUrl.lastIndexOf("/") + 1);
	}
	
	public static String getQueryFilePath(String lsqIdUrl) {
		return DATA_DIR + Utils.getQueryId(lsqIdUrl) + QUERY_FILE_EXT;
	}
	
	public static String getConstructQueriesFilePath(String lsqIdUrl) {
		return DATA_DIR + Utils.getQueryId(lsqIdUrl) + CONSTRUCT_QUERIES_FILE_EXT;
	}
	
	public static String getQueryDataFilePath(String lsqIdUrl) {
		return DATA_DIR + Utils.getQueryId(lsqIdUrl) + QUERY_DATA_FILE_EXT;
	}
	
	public static String getQueryResultFilePath(String lsqIdUrl) {
		return DATA_DIR + Utils.getQueryId(lsqIdUrl) + QUERY_RESULT_FILE_EXT;
	}
	
	public static void writeQueryFile(String lsqIdUrl, String query) {

		try {
			FileWriter writer = new FileWriter(Utils.getQueryFilePath(lsqIdUrl));
		  	writer.write(lsqIdUrl);
		  	writer.write("\n");
//		  	using the factory we get a formatting that is more readable. 
//		  	but sometimes it then writes no whitespace ?! (http://lsq.aksw.org/res/DBpedia-q390826)
		  	writer.write(query);//QueryFactory.create(query).toString()); 
		  	writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeConstructQueriesFile(String lsqIdUrl, List<Query> queries) {

		try {
			FileWriter writer = new FileWriter(getConstructQueriesFilePath(lsqIdUrl));
		  	for (Query query : queries) {
		  		writer.write(query + "\n----------------------------------------------\n"); 
			}
		  	writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeQueryDataFile(String lsqIdUrl, Model m) {

		try {
			if(new File(Utils.getQueryDataFilePath(lsqIdUrl)).isFile())
				m.read(Utils.getQueryDataFilePath(lsqIdUrl));
			
			FileWriter writer = new FileWriter(Utils.getQueryDataFilePath(lsqIdUrl));
//    	  	writer.write(logQueryIds.get(i));
//    	  	writer.write("\n");
			
            m.write(writer, "RDF/XML");
    	  		writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeQueryResultFile(String lsqIdUrl, ResultSet rs) {

		try {
			FileWriter writer = new FileWriter(Utils.getQueryResultFilePath(lsqIdUrl));
//    	  	writer.write(logQueryIds.get(i));
//    	  	writer.write("\n");
			writer.write(ResultSetFormatter.asXMLString(rs));
//			while(rs.hasNext()) {
//				writer.write(rs.next().toString());
//			}
    	  	writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	returns array with: 0 : id, 1 : query
	public static String[] readQueryFile(File f) {
		try {
			Scanner s = new Scanner(f);
			String id = s.nextLine();
			String q = s.nextLine();
			while(s.hasNextLine()) q += s.nextLine();
			String[] result = {id, q};
			s.close();
			
			return result;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			
		return null;
	}
	
	public static void cleanDataDir() {
		
		for(File file: (new File(DATA_DIR)).listFiles()) 
			file.delete();
	}

	public static void main(String[] args) {
		
	}
	

}
