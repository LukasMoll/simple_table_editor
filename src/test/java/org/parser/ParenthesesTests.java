package org.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.main.*;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParenthesesTests {
    private Cell cell;
    private Parser parser;

    @BeforeEach
    public void setUp() {
        int width = 10;
        int height = 10;
        int screenWidth = 10;
        int screenHeight = 10;
        var tableModel = new TableModel(width, height, screenWidth, screenHeight);
        var tableView = new TableView(tableModel);
        new TableController(tableView, tableModel, 1);
        SwingUtilities.invokeLater(() -> tableView.setVisible(true));
        parser = new Parser();
        cell = tableModel.getCell(2, 2);
    }

    @Test
    public void testNestedParentheses() {
        String input = "(3 + (4 - 1)) * 2";
        cell.setValue(input);

        Expr expression = parser.parse(cell);
        assertEquals(12.0, expression.evaluate(), "Nested parentheses failed");
    }

    @Test
    public void testMultipleLevelsOfNesting() {
        String input = "((2 + 3) * (3 - 1)) / 2";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(5.0, expression.evaluate(), "Multiple levels of nesting failed");
    }

    @Test
    public void testExtraParentheses() {
        String input = "(3 + 2))";
        cell.setValue(input);

        assertThrows(RuntimeException.class, () -> parser.parse(cell), "Extra parentheses should throw error");
    }

    @Test
    public void testMissingParentheses() {
        String input = "(3 + 2";
        cell.setValue(input);

        assertThrows(RuntimeException.class, () -> parser.parse(cell), "Missing parentheses should throw error");
    }
}
