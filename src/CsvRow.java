public class CsvRow {
	private long timeStamp;
	private double closePrice;
	private double highPrice;
	private double lowPrice;
	private double openPrice;
	private long volume;

	public CsvRow(long timeStamp, double closePrice, double highPrice,
			double lowPrice, double openPrice, long volume) {
		this.timeStamp = timeStamp;
		this.closePrice = closePrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.openPrice = openPrice;
		this.volume = volume;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public double getClosePrice() {
		return closePrice;
	}

	public double getHighPrice() {
		return highPrice;
	}

	public double getLowPrice() {
		return lowPrice;
	}

	public double getOpenPrice() {
		return openPrice;
	}

	public long getVolume() {
		return volume;
	}

	@Override
	public String toString() {
		/*
		 * return "CsvRow [timeStamp=" + timeStamp + ", closePrice=" +
		 * closePrice + ", highPrice=" + highPrice + ", lowPrice=" + lowPrice +
		 * ", openPrice=" + openPrice + ", volume=" + volume + "]";
		 */
		return String.format("%s: $%.4f", new java.util.Date(
				(long) timeStamp * 1000), closePrice);
	}
}
