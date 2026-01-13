package beerescue.ui.ong;

import beerescue.estaciones.Estacion;
import beerescue.estaciones.EstacionFloral;
import beerescue.estaciones.EstacionHidrica;
import beerescue.database.UtilidadEstaciones;
import beerescue.ui.MenuONGView;
import beerescue.ui.components.*;
import beerescue.usuarios.Usuario;
import beerescue.database.UtilsDB;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EliminarEstacionView {

    private final Stage stage;
    private final Usuario usuario;
    private final UtilidadEstaciones utilidadEstaciones = new UtilidadEstaciones();

    private PanelBusqueda panelBusqueda;
    private VBox panelInfo;
    private Label lblCodigoValor;
    private Label lblUbicacionValor;
    private Label lblTipoValor;
    private Label lblEstadoValor;
    private Label lblProblemasValor;
    private Label lblPropietarioValor;
    private Label lblFechaInstalacionValor;
    private Label lblDetallesValor;

    private CheckBox chkConfirmar;
    private Button btnEliminar;

    private Estacion estacionActual;

    public EliminarEstacionView(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        root.setTop(construirHeader());
        root.setCenter(construirContenido());

        Scene scene = new Scene(root, 900, 720);
        stage.setScene(scene);
        stage.show();
    }

    // ------------------------------------------------------------------------
    // UI
    // ------------------------------------------------------------------------

    private HBox construirHeader() {
        return HeaderComponent.create("BeeRescue - Eliminar Estación","Volver al menú","#d32f2f",() -> new MenuONGView(stage, usuario).show());
    }

    private Node construirContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.setPadding(new Insets(30));
        contenedor.setMaxWidth(750);

        Label lblTitulo = new Label("Eliminar estación del sistema");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #d32f2f;");

        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 0);");

        panelBusqueda = construirPanelBusqueda();
        card.getChildren().addAll(construirPanelAdvertencia(), panelBusqueda.getContainer(), panelInfo = construirPanelInfo());
        panelInfo.setVisible(false);

        ScrollPane scroll = new ScrollPane(card);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return new VBox(lblTitulo, scroll);
    }

    private Node construirPanelAdvertencia() {
        return AlertPanel.createSimple(AlertPanel.AlertType.WARNING,"Esta acción eliminará la estación de forma permanente. Verifique el código antes de proceder.");
    }

    private PanelBusqueda construirPanelBusqueda() {
        return PanelBusqueda.create("Buscar estación por código","Código:","Ingrese el código de la estación",() -> buscarEstacion());
    }

    private VBox construirPanelInfo() {
        InfoPanel infoPanel = InfoPanel.create("Información de la estación", "#d32f2f");
        
        lblCodigoValor = infoPanel.addRow(0, "Código:");
        lblUbicacionValor = infoPanel.addRow(1, "Ubicación:");
        lblTipoValor = infoPanel.addRow(2, "Tipo:");
        lblEstadoValor = infoPanel.addRow(3, "Estado:");
        lblProblemasValor = infoPanel.addRow(4, "¿Tiene problemas?");
        lblPropietarioValor = infoPanel.addRow(5, "ID propietario:");
        lblFechaInstalacionValor = infoPanel.addRow(6, "Fecha de instalación:");
        lblDetallesValor = infoPanel.addRow(7, "Detalles específicos:");

        chkConfirmar = new CheckBox("Confirmo que deseo eliminar esta estación de forma permanente.");
        chkConfirmar.setStyle("-fx-font-weight: bold; -fx-text-fill: #d32f2f;");
        chkConfirmar.setOnAction(e -> btnEliminar.setDisable(!chkConfirmar.isSelected()));

        btnEliminar = new Button("Eliminar estación");
        btnEliminar.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setDisable(true);
        btnEliminar.setOnAction(e -> confirmarEliminacion());

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #9e9e9e; -fx-text-fill: white;");
        btnCancelar.setOnAction(e -> limpiarVista());

        HBox botones = new HBox(12, btnEliminar, btnCancelar);
        botones.setAlignment(Pos.CENTER_LEFT);

        infoPanel.addSeparator();
        infoPanel.addNode(chkConfirmar);
        infoPanel.addNode(botones);
        
        // Apply custom border style
        VBox container = infoPanel.getContainer();
        container.setStyle("-fx-background-color: white; -fx-border-color: #d32f2f; -fx-border-width: 2; " + "-fx-background-radius: 10; -fx-border-radius: 10;");
        
        panelInfo = container;
        return panelInfo;
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private void buscarEstacion() {
        if (panelBusqueda.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese el código de la estación.");
            return;
        }

        try {
            int codigo = Integer.parseInt(panelBusqueda.getText().trim());
            estacionActual = UtilsDB.obtenerEstacionPorCodigo(codigo);

            if (estacionActual == null) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sin resultados", "No existe una estación con el código " + codigo);
                panelInfo.setVisible(false);
                return;
            }

            cargarDatosEstacion(estacionActual);
            panelInfo.setVisible(true);
            chkConfirmar.setSelected(false);
            btnEliminar.setDisable(true);

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El código debe ser un número válido.");
        }
    }

    private void cargarDatosEstacion(Estacion estacion) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        lblCodigoValor.setText(String.valueOf(estacion.getId()));
        lblUbicacionValor.setText(estacion.getUbicacion());
        lblTipoValor.setText(estacion.getTipo().name());
        lblEstadoValor.setText(estacion.getEstadoOperativo().name());
        lblProblemasValor.setText(estacion.isTieneProblemas() ? "Sí" : "No");
        lblPropietarioValor.setText(String.valueOf(estacion.getIdDueno()));
        lblFechaInstalacionValor.setText(estacion.getFechaInstalacion() != null ? df.format(estacion.getFechaInstalacion()) : "-");

        if (estacion instanceof EstacionFloral floral) {
            lblDetallesValor.setText("Flores: " + floral.getFlores() +
                    " | Plantas: " + floral.getNumPlantas());
        } else if (estacion instanceof EstacionHidrica hidrica) {
            lblDetallesValor.setText(String.format("Capacidad: %.1f L | Nivel actual: %.1f L",
                    hidrica.getCapacidadAgua(), hidrica.getNivelActual()));
        } else {
            lblDetallesValor.setText("-");
        }
    }

    private void confirmarEliminacion() {
        if (estacionActual == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Busque primero una estación.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación");
        confirmacion.setHeaderText("¿Está seguro de eliminar la estación?");
        confirmacion.setContentText("Código: " + estacionActual.getId() + "\nEsta acción no se puede deshacer.");
        ButtonType btnSi = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnNo = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacion.getButtonTypes().setAll(btnSi, btnNo);

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == btnSi) {
                utilidadEstaciones.eliminar(estacionActual.getId());
                mostrarAlerta(Alert.AlertType.INFORMATION,"Estación eliminada","La estación se eliminó correctamente.");
                new MenuONGView(stage, usuario).show();
            }
        });
    }

    private void limpiarVista() {
        panelBusqueda.clear();
        panelInfo.setVisible(false);
        estacionActual = null;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}