Content & Usage

1. TestDataGenerator
Goal: generate test data using existing trade/nbbo file
Usage:
	a) no argument: generate 8 sets of trade and nbbo data using exact format as provided.
	Name of the test files are test_trade_(1-8) and test_nbbo_(1-8). 
	The test files contains different number of lines:
	test_x_1: 100
	test_x_2: 100
	test_x_3: 200
	test_x_4: 200
	test_x_5: 1
	test_x_6: 1
	test_x_7: 1
	test_x_8: 10000
	b) with argument: generate 1 set of trade and nbbo data using exact format as provided.
	Usage: java TestDataGenerator <baseTradeFilePath> <targetTFileNumLines> <baseNbboFilePath> <targetNbboFileNumLines>
	Generated test data will be named "test_trade_custom" and "test_nbbo_custom".
	

2. TradeFileReader