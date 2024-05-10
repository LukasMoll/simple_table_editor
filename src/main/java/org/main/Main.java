package org.main;


public class Main {
    public static void main(String[] args) {
        TableModel model = new TableModel();
        TableView view = new TableView(model);
        new TableController(model, view);
    }
}
