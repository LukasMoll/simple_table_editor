package org.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NamedFunctionsTests {
    @Test
    public void testUnsupportedCharacters() {
        String input = "3 + a";
        Parser parser = new Parser(input, null);

        assertThrows(RuntimeException.class, parser::parse, "Unsupported characters should throw error");
    }

    @Test
    public void testSingleFunction() {
        String input = "SIN(30)";
        Parser parser = new Parser(input,null);

        Expr expression = parser.parse();

        assertEquals(0.5, expression.evaluate(), 0.01, "Sine function failed");
    }

    @Test
    public void testFunctionWithMultipleArguments() {
        String input = "MAX(1, 2, 3)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(3.0, expression.evaluate(), "Max function with multiple arguments failed");
    }

    @Test
    public void testFunctionWithIncorrectArguments() {
        String input = "SIN(30, 40)";
        Parser parser = new Parser(input, null);

        assertThrows(IllegalArgumentException.class, () -> {
            Expr expression = parser.parse();
            expression.evaluate();
        }, "Function with incorrect number of arguments should throw exception");
    }

    @Test
    public void testComplexFunctionalExpression() {
        String input = "3 * POW(2, 3) + SIN(30)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(24.5, expression.evaluate(), 0.01, "Complex functional expression failed");
    }

    @Test
    public void testCosFunction() {
        String input = "COS(60)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(0.5, expression.evaluate(), 0.01, "Cosine function failed");
    }

    @Test
    public void testTanFunction() {
        String input = "TAN(45)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(1.0, expression.evaluate(), 0.01, "Tangent function failed");
    }

    @Test
    public void testDegreesFunction() {
        String input = "DEGREES(2)";
        double expected = Math.toDegrees(2);
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(expected, expression.evaluate(), 0.01, "Degrees function failed");
    }

    @Test
    public void testRadiansFunction() {
        String input = "RADIANS(180)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(Math.PI, expression.evaluate(), "Radians function failed");
    }

    @Test
    public void testSqrtFunction() {
        String input = "SQRT(16)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(4.0, expression.evaluate(), "Square root function failed");
    }

    @Test
    public void testExpFunction() {
        String input = "EXP(1)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(Math.E, expression.evaluate(), 0.01, "Exponential function failed");
    }

    @Test
    public void testMinFunction() {
        String input = "MIN(4, 1, 2, 3)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(1.0, expression.evaluate(), "Min function failed");
    }

    @Test
    public void testMaxFunctionWithComplexInput() {
        String input = "MAX(3, 2*2, 1+5)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(6.0, expression.evaluate(), "Max function with complex input failed");
    }

    @Test
    public void testSumFunction() {
        String input = "SUM(1, 2, 3)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(6.0, expression.evaluate(), "Sum function failed");
    }

    @Test
    public void testAverageFunction() {
        String input = "AVERAGE(2, 4)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(3.0, expression.evaluate(), "Average function failed");
    }

    @Test
    public void testModFunction() {
        String input = "MOD(10, 4)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(2.0, expression.evaluate(), "Modulo function failed");
    }

    @Test
    public void testLog10Function() {
        String input = "LOG10(100)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertEquals(2.0, expression.evaluate(), "Log10 function failed");
    }

    @Test
    public void testFunctionsWithIncorrectArgumentCounts() {
        String input = "MIN(1)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertThrows(IllegalArgumentException.class, expression::evaluate, "Function with incorrect number of arguments should throw exception");
    }

    @Test
    public void testFunctionsWithIncorrectArgumentTypes() {
        String input = "SIN(30, 40)";
        Parser parser = new Parser(input, null);

        Expr expression = parser.parse();

        assertThrows(IllegalArgumentException.class, expression::evaluate, "Function with incorrect number of arguments should throw exception");
    }
}
