import java.io.IOException;
import java.util.*;
public class Main {
    public static void main(String[] args) throws IOException {

        Node newNode = new Node("https://en.wikipedia.org/wiki/Computer_keyboard");
        newNode.scrape();


        HashMap<String, ArrayList<String>> graphData = new HashMap<>();
        String url = "https://en.wikipedia.org/wiki/Lemar_Aftaab";
        crawl(graphData, url);
        crawl(graphData, "https://en.wikipedia.org/wiki/Computer_keyboard");
        crawl(graphData, "https://en.wikipedia.org/wiki/Chocolate");
        crawl(graphData, "https://en.wikipedia.org/wiki/Pittsburgh_Penguins");

        System.out.println(graphData);
        Visualizer visual = new Visualizer(graphData);


    }

    public static void crawl(HashMap<String, ArrayList<String>> graphData, String url) {
        String parent = null;
        for (int i = 0; i < 25; i++) {
            if (parent == null) {
                Node current = new Node(url);
                url = current.getFirstURL();
                parent = current.getSelfTitle();
                if(current.getSelfTitle().equalsIgnoreCase("philosophy")) {
                    graphData.put("Philosophy", new ArrayList<>());
                    break;
                }
            } else {
                Node current = new Node(url);
                if (!graphData.containsKey(parent)){
                    graphData.put(parent, new ArrayList<>());
                }
                graphData.get(parent).add(current.getSelfTitle());
                parent = current.getSelfTitle();
                url = current.getFirstURL();
                if(current.getSelfTitle().equalsIgnoreCase("philosophy")) {
                    graphData.put("Philosophy", new ArrayList<>());
                    break;
                }
            }
        }
    }
}