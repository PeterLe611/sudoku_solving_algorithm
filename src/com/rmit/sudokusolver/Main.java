package com.rmit.sudokusolver;

import com.rmit.sudokusolver.algorithms.AC3.SudokuSolverAC3;
import com.rmit.sudokusolver.algorithms.RMIT_Sudoku_Solver;
import com.rmit.sudokusolver.algorithms.BacktrackingSolver;
import com.rmit.sudokusolver.models.SudokuBoard;

public class Main {
    // Easy puzzle (can be solved with simple elimination)
    private static final int[][] EASY_PUZZLE = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    // Medium puzzle (requires some backtracking)
    private static final int[][] MEDIUM_PUZZLE = {
            {0, 0, 0, 2, 6, 0, 7, 0, 1},
            {6, 8, 0, 0, 7, 0, 0, 9, 0},
            {1, 9, 0, 0, 0, 4, 5, 0, 0},
            {8, 2, 0, 1, 0, 0, 0, 4, 0},
            {0, 0, 4, 6, 0, 2, 9, 0, 0},
            {0, 5, 0, 0, 0, 3, 0, 2, 8},
            {0, 0, 9, 3, 0, 0, 0, 7, 4},
            {0, 4, 0, 0, 5, 0, 0, 3, 6},
            {7, 0, 3, 0, 1, 8, 0, 0, 0}
    };

    // Hard puzzle (requires extensive backtracking)
    private static final int[][] HARD_PUZZLE = {
            {8, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 3, 6, 0, 0, 0, 0, 0},
            {0, 7, 0, 0, 9, 0, 2, 0, 0},
            {0, 5, 0, 0, 0, 7, 0, 0, 0},
            {0, 0, 0, 0, 4, 5, 7, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 3, 0},
            {0, 0, 1, 0, 0, 0, 0, 6, 8},
            {0, 0, 8, 5, 0, 0, 0, 1, 0},
            {0, 9, 0, 0, 0, 0, 4, 0, 0}
    };

    public static void main(String[] args) {
        solvePuzzle(EASY_PUZZLE, "Easy");
        solvePuzzle(MEDIUM_PUZZLE, "Medium");
        solvePuzzle(HARD_PUZZLE, "Hard");

        solvePuzzleAC3(EASY_PUZZLE, "Easy");
        solvePuzzleAC3(MEDIUM_PUZZLE, "Medium");
        solvePuzzleAC3(HARD_PUZZLE, "Hard");
    }

    private static void solvePuzzleAC3(int[][] puzzle, String difficulty) {
        System.out.println("\n=== " + difficulty + " Puzzle ===");

        SudokuBoard board = new SudokuBoard();
        board.setGrid(puzzle);

        System.out.println("\nOriginal puzzle:");
        board.print();

        SudokuSolverAC3 solver = new SudokuSolverAC3();

        // Capture start time
        long startTime = System.currentTimeMillis();
        System.out.println("\nStart Time: " + new java.util.Date(startTime));

        boolean solved = solver.solve(board.getGrid());

        // Capture end time
        long endTime = System.currentTimeMillis();
        System.out.println("End Time: " + new java.util.Date(endTime));

        // Calculate duration
        long duration = endTime - startTime;
        System.out.println("Time Executed: " + duration + " milliseconds");

        System.out.println("\nSolved puzzle:");
        if (solved) {
            board.print();
        } else {
            System.out.println("Could not solve this puzzle.");
        }

        System.out.println("\n" + "=".repeat(40));
    }

    private static void solvePuzzle(int[][] puzzle, String difficulty) {
        System.out.println("\n=== " + difficulty + " Puzzle ===");

        SudokuBoard board = new SudokuBoard();
        board.setGrid(puzzle);

        System.out.println("\nOriginal puzzle:");
        board.print();

        RMIT_Sudoku_Solver solver = new BacktrackingSolver();

        // Capture start time
        long startTime = System.currentTimeMillis();
        System.out.println("\nStart Time: " + new java.util.Date(startTime));

        boolean solved = solver.solve(board.getGrid());

        // Capture end time
        long endTime = System.currentTimeMillis();
        System.out.println("End Time: " + new java.util.Date(endTime));

        // Calculate duration
        long duration = endTime - startTime;
        System.out.println("Time Executed: " + duration + " milliseconds");

        System.out.println("\nSolved puzzle:");
        if (solved) {
            board.print();
        } else {
            System.out.println("Could not solve this puzzle.");
        }

        System.out.println("\n" + "=".repeat(40));
    }
}