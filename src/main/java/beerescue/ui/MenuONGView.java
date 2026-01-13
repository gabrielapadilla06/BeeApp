package beerescue.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import beerescue.usuarios.Usuario;
import beerescue.ui.ong.*;

public class MenuONGView {
    private Stage stage;
    private Usuario usuario;

    public MenuONGView(Stage stage, Usuario usuario) {
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
        header.setStyle("-fx-background-color: #ff6f00;");

        Label lblTitulo = new Label("BeeRescue - Panel ONG");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblUsuario = new Label("Usuario: " + usuario.getNombre());
        lblUsuario.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; " +
                "-fx-font-size: 12px; -fx-padding: 8 15;");
        btnCerrarSesion.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        header.getChildren().addAll(lblTitulo, spacer, lblUsuario, btnCerrarSesion);

        // Centro - Opciones
        VBox centro = new VBox(15);
        centro.setAlignment(Pos.TOP_CENTER);
        centro.setPadding(new Insets(30));

        Label lblOpciones = new Label("Seleccione una opción:");
        lblOpciones.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Grid de botones
        GridPane gridBotones = new GridPane();
        gridBotones.setHgap(15);
        gridBotones.setVgap(15);
        gridBotones.setAlignment(Pos.CENTER);

        // Crear botones según el menú requerido
        Button btnCrearEstacion = crearBotonMenu("Crear Nueva Estación", "#00bcd4");
        Button btnActualizarSolicitud = crearBotonMenu("Actualizar Estado Solicitud", "#2196f3");
        Button btnEliminarEstacion = crearBotonMenu("Eliminar Estación", "#d32f2f");
        Button btnActualizarIncidente = crearBotonMenu("Actualizar Reporte de Incidente", "#ff9800");
        Button btnMostrarMantenimientos = crearBotonMenu("Mostrar Visitas de Mantenimiento", "#9c27b0");
        Button btnCrearMantenimiento = crearBotonMenu("Crear Reporte de Mantenimiento", "#673ab7");

        // Posicionar botones en el grid (3 columnas)
        gridBotones.add(btnCrearEstacion, 0, 0);
        gridBotones.add(btnActualizarSolicitud, 1, 0);
        gridBotones.add(btnEliminarEstacion, 2, 0);
        gridBotones.add(btnActualizarIncidente, 0, 1);
        gridBotones.add(btnMostrarMantenimientos, 1, 1);
        gridBotones.add(btnCrearMantenimiento, 2, 1);

        // Eventos de los botones
        btnCrearEstacion.setOnAction(e -> {
            CrearEstacionView crearEstacionView = new CrearEstacionView(stage, usuario);
            crearEstacionView.show();
        });

        btnActualizarSolicitud.setOnAction(e -> {
            ActualizarSolicitudView actualizarSolicitudView = new ActualizarSolicitudView(stage, usuario);
            actualizarSolicitudView.show();
        });

        btnEliminarEstacion.setOnAction(e -> {
            EliminarEstacionView eliminarEstacionView = new EliminarEstacionView(stage, usuario);
            eliminarEstacionView.show();
        });

        btnActualizarIncidente.setOnAction(e -> {
            ActualizarIncidenteView actualizarIncidenteView = new ActualizarIncidenteView(stage, usuario);
            actualizarIncidenteView.show();
        });

        btnMostrarMantenimientos.setOnAction(e -> {
            MostrarMantenimientosView mostrarMantenimientosView = new MostrarMantenimientosView(stage, usuario);
            mostrarMantenimientosView.show();
        });

        btnCrearMantenimiento.setOnAction(e -> {
            CrearMantenimientoView crearMantenimientoView = new CrearMantenimientoView(stage, usuario);
            crearMantenimientoView.show();
        });

        centro.getChildren().addAll(lblOpciones, gridBotones);

        root.setTop(header);
        root.setCenter(centro);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    private Button crearBotonMenu(String texto, String color) {
        Button btn = new Button(texto);
        btn.setPrefSize(280, 80);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;");

        btn.setOnMouseEntered(e ->
                btn.setStyle("-fx-background-color: derive(" + color + ", -20%); " + "-fx-text-fill: white; -fx-font-size: 14px; " + "-fx-font-weight: bold;-fx-background-radius: 10; " + "-fx-cursor: hand;"));
        btn.setOnMouseExited(e ->
                btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " + "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;"));

        return btn;
    }
}