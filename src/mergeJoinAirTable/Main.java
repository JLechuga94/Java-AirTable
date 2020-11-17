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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws IOException {
		// Change the value of this variable to create new data in AirTable
		boolean newData = true;
		
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
			
			charPairs = AirTable.createCharPairs(alphabet, alphabetSize1, alphabetSize2);
			R = AirTable.createRelation(charPairs, sizeR);
			S = AirTable.createRelation(charPairs, sizeS);
			
			AirTable.clearAirTableData(airTableRelations);
			AirTable.sendAirTableData(R, "RD");
			AirTable.sendAirTableData(S, "SD");
			System.out.println("\n******************** Finalized AirTable Data Management System ********************\n");
		}
		else 
		{
			ArrayList<String> mergedAirTableRelations = new ArrayList<String>(
					Arrays.asList("RSD", "C00", "C01","C02", "C03", "C04", "C05", "C06", "C07", "C08", "C09"));
			AirTable.clearAirTableData(mergedAirTableRelations);
			}
		
		System.out.println("*************** Initialized Merge Operation ****************\n\n");
		
		boolean descriptor = true;
		
		InputStream responseStreamRD = API.GET("RD");
		int[] RD = Parser.parseValues(responseStreamRD, descriptor);
		
		InputStream responseStreamSD = API.GET("SD");
		int[] SD = Parser.parseValues(responseStreamSD, descriptor);
		
		/** With this Math.max() we ensure that we will have enough blocs in RS
		 * to store all the merged elements. In this way we don't have to double 
		 * check inside the algorithm loops if we still have RS blocks left to
		 * store information 
		 **/
		int sizeRSD = Math.max(RD.length, SD.length);
		int[][] RS = Algorithm.innerLoopJoin(RD, SD, sizeRSD);
		
		Algorithm.displayMergedData(RS);
		System.out.println("\n****************** Finished Merge Operation ****************\n\n");
		
		System.out.println("\n************** Sending merged Data to AirTable *************\n");
		ArrayList<String> mergedData = AirTable.transformMergeDataToArray(RS);
		AirTable.sendAirTableData(mergedData, "RSD");
		System.out.println("\n*****************************************  Program terminated *****************************************\n");
	
	}

}
