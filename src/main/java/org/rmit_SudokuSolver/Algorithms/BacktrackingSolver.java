package org.rmit_SudokuSolver.Algorithms;


public class BacktrackingSolver implements RMIT_Sudoku_Solver {
    private static final int SIZE = 9;
    private static final int EMPTY = 0;

    @Override
    public boolean solve(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;

                            if (solve(board)) {
                                return true;
                            }

                            // Backtrack
                            board[row][col] = EMPTY;
                        }
                    }
                    return false; // Trigger backtracking
                }
            }
        }
        return true; // All cells filled
    }

    @Override
    public String getApproachName() {
        return "Backtrack Algorithm";
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // Check row
        for (int c = 0; c < SIZE; c++) {
            if (board[row][c] == num) {
                return false;
            }
        }

        // Check column
        for (int r = 0; r < SIZE; r++) {
            if (board[r][col] == num) {
                return false;
            }
        }

        // Check 3x3 box
        int boxStartRow = row - row % 3;
        int boxStartCol = col - col % 3;
        for (int r = boxStartRow; r < boxStartRow + 3; r++) {
            for (int c = boxStartCol; c < boxStartCol + 3; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }

        return true;
    }
}