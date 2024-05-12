package org.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.swing.SwingUtilities;


class CellTests {
    private Cell cell;

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
        cell = tableModel.getCell(2, 2);
    }

    @Test
    public void testValueSetAndRetrieve() {
        // Set a value and verify if it's correctly retrieved
        cell.setValue("100");
        Assertions.assertEquals("100", cell.getValue(), "Cell should store and return the same value.");
    }

    @Test
    public void testParsingUpdatesParsedValue() {
        // Simulate parsing outcome
        String expectedResult = "200.0";
        cell.setValue("100 * 2");  // Assuming the parser will correctly parse this to 200
        cell.setParsedValue(expectedResult);  // Mocking the outcome of parsing
        cell.parse();  // Act of parsing which should ideally set the parsed value
        Assertions.assertEquals(expectedResult, cell.getParsedValue(), "Parsed value should be updated after parsing.");
    }

    @Test
    public void testParseWithErrorHandling() {
        try {
            cell.setValue("bad input");
            cell.parse();
            Assertions.assertEquals("bad input", cell.getParsedValue(), "On parse error, parsed value should revert to original value.");
        } catch (Exception e) {
            Assertions.fail("Cell parsing should handle errors gracefully.");
        }
    }
}
