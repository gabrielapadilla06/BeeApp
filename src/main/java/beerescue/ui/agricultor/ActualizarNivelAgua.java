package beerescue.ui.agricultor;

import beerescue.database.UtilsDB;
import beerescue.estaciones.EstacionHidrica;
import beerescue.database.UtilidadEstaciones;
import beerescue.ui.MenuAgricultorView;
import beerescue.ui.components.*;
import beerescue.usuarios.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ActualizarNivelAgua {

    private final Stage stage;
    private final Usuario usuario;
    private final UtilidadEstaciones utilidadEstaciones = new UtilidadEstaciones();

    private final ObservableList<EstacionHidrica> estacionesObservable = FXCollections.observableArrayList();

    private ComboBox<EstacionHidrica> cboEstaciones;
    private TextField txtCapacidad;
    private TextField txtNivelActual;
    private TextField txtNuevoNivel;
    private Label lblPorcentaje;
    private ProgressBar progressBar;

    public ActualizarNivelAgua(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        root.setTop(construirHeader());
        root.setCenter(construirContenido());

        cargarEstacionesHidricas();

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // ---------------- UI Builders ----------------

    private HBox construirHeader() {
        return HeaderComponent.create("Actualizar Nivel de Agua","Volver al menú","#9c27b0",() -> new MenuAgricultorView(stage, usuario).show());
    }

    private VBox construirContenido() {
        VBox centro = new VBox(20);
        centro.setAlignment(Pos.TOP_CENTER);
        centro.setPadding(new Insets(30));
        centro.setMaxWidth(600);

        VBox card = FormCard.create(15);

        Label lblSubtitulo = new Label("Seleccione una estación hídrica y actualice su nivel de agua");
        lblSubtitulo.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        lblSubtitulo.setWrapText(true);

        Label lblEstacion = new Label("Estación Hídrica:");
        lblEstacion.setStyle("-fx-font-weight: bold;");

        cboEstaciones = new ComboBox<>(estacionesObservable);
        cboEstaciones.setPromptText("Seleccione una estación...");
        cboEstaciones.setMaxWidth(Double.MAX_VALUE);
        cboEstaciones.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(EstacionHidrica item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "ID: " + item.getId() + " - " + item.getUbicacion());
            }
        });
        cboEstaciones.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(EstacionHidrica item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "ID: " + item.getId() + " - " + item.getUbicacion());
            }
        });
        cboEstaciones.setOnAction(e -> mostrarDatosEstacion());

        VBox infoActual = construirPanelInfoActual();

        Label lblNuevoNivel = new Label("Nuevo Nivel de Agua (litros):");
        lblNuevoNivel.setStyle("-fx-font-weight: bold;");

        txtNuevoNivel = new TextField();
        txtNuevoNivel.setPromptText("Ingrese el nuevo nivel en litros");

        HBox botones = construirBotones();

        card.getChildren().addAll(
                lblSubtitulo,
                new Separator(),
                lblEstacion,
                cboEstaciones,
                infoActual,
                lblNuevoNivel,
                txtNuevoNivel,
                botones
        );

        centro.getChildren().add(card);
        return centro;
    }

    private VBox construirPanelInfoActual() {
        VBox panel = new VBox(10);
        panel.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 15; -fx-background-radius: 5;");

        Label lblInfo = new Label("Información Actual");
        lblInfo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        txtCapacidad = crearCampoBloqueado("Capacidad Total:");
        txtNivelActual = crearCampoBloqueado("Nivel Actual:");

        progressBar = new ProgressBar(0);
        progressBar.setPrefHeight(20);
        progressBar.setMaxWidth(Double.MAX_VALUE);

        lblPorcentaje = new Label("0%");
        lblPorcentaje.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        panel.getChildren().addAll(
                lblInfo,
                crearFilaCampo("Capacidad Total:", txtCapacidad),
                crearFilaCampo("Nivel Actual:", txtNivelActual),
                progressBar,
                lblPorcentaje
        );
        return panel;
    }

    private HBox crearFilaCampo(String etiqueta, TextField campo) {
        Label label = new Label(etiqueta);
        label.setStyle("-fx-font-weight: bold;");
        HBox fila = new HBox(10, label, campo);
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

    private TextField crearCampoBloqueado(String prompt) {
        TextField txt = new TextField();
        txt.setEditable(false);
        txt.setMaxWidth(160);
        txt.setStyle("-fx-background-color: #f0f0f0;");
        txt.setPromptText(prompt);
        return txt;
    }

    private HBox construirBotones() {
        return ButtonGroup.create(
                "Actualizar Nivel", "#9c27b0", () -> actualizarNivel(),
                "Limpiar", "#757575", () -> limpiarFormulario(),
                ButtonGroup.ButtonAlignment.CENTER
        );
    }

    // ---------------- Logic ----------------

    private void cargarEstacionesHidricas() {
        List<EstacionHidrica> estaciones = UtilsDB.obtenerEstacionesHidricasPorUsuario(usuario.getId());
        estacionesObservable.setAll(estaciones);

        if (estaciones.isEmpty()) {
            mostrarAlerta("Sin Estaciones", "No tiene estaciones hídricas registradas.", Alert.AlertType.INFORMATION);
        }
    }

    private void mostrarDatosEstacion() {
        EstacionHidrica estacion = cboEstaciones.getSelectionModel().getSelectedItem();
        if (estacion == null) {
            limpiarPanelInfo();
            return;
        }

        txtCapacidad.setText(String.format("%.2f L", estacion.getCapacidadAgua()));
        txtNivelActual.setText(String.format("%.2f L", estacion.getNivelActual()));

        double porcentaje = estacion.getCapacidadAgua() == 0
                ? 0
                : (estacion.getNivelActual() / estacion.getCapacidadAgua()) * 100;

        progressBar.setProgress(porcentaje / 100);
        lblPorcentaje.setText(String.format("%.1f%%", porcentaje));

        if (porcentaje < 30) {
            progressBar.setStyle("-fx-accent: #f44336;");
        } else if (porcentaje < 70) {
            progressBar.setStyle("-fx-accent: #ff9800;");
        } else {
            progressBar.setStyle("-fx-accent: #4caf50;");
        }
    }

    private void actualizarNivel() {
        EstacionHidrica estacion = cboEstaciones.getSelectionModel().getSelectedItem();
        if (estacion == null) {
            mostrarAlerta("Error", "Debe seleccionar una estación", Alert.AlertType.WARNING);
            return;
        }
        if (txtNuevoNivel.getText().trim().isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar el nuevo nivel de agua", Alert.AlertType.WARNING);
            return;
        }

        try {
            double nuevoNivel = Double.parseDouble(txtNuevoNivel.getText().trim());
            if (nuevoNivel < 0) {
                mostrarAlerta("Error", "El nivel de agua no puede ser negativo", Alert.AlertType.WARNING);
                return;
            }
            if (nuevoNivel > estacion.getCapacidadAgua()) {
                mostrarAlerta("Error", "El nivel no puede exceder la capacidad (" +
                        estacion.getCapacidadAgua() + " L)", Alert.AlertType.WARNING);
                return;
            }

            boolean exito = utilidadEstaciones.actualizarNivelAguaEstacion(estacion.getId(), nuevoNivel);
            if (exito) {
                mostrarAlerta("Éxito", "Nivel de agua actualizado correctamente", Alert.AlertType.INFORMATION);
                estacion.setNivelActual(nuevoNivel);
                mostrarDatosEstacion();
                txtNuevoNivel.clear();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el nivel de agua", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un número válido", Alert.AlertType.WARNING);
        }
    }

    private void limpiarFormulario() {
        cboEstaciones.getSelectionModel().clearSelection();
        txtNuevoNivel.clear();
        limpiarPanelInfo();
    }

    private void limpiarPanelInfo() {
        txtCapacidad.clear();
        txtNivelActual.clear();
        progressBar.setProgress(0);
        lblPorcentaje.setText("0%");
        progressBar.setStyle("-fx-accent: #2196f3;");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}