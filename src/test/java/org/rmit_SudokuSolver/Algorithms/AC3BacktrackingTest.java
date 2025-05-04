package org.rmit_SudokuSolver.Algorithms;

import org.junit.Test;
import static org.junit.Assert.*;

import org.rmit_SudokuSolver.Utils.PuzzleLoader;
import java.io.IOException;

public class AC3BacktrackingTest {

    AC3_BacktrackingSolver solver = new AC3_BacktrackingSolver();

    @Test
    public void testEasyPuzzle() throws IOException {
        int[][] board = PuzzleLoader.loadFromFile("src/main/resources/easy.txt");
        long start = System.nanoTime();
        boolean solved = solver.solve(board);
        long end = System.nanoTime();
        assertTrue(solved);
        System.out.println("Backtracking (Easy): " + (end - start) / 1_000_000 + " ms");
    }

    @Test
    public void testMediumPuzzle() throws IOException {
        int[][] board = PuzzleLoader.loadFromFile("src/main/resources/medium.txt");
        long start = System.nanoTime();
        boolean solved = solver.solve(board);
        long end = System.nanoTime();
        assertTrue(solved);
        System.out.println("Backtracking (Medium): " + (end - start) / 1_000_000 + " ms");
    }

    @Test
    public void testHardPuzzle() throws IOException {
        int[][] board = PuzzleLoader.loadFromFile("src/main/resources/hard.txt");
        long start = System.nanoTime();
        boolean solved = solver.solve(board);
        long end = System.nanoTime();
        assertTrue(solved);
        System.out.println("Backtracking (Hard): " + (end - start) / 1_000_000 + " ms");
    }
}
