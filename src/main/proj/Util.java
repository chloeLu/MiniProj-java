package proj;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Util {
	public static void emptyOrCreateOutputFile(String outFileName) {
		try {
			BufferedWriter emptyBw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFileName, false),
					StandardCharsets.UTF_8));
			emptyBw.write("");// appends the string to the file
			emptyBw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}
}
