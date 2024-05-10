package org.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class UnaryOperatorsTests {

    @Test
    public void testSineFunction() {
        String input = "-3";
        Parser parser = new Parser(input, null);
        Expr expression = parser.parse();
        assertEquals(-3.0, expression.evaluate(), 0.01, "Sine function failed");
    }
}
