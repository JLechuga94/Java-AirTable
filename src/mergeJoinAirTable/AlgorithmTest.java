package mergeJoinAirTable;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class AlgorithmTest {

	@Test
	public void testSpaceAdder() {
		ArrayList<String> relation = new ArrayList<String>(Arrays.asList("AA", "AB", "AC"));
		
		ArrayList<String> result = Algorithm.spaceAdder(relation);
		assertEquals(result, Arrays.asList("  AA", "  AB", "  AC"));
	}

}
