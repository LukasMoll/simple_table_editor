package org.parser;

import org.main.SimpleTableEditor;

import javax.swing.*;
import java.util.*;

public class Parser {
    private final StringTokenizer tokenizer;
    private String currentToken;
    private int openParentheses = 0; // Track the number of unclosed '('
    private final SimpleTableEditor simpleTableEditor;

    public Parser(String input, SimpleTableEditor simpleTableEditor) {
        tokenizer = new StringTokenizer(input, "+-*/() ,", true);
        nextToken();
        this.simpleTableEditor = simpleTableEditor;
    }

    private void nextToken() {
        while (tokenizer.hasMoreTokens()) {
            currentToken = tokenizer.nextToken().trim();
            if (!currentToken.isEmpty()) {
                return;
            }
        }
        currentToken = null;
    }

    public Expr parse() {
        if (currentToken == null) {
            // Return a default expression or handle the empty input specifically.
            return new NumberExpr(0);  // Example: return 0 for empty input.
        }
        var result = parseExpression();
        if (openParentheses != 0) {
            throw new RuntimeException("Mismatched parentheses");
        }
        if (currentToken != null) {
            throw new RuntimeException("Unexpected token: " + currentToken);
        }
        return result;
    }

    private Expr parseExpression() {
        var result = parseTerm();
        while (("+".equals(currentToken) || "-".equals(currentToken))) {
            var op = currentToken.charAt(0);
            nextToken();
            result = new BinaryExpr(result, op, parseTerm());
        }
        return result;
    }

    private Expr parseTerm() {
        var result = parseFactor();
        while (("*".equals(currentToken) || "/".equals(currentToken))) {
            var op = currentToken.charAt(0);
            nextToken();
            result = new BinaryExpr(result, op, parseFactor());
        }
        return result;
    }

    private Expr parseFactor() {
        if (currentToken == null) {
            throw new RuntimeException("Unexpected end of expression");
        }
        if ("(".equals(currentToken)) {
            openParentheses++;
            nextToken();
            var result = parseExpression();
            if (!")".equals(currentToken)) {
                throw new RuntimeException("Missing ')'");
            }
            openParentheses--;
            nextToken();
            return result;
        } else if ("-".equals(currentToken)) {
            nextToken();
            var negatedExpression = parseFactor();
            return new UnaryExpr('-', negatedExpression);
        } else if (Character.isLetter(currentToken.charAt(0)) && currentToken.length() > 1 && Character.isDigit(currentToken.charAt(1))) {
            var cellRef = currentToken;
            nextToken();
            return new CellExpr(this.simpleTableEditor, cellRef);
        } else if (currentToken.matches("[-+]?\\d*\\.?\\d+")) {
            var number = Double.parseDouble(currentToken);
            nextToken();
            return new NumberExpr(number);
        } else if (Character.isLetter(currentToken.charAt(0))) {
            var func = currentToken;
            nextToken();
            if (!"(".equals(currentToken)) {
                throw new RuntimeException("Missing '(' after function name");
            }
            nextToken();
            var args = new ArrayList<Expr>();
            while (currentToken != null && !")".equals(currentToken)) {
                args.add(parseExpression());
                if (",".equals(currentToken)) nextToken();
            }
            if (!")".equals(currentToken)) {
                throw new RuntimeException("Missing ')'");
            }
            nextToken(); // Skip ')'
            return new FunctionExpr(func, args);
        }
        throw new RuntimeException("Unexpected token: " + currentToken);
    }
}