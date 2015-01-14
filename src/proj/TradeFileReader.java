package proj;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Set;

import proj.object.Record;

public class TradeFileReader {
	public void readFromRawFile(String pathStr) throws IOException {
		Path path = FileSystems.getDefault().getPath("", pathStr);
		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		String line = null;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

	public static void main(String[] args) {
		TradeFileReader reader = new TradeFileReader();
		try {
			reader.readFromRawFile("C:\\Working\\workspaceLuna\\project\\taqtrade20141030");
		} catch (Exception e) {
			// do nothing
		}
	}

	// public Set<Record> readFromPersistence(String path) {
	//
	// }
}
