package org.main;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class TableView extends JFrame {
    private JTable table;
    private TableModel tableModel;

    public TableView(TableModel model) {
        this.tableModel = model;
        setTitle("Table Editor");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        table = new JTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return tableModel.getData().size();
            }

            @Override
            public int getColumnCount() {
                return tableModel.getData().isEmpty() ? 0 : tableModel.getData().get(0).size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return tableModel.getData().get(rowIndex).get(columnIndex);
            }

            @Override
            public void setValueAt(Object value, int rowIndex, int columnIndex) {
                tableModel.setValueAt(rowIndex, columnIndex, value);
                fireTableCellUpdated(rowIndex, columnIndex);
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }
        });

        add(new JScrollPane(table));
        setVisible(true);
    }
}
