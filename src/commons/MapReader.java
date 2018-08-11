package commons;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MapReader {

	public static String WALL = "WAL";
	
	public static String SPACE = "000";
	
	public static String TANK_1 = "TA1";
	
	public static String TANK_2 = "TA2";

    public static String BREAKABLE_WALL = "BWL";

	public static String[][] readMap(String mapFileName) throws IOException {
		String[][] array = new String[Globals.MAX_NUMBER_OF_BLOCKS][Globals.MAX_NUMBER_OF_BLOCKS];
		CommonAPIs.validateFileExists(mapFileName);
		BufferedReader br = new BufferedReader(new FileReader(mapFileName));
		String line;
		int row = 0, col = 0;
		while ((line = br.readLine()) != null) {
			col = 0;
			String[] tokens = line.split(",");
			for (String value : tokens) {
				array[row][col] = value;
				col += 1;
			}
			row += 1;
		}
		br.close();
		return array;
	}
}
