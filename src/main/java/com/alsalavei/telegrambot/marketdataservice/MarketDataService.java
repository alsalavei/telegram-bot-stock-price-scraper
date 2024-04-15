package com.alsalavei.telegrambot.marketdataservice;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MarketDataService implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private MarketDataProvider yahooFinanceDataProvider;
    private MarketDataProvider googleFinanceDataProvider;

    public MarketDataService(String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
        this.yahooFinanceDataProvider = new YahooFinanceDataProvider();
        this.googleFinanceDataProvider = new GoogleFinanceDataProvider();
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
        System.out.println(data);
        if (data.contains("Could not retrieve data") || data.isEmpty()) {
            data = googleFinanceDataProvider.getMarketData(ticker);
        }
        return data;
    }
}