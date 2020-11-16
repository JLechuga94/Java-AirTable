package mergeJoinAirTable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;

public class Algorithm {
	
	// Storage size for output merged data
	private static int storageSize = 10;
	private static int RDSize = 10;
	private static int SDSize = 5;
	
	public static void main(String[] args) throws IOException {
		System.out.println("*************** Initialized Merge Operation ****************\n\n");
		int[] RD = createDescriptor(RDSize);
		int[] SD = createDescriptor(SDSize);
		
		/** With this Math.max() we ensure that we will have enough blocs in RS
		 * to store all the merged elements. IN this way we don't have to double 
		 * check inside the algorithm loops if we still have RS blocks left to
		 * store information 
		 **/
		int[] RSD = createDescriptor(Math.max(RD.length, SD.length));
		int[][] RS = innerLoopJoin(RD, SD, RSD);
		
		displayMergedData(RS);
		System.out.println("\n****************** Finished Merge Operation ****************\n\n");
		
		System.out.println("\n************** Sending merged Data to AirTable *************\n");
		ArrayList<String> mergedData = AirTable.transformMergeDataToArray(RS);
		AirTable.sendAirTableData(mergedData, "RS");
		System.out.println("\n*****************************************  Program terminated *****************************************\n");
	}
	
	private static int[][] innerLoopJoin(int[] RD, int[] SD, int[] RSD) throws IOException {
		
		
		int cell = 0;
		int blocRSIndex = 0;
		int[][] RS = new int[RSD.length][storageSize];
		
		int i = 0;
		while(i < RD.length) {
			// Read block R
			//System.out.println("Block of R analyzed: A0"+ i + "\n");
			
			InputStream responseStreamR = API.GET("A0" + i);
			int[] blocR = Parser.parseValues(responseStreamR);
			System.out.println(String.format("------------------- Analyzing R bloc A0%d -------------------", i));
			System.out.println(Arrays.toString(blocR));
			System.out.println(spaceAdder(Parser.asciiTableToStringArray(blocR)) + "\n");
			
			int j = 0;
			while(j < SD.length) {
				// Read block S
				InputStream responseStreamS = API.GET("B0" + j);
				int[] blocS = Parser.parseValues(responseStreamS);
				
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
	
	// Sets the number of blocs of R, S and RS available
	private static int[] createDescriptor(int size) {
		int[] descriptor = new int[size];
		for(int i = 0; i < size; i++) {
			descriptor[i] = i;
		}
		return descriptor;
	}
	
	// Function only for visualization purposes during run of algorithm
	// It displays the String value of each ASCII value below them
	private static ArrayList<String> spaceAdder(ArrayList<String> relation){
		for(int i = 0; i < relation.size(); i++) {
			relation.set(i, "  " + relation.get(i));
		}
		return relation;
	}
	
	// Display of Merged Data after run of the algorithm
	private static void displayMergedData(int[][] RS) {
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
