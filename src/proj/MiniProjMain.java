package proj;

import java.text.DecimalFormat;

public class MiniProjMain {

	private static final DecimalFormat DF = new DecimalFormat("##.##");

	public static void main(String[] args) {
		try {
			TradeFileReader reader = new TradeFileReader("../project/preProcessedFile");
			long beforePreProcess = System.nanoTime();
			reader.preProcessRawTradeFile("C:\\Working\\workspaceLuna\\project\\test_trade_5");
			reader.preProcessRawNbboFile("C:\\Working\\workspaceLuna\\project\\test_nbbo_5");
			long afterPreProcess = System.nanoTime();
			System.out.println("Preprocessing completed. Time taken: " + nanoToMinuteStr(afterPreProcess-beforePreProcess)
					+ " minutes");
		} catch (Exception e) {
			// do nothing
		}
	}
	
	
	public static String nanoToMinuteStr(long nano){
		return DF.format(((double) nano) / 1000 / 1000 / 60);
	}
}
