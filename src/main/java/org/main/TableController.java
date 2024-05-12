package org.main;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;


public class TableController {
    private final TableModel tableModel;
    private final TableView view;
    private final Deque<TableModelSnapshot> history = new ArrayDeque<>();
    private final int maxHistory;


    public TableController(TableView view, TableModel tableModel, int maxHistory) {
        this.view = view;
        this.tableModel = tableModel;
        this.maxHistory = maxHistory;
        tableModel.setListener(this);
        setupCellSelectionListener();
        setupCellEditorListener();
        setupKeyBindings();
        history.add(new TableModelSnapshot(tableModel));
    }

    private void setupCellSelectionListener() {
        view.getTable().getSelectionModel().addListSelectionListener(event -> handleCellSelection());
        view.getTable().getColumnModel().getSelectionModel().addListSelectionListener(event -> handleCellSelection());
    }

    private void setupCellEditorListener() {
        view.getEditor().addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                handleCellEdit();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {}
        });
    }

    private void setupKeyBindings() {
        String os = System.getProperty("os.name").toLowerCase();
        KeyStroke undoKeyStroke;
        if (os.contains("mac")) {
            undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
        } else {
            undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        }

        var table = view.getTable();
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(undoKeyStroke, "undoAction");
        table.getActionMap().put("undoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (history.size() <= 1) {
                    return;
                }
                history.pollLast();
                tableModel.restoreFromSnapshot(history.getLast());
                var cells = tableModel.getCells();
                for (int i = 0; i < cells.size(); i++) {
                    for (int j = 0; j < cells.get(i).size(); j++) {
                        view.setCellValue(i, j, cells.get(i).get(j).getParsedValue());
                    }
                }
            }
        });
    }

    private void handleCellSelection() {
        handleCellDeselection();
        var selectedRow = view.getSelectedRow();
        var selectedColumn = view.getSelectedColumn();
        if (selectedRow == -1 || selectedColumn == -1) {
            return;
        }
        var selectedCell = tableModel.getCell(selectedRow, selectedColumn);
        tableModel.setSelectedCell(selectedCell);
        view.setCellValue(selectedRow, selectedColumn, selectedCell.getValue());
    }

    private void handleCellDeselection() {
        var selectedCell = tableModel.getSelectedCell();
        if (selectedCell != null) {
            var parsedValue = selectedCell.getParsedValue();
            if (parsedValue != null) {
                view.setCellValue(selectedCell.getRow(), selectedCell.getColumn(), parsedValue);
            }
        }
    }

    public void handleCellUpdate(Cell cell) {
        var selectedCell = tableModel.getSelectedCell();
        if (cell.equals(selectedCell)) {
            view.setSelectedCellValue(cell.getValue());
        } else {
            view.setCellValue(cell.getRow(), cell.getColumn(), cell.getParsedValue());
        }
    }

    private void handleCellEdit() {
        var value = view.getSelectedCellValue();
        tableModel.parse(value);
        if (history.size() == maxHistory) {
            history.poll();
        }
        history.add(new TableModelSnapshot(tableModel));
    }
}
