package com.alsalavei.telegrambot.marketdataservice;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");
        if (botToken == null) {
            throw new RuntimeException("'BOT_TOKEN' is not set in the environment variable");
        }

        try (TelegramBotsLongPollingApplication botApplication = new TelegramBotsLongPollingApplication()) {
            botApplication.registerBot(botToken, new MarketDataService(botToken));
            System.out.println("MarketDataService successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
