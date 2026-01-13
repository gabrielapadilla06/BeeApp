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

public class RegistroView {
    private Stage stage;
    private LoginView loginView;
    private InicioSesion inicioSesion;

    public RegistroView(Stage stage, LoginView loginView) {
        this.stage = stage;
        this.loginView = loginView;
        this.inicioSesion = new InicioSesion();
    }

    public void show() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #bbdefb);");

        // Título
        Label titulo = new Label("Registro de Usuario");
        titulo.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #ff6f00;");

        Label subtitulo = new Label("Complete el formulario para crear una cuenta");
        subtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #424242;");

        // Panel del formulario
        VBox formPanel = new VBox(15);
        formPanel.setAlignment(Pos.CENTER);
        formPanel.setPadding(new Insets(30));
        formPanel.setMaxWidth(450);
        formPanel.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);");

        // Nombre completo
        Label lblNombre = new Label("Nombre completo:");
        lblNombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ingrese su nombre completo");
        txtNombre.setPrefHeight(40);
        txtNombre.setStyle("-fx-font-size: 14px; -fx-background-radius: 5;");

        // Usuario
        Label lblUsuario = new Label("Usuario:");
        lblUsuario.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Ingrese su nombre de usuario");
        txtUsuario.setPrefHeight(40);
        txtUsuario.setStyle("-fx-font-size: 14px; -fx-background-radius: 5;");

        // Contraseña
        Label lblPassword = new Label("Contraseña:");
        lblPassword.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Ingrese su contraseña");
        txtPassword.setPrefHeight(40);
        txtPassword.setStyle("-fx-font-size: 14px; -fx-background-radius: 5;");

        // Confirmar contraseña
        Label lblConfirmPassword = new Label("Confirmar contraseña:");
        lblConfirmPassword.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        PasswordField txtConfirmPassword = new PasswordField();
        txtConfirmPassword.setPromptText("Confirme su contraseña");
        txtConfirmPassword.setPrefHeight(40);
        txtConfirmPassword.setStyle("-fx-font-size: 14px; -fx-background-radius: 5;");

        // Rol de usuario
        Label lblRol = new Label("Seleccione su rol:");
        lblRol.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        ToggleGroup groupRol = new ToggleGroup();
        
        RadioButton rbONG = new RadioButton("ONG");
        rbONG.setToggleGroup(groupRol);
        rbONG.setStyle("-fx-font-size: 14px;");
        rbONG.setSelected(true);
        
        RadioButton rbAgricultor = new RadioButton("Agricultor");
        rbAgricultor.setToggleGroup(groupRol);
        rbAgricultor.setStyle("-fx-font-size: 14px;");
        
        HBox rolBox = new HBox(20);
        rolBox.setAlignment(Pos.CENTER_LEFT);
        rolBox.getChildren().addAll(rbONG, rbAgricultor);

        // Mensaje de error/info
        Label lblMensaje = new Label();
        lblMensaje.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 13px;");
        lblMensaje.setWrapText(true);
        lblMensaje.setMaxWidth(390);

        // Botones
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnRegistrar = new Button("Registrar");
        btnRegistrar.setPrefWidth(170);
        btnRegistrar.setPrefHeight(45);
        btnRegistrar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;");

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setPrefWidth(170);
        btnCancelar.setPrefHeight(45);
        btnCancelar.setStyle("-fx-background-color: #757575; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;");

        // Efectos hover
        btnRegistrar.setOnMouseEntered(e -> btnRegistrar.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        btnRegistrar.setOnMouseExited(e -> btnRegistrar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        btnCancelar.setOnMouseEntered(e -> btnCancelar.setStyle("-fx-background-color: #616161; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        btnCancelar.setOnMouseExited(e -> btnCancelar.setStyle("-fx-background-color: #757575; -fx-text-fill: white; " + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 5;"));

        // Evento de registro
        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String password = txtPassword.getText().trim();
            String confirmPassword = txtConfirmPassword.getText().trim();

            // Validaciones
            if (nombre.isEmpty() || usuario.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                lblMensaje.setText("Por favor complete todos los campos");
                lblMensaje.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 13px;");
                return;
            }

            if (!password.equals(confirmPassword)) {
                lblMensaje.setText("Las contraseñas no coinciden");
                lblMensaje.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 13px;");
                txtPassword.clear();
                txtConfirmPassword.clear();
                return;
            }

            if (password.length() < 4) {
                lblMensaje.setText("La contraseña debe tener al menos 4 caracteres");
                lblMensaje.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 13px;");
                return;
            }

            // Obtener el rol seleccionado
            RolUsuario rol = rbONG.isSelected() ? RolUsuario.ONG : RolUsuario.AGRICULTOR;

            // Intentar registrar el usuario
            Usuario nuevoUsuario = inicioSesion.signupGUI(nombre, usuario, password, rol);

            if (nuevoUsuario != null) {
                // Registro exitoso
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registro Exitoso");
                alert.setHeaderText(null);
                alert.setContentText("Usuario registrado correctamente.\n\nYa puede iniciar sesión con sus credenciales.");
                alert.showAndWait();

                // Volver a la pantalla de login
                loginView.show();
            } else {
                // Error: usuario ya existe
                lblMensaje.setText("El nombre de usuario ya existe. Por favor elija otro.");
                lblMensaje.setStyle("-fx-text-fill: #d32f2f; -fx-font-size: 13px;");
                txtUsuario.clear();
                txtUsuario.requestFocus();
            }
        });

        // Evento de cancelar
        btnCancelar.setOnAction(e -> {
            loginView.show();
        });

        buttonBox.getChildren().addAll(btnRegistrar, btnCancelar);

        formPanel.getChildren().addAll(
                lblNombre, txtNombre,
                lblUsuario, txtUsuario,
                lblPassword, txtPassword,
                lblConfirmPassword, txtConfirmPassword,
                lblRol, rolBox,
                lblMensaje,
                buttonBox
        );

        root.getChildren().addAll(titulo, subtitulo, formPanel);

        Scene scene = new Scene(root, 700, 700);
        stage.setScene(scene);
        stage.show();
    }
}