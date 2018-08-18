package app.kevnet.TcxEditor;

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

  private static final String MAXIMUM_SPEED = "MaximumSpeed";
  private static final String DISTANCE_METERS = "DistanceMeters";
  private static final String NAMESPACE = "ns3:";
  private static final String SPEED = NAMESPACE + "Speed";
  private static final String AVERAGE_SPEED = NAMESPACE + "AvgSpeed";
  private static final String NEW_FILE_SUFFIX = "_modified";
  private final String filePath;
  private final double multiplier;

  public TcxParser(final String filePath, final double multiplier) {
    this.filePath = filePath;
    this.multiplier = multiplier;
  }

  /**
   * Parse the TCX file specified in the UI, modify the data based on the multiplier, and create a
   * new file in the same directory with "_modified" appended to the file name.
   *
   * @return {@code true} if the process was successful, otherwsie {@code false}.
   */
  public boolean parseFile() {
    try {
      File tcxFile = new File(filePath);
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      Document document = documentBuilder.parse(tcxFile);
      document.getDocumentElement().normalize();

      NodeList maximumSpeedNodes = document.getElementsByTagName(MAXIMUM_SPEED);
      for (int i = 0; i < maximumSpeedNodes.getLength(); i++) {
        Node maximumSpeed = maximumSpeedNodes.item(i);
        double value = Double.valueOf(maximumSpeed.getTextContent());
        value = value * multiplier;
        maximumSpeed.setTextContent(String.valueOf(value));
      }

      NodeList distanceMetersNodes = document.getElementsByTagName(DISTANCE_METERS);
      for (int i = 0; i < distanceMetersNodes.getLength(); i++) {
        Node distanceMeters = distanceMetersNodes.item(i);
        double value = Double.valueOf(distanceMeters.getTextContent());
        value = value * multiplier;
        distanceMeters.setTextContent(String.valueOf(value));
      }

      NodeList speedNodes = document.getElementsByTagName(SPEED);
      for (int i = 0; i < speedNodes.getLength(); i++) {
        Node speed = speedNodes.item(i);
        double value = Double.valueOf(speed.getTextContent());
        value = value * multiplier;
        speed.setTextContent(String.valueOf(value));
      }

      NodeList averageSpeedNodes = document.getElementsByTagName(AVERAGE_SPEED);
      for (int i = 0; i < averageSpeedNodes.getLength(); i++) {
        Node averageSpeed = averageSpeedNodes.item(i);
        double value = Double.valueOf(averageSpeed.getTextContent());
        value = value * multiplier;
        averageSpeed.setTextContent(String.valueOf(value));
      }

      String newFilePath = filePath.substring(0, filePath.indexOf(TcxEditorForm.EXTENSION));
      newFilePath = newFilePath.concat(NEW_FILE_SUFFIX + TcxEditorForm.EXTENSION);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(new File(newFilePath));
      transformer.transform(source, result);
      return true;
    } catch (IOException | SAXException | TransformerException | ParserConfigurationException e) {
      e.printStackTrace();
    }
    return false;
  }
}
