package org.rmit_SudokuSolver.Algorithms.DancingLinks_Backtracking;

import java.util.ArrayList;
import java.util.List;

public class DancingLinks_BacktrackingSolver {

    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;
    private static final int EMPTY = 0;

    // DLX Node structure
    private static class Node {
        Node left, right, up, down;
        ColumnHeader header;

        Node() {
            this.left = this.right = this.up = this.down = this;
        }
    }

    private static class ColumnHeader extends Node {
        int size; // Number of nodes in the column
        int id; // For debugging

        ColumnHeader(int id) {
            this.id = id;
            this.size = 0;
        }
    }

    private ColumnHeader header;
    private List<Node> solution;
    private int[][] solutionBoard;

    public DancingLinks_BacktrackingSolver() {
        header = createDLXMatrix();
        solution = new ArrayList<>();
    }

    private ColumnHeader createDLXMatrix() {
        // Create column headers
        ColumnHeader root = new ColumnHeader(-1);
        ColumnHeader prev = root;

        // 4 types of constraints: cell, row, column, box
        for (int i = 0; i < SIZE * SIZE * 4; i++) {
            ColumnHeader col = new ColumnHeader(i);
            col.left = prev;
            prev.right = col;
            prev = col;
        }
        prev.right = root;
        root.left = prev;

        return root;
    }

    public boolean solve(int[][] board) {
        solutionBoard = new int[SIZE][SIZE];
        addSudokuConstraints(board);
        return search(0);
    }

    private void addSudokuConstraints(int[][] board) {
        // For each cell, row, column, and possible number
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                for (int num = 1; num <= SIZE; num++) {
                    // If cell is empty or already has this number
                    if (board[row][col] == EMPTY || board[row][col] == num) {
                        // Calculate constraint positions
                        int cellConstraint = row * SIZE + col;
                        int rowConstraint = SIZE * SIZE + row * SIZE + (num - 1);
                        int colConstraint = 2 * SIZE * SIZE + col * SIZE + (num - 1);
                        int boxConstraint = 3 * SIZE * SIZE +
                                ((row / BOX_SIZE) * BOX_SIZE + (col / BOX_SIZE)) * SIZE + (num - 1);

                        // Create a node for each constraint
                        Node[] nodes = new Node[4];
                        ColumnHeader[] headers = new ColumnHeader[4];

                        headers[0] = getColumnHeader(cellConstraint);
                        headers[1] = getColumnHeader(rowConstraint);
                        headers[2] = getColumnHeader(colConstraint);
                        headers[3] = getColumnHeader(boxConstraint);

                        // Link the nodes
                        for (int i = 0; i < 4; i++) {
                            nodes[i] = new Node();
                            nodes[i].header = headers[i];

                            // Link to column header
                            nodes[i].up = headers[i].up;
                            nodes[i].down = headers[i];
                            headers[i].up.down = nodes[i];
                            headers[i].up = nodes[i];
                            headers[i].size++;

                            // Link to other nodes in the same row
                            if (i > 0) {
                                nodes[i].left = nodes[i-1];
                                nodes[i-1].right = nodes[i];
                            }
                        }
                        nodes[0].left = nodes[3];
                        nodes[3].right = nodes[0];
                    }
                }
            }
        }
    }

    private ColumnHeader getColumnHeader(int id) {
        ColumnHeader current = (ColumnHeader) header.right;
        while (current != header) {
            if (current.id == id) {
                return current;
            }
            current = (ColumnHeader) current.right;
        }
        return null;
    }

    private boolean search(int depth) {
        if (header.right == header) {
            // Solution found
            processSolution();
            return true;
        }

        ColumnHeader col = selectColumn();
        cover(col);

        for (Node row = col.down; row != col; row = row.down) {
            solution.add(row);

            for (Node node = row.right; node != row; node = node.right) {
                cover(node.header);
            }

            if (search(depth + 1)) {
                return true;
            }

            solution.remove(solution.size() - 1);

            for (Node node = row.left; node != row; node = node.left) {
                uncover(node.header);
            }
        }

        uncover(col);
        return false;
    }

    private void cover(ColumnHeader col) {
        col.right.left = col.left;
        col.left.right = col.right;

        for (Node row = col.down; row != col; row = row.down) {
            for (Node node = row.right; node != row; node = node.right) {
                node.up.down = node.down;
                node.down.up = node.up;
                node.header.size--;
            }
        }
    }

    private void uncover(ColumnHeader col) {
        for (Node row = col.up; row != col; row = row.up) {
            for (Node node = row.left; node != row; node = node.left) {
                node.header.size++;
                node.up.down = node;
                node.down.up = node;
            }
        }

        col.right.left = col;
        col.left.right = col;
    }

    private ColumnHeader selectColumn() {
        ColumnHeader selected = null;
        int minSize = Integer.MAX_VALUE;

        for (ColumnHeader col = (ColumnHeader) header.right; col != header; col = (ColumnHeader) col.right) {
            if (col.size < minSize) {
                minSize = col.size;
                selected = col;
            }
        }

        return selected;
    }

    private void processSolution() {
        for (Node rowNode : solution) {
            // Find the first node in the row to get all constraints
            Node node = rowNode;
            int row = -1, col = -1, num = -1;

            do {
                int constraintId = node.header.id;

                if (constraintId < SIZE * SIZE) {
                    // Cell constraint
                    row = constraintId / SIZE;
                    col = constraintId % SIZE;
                } else if (constraintId < 2 * SIZE * SIZE) {
                    // Row constraint
                    int temp = constraintId - SIZE * SIZE;
                    if (row == -1) {
                        row = temp / SIZE;
                    }
                    num = (temp % SIZE) + 1;
                } else if (constraintId < 3 * SIZE * SIZE) {
                    // Column constraint
                    int temp = constraintId - 2 * SIZE * SIZE;
                    if (col == -1) {
                        col = temp / SIZE;
                    }
                }

                node = node.right;
            } while (node != rowNode);

            solutionBoard[row][col] = num;
        }
    }

    public int[][] getSolution() {
        return solutionBoard;
    }

    public static void main(String[] args) {
        int[][] puzzle = {
                {0, 2, 0,    0, 0, 0,    0, 0, 0},
                {0, 0, 0,    6, 0, 0,    0, 0, 3},
                {0, 7, 4,    0, 8, 0,    0, 0, 0},

                {0, 0, 0,    0, 0, 3,    0, 0, 2},
                {0, 8, 0,    0, 4, 0,    0, 1, 0},
                {6, 0, 0,    5, 0, 0,    0, 0, 0},

                {0, 0, 0,    0, 1, 0,    7, 8, 0},
                {5, 0, 0,    0, 0, 9,    0, 0, 0},
                {0, 0, 0,    0, 0, 0,    0, 4, 0}
        };
        printBoard(puzzle);
        System.out.println("------------------------------------------");
        DancingLinks_BacktrackingSolver solver = new DancingLinks_BacktrackingSolver();
        if (solver.solve(puzzle)) {
            int[][] solution = solver.getSolution();
            printBoard(solution);
        } else {
            System.out.println("No solution exists");
        }
    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
                if ((j + 1) % BOX_SIZE == 0 && j != SIZE - 1) {
                    System.out.print("| ");
                }
            }
            System.out.println();
            if ((i + 1) % BOX_SIZE == 0 && i != SIZE - 1) {
                System.out.println("---------------------");
            }
        }
    }
}

