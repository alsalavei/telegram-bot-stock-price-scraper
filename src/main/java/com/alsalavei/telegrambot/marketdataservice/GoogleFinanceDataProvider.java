package com.alsalavei.telegrambot.marketdataservice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GoogleFinanceDataProvider implements MarketDataProvider {

    @Override
    public String getMarketData(String ticker) {
        String[] exchanges = {"NASDAQ", "LON"};

        for (String exchange : exchanges) {
            String url = "https://www.google.com/finance/quote/" + ticker + ":" + exchange;
            try {
                String price = getPriceFromElement(url);
                if (!"Unknown Price".equals(price)) {
                    return "The current price of " + ticker.toUpperCase() + " on " + exchange + " is: " + price;
                }
            } catch (Exception e) {
                // Logs if needed
            }
        }
        return "GoogleFinance: Could not retrieve data for " + ticker;
    }

    private String getPriceFromElement(String url) throws Exception {
        Document doc = Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                .get();
        // Extracting the stock price from CSS Element
        Element priceElement = doc.selectFirst("div.YMlKec.fxKbKc");
        return priceElement != null ? priceElement.text() : "Unknown Price";
    }
}