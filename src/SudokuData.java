import java.util.ArrayList;
import java.util.List;

public class SudokuData {
    public List<CellEntry> cells = new ArrayList<>();

    public static class CellEntry {
        public int row;
        public int col;
        public int value;

        public CellEntry(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }
    }
}
