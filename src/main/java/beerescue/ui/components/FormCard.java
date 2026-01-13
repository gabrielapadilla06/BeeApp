package beerescue.ui.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class FormCard {
    
    /**
     * Creates a styled form card container
     * 
     * @param children The nodes to add to the card
     * @return Configured VBox card
     */
    public static VBox create(Node... children) {
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.getChildren().addAll(children);
        return card;
    }
    
    /**
     * Creates a styled form card container with custom spacing
     * 
     * @param spacing The spacing between children
     * @param children The nodes to add to the card
     * @return Configured VBox card
     */
    public static VBox create(double spacing, Node... children) {
        VBox card = new VBox(spacing);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        card.getChildren().addAll(children);
        return card;
    }
}

