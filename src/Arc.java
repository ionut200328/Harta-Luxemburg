import java.awt.*;
import java.util.ArrayList;

public class Arc {
    private Nod from, to;
    private double cost;
    private Color color = Color.BLACK;
    private float stroke = 1.0f;

    public void setCost(double cost) {
        this.cost = cost;
    }
    public void setTo(Nod to) {
        this.to = to;
    }
    public void setFrom(Nod from) {
        this.from = from;
    }

    public double getCost() {
        return cost;
    }
    public Nod getTo() {
        return to;
    }
    public Nod getFrom() {
        return from;
    }

    public Arc() {
    }

    public Arc(Nod from, Nod to, double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public void DrawArc(Graphics g) {
        g.setColor(color);
        //set stroke width
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(new BasicStroke(stroke));
        g.drawLine(from.getLon(), from.getLat(), to.getLon(), to.getLat());
    }

    public void setColor(Color blue) {
        this.color = blue;
    }

    public void setStroke(float stroke) {
        this.stroke = stroke;
    }
}
