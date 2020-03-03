package HW;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Tokenizer class.
// Complicated regex from https://stackoverflow.com/questions/28411032/java-regex-remove-comments
// slightly modified to remove strings too instead of just putting them back after finding them
//
// CONSTRUCTION: with a Reader object
// ******************PUBLIC OPERATIONS***********************
// String getNextID( )        --> Get next Java identifier
// int getLineNumber( )       --> Return line number of current ID
// int getErrorCount( )       --> Return error count (always 0)
// ******************ERRORS**********************************
// This version assumes a compilable Java program text, so no errors are reported
public class RegexTokenizer implements JavaTokenizer{
	private int currentLine; // Current line
	private int errors; // Number of errors seen (not in use)
	// Java text of program, simplified by removing comments and quoted structures
	private String contents;
	// Scanner on contents String, on currentLine
	private Scanner scan;
	// tokens on this line being scanned
	private List<String> tokens;

	/**
	 * Implementation .
	 * 
	 * @param inStream the stream containing a program.
	 */
	@Override
	public void RegexTokenizer(Reader inStream) {
		String currentLineString;
		errors = 0;
		currentLine = 1;
		char[] arr = new char[8 * 1024];

		StringBuilder buffer = new StringBuilder();
		int numCharsRead;
		try {
			while ((numCharsRead = inStream.read(arr, 0, arr.length)) != -1) {
				buffer.append(arr, 0, numCharsRead);
			}
			inStream.close();
		} catch (IOException e) {
//			System.out.println("error: " + e);
		}
		contents = buffer.toString();
//		System.out.println("orig contents: " + contents);
		contents = removeCommentsAndStrings(contents);
//		System.out.println("contents after comments removed: " + contents);
		scan = new Scanner(contents);
		currentLineString = scan.nextLine();
//		System.out.println("first linestring = " + currentLineString);
		tokens = findIds(currentLineString);
		System.out.println("first tokens: " + tokens);
	}

	/*
	 * Return next identifier, skipping comments string constants, and character
	 * constants. Return nullstring if end of stream is reached.
	 */
	public String getNextID() {
		String currentLineString;
		String token = null;
		if (tokens.isEmpty()) { // end of this line
			while (scan.hasNextLine()) {
				currentLine++;
				currentLineString = scan.nextLine();
				//System.out.println("getting new line: " + currentLineString);
				tokens = findIds(currentLineString);
				if (!tokens.isEmpty()) {
					token = tokens.get(0);
					tokens.remove(token);
					return token; 
				}				
			}
			return "";
		} else { // tokens is not empty, use one
			token = tokens.get(0);
			tokens.remove(token);
		}
		return token;
	}

	/**
	 * Gets current line number.
	 * 
	 * @return current line number.
	 */
	public Integer getLineNumber() {
		return currentLine;
	}

	/**
	 * Gets error count.
	 * 
	 * @return error count.
	 */
	public int getErrorCount() {
		return errors;
	}
	
    // Use a regex to remove all comments and strings from a Java program
	private String removeCommentsAndStrings(String input) {
		// from https://stackoverflow.com/questions/28411032/java-regex-remove-comments
		final String regex = "((['\"])(?:(?!\\2|\\\\).|\\\\.)*\\2)|\\/\\/[^\\n]*|\\/\\*(?:[^*]|\\*(?!\\/))*\\*\\/";
		final Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			// Use the power of matcher to examine the found substrings
//			System.out.println("found start = " + matcher.start());
//			System.out.println("matched string: " + input.substring(matcher.start(), matcher.end()));
			String s = input.substring(matcher.start(), matcher.end());
			// Find the lines being replaced and compose a replacement string of just
			// their newlines to preserve the line structure of the file		
			StringBuilder sb1 = new StringBuilder();
			for (int i = 0; i < s.length(); i++)
				if (s.charAt(i) == '\n') {
					sb1.append('\n');
				}
			String newlines = sb1.toString();
			matcher.appendReplacement(sb, newlines); // replace old text with its newlines
//			System.out.println("after appendrep, sb = "+ sb.toString());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private List<String> findIds(String input) {
		// TODO: fill in regex...
		String idRegex = "[a-zA-Z]+";
		List<String> allMatches = new ArrayList<String>();
		Matcher matcher = Pattern.compile(idRegex).matcher(input);
		while (matcher.find()) {
			// Use the power of matcher to examine the found substrings
//			System.out.println("found start = " + matcher.start());
//			System.out.println("string: " + input.substring(matcher.start(), matcher.end()));
			String id = input.substring(matcher.start(), matcher.end());
			allMatches.add(id);
		}
		return allMatches;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO: write simple test: find the tokens for a Java source file
		File initialFile = new File("C:\\Users\\Rober\\eclipse-workspace\\Anna1\\src\\HW\\RegexTokenizer.java");
		InputStream targetStream = new FileInputStream(initialFile);
		JavaTokenizer tok = new RegexTokenizer();
		tok.RegexTokenizer(new InputStreamReader(targetStream));
	}

	@Override
	public void WeissTokenizer(Reader inStream) {

	}

	
	

	
}
