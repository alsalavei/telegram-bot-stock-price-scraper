package com.alsalavei.telegrambot.marketdataservice;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MarketDataService implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private MarketDataProvider yahooFinanceDataProvider;
    private MarketDataProvider nasdaqDataProvider;

    public MarketDataService(String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.yahooFinanceDataProvider = new YahooFinanceDataProvider();
        this.nasdaqDataProvider = new NasdaqDataProvider();
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String ticker = update.getMessage().getText();
            String responseMessage = getMarketData(ticker);

            if (message_text.equals(ticker)) {
                // User send /start
                SendMessage message = SendMessage // Create a message object object
                        .builder()
                        .chatId(chat_id)
                        .text(responseMessage)
                        .build();
                try {
                    telegramClient.execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                // Unknown command
                SendMessage message = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text("Unknown command")
                        .build();
                try {
                    telegramClient.execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getMarketData(String ticker) {
        String data = yahooFinanceDataProvider.getMarketData(ticker);
        if (data.contains("error") || data.isEmpty()) {
            data = nasdaqDataProvider.getMarketData(ticker);
        }
        return data;
    }
}

/**
 * public class MarketDataService implements LongPollingSingleThreadUpdateConsumer {
 *     private final TelegramClient telegramClient;
 *
 *     public MarketDataService(String botToken) {
 *         telegramClient = new OkHttpTelegramClient(botToken);
 *     }
 *
 *     @Override
 *     public void consume(Update update) {
 *         if (update.hasMessage() && update.getMessage().hasText()) {
 *             // Set variables
 *             String message_text = update.getMessage().getText();
 *             long chat_id = update.getMessage().getChatId();
 *             String ticker = update.getMessage().getText();
 *             String responseMessage = getStockInfo(ticker);
 *
 *             if (message_text.equals(ticker)) {
 *                 // User send /start
 *                 SendMessage message = SendMessage // Create a message object object
 *                         .builder()
 *                         .chatId(chat_id)
 *                         .text(responseMessage)
 *                         .build();
 *                 try {
 *                     telegramClient.execute(message); // Sending our message object to user
 *                 } catch (TelegramApiException e) {
 *                     e.printStackTrace();
 *                 }
 *             } else {
 *                 // Unknown command
 *                 SendMessage message = SendMessage
 *                         .builder()
 *                         .chatId(chat_id)
 *                         .text("Unknown command")
 *                         .build();
 *                 try {
 *                     telegramClient.execute(message); // Sending our message object to user
 *                 } catch (TelegramApiException e) {
 *                     e.printStackTrace();
 *                 }
 *             }
 *         }
 *     }
 *
 *     private String getStockInfo(String ticker) {
 *         try {
 *             String url = "https://finance.yahoo.com/quote/" + ticker;
 *             Document doc = Jsoup.connect(url).get();
 *
 *             // Extracting the company name <h1 class="D(ib) Fz(18px)">Tesla, Inc. (TSLA)</h1>
 *             Element companyNameElement = doc.selectFirst("h1.D\\(ib\\).Fz\\(18px\\)");
 *             String companyName = companyNameElement != null ? companyNameElement.text() : "Unknown Company";
 *             // Extracting the stock price <fin-streamer class="Fw(b) Fz(36px) Mb(-4px) D(ib)" data-symbol="TSLA"
 *             Element priceElement = doc.selectFirst("fin-streamer.Fw\\(b\\).Fz\\(36px\\).Mb\\(-4px\\).D\\(ib\\)");
 *             String price = priceElement != null ? priceElement.text() : "Unknown Price";
 *             // Retrieve exchange name and currency <div class="C($tertiaryColor) Fz(12px)"><span>NasdaqGS
 *             Element exchangeInfoElement = doc.selectFirst(".C\\(\\$tertiaryColor\\).Fz\\(12px\\)");
 *             String exchangeInfo = exchangeInfoElement != null ? exchangeInfoElement.text() : "Unknown Exchange Information";
 *
 *             return (!"Unknown Company".equals(companyName) && !"Unknown Price".equals(price))
 *                     ? "The current price of " + companyName + " is: " + price + "\n" + exchangeInfo
 *                     : "Could not retrieve the stock data for " + ticker + ".";
 *         } catch (Exception e) {
 *             return "An error occurred while retrieving data for " + ticker + ".";
 *         }
 *     }
 * }
 */