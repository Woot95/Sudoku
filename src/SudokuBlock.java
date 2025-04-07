import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class SudokuBlock extends JPanel {

    private final int SIZE = 3;
    protected final JButton[][] cells = new JButton[SIZE][SIZE];

    public SudokuBlock(int blockRow, int blockCol) {
        setLayout(new GridLayout(SIZE, SIZE));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        initializeCells(blockRow, blockCol);
    }

    private void initializeCells(int blockRow, int blockCol) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int row = blockRow * SIZE + i;
                int col = blockCol * SIZE + j;
                JButton cell = createCell(row, col);
                cells[i][j] = cell;
                add(cell);
            }
        }
    }

    private JButton createCell(int row, int col) {
        JButton cell = new JButton();
        cell.setFont(new Font("Arial", Font.BOLD, 18));

        cell.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCellInput(cell);
            }
        });

        return cell;
    }

    private void handleCellInput(JButton cell) {
        String input = JOptionPane.showInputDialog(this, "Zahl eingeben (1–9):");

        if (input != null) {
            try {
                int value = Integer.parseInt(input);
                if (value >= 1 && value <= 9) {
                    cell.setText(String.valueOf(value));
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Ungültige Eingabe...",
                            "",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ungültige Eingabe...",
                        "",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public JButton getCell(int i, int j) {
        return cells[i][j];
    }

    public void clear() {
        for (JButton[] row : cells) {
            for (JButton cell : row) {
                cell.setText("");
            }
        }
    }
}
