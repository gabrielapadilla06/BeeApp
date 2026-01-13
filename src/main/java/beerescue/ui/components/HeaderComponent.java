package beerescue.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class HeaderComponent {
    
    /**
     * Creates a header component with customizable title, back button, and color
     * 
     * @param title The title text to display
     * @param backButtonText The text for the back button (e.g., "← Volver", "Volver al menú")
     * @param backgroundColor The background color (e.g., "#ff9800", "#2196f3")
     * @param style The header style (LEFT_BACK or RIGHT_BACK)
     * @param onBackAction The action to execute when back button is clicked
     * @return Configured HBox header
     */
    public static HBox create(String title, String backButtonText, String backgroundColor, Runnable onBackAction) {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: " + backgroundColor + ";");
        
        Button btnVolver = new Button(backButtonText);
        
        // Title on left, back button on right
        Label lblTitulo = new Label(title);
        lblTitulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Derive darker shade for button
        String buttonColor = deriveDarkerColor(backgroundColor);
        btnVolver.setStyle("-fx-background-color: " + buttonColor + "; -fx-text-fill: white; " +
                         "-fx-font-weight: bold;");
        btnVolver.setOnAction(e -> onBackAction.run());
        
        header.getChildren().addAll(lblTitulo, spacer, btnVolver);
        
        return header;
    }
    
    /**
     * Helper method to derive a darker shade of a color for buttons
     */
    private static String deriveDarkerColor(String color) {
        // Common color mappings for darker shades
        return switch (color) {
            case "#ff9800" -> "#f57c00";
            case "#2196f3" -> "#1976d2";
            case "#00bcd4" -> "#0097a7";
            case "#9c27b0" -> "#7b1fa2";
            case "#d32f2f" -> "#b71c1c";
            case "#673ab7" -> "#512da8";
            case "#4caf50" -> "#388e3c";
            default -> color;
        };
    }
}

