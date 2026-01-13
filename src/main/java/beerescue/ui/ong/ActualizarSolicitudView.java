package beerescue.ui.ong;

import beerescue.database.UtilsDB;
import beerescue.estaciones.EstadoSolicitud;
import beerescue.estaciones.SolicitudInstalacion;
import beerescue.estaciones.TipoEstacion;
import beerescue.database.UtilidadEstaciones;
import beerescue.ui.MenuONGView;
import beerescue.ui.components.*;
import beerescue.usuarios.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ActualizarSolicitudView {

    private final Stage stage;
    private final Usuario usuario;
    private final UtilidadEstaciones utilidadEstaciones = new UtilidadEstaciones();

    private PanelBusqueda panelBusqueda;
    private VBox panelInfo;

    private Label lblUsuarioValor;
    private Label lblTipoValor;
    private Label lblUbicacionValor;
    private Label lblCostoValor;
    private Label lblFechaValor;
    private Label lblEstadoActualValor;

    private ComboBox<EstadoSolicitud> cboNuevoEstado;
    private SolicitudInstalacion solicitudActual;

    public ActualizarSolicitudView(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        root.setTop(construirHeader());
        root.setCenter(construirContenido());

        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.show();
    }

    // ------------------------------------------------------------------------
    // UI
    // ------------------------------------------------------------------------

    private HBox construirHeader() {
        return HeaderComponent.create("BeeRescue - Actualizar Solicitud","Volver al menú","#2196f3",() -> new MenuONGView(stage, usuario).show());
    }

    private Node construirContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.setPadding(new Insets(30));

        Label lblTitulo = new Label("Actualizar estado de una solicitud");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        VBox panelBusqueda = construirPanelBusqueda();
        panelInfo = construirPanelInfo();
        panelInfo.setVisible(false);

        contenedor.getChildren().addAll(lblTitulo, panelBusqueda, panelInfo);

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return scroll;
    }

    private VBox construirPanelBusqueda() {
        panelBusqueda = PanelBusqueda.create(
                "Buscar solicitud",
                "ID:",
                "ID de la solicitud",
                () -> buscarSolicitud()
        );
        return panelBusqueda.getContainer();
    }

    private VBox construirPanelInfo() {
        InfoPanel infoPanel = InfoPanel.create("Información de la solicitud", null);
        
        lblUsuarioValor = infoPanel.addRow(0, "Usuario solicitante:");
        lblTipoValor = infoPanel.addRow(1, "Tipo de estación:");
        lblUbicacionValor = infoPanel.addRow(2, "Ubicación / Detalle:");
        lblCostoValor = infoPanel.addRow(3, "Costo estimado:");
        lblFechaValor = infoPanel.addRow(4, "Fecha de solicitud:");
        lblEstadoActualValor = infoPanel.addRow(5, "Estado actual:");
        lblEstadoActualValor.setStyle("-fx-font-weight: bold; -fx-text-fill: #ff9800;");

        infoPanel.addSeparator();
        infoPanel.addNode(construirFormularioEstado());

        panelInfo = infoPanel.getContainer();
        return panelInfo;
    }

    private VBox construirFormularioEstado() {
        VBox contenedor = new VBox(15);

        Label lbl = new Label("Nuevo estado");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        cboNuevoEstado = new ComboBox<>();
        cboNuevoEstado.getItems().addAll(EstadoSolicitud.values());
        cboNuevoEstado.setPromptText("Seleccione un estado");
        cboNuevoEstado.setPrefWidth(300);

        HBox botones = ButtonGroup.create(
                "Guardar cambios", "#4caf50", () -> guardarCambios(),
                "Cancelar", "#9e9e9e", () -> limpiarVista(),
                ButtonGroup.ButtonAlignment.LEFT
        );

        contenedor.getChildren().addAll(lbl, cboNuevoEstado, botones);
        return contenedor;
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private void buscarSolicitud() {
        if (panelBusqueda.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese el ID de la solicitud.");
            return;
        }

        try {
            int id = Integer.parseInt(panelBusqueda.getText().trim());
            solicitudActual = UtilsDB.obtenerSolicitudPorId(id);

            if (solicitudActual == null) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sin resultados", "No existe una solicitud con el ID " + id);
                panelInfo.setVisible(false);
                return;
            }

            mostrarSolicitud(solicitudActual);

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El ID debe ser un número.");
        }
    }

    private void mostrarSolicitud(SolicitudInstalacion solicitud) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        lblUsuarioValor.setText("ID " + solicitud.getIdUsuario());
        lblTipoValor.setText(formatearTipo(solicitud.getTipoEstacion()));
        lblUbicacionValor.setText(solicitud.getUbicacion());
        lblCostoValor.setText(String.format("$ %.2f", solicitud.getCostoEstimado()));
        lblFechaValor.setText(df.format(solicitud.getFechaSolicitud()));
        lblEstadoActualValor.setText(solicitud.getEstadoSolicitud().name());
        aplicarColorEstado(solicitud.getEstadoSolicitud());

        cboNuevoEstado.setValue(solicitud.getEstadoSolicitud());
        panelInfo.setVisible(true);
    }

    private void aplicarColorEstado(EstadoSolicitud estado) {
        String color = switch (estado) {
            case PENDIENTE -> "#ff9800";
            case APROBADA -> "#4caf50";
            case RECHAZADA -> "#f44336";
        };
        lblEstadoActualValor.setStyle("-fx-font-weight: bold; -fx-text-fill: " + color + ";");
    }

    private String formatearTipo(TipoEstacion tipo) {
        return tipo.name().replace('_', ' ');
    }

    private void guardarCambios() {
        if (solicitudActual == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Busque primero una solicitud.");
            return;
        }
        if (cboNuevoEstado.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione el nuevo estado.");
            return;
        }

        EstadoSolicitud nuevoEstado = cboNuevoEstado.getValue();
        boolean exito = utilidadEstaciones.actualizarEstadoSolicitud(solicitudActual.getId(), nuevoEstado);

        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION,"Solicitud actualizada","La solicitud " + solicitudActual.getId() + " ahora está " + nuevoEstado + ".");
            limpiarVista();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR,"Error","No se pudo actualizar la solicitud. Intente nuevamente.");
        }
    }

    private void limpiarVista() {
        panelBusqueda.clear();
        panelInfo.setVisible(false);
        cboNuevoEstado.getSelectionModel().clearSelection();
        solicitudActual = null;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}