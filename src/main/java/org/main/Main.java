package org.main;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Requires 5 arguments: <width> <height> <screenWidth> <screenHeight> <maxHistory>");
            System.exit(1);
        }
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        int screenWidth = Integer.parseInt(args[2]);
        int screenHeight = Integer.parseInt(args[3]);
        int maxHistory = Integer.parseInt(args[4]);
        var tableModel = new TableModel(width, height, screenWidth, screenHeight);
        var tableView = new TableView(tableModel);
        new TableController(tableView, tableModel, maxHistory);
        SwingUtilities.invokeLater(() -> tableView.setVisible(true));
    }
}
