package main.proj;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class Util {
	private static final DecimalFormat DF = new DecimalFormat("##.##");
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

	public static String nanoToMilliScdStr(long nano) {
		return DF.format(((double) nano) / 1000 / 1000);
	}

	public static String nanoToMinuteStr(long nano) {
		return DF.format(((double) nano) / 1000 / 1000 / 1000 / 60);
	}
	
	public static long printStartMsg(String prefix, String fnName){
		System.out.println(prefix + "Started " + fnName);
		return System.nanoTime();
	}
	
	public static void printEndMsg(String prefix, String fnName, long startTime){
		long now = System.nanoTime();
		System.out.println(prefix + fnName + " completed. Time taken: " + Util.nanoToMilliScdStr(now - startTime) + " milliseconds ("
				+ Util.nanoToMinuteStr(now - startTime) + " minutes)");
	}
}
