package org.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class BinaryOperatorsTests {

    @Test
    public void testSimpleAddition() {
        String input = "3 + 4";
        Parser parser = new Parser(input, null);
        Expr expression = parser.parse();
        assertEquals(7.0, expression.evaluate(), "Simple addition failed");
    }

    @Test
    public void testComplexExpression() {
        String input = "3 + 4 * 2";
        Parser parser = new Parser(input, null);
        Expr expression = parser.parse();
        assertEquals(11.0, expression.evaluate(), "Expression with priority failed");
    }

    @Test
    public void testDivisionAndSubtraction() {
        String input = "8 - 2 / 2";
        Parser parser = new Parser(input, null);
        Expr expression = parser.parse();
        assertEquals(7.0, expression.evaluate(), "Complex operation with division and subtraction failed");
    }

    @Test
    public void testMissingOperator() {
        String input = "3 3";
        Parser parser = new Parser(input, null);

        assertThrows(RuntimeException.class, parser::parse, "Missing operator should throw error");
    }
}