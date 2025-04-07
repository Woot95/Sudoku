import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class SudokuFrame extends JFrame {

    private SudokuBlock[][] blocks = new SudokuBlock[3][3];

    public SudokuFrame() {
        setTitle("Sudoku");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 550);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(3, 3));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Blöcke hinzufügen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                blocks[i][j] = new SudokuBlock(i, j);
                gridPanel.add(blocks[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        setJMenuBar(createMenu());

        newGame();
        setVisible(true);
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Datei");

        JMenuItem newItem = new JMenuItem("Neu");
        JMenuItem importItem = new JMenuItem("Von XML importieren");
        JMenuItem saveItem = new JMenuItem("Speichern");
        JMenuItem loadItem = new JMenuItem("Laden");

        newItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });

        importItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                importSudoku();
            }
        });


        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });

        loadItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        menu.add(newItem);
        menu.add(importItem);
        menu.add(saveItem);
        menu.add(loadItem);
        menuBar.add(menu);

        return menuBar;
    }

    private void newGame() {
        SudokuGenerator generator = new SudokuGenerator();
        int[][] puzzle = generator.generatePuzzle(30);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                JButton cell = getSudokuField(i,j);
                if (puzzle[i][j] != 0) {
                    cell.setText(String.valueOf(puzzle[i][j]));
                    cell.setEnabled(false); // fixe Startzahlen
                } else {
                    cell.setText("");
                    cell.setEnabled(true);
                }
            }
        }
    }

    private void importSudoku() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                SudokuData data = SudokuXMLReader.readFromXml(file.getAbsolutePath());

                resetAllCells();

                applySudokuData(data);

                JOptionPane.showMessageDialog(this, "Spiel erfolgreich geladen!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Fehler beim Laden: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveGame() {
        String gameName = JOptionPane.showInputDialog(this, "Spielname eingeben:");

        if (gameName == null || gameName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Speichern abgebrochen: Kein Name angegeben.");
            return;
        }

        SudokuData data = createSudokuDataFromBoard();

        try {
            StringWriter writer = new StringWriter();
            SudokuXMLWriter.writeToXml(data, writer);
            SudokuDBHandler.saveGameToDatabase(gameName, writer.toString());
            JOptionPane.showMessageDialog(this, "Spiel wurde unter '" + gameName + "' gespeichert.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        String gameName = JOptionPane.showInputDialog(this, "Name des gespeicherten Spiels eingeben:");

        if (gameName == null || gameName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ladevorgang abgebrochen: Kein Name angegeben.");
            return;
        }

        try {
            String xmlContent = SudokuDBHandler.loadGameFromDatabase(gameName);
            if (xmlContent == null) {
                JOptionPane.showMessageDialog(this, "Kein Spiel mit dem Namen '" + gameName + "' gefunden.");
                return;
            }

            // XML-String in SudokuData umwandeln
            SudokuData data = SudokuXMLReader.readFromXmlString(xmlContent);

            resetAllCells();

            applySudokuData(data);

            JOptionPane.showMessageDialog(this, "Spiel '" + gameName + "' erfolgreich geladen!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Laden: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private SudokuData createSudokuDataFromBoard() {
        SudokuData data = new SudokuData();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                JButton cell = getSudokuField(i,j);
                String text = cell.getText();
                if (text != null && !text.isEmpty()) {
                    try {
                        int value = Integer.parseInt(text);
                        data.cells.add(new SudokuData.CellEntry(i, j, value));
                    } catch (NumberFormatException e) {
                        // ungültiger Wert wird ignoriert
                    }
                }
            }
        }
        return data;
    }

    private void resetAllCells() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JButton cell = getSudokuField(row, col);
                resetCell(cell);
            }
        }
    }

    private void resetCell(JButton cell) {
        cell.setText("");
        cell.setEnabled(true);
    }

    private void applySudokuData(SudokuData data) {
        for (SudokuData.CellEntry cell : data.cells) {
            JButton button = getSudokuField(cell.row, cell.col);
            button.setText(String.valueOf(cell.value));
            button.setEnabled(false);
        }
    }

    private JButton getSudokuField(int row, int col) {
        return blocks[row / 3][col / 3].getCell(row % 3, col % 3);
    }
}
