package org.main;

import org.parser.Parser;

import java.util.*;

public class TableModel {
    private List<List<Object>> data; // Your data grid

    public TableModel() {
        data = new ArrayList<>();
        // Initialize with some dummy data
        addRow(Arrays.asList("Row 1", "Data", "More Data"));
        addRow(Arrays.asList("Row 2", "Data", "More Data"));
    }

    public List<List<Object>> getData() {
        return data;
    }

    public void addRow(List<Object> rowData) {
        data.add(rowData);
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            data.remove(rowIndex);
        }
    }

    public void setValueAt(int rowIndex, int columnIndex, Object value) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            List<Object> row = data.get(rowIndex);
            if (columnIndex >= 0 && columnIndex < row.size()) {
                row.set(columnIndex, value);
            }
        }
    }
}

