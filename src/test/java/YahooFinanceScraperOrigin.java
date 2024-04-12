import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Scanner;

public class YahooFinanceScraper1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the stock ticker: ");
        String tickerYahoo = scanner.nextLine().toUpperCase();

        try {
            String url = "https://finance.yahoo.com/quote/" + tickerYahoo;
            Document doc = Jsoup.connect(url).get();

            // Извлекаем название компании <h1 class="D(ib) Fz(18px)">Tesla, Inc. (TSLA)</h1>
            Element companyNameElement = doc.selectFirst("h1.D\\(ib\\).Fz\\(18px\\)");
            String companyName = companyNameElement != null ? companyNameElement.text() : "Unknown Company";

            // Извлекаем цену акции <fin-streamer class="Fw(b) Fz(36px) Mb(-4px) D(ib)" data-symbol="TSLA"
            Element priceElement = doc.selectFirst("fin-streamer.Fw\\(b\\).Fz\\(36px\\).Mb\\(-4px\\).D\\(ib\\)");
            String price = priceElement != null ? priceElement.text() : "Unknown Price";

            Element stockExchangeElement = doc.selectFirst("div.C\\(\\$tertiaryColor\\)\\.Fz\\(12px\\)");
            String stockExchange = stockExchangeElement != null ? stockExchangeElement.text() : "NasdaqGS Info Unavailable";

            //<div class="C($tertiaryColor) Fz(12px)"><span>NasdaqGS - NasdaqGS Real Time Price. Currency in USD</span></div>

            // Выводим результат
            if (!"Unknown Company".equals(companyName) && !"Unknown Price".equals(price)) {
                System.out.println("The current price of " + companyName + " is: " + price + stockExchange);
            } else {
                System.out.println("Could not retrieve the stock data.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while retrieving data.");
            e.printStackTrace();
        }
    }
}
