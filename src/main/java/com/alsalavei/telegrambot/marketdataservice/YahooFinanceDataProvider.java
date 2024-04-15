package com.alsalavei.telegrambot.marketdataservice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class YahooFinanceDataProvider implements MarketDataProvider {
    @Override
    public String getMarketData(String ticker) {
        try {
            String url = "https://finance.yahoo.com/quote/" + ticker;
            Document doc = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                    .get();

            // Extracting the stock price from CSS Element: <fin-streamer class="livePrice svelte-mgkamr" data-symbol="AAPL" data-testid="qsp-price" data-field="regularMarketPrice" data-trend="none" data-pricehint="2" data-value="176.55" active=""><span>176.55</span></fin-streamer>
            Element priceElement = doc.selectFirst(String.format("fin-streamer[data-symbol='%s'][data-testid='qsp-price']", ticker));
            String price = priceElement != null && !"-".equals(priceElement.text()) && !priceElement.text().isEmpty() ? priceElement.text() : "Unknown Price";

            return (!"Unknown Price".equals(price))
                    ? "The current price of " + ticker.toUpperCase() + " is: " + price
                    : "Could not retrieve data for " + ticker;
        } catch (Exception e) {
            return "Error: Could not retrieve data for for " + ticker;
        }
    }
}
