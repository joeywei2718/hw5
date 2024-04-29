import java.io.IOException;
import java.util.*;
public class Main {
    public static void main(String[] args) throws IOException {

        Node newNode = new Node("https://en.wikipedia.org/wiki/Computer_keyboard");
        newNode.scrape();

        HashMap<String, ArrayList<String>> graphData = new HashMap<>();
        HashMap<String, ArrayList<String>> fullTraversal = new HashMap<>();

        crawl(graphData, fullTraversal, "https://en.wikipedia.org/wiki/Lemar_Aftaab");
        crawl(graphData, fullTraversal,"https://en.wikipedia.org/wiki/Computer_keyboard");
        crawl(graphData, fullTraversal,"https://en.wikipedia.org/wiki/Chocolate");
        crawl(graphData, fullTraversal,"https://en.wikipedia.org/wiki/Pittsburgh_Penguins");

        System.out.println(graphData);
        System.out.println(fullTraversal);
        Visualizer visual = new Visualizer(graphData);


    }


    public static void crawl(HashMap<String, ArrayList<String>> graphData, HashMap<String, ArrayList<String>> traverse, String url) {
        Node root = new Node(url);
        String rootName = root.getSelfTitle();
        String parent = rootName;

        for (int i = 0; i < 25; i++) {
            Node current = new Node(url);
            String currentTitle = current.getSelfTitle();

            if (!graphData.containsKey(parent))
                graphData.put(parent, new ArrayList<>());

            graphData.get(parent).add(currentTitle);
            traverse.computeIfAbsent(rootName, k -> new ArrayList<>()).add(currentTitle);

            parent = currentTitle;
            url = current.getFirstURL();

            if (currentTitle.equalsIgnoreCase("philosophy")) {
                graphData.put("Philosophy", new ArrayList<>());
                break;
            }
        }
    }
}