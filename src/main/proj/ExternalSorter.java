package main.proj;

import java.util.*;
import java.io.*;

public class ExternalSorter {
	private static final int MAXTEMPFILES = 1024;
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

	/**
	 * Estimate best size of each block file <br>
	 * 
	 * @param origFile
	 * @return
	 */
	public static long estimateBestTempFileSize(File origFile) {
		long origFileSize = origFile.length();
		long tempFileSize = origFileSize / MAXTEMPFILES;
		long freemem = Runtime.getRuntime().freeMemory();
		if (tempFileSize >= freemem) { // block size larger than free memory, exit
			System.err.println("Not enough memory... existing.");
			System.exit(0);
		}
		if (tempFileSize < freemem / 2) {
			tempFileSize = freemem / 2;
		}
		return tempFileSize;
	}

	public static List<File> sortInBatch(File file, Comparator<String> cmp) throws IOException {
		List<File> files = new ArrayList<File>();
		BufferedReader fbr = new BufferedReader(new FileReader(file));
		long blocksize = estimateBestTempFileSize(file);// in bytes
		try {
			List<String> tempLines = new ArrayList<String>();
			String line = "";
			try {
				while (line != null) {
					long currentblocksize = 0;// in bytes
					while ((currentblocksize < blocksize) && ((line = fbr.readLine()) != null)) {
						tempLines.add(line);
						currentblocksize += line.length();
					}
					files.add(sortAndSave(tempLines, cmp));
					tempLines.clear();
				}
			} catch (EOFException oef) {
				if (tempLines.size() > 0) {
					files.add(sortAndSave(tempLines, cmp));
					tempLines.clear();
				}
			}
		} finally {
			fbr.close();
		}
		return files;
	}

	public static File sortAndSave(List<String> tmplist, Comparator<String> cmp) throws IOException {
		Collections.sort(tmplist, cmp); //
		File newtmpfile = File.createTempFile("externalsort", "tempfile");
		newtmpfile.deleteOnExit();
		BufferedWriter fbw = new BufferedWriter(new FileWriter(newtmpfile));
		try {
			for (String r : tmplist) {
				fbw.write(r);
				fbw.newLine();
			}
		} finally {
			fbw.close();
		}
		return newtmpfile;
	}

	// This merges a bunch of temporary flat files
	// @param files
	// @param output file
	// @return The number of lines sorted. (P. Beaudoin)

	public static int mergeSortedFiles(List<File> files, File outputfile, final Comparator<String> cmp) throws IOException {
		PriorityQueue<BinaryFileBuffer> pq = new PriorityQueue<BinaryFileBuffer>(11, new Comparator<BinaryFileBuffer>() {
			public int compare(BinaryFileBuffer i, BinaryFileBuffer j) {
				return cmp.compare(i.peek(), j.peek());
			}
		});
		for (File f : files) {
			BinaryFileBuffer bfb = new BinaryFileBuffer(f);
			pq.add(bfb);
		}
		BufferedWriter fbw = new BufferedWriter(new FileWriter(outputfile));
		int rowcounter = 0;
		try {
			while (pq.size() > 0) {
				BinaryFileBuffer bfb = pq.poll();
				String r = bfb.pop();
				fbw.write(r);
				fbw.newLine();
				++rowcounter;
				if (bfb.empty()) {
					bfb.fbr.close();
					bfb.originalfile.delete();// we don't need you anymore
				} else {
					pq.add(bfb); // add it back
				}
			}
		} finally {
			fbw.close();
			for (BinaryFileBuffer bfb : pq)
				bfb.close();
		}
		return rowcounter;
	}

	public void externalSort(String inputFilePath, String outputFilePath) throws IOException {
		List<File> tempFiles = sortInBatch(new File(inputFilePath), COMPARATOR);
		mergeSortedFiles(tempFiles, new File(outputFilePath), COMPARATOR);
	}
}

class BinaryFileBuffer {
	public static int BUFFERSIZE = 2048;
	public BufferedReader fbr;
	public File originalfile;
	private String cache;
	private boolean empty;

	public BinaryFileBuffer(File f) throws IOException {
		originalfile = f;
		fbr = new BufferedReader(new FileReader(f), BUFFERSIZE);
		reload();
	}

	public boolean empty() {
		return empty;
	}

	private void reload() throws IOException {
		try {
			if ((this.cache = fbr.readLine()) == null) {
				empty = true;
				cache = null;
			} else {
				empty = false;
			}
		} catch (EOFException oef) {
			empty = true;
			cache = null;
		}
	}

	public void close() throws IOException {
		fbr.close();
	}

	public String peek() {
		if (empty())
			return null;
		return cache.toString();
	}

	public String pop() throws IOException {
		String answer = peek();
		reload();
		return answer;
	}

}