package mergeJoinAirTable;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// Change the value of this variable to create new data in AirTable
		boolean newData = false;
		
		AirTable.main(args, newData);
		Algorithm.main(args);
	
	}

}
