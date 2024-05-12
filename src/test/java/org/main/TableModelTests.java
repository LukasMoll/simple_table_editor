package org.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;


public class TableModelTests {
    @Test
    public void testColumnLabelToColumnConversion() {
        TableModel tableModel = new TableModel(5, 3, 0, 0);
        Assertions.assertEquals(1, tableModel.columnLabelToColumn("A"));
        Assertions.assertEquals(2, tableModel.columnLabelToColumn("B"));
    }

    @Test
    public void testRowLabelToRowConversion() {
        TableModel tableModel = new TableModel(5, 3, 0, 0);
        Assertions.assertEquals(0, tableModel.rowLabelToRow("1"));
        Assertions.assertEquals(2, tableModel.rowLabelToRow("3"));
    }

    @Test
    public void testCellRetrievalAndValueUpdate() {
        TableModel tableModel = new TableModel(3, 3, 0, 0);
        tableModel.setCellParsedValue(1, 1, "Test");
        Cell cell = tableModel.getCell(1, 1);
        Assertions.assertEquals("Test", cell.getParsedValue());
    }

    @Test
    public void testLabelGenerationEdgeCases() {
        TableModel smalltableModel = new TableModel(0, 0, 0, 0);
        Assertions.assertArrayEquals(new String[]{""}, smalltableModel.getColumnLabels());
        Assertions.assertArrayEquals(new String[]{}, smalltableModel.getRowLabels());

        TableModel largetableModel = new TableModel(28, 1, 0, 0);
        Assertions.assertTrue(largetableModel.getColumnLabels()[27].equals("AA"));
        Assertions.assertTrue(largetableModel.getColumnLabels()[28].equals("AB"));
    }

    @Test
    public void testtableModel() {
        TableModel tableModel = new TableModel(5, 3, 0, 0);
        Assertions.assertEquals(5, tableModel.getWidth());
        Assertions.assertEquals(3, tableModel.getHeight());
        Assertions.assertArrayEquals(new String[]{"", "A", "B", "C", "D", "E"}, tableModel.getColumnLabels());
        Assertions.assertArrayEquals(new String[]{"1", "2", "3"}, tableModel.getRowLabels());
    }

    @Test
    public void testBigtableModel() {
        TableModel tableModel = new TableModel(100, 100, 0, 0);

        // Validate tableModel dimensions
        Assertions.assertEquals(100, tableModel.getWidth());
        Assertions.assertEquals(100, tableModel.getHeight());

        // Validate x-axis labels are correct for 100 columns
        String[] expectedXAxisLabels = new String[]{
                "", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL",
                "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC",
                "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT",
                "BU", "BV", "BW", "BX", "BY", "BZ", "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK",
                "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV"
        };
        Assertions.assertArrayEquals(expectedXAxisLabels, tableModel.getColumnLabels());

        // Validate y-axis labels are correct for 100 rows
        String[] expectedYAxisLabels = IntStream.rangeClosed(1, 100)
                .mapToObj(Integer::toString)
                .toArray(String[]::new);
        Assertions.assertArrayEquals(expectedYAxisLabels, tableModel.getRowLabels());
    }
}