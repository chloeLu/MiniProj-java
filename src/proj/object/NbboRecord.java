package proj.object;

public class NbboRecord implements Record{
	private final String time;
	private final String symbol;
	private final String ask_price;
	private final String ask_size;
	private final String bid_price;
	private final String bid_size;
	
	public NbboRecord(String time, String symbol, String ask_price, String ask_size, String bid_price, String bid_size) {
		this.time = time;
		this.symbol = symbol;
		this.ask_price = ask_price;
		this.ask_size = ask_size;
		this.bid_price = bid_price;
		this.bid_size = bid_size; 
	}
	
	public final String getRecordType() {
		return RecordType.N.name();
	}
	
	@Override
	public String getTime() {
		return time;
	}
	
	public String getSymbol() {
		return symbol;
	}

	public String getAsk_price() {
		return ask_price;
	}

	public String getAsk_size() {
		return ask_size;
	}

	public String getBid_price() {
		return bid_price;
	}

	public String getBid_size() {
		return bid_size;
	}
}
