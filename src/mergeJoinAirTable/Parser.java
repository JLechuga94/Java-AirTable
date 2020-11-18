/**
 * TP  n°: 4 V n°: 2
 * 
 * Titre du TP: Merge Join AirTable
 * 
 * Date: 16 novembre 2020
 * 
 * E1: Lechuga Lopez Leopoldo Julian
 * E2: Morakhovski Alexander
 * 
 * email: julian.lechuga305@gmail.com 
 * email: alexmorakhovski@gmail.com
 *
 * Remarques: TP4
 */

package mergeJoinAirTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {
	
	private static String valueRgx = "\"Value\":\"([^\"]*)\"";
	private static String idRgx = "\"id\":\"([^\"]*)\"";
	
	public static String buildJSON(ArrayList<String> relation) {
		
		StringBuilder json = new StringBuilder();
		json.append("{\"records\":[");
		
		for(int i = 0; i < relation.size()-1; i++) {
			String element = String.format("{\"fields\": {\"Value\":\"%s\"}},", relation.get(i));
			json.append(element);
			}
		json.append(String.format("{\"fields\": {\"Value\":\"%s\"}}", relation.get(relation.size()-1)));
		json.append("]}");
		//System.out.println(json.toString());
		
		return json.toString();
	}
	
	public static int[] parseValues(InputStream responseStream, boolean descriptor){
		
		String json = convertStreamToString(responseStream);
		
		ArrayList<String> values = getAllMatches(json, valueRgx);
		int[] table = new int[values.size()];
		
		if(!descriptor) {
			table = stringArrayToASCIITable(values);
		}
		else {
			for(int i = 0; i < values.size(); i++) {
				table[i] = Integer.parseInt(values.get(i));  
			}
		}
		        
        return table;
	}
	
	public static ArrayList<String> parseIDs(InputStream responseStream){
		
		String json = convertStreamToString(responseStream);
		
		ArrayList<String> ids = getAllMatches(json, idRgx);
        
        return ids;
	}
	
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				}
			}
		catch (IOException e) {
			e.printStackTrace();
			} 
		finally {
		    try {
		        is.close();
		    } 
		    catch (IOException e) {
		        e.printStackTrace();
		        }
		    }
		return sb.toString();
		}
	
	// Function to find all matches of regex string in JSON response
	private static ArrayList<String> getAllMatches(String text, String regex) {
        ArrayList<String> matches = new ArrayList<String>();
        Matcher m = Pattern.compile(regex).matcher(text);
        while(m.find()) {
            matches.add(m.group(1));
        }
        return matches;
    }
	
	private static int[] stringArrayToASCIITable(ArrayList<String> relation) {

		int[] asciiRelation = new int[relation.size()];
		for(int i = 0; i < relation.size(); i++ ) {
			
			char char1 = relation.get(i).charAt(0);
			char char2 = relation.get(i).charAt(1);
			int ascii1 = (int) char1;
			int ascii2 = (int) char2;
			int blocValue = ascii1*100 + ascii2;
			asciiRelation[i] = blocValue;
			
		}
//		System.out.println(Arrays.toString(asciiRelation));
		
		return asciiRelation;
	}
	
	public static String asciiIntToString(int asciiInt) {
		String asciiStr = null;
		// To make sure we don't include empty strings
		if(asciiInt != 0) {
			int char1 = asciiInt/100;
			int char2 = asciiInt%100;
			asciiStr = Character.toString((char) char1) + Character.toString((char) char2);
		}
		return asciiStr;
	}
	
	public static ArrayList<String> asciiTableToStringArray(int[] asciiTable) {
		ArrayList<String> relation = new ArrayList<String>();
		for(int i = 0; i < asciiTable.length; i++) {
			String str = asciiIntToString(asciiTable[i]);
			if(str != null)
				relation.add(str);
		}
		//System.out.println(relation);
		return relation;
	}
}
