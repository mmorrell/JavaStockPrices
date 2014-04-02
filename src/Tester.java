public class Tester {

	private static double minimumPrice = Double.MAX_VALUE, maximumPrice = 0;

	public static void main(String[] args) {
		String symbol = "ILMN";
		CsvData spyData = new CsvData(symbol);
		CsvRow[] priceArray = spyData.getPrices();

		for (int i = 0; i < priceArray.length; i++) {
			System.out.printf("#%d [%s]: %s", i + 1, symbol, priceArray[i]);

			// 5 period simple moving average
			if (i >= 4) {
				double fivePeriodMa = getSimpleMovingAverage(5, priceArray, i);
				System.out.printf(" 5sma: $%.2f", fivePeriodMa);
			}

			// 20 period simple moving average
			if (i >= 19) {
				double twentyPeriodMa = getSimpleMovingAverage(20, priceArray, i);
				System.out.printf(" 20sma: $%.2f", twentyPeriodMa);
			}

			processPrice(priceArray[i].getClosePrice());
			System.out.print("\n");
		}

		System.out.printf(
				"Low price: %.2f -- High Price: %.2f -- Range: $%.2f",
				minimumPrice, maximumPrice, maximumPrice - minimumPrice);

	}

	private static void processPrice(double price) {
		if (price < minimumPrice) {
			minimumPrice = price;
		}
		if (price > maximumPrice) {
			maximumPrice = price;
		}
	}

	private static double getSimpleMovingAverage(int smaPeriod, CsvRow[] prices, int index){
		double movingAverage = 0;
		if (smaPeriod > 0){
			for (int i = 0; i < smaPeriod; i++) {
				movingAverage += prices[index - i].getClosePrice();
			}
			movingAverage = movingAverage / smaPeriod;
		}
		return movingAverage;
	}
}
