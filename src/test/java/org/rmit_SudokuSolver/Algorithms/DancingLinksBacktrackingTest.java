package org.rmit_SudokuSolver.Algorithms;

import org.junit.Test;

import org.rmit_SudokuSolver.Models.ArrayList;
import org.rmit_SudokuSolver.Utils.PerformanceTester;
import org.rmit_SudokuSolver.Utils.PuzzleLoader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.rmit_SudokuSolver.Utils.PuzzleLoader.deepCopy;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DancingLinksBacktrackingTest {

    RMIT_Sudoku_Solver solver = new DancingLinks_BacktrackingSolver();

    @Test
    public void test1_EasyPuzzle() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        ArrayList<int[][]> puzzles = PuzzleLoader.loadAllFromDirectory("src/main/resources" +
                "/easy");
        for(int i = 0; i < puzzles.size(); i++){
            PerformanceTester.evaluate("Easy", deepCopy(puzzles.get(i)), solver);
        }
    }

    @Test
    public void test2_MediumPuzzle() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        ArrayList<int[][]> puzzles = PuzzleLoader.loadAllFromDirectory("src/main/resources" +
                "/medium");
        for(int i = 0; i < puzzles.size(); i++){
            PerformanceTester.evaluate("Medium", deepCopy(puzzles.get(i)), solver);
        }
    }

    @Test
    public void test3_HardPuzzle() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        ArrayList<int[][]> puzzles = PuzzleLoader.loadAllFromDirectory("src/main/resources" +
                "/hard");
        for(int i = 0; i < puzzles.size(); i++){
            PerformanceTester.evaluate("Hard", deepCopy(puzzles.get(i)), solver);
        }
    }
}
