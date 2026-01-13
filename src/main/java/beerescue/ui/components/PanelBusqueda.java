package beerescue.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PanelBusqueda {
    
    private TextField textField;
    private Button searchButton;
    
    /**
     * Creates a search panel component
     * 
     * @param title The title/label for the search panel (e.g., "Buscar solicitud", "Buscar incidente")
     * @param fieldLabel The label for the input field (e.g., "ID:", "Código:")
     * @param placeholder The placeholder text for the input field
     * @param onSearchAction The action to execute when search button is clicked
     * @return Configured VBox search panel
     */
    public static PanelBusqueda create(String title, String fieldLabel, String placeholder, Runnable onSearchAction) {
        PanelBusqueda panel = new PanelBusqueda();
        
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label lbl = new Label(title);
        lbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        HBox fila = new HBox(10);
        fila.setAlignment(Pos.CENTER_LEFT);
        
        panel.textField = new TextField();
        panel.textField.setPromptText(placeholder);
        panel.textField.setPrefWidth(180);
        
        panel.searchButton = new Button("Buscar");
        panel.searchButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-font-size: 14px;");
        panel.searchButton.setOnAction(e -> onSearchAction.run());
        
        fila.getChildren().addAll(new Label(fieldLabel), panel.textField, panel.searchButton);
        card.getChildren().addAll(lbl, fila);
        
        panel.container = card;
        return panel;
    }
    
    private VBox container;
    
    /**
     * Gets the container VBox
     */
    public VBox getContainer() {
        return container;
    }
    
    /**
     * Gets the text field for accessing the input value
     */
    public TextField getTextField() {
        return textField;
    }
    
    /**
     * Gets the search button
     */
    public Button getSearchButton() {
        return searchButton;
    }
    
    /**
     * Gets the text value from the input field
     */
    public String getText() {
        return textField.getText();
    }
    
    /**
     * Clears the input field
     */
    public void clear() {
        textField.clear();
    }
}
