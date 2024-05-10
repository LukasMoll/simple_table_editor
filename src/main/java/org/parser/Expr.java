package org.parser;

import org.main.Cell;
import org.main.Frame;
import org.main.SimpleTableEditor;

import java.util.List;
import java.util.Map;

public interface Expr {
    double evaluate();
}

class NumberExpr implements Expr {
    private final double value;

    public NumberExpr(double value) {
        this.value = value;
    }

    @Override
    public double evaluate() {
        return value;
    }
}

class BinaryExpr implements Expr {
    private final Expr left, right;
    private final char operator;

    public BinaryExpr(Expr left, char operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public double evaluate() {
        return switch (operator) {
            case '+' -> left.evaluate() + right.evaluate();
            case '-' -> left.evaluate() - right.evaluate();
            case '*' -> left.evaluate() * right.evaluate();
            case '/' -> left.evaluate() / right.evaluate();
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };
    }
}

class FunctionExpr implements Expr {
    private final String functionName;
    private final List<Expr> arguments;

    public FunctionExpr(String functionName, List<Expr> arguments) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public double evaluate() {
        return switch (functionName.toUpperCase()) {
            case "POW" -> {
                if (arguments.size() != 2) {
                    throw new IllegalArgumentException("POW function takes two arguments");
                }
                yield Math.pow(arguments.get(0).evaluate(), arguments.get(1).evaluate());
            }
            case "SIN" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("SIN function takes one argument");
                }
                yield Math.sin(Math.toRadians(arguments.get(0).evaluate()));
            }
            case "COS" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("COS function takes one argument");
                }
                yield Math.cos(Math.toRadians(arguments.get(0).evaluate()));
            }
            case "TAN" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("TAN function takes one argument");
                }
                yield Math.tan(Math.toRadians(arguments.get(0).evaluate()));
            }
            case "SQRT" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("SQRT function takes one argument");
                }
                yield Math.sqrt(arguments.get(0).evaluate());
            }
            case "LOG" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("LOG function takes one argument");
                }
                yield Math.log(arguments.get(0).evaluate());
            }
            case "EXP" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("EXP function takes one argument");
                }
                yield Math.exp(arguments.get(0).evaluate());
            }
            case "ABS" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("ABS function takes one argument");
                }
                yield Math.abs(arguments.get(0).evaluate());
            }
            case "ROUND" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("ROUND function takes one argument");
                }
                yield Math.round(arguments.get(0).evaluate());
            }
            case "CEIL" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("CEIL function takes one argument");
                }
                yield Math.ceil(arguments.get(0).evaluate());
            }
            case "FLOOR" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("FLOOR function takes one argument");
                }
                yield Math.floor(arguments.get(0).evaluate());
            }
            case "MIN" -> {
                if (arguments.size() < 2) {
                    throw new IllegalArgumentException("MIN function takes at least two arguments");
                }
                yield arguments.stream().mapToDouble(Expr::evaluate).min().orElseThrow();
            }
            case "MAX" -> {
                if (arguments.size() < 2) {
                    throw new IllegalArgumentException("MAX function takes at least two arguments");
                }
                yield arguments.stream().mapToDouble(Expr::evaluate).max().orElseThrow();
            }
            case "SUM" -> arguments.stream().mapToDouble(Expr::evaluate).sum();
            case "AVERAGE" -> {
                if (arguments.isEmpty()) {
                    throw new IllegalArgumentException("AVERAGE function takes at least one argument");
                }
                yield arguments.stream().mapToDouble(Expr::evaluate).average().orElseThrow();
            }
            case "MOD" -> {
                if (arguments.size() != 2) {
                    throw new IllegalArgumentException("MOD function takes two arguments");
                }
                yield arguments.get(0).evaluate() % arguments.get(1).evaluate();
            }
            case "LOG10" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("LOG10 function takes one argument");
                }
                yield Math.log10(arguments.get(0).evaluate());
            }
            case "DEGREES" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("DEGREES function takes one argument");
                }
                yield Math.toDegrees(arguments.get(0).evaluate());
            }
            case "RADIANS" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("RADIANS function takes one argument");
                }
                yield Math.toRadians(arguments.get(0).evaluate());
            }
            case "SIGNUM" -> {
                if (arguments.size() != 1) {
                    throw new IllegalArgumentException("SIGNUM function takes one argument");
                }
                yield Math.signum(arguments.get(0).evaluate());
            }
            default -> throw new IllegalArgumentException("Unsupported function: " + functionName);
        };
    }
}

class CellExpr implements Expr {
    private final SimpleTableEditor simpleTableEditor;
    private final String cellRef;

    public CellExpr(SimpleTableEditor simpleTableEditor, String cellRef) {
        this.simpleTableEditor = simpleTableEditor;
        this.cellRef = cellRef;
    }

    @Override
    public double evaluate() {
        Frame frame = simpleTableEditor.getFrame();

        // Decode column index from Excel-like reference
        String rowLabel = "";
        String columnLabel = "";
        boolean isColumnPart = true;
        for (Character c : cellRef.toCharArray()) {
            if (Character.isLetter(c) && isColumnPart) {
                columnLabel += c;
            } else if (Character.isDigit(c)) {
                rowLabel += c;
                isColumnPart = false;
            } else {
                throw new IllegalArgumentException("Invalid cell reference: " + cellRef);
            }
        }
        if (rowLabel.isEmpty() || columnLabel.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell reference: " + cellRef);
        }
        int column = frame.getColumnLabelToColumn().get(columnLabel); // Extract and decode column letters
        int row = Integer.parseInt(rowLabel) - 1; // Extract and decode row number

        // Fetch the cell value from the JTable within the SimpleTableEditor
        Object cellValue = simpleTableEditor.getTable().getValueAt(row, column);
        if (cellValue == null) {
            throw new IllegalArgumentException("Empty cell value at " + cellRef);
        }
        try {
            var result = Double.parseDouble(cellValue.toString());
            boolean noCycle = simpleTableEditor.getDependencyGraph().addDependency(new Cell(row, column), simpleTableEditor.getCurrentCell());
            if (!noCycle) {
                throw new IllegalArgumentException("Cycle detected in cell dependencies");
            }
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Non-numeric cell value at " + cellRef);
        }
    }
}



class UnaryExpr implements Expr {
    private final Expr expr;
    private final char operator;

    public UnaryExpr(char operator, Expr expr) {
        this.operator = operator;
        this.expr = expr;
    }

    @Override
    public double evaluate() {
        if (operator == '-') {
            return -expr.evaluate(); // Negation
        }
        throw new IllegalArgumentException("Unsupported unary operator: " + operator);
    }
}
