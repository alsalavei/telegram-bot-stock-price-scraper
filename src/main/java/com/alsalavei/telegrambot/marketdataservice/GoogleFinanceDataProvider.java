package com.alsalavei.telegrambot.marketdataservice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GoogleFinanceDataProvider implements MarketDataProvider {
    private final HttpClient client;

    public GoogleFinanceDataProvider() {
        // Initialize HttpClient once and reuse it
        this.client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
    }

    @Override
    public String getMarketData(String ticker) {
        // Define stock exchanges to query
        String[] exchanges = {"NASDAQ", "LON"};

        // Iterate over each exchange to find the stock price
        for (String exchange : exchanges) {
            try {
                // Send HTTP request and receive response as a string
                String response = sendHttpRequestAndGetResponse(exchange, ticker);
                // Extract and check the stock price from the HTML content
                String price = extractPriceFromHtml(response);
                if (!"Unknown Price".equals(price)) {
                    return String.format("The current price of %s on %s is: %s", ticker.toUpperCase(), exchange, price);
                }
            } catch (IOException | InterruptedException e) {
                // Handle potential errors during HTTP request or response handling
                e.printStackTrace();
            }
        }
        return String.format("GoogleFinance: Could not retrieve data for %s", ticker);
    }

    private String sendHttpRequestAndGetResponse(String exchange, String ticker) throws IOException, InterruptedException {
        // Construct the URL for the Google Finance page of the ticker
        String url = "https://www.google.com/finance/quote/" + ticker + ":" + exchange;

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Execute the HTTP request and obtain the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Return the body of the response if the status code is HTTP 200 (OK)
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            System.out.println("Failed to retrieve the webpage after redirection. Status code: " + response.statusCode());
            return "";
        }
    }

    private String extractPriceFromHtml(String htmlContent) {
        // Parse the HTML content to extract elements
        Document doc = Jsoup.parse(htmlContent);
        // Find the stock price element using CSS query
        Element priceElement = doc.selectFirst(".YMlKec.fxKbKc");

        // Return the stock price text if the element is found, otherwise "Unknown Price"
        return priceElement != null ? priceElement.text().trim() : "Unknown Price";
    }
}
