package main.proj;

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

	private static final String TRADE_PATH_STR = Constants.outDir + "/taqtrade20131218";
	private static final String NBBO_PATH_STR =  Constants.outDir + "/taqnbbo20131218";

	private static final String USAGE = "java TestDataGenerator <baseTradeFileName> <targetTFileNumLines> <baseNbboFilePath> <targetNbboFileNumLines> \n";
	
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

		if (args.length == 0) {
			System.out.println("Generating 8 sets of test data...");
			generateDefault();
			System.out.println("Done!");
		} else {
			if (args.length != 4) {
				System.out.println("Wrong format. Usage: \n" + USAGE);
				System.exit(0);
			}
			System.out.println("Generating customized set of test data...");
			try {
				String tradePathStr = Constants.outDir + "/" + args[0];
				long tradeFileLines = Long.parseLong(args[1]);
				String nbboPathStr = Constants.outDir + "/" + args[2];
				long nbboFileLines = Long.parseLong(args[3]);
				generateCustom(tradePathStr, tradeFileLines, nbboPathStr, nbboFileLines);
				System.out.println("Done!");
			} catch (NumberFormatException nfe) {
				System.out.println("Wrong format. Usage: \n" + USAGE);
			} catch (IOException ioe) {
				System.out.println("Error when writing files. Check if directory exists:" + Constants.outDir);
			}
		}

	}

	private static void generateCustom(String tPath, long tLines, String nPath, long nLines) throws IOException {
		TestDataGenerator generator = new TestDataGenerator(0.0001, tLines, nLines);
		generator.generateRawTradeTestFile(Constants.outDir + "/test_trade_custom", tPath);
		generator.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_custom", nPath);
	}

	private static void generateDefault() {
		try {
			TestDataGenerator generator1 = new TestDataGenerator(0.0001, 100, 100);
			generator1.generateRawTradeTestFile(Constants.outDir + "/test_trade_1", TRADE_PATH_STR);
			generator1.generateRawTradeTestFile(Constants.outDir + "/test_trade_2", TRADE_PATH_STR);
			generator1.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_1", NBBO_PATH_STR);
			generator1.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_2", NBBO_PATH_STR);

			TestDataGenerator generator2 = new TestDataGenerator(0.0001, 200, 200);
			generator2.generateRawTradeTestFile(Constants.outDir + "/test_trade_3", TRADE_PATH_STR);
			generator2.generateRawTradeTestFile(Constants.outDir + "/test_trade_4", TRADE_PATH_STR);
			generator2.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_3", NBBO_PATH_STR);
			generator2.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_4", NBBO_PATH_STR);

			TestDataGenerator generator3 = new TestDataGenerator(0.0001, 1, 1);
			generator3.generateRawTradeTestFile(Constants.outDir + "/test_trade_5", TRADE_PATH_STR);
			generator3.generateRawTradeTestFile(Constants.outDir + "/test_trade_6", TRADE_PATH_STR);
			generator3.generateRawTradeTestFile(Constants.outDir + "/test_trade_7", TRADE_PATH_STR);
			generator3.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_5", NBBO_PATH_STR);
			generator3.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_6", NBBO_PATH_STR);
			generator3.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_7", NBBO_PATH_STR);

			TestDataGenerator generator4 = new TestDataGenerator(0.001, 10000, 10000);
			generator4.generateRawTradeTestFile(Constants.outDir + "/test_trade_8", TRADE_PATH_STR);
			generator4.generateRawNbboTestFile(Constants.outDir + "/test_nbbo_8", NBBO_PATH_STR);
		} catch (Exception e) {
			// do nothing
		}
	}
}
