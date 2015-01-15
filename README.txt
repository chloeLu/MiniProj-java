IMPORTANT NOTES: 

1. Data folder directory is hardcoded: <jar dir>/data. Ensure that this folder exists before running jar. ALL data files should go under this folder!

2. We use these 2 data sets as initial data set:
ftp://ftp.nyxdata.com/Historical%20Data%20Samples/Daily%20TAQ/EQY_US_ALL_NBBO_20131218.zip
ftp://ftp.nyxdata.com/Historical%20Data%20Samples/Daily%20TAQ/EQY_US_ALL_TRADE_20131218.zip
Extract NBBO zip under /data and rename the file as "taqnbbo20131218"
Extract Trade zip under /data and rename the file as "taqtrade20131218"
(These are also hardcoded values)

3. Considering the humongous size of the data, we make use of an open-source external sort library, which can be found here:
https://code.google.com/p/externalsortinginjava/


-------------------------------------------------

USER MANUAL

1. TestDataGenerator - generate test data
Goal: generate test data using existing trade/nbbo file
Usage:
	a) java -cp MiniProj-java-0.0.1.jar main.proj.TestDataGenerator
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
	Example: java -cp MiniProj-java-0.0.1.jar main.proj.TestDataGenerator taqtrade20131218 100 taqnbbo20131218 100
	Effect: generate 1 set of trade and nbbo data using exact format as provided.
	Generated test data is named "test_trade_custom" and "test_nbbo_custom".
	

2. TradeFileReader