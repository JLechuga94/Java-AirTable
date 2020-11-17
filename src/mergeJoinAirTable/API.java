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

import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class API {
	
	private static String urlBase = "https://api.airtable.com/v0/appc8HWCQkNG0gzVP/";
	private static String apiKey = "Bearer keyiG1lq5KCAa4s5U";
	
	public static void main(String[] args) {
	}
	
	public static InputStream GET(String airTableName) throws IOException {
		// Create a neat value object to hold the URL
		String urlString = urlBase + airTableName + "?view=Grid%20view";
        URL url = new URL(urlString);

        // Open a connection(?) on the URL(?) and cast the response(??)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", apiKey);

        // This line makes the request
        InputStream responseStream = connection.getInputStream();
        
        //connection.disconnect();
        int responseCode = connection.getResponseCode();
        
        if(responseCode != 200) {
			System.out.println("GET Operation went wrong. Code: " + responseCode);
		}
        
        return responseStream;
	}
	
	public static void POST(String json, String airTableName) throws IOException {
		// Create a neat value object to hold the URL
		
		// Create a neat value object to hold the URL
		String urlString = urlBase + airTableName;
        URL url = new URL(urlString);

        // Open a connection(?) on the URL(?) and cast the response(??)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        
        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json; utf-8");
        connection.setRequestProperty("Authorization", apiKey);
        
        connection.setDoOutput(true);
        
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);			
        }
        
        try(BufferedReader br = new BufferedReader(
        		new InputStreamReader(connection.getInputStream(), "utf-8"))) {
    		    StringBuilder response = new StringBuilder();
    		    String responseLine = null;
    		    while ((responseLine = br.readLine()) != null) {
    		        response.append(responseLine.trim());
    		    }
    		    System.out.println(response.toString());
    		    if(connection.getResponseCode() == 200) {
    		    	System.out.println("POST to AirTable was succesful\n");
    		    }
    		    else {
    		    	System.out.println("POST unsuccesful. Something went wrong.");
    		    }
    		    }
        connection.disconnect();
	}
	
	public static void DELETE(String airTableName) throws IOException {
		
		InputStream responseStream = GET(airTableName);
		ArrayList<String> ids = Parser.parseIDs(responseStream);
		
		if(ids.size() > 0) {
			System.out.println(String.format("\nTable: %s | # of rows to be deleted: %d", airTableName, ids.size()));
			//System.out.println(ids);
			System.out.println("Deleting...");
			for(int i = 0; i < ids.size(); i++) {
				String urlString = urlBase + airTableName + "/"+ ids.get(i);
				URL url = new URL(urlString);
				
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("DELETE");
				
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		        connection.setRequestProperty("Authorization", apiKey);
				
		        connection.setDoOutput(true);
				
				int responseCode = connection.getResponseCode();
				
				if(responseCode != 200) {
					System.out.println("DEL Operation went wrong. Code: " + responseCode);
				}
				connection.disconnect();
			}
			System.out.println("Deleted.");
		}
		
		
	}
}
