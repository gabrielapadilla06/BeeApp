package beerescue.ui.agricultor;

import beerescue.estaciones.Estacion;
import beerescue.estaciones.EstacionFloral;
import beerescue.estaciones.EstacionHidrica;
import beerescue.ui.MenuAgricultorView;
import beerescue.ui.components.*;
import beerescue.usuarios.Usuario;
import beerescue.database.UtilsDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class MisEstacionesView {

    private final Stage stage;
    private final Usuario usuario;

    private final TableView<EstacionData> tabla = new TableView<>();
    private final ObservableList<EstacionData> estacionesObservable = FXCollections.observableArrayList();

    public MisEstacionesView(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // === HEADER ===
        HBox header = construirHeader();
        root.setTop(header);

        // === CONTENIDO CENTRAL ===
        VBox centro = construirContenido();
        root.setCenter(centro);

        // Cargar data inicial
        cargarEstaciones();

        Scene scene = new Scene(root, 900, 650);
        stage.setScene(scene);
        stage.show();
    }

    // ---------------- UI BUILDERS ----------------

    private HBox construirHeader() {
        return HeaderComponent.create("Mis Estaciones para abejas","Volver al menú","#4caf50", () -> volverAlMenu());
    }

    private VBox construirContenido() {
        VBox centro = new VBox(20);
        centro.setAlignment(Pos.TOP_CENTER);
        centro.setPadding(new Insets(30));

        centro.getChildren().addAll(
                construirInfoUsuario(),
                construirTabla(),
                construirBotonesAccion()
        );

        return centro;
    }

    private Node construirInfoUsuario() {
        HBox infoBox = new HBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label lblInfo = new Label("Estaciones asignadas a: " + usuario.getNombre());
        lblInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        infoBox.getChildren().add(lblInfo);

        return infoBox;
    }

    private Node construirTabla() {
        tabla.setItems(estacionesObservable);
        tabla.setPlaceholder(new Label("No se encontraron estaciones para este agricultor."));
        tabla.getStyleClass().add("table-view");
        tabla.setPrefHeight(420);

        TableColumn<EstacionData, Integer> colId = new TableColumn<>("Código");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(80);

        TableColumn<EstacionData, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setPrefWidth(120);

        TableColumn<EstacionData, String> colUbicacion = new TableColumn<>("Ubicación");
        colUbicacion.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        colUbicacion.setPrefWidth(260);

        TableColumn<EstacionData, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setPrefWidth(140);

        TableColumn<EstacionData, String> colInfo = new TableColumn<>("Dato específico");
        colInfo.setCellValueFactory(new PropertyValueFactory<>("datoEspecifico"));
        colInfo.setPrefWidth(200);

        agregarColumnas(colId, colTipo, colUbicacion, colEstado, colInfo);

        VBox contenedorTabla = new VBox(tabla);
        contenedorTabla.setPadding(new Insets(10));
        contenedorTabla.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        return contenedorTabla;
    }

    private Node construirBotonesAccion() {
        HBox botonesAccion = new HBox(15);
        botonesAccion.setAlignment(Pos.CENTER);

        Button btnVerDetalles = new Button("Ver Detalles");
        btnVerDetalles.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; " +
                                "-fx-font-size: 14px; -fx-padding: 10 20;");
        btnVerDetalles.setOnAction(e -> {
            EstacionData seleccionada = tabla.getSelectionModel().getSelectedItem();
            if (seleccionada != null) {
                mostrarDetalles(seleccionada);
            } else {
                mostrarAlerta("Seleccione una estación de la tabla");
            }
        });

        Button btnActualizar = new Button("Actualizar Lista");
        btnActualizar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; " +
                               "-fx-font-size: 14px; -fx-padding: 10 20;");
        btnActualizar.setOnAction(e -> cargarEstaciones());

        botonesAccion.getChildren().addAll(btnVerDetalles, btnActualizar);
        return botonesAccion;
    }

    // ---------------- LOGIC ----------------
    @SafeVarargs
    private final void agregarColumnas(TableColumn<EstacionData, ?>... columnas) {
        tabla.getColumns().addAll(columnas);
    }

    private void cargarEstaciones() {
        List<Estacion> estaciones = UtilsDB.obtenerEstacionesPorUsuario(usuario.getId());
        estacionesObservable.clear();

        for (Estacion estacion : estaciones) {
            String tipo;
            String datoEspecifico;

            if (estacion instanceof EstacionFloral floral) {
                tipo = "Floral";
                datoEspecifico = "Flores: " + floral.getFlores() +
                                 " | Plantas: " + floral.getNumPlantas();
            } else if (estacion instanceof EstacionHidrica hidrica) {
                tipo = "Hídrica";
                datoEspecifico = String.format("Capacidad: %.1f L | Nivel: %.1f L",
                        hidrica.getCapacidadAgua(), hidrica.getNivelActual());
            } else {
                tipo = "General";
                datoEspecifico = "-";
            }

            estacionesObservable.add(new EstacionData(
                    estacion.getId(),
                    tipo,
                    estacion.getUbicacion(),
                    estacion.getEstadoOperativo().name(),
                    datoEspecifico
            ));
        }
    }

    private void volverAlMenu() {
        new MenuAgricultorView(stage, usuario).show();
    }

    private void mostrarDetalles(EstacionData estacion) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de Estación");
        alert.setHeaderText("Estación " + estacion.getId() + " (" + estacion.getTipo() + ")");
        alert.setContentText(
                "Ubicación: " + estacion.getUbicacion() + "\n" +
                "Estado: " + estacion.getEstado() + "\n" +
                estacion.getDatoEspecifico()
        );
        alert.showAndWait();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clase interna para items de la tabla
    public static class EstacionData {
        private final int id;
        private final String tipo;
        private final String ubicacion;
        private final String estado;
        private final String datoEspecifico;

        public EstacionData(int id, String tipo, String ubicacion, String estado, String datoEspecifico) {
            this.id = id;
            this.tipo = tipo;
            this.ubicacion = ubicacion;
            this.estado = estado;
            this.datoEspecifico = datoEspecifico;
        }

        public int getId() { return id; }
        public String getTipo() { return tipo; }
        public String getUbicacion() { return ubicacion; }
        public String getEstado() { return estado; }
        public String getDatoEspecifico() { return datoEspecifico; }
    }
}