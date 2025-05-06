package org.rmit_SudokuSolver.Utils;

import org.rmit_SudokuSolver.Models.ArrayList;

import java.io.*;

public class PuzzleLoader {

    public static int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone(); // copies the contents of each row
        }

        return copy;
    }

    public static int[][] loadFromFile(String filePath) throws IOException {
        int[][] board = new int[9][9];

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

        return board;
    }

    public static ArrayList<int[][]> loadAllFromDirectory(String dirPath) throws IOException {
        File dir = new File(dirPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            throw new FileNotFoundException("No puzzle files found in directory: " + dirPath);
        }

        ArrayList<int[][]> puzzles = new ArrayList<>();
        for (File file : files) {
            // Load each puzzle from file and add it to the list
            int[][] puzzle = loadFromFile(file.getAbsolutePath());
            puzzles.insertAt(puzzles.size(), puzzle); // Insert the loaded puzzle into the ArrayList
        }

        return puzzles;
    }

}
