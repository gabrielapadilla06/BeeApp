package beerescue.ui;

import beerescue.ui.agricultor.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import beerescue.usuarios.Usuario;

public class MenuAgricultorView {
    private Stage stage;
    private Usuario usuario;

    public MenuAgricultorView(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #4caf50;");

        Label lblTitulo = new Label("BeeRescue - Panel Agricultor");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblUsuario = new Label("Agricultor: " + usuario.getNombre());
        lblUsuario.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
        btnCerrarSesion.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        header.getChildren().addAll(lblTitulo, spacer, lblUsuario, btnCerrarSesion);

        // Centro - Opciones
        VBox centro = new VBox(15);
        centro.setAlignment(Pos.TOP_CENTER);
        centro.setPadding(new Insets(30));

        Label lblOpciones = new Label("Mis Opciones:");
        lblOpciones.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane gridBotones = new GridPane();
        gridBotones.setHgap(15);
        gridBotones.setVgap(15);
        gridBotones.setAlignment(Pos.CENTER);

        Button btnMisEstaciones = crearBotonMenu("Mis Estaciones", "#4caf50");
        Button btnSolicitar = crearBotonMenu("Solicitar Estación", "#2196f3");
        Button btnReportarIncidente = crearBotonMenu("Reportar Incidente", "#ff9800");
        Button btnActualizarNivel = crearBotonMenu("Actualizar Nivel de Agua", "#9c27b0");

        gridBotones.add(btnMisEstaciones, 0, 0);
        gridBotones.add(btnSolicitar, 1, 0);
        gridBotones.add(btnReportarIncidente, 0, 1);
        gridBotones.add(btnActualizarNivel, 1, 1);

        // Eventos - cada uno navega a una pantalla diferente
        btnMisEstaciones.setOnAction(e -> {
            MisEstacionesView vista = new MisEstacionesView(stage, usuario);
            vista.show();
        });

        btnSolicitar.setOnAction(e -> {
            SolicitarEstacionView vista = new SolicitarEstacionView(stage, usuario);
            vista.show();
        });

        btnReportarIncidente.setOnAction(e -> {
            ReportarIncidenteView vista = new ReportarIncidenteView(stage, usuario);
            vista.show();
        });

        btnActualizarNivel.setOnAction(e -> {
            ActualizarNivelAgua vista = new ActualizarNivelAgua(stage, usuario);
            vista.show();
        });

        centro.getChildren().addAll(lblOpciones, gridBotones);

        root.setTop(header);
        root.setCenter(centro);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private Button crearBotonMenu(String texto, String color) {
        Button btn = new Button(texto);
        btn.setPrefSize(250, 80);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10;");
        return btn;
    }
}