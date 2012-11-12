package alliterationLocator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import NGramSet.AlliterationImpl;

public class InterTextualFinder {
	private String paramString;
	private static AlliterationFinder<AlliterationImpl> comparer;
	private static List<List<String>> commonNGrams;

	public void findAlliterationsGivenParams(
		String primarySourcePath, 
		int minimumMatches, 
		boolean useStopWords
	) {
		File file = new File(primarySourcePath);

		List<String> files = new ArrayList<String>(1);

		readInFiles(file, files);

		double start = System.currentTimeMillis();
		
		comparer = new AlliterationFinder<AlliterationImpl>();
		comparer.setUseStopWords(useStopWords);
		commonNGrams = comparer.findAlliterations(files.get(0), minimumMatches);
		
		//System.out.println(files.get(0));

		double end = System.currentTimeMillis();
		double totalTime = end - start;
		totalTime /= (1000 * 6);// convert to minutes
		totalTime = totalTime / 10;

		paramString = convertParametersToString(
			primarySourcePath, minimumMatches, 
			useStopWords, totalTime
		);
	}
	
	public static String toString(String paramsAsString, List<List<String>> commonNGrams) {
		StringBuilder str = new StringBuilder();
		
		int matchCount = 0;
		
		for(List<String> alliteration : commonNGrams) {
			//Alliteration nGram = itr.next();
			str.append(alliteration.toString());
			str.append("\n");
		}
		
		String stringToSaveToFile =
			paramsAsString
			+ "Alliterations Found: " + commonNGrams.size()+"\n\n"
			+ str.toString()
		;
		return stringToSaveToFile;
	}

	private static String convertParametersToString(
		String primarySource, int minimumMatches, 
		boolean useStopWords, double totalTime
	) {
		String params = new String();

		params += "Primary Source: " + primarySource + "\n";

		/*
		params += "Use Stop Words: ";
		if (useStopWords)
			params += "Yes" + "\n";
		else
			params += "No" + "\n";
		*/
		
		params += "Time to complete: " + totalTime + " second" + ( (totalTime > 2) ? "s" : "" ) + ".\n";
		return params;
	}

	private static void readInFiles(File file, List<String> files) {
		System.out.println("Consider making everything compatible with unicode.\n");
		FileInputStream fis = null;
		// InputStreamReader in = null;

		try {
			fis = new FileInputStream(file);
			if (fis != null) {
				// in = new InputStreamReader(fis, "UTF-8");
				String newLine = read(file.toString(), "UTF-8");
				//String newLine = read(f.toString(), "unicode");
				
				files.add(newLine);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String read(String filename, String fEncoding) throws IOException {
		File fFilename = new File(filename);

		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner scanner = new Scanner(new FileInputStream(fFilename), fEncoding);
		try {
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine() + NL);
			}
		} finally {
			scanner.close();
		}
		return text.toString();
	}

	public void saveTo(String outFilePath) {
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(outFilePath);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(toString());
			  //Close the output stream
			  out.close();
		  }	catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		  }
	}
	
	public String toString() {
		if(paramString == null || commonNGrams == null)
			return "No data. ";
		else
			return toString(paramString, commonNGrams).replaceAll("\n", System.getProperty("line.separator"));
	}
}
