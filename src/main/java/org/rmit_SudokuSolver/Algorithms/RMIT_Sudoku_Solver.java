package org.rmit_SudokuSolver.Algorithms;

public interface RMIT_Sudoku_Solver {
    boolean solve(int[][] board);
    String getApproachName();
    int getStepCount();
}
