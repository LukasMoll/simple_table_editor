package org.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.main.Cell;
import org.main.TableModel;
import org.main.TableController;
import org.main.TableView;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class BinaryOperatorsTests {
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
    public void testSimpleAddition() {
        String input = "3 + 4";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(7.0, expression.evaluate(), "Simple addition failed");
    }

    @Test
    public void testComplexExpression() {
        String input = "3 + 4 * 2";
        cell.setValue(input);

        Expr expression = parser.parse(cell);
        assertEquals(11.0, expression.evaluate(), "Expression with priority failed");
    }

    @Test
    public void testDivisionAndSubtraction() {
        String input = "8 - 2 / 2";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(7.0, expression.evaluate(), "Complex operation with division and subtraction failed");
    }

    @Test
    public void testMissingOperator() {
        String input = "3 3";

        cell.setValue(input);

        assertThrows(RuntimeException.class, () -> parser.parse(cell));
    }

    @Test
    public void testMissingOperand() {
        String input = "3 +";

        cell.setValue(input);

        assertThrows(RuntimeException.class, () -> parser.parse(cell));
    }

    @Test
    public void testMissingOperand2() {
        String input = "+ 3";

        cell.setValue(input);

        assertThrows(RuntimeException.class, () -> parser.parse(cell));
    }
}