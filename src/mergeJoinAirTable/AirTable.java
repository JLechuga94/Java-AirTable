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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class AirTable{
	
	private static int sizeBloc = 10;
	
	public static void main(String[] args, boolean newData) throws IOException {
		if(newData) {
			ArrayList<String> airTableRelations = new ArrayList<String>(
					Arrays.asList("RD","SD", "RSD", 
							"A00", "A01", "A02", "A03", "A04", "A05","A06", "A07", "A08","A09", 
							"B00", "B01", "B02", "B03", "B04", "B05", "B06", "B07", "B08", "B09",
							"C00", "C01","C02", "C03", "C04", "C05", "C06", "C07", "C08", "C09"));
			
			System.out.println("******************** Initialized AirTable Data Management System ********************\n");
			
			String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			int alphabetSize1 = 26;
			int alphabetSize2 = 8;
			
			ArrayList<String> charPairs = new ArrayList<String>();
			ArrayList<String> R = new ArrayList<String>();
			ArrayList<String> S = new ArrayList<String>();
			
			int sizeR = 96;
			int sizeS = 46;
			
			charPairs = createCharPairs(alphabet, alphabetSize1, alphabetSize2);
			R = createRelation(charPairs, sizeR);
			S = createRelation(charPairs, sizeS);
			
			clearAirTableData(airTableRelations);
			sendAirTableData(R, "RD");
			sendAirTableData(S, "SD");
			System.out.println("\n******************** Finalized AirTable Data Management System ********************\n");
		}
		else 
		{
			ArrayList<String> mergedAirTableRelations = new ArrayList<String>(
					Arrays.asList("RSD", "C00", "C01","C02", "C03", "C04", "C05", "C06", "C07", "C08", "C09"));
			clearAirTableData(mergedAirTableRelations);
			}
		
		}
	
	private static ArrayList<String> createCharPairs(String alphabet, int alphabetSize1, int alphabetSize2) {
		
		String sigma1 = alphabet.substring(0, alphabetSize1);
		String sigma2 = alphabet.substring(0, alphabetSize2);
		ArrayList<String> charPairs = new ArrayList<String>();
		
		for (int i = 0; i < sigma1.length(); i++){
			char char1 = sigma1.charAt(i);
			for(int j = 0; j < sigma2.length(); j++) {
				char char2 = sigma2.charAt(j);
				String cell = String.format("%c%c", char1, char2);
				charPairs.add(cell);
			}
		}
		Collections.shuffle(charPairs);
		
		System.out.println("------ Char Pairs for creation of Relations -----");
		System.out.println(charPairs);
		System.out.println("Size : " + charPairs.size() + "\n");
		
		return charPairs;
	}
	
	private static ArrayList<String> createRelation(ArrayList<String> charPairs, int relationSize) {
		
		ArrayList<String> relation = new ArrayList<String>();
		Random random = new java.util.Random();
		String charCouple = "";
		
		for(int k = 0; k < relationSize; k++) {
			boolean init = false;
			//This avoids duplicates in each relation
			while(relation.contains(charCouple) || init == false) {
				init = true;
				int randomIndex = random.nextInt(charPairs.size());
				charCouple = charPairs.get(randomIndex);
			}
			relation.add(charCouple);
		}
		System.out.println("----- Relation created from CharPairs -----");
		System.out.println(relation);
		System.out.println("Size : " + relation.size() + "\n");
		return relation;
	}

	private static void clearAirTableData(ArrayList<String> airTableRelations) throws IOException {
		System.out.println("----- Clearing existing Data from AirTable -----");
		String relation = null;
		for(int i = 0; i < airTableRelations.size(); i++) {
			relation = airTableRelations.get(i);
			API.DELETE(relation);
		}
		System.out.println("\n----- Finished clearing existing Data from AirTable -----\n");
	}
	
	/*** This function sends all the data generated from randomly selecting pairs of characters in groups of 10
	 * since AirTable limits each call to 10 records. We need to send R and S in packets of 10 cells.
	 ***/
	public static void sendAirTableData(ArrayList<String> relation, String relationName) throws IOException {
		System.out.println("----- Sending new Data to AirTable -----\n");
		
		ArrayList<String> dataBloc = new ArrayList<String>();
		ArrayList<String> descriptor = new ArrayList<String>();	
		String json = null;
		
		String blocName = null;
		int counter = 0;
		
		while(relation.size() > 0) {
			
			while(dataBloc.size() < sizeBloc && relation.size() > 0) {
				dataBloc.add(relation.remove(0));
			}
			
			if(relationName == "RD")
				blocName = "A0" +  Integer.toString(counter);
			else if(relationName == "SD")
				blocName = "B0" +  Integer.toString(counter);
			else if (relationName == "RSD")
				blocName = "C0" +  Integer.toString(counter);
			
			json = Parser.buildJSON(dataBloc);
			
			System.out.println(String.format("Sending bloc: %s of relation: %s", blocName, relationName));
			API.POST(json, blocName);
			
			dataBloc.clear();
			descriptor.add(Integer.toString(counter));
			counter++;
		}
		
		json = Parser.buildJSON(descriptor);
		System.out.println(String.format("Sending descriptor: %s", relationName));
		API.POST(json, relationName);
		
		System.out.println("----- Finished sending new Data to AirTable -----\n");
	}

	public static ArrayList<String> transformMergeDataToArray(int[][] RS){
		ArrayList<String> mergedData = new ArrayList<String>();
		
		for(int i = 0; i < RS.length; i++) {
			if(Arrays.stream(RS[i]).sum() > 0) {
				ArrayList<String> rsBloc = Parser.asciiTableToStringArray(RS[i]);
				mergedData.addAll(rsBloc);
			}
		}
		System.out.println("Merged data");
		System.out.println(mergedData + "\n");
		return mergedData;
	}
}
