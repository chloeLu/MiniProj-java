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
	private final long NUMLINES;

	public TestDataGenerator(double probabilty, long numLines) {
		this.P = probabilty;
		this.NUMLINES = numLines;
	}
	
	public void generateRawTestFile(String outFileName, String base) throws IOException {
		Path path = FileSystems.getDefault().getPath("", base);
		BufferedReader reader = Files.newBufferedReader(path,
				StandardCharsets.UTF_8);
		String line = reader.readLine(); // skip the first line
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(outFileName, true),
						StandardCharsets.UTF_8));
		int count = 0;
		try {
			while ((line = reader.readLine()) != null && count < NUMLINES) {
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
			TestDataGenerator generator1 = new TestDataGenerator(0.001, 100);
			generator1.generateRawTestFile("test_trade_1", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
			generator1.generateRawTestFile("test_trade_2", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
			generator1.generateRawTestFile("test_nbbo_1", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
			generator1.generateRawTestFile("test_nbbo_2", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
			
			TestDataGenerator generator2 = new TestDataGenerator(0.0001, 200);
			generator2.generateRawTestFile("test_trade_3", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
			generator2.generateRawTestFile("test_trade_4", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
			generator2.generateRawTestFile("test_nbbo_3", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
			generator2.generateRawTestFile("test_nbbo_4", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
			
			TestDataGenerator generator3 = new TestDataGenerator(0.0001, 1);
			generator3.generateRawTestFile("test_trade_5", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
			generator3.generateRawTestFile("test_trade_6", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
			generator3.generateRawTestFile("test_trade_7", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
			generator3.generateRawTestFile("test_nbbo_5", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
			generator3.generateRawTestFile("test_nbbo_6", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
			generator3.generateRawTestFile("test_nbbo_7", "E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
			
			
			// reader.preProcessRawTradeFile("E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqtrade20131218");
//			reader.preProcessRawNbboFile("E:\\Working\\workspaceLuna\\MiniProj-java\\data\\taqnbbo20131218");
		} catch (Exception e) {
			// do nothing
		}
	}
}
