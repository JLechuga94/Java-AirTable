package mergeJoinAirTable.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import mergeJoinAirTable.AirTable;

public class AirTableTesting {

	@Test
	public void testCharPairsCreation() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int alphabetSize1 = 26;
		int alphabetSize2 = 8;
		
		ArrayList<String> charPairs= AirTable.createCharPairs(alphabet, alphabetSize1, alphabetSize2);
		
		assertEquals(charPairs.size(), alphabetSize1*alphabetSize2);
	}
	
	@Test
	public void testRelationCreation() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int alphabetSize1 = 26;
		int alphabetSize2 = 8;
		int relationSize = 10;
		ArrayList<String> charPairs= AirTable.createCharPairs(alphabet, alphabetSize1, alphabetSize2);
		
		ArrayList<String> relation = AirTable.createRelation(charPairs, relationSize);
		
		assertEquals(relation.size(), relationSize);
	}
	
	@Test
	public void testMergedDataIntsToStringArray() {
		int[][] RS = new int[][] {
			{6565, 6566, 6567},
			{6666, 6667, 6668},
			{6767, 6768, 6769}
		};
		
		ArrayList<String> result = new ArrayList<String>(Arrays.asList("AA", "AB", "AC", "BB", "BC", "BD", "CC", "CD", "CE"));
		ArrayList<String> strRS = AirTable.transformMergeDataToArray(RS);
		
		assertEquals(result, strRS);
	}

	
}
