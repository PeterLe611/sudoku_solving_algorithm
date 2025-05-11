package org.rmit_SudokuSolver.Utils;

import java.io.FileWriter;
import java.io.IOException;

// A utility class solely to log results into a CSV format and use for graphing
public class PerformanceLogger {
    public static void log(String difficulty, String algorithm, double timeMs,
                           double memoryKB) {
        try (FileWriter writer = new FileWriter("performance_results.csv", true)) { // Save
            // to performance_results.csv for logging results and graphing values
            writer.write(difficulty + "," + algorithm + "," + timeMs + "," + memoryKB + "\n"); // Saves every result in this format
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
