package com.alsalavei.telegrambot.marketdataservice;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");

        try (TelegramBotsLongPollingApplication botApplication = new TelegramBotsLongPollingApplication()) {
            botApplication.registerBot(botToken, new MarketDataService(botToken));
            System.out.println("MarketDataService successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
