package proj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestDataGenerator {

	private final double P;
	private final long TRADE_NUMLINES;
	private final long NBBO_NUMLINES;
	
	private static final String tradePathStr = "C:\\Working\\workspaceLuna\\project\\taqtrade20131218";
	private static final String nbboPathStr = "C:\\Working\\workspaceLuna\\project\\taqnbbo20131218";
	private static final String outDir = "..\\project\\";

	public TestDataGenerator(double probabilty, long tradeNumLines, long nbboNumLines) {
		this.P = probabilty;
		this.TRADE_NUMLINES = tradeNumLines;
		this.NBBO_NUMLINES = nbboNumLines;
	}
	
	public void generateRawNbboTestFile(String outFileName, String baseFilePath) throws IOException {
		generateRawTradeTestFile(outFileName, baseFilePath, NBBO_NUMLINES);
	}
	
	public void generateRawTradeTestFile(String outFileName, String baseFilePath) throws IOException {
		generateRawTradeTestFile(outFileName, baseFilePath, TRADE_NUMLINES);
	}

	public void generateRawTradeTestFile(String outFileName, String baseFilePath, long numLines) throws IOException {
		Util.emptyOrCreateOutputFile(outFileName);
		Path path = FileSystems.getDefault().getPath("", baseFilePath);
		BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
		String line = reader.readLine(); // skip the first line
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileName, true), StandardCharsets.UTF_8));
		bw.write("Dummy line to simulate real data\n");
		int count = 0;
		try {
			while ((line = reader.readLine()) != null && count < numLines) {
				if (Math.random() < P) {
					bw.write(line + "\n");
					count++;
				}
			}
		} finally {
			bw.close();
		}
	}

	public static void main(String[] args) {
		try {
			TestDataGenerator generator1 = new TestDataGenerator(0.0001, 100, 100);
			generator1.generateRawTradeTestFile(outDir + "test_trade_1", tradePathStr);
			generator1.generateRawTradeTestFile(outDir + "test_trade_2", tradePathStr);
			generator1.generateRawNbboTestFile(outDir + "test_nbbo_1", nbboPathStr);
			generator1.generateRawNbboTestFile(outDir + "test_nbbo_2", nbboPathStr);

			TestDataGenerator generator2 = new TestDataGenerator(0.0001, 200, 200);
			generator2.generateRawTradeTestFile(outDir + "test_trade_3", tradePathStr);
			generator2.generateRawTradeTestFile(outDir + "test_trade_4", tradePathStr);
			generator2.generateRawNbboTestFile(outDir + "test_nbbo_3", nbboPathStr);
			generator2.generateRawNbboTestFile(outDir + "test_nbbo_4", nbboPathStr);

			TestDataGenerator generator3 = new TestDataGenerator(0.0001, 1 , 1);
			generator3.generateRawTradeTestFile(outDir + "test_trade_5", tradePathStr);
			generator3.generateRawTradeTestFile(outDir + "test_trade_6", tradePathStr);
			generator3.generateRawTradeTestFile(outDir + "test_trade_7", tradePathStr);
			generator3.generateRawNbboTestFile(outDir + "test_nbbo_5", nbboPathStr);
			generator3.generateRawNbboTestFile(outDir + "test_nbbo_6", nbboPathStr);
			generator3.generateRawNbboTestFile(outDir + "test_nbbo_7", nbboPathStr);
		} catch (Exception e) {
			// do nothing
		}
	}
}
