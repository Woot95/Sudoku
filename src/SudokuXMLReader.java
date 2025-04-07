import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.*;
import java.io.File;
import java.io.StringReader;

public class SudokuXMLReader {

    public static SudokuData readFromXml(String filePath) throws Exception {
        File xmlFile = new File(filePath);
        DocumentBuilder dBuilder = createDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        return parseDocumentToSudokuData(doc);
    }

    public static SudokuData readFromXmlString(String xmlContent) throws Exception {
        DocumentBuilder dBuilder = createDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xmlContent));
        Document doc = dBuilder.parse(is);
        return parseDocumentToSudokuData(doc);
    }

    private static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        return dbFactory.newDocumentBuilder();
    }

    private static SudokuData parseDocumentToSudokuData(Document doc) {
        doc.getDocumentElement().normalize();
        SudokuData data = new SudokuData();
        NodeList feldList = doc.getElementsByTagName("feld");

        for (int i = 0; i < feldList.getLength(); i++) {
            Node node = feldList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element feld = (Element) node;

                int value = Integer.parseInt(feld.getAttribute("zahl"));
                int row = Integer.parseInt(feld.getAttribute("positionY"));
                int col = Integer.parseInt(feld.getAttribute("positionX"));

                data.cells.add(new SudokuData.CellEntry(row, col, value));
            }
        }

        return data;
    }
}
