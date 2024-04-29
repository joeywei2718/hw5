import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.view.Viewer;

import java.util.ArrayList;
import java.util.HashMap;

import static org.graphstream.ui.layout.Layouts.newLayoutAlgorithm;

public class Visualizer {
    public Visualizer(HashMap<String, ArrayList<String>> crawlGraph) {

        System.setProperty("org.graphstream.ui", "swing");

        SingleGraph graph = new SingleGraph("Graph Visualizer");

        graph.setStrict(false);
        graph.setAutoCreate(true);

        for (HashMap.Entry<String, ArrayList<String>> entry : crawlGraph.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> connectedNodes = entry.getValue();

            // Add node
            Node node = graph.addNode(key);
            node.setAttribute("ui.label", key);
            node.setAttribute("ui.style", "text-size: 14;"); // Larger text size
            node.setAttribute("ui.label.position", "20 below"); // Position label 10 units below the node

            // Add edges
            for (String connectedNode : connectedNodes) {
                graph.addEdge(key + connectedNode, key, connectedNode);
            }
        }

        Viewer view = graph.display();

        Layout layout = newLayoutAlgorithm();
        layout.setForce(2);
        view.enableAutoLayout();
    }
}
