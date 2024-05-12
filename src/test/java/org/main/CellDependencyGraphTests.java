package org.main;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;


public class CellDependencyGraphTests {
    private CellDependencyGraph graph;
    private Cell cellA;
    private Cell cellB;
    private Cell cellC;
    private Cell cellD;

    @Before
    public void setUp() {
        graph = new CellDependencyGraph();
        cellA = new Cell(0, 1, "", "", graph, null, null);
        cellB = new Cell(0, 2, "", "", graph, null, null);
        cellC = new Cell(0, 3, "", "", graph, null, null);
        cellD = new Cell(0, 4, "", "", graph, null, null);
    }

    @Test
    public void testAddDependencyWithoutCycle() {
        assertTrue(graph.addDependency(cellA, cellB));
        assertTrue(graph.getAllDependents(cellA).contains(cellB));
    }

    @Test
    public void testAddDependencyWithCycle() {
        assertTrue(graph.addDependency(cellA, cellB));
        assertTrue(graph.addDependency(cellB, cellC));
        assertFalse(graph.addDependency(cellC, cellA)); // This should create a cycle
    }

    @Test
    public void testGetAllDependents() {
        graph.addDependency(cellA, cellB);
        graph.addDependency(cellA, cellC);
        graph.addDependency(cellB, cellD);
        List<Cell> dependents = graph.getAllDependents(cellA);
        assertTrue(dependents.containsAll(Arrays.asList(cellB, cellC, cellD)));
        assertEquals(3, dependents.size());
    }

    @Test
    public void testRemoveDependent() {
        graph.addDependency(cellA, cellB);
        graph.addDependency(cellA, cellC);
        graph.removeDependent(cellB);
        assertFalse(graph.getAllDependents(cellA).contains(cellB));
        assertTrue(graph.getAllDependents(cellA).contains(cellC));
    }

    @Test
    public void testMultipleOperations() {
        graph.addDependency(cellA, cellB);
        graph.addDependency(cellB, cellC);
        graph.addDependency(cellC, cellD);
        graph.removeDependent(cellC);
        assertFalse(graph.getAllDependents(cellB).contains(cellC));
        assertTrue(graph.getAllDependents(cellB).isEmpty());
    }
}



