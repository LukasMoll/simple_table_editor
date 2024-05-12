package org.main;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class TableView extends JFrame {
    private final JTable table;
    private final DefaultCellEditor editor;

    public TableView(TableModel tableModel) {
        super("Simple Table Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(tableModel.getScreenWidth(), tableModel.getScreenHeight());
        setLocationRelativeTo(null);

        var model = createTableModel(tableModel.getColumnLabels(), tableModel.getRowLabels());
        fillRowLabels(model, tableModel.getRowLabels());
        editor = new DefaultCellEditor(new JTextField());
        table = new JTable(model);
        setupTable();
        var scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public void setCellValue(int row, int column, String value) {
        if (row < 0 || row >= table.getRowCount() || column <= 0 || column > table.getColumnCount()) {
            return;
        }
        table.setValueAt(value, row, column);
    }

    public String getSelectedCellValue() {
        return table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
    }

    public void setSelectedCellValue(String value) {
        table.setValueAt(value, table.getSelectedRow(), table.getSelectedColumn());
    }

    private void fillRowLabels(DefaultTableModel model, String[] rowLabels) {
        for (int i = 0; i < rowLabels.length; i++) {
            model.setValueAt(rowLabels[i], i, 0);
        }
    }

    private DefaultTableModel createTableModel(String[] columnLabels, String[] rowLabels) {
        return new DefaultTableModel(columnLabels, rowLabels.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
    }

    private void setupTable() {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                var component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                component.setBackground(isSelected ? Color.CYAN : Color.WHITE);
                setForeground(Color.BLACK);
                setHorizontalAlignment(CENTER);
                return component;
            }
        });
        disableSelectionFirstColumn();
        table.setDefaultEditor(Object.class, editor);
    }

    private void disableSelectionFirstColumn() {
        table.getColumnModel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedColumn = table.getSelectedColumn();
                // If the first column is selected, move the selection to the second column
                if (selectedColumn == 0) {
                    table.setColumnSelectionInterval(1, 1);
                }
            }
        });
    }

    public JTable getTable() {
        return table;
    }

    public DefaultCellEditor getEditor() {
        return editor;
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public int getSelectedColumn() {
        return table.getSelectedColumn();
    }
}
