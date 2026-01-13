package beerescue.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import beerescue.usuarios.InicioSesion;
import beerescue.usuarios.Usuario;
import beerescue.usuarios.RolUsuario;

public class LoginView {
    private Stage stage;
    private InicioSesion inicioSesion;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.inicioSesion = new InicioSesion();
    }

    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        // Título
        Label titulo = new Label("BeeRescue Network");
        titulo.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #ff6f00;");

        Label subtitulo = new Label("Sistema de Gestión de Estaciones");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #424242;");

        // Panel del formulario
        VBox formPanel = new VBox(15);
        formPanel.setAlignment(Pos.CENTER);
        formPanel.setPadding(new Insets(30));
        formPanel.setMaxWidth(400);
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 15; " + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);");

        // Usuario
        Label lblUsuario = new Label("Usuario:");
        lblUsuario.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Ingrese su usuario");
        txtUsuario.setPrefHeight(40);
        txtUsuario.setStyle("-fx-font-size: 14px; -fx-background-radius: 5;");

        // Contraseña
        Label lblPassword = new Label("Contraseña:");
        lblPassword.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Ingrese su contraseña");
        txtPassword.setPrefHeight(40);
        txtPassword.setStyle("-fx-font-size: 14px; -fx-background-radius: 5;");

        // Mensaje de error
        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 13px;");
        lblMensaje.setWrapText(true);
        lblMensaje.setMaxWidth(350);

        // Botones
        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.setPrefWidth(350);
        btnLogin.setPrefHeight(45);
        btnLogin.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;");

        Button btnRegistro = new Button("Registrar Nuevo Usuario");
        btnRegistro.setPrefWidth(350);
        btnRegistro.setPrefHeight(40);
        btnRegistro.setStyle("-fx-background-color: transparent; -fx-text-fill: #1976d2; " +
                "-fx-font-size: 14px; -fx-underline: true;");

        // Efectos hover
        btnLogin.setOnMouseEntered(e ->
                btnLogin.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        btnLogin.setOnMouseExited(e -> btnLogin.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        // Evento de login
        btnLogin.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String password = txtPassword.getText().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                lblMensaje.setText("Por favor complete todos los campos");
                return;
            }

            Usuario user = inicioSesion.loginGUI(usuario, password);

            if (user != null) {
                if (user.getRol() == RolUsuario.ONG) {
                    MenuONGView menuONG = new MenuONGView(stage, user);
                    menuONG.show();
                } else {
                    MenuAgricultorView menuAgricultor = new MenuAgricultorView(stage, user);
                    menuAgricultor.show();
                }
            } else {
                lblMensaje.setText("Usuario o contraseña incorrectos");
                txtPassword.clear();
            }
        });

        // Evento de registro
        btnRegistro.setOnAction(e -> {
            RegistroView registroView = new RegistroView(stage, this);
            registroView.show();
        });

        // Enter para login
        txtPassword.setOnAction(e -> btnLogin.fire());

        formPanel.getChildren().addAll(
                lblUsuario, txtUsuario,
                lblPassword, txtPassword,
                lblMensaje,
                btnLogin, btnRegistro
        );

        root.getChildren().addAll(titulo, subtitulo, formPanel);

        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.show();
    }
}