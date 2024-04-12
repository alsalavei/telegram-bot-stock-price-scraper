package com.alsalavei.telegrambot.yahoofinancescraper;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");
        if (botToken == null) {
            throw new RuntimeException("'BOT_TOKEN' is not set in the environment variable");
        }

        try (TelegramBotsLongPollingApplication botApplication = new TelegramBotsLongPollingApplication()) {
            botApplication.registerBot(botToken, new YahooFinanceScraper(botToken));
            System.out.println("YahooFinanceScraper successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
