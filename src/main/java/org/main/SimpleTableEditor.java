package org.main;

import org.parser.Parser;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class SimpleTableEditor extends JFrame {
    private int editingRow = -1;
    private int editingColumn = -1;
    private final Map<Cell, String> originalCellValue = new HashMap<>();
    private final Map<Cell, String> calculatedCellValue = new HashMap<>();
    private Cell lastSelectedCell = null;
    private CellDependencyGraph dependencyGraph = new CellDependencyGraph();
    private final Frame frame;
    private final JTable table;
    private Cell currentCell = null;


    public SimpleTableEditor(Frame frame) {
        super("Simple Table Editor");
        this.frame = frame;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        var model = createTableModel(frame.getColumnLabels(), frame.getRowLabels());
        fillRowLabels(model, frame.getRowLabels());

        table = new JTable(model);
        setupTable();
        var scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private DefaultTableModel createTableModel(String[] columnLabels, String[] rowLabels) {
        return new DefaultTableModel(columnLabels, rowLabels.length) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
    }

    private void fillRowLabels(DefaultTableModel model, String[] rowLabels) {
        for (int i = 0; i < rowLabels.length; i++) {
            model.setValueAt(rowLabels[i], i, 0);
        }
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

        var editor = new DefaultCellEditor(new JTextField());
        setupEditorListener(editor);
        table.setDefaultEditor(Object.class, editor);

        table.getSelectionModel().addListSelectionListener(event -> handleCellSelection());
        table.getColumnModel().getSelectionModel().addListSelectionListener(event -> handleCellSelection());
    }

    private void setupEditorListener(DefaultCellEditor editor) {
        editor.addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                handleEditingStopped(editor);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                editingRow = -1;
                editingColumn = -1;
                System.out.println("Editing canceled");
            }
        });
    }

    private void handleEditingStopped(DefaultCellEditor editor) {
        editingRow = table.getSelectedRow();
        editingColumn = table.getSelectedColumn();
        System.out.println("Editing stopped at [" + editingRow + ", " + editingColumn + "]");
        if (editingRow != -1 && editingColumn != -1) {
            var value = editor.getCellEditorValue();
            handleEditingFinalized(editingRow, editingColumn, value);
            editingRow = -1;
            editingColumn = -1;
        }
    }

    private void handleCellSelection() {
        var selectedRow = table.getSelectedRow();
        var selectedColumn = table.getSelectedColumn();
        if (selectedColumn == 0) {
            table.clearSelection();
        } else if (selectedRow >= 0 && selectedColumn >= 0) {
            updateCellValues(new Cell(selectedRow, selectedColumn));
        }
    }

    private void updateCellValues(Cell currentSelectedCell) {
        if (lastSelectedCell != null && calculatedCellValue.containsKey(lastSelectedCell)) {
            table.getModel().setValueAt(calculatedCellValue.get(lastSelectedCell), lastSelectedCell.row(), lastSelectedCell.column());
        }
        if (originalCellValue.containsKey(currentSelectedCell)) {
            table.getModel().setValueAt(originalCellValue.get(currentSelectedCell), currentSelectedCell.row(), currentSelectedCell.column());
        }
        lastSelectedCell = currentSelectedCell;
    }

    private void handleEditingFinalized(int row, int column, Object value) {
        var currentValue = table.getModel().getValueAt(row, column);
        System.out.println("Current value at [" + row + ", " + column + "]: " + currentValue);
        try {
            var selectedCell = new Cell(row, column);
            String input = value.toString();
            if (input.trim().isEmpty()) {
                table.getModel().setValueAt("", row, column);
                calculatedCellValue.put(new Cell(row, column), "");
                originalCellValue.put(new Cell(row, column), "");
                var dependents = dependencyGraph.getAllDependents(selectedCell);
                for (var dependent : dependents) {
                    table.getModel().setValueAt("error", dependent.row(), dependent.column());
                    calculatedCellValue.put(new Cell(dependent.row(), dependent.column()), "error");
                }
                return;
            }
            dependencyGraph.removeDependent(selectedCell);
            var dependents = dependencyGraph.getAllDependents(selectedCell);

            parseCell(input, row, column);
            for (var dependent : dependents) {
                parseCell(originalCellValue.get(dependent), dependent.row(), dependent.column());
            }
            System.out.println("Edited value at [" + row + ", " + column + "]: " + value);
        } catch (RuntimeException e) {
            System.err.println("Failed to parse and evaluate expression: " + e.getMessage());
            table.getModel().setValueAt(value, row, column);
            calculatedCellValue.put(new Cell(row, column), "error");
            originalCellValue.put(new Cell(row, column), value.toString());
        }
    }

    private void parseCell(String input, int row, int column) {
        currentCell = new Cell(row, column);
        var parser = new Parser(input, this);
        var expression = parser.parse();
        var result = expression.evaluate();
        var resultValue = Double.toString(result);
        table.getModel().setValueAt(resultValue, row, column);
        calculatedCellValue.put(new Cell(row, column), resultValue);
        originalCellValue.put(new Cell(row, column), input);
    }

    public JTable getTable() {
        return table;
    }

    public Frame getFrame() {
        return frame;
    }

    public CellDependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SimpleTableEditor <width> <height>");
            System.exit(1);
        } else {
            var frame = new Frame(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            SwingUtilities.invokeLater(() -> new SimpleTableEditor(frame).setVisible(true));
        }
    }
}