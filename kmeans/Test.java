package kmeans;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Test {
    public static void main(String[] args) throws IOException {
		CSVReader reader = new CSVReader(new FileReader("data.csv"));
		List<String[]> myEntries = reader.readAll();
    	
    	KmeansParser parser = new KmeansParser(myEntries);
    	parser.doKmeans(5);
    	parser.
    }
}