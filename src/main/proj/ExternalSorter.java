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
		Collections.sort(tmplist, cmp); 
		File newtmpfile = File.createTempFile("externalsort", "tempfile");
		newtmpfile.deleteOnExit(); // Java VM deletes temp file upon exit.
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

	/**
	 * Merge method
	 * Make use of a priority queue to manage all the buffers
	 * Each buffers actually stores 1 line. 
	 * @param files
	 * @param outputfile
	 * @param cmp
	 * @throws IOException
	 */
	public static void mergeSortedFiles(List<File> files, File outputfile, final Comparator<String> cmp) throws IOException {
		PriorityQueue<LineBuffer> pq = new PriorityQueue<LineBuffer>(11, new Comparator<LineBuffer>() {
			public int compare(LineBuffer i, LineBuffer j) {
				return cmp.compare(i.peek(), j.peek());
			}
		});
		for (File f : files) {
			LineBuffer lb = new LineBuffer(f);
			pq.add(lb);
		}
		BufferedWriter fbw = new BufferedWriter(new FileWriter(outputfile));
		try {
			while (pq.size() > 0) {
				LineBuffer lb = pq.poll();
				String r = lb.pop();
				fbw.write(r);
				fbw.newLine();
				if (lb.empty()) {
					lb.closeBufferedReader();
				} else {
					pq.add(lb); // add it back and let pq to re-organize
				}
			}
		} finally {
			fbw.close();
			for (LineBuffer lb : pq)
				lb.closeBufferedReader();
		}
	}

	public void externalSort(String inputFilePath, String outputFilePath) throws IOException {
		List<File> tempFiles = sortInBatch(new File(inputFilePath), COMPARATOR);
		mergeSortedFiles(tempFiles, new File(outputFilePath), COMPARATOR);
	}
}

class LineBuffer {
	private BufferedReader fbr;
	private String currLine;
	private boolean isEmpty;

	public LineBuffer(File f) throws IOException {
		fbr = new BufferedReader(new FileReader(f));
		reload();
	}

	public boolean empty() {
		return isEmpty;
	}

	private void reload() throws IOException {
		try {
			if ((this.currLine = fbr.readLine()) == null) {
				isEmpty = true;
				currLine = null;
			} else {
				isEmpty = false;
			}
		} catch (EOFException oef) {
			isEmpty = true;
			currLine = null;
		}
	}

	public void closeBufferedReader() throws IOException {
		fbr.close();
	}

	public String peek() {
		if (empty())
			return null;
		return currLine.toString();
	}

	public String pop() throws IOException {
		String result = peek();
		reload();
		return result;
	}

}