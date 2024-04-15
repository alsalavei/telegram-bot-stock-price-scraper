package com.alsalavei.telegrambot.marketdataservice;

public class NasdaqDataProvider implements MarketDataProvider {
    @Override
    public String getMarketData(String ticker) {
        return ticker;
    }
}
