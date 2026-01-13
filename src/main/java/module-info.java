module beerescue.beeapp {
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires transitive java.sql;

    opens beerescue.beeapp to javafx.fxml;
    opens beerescue.ui to javafx.fxml;
    opens beerescue.ui.agricultor to javafx.fxml;
    opens beerescue.ui.ong to javafx.fxml;
    opens beerescue.ui.components to javafx.fxml;
    opens beerescue.usuarios to javafx.fxml;
    opens beerescue.estaciones to javafx.fxml;
    opens beerescue.reportes to javafx.fxml;

    exports beerescue.beeapp;
    exports beerescue.ui;
    exports beerescue.ui.agricultor;
    exports beerescue.ui.ong;
    exports beerescue.ui.components;
    exports beerescue.usuarios;
    exports beerescue.estaciones;
    exports beerescue.reportes;
    exports beerescue.database;
}