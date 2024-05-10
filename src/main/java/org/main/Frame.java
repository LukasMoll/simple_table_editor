package org.main;

import java.util.HashMap;
import java.util.Map;

public class Frame {
    private final int width;
    private final int height;
    private final String[] columnLabels;
    private final String[] rowLabels;
    private final Map<String, Integer> columnLabelToColumn = new HashMap<>();

    public Frame(int width, int height) {
        this.width = width;
        this.height = height;
        this.columnLabels = generateColumnLabels(width);
        this.rowLabels = generateRowLabels(height);
    }

    // Generates labels for the X-axis (columns) similar to Excel (A, B, C, ..., Z, AA, AB, ...)
    private String[] generateColumnLabels(int count) {
        String[] labels = new String[count + 1];
        labels[0] = ""; // Empty label for the first cell
        for (int i = 0; i < count; i++) {
            labels[i + 1] = toExcelColumn(i);
            columnLabelToColumn.put(toExcelColumn(i), i + 1);
        }
        return labels;
    }

    // Generates labels for the Y-axis (rows) starting from 1 up to 'height'
    private String[] generateRowLabels(int count) {
        String[] labels = new String[count];
        for (int i = 0; i < count; i++) {
            labels[i] = String.valueOf(i + 1);
        }
        return labels;
    }

    // Helper method to convert a column index to its Excel-style column label
    private String toExcelColumn(int index) {
        StringBuilder columnName = new StringBuilder();
        int cycle = index;
        while (cycle >= 0) {
            int remainder = cycle % 26;
            columnName.insert(0, (char) (remainder + 'A'));
            cycle = (cycle / 26) - 1;
        }
        return columnName.toString();
    }

    // Getters for width, height, and labels
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String[] getColumnLabels() {
        return columnLabels;
    }

    public String[] getRowLabels() {
        return rowLabels;
    }
    public Map<String, Integer> getColumnLabelToColumn() {
        return columnLabelToColumn;
    }
}
