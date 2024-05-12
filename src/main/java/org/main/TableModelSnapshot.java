package org.main;

import java.util.ArrayList;
import java.util.List;

public class TableModelSnapshot {
    private final List<List<Cell>> cells;
    private final CellDependencyGraph cellDependencyGraph;

    public TableModelSnapshot(TableModel tableModel) {
        this.cells = new ArrayList<>();
        for (int i = 0; i < tableModel.getHeight(); i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < tableModel.getWidth(); j++) {
                row.add(tableModel.getCell(i, j).copy());
            }
            this.cells.add(row);
        }
        var dependencyGraph = tableModel.getCell(1, 1).getCellDependencyGraph();
        this.cellDependencyGraph = new CellDependencyGraph(dependencyGraph);
    }

    public void restore(TableModel tableModel) {
        for (int i = 0; i < tableModel.getHeight(); i++) {
            for (int j = 0; j < tableModel.getWidth(); j++) {
                var cell = tableModel.getCell(i, j);
                var snapshotCell = this.cells.get(i).get(j);
                cell.setValue(snapshotCell.getValue());
                cell.setParsedValue(snapshotCell.getParsedValue());
            }
        }
        var dependencyGraph = tableModel.getCell(1, 1).getCellDependencyGraph();
        dependencyGraph.restore(this.cellDependencyGraph);
    }
}
