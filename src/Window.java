import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Window extends JPanel {
    private static int width = 1000;
    private static int height = 700;

    private Nod selectedNode = null;
    private Nod endNode = null;
    private Nod startNode = null;

    private Map<Nod, Double> distance;  // Distance from startNode to each node
    private Map<Nod, Nod> previous;     // Previous node in the shortest path

    private float minLat;
    private float maxLat;
    private float minLon;
    private float maxLon;
    private double scale;
    private ArrayList<Nod> nodes;
    private ArrayList<Arc> arcs;

    public Window() {
        ReadXML readXML = new ReadXML();
        this.nodes = readXML.getNodes();
        this.arcs = readXML.getArcs();
        minLat = readXML.getMinLat();
        maxLat = readXML.getMaxLat();
        minLon = readXML.getMinLon();
        maxLon = readXML.getMaxLon();
        for (Nod node : nodes) {
            node.setLon(LongToX(node.getLon()));
            node.setLat(LatToY(node.getLat()));
        }
        for (Arc arc : arcs) {
            arc.setFrom(nodes.get(arc.getFrom().getId()));
            arc.setTo(nodes.get(arc.getTo().getId()));
        }
    }

    public double lerpf(double a, double b, double t) {
        double interpolatedValue = (1-t)*a + t*b;
        return interpolatedValue;
    }
    public int LongToX(double lon) {
       return (int)lerpf(0, width, (lon - minLon) / (maxLon - minLon));
    }
    public int LatToY(double lat) {
        return (int)lerpf(0, height, (lat - minLat) / (maxLat - minLat));
    }

    void drawHarta(Graphics g) {
        for (Arc arc : arcs) {
            arc.DrawArc(g);
        }
        for (Nod node : nodes) {
            node.DrawNod(g);
        }
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                selectedNode = null;
                for (Nod node : nodes) {
                    if (Math.abs(node.getLon() - e.getX()) < 5 && Math.abs(node.getLat() - e.getY()) < 5) {
                        selectedNode = node;
                        break;
                    }
                }
                if (selectedNode != null) {
                    if (startNode == null) {
                        startNode = selectedNode;
                        System.out.println("Start node: " + startNode.getId());
                    } else if(endNode != selectedNode){
                        endNode = selectedNode;
                        System.out.println("End node: " + endNode.getId());
                        if (startNode != null && endNode != null && startNode != endNode)
                            dijkstra(startNode, endNode);
                    }
                }
            }
        });

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawHarta(g);
        repaintCount++;
        System.out.println("Repaint count: " + repaintCount);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    private int repaintCount = 0;

    public void dijkstra(Nod start, Nod end) {
        distance = new HashMap<>();
        previous = new HashMap<>();
        PriorityQueue<NodeDistancePair> priorityQueue = new PriorityQueue<>();

        // Initialization
        for (Nod node : nodes) {
            distance.put(node, Double.MAX_VALUE);
            previous.put(node, null);
        }

        distance.put(start, 0.0);
        priorityQueue.add(new NodeDistancePair(start, 0.0));

        previous.put(start, null); // Starting node has no previous node

        // Dijkstra's algorithm
        while (!priorityQueue.isEmpty()) {
            Nod current = priorityQueue.poll().getNode();

            for (Arc arc : arcs) {
                if (arc.getFrom().equals(current)) {
                    Nod neighbor = arc.getTo();
                    double newDistance = distance.get(current) + arc.getCost();

                    if (newDistance < distance.get(neighbor)) {
                        distance.put(neighbor, newDistance);
                        previous.put(neighbor, current); // Store the previous node
                        priorityQueue.add(new NodeDistancePair(neighbor, newDistance));
                    }
                }
            }
        }

        // Color the edges and nodes in the shortest path
        Nod current = end;
        while (current != null && previous.get(current) != null) {
            Arc arc = getArcByNodes(previous.get(current), current);
            if (arc != null) {
                arc.setColor(Color.GREEN); // Color the edge
                arc.setStroke(20.0f);
                arc.getFrom().setColor(Color.GREEN); // Color the node
                arc.getTo().setColor(Color.GREEN); // Color the node
            }

            Nod node = previous.get(current);
            if (node != null) {
                node.DrawNod(getGraphics()); // Color the node
            }

            current = previous.get(current);
        }


        /*//print the shortest path
        current = end;
        while (current != null && previous.get(current) != null) {
            System.out.print(current.getId() + " ");
            current = previous.get(current);
        }
        System.out.println(start.getId());*/

        repaint();
    }

    private Arc getArcByNodes(Nod from, Nod to) {
        for (Arc arc : arcs) {
            if (arc.getFrom().equals(from) && arc.getTo().equals(to)) {
                return arc;
            }
        }
        return null;
    }

    private static class NodeDistancePair implements Comparable<NodeDistancePair> {
        private final Nod node;
        private final double distance;

        public NodeDistancePair(Nod node, double distance) {
            this.node = node;
            this.distance = distance;
        }

        public Nod getNode() {
            return node;
        }

        public double getDistance() {
            return distance;
        }

        @Override
        public int compareTo(NodeDistancePair other) {
            return Double.compare(this.distance, other.distance);
        }
    }


    public static void main(String[] args) {
        Window window = new Window();
        JFrame frame = new JFrame("Harta");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(window);
        frame.pack();
        frame.setVisible(true);
    }
}
