import java.util.*;

public class SudokuGenerator {
    private int[][] board = new int[9][9];
    private Random random = new Random();

    public int[][] generatePuzzle(int clues) {
        fillBoard(0, 0);              // Vollständiges Board generieren
        removeCells(81 - clues);      // Zellen entfernen, sodass nur noch "clues" übrig bleiben
        return board;
    }

    private boolean fillBoard(int row, int col) {
        if (row == 9) return true;
        if (col == 9) return fillBoard(row + 1, 0);

        List<Integer> numbers = getShuffledNumbers();
        for (int num : numbers) {
            if (isSafe(row, col, num)) {
                board[row][col] = num;
                if (fillBoard(row, col + 1)) return true;
                board[row][col] = 0; // backtrack
            }
        }
        return false;
    }

    private List<Integer> getShuffledNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) numbers.add(i);
        Collections.shuffle(numbers);
        return numbers;
    }

    private boolean isSafe(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[startRow + i][startCol + j] == num)
                    return false;

        return true;
    }

    private void removeCells(int count) {
        int removed = 0;
        while (removed < count) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                removed++;
            }
        }
    }
}
