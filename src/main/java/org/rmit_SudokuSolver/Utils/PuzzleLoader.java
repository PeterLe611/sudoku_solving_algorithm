package org.rmit_SudokuSolver.Utils;

import java.io.*;
import java.util.*;

public class PuzzleLoader {

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
        }

        return board;
    }
}
