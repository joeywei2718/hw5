import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {

        Node newNode = new Node("https://en.wikipedia.org/wiki/Computer_keyboard");
        newNode.scrape();

        HashMap<String, ArrayList<String>> graphData = new HashMap<>();
        HashMap<String, ArrayList<String>> fullTraversal = new HashMap<>();
        HashMap<String, ArrayList<String>> clusters = new HashMap<>();


        crawl(graphData, fullTraversal, "https://en.wikipedia.org/wiki/Lemar_Aftaab");
        crawl(graphData, fullTraversal,"https://en.wikipedia.org/wiki/Computer_keyboard");
        crawl(graphData, fullTraversal,"https://en.wikipedia.org/wiki/Chocolate");
        crawl(graphData, fullTraversal,"https://en.wikipedia.org/wiki/Pittsburgh_Penguins");
        crawl(graphData, fullTraversal,"https://en.wikipedia.org/wiki/Boeing_747");

        //System.out.println(graphData);
        System.out.println(cluster(fullTraversal));
        //System.out.println(fullTraversal);
        //System.out.println(distance(fullTraversal));

        ArrayList<ArrayList<String>> distances = distance(fullTraversal);

        for(ArrayList<String> link : distances) {

            System.out.println("Node Distances of " +link.getFirst() + " to:");

            System.out.println(link.subList(1, link.size()));

        }
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

    public static ArrayList<ArrayList<String>> distance(HashMap<String, ArrayList<String>> dist) {

        ArrayList<ArrayList<String>> distances = new ArrayList<>();

        for(Map.Entry<String, ArrayList<String>> selectentry: dist.entrySet()) {

            ArrayList<String> em = new ArrayList<>();

            for (Map.Entry<String, ArrayList<String>> otherentry : dist.entrySet()) {

                ArrayList<String> l1 = selectentry.getValue();

                ArrayList<String> l2 = otherentry.getValue();

                ArrayList<String> l3 = new ArrayList<>(l1);
                l3.addAll(l2);

                Set<String> unique = new HashSet<>(l3);

                for (String x : new HashSet<>(unique)) {
                    if (l1.contains(x) && l2.contains(x)) {
                        unique.remove(x);
                    }
                }
                String differenceInt = String.valueOf(unique.size());
                em.add(otherentry.getKey() + ": "+ differenceInt);
            }
            em.addFirst(selectentry.getKey());
            distances.add(em);
        }
        return distances;
    }

    public static HashMap<String, ArrayList<String>> cluster(HashMap<String, ArrayList<String>> clusters) {
        HashMap<String, ArrayList<String>> clusterMap = new HashMap<>();
        ArrayList<String> clusterList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> selectEntry : clusters.entrySet()) {

            ArrayList<String> l1 = selectEntry.getValue();

            for (Map.Entry<String, ArrayList<String>> otherEntry : clusters.entrySet()) {
                ArrayList<String> temp = new ArrayList<>();
                ArrayList<String> l2 = otherEntry.getValue();

                if (selectEntry.getValue().equals(otherEntry.getValue())) {
                    break;
                }
                // Iterate over each element in l1
                for (String element : l1) {
                    // If the element is present in l2, save it and break the loop
                    if (l2.contains(element)) {
                        temp.add(element);
                        break;
                    }
                }

                if (!temp.isEmpty()) {
                    String intersect = temp.getFirst(); // Get the first element from temp
                    if (!clusterList.contains(intersect)) {
                        clusterList.add(intersect);
                    }

                }
            }
        }

        for (String clusterName:clusterList) {
            ArrayList<String> contained = new ArrayList<>();
            for (Map.Entry<String, ArrayList<String>> select : clusters.entrySet()) {
                ArrayList<String> path = select.getValue();
                if (path.contains(clusterName)) {
                    contained.add(select.getKey());
                }
            }
            clusterMap.put(clusterName,contained);

        }

        return clusterMap;
    }

}