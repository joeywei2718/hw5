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
            if (key.equalsIgnoreCase("Philosophy")) {

                node.setAttribute("ui.style", "fill-color: red; text-size: 14; text-style: bold;");
                node.setAttribute("xyz", 1, 3, 0);


            }

            else {
                node.setAttribute("ui.style", "fill-color: green; text-size: 12;");

                node.setAttribute("ui.label.position", "10, -40"); // Position label 10 units below the node
            }
            // Add edges
            for (String connectedNode : connectedNodes) {
                if (!key.equals(connectedNode)) {
                    graph.setAttribute("ui.stylesheet", "edge {arrow-shape: arrow; }");
                    graph.addEdge(key + connectedNode, key, connectedNode, true);
                }
            }
        }

        Viewer view = graph.display();

        Layout layout = newLayoutAlgorithm();
        layout.setForce(1);

        view.enableAutoLayout();
    }
}
