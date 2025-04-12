package com.rmit.sudokusolver.algorithms.AC3;

import com.rmit.sudokusolver.algorithms.RMIT_Sudoku_Solver;

import java.util.*;

public class SudokuSolverAC3 implements RMIT_Sudoku_Solver {
    static final int SIZE = 9;

    public static void main(String[] args) {
        int[][] board = {
                {0, 0, 0, 2, 6, 0, 7, 0, 1},
                {6, 8, 0, 0, 7, 0, 0, 9, 0},
                {1, 9, 0, 0, 0, 4, 5, 0, 0},
                {8, 2, 0, 1, 0, 0, 0, 4, 0},
                {0, 0, 4, 6, 0, 2, 9, 0, 0},
                {0, 5, 0, 0, 0, 3, 0, 2, 8},
                {0, 0, 9, 3, 0, 0, 0, 7, 4},
                {0, 4, 0, 0, 5, 0, 0, 3, 6},
                {7, 0, 3, 0, 1, 8, 0, 0, 0}
        };

        SudokuSolverAC3 solver = new SudokuSolverAC3();
        if (solver.solve(board)) {
            solver.printBoard(board);
        } else {
            System.out.println("No solution found.");
        }
    }

    // Entry point: apply AC3 then backtracking
    public boolean solve(int[][] board) {
        Map<String, Set<Integer>> domains = initializeDomains(board);
        if (!ac3(domains)) return false;
        return backtrack(board, domains);
    }

    @Override
    public String getApproachName() {
        return "AC3 +  Backtrack";
    }

    // Initialize domains for all cells
    private Map<String, Set<Integer>> initializeDomains(int[][] board) {
        Map<String, Set<Integer>> domains = new HashMap<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String key = row + "," + col;
                if (board[row][col] == 0) {
                    domains.put(key, getPossibleValues(board, row, col));
                } else {
                    domains.put(key, new HashSet<>(Collections.singletonList(board[row][col])));
                }
            }
        }
        return domains;
    }

    // Get all possible values for a cell
    private Set<Integer> getPossibleValues(int[][] board, int row, int col) {
        Set<Integer> possible = new HashSet<>();
        for (int i = 1; i <= 9; i++) possible.add(i);

        for (int i = 0; i < SIZE; i++) {
            possible.remove(board[row][i]);
            possible.remove(board[i][col]);
        }

        int rStart = row / 3 * 3, cStart = col / 3 * 3;
        for (int r = rStart; r < rStart + 3; r++)
            for (int c = cStart; c < cStart + 3; c++)
                possible.remove(board[r][c]);

        return possible;
    }

    // AC-3 Algorithm
    private boolean ac3(Map<String, Set<Integer>> domains) {
        Queue<String[]> queue = new LinkedList<>();

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String xi = r + "," + c;
                for (String xj : getNeighbors(r, c)) {
                    queue.add(new String[]{xi, xj});
                }
            }
        }

        while (!queue.isEmpty()) {
            String[] arc = queue.poll();
            if (revise(domains, arc[0], arc[1])) {
                if (domains.get(arc[0]).isEmpty()) return false;
                for (String xk : getNeighbors(arc[0])) {
                    if (!xk.equals(arc[1])) {
                        queue.add(new String[]{xk, arc[0]});
                    }
                }
            }
        }
        return true;
    }

    private boolean revise(Map<String, Set<Integer>> domains, String xi, String xj) {
        boolean revised = false;
        Set<Integer> toRemove = new HashSet<>();
        for (int x : domains.get(xi)) {
            boolean satisfied = false;
            for (int y : domains.get(xj)) {
                if (x != y) {
                    satisfied = true;
                    break;
                }
            }
            if (!satisfied) toRemove.add(x);
        }

        if (!toRemove.isEmpty()) {
            domains.get(xi).removeAll(toRemove);
            revised = true;
        }
        return revised;
    }

    private Set<String> getNeighbors(String key) {
        String[] parts = key.split(",");
        return getNeighbors(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    private Set<String> getNeighbors(int row, int col) {
        Set<String> neighbors = new HashSet<>();
        for (int i = 0; i < SIZE; i++) {
            if (i != col) neighbors.add(row + "," + i);
            if (i != row) neighbors.add(i + "," + col);
        }

        int rStart = row / 3 * 3, cStart = col / 3 * 3;
        for (int r = rStart; r < rStart + 3; r++)
            for (int c = cStart; c < cStart + 3; c++)
                if (r != row || c != col)
                    neighbors.add(r + "," + c);

        return neighbors;
    }

    // Backtracking search with domain map
    private boolean backtrack(int[][] board, Map<String, Set<Integer>> domains) {
        String unassigned = null;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c] == 0) {
                    unassigned = r + "," + c;
                    break;
                }
            }
            if (unassigned != null) break;
        }

        if (unassigned == null) return true;

        String[] parts = unassigned.split(",");
        int row = Integer.parseInt(parts[0]), col = Integer.parseInt(parts[1]);
        for (int val : domains.get(unassigned)) {
            if (isSafe(board, row, col, val)) {
                board[row][col] = val;
                Map<String, Set<Integer>> newDomains = deepCopy(domains);
                newDomains.get(unassigned).clear();
                newDomains.get(unassigned).add(val);
                if (ac3(newDomains) && backtrack(board, newDomains)) return true;
                board[row][col] = 0;
            }
        }

        return false;
    }

    // Check if placing value is valid
    private boolean isSafe(int[][] board, int row, int col, int val) {
        for (int i = 0; i < SIZE; i++)
            if (board[row][i] == val || board[i][col] == val)
                return false;

        int rStart = row / 3 * 3, cStart = col / 3 * 3;
        for (int r = rStart; r < rStart + 3; r++)
            for (int c = cStart; c < cStart + 3; c++)
                if (board[r][c] == val)
                    return false;

        return true;
    }

    // Deep copy domain map
    private Map<String, Set<Integer>> deepCopy(Map<String, Set<Integer>> original) {
        Map<String, Set<Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    // Print the board
    private void printBoard(int[][] board) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
    }
}
