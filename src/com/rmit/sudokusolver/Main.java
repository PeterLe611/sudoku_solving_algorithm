package com.rmit.sudokusolver;

import com.rmit.sudokusolver.algorithms.AC3_backtracking.AC3_BacktrackingSolver;
import com.rmit.sudokusolver.algorithms.RMIT_Sudoku_Solver;
import com.rmit.sudokusolver.algorithms.bitmasking_backtracking.BitmaskBacktrackingSolver;
import com.rmit.sudokusolver.algorithms.standard_backtracking.StandardBacktrackingSolver;
import com.rmit.sudokusolver.models.SudokuBoard;
import com.rmit.sudokusolver.utils.PuzzleLoader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String easyPath = "src/resources/puzzles/easy.txt";
        String mediumPath = "src/resources/puzzles/medium.txt";
        String hardPath = "src/resources/puzzles/hard.txt";

        System.out.println("\n\n===== STANDARD BACKTRACKING SOLVER =====\n");
        solveWithSolver(new StandardBacktrackingSolver(), "Standard Backtracking", easyPath, mediumPath, hardPath);

        System.out.println("\n\n===== BITMASK BACKTRACKING SOLVER =====\n");
        solveWithSolver(new BitmaskBacktrackingSolver(), "Bitmask Backtracking", easyPath, mediumPath, hardPath);

        System.out.println("\n\n===== AC3 BACKTRACKING SOLVER =====\n");
        solveWithSolver(new AC3_BacktrackingSolver(), "Bitmask Backtracking", easyPath, mediumPath, hardPath);
    }

    private static void solveWithSolver(RMIT_Sudoku_Solver solver, String solverName, String easy, String medium, String hard) throws IOException {
        System.out.println("\n=== " + solverName + " ===");
        solvePuzzle(easy, "Easy", solver);
        solvePuzzle(medium, "Medium", solver);
        solvePuzzle(hard, "Hard", solver);
    }

    private static void solvePuzzle(String filePath, String difficulty, RMIT_Sudoku_Solver solver) throws IOException {
        System.out.println("\n=== " + difficulty + " Puzzle ===");

        int[][] puzzle = PuzzleLoader.loadFromFile(filePath);

        SudokuBoard board = new SudokuBoard();
        board.setGrid(puzzle);

        System.out.println("\nOriginal puzzle:");
        board.print();

        long startTime = System.currentTimeMillis();
        System.out.println("\nStart Time: " + new java.util.Date(startTime));

        boolean solved = solver.solve(board.getGrid());

        long endTime = System.currentTimeMillis();
        System.out.println("End Time: " + new java.util.Date(endTime));

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