package org.rmit_SudokuSolver.Algorithms;

public class Bitmasking_BacktrackingSolver implements RMIT_Sudoku_Solver {
    private int[] rows = new int[9];
    private int[] cols = new int[9];
    private int[][] boxes = new int[3][3];

    public boolean solve(int[][] board) {
        initBits(board); // Initialize bitmasks
        return backtrack(board, 0, 0);
    }

    @Override
    public String getApproachName() {
        return "Bitmasking";
    }

    private boolean backtrack(int[][] board, int row, int col) {
        if (row == 9) return true;
        if (col == 9) return backtrack(board, row + 1, 0);
        if (board[row][col] != 0) return backtrack(board, row, col + 1);

        int boxRow = row / 3, boxCol = col / 3;
        int used = rows[row] | cols[col] | boxes[boxRow][boxCol];

        for (int num = 1; num <= 9; num++) {
            int mask = 1 << num;
            if ((used & mask) == 0) {
                // Set the bit
                rows[row] |= mask;
                cols[col] |= mask;
                boxes[boxRow][boxCol] |= mask;
                board[row][col] = num;

                if (backtrack(board, row, col + 1)) return true;

                // Unset the bit
                rows[row] ^= mask;
                cols[col] ^= mask;
                boxes[boxRow][boxCol] ^= mask;
                board[row][col] = 0;
            }
        }
        return false;
    }

    private void initBits(int[][] board) {
        // Clear previous state
        for (int i = 0; i < 9; i++) {
            rows[i] = 0;
            cols[i] = 0;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boxes[i][j] = 0;
            }
        }

        // Now set bits based on the current board
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != 0) {
                    int num = board[r][c];
                    int mask = 1 << num;
                    rows[r] |= mask;
                    cols[c] |= mask;
                    boxes[r / 3][c / 3] |= mask;
                }
            }
        }
    }

}