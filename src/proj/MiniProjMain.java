package proj;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

import com.google.code.externalsorting.ExternalSort;

public class MiniProjMain {

	private static final DecimalFormat DF = new DecimalFormat("##.##");
	private static int DEFAULT_MAXTEMPFILES = 1024;
	private static final String outputFileName = Constants.outDir + "\\sortedFile";
	private static final Comparator<String> COMPARATOR = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			long result = Long.valueOf(o1.substring(0, 9)) - Long.valueOf(o2.substring(0, 9));
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	public static void main(String[] args) {
		try {
			TradeFileReader reader = new TradeFileReader("../project/preProcessedFile");
			long beforePreProcess = System.nanoTime();
			reader.preProcessRawTradeFile("C:\\Working\\workspaceLuna\\project\\test_trade_1");
			reader.preProcessRawNbboFile("C:\\Working\\workspaceLuna\\project\\test_nbbo_1");
			long afterPreProcess = System.nanoTime();
			System.out.println("Preprocessing completed. Time taken: " + nanoToMinuteStr(afterPreProcess - beforePreProcess) + " minutes");

			System.out.println("Started external sort. Time taken: " + nanoToMinuteStr(afterPreProcess - beforePreProcess) + " minutes");
			long beforeEs = System.nanoTime();
			List<File> list = ExternalSort.sortInBatch(new File(reader.getOutFileName()), COMPARATOR, DEFAULT_MAXTEMPFILES, Constants.DEFAULT_CHARSET,
					new File("C:\\Working\\workspaceLuna\\project"), false, 0, false);
			ExternalSort.mergeSortedFiles(list, new File(outputFileName), COMPARATOR, Constants.DEFAULT_CHARSET, false, false, false);
			long afterEs = System.nanoTime();
			System.out.println("External sort completed. Time taken: " + nanoToMinuteStr(afterEs - beforeEs) + " minutes");
			System.out.println("Output file:" + outputFileName);
		} catch (Exception e) {
			// do nothing
		}
	}

	public static String nanoToMinuteStr(long nano) {
		return DF.format(((double) nano) / 1000 / 1000 / 60);
	}
}
