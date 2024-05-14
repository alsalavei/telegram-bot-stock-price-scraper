package com.alsalavei.telegrambot.marketdataservice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YahooFinanceDataProvider implements MarketDataProvider {
    @Override
    public String getMarketData(String ticker) {
        try {
            String url = "https://finance.yahoo.com/quote/" + ticker;
            Document doc = Jsoup.connect(url).get();

            // Extracting the stock price from CSS Element
            Element priceElement = doc.selectFirst(String.format("fin-streamer[data-symbol='%s'][data-testid='qsp-price']", ticker));
            String price = priceElement != null && !"-".equals(priceElement.text()) && !priceElement.text().isEmpty() ? priceElement.text() : "Unknown Price";

            return (!"Unknown Price".equals(price))
                    ? "The current price of " + ticker.toUpperCase() + " is: " + price
                    : "Could not retrieve data for " + ticker;
        } catch (Exception e) {
            return "Error: Could not retrieve data for " + ticker;
        }
    }
}
