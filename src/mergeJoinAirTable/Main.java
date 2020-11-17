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

public class Main {

	public static void main(String[] args) throws IOException {
		// Change the value of this variable to create new data in AirTable
		boolean newData = false;
		
		AirTable.main(args, newData);
		Algorithm.main(args);
	
	}

}
