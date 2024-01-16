import java.awt.*;
import java.util.ArrayList;

public class Nod {
    private int id;
    private int lat;
    private int lon;
    private Color color = Color.RED;

    public void setLon(int lon) {
        this.lon = lon;
    }
    public void setLat(int lat) {
        this.lat = lat;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getLon() {
        return lon;
    }
    public int getLat() {
        return lat;
    }
    public int getId() {
        return id;
    }

    public void setPos(int lon, int lat) {
        this.lat = lat;
        this.lon = lon;
    }

    public Nod() {
    }

    public Nod(int id, int lat, int lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public double TransfLat(int lat, int minLat, int maxLat, double scale) {
        return (lat - minLat) * scale / (maxLat - minLat);
    }
    public double TransfLon(int lon, int minLon, int maxLon, double scale) {
        return (lon - minLon) * scale / (maxLon - minLon);
    }

    public void DrawNod(Graphics g) {
        g.setColor(color);
        g.fillOval(lon, lat, 1, 1);
        g.drawOval(lon, lat, 1, 1);
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public Nod getClosesNode(ArrayList<Nod> nodes, int x, int y) {
        Nod closestNode = null;
        double minDistance = Double.MAX_VALUE;
        for (Nod node : nodes) {
            double distance = Math.sqrt(Math.pow(node.getLon() - x, 2) + Math.pow(node.getLat() - y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = node;
            }
        }
        return closestNode;
    }
}
