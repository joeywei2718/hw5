import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class WikiScraper {

    public String scrape(String url) {
        String output = "";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Invalid URL");
        }

        Elements paragraphs = document.select("p");
        for (Element para : paragraphs) {
            output = output + "\n" + para.text();
        }
        return output;

    }

}
