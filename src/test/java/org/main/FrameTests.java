package org.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class FrameTests {
    @Test
    public void testFrame() {
        Frame frame = new Frame(5, 3);
        Assertions.assertEquals(5, frame.getWidth());
        Assertions.assertEquals(3, frame.getHeight());
        Assertions.assertEquals(List.of("A", "B", "C", "D", "E"), frame.getColumnLabels());
        Assertions.assertEquals(List.of("1", "2", "3"), frame.getRowLabels());
    }

    @Test
    public void testBigFrame() {
        Frame frame = new Frame(100, 100);

        // Validate frame dimensions
        Assertions.assertEquals(100, frame.getWidth());
        Assertions.assertEquals(100, frame.getHeight());

        // Validate x-axis labels are correct for 100 columns
        List<String> expectedXAxisLabels = List.of(
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL",
                "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC",
                "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT",
                "BU", "BV", "BW", "BX", "BY", "BZ", "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK",
                "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV"
        );
        Assertions.assertEquals(expectedXAxisLabels, frame.getColumnLabels());

        // Validate y-axis labels are correct for 100 rows
        List<String> expectedYAxisLabels = IntStream.rangeClosed(1, 100)
                .mapToObj(Integer::toString)
                .collect(Collectors.toList());
        Assertions.assertEquals(expectedYAxisLabels, frame.getRowLabels());
    }
}
