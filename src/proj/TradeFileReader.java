package proj;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class TradeFileReader {
	private final String outFileName;

	public TradeFileReader() {
		this("preProcessedFile");
	}

	public TradeFileReader(String out) {
		outFileName = out;
		Util.emptyOrCreateOutputFile(outFileName);
	}

	/**
	 * Filter out useless columns and reduce file size
	 * 
	 * @param pathStr
	 *            : absolute path to raw trade file
	 * @throws IOException
	 */
	public void preProcessRawTradeFile(String pathStr) throws IOException {
		Path path = FileSystems.getDefault().getPath("", pathStr);
		BufferedReader reader = Files.newBufferedReader(path,
				StandardCharsets.UTF_8);
		String line = reader.readLine(); // skip the first line
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(outFileName, true),
						StandardCharsets.UTF_8));
		try {
			while ((line = reader.readLine()) != null) {
				bw.write(preProcessRawTradeLine(line) + "\n");
			}
		} finally {
			bw.close();
		}
	}

	/**
	 * Filter out useless columns and reduce file size
	 * 
	 * @param pathStr
	 *            : absolute path to raw trade file
	 * @throws IOException
	 */
	public void preProcessRawNbboFile(String pathStr) throws IOException {
		Path path = FileSystems.getDefault().getPath("", pathStr);
		BufferedReader reader = Files.newBufferedReader(path,
				StandardCharsets.UTF_8);
		String line = reader.readLine(); // skip the first line
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(outFileName, true),
						StandardCharsets.UTF_8));
		try {
			while ((line = reader.readLine()) != null) {
				bw.write(preProcessRawNbboLine(line) + "\n");
			}
		} finally {
			bw.close();
		}
	}

	private String preProcessRawTradeLine(String line) {
		// Time: base-0; size-9
		// Symbol: base-10; size-16
		// Qty: base:30; size-9
		// Price: base:39 size:11
		return line.substring(0, 9) + line.substring(10, 26)
				+ line.substring(30, 50);
	}

	private String preProcessRawNbboLine(String line) {
		// Time: base-0; size-9
		// Symbol: base-10; size-16
		// Bid-Price: base-26; size-11
		// Bid-Size: base-37; size-7
		// Ask-Price: base-44; size-11
		// Ask-Size: vase:55; size-7
		return line.substring(0, 62);
	}
}
