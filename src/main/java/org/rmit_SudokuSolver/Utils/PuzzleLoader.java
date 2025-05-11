package org.rmit_SudokuSolver.Utils;

import org.rmit_SudokuSolver.Models.ArrayList;
import org.rmit_SudokuSolver.Models.BoardStatus;

import java.io.*;

public class PuzzleLoader {

    // Deep copy method for copying 2D array
    public static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone(); // copies the contents of each row
        }
        return copy;
    }

    // Validate the loaded Sudoku board
    private static BoardStatus validateBoard(int[][] board) {
        if (board == null || board.length != 9) {
            return BoardStatus.NULL_OR_WRONG_SIZE; // Invalid size
        }

        // Validate the values within the board
        for (int[] row : board) {
            if (row == null || row.length != 9) {
                return BoardStatus.NULL_OR_WRONG_SIZE; // Invalid row size
            }
            for (int val : row) {
                if (val < 0 || val > 9) {
                    return BoardStatus.INVALID_VALUE_RANGE; // Invalid number range
                }
            }
        }

        boolean hasNonZero = false;
        boolean hasZero = false;
        for (int[] row : board) {
            for (int val : row) {
                if (val == 0) hasZero = true;
                else hasNonZero = true;
            }
        }

        if (!hasNonZero) {
            return BoardStatus.COMPLETELY_EMPTY; // No values to solve
        }
        if (!hasZero) {
            return BoardStatus.FULLY_SOLVED; // Already solved
        }

        return BoardStatus.VALID; // Valid grid
    }

    // Load a single puzzle from file
    public static int[][] loadFromFile(String filePath) throws IOException {
        int[][] board = new int[9][9];

        // Get the boards from the edge_cases file path
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < 9) {
                String[] tokens = line.trim().split("\\s+");
                for (int col = 0; col < 9 && col < tokens.length; col++) {
                    board[row][col] = Integer.parseInt(tokens[col]);
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found, " + e.getMessage());
            System.exit(1);
        }

        // Validate the loaded board before starting any algorithm
        BoardStatus status = validateBoard(board);
        if (status != BoardStatus.VALID) {
            System.out.println("Invalid puzzle in file: " + filePath);
            switch (status) {
                case INVALID_VALUE_RANGE: // Invalid board with 'out of range' values
                    System.out.println("Reason: Board contains values outside the range " +
                            "[0–9].\n");
                    break;
                case COMPLETELY_EMPTY: // Completely empty board => multiple results boards
                    // like this should be handled carefully
                    System.out.println("Reason: Board is completely empty.\n");
                    break;
                case FULLY_SOLVED: // A fully solved board
                    System.out.println("Reason: Board is already fully solved.\n");
                    break;
                default: // Random unknown issue
                    System.out.println("Reason: Unknown issue.\n");
            }
            return null; // Return null to indicate invalid board
        }

        return board;
    }

    // Load all puzzles from a directory
    public static ArrayList<int[][]> loadAllFromDirectory(String dirPath) throws IOException {
        File dir = new File(dirPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            throw new FileNotFoundException("No puzzle files found in directory: " + dirPath);
        }

        ArrayList<int[][]> puzzles = new ArrayList<>();

        for (File file : files) {
            int[][] puzzle = loadFromFile(file.getAbsolutePath());
            if (puzzle != null) {
                puzzles.insertAt(puzzles.size(), puzzle); // Add puzzle if valid
            }
        }

        return puzzles;
    }
}
