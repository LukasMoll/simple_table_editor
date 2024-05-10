package org.main;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class TableController {
    private TableModel model;
    private TableView view;

    public TableController(TableModel model, TableView view) {
        this.model = model;
        this.view = view;

        // Here you might add action listeners to view buttons, etc.
        // For example, to add a row when a button is clicked
        // view.getAddRowButton().addActionListener(e -> addRow());
    }

    private void addRow() {
        model.addRow(new ArrayList<>(Collections.nCopies(model.getData().get(0).size(), "New Data")));
    }

    private void removeRow(int rowIndex) {
        model.removeRow(rowIndex);
    }
}

