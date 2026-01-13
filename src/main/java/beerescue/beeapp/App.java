package beerescue.beeapp;

import javafx.application.Application;
import javafx.stage.Stage;
import beerescue.ui.LoginView;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BeeRescue Network");
        primaryStage.setResizable(false);

        LoginView loginView = new LoginView(primaryStage);
        loginView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}