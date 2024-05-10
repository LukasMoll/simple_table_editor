package org.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;

public class SimpleTableEditorHistoryTests {
    private SimpleTableEditor editor;
    private Robot robot;
    private String osName;

    @Before
    public void setUp() throws Exception {
        // Setup the frame and editor
        org.main.Frame frame = new Frame(5, 5);
        editor = new SimpleTableEditor(frame);
        SwingUtilities.invokeLater(() -> editor.setVisible(true));
        osName = System.getProperty("os.name").toLowerCase();

        // Initialize Robot
        try {
            robot = new Robot();
            // Allow some time for the UI to settle
            robot.delay(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        // Close the frame
        SwingUtilities.invokeLater(() -> editor.dispose());
    }

    private static void pressAndRelease(Robot robot, int modifierKey, int key) {
        robot.keyPress(modifierKey);    // Press the modifier key (Command or Control)
        robot.keyPress(key);           // Press the Z key
        robot.delay(100);              // Optional: wait a bit for the action to be processed
        robot.keyRelease(key);         // Release the Z key
        robot.keyRelease(modifierKey); // Release the modifier key
    }

    @Test
    public void testUndoWithKeyPress() throws Exception {
        // Ensure the table is focused for key events to be received
        JTable table = editor.getTable();
        SwingUtilities.invokeLater(() -> table.requestFocus());

        // Simulate a change
        SwingUtilities.invokeAndWait(() -> {
            table.getModel().setValueAt("5", 0, 1);
        });

        String osName = System.getProperty("os.name").toLowerCase();
        pressAndRelease(robot, osName.contains("mac") ? KeyEvent.VK_META : KeyEvent.VK_CONTROL, KeyEvent.VK_Z);

        SwingUtilities.invokeAndWait(() -> {
            Object value = table.getModel().getValueAt(0, 1);
            assertEquals("", value.toString());  // Expect the cell to be empty or reverted to its original state
        });
    }

    @Test
    public void testMultipleUndoOperations() throws Exception {
        JTable table = editor.getTable();
        SwingUtilities.invokeAndWait(() -> {
            table.getModel().setValueAt("5", 0, 1);
            table.getModel().setValueAt("10", 0, 1);
            table.getModel().setValueAt("15", 0, 1);
        });

        int undoKey = osName.contains("mac") ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;
        for (int i = 0; i < 3; i++) {
            pressAndRelease(robot, undoKey, KeyEvent.VK_Z);
        }

        SwingUtilities.invokeAndWait(() -> {
            Object value = table.getModel().getValueAt(0, 1);
            assertEquals("", value.toString());  // Assuming the initial value was empty
        });
    }

    @Test
    public void testUndoWithoutChanges() throws Exception {
        JTable table = editor.getTable();
        SwingUtilities.invokeAndWait(() -> {
            table.getModel().setValueAt("5", 0, 1);
            Object initialValue = table.getModel().getValueAt(0, 1);
            pressAndRelease(robot, osName.contains("mac") ? KeyEvent.VK_META : KeyEvent.VK_CONTROL, KeyEvent.VK_Z);
            pressAndRelease(robot, osName.contains("mac") ? KeyEvent.VK_META : KeyEvent.VK_CONTROL, KeyEvent.VK_Z);
            pressAndRelease(robot, osName.contains("mac") ? KeyEvent.VK_META : KeyEvent.VK_CONTROL, KeyEvent.VK_Z);
            Object valueAfterUndo = table.getModel().getValueAt(0, 1);
            assertEquals(initialValue, valueAfterUndo);
        });
    }

    @Test
    public void testUndoWithDifferentCells() throws Exception {
        JTable table = editor.getTable();
        SwingUtilities.invokeAndWait(() -> {
            table.getModel().setValueAt("A", 0, 0);
            table.getModel().setValueAt("B", 1, 1);
        });

        // Undo last change
        pressAndRelease(robot, osName.contains("mac") ? KeyEvent.VK_META : KeyEvent.VK_CONTROL, KeyEvent.VK_Z);

        // Check first cell change is still intact
        SwingUtilities.invokeAndWait(() -> {
            assertEquals("A", table.getModel().getValueAt(0, 0).toString());
            assertEquals("", table.getModel().getValueAt(1, 1).toString()); // Assuming initial was empty
        });
    }

    @Test
    public void testUndoAfterProgrammaticChanges() throws Exception {
        JTable table = editor.getTable();
        SwingUtilities.invokeAndWait(() -> {
            table.getModel().setValueAt("X", 2, 2);
        });

        // Undo
        pressAndRelease(robot, osName.contains("mac") ? KeyEvent.VK_META : KeyEvent.VK_CONTROL, KeyEvent.VK_Z);

        SwingUtilities.invokeAndWait(() -> {
            Object value = table.getModel().getValueAt(2, 2);
            assertEquals("", value.toString());  // Assuming initial value was empty
        });
    }
}
