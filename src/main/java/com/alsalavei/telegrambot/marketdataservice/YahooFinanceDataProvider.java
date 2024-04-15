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

            // Extracting the company name <h1 class="D(ib) Fz(18px)">Tesla, Inc. (TSLA)</h1>
            Element companyNameElement = doc.selectFirst("h1.D\\(ib\\).Fz\\(18px\\)");
            String companyName = companyNameElement != null ? companyNameElement.text() : "Unknown Company";

            // Extracting the stock price <fin-streamer class="Fw(b) Fz(36px) Mb(-4px) D(ib)" data-symbol="TSLA"
            //Element priceElement = doc.selectFirst("fin-streamer.Fw\\(b\\).Fz\\(36px\\).Mb\\(-4px\\).D\\(ib\\)");
            //Element priceElement = doc.select("fin-streamer[data-symbol=AAPL][data-field=regularMarketPrice]").first();
            Element priceElement = doc.selectFirst("fin-streamer[data-symbol=AAPL][data-field=regularMarketPrice]");
            String price = priceElement != null ? priceElement.text() : "Unknown Price";

            // Retrieve exchange name and currency <div class="C($tertiaryColor) Fz(12px)"><span>NasdaqGS
            Element exchangeInfoElement = doc.selectFirst(".C\\(\\$tertiaryColor\\).Fz\\(12px\\)");
            String exchangeInfo = exchangeInfoElement != null ? exchangeInfoElement.text() : "Unknown Exchange Information";

            return (!"Unknown Company".equals(companyName) && !"Unknown Price".equals(price))
                    ? "The current price of " + companyName + " is: " + price + "\n" + exchangeInfo
                    : "Could not retrieve the stock data for " + ticker + ".";
        } catch (Exception e) {
            return "An error occurred while retrieving data for " + ticker + ".";
        }
    }
}
