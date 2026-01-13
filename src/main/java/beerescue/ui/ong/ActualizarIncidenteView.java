package beerescue.ui.ong;

import beerescue.database.UtilsDB;
import beerescue.reportes.EstadoReporte;
import beerescue.reportes.Incidente;
import beerescue.reportes.Severidad;
import beerescue.database.UtilidadReportes;
import beerescue.ui.MenuONGView;
import beerescue.ui.components.*;
import beerescue.usuarios.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ActualizarIncidenteView {

    private final Stage stage;
    private final Usuario usuario;
    private final UtilidadReportes utilidadReportes = new UtilidadReportes();

    private PanelBusqueda panelBusqueda;
    private VBox panelInfo;
    private Label lblFechaValor;
    private Label lblTipoValor;
    private Label lblSeveridadValor;
    private Label lblEstacionValor;
    private TextArea txtDescripcionValor;
    private Label lblEstadoActual;
    private ToggleGroup grupoEstado;

    private Incidente incidenteActual;

    public ActualizarIncidenteView(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        root.setTop(construirHeader());
        root.setCenter(construirContenido());

        Scene scene = new Scene(root, 900, 740);
        stage.setScene(scene);
        stage.show();
    }

    // ------------------------------------------------------------------------
    // UI
    // ------------------------------------------------------------------------

    private HBox construirHeader() {
        return HeaderComponent.create("BeeRescue - Actualizar Incidente","Volver al menú","#ff9800", () -> new MenuONGView(stage, usuario).show()
        );
    }

    private Node construirContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.setPadding(new Insets(30));

        Label lblTitulo = new Label("Actualizar estado de un incidente");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        panelBusqueda = construirPanelBusqueda();
        panelInfo = construirPanelInfo();
        panelInfo.setVisible(false);

        contenedor.getChildren().addAll(lblTitulo, panelBusqueda.getContainer(), panelInfo);

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return scroll;
    }

    private PanelBusqueda construirPanelBusqueda() {
        return PanelBusqueda.create( "Buscar incidente","ID:","ID del incidente",() -> buscarIncidente());
    }

    private VBox construirPanelInfo() {
        InfoPanel infoPanel = InfoPanel.create("Información del incidente", null);
        
        lblFechaValor = infoPanel.addRow(0, "Fecha:");
        lblTipoValor = infoPanel.addRow(1, "Tipo de problema:");
        lblSeveridadValor = infoPanel.addRow(2, "Severidad:");
        lblEstacionValor = infoPanel.addRow(3, "Código de estación:");

        lblEstadoActual = new Label("-");
        lblEstadoActual.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        infoPanel.addRow(4, "Estado actual:", lblEstadoActual);

        txtDescripcionValor = new TextArea();
        txtDescripcionValor.setEditable(false);
        txtDescripcionValor.setWrapText(true);
        txtDescripcionValor.setPrefRowCount(4);
        txtDescripcionValor.setStyle("-fx-background-color: #f5f5f5;");
        VBox descripcionBox = new VBox(new Label("Descripción:"), txtDescripcionValor);
        descripcionBox.setSpacing(6);

        infoPanel.addNode(descripcionBox);
        infoPanel.addSeparator();
        infoPanel.addNode(construirFormularioEstado());

        panelInfo = infoPanel.getContainer();
        return panelInfo;
    }

    private VBox construirFormularioEstado() {
        VBox contenedor = new VBox(15);

        Label lbl = new Label("Actualizar estado");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        grupoEstado = new ToggleGroup();

        RadioButton rbAbierto = crearRadio("Abierto", EstadoReporte.ABIERTO);
        RadioButton rbProceso = crearRadio("En proceso", EstadoReporte.EN_PROCESO);
        RadioButton rbResuelto = crearRadio("Resuelto", EstadoReporte.RESUELTO);

        HBox opciones = new HBox(20, rbAbierto, rbProceso, rbResuelto);
        opciones.setAlignment(Pos.CENTER_LEFT);

        VBox panelInfoEstado = new VBox(8);
        panelInfoEstado.setPadding(new Insets(10));
        panelInfoEstado.setStyle("-fx-background-color: #eceff1; -fx-background-radius: 6;");
        panelInfoEstado.setVisible(false);

        Label lblInfo = new Label();
        lblInfo.setWrapText(true);
        panelInfoEstado.getChildren().add(lblInfo);

        grupoEstado.selectedToggleProperty().addListener((obs, old, nuevo) -> {
            if (nuevo == null) {
                panelInfoEstado.setVisible(false);
                return;
            }

            panelInfoEstado.setVisible(true);
            EstadoReporte estado = (EstadoReporte) nuevo.getUserData();
            switch (estado) {
                case ABIERTO -> {
                    panelInfoEstado.setStyle("-fx-background-color: #ffebee; -fx-background-radius: 6;");
                    lblInfo.setText("El incidente permanecerá pendiente de atención.");
                }
                case EN_PROCESO -> {
                    panelInfoEstado.setStyle("-fx-background-color: #fff9c4; -fx-background-radius: 6;");
                    lblInfo.setText("El incidente se marcará como en atención por el equipo técnico.");
                }
                case RESUELTO -> {
                    panelInfoEstado.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 6;");
                    lblInfo.setText("El incidente será marcado como resuelto y cerrado.");
                }
            }
        });

        HBox botones = ButtonGroup.create(
                "Guardar cambios", "#4caf50", () -> guardarCambios(),
                "Cancelar", "#9e9e9e", () -> limpiarVista(),
                ButtonGroup.ButtonAlignment.LEFT
        );

        contenedor.getChildren().addAll(lbl, opciones, panelInfoEstado, botones);
        return contenedor;
    }

    private RadioButton crearRadio(String texto, EstadoReporte estado) {
        RadioButton rb = new RadioButton(texto);
        rb.setToggleGroup(grupoEstado);
        rb.setUserData(estado);
        rb.setStyle("-fx-font-size: 14px;");
        return rb;
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private void buscarIncidente() {
        if (panelBusqueda.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese el ID del incidente.");
            return;
        }

        try {
            int id = Integer.parseInt(panelBusqueda.getText().trim());
            incidenteActual = UtilsDB.obtenerIncidentePorId(id);

            if (incidenteActual == null) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sin resultados", "No se encontró ningún incidente con el ID " + id);
                panelInfo.setVisible(false);
                return;
            }

            mostrarIncidente(incidenteActual);

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El ID debe ser un número.");
        }
    }

    private void mostrarIncidente(Incidente incidente) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        lblFechaValor.setText(df.format(incidente.getFecha()));
        lblTipoValor.setText(formatearEnum(incidente.getTipoProblema()));
        lblSeveridadValor.setText(incidente.getSeveridad().name());
        aplicarColorSeveridad(incidente.getSeveridad());
        lblEstacionValor.setText(String.valueOf(incidente.getCodigoEstacion()));
        txtDescripcionValor.setText(incidente.getDescripcion());

        lblEstadoActual.setText(incidente.getEstadoReporte().name());
        aplicarColorEstado(incidente.getEstadoReporte());

        grupoEstado.selectToggle(null);
        panelInfo.setVisible(true);
    }

    private void aplicarColorSeveridad(Severidad severidad) {
        String color = switch (severidad) {
            case ALTA -> "#d32f2f";
            case MEDIA -> "#ff9800";
            case BAJA -> "#388e3c";
        };
        lblSeveridadValor.setStyle("-fx-font-weight: bold; -fx-text-fill: " + color + ";");
    }

    private void aplicarColorEstado(EstadoReporte estado) {
        String color = switch (estado) {
            case ABIERTO -> "#f44336";
            case EN_PROCESO -> "#ff9800";
            case RESUELTO -> "#4caf50";
        };
        lblEstadoActual.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
    }

    private void guardarCambios() {
        if (incidenteActual == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Busque primero un incidente.");
            return;
        }
        if (grupoEstado.getSelectedToggle() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione un nuevo estado.");
            return;
        }

        EstadoReporte nuevoEstado = (EstadoReporte) grupoEstado.getSelectedToggle().getUserData();
        boolean exito = utilidadReportes.actualizarEstadoIncidente(incidenteActual.getId(), nuevoEstado);

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Incidente actualizado", "El incidente " + incidenteActual.getId() + " ahora está en estado " + nuevoEstado + ".");
            limpiarVista();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error","No se pudo actualizar el incidente. Intente nuevamente.");
        }
    }

    private void limpiarVista() {
        panelBusqueda.clear();
        panelInfo.setVisible(false);
        grupoEstado.selectToggle(null);
        incidenteActual = null;
    }

    private String formatearEnum(Enum<?> e) {
        return e.name().replace('_', ' ');
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}