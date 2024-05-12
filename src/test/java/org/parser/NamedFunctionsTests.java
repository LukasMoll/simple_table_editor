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

public class NamedFunctionsTests {
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
    public void testUnsupportedCharacters() {
        String input = "3 + a";
        cell.setValue(input);

        assertThrows(RuntimeException.class, () -> {
            Expr expression = parser.parse(cell);
            expression.evaluate();
        }, "Unsupported characters should throw exception");
    }

    @Test
    public void testSingleFunction() {
        String input = "SIN(30)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(0.5, expression.evaluate(), 0.01, "Sine function failed");
    }

    @Test
    public void testFunctionWithMultipleArguments() {
        String input = "MAX(1, 2, 3)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(3.0, expression.evaluate(), "Max function with multiple arguments failed");
    }

    @Test
    public void testFunctionWithIncorrectArguments() {
        String input = "SIN(30, 40)";
        cell.setValue(input);

        assertThrows(IllegalArgumentException.class, () -> {
            Expr expression = parser.parse(cell);
            expression.evaluate();
        }, "Function with incorrect number of arguments should throw exception");
    }

    @Test
    public void testComplexFunctionalExpression() {
        String input = "3 * POW(2, 3) + SIN(30)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(24.5, expression.evaluate(), 0.01, "Complex functional expression failed");
    }

    @Test
    public void testSinFunction() {
        String input = "SIN(30)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(0.5, expression.evaluate(), 0.01, "Sine function failed");
    }

    @Test
    public void testCosFunction() {
        String input = "COS(60)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(0.5, expression.evaluate(), 0.01, "Cosine function failed");
    }

    @Test
    public void testTanFunction() {
        String input = "TAN(45)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(1.0, expression.evaluate(), 0.01, "Tangent function failed");
    }

    @Test
    public void testLogFunction() {
        String input = "LOG(10)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(2.302585092994046, expression.evaluate(), 0.01, "Logarithm function failed");
    }

    @Test
    public void testExpFunction() {
        String input = "EXP(1)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(Math.E, expression.evaluate(), 0.01, "Exponential function failed");
    }

    @Test
    public void testLog10Function() {
        String input = "LOG10(100)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(2.0, expression.evaluate(), 0.01, "Log10 function failed");
    }

    @Test
    public void testRoundFunction() {
        String input = "ROUND(2.5)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(3.0, expression.evaluate(), 0.01, "Round function failed");
    }

    @Test
    public void testCeilFunction() {
        String input = "CEIL(2.1)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(3.0, expression.evaluate(), 0.01, "Ceil function failed");
    }

    @Test
    public void testFloorFunction() {
        String input = "FLOOR(2.9)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(2.0, expression.evaluate(), 0.01, "Floor function failed");
    }

    @Test
    public void testSqrtFunction() {
        String input = "SQRT(16)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(4.0, expression.evaluate(), 0.01, "Square root function failed");
    }

    @Test
    public void testAbsFunction() {
        String input = "ABS(-5)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(5.0, expression.evaluate(), 0.01, "Absolute function failed");
    }

    @Test
    public void testPowFunction() {
        String input = "POW(2, 3)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(8.0, expression.evaluate(), 0.01, "Power function failed");
    }

    @Test
    public void testModFunction() {
        String input = "MOD(10, 4)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(2.0, expression.evaluate(), 0.01, "Modulo function failed");
    }

    @Test
    public void testMaxFunction() {
        String input = "MAX(1, 2, 3)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(3.0, expression.evaluate(), "Max function failed");
    }

    @Test
    public void testMinFunction() {
        String input = "MIN(1, 2, 3)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(1.0, expression.evaluate(), "Min function failed");
    }

    @Test
    public void testSumFunction() {
        String input = "SUM(1, 2, 3)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(6.0, expression.evaluate(), "Sum function failed");
    }

    @Test
    public void testAverageFunction() {
        String input = "AVERAGE(1, 2, 3)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(2.0, expression.evaluate(), "Average function failed");
    }

    @Test
    public void testDegreesFunction() {
        String input = "DEGREES(2)";
        double expected = Math.toDegrees(2);
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(expected, expression.evaluate(), 0.01, "Degrees function failed");
    }

    @Test
    public void testRadiansFunction() {
        String input = "RADIANS(180)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(Math.PI, expression.evaluate(), "Radians function failed");
    }

    @Test
    public void testSignumFunction() {
        String input = "SIGNUM(-5)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(-1.0, expression.evaluate(), 0.01, "Signum function failed");
    }

    @Test
    public void testMaxFunctionWithComplexInput() {
        String input = "MAX(3, 2*2, 1+5)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertEquals(6.0, expression.evaluate(), "Max function with complex input failed");
    }

    @Test
    public void testFunctionsWithIncorrectArgumentCounts() {
        String input = "MIN(1)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertThrows(IllegalArgumentException.class, expression::evaluate, "Function with incorrect number of arguments should throw exception");
    }

    @Test
    public void testFunctionsWithIncorrectArgumentTypes() {
        String input = "SIN(30, 40)";
        cell.setValue(input);

        Expr expression = parser.parse(cell);

        assertThrows(IllegalArgumentException.class, expression::evaluate, "Function with incorrect number of arguments should throw exception");
    }
}
