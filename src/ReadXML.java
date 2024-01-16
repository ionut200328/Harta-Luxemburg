import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;

public class ReadXML {
    private ArrayList<Nod> nodes;
    private ArrayList<Arc> arcs;

    float minLat;
    float maxLat;
    float minLon;
    float maxLon;

    public float getMinLat() {
        return minLat;
    }
    public float getMaxLat() {
        return maxLat;
    }
    public float getMinLon() {
        return minLon;
    }
    public float getMaxLon() {
        return maxLon;
    }


    public ArrayList<Nod> getNodes() {
        return nodes;
    }
    public ArrayList<Arc> getArcs() {
        return arcs;
    }


    public ReadXML() {
        try{
            File read = new File("src/Harta_Luxemburg.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler();

            saxParser.parse(read, handler);

            maxLat = handler.getMaxLat();
            minLat = handler.getMinLat();
            maxLon = handler.getMaxLon();
            minLon = handler.getMinLon();

            this.nodes = handler.getNodes();
            this.arcs = handler.getArcs();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ReadXML readXML = new ReadXML();
        System.out.println(readXML.nodes.size());
        System.out.println(readXML.arcs.size());
        for(int i=0;i<20;++i)
        {
            System.out.println(readXML.nodes.get(i).getId()+" "+readXML.nodes.get(i).getLat()+" "+readXML.nodes.get(i).getLon());
        }
    }

}

class XMLHandler extends org.xml.sax.helpers.DefaultHandler {
    private ArrayList<Nod> nodes = new ArrayList<>();
    private ArrayList<Arc> arcs = new ArrayList<>();

    private float minLon = Float.MAX_VALUE;
    private float maxLon = Float.MIN_VALUE;
    private float minLat = Float.MAX_VALUE;
    private float maxLat = Float.MIN_VALUE;

    public float getMinLon() {
        return minLon;
    }
    public float getMaxLon() {
        return maxLon;
    }
    public float getMinLat() {
        return minLat;
    }
    public float getMaxLat() {
        return maxLat;
    }

    public ArrayList<Nod> getNodes() {
        return nodes;
    }

    public ArrayList<Arc> getArcs() {
        return arcs;
    }

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
            throws SAXException {

        if(qName.equalsIgnoreCase("node")) {
            Nod node = new Nod();
            int id = Integer.parseInt(attributes.getValue("id"));
            int lat = Integer.parseInt(attributes.getValue("latitude"));
            int lon = Integer.parseInt(attributes.getValue("longitude"));
            node.setLat(lat);
            node.setLon(lon);
            node.setId(id);
            nodes.add(node);

            if(lat < minLat) {
                minLat = lat;
            }
            if(lat > maxLat) {
                maxLat = lat;
            }
            if(lon < minLon) {
                minLon = lon;
            }
            if(lon > maxLon) {
                maxLon = lon;
            }
        }
        else if(qName.equalsIgnoreCase("arc")) {
            Arc arc = new Arc();
            arc.setFrom(nodes.get(Integer.parseInt(attributes.getValue("from"))));
            arc.setTo(nodes.get(Integer.parseInt(attributes.getValue("to"))));
            arc.setCost(Double.parseDouble(attributes.getValue("length")));

            arcs.add(arc);
        }
    }
}

