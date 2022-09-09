package com.kevinkoehler.tcxeditor;

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

  private void updateNodes(Document document, String tagName) {
    NodeList nodes = document.getElementsByTagName(tagName);
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      double value = Double.parseDouble(node.getTextContent());
      value = value * multiplier;
      node.setTextContent(String.valueOf(value));
    }
  }

  /**
   * Parse the TCX file specified in the UI, modify the data based on the
   * multiplier, and create a new file in the same directory with "_modified"
   * appended to the file name.
   *
   * @return {@code true} if the process was successful, otherwsie {@code
   * false}.
   */
  public boolean parseFile() {
    try {
      File tcxFile = new File(filePath);
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = documentBuilder.parse(tcxFile);
      document.getDocumentElement().normalize();
      updateNodes(document, MAXIMUM_SPEED);
      updateNodes(document, DISTANCE_METERS);
      updateNodes(document, SPEED);
      updateNodes(document, AVERAGE_SPEED);

      String newFilePath = filePath
          .substring(0, filePath.indexOf(TcxEditorForm.EXTENSION));
      newFilePath = newFilePath
          .concat(NEW_FILE_SUFFIX + TcxEditorForm.EXTENSION);
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
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
