package org.main;

import org.parser.Parser;


public class Cell {
    private String value;
    private String parsedValue;
    private final CellDependencyGraph cellDependencyGraph;
    private final TableModel tableModel;
    private final int row;
    private final int column;
    private final Parser parser;


    public Cell(int row, int column, String value, String parsedValue, CellDependencyGraph cellDependencyGraph, TableModel tableModel, Parser parser) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.parsedValue = parsedValue;
        this.cellDependencyGraph = cellDependencyGraph;
        this.tableModel = tableModel;
        this.parser = parser;
    }

    public Cell copy() {
        return new Cell(row, column, value, parsedValue, cellDependencyGraph, tableModel, parser);
    }

    public void setValue(String value) {
        this.value = value;
        tableModel.handleCellUpdate(this);
    }

    public String getValue() {
        return value;
    }

    public void setParsedValue(String parsedValue) {
        this.parsedValue = parsedValue;
    }

    public String getParsedValue() {
        return parsedValue;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void parse() {
        try {
            var expr = parser.parse(this);
            var result = expr.evaluate();
            parsedValue = String.valueOf(result);
        } catch (Exception e) {
            setParsedValue(value);
        } finally {
            tableModel.handleCellUpdate(this);
        }
        var dependents = cellDependencyGraph.getAllDependents(this);
        for (var dependent : dependents) {
            dependent.parse();
        }
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public CellDependencyGraph getCellDependencyGraph() {
        return cellDependencyGraph;
    }
}
