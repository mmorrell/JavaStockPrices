import java.util.Deque;
import java.util.LinkedList;
import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class CsvData {
	private Deque<CsvRow> dataDeque = new LinkedList<CsvRow>();
	private String stockSymbol;

	public CsvData(String stockSymbol) {
		if (stockSymbol != null && !stockSymbol.isEmpty()) {
			this.stockSymbol = stockSymbol;
			parseYahooFinance();
		}
	}

	private void parseYahooFinance() {
		String yahooUrl = String
				.format("http://chartapi.finance.yahoo.com/instrument/1.0/%s/chartdata;type=quote;range=1d/csv",
						stockSymbol);
		String csvText = getHtml(yahooUrl);

		String[] csvLines = csvText.split("\n");

		boolean readingData = false;
		for (String line : csvLines) {
			if (readingData) {
				String[] rowValues = line.split(",");
				if (rowValues.length == 6) {
					CsvRow csvRow = new CsvRow(Long.parseLong(rowValues[0]),
							Double.parseDouble(rowValues[1]),
							Double.parseDouble(rowValues[2]),
							Double.parseDouble(rowValues[3]),
							Double.parseDouble(rowValues[4]),
							Long.parseLong(rowValues[5]));
					dataDeque.addLast(csvRow);
				}
			} else {
				if (line.contains("volume:")) {
					readingData = true;
				}
			}
		}
	}

	public CsvRow[] getPrices() {
		return dataDeque.toArray(new CsvRow[0]);
	}

	public String getHtml(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String responseBody = "";
		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity)
								: null;
					} else {
						throw new ClientProtocolException(
								"Unexpected response status: " + status);
					}
				}
			};
			responseBody = httpclient.execute(httpget, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseBody;
	}

	public long getNumRows() {
		return dataDeque.size();
	}
}
