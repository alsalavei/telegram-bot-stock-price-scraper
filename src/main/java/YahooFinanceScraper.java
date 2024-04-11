import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class YahooFinanceScraper {

    public static void main(String[] args) {
        String url = "https://finance.yahoo.com/quote/AAPL";
        try {
            Document doc = Jsoup.connect(url).get();

            // Используйте соответствующий CSS селектор для извлечения цены.
            // Селектор может изменяться, поэтому его может потребоваться обновить.
            Elements priceElements = doc.select("fin-streamer.Fw\\(b\\).Fz\\(36px\\).Mb\\(-4px\\).D\\(ib\\)");

            if (!priceElements.isEmpty()) {
                String price = priceElements.first().text();
                System.out.println("The current price of AAPL is: " + price);
            } else {
                System.out.println("Unable to find the price element.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
