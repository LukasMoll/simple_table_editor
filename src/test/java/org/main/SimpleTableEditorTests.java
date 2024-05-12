package org.main;

import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.JTableFixture;
import org.junit.Test;

import static org.assertj.swing.data.TableCell.row;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.awt.event.KeyEvent;


public class SimpleTableEditorTests extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        GuiActionRunner.execute(() -> {
            TableModel tableModel = new TableModel(10, 10, 10, 10);
            TableView tableView = new TableView(tableModel);
            new TableController(tableView, tableModel, 1);
            tableView.setVisible(true);
            window = new FrameFixture(robot(), tableView);
            return tableView;
        });
        window.show(); // Show the frame to test
    }

    @Test
    public void testCellEditing() {
        JTableFixture table = window.table();

        table.cell(row(0).column(1)).enterValue("123");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("123.0", table.target().getValueAt(0, 1));
    }

    @Test
    public void testCellEditingFormula() {
        JTableFixture table = window.table();

        table.cell(row(0).column(1)).enterValue("2");
        table.cell(row(1).column(1)).enterValue("3");
        table.cell(row(2).column(1)).enterValue("A1+A2");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("5.0", table.target().getValueAt(2, 1));
    }

    @Test
    public void testSelectionBehavior() {
        JTableFixture table = window.table();

        table.cell(row(0).column(1)).enterValue("123");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("123.0", table.target().getValueAt(0, 1));
        table.cell(row(0).column(1)).click();
        assertEquals("123", table.target().getValueAt(0, 1));
        table.cell(row(1).column(2)).click();
        assertEquals("123.0", table.target().getValueAt(0, 1));
    }

    @Test
    public void testFirstColumnNotEditable() {
        JTableFixture table = window.table();

        // Assert that attempting to enter a value in a non-editable cell throws an exception
        assertThrows(IllegalStateException.class, () -> table.cell(row(0).column(0)).enterValue("123"));
    }

    @Test
    public void testComplexMultistep() {
        JTableFixture table = window.table();

        // Step 1: Enter individual numbers
        table.cell(row(0).column(1)).enterValue("10");
        table.cell(row(1).column(1)).enterValue("20");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);  // Commit the entries

        // Step 2: Enter a formula in the third row to sum the first two rows
        table.cell(row(2).column(1)).enterValue("A1+A2");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        // Step 3: Validate the sum calculated by the formula
        assertEquals("30.0", table.target().getValueAt(2, 1).toString());

        // Step 4: Modify the first entry and verify the updated sum
        table.cell(row(0).column(1)).enterValue("15");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        // Step 5: Verify the sum is updated correctly
        assertEquals("35.0", table.target().getValueAt(2, 1).toString());
    }

    @Test
    public void testUpperLeftCorner() {
        JTableFixture table = window.table();

        table.cell(row(0).column(1)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1", table.target().getValueAt(0, 0));
    }

    @Test
    public void testUpperRightCorner() {
        JTableFixture table = window.table();

        table.cell(row(0).column(5)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(0, 5));
    }

    @Test
    public void testLowerLeftCorner() {
        JTableFixture table = window.table();

        table.cell(row(2).column(1)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(2, 1));
    }

    @Test
    public void testLowerRightCorner() {
        JTableFixture table = window.table();

        table.cell(row(2).column(5)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(2, 5));
    }

    @Test
    public void testUpperLeftReference() {
        JTableFixture table = window.table();

        table.cell(row(0).column(1)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(1).column(1)).enterValue("A1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(1, 1));
    }

    @Test
    public void testUpperRightReference() {
        JTableFixture table = window.table();

        table.cell(row(0).column(5)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(1).column(5)).enterValue("E1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(1, 5));
    }

    @Test
    public void testLowerLeftReference() {
        JTableFixture table = window.table();

        table.cell(row(2).column(1)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(0).column(1)).enterValue("A3");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(0, 1));
    }

    @Test
    public void testLowerRightReference() {
        JTableFixture table = window.table();

        table.cell(row(2).column(5)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(0).column(5)).enterValue("E3");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(0, 5));
    }

    @Test
    public void testNestedReferenceRemoval() {
        JTableFixture table = window.table();

        table.cell(row(0).column(1)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(1).column(1)).enterValue("A1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(2).column(1)).enterValue("A2");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(1, 1));
        assertEquals("1.0", table.target().getValueAt(2, 1));

        table.cell(row(0).column(1)).enterValue("");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(0).column(2)).enterValue("test");

        assertEquals("", table.target().getValueAt(0, 1));
        assertEquals("A1", table.target().getValueAt(1, 1));
        assertEquals("A2", table.target().getValueAt(2, 1));
    }

    @Test
    public void testNestedReferenceEdit() {
        JTableFixture table = window.table();

        table.cell(row(0).column(1)).enterValue("1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(0).column(2)).enterValue("2");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(0).column(3)).enterValue("A1+B1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);
        table.cell(row(0).column(4)).enterValue("A1+B1+C1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(0, 1));
        assertEquals("2.0", table.target().getValueAt(0, 2));
        assertEquals("3.0", table.target().getValueAt(0, 3));
        assertEquals("6.0", table.target().getValueAt(0, 4));

        table.cell(row(0).column(3)).enterValue("A1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        assertEquals("1.0", table.target().getValueAt(0, 1));
        assertEquals("2.0", table.target().getValueAt(0, 2));
        assertEquals("1.0", table.target().getValueAt(0, 3));
        assertEquals("4.0", table.target().getValueAt(0, 4));
    }

    @Test
    public void testCircularReference() {
        JTableFixture table = window.table();

        // Setup initial values and references
        table.cell(row(0).column(1)).enterValue("10");
        table.cell(row(1).column(1)).enterValue("A1");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);  // Commit the entries

        // Attempt to create a circular reference
        table.cell(row(0).column(1)).enterValue("A2");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        // Check for error message or indication of failure
        assertEquals("A2", table.target().getValueAt(0, 1).toString());
        assertEquals("A1", table.target().getValueAt(1, 1).toString());
    }

    @Test
    public void testMultiLevelDependencies() {
        JTableFixture table = window.table();

        table.cell(row(0).column(1)).enterValue("5");
        table.cell(row(1).column(1)).enterValue("A1 * 2");
        table.cell(row(2).column(1)).enterValue("A2 + 10");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);  // Commit the entries

        assertEquals("10.0", table.target().getValueAt(1, 1).toString());
        assertEquals("20.0", table.target().getValueAt(2, 1).toString());

        // Update the base value
        table.cell(row(0).column(1)).enterValue("6");
        table.cell(row(0).column(2)).select();

        // Check if the dependent values updated correctly
        assertEquals("12.0", table.target().getValueAt(1, 1).toString());
        assertEquals("22.0", table.target().getValueAt(2, 1).toString());
    }

    @Test
    public void testUndoOnMac() {
        System.setProperty("os.name", "Mac OS X");  // Simulate macOS environment

        JTableFixture table = window.table();

        // Initial value entry
        table.cell(row(0).column(1)).enterValue("50");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        // Perform undo operation
        robot().pressKey(KeyEvent.VK_META);  // Command key
        robot().pressAndReleaseKey(KeyEvent.VK_Z);
        robot().releaseKey(KeyEvent.VK_META);

        // Check if the undo operation reverted the value
        assertEquals("", table.target().getValueAt(0, 1).toString());
    }

    // Test the Undo operation on Windows (Ctrl + Z)
    @Test
    public void testUndoOnWindows() {
        System.setProperty("os.name", "Windows 10");  // Simulate Windows environment

        JTableFixture table = window.table();

        // Initial value entry
        table.cell(row(0).column(1)).enterValue("50");
        robot().pressAndReleaseKeys(KeyEvent.VK_ENTER);

        // Perform undo operation
        robot().pressKey(KeyEvent.VK_CONTROL);  // Control key
        robot().pressAndReleaseKey(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);
        robot().releaseKey(KeyEvent.VK_CONTROL);

        // Check if the undo operation reverted the value
        assertEquals("", table.target().getValueAt(0, 1).toString());
    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
    }
}
