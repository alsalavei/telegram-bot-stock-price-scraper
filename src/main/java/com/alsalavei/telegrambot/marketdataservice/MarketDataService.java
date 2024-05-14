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
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String ticker = update.getMessage().getText();
            String responseMessage = getMarketData(ticker);

            if (messageText.equals(ticker)) {
                SendMessage message = SendMessage // Create a message object
                        .builder()
                        .chatId(chatId)
                        .text(responseMessage)
                        .build();
                try {
                    telegramClient.execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                SendMessage message = SendMessage // A block is required when creating telegram commands
                        .builder()
                        .chatId(chatId)
                        .text("Unknown command")
                        .build();
                try {
                    telegramClient.execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getMarketData(String ticker) {
        String data = yahooFinanceDataProvider.getMarketData(ticker);
        if (data.contains("Could not retrieve data") || data.isEmpty()) {
            data = googleFinanceDataProvider.getMarketData(ticker);
        }
        return data;
    }
}