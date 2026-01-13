package beerescue.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ButtonGroup {
    
    public enum ButtonAlignment {
        CENTER,
        LEFT,
        RIGHT
    }
    
    /**
     * Creates a button group with primary and secondary buttons
     * 
     * @param primaryText The text for the primary button
     * @param primaryColor The color for the primary button (e.g., "#4caf50", "#2196f3")
     * @param onPrimaryAction The action for the primary button
     * @param secondaryText The text for the secondary button
     * @param secondaryColor The color for the secondary button (e.g., "#757575", "#9e9e9e")
     * @param onSecondaryAction The action for the secondary button
     * @param alignment The alignment of the buttons
     * @return Configured HBox button group
     */
    public static HBox create(String primaryText, String primaryColor, Runnable onPrimaryAction, String secondaryText, String secondaryColor, Runnable onSecondaryAction, ButtonAlignment alignment) {
        Button btnPrimary = new Button(primaryText);
        btnPrimary.setStyle("-fx-background-color: " + primaryColor + "; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-padding: 12 30;");
        btnPrimary.setOnAction(e -> onPrimaryAction.run());
        
        Button btnSecondary = new Button(secondaryText);
        btnSecondary.setStyle("-fx-background-color: " + secondaryColor + "; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-padding: 12 30;");
        btnSecondary.setOnAction(e -> onSecondaryAction.run());
        
        HBox box = new HBox(15, btnPrimary, btnSecondary);
        
        switch (alignment) {
            case CENTER -> box.setAlignment(Pos.CENTER);
            case LEFT -> box.setAlignment(Pos.CENTER_LEFT);
            case RIGHT -> box.setAlignment(Pos.CENTER_RIGHT);
        }
        
        return box;
    }
    
    /**
     * Creates a button group with custom buttons
     * 
     * @param alignment The alignment of the buttons
     * @param buttons The buttons to add
     * @return Configured HBox button group
     */
    public static HBox create(ButtonAlignment alignment, Button... buttons) {
        HBox box = new HBox(15);
        box.getChildren().addAll(buttons);
        
        switch (alignment) {
            case CENTER -> box.setAlignment(Pos.CENTER);
            case LEFT -> box.setAlignment(Pos.CENTER_LEFT);
            case RIGHT -> box.setAlignment(Pos.CENTER_RIGHT);
        }
        
        return box;
    }
}

