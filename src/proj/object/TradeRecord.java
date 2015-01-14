package proj.object;

public class TradeRecord implements Record {
	private final String time;
	private final String symbol;
	private final String qty;
	private final String price;
	
	public TradeRecord(String time, String symbol, String qty, String price) {
		this.time = time;
		this.symbol = symbol;
		this.qty = qty;
		this.price = price; 
	}
	
	public final String getRecordType() {
		return RecordType.T.name();
	}
	
	public String getSymbol() {
		return symbol;
	}

	public String getQty() {
		return qty;
	}

	public String getPrice() {
		return price;
	}

	@Override
	public String getTime() {
		return time;
	}
}
