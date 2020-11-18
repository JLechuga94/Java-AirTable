package mergeJoinAirTable;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class ParserTest {

	@Test
	public void testJSONBuilder() {
		ArrayList<String> relation = new ArrayList<String>(
				Arrays.asList("AA", "AB", "AC", "BB", "BC", "BD")
				);
		StringBuilder expectedJSON = new StringBuilder();
		expectedJSON.append("{\"records\":[");
		expectedJSON.append("{\"fields\": {\"Value\":\"AA\"}},");
		expectedJSON.append("{\"fields\": {\"Value\":\"AB\"}},");
		expectedJSON.append("{\"fields\": {\"Value\":\"AC\"}},");
		expectedJSON.append("{\"fields\": {\"Value\":\"BB\"}},");
		expectedJSON.append("{\"fields\": {\"Value\":\"BC\"}},");
		expectedJSON.append("{\"fields\": {\"Value\":\"BD\"}}]}");
		
		String jsonResult = Parser.buildJSON(relation);
		
		assertEquals(jsonResult, expectedJSON.toString());
	}
	
	@Test
	public void testValueParser() throws IOException {
		int[] expected = new int[] {0,1,2,3,4,5,6,7,8,9};
		
		boolean descriptor = true;
		InputStream responseStream = API.GET("RD");
		int[] result = Parser.parseValues(responseStream, descriptor);
		
		assertArrayEquals(result, expected);
	}
	
	@Test
	public void testIDsParser() throws IOException {
		
		InputStream responseStreamRD = API.GET("RD");
		ArrayList<String> result = Parser.parseIDs(responseStreamRD);
		
		assertEquals(result.size(), 10);
	}
	
	@Test
	// We need to consider that in the future we may receive only
	// one character of the string
	public void testASCIItoStringTransformer() {
		// ASCII value for AB
		int value = 6566;
		
		String result = Parser.asciiIntToString(value);
		
		assertEquals(result, "AB");
	}
	
	@Test
	public void testASCIItoStringArrayTransformer() {
		int[] RS = new int[]{6565, 6566, 6567};
		
		ArrayList<String> result = Parser.asciiTableToStringArray(RS);
		
		assertEquals(result, Arrays.asList("AA", "AB", "AC"));
	}

}
