import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Node class, represents one wikipedia page traversal. Contains information on the home website and the target first link
public class Node {
    public String selfURL;
    public String selfTitle;
    public String firstURL;
    public String firstTitle;

    public Node(String url) {

        this.selfURL = url;
        //Calls for traversal of site to find first link
        this.searchLink();

    }

    public String getSelfURL() {return this.selfURL;}
    public String getSelfTitle() {return this.selfTitle;}
    public String getFirstURL() {return this.firstURL;}
    public String getFirstTitle() {return this.firstTitle;}
    public void scrape() throws IOException {
        String output = "";
        Document document = null;
        try {
            document = Jsoup.connect(this.selfURL).get();
        } catch (IOException e) {
            System.out.println("Invalid URL");
        }

        assert document != null;
        Elements paragraphs = document.select("p");
        for (Element para : paragraphs) {
            output = output + "\n" + para.text();
        }

        File doc = new File(this.getSelfTitle() + ".txt");

        FileWriter writer = new FileWriter(this.getSelfTitle() + ".txt");
        writer.write(output);
        writer.close();


    }
    // JSoup document crawling
    public void searchLink() {

        Document doc = null;

        try {
            doc = Jsoup.connect(this.selfURL).get();
        }

        catch (IOException e) {
            System.out.println("Not a valid link");
            e.printStackTrace();
        }

        assert doc != null;

        // Finds wikipedia article title
        try {
            String title = doc.selectFirst(".mw-page-title-main").text();
            this.selfTitle = title;
        }
        // For minor wikipedia articles
        catch (NullPointerException e) {

            String title = doc.selectFirst("#firstHeading").text();
            this.selfTitle = title;
        }

        Element body = doc.selectFirst("div.mw-body-content");
        assert body != null;
        Elements links = body.select("a");

        // Parse through all links on website
        for (Element link : links) {
            Element parentTag = link.parent();
            //Filter for desired a tag
            if (parentTag.tagName().equals("p") &&
                    !parentTag.parent().tagName().equals("td") &&
                    !link.attr("title").equals("Help:Pronunciation respelling key") &&
                    !link.attr("title").contains("language") &&
                    !link.attr("title").contains("English")

            ) {
                String text = link.text();
                String url2 = link.absUrl("href");

                this.firstURL = url2;
                this.firstTitle = text;
                return; //Break when first valid link is found
            }
        }
    }
}


