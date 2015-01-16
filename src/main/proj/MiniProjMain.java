package main.proj;

import java.text.DecimalFormat;

public class MiniProjMain {

	private static final DecimalFormat DF = new DecimalFormat("##.##");
	private static final String outputFileName = Constants.outDir + "/sortedFile";
	private static final String USAGE = "java MiniProjMain <tradeFileName> <nbboFileName>\n";
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Wrong format. Usage:" + USAGE);
		}
		String tradeFileName = args[0];
		String nbboFileName = args[1];
		TradeFileReader reader = new TradeFileReader(Constants.outDir + "/preProcessedFile");
		ExternalSorter sorter = new ExternalSorter();
		
		preProcess(reader, tradeFileName, nbboFileName);
		
		externalSort(reader, sorter);
	}
	
	private static void preProcess(TradeFileReader reader, String tradeFileName, String nbboFileName) {
		try {
			long beforePreProcess = System.nanoTime();
			System.out.println("Started preprocess.");
			reader.preProcessRawTradeFile(Constants.outDir + "/" + tradeFileName);
			reader.preProcessRawNbboFile(Constants.outDir + "/" + nbboFileName);
			long afterPreProcess = System.nanoTime();
			System.out.println("Preprocessing completed. Time taken: " + nanoToMilliScdStr(afterPreProcess - beforePreProcess) + " milliseconds ("
					+ nanoToMinuteStr(afterPreProcess - beforePreProcess) + " minutes)");
		} catch (Exception e) {
			System.out.println("Exception during preprocessing: " + e.getMessage());
		}
	}
	
	private static void externalSort(TradeFileReader reader, ExternalSorter sorter){
		try {
			long beforeEs = System.nanoTime();
			System.out.println("Started external sort.");
			sorter.externalSort(reader.getOutFileName(), outputFileName);
			long afterEs = System.nanoTime();
			System.out.println("External sort completed. Time taken: " + nanoToMilliScdStr(afterEs - beforeEs) + " milliseconds ("
					+ nanoToMinuteStr(afterEs - beforeEs) + " minutes)");
			System.out.println("Output file:" + outputFileName);
		} catch (Exception e) {
			System.out.println("Exception during external sort: " + e.getMessage());
		}
	}

	public static String nanoToMilliScdStr(long nano) {
		return DF.format(((double) nano) / 1000 / 1000);
	}

	public static String nanoToMinuteStr(long nano) {
		return DF.format(((double) nano) / 1000 / 1000 / 1000 / 60);
	}
}
