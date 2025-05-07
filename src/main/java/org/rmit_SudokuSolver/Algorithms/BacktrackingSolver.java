package org.rmit_SudokuSolver.Algorithms;

public class BacktrackingSolver implements RMIT_Sudoku_Solver {
    private static final int SIZE = 9;
    private static final int EMPTY = 0;

    private int stepCount = 0; // Step counter

    @Override
    public boolean solve(int[][] board) {
        stepCount = 0; // Reset step count at start
        return backtrack(board);
    }

    private boolean backtrack(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            stepCount++; // Count every assignment

                            if (backtrack(board)) {
                                return true;
                            }

                            board[row][col] = EMPTY; // Backtrack
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
        return "Backtracking";
    }

    @Override
    public int getStepCount() {
        return stepCount;
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
