package org.rmit_SudokuSolver;

import org.rmit_SudokuSolver.Algorithms.*;
import org.rmit_SudokuSolver.Models.SudokuBoard;
import org.rmit_SudokuSolver.Utils.PuzzleLoader;

import java.io.IOException;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        String easyPath = "src/main/resources/easy/easy2.txt";
        String mediumPath = "src/resources/puzzles/medium1.txt";
        String hardPath = "src/resources/puzzles/hard1.txt";

        System.out.println("\n\n===== STANDARD BACKTRACKING SOLVER =====\n");
        solveWithSolver(new BacktrackingSolver(), "Standard Backtracking", easyPath,
                mediumPath, hardPath);

        System.out.println("\n\n===== DANCING LINKS BACKTRACKING SOLVER =====\n");
        solveWithSolver(new DancingLinks_BacktrackingSolver(), "DancingLinks Backtracking",
                easyPath, mediumPath, hardPath);

        System.out.println("\n\n===== AC3 BACKTRACKING SOLVER =====\n");
        solveWithSolver(new AC3_BacktrackingSolver(), "Bitmask Backtracking", easyPath,
                mediumPath, hardPath);
    }

    private static void solveWithSolver(RMIT_Sudoku_Solver solver, String solverName,
                                        String easyPath, String mediumPath, String hardPath) throws IOException, ExecutionException,
            InterruptedException, TimeoutException {
        System.out.println("\n=== " + solverName + " ===");

        solvePuzzle(easyPath, "Easy", solver);
        solvePuzzle(mediumPath, "Medium", solver);
        solvePuzzle(hardPath, "Hard", solver);
    }

    private static void solvePuzzle(String filePath, String difficulty, RMIT_Sudoku_Solver solver) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        System.out.println("\n=== " + difficulty + " Puzzle ===");
        int[][] puzzle = PuzzleLoader.loadFromFile(filePath);

        SudokuBoard board = new SudokuBoard();
        board.setGrid(puzzle);
        System.out.println("Original puzzle: ");
        board.print();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(
                () -> solver.solve(puzzle)); // Assign task to the thread
        boolean solved = future.get(120, TimeUnit.SECONDS);
        future.cancel(true);

        long startTime = System.currentTimeMillis();
        System.out.println("\nStart Time: " + new java.util.Date(startTime));

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