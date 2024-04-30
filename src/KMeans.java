import java.util.*;

public class KMeans {
    // Goals:
    private final HashMap<Node, Integer> nodeToInt;
    private final HashMap<Integer, Node> intToNode;
    private final LinkedList<Integer> nodeNumbers;
    private final HashMap<Node, LinkedList<Node>> adjacency;
    private int maxNode;

    // Thought: Most likely the best way is for us to create a graph with a single node
    // and then to use this class to do all of the scraping

    /**
     * Constructor for a Wikipedia graph with a specified adjacency list.
     * @param Network Hashmap representing
     */
    public KMeans(HashMap<Node, Integer> Network, HashMap<Node, LinkedList<Node>> adjacency) {
        // Instantiate the dictionaries between node numbers and the actual node object
        this.nodeToInt = Network;
        this.intToNode = new HashMap<>();
        this.nodeNumbers = new LinkedList<>();
        for (Node n: Network.keySet()) {
            this.nodeNumbers.add(Network.get(n));
            intToNode.put(Network.get(n), n);
        }

        // Determine which node we are starting on
        int maxNode = 0;
        for (int i: nodeNumbers) {
            if (i >= maxNode) {
                maxNode = i;
            }
        }

        // Instantiate adjacency list
        this.adjacency = adjacency;
    }

    /**
     * Constructor for a Wikipedia graph without an adjacency list already available.
     * @param Network Hashmap representing
     */
    public KMeans(HashMap<Node, Integer> Network) {
        // Instantiate the dictionaries between node numbers and the actual node object
        this.nodeToInt = Network;
        this.intToNode = new HashMap<>();
        this.nodeNumbers = new LinkedList<>();
        for (Node n: Network.keySet()) {
            this.nodeNumbers.add(Network.get(n));
            intToNode.put(Network.get(n), n);
        }

        // Determine which node we are starting on
        int maxNode = 0;
        for (int i: nodeNumbers) {
            if (i >= maxNode) {
                maxNode = i;
            }
        }

        // Instantiate adjacency list
        this.adjacency = new HashMap<>();

        // Add a new linked list that hasn't been instantiated for every node to flesh this out
        for (Node n: Network.keySet()) {
            adjacency.put(n, new LinkedList<>());
        }
    }

    public HashMap<Node, Integer> getNodeToInt() {
        return this.nodeToInt;
    }

    public HashMap<Integer, Node> getIntToNode() {
        return this.intToNode;
    }

    /**
     * Add a new node based on existing edges
     * @param n Node to add.
     * @param fromNodes Nodes accessible from Node n.
     * @param toNodes Nodes pointing to Node n.
     */
    public void addNode(Node n, LinkedList<Node> fromNodes, LinkedList<Node> toNodes) {
        // Check and add to adjacency first
        if (!adjacency.keySet().contains(n)) {
            adjacency.put(n, new LinkedList<>());
        } else {
            System.out.println("Warning: Could not add new node; this node already exists.");
        }

        // Add node to hashmaps
        nodeToInt.put(n, maxNode);
        intToNode.put(maxNode, n);
        nodeNumbers.add(maxNode);
        maxNode++;

        // Update adjacency
        for (Node currentNode: toNodes) {
            adjacency.get(currentNode).add(n);
        }

        for (Node currentNode: fromNodes) {
            adjacency.get(n).add(currentNode);
        }
    }

    /**
     * Obtain the next link and form a new node in the graph starting at the specified node.
     * @param n Node to start crawling from.
     */
    public void crawl(Node n) {
        // Start crawling
        n.searchLink();
        String urlToCrawl = n.getFirstURL();
        Node nextNodeToAdd = new Node(urlToCrawl);

        // Add to existing maps
        nodeToInt.put(nextNodeToAdd, maxNode + 1);
        intToNode.put(maxNode + 1, nextNodeToAdd);

        // Update adjacency
        adjacency.get(n).add(nextNodeToAdd);

        // Iterate max node
        maxNode += 1;
    }

    ArrayList<Document> docs = new ArrayList<>();
    public void getDocument(String file) {

        try {
            Document current = new Document(file);
            docs.add(current);

        }
        catch (NullPointerException e){
            System.out.println("File Not Found");
        }
    }


    /**
     * Compute and return embeddings for each of the nodes/documents.
     */
    public HashMap<Document, HashMap<String, Double>> computeEmbeddings() {
        // Obtain list of documents to feed to corpus

        // Instantiate model
        Corpus corpus = new Corpus(docs);
        VectorSpaceModel vectorSpace = new VectorSpaceModel(corpus);
        vectorSpace.createTfIdfWeights();
        return vectorSpace.getTfIdfWeights();
    }

    /**
     * Perform K-Means Clustering for the graph.
     */
    public HashMap<HashMap<String, Double>, LinkedList<Document>> kMeans(boolean printCommunities, int numOfCommunities, int numIterations) {
        // Step 0: Get the weights
        HashMap<Document, HashMap<String, Double>> weights = this.computeEmbeddings();

        // Step 1: Instantiate centroids
        LinkedList<HashMap<String, Double>> listOfCentroids = new LinkedList<>();
        for (int i = 0; i < numOfCommunities; i++) {
            // Generate random numbers between 0 and 1
            HashMap<String, Double> currentCentroid = new HashMap<>();

            // Get the strings that correspond to each vector
            Document firstDocument = weights.keySet().iterator().next();
            Set<String> allStrings = weights.get(firstDocument).keySet();

            // Loop through each string, add it to the centroid with a random value
            for (String s: allStrings) {
                currentCentroid.put(s, Math.random());
            }

            // Add the centroid to the list
            listOfCentroids.add(currentCentroid);
        }

        HashMap<HashMap<String, Double>, LinkedList<Document>> assignments = new HashMap<>();

        for (int i = 0; i < numIterations; i++) {
            LinkedList<HashMap<String, Double>> newCentroids = new LinkedList<>();
            // Step 2: Assignment to clusters

            // Instantiate assignment map
            assignments = new HashMap<>();
            for (HashMap<String, Double> centroid: listOfCentroids) {
                assignments.put(centroid, new LinkedList<>());
            }

            // Loop through each node and see which centroid it will be assignment to
            for (Document d: weights.keySet()) {
                double minDistance = Integer.MAX_VALUE;
                HashMap<String, Double> closestCluster = new HashMap<>();

                for (HashMap<String, Double> centroid: listOfCentroids) {
                    // Compute a difference vector
                    HashMap<String, Double> difference = new HashMap<>();
                    for (String s: centroid.keySet()) {
                        difference.put(s, centroid.get(s) - weights.get(d).get(s));
                    }

                    double currentDistance = VectorSpaceModel.getMagnitude(difference);
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        closestCluster = centroid;
                    }
                }

                // Assign the node to the lowest distance
                assignments.get(closestCluster).add(d);
            }

            // Step 3: Update centroids
            for (HashMap<String, Double> centroid: assignments.keySet()) {
                LinkedList<Document> documentsInCluster = assignments.get(centroid);
                double numOfDocuments = documentsInCluster.size();
                HashMap<String, Double> average = new HashMap<>();

                // Initialize vector
                for (String s: centroid.keySet()) {
                    average.put(s, 0.0);
                }

                // Sum up the weights of the documents
                for (Document d: documentsInCluster) {
                    HashMap<String, Double> currentWeights = weights.get(d);
                    for (String s: currentWeights.keySet()) {
                        average.put(s, average.get(s) + currentWeights.get(s));
                    }
                }

                // Divide each one by the number of documents
                for (String s: average.keySet()) {
                    average.put(s, average.get(s) / numOfDocuments);
                }

                // Save the centroids
                newCentroids.add(average);
            }
            listOfCentroids = newCentroids;
        }
        return assignments;
    }
}