package org.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.main.Cell;
import org.main.TableModel;
import org.main.TableController;
import org.main.TableView;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UnaryOperatorsTests {
    private Cell cell;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        int width = 10;
        int height = 10;
        int screenWidth = 10;
        int screenHeight = 10;
        var tableModelrame = new TableModel(width, height, screenWidth, screenHeight);
        var tableView = new TableView(tableModelrame);
        new TableController(tableView, tableModelrame, 1);
        SwingUtilities.invokeLater(() -> tableView.setVisible(true));
        parser = new Parser();
        cell = tableModelrame.getCell(2, 2);
    }

    @Test
    public void testNegationSign() {
        String input = "-3";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(-3.0, expression.evaluate(), "Negation sign failed");
    }
}
