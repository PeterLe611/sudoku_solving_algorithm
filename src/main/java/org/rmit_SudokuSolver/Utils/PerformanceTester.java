package org.rmit_SudokuSolver.Utils;

import org.rmit_SudokuSolver.Algorithms.RMIT_Sudoku_Solver;
import org.rmit_SudokuSolver.Models.SudokuBoard;

import java.util.concurrent.*;

// This class's role is suggested in its name, to test each function as well as calculating important metrics
public class PerformanceTester {
    static final int NUM_RUNS = 3; // the number of times an algorithm will be run for each
    // test
    // cases, for example: easy1.txt will run 5 times, the same with every other puzzle

    private static boolean isValidInput(int[][] board) {
        if (board == null || board.length != 9) return false;
        for (int i = 0; i < 9; i++) {
            if (board[i] == null || board[i].length != 9) return false;
            for (int j = 0; j < 9; j++) {
                int val = board[i][j];
                if (val < 0 || val > 9) return false;
            }
        }

        return true;
    }

    public static void evaluate(String difficulty,int[][] board, RMIT_Sudoku_Solver solver) {
        for (int i = 1; i <= NUM_RUNS; i++) {
            if (!isValidInput(
                    board)) { // Quick puzzle input validation test before actually solving
                System.out.println(
                        "Invalid puzzle input for difficulty: " + difficulty + " - The board is either not a 9x9 Grid board, or " +
                                "initial values violate a Sudoku board constraint.");
                return;
            }

            // Perform garbage collection to reduce noise in memory measurement
            System.gc();

            // Set up a single-thread executor to run the solver with a timeout
            ExecutorService executor = Executors.newSingleThreadExecutor();

            // Submit the solver task to the executor
            Future<Boolean> future = executor.submit(() -> solver.solve(board));

            boolean solved = false;
            double durationMs = 0;
            double memoryUsedKB = 0;

            try {
                // Record the start time
                long startTime = System.nanoTime();

                // Wait for the solver to finish with a 2-minute timeout
                solved = future.get(120, TimeUnit.SECONDS);

                // Record the end time
                long endTime = System.nanoTime();

                // Calculate total duration in milliseconds
                durationMs = (double) (endTime - startTime) / 1_000_000;

                // Measure memory used after solving (not delta)
                long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                memoryUsedKB = (double) usedMemory / 1024;
            } catch (TimeoutException e) {
                // If the solver exceeds 2 minutes, cancel it
                System.out.println("Solver exceeded time limit: " + difficulty);
                future.cancel(true); // Attempt to stop the solver
            } catch (Exception e) {
                // Catch and print any other exception that occurs during execution
                System.out.println("Solver error: " + e.getMessage());
            } finally {
                // Shut down the executor to free resources
                executor.shutdownNow();
            }

            // Log result if solver failed (either due to timeout or logic failure)
            if (!solved) {
                System.out.println("Failed to solve: " + difficulty);
            }

            System.out.println("Solved puzzle: ");
            SudokuBoard solvedPuzzle = new SudokuBoard();
            solvedPuzzle.setGrid(board);
            solvedPuzzle.print();

            // Print final evaluation report
            System.out.println("Difficulty: " + difficulty);
            System.out.printf("Time: %.3f ms\n", durationMs);
            System.out.printf("Memory: %.3f KB\n", memoryUsedKB);
            System.out.println();

            PerformanceLogger.log(difficulty, solver.getApproachName(), durationMs,
                    memoryUsedKB);
        }
    }
}