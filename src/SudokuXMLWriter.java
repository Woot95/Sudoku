import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SudokuXMLWriter {

    public static void writeToXml(SudokuData data, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writeToXml(data, writer);
        }
    }

    public static void writeToXml(SudokuData data, Writer writer) throws IOException {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        xml.append("<sudoku>\n");

        for (SudokuData.CellEntry cell : data.cells) {
            xml.append("  <feld ")
                    .append("zahl=\"").append(cell.value).append("\" ")
                    .append("positionY=\"").append(cell.row).append("\" ")
                    .append("positionX=\"").append(cell.col).append("\"")
                    .append(" />\n");
        }

        xml.append("</sudoku>\n");
        writer.write(xml.toString());
    }
}
