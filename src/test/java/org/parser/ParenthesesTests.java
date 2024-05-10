package org.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParenthesesTests {

    @Test
    public void testNestedParentheses() {
        String input = "(3 + (4 - 1)) * 2";
        Parser parser = new Parser(input, null);
        Expr expression = parser.parse();
        assertEquals(12.0, expression.evaluate(), "Nested parentheses failed");
    }

    @Test
    public void testMultipleLevelsOfNesting() {
        String input = "((2 + 3) * (3 - 1)) / 2";
        Parser parser = new Parser(input, null);
        Expr expression = parser.parse();
        assertEquals(5.0, expression.evaluate(), "Multiple levels of nesting failed");
    }

    @Test
    public void testExtraParentheses() {
        String input = "(3 + 2))";
        Parser parser = new Parser(input, null);

        assertThrows(RuntimeException.class, parser::parse, "Extra parentheses should throw error");
    }

    @Test
    public void testMissingParentheses() {
        String input = "(3 + 2";
        Parser parser = new Parser(input, null);

        assertThrows(RuntimeException.class, parser::parse, "Missing parentheses should throw error");
    }
}
