import java.io.IOException;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException {




        HashMap<String, ArrayList<String>> graphData = new HashMap<>();
        HashMap<String, ArrayList<String>> fullTraversal = new HashMap<>();

        Scanner input = new Scanner(System.in);

        System.out.println("Wikipedia Traversal + Graph Program - NETS150 HW5");
        System.out.println("Type - 1 - to run demo, type - 2 - to input custom parameters");
        String select = input.next();
        input.nextLine();

        if (select.equals("1")) {
            crawl(graphData, fullTraversal, "https://en.wikipedia.org/wiki/Morse_Code");
            crawl(graphData, fullTraversal, "https://en.wikipedia.org/wiki/Green");
            crawl(graphData, fullTraversal, "https://en.wikipedia.org/wiki/Pablo_Picasso");
            crawl(graphData, fullTraversal, "https://en.wikipedia.org/wiki/Beer");
            crawl(graphData, fullTraversal, "https://en.wikipedia.org/wiki/Calculus");
            crawl(graphData, fullTraversal, "https://en.wikipedia.org/wiki/Python_(programming_language)");



            System.out.println("Do all the links converge to Philosophy?");
            System.out.println(converge(fullTraversal) + "\n");

            System.out.println("Which links are convergence points for multiple links?");
            System.out.println(cluster(fullTraversal)+ "\n");


            ArrayList<ArrayList<String>> distances = distance(fullTraversal);
            System.out.println("What are the node distances between the links?");
            for (ArrayList<String> link : distances) {

                System.out.println("Node Distances of " + link.getFirst() + " to:");

                System.out.println(link.subList(1, link.size()));

            }
            Visualizer visual = new Visualizer(graphData);
        }

        else if(select.equals("2")) {
            ArrayList<String> links = new ArrayList<>();
            System.out.println("How many Wikipedia links would you like to add to the graph/traversal? (1-10)");
            int count = input.nextInt();
            input.nextLine();
            for(int i = 0; i < count; i++) {
                System.out.println("Paste Wikipedia link #" + (i+1));
                String link = input.nextLine();
                links.add(link);
            }

            for (String link:links) {

                crawl(graphData,fullTraversal,link);

            }
            Visualizer visual = new Visualizer(graphData);
            System.out.println("Drag nodes to move the graph around. Philosophy is highlighted in red.");
            boolean running = true;

            while (running) {

                System.out.println("Analytics:");
                System.out.println("1 - Node Distances between links");
                System.out.println("2 - Node Clustering Points");
                System.out.println("3 - Convergence to Philosophy");
                System.out.println("4 - Close Program");
                int in = input.nextInt();

                switch(in) {
                    case 1:

                        System.out.println("Lists the node distance between each link and every other link, using the \n" );
                        System.out.println("generated graph structure from the traversal");
                        ArrayList<ArrayList<String>> distances = distance(fullTraversal);

                        for (ArrayList<String> link : distances) {

                            System.out.println("Node Distances of " + link.getFirst() + " to:");

                            System.out.println(link.subList(1, link.size()));

                        }

                        break;

                    case 2:
                        System.out.println("Lists nodes that serve as clustering hubs, and lists which links converge to these hubs.");
                        System.out.println("Serves as the convergence point of the traversals.");
                        System.out.println(cluster(fullTraversal));
                        break;


                    case 3:

                        System.out.println("Do all of the links converge to Philosophy?");
                        System.out.println(converge(fullTraversal));
                        break;

                    case 4:
                        running = false;
                        System.out.println("Closing...");
                        break;

                }

            }

        }
        input.close();
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

    public static boolean converge(HashMap<String, ArrayList<String>> x) {
        for (Map.Entry<String, ArrayList<String>> select: x.entrySet()) {

            ArrayList<String> path = x.get(select.getKey());
            String finalLink = path.getLast();

            if (!finalLink.equalsIgnoreCase("philosophy")){

                return false;
            }
        }
        return true;
    }

    /*
    Works from the fullTraversal HashMap data structure. Input is structured as follows:

    [ [StartingLink1, [FirstLink, SecondLink, ThirdLink,...,Philosophy]],
      [StartingLink2, [FirstLink, SecondLink, ThirdLink,...,Philosophy]],
      ...
    ]

    Selects loops through key + value pairs,

    For each KV pair, loop through the other KV pairs.

    For each combination of KV pairs, perform the following algorithm:

    Select value Arrays of (Array of the links) for both KV's, and SUM

    Loop through sum. For values that appear in both entries, remove them from the SUM array.

    Take length of the array.

    If the paths converge to Philosophy (the node graphs for the two links are connected), add this length to the output array

    If Philosophy is not present (disconnected node graphs), report distance as N/A

     */
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
                if (l1.contains("Philosophy")) {
                    em.add(otherentry.getKey() + ": " + differenceInt);
                }
                else {
                    em.add(otherentry.getKey() + ": NA");

                }
            }
            em.addFirst(selectentry.getKey());
            distances.add(em);
        }
        return distances;
    }
    /*

    Works from the fullTraversal HashMap data structure. Input is structured as follows:

    [ [StartingLink1, [FirstLink, SecondLink, ThirdLink,...,Philosophy]],
      [StartingLink2, [FirstLink, SecondLink, ThirdLink,...,Philosophy]],
      ...
    ]

    For each KV pair (Entry in the hashmap), loop through the other KV pairs.

    For each combination of KV pairs, perform the following algorithm:

    Check if it is comparing the same KV pair. If yes, break.

    Loop through the Value Arrays. If an element is present in both Value Arrays, add to the temp Array.

    If Temp is not empty, get the first shared element. This is the first intersection, and the convergence of the two traversals.

    If not already in the intersect list, add element to intersect.

    Creates array intersect, which is an array of all the nodes that are convergence points for all of the starting links.

    Then, find the respective starting links that pass through each convergence point.

    Loop through the list of convergence nodes.

    For each possible convergence node, loop through the fullTraversal HashMap.

    If the convergence node appears in the Value array of a given link key, add this key to the clusterMap value.

    Possible final output is of form:


    [[Science, [StartingLink1, StartingLink2]],
     [Physics, [StartingLink1,StartingLink3]],
     [Geography, [StartingLink2,StartingLink3]
    ]

    Where Science, Phyiscs, and Geography are convergence points for the starting links.
     */
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