package beerescue.ui.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class InfoPanel {
    
    private GridPane grid;
    private VBox container;
    
    /**
     * Creates an info panel with a grid layout for label-value pairs
     * 
     * @param title The title of the info panel
     * @param titleColor Optional color for the title (null for default)
     * @return InfoPanel instance
     */
    public static InfoPanel create(String title, String titleColor) {
        InfoPanel panel = new InfoPanel();
        
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 0);");
        
        Label lbl = new Label(title);
        lbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        if (titleColor != null) {
            lbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + titleColor + ";");
        }
        
        panel.grid = new GridPane();
        panel.grid.setHgap(15);
        panel.grid.setVgap(12);
        
        card.getChildren().addAll(lbl, panel.grid);
        panel.container = card;
        
        return panel;
    }
    
    /**
     * Adds a row to the info panel
     * 
     * @param rowIndex The row index (0-based)
     * @param labelText The label text
     * @return The Label that will display the value
     */
    public Label addRow(int rowIndex, String labelText) {
        Label lblTitulo = new Label(labelText);
        lblTitulo.setStyle("-fx-font-weight: bold;");
        
        Label lblValor = new Label("-");
        lblValor.setStyle("-fx-font-size: 14px;");
        
        grid.add(lblTitulo, 0, rowIndex);
        grid.add(lblValor, 1, rowIndex);
        
        return lblValor;
    }
    
    /**
     * Adds a row with a custom value node
     * 
     * @param rowIndex The row index (0-based)
     * @param labelText The label text
     * @param valueNode The node to display as the value
     */
    public void addRow(int rowIndex, String labelText, Node valueNode) {
        Label lblTitulo = new Label(labelText);
        lblTitulo.setStyle("-fx-font-weight: bold;");
        
        grid.add(lblTitulo, 0, rowIndex);
        grid.add(valueNode, 1, rowIndex);
    }
    
    /**
     * Adds a separator to the container
     */
    public void addSeparator() {
        container.getChildren().add(new Separator());
    }
    
    /**
     * Adds a node to the container
     */
    public void addNode(Node node) {
        container.getChildren().add(node);
    }
    
    /**
     * Gets the container VBox
     */
    public VBox getContainer() {
        return container;
    }
    
    /**
     * Gets the grid pane
     */
    public GridPane getGrid() {
        return grid;
    }
}

