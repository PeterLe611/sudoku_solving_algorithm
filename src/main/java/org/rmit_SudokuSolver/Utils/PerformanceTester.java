package org.rmit_SudokuSolver.Utils;

import org.rmit_SudokuSolver.Algorithms.RMIT_Sudoku_Solver;
import java.util.concurrent.*;

// This class's role is suggested in its name, to test each function as well as calculating important metrics
public class PerformanceTester {
    static final int NUM_RUNS = 3; // the number of times an algorithm will be run for each
    // test
    // cases, for example: easy1.txt will run 5 times, the same with every other puzzle

    private static void printSideBySideBoards(int[][] initial, int[][] solved) {
        System.out.println("Initial Puzzle\t\t\tSolved Puzzle");
        for (int i = 0; i < 9; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                row.append(initial[i][j] == 0 ? ". " : initial[i][j] + " ");
            }
            row.append("\t\t");
            for (int j = 0; j < 9; j++) {
                row.append(solved[i][j] + " ");
            }
            System.out.println(row);
        }
    }

    public static void evaluate(String difficulty,int[][] board, RMIT_Sudoku_Solver solver) {
        for (int i = 1; i <= NUM_RUNS; i++) {
            // Save initial board state
            int[][] initialBoard = new int[9][9];
            for (int r = 0; r < 9; r++) {
                System.arraycopy(board[r], 0, initialBoard[r], 0, 9);
            }

            // Create a fresh copy of the board for solving
            int[][] clonedBoard = new int[9][9];
            for (int r = 0; r < 9; r++) {
                System.arraycopy(board[r], 0, clonedBoard[r], 0, 9);
            }

            // Perform garbage collection to reduce noise in memory measurement
            System.gc();

            // Set up a single-thread executor to run the solver with a timeout
            ExecutorService executor = Executors.newSingleThreadExecutor();

            // Submit the solver task to the executor
            Future<Boolean> future = executor.submit(() -> solver.solve(clonedBoard));

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
                System.out.println("Failed to solve puzzle for algorithm: " + solver.getApproachName() + ", the board cannot be solved!");
            }

            printSideBySideBoards(initialBoard, clonedBoard); // Print the two boards side by side

            // Print final evaluation report
            System.out.println("Difficulty: " + difficulty);
            System.out.printf("Time: %.3f ms\n", durationMs);
            System.out.printf("Memory: %.3f KB\n", memoryUsedKB);
            System.out.printf("Steps taken: %d\n", solver.getStepCount());
            System.out.println();

            PerformanceLogger.log(difficulty, solver.getApproachName(), durationMs,
                    memoryUsedKB);
        }
    }
}