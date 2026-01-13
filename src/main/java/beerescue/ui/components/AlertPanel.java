package beerescue.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AlertPanel {
    
    public enum AlertType {
        WARNING("#fff3cd", "#ffc107"),
        INFO("#d1ecf1", "#0c5460"),
        SUCCESS("#d4edda", "#155724"),
        ERROR("#f8d7da", "#721c24");
        
        private final String bgColor;
        private final String borderColor;
        
        AlertType(String bgColor, String borderColor) {
            this.bgColor = bgColor;
            this.borderColor = borderColor;
        }
    }
    
    /**
     * Creates an alert panel with icon, title, and description
     * 
     * @param type The type of alert (WARNING, INFO, SUCCESS, ERROR)
     * @param title The title text
     * @param description The description text
     * @return Configured HBox alert panel
     */
    public static HBox create(AlertType type, String title, String description) {
        HBox panel = new HBox(15);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setStyle(String.format("-fx-background-color: %s; -fx-border-color: %s; -fx-border-width: 2; " +  "-fx-background-radius: 10; -fx-border-radius: 10;", type.bgColor, type.borderColor));
        
        VBox textos = new VBox(5);
        Label lblTitulo = new Label(title);
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        Label lblDescripcion = new Label(description);
        lblDescripcion.setStyle("-fx-font-size: 12px;");
        lblDescripcion.setWrapText(true);
        
        textos.getChildren().addAll(lblTitulo, lblDescripcion);
        panel.getChildren().addAll(textos);
        
        return panel;
    }
    
    /**
     * Creates a simple alert panel with just icon and text
     * 
     * @param type The type of alert
     * @param text The text to display
     * @return Configured HBox alert panel
     */
    public static HBox createSimple(AlertType type, String text) {
        HBox panel = new HBox(15);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setStyle(String.format("-fx-background-color: %s; -fx-border-color: %s; -fx-border-width: 2; " +"-fx-background-radius: 10; -fx-border-radius: 10;", type.bgColor, type.borderColor));
        
        Label texto = new Label(text);
        texto.setWrapText(true);
        texto.setStyle("-fx-font-weight: bold;");
        
        panel.getChildren().addAll(texto);
        return panel;
    }
}

