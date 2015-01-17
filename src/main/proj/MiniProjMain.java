package main.proj;


public class MiniProjMain {

	
	private static final String outputFileName = Constants.outDir + "/sortedFile";
	private static final String USAGE = "java MiniProjMain <tradeFileName> <nbboFileName>\n";
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Wrong format. Usage:" + USAGE);
		}
		String tradeFileName = args[0];
		String nbboFileName = args[1];
		String preProcessedFileName = Constants.outDir + "/preProcessedFile";
		ExternalSorter sorter = new ExternalSorter();
		
		TradeFileReader reader = new TradeFileReader(preProcessedFileName);
		preProcess(reader, tradeFileName, nbboFileName);
		
		externalSort(reader.getOutFileName(), sorter);
//		externalSort(preProcessedFileName, sorter);
	}
	
	private static void preProcess(TradeFileReader reader, String tradeFileName, String nbboFileName) {
		try {
			long startTime = Util.printStartMsg("", "preprocess");
			reader.preProcessRawTradeFile(Constants.outDir + "/" + tradeFileName);
			reader.preProcessRawNbboFile(Constants.outDir + "/" + nbboFileName);
			Util.printEndMsg("", "Preprocessing", startTime);
		} catch (Exception e) {
			System.out.println("Exception during preprocessing: " + e.getMessage());
		}
	}
	
	private static void externalSort(String inFileStr, ExternalSorter sorter){
		try {
			long startTime = Util.printStartMsg("", "external sort");
			sorter.externalSort(inFileStr, outputFileName);
			Util.printEndMsg("", "External sort", startTime);
			System.out.println("Output file:" + outputFileName);
		} catch (Exception e) {
			System.out.println("Exception during external sort: " + e.getMessage());
		}
	}

}
