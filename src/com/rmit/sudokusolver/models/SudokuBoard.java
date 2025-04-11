package com.rmit.sudokusolver.models;

public class SudokuBoard {
    private int[][] grid = new int[9][9];

    // Constructor
    public SudokuBoard() {
        // Initialize empty board
    }

    // Getters and setters
    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    // Print method
    public void print() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
