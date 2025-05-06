package org.rmit_SudokuSolver.Utils;

import java.io.FileWriter;
import java.io.IOException;

// A utility class solely to log results into a CSV format and use for graphing
public class PerformanceLogger {
    public static void log(String difficulty, String algorithm, double timeMs,
                           double memoryKB) {
        try (FileWriter writer = new FileWriter("performance_results.csv", true)) {
            writer.write(difficulty + "," + algorithm + "," + timeMs + "," + memoryKB + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
