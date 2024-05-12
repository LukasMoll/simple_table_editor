package org.main;

import org.parser.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableModel {
    private final int width;
    private final int height;
    private final int screenWidth;
    private final int screenHeight;
    private final String[] columnLabels;
    private final String[] rowLabels;
    private final Map<String, Integer> columnLabelToColumn = new HashMap<>();
    private final List<List<Cell>> cells = new ArrayList<>();
    private Cell selectedCell;
    private TableController tableController = null;


    public TableModel(int width, int height, int screenWidth, int screenHeight) {
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.columnLabels = generateColumnLabels(width);
        this.rowLabels = generateRowLabels(height);
        var cellDependencyGraph = new CellDependencyGraph();
        var parser = new Parser();
        for (int i = 0; i < height; i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j <= width; j++) {
                row.add(new Cell(i, j,"", "", cellDependencyGraph, this, parser));
            }
            cells.add(row);
        }
    }

    public List<List<Cell>> getCells() {
        return cells;
    }

    public TableModelSnapshot createSnapshot() {
        return new TableModelSnapshot(this);
    }

    public void restoreFromSnapshot(TableModelSnapshot snapshot) {
        snapshot.restore(this);
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

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public String[] getColumnLabels() {
        return columnLabels;
    }

    public String[] getRowLabels() {
        return rowLabels;
    }

    public int columnLabelToColumn(String columnLabel) {
        return columnLabelToColumn.get(columnLabel);
    }

    public int rowLabelToRow(String rowLabel) {
        return Integer.parseInt(rowLabel) - 1;
    }

    public Cell getCell(int row, int column) {
        return cells.get(row).get(column);
    }

    public void setCellParsedValue(int row, int column, String parsedValue) {
        var cell = cells.get(row).get(column);
        if (parsedValue == null) {
            cell.setParsedValue(cell.getValue());
        }
        cell.setParsedValue(parsedValue);
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(Cell cell) {
        selectedCell = cell;
    }

    public void handleCellUpdate(Cell cell) {
        tableController.handleCellUpdate(cell);
    }

    public void setListener(TableController tableController) {
        this.tableController = tableController;
    }

    public void parse(String value) {
        if (selectedCell != null) {
            selectedCell.setValue(value);
            selectedCell.parse();
        }
    }
}
