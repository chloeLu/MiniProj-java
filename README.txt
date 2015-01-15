IMPORTANT NOTES: 

1. Data folder directory is hardcoded: <jar dir>/data. Ensure that this folder exists before running jar. ALL data files should go under this folder!

2. We use these 2 data sets as initial data set:
ftp://ftp.nyxdata.com/Historical%20Data%20Samples/Daily%20TAQ/EQY_US_ALL_NBBO_20131218.zip
ftp://ftp.nyxdata.com/Historical%20Data%20Samples/Daily%20TAQ/EQY_US_ALL_TRADE_20131218.zip
Extract NBBO zip under /data and rename the file as "taqnbbo20131218"
Extract Trade zip under /data and rename the file as "taqtrade20131218"
(These are hardcoded in TestDataGenerator)

3. Considering the humongous size of the data, we make use of an open-source external sort library: https://code.google.com/p/externalsortinginjava/

-------------------------------------------------

REQUIRED ENV

Java 7 is needed

-------------------------------------------------

USER MANUAL

1. TestDataGenerator - generate test data
Goal: generate test data based on existing trade/nbbo file (the one downloaded from website)
Usage:
	a) java -cp MiniProj-0.0.1-jar-with-dependencies.jar main.proj.TestDataGenerator
	Effect: generate 8 sets of trade and nbbo data using exact format as provided under data folder.
	Name of the test files are test_trade_(1-8) and test_nbbo_(1-8). 
	Number of lines each file contains:
	test_x_1: 100
	test_x_2: 100
	test_x_3: 200
	test_x_4: 200
	test_x_5: 1
	test_x_6: 1
	test_x_7: 1
	test_x_8: 10000
	b) java TestDataGenerator <baseTradeFilePath> <targetTFileNumLines> <baseNbboFilePath> <targetNbboFileNumLines>
	Example: java -cp MiniProj-0.0.1-jar-with-dependencies.jar main.proj.TestDataGenerator taqtrade20131218 100 taqnbbo20131218 100
	Effect: generate 1 set of trade and nbbo data using exact format as provided.
	Generated test data is named "test_trade_custom" and "test_nbbo_custom".
	

2. MiniProjMain - Main class for the sorting function
Usage: java -cp MiniProj-0.0.1.jar main.proj.MiniProjMain <testRawTradeFile> <testRawNbboFile>
Example: java -cp MiniProj-0.0.1.jar main.proj.MiniProjMain test_trade_1 test_nbbo_6
Output: combined entries sorted by time (first 9 bytes)
		In output file, trade entries only include time[9]/symbol[16]/qty[9]/price[11]; 
		nbbo entries include time[9]/symbol[16]/bid_price[11]/bid_size[7]/ask_price[11]/ask_size[7]
InMoreDetails: Process takes 2 parts
		1:generate preProcessedFile. This file contains the combined trade&nbbo data with only necessary fields
		2:external sort based on preProcessedFile
-------------------------------------------------

PERFORMANCE

Generating output file for raw data (trade:~2G; nbbo:~22G) takes less than 30 mins on my desktop PC (normal hardware, 4-core-CPU 8GB ram no SSD)
Preprocessing (discarding unused parts & combine) takes ~6 mins.
Sorting takes ~20 mins

Sample log:

E:\Working\workspaceLuna\MiniProj-java>java -cp MiniProj-0.0.1.jar main.proj.MiniProjMain taqtrade20131218 taqnbbo20131218
Started preprocess.
Preprocessing completed. Time taken: 333633.67 milliseconds (5.56 minutes)
Started external sort.
External sort completed. Time taken: 1254864.47 milliseconds (20.91 minutes)
Output file:./data/sortedFile

-------------------------------------------------