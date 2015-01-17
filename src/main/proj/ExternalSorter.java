package main.proj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ExternalSorter {
	private static final int MAXTEMPFILES = 1024;
	private static final String LOG_PREFIX = "---->";
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
		System.out.println("Estimated best temp file size:" + tempFileSize);
		return tempFileSize;
	}

	public List<File> sortInBatch(File file) throws IOException {
		List<File> files = new ArrayList<File>();
		BufferedReader fbr = new BufferedReader(new FileReader(file));
		long blocksize = estimateBestTempFileSize(file);// in bytes
		try {
			List<String> tempLines = new ArrayList<String>();
			String line = "";
			int count = 1; // for output purpose
			try {
				while (line != null) {
					long currentblocksize = 0;// in bytes
					while ((currentblocksize < blocksize) && ((line = fbr.readLine()) != null)) {
						tempLines.add(line);
						currentblocksize += line.length();
					}
					files.add(sortAndSave(tempLines, count));
					tempLines.clear();
					count++;
				}
			} catch (EOFException oef) {
				if (tempLines.size() > 0) {
					files.add(sortAndSave(tempLines, count));
					tempLines.clear();
				}
			}
		} finally {
			fbr.close();
		}
		return files;
	}

	public File sortAndSave(List<String> tmplist, int id) throws IOException {
		Collections.sort(tmplist, COMPARATOR);
		File newtmpfile = File.createTempFile("externalsort_tempfile_","s");
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
		System.out.println("Created temp file: " + id + ": " + newtmpfile.getAbsolutePath());
		return newtmpfile;
	}

	/**
	 * Merge method Make use of a priority queue to manage all the buffers Each buffers actually stores 1 line.
	 * 
	 * @param files
	 * @param outputfile
	 * @param cmp
	 * @throws IOException
	 */
	public static void mergeSortedFiles(List<File> files, File outputfile) throws IOException {
		PriorityQueue<LineBuffer> pq = new PriorityQueue<LineBuffer>(11, new Comparator<LineBuffer>() {
			public int compare(LineBuffer i, LineBuffer j) {
				return COMPARATOR.compare(i.peek(), j.peek());
			}
		});
		for (File f : files) {
			LineBuffer lb = new LineBuffer(f);
			pq.add(lb);
		}
		BufferedWriter fbw = new BufferedWriter(new FileWriter(outputfile));
		System.out.println("Merging temp files.........");
		try {
			while (pq.size() > 0) {
				LineBuffer lb = pq.poll();
				String r = lb.pop();
				fbw.write(r);
				fbw.newLine();
				if (lb.isEmpty()) {
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

	public void externalSort(String inputFileStr, String outputFileStr) throws IOException {
		
		long startTime = Util.printStartMsg(LOG_PREFIX, "sortInBatch");
		List<File> tempFiles = sortInBatch(new File(inputFileStr));
		Util.printEndMsg(LOG_PREFIX, "SortInBatch", startTime);
		
		startTime = Util.printStartMsg(LOG_PREFIX, "merge");
		mergeSortedFiles(tempFiles, new File(outputFileStr));
		Util.printEndMsg(LOG_PREFIX, "SortInBatch", startTime);
	}
}

class LineBuffer {
	private BufferedReader fbr;
	private String currLine;
	private boolean isEmpty;
	private File origFile;

	public LineBuffer(File f) throws IOException {
		origFile = f;
		fbr = new BufferedReader(new FileReader(f));
		reload();
	}

	public boolean isEmpty() {
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
		if (isEmpty())
			return null;
		return currLine.toString();
	}

	public String pop() throws IOException {
		String result = peek();
		reload();
		return result;
	}

	public void printDoneNotification() {
		System.out.println("Done, removing temp file from the list: " + origFile.getAbsolutePath());
	}

}