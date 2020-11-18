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
import java.io.*;

public class Algorithm {
	
	// Storage size for output merged data
	private static int storageSize = 10;
	
	public static int[][] innerLoopJoin(int[] RD, int[] SD, int sizeRSD) throws IOException {
		
		int cell = 0;
		int blocRSIndex = 0;
		int[][] RS = new int[sizeRSD][storageSize];
		
		int i = 0;
		while(i < RD.length) {
			// Read block R
			
			InputStream responseStreamR = API.GET("A0" + i);
			int[] blocR = Parser.parseValues(responseStreamR, false);
			System.out.println(String.format("------------------- Analyzing R bloc A0%d -------------------", i));
			System.out.println(Arrays.toString(blocR));
			System.out.println(spaceAdder(Parser.asciiTableToStringArray(blocR)) + "\n");
			
			int j = 0;
			while(j < SD.length) {
				// Read block S
				InputStream responseStreamS = API.GET("B0" + j);
				int[] blocS = Parser.parseValues(responseStreamS, false);
				
				//Initialize row of bloc R
				int k = 0;
				while(k < blocR.length) {
					// Initialize row of bloc S
					int w = 0;
					while(w < blocS.length) {
						//  If 
						if(blocR[k] == blocS[w]) {
							System.out.println(String.format("S bloc matched: B0%d", j));
							System.out.println(String.format("Matched element: %d --> %s", blocR[k], Parser.asciiIntToString(blocR[k])));
							System.out.println(Arrays.toString(blocS));
							System.out.println(spaceAdder(Parser.asciiTableToStringArray(blocS)) + "\n");

							if(cell < RS[blocRSIndex].length) {
								RS[blocRSIndex][cell] = blocR[k];
								} 
							else {
								blocRSIndex++;
								cell = 0;
								RS[blocRSIndex][cell] = blocR[k];
								}
							cell++;	
							
							}
						w++;
					}
					k++;
				}
				j++;
			}
			i++;
		}
		return RS;
	}
	
	// Function only for visualization purposes during run of algorithm
	// It displays the String value of each ASCII value below them
	public static ArrayList<String> spaceAdder(ArrayList<String> relation){
		for(int i = 0; i < relation.size(); i++) {
			relation.set(i, "  " + relation.get(i));
		}
		return relation;
	}
	
	// Display of Merged Data after run of the algorithm
	public static void displayMergedData(int[][] RS) {
		System.out.println("*******************  Merged Data  *******************\n");
		for(int i = 0; i < RS.length; i++) {
			if(Arrays.stream(RS[i]).sum() > 0) {
				ArrayList<String> rsBloc = Parser.asciiTableToStringArray(RS[i]);
				System.out.println(String.format("Merged table #%d", i));
				System.out.println(Arrays.toString(RS[i]));
				System.out.println(spaceAdder(rsBloc) + "\n");
				
			}
		}
	}
	
	

}
