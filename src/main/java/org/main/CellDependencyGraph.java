package org.main;

import java.util.*;

public class CellDependencyGraph {
    private final Map<Cell, Set<Cell>> cellDependencyGraph = new HashMap<>();
    private final Map<Cell, Set<Cell>> inverseCellDependencyGraph = new HashMap<>();

    public CellDependencyGraph() {}

    public CellDependencyGraph(CellDependencyGraph another) {
        for (Map.Entry<Cell, Set<Cell>> entry : another.cellDependencyGraph.entrySet()) {
            cellDependencyGraph.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        for (Map.Entry<Cell, Set<Cell>> entry : another.inverseCellDependencyGraph.entrySet()) {
            inverseCellDependencyGraph.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
    }

    public boolean addDependency(Cell independent, Cell dependent) {

        if (createsCycle(dependent, independent)) {
            return false; // Dependency would create a cycle, so it is not added
        }

        cellDependencyGraph.computeIfAbsent(independent, k -> new HashSet<>()).add(dependent);
        inverseCellDependencyGraph.computeIfAbsent(dependent, k -> new HashSet<>()).add(independent);

        return true; // Dependency added successfully
    }

    private boolean createsCycle(Cell current, Cell target) {
        if (current.equals(target)) {
            return true; // Cycle detected
        }
        Set<Cell> nextCells = cellDependencyGraph.get(current);
        if (nextCells != null) {
            for (Cell next : nextCells) {
                if (createsCycle(next, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Cell> getAllDependents(Cell cell) {
        List<Cell> result = new ArrayList<>();
        Set<Cell> dependents = cellDependencyGraph.getOrDefault(cell, new HashSet<>());
        for (Cell dependent : dependents) {
            getAllDependentsDFS(dependent, result);
        }
        Collections.reverse(result); // Reverse to get correct topological order
        return result;
    }

    private void getAllDependentsDFS(Cell cell, List<Cell> result) {
        Set<Cell> dependents = cellDependencyGraph.getOrDefault(cell, new HashSet<>());
        for (Cell dependent : dependents) {
            getAllDependentsDFS(dependent, result);
        }
        result.add(cell); // Postorder: This cell depends on all recursively visited cells
    }

    public void removeDependent(Cell cell) {
        // Remove this cell from the dependencies of other cells using the inverse mapping
        if (inverseCellDependencyGraph.containsKey(cell)) {
            for (Cell independent : inverseCellDependencyGraph.get(cell)) {
                cellDependencyGraph.get(independent).remove(cell);
            }
            inverseCellDependencyGraph.remove(cell);
        }
    }
}
