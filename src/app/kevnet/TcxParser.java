package app.kevnet;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TcxParser {

  private final String filePath;
  private final double speedDistanceMultiplier;

  public TcxParser(String filePath, double speedDistanceMultiplier) {
    this.filePath = filePath;
    this.speedDistanceMultiplier = speedDistanceMultiplier;
  }

  public void updateFile() {
    try {
      File tcxFile = new File(filePath);
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(tcxFile);
      document.getDocumentElement().normalize();

      NodeList maximumSpeedNodes = document.getElementsByTagName("MaximumSpeed");
      for (int i = 0; i < maximumSpeedNodes.getLength(); i++) {
        Node maximumSpeed = maximumSpeedNodes.item(i);
        double value = Double.valueOf(maximumSpeed.getTextContent());
        value = value * speedDistanceMultiplier;
        maximumSpeed.setTextContent(String.valueOf(value));
      }

      NodeList distanceMetersNodes = document.getElementsByTagName("DistanceMeters");
      for (int i = 0; i < distanceMetersNodes.getLength(); i++) {
        Node distanceMeters = distanceMetersNodes.item(i);
        double value = Double.valueOf(distanceMeters.getTextContent());
        value = value * speedDistanceMultiplier;
        distanceMeters.setTextContent(String.valueOf(value));
      }

      NodeList speedNodes = document.getElementsByTagName("ns3:Speed");
      for (int i = 0; i < speedNodes.getLength(); i++) {
        Node speed = speedNodes.item(i);
        double value = Double.valueOf(speed.getTextContent());
        value = value * speedDistanceMultiplier;
        speed.setTextContent(String.valueOf(value));
      }

      NodeList averageSpeedNodes = document.getElementsByTagName("ns3:AvgSpeed");
      for (int i = 0; i < averageSpeedNodes.getLength(); i++) {
        Node averageSpeed = averageSpeedNodes.item(i);
        double value = Double.valueOf(averageSpeed.getTextContent());
        value = value * speedDistanceMultiplier;
        averageSpeed.setTextContent(String.valueOf(value));
      }

      String newFilePath = filePath.substring(0, filePath.indexOf(".tcx"));
      newFilePath = newFilePath.concat("_new.tcx");

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(new File(newFilePath));
      transformer.transform(source, result);
    } catch (IOException | SAXException | TransformerException | ParserConfigurationException e) {
      e.printStackTrace();
    }

  }
}
