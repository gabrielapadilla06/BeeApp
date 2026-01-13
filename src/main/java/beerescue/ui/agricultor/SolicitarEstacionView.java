package beerescue.ui.agricultor;

import beerescue.estaciones.EstadoSolicitud;
import beerescue.estaciones.SolicitudInstalacion;
import beerescue.estaciones.TipoEstacion;
import beerescue.database.UtilidadEstaciones;
import beerescue.ui.MenuAgricultorView;
import beerescue.ui.components.*;
import beerescue.usuarios.Usuario;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Date;

public class SolicitarEstacionView {

    private final Stage stage;
    private final Usuario usuario;
    private final UtilidadEstaciones utilidadEstaciones = new UtilidadEstaciones();

    private TextField txtUbicacion;
    private ComboBox<TipoEstacion> cboTipoEstacion;
    public SolicitarEstacionView(Stage stage, Usuario usuario) {
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
    // UI builders
    // ------------------------------------------------------------------------

    private HBox construirHeader() {
        return HeaderComponent.create("Solicitar Estación para abejas","Volver al menú","#2196f3",() -> volverAlMenu());
    }

    private Node construirContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.setPadding(new Insets(30));
        contenedor.setMaxWidth(650);

        VBox card = FormCard.create(20);

        Label lblInstrucciones = new Label("Complete el formulario para solicitar una estación:");
        lblInstrucciones.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        card.getChildren().addAll(lblInstrucciones, construirFormulario(), construirNota(), construirBotones());

        contenedor.getChildren().add(card);

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return scroll;
    }

    private GridPane construirFormulario() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 0, 0));

        int fila = 0;

        grid.add(label("Ubicación:*"), 0, fila);
        grid.add(txtUbicacion = textField("Dirección completa o coordenadas"), 1, fila++);

        grid.add(label("Tipo de estación:*"), 0, fila);
        grid.add(cboTipoEstacion = comboTipo(), 1, fila++);

        return grid;
    }

    private Label label(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }

    private TextField textField(String placeholder) {
        TextField txt = new TextField();
        txt.setPromptText(placeholder);
        txt.setPrefWidth(350);
        return txt;
    }


    private ComboBox<TipoEstacion> comboTipo() {
        ComboBox<TipoEstacion> combo = new ComboBox<>();
        combo.getItems().addAll(TipoEstacion.values());
        combo.setPromptText("Seleccione el tipo de estación");
        combo.setPrefWidth(350);
        combo.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(TipoEstacion item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.name());
            }
        });
        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipoEstacion item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.name());
            }
        });
        return combo;
    }


    private Label construirNota() {
        Label lblNota = new Label("* Campos obligatorios");
        lblNota.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        return lblNota;
    }

    private HBox construirBotones() {
        HBox box = ButtonGroup.create("Enviar Solicitud", "#4caf50", () -> procesarSolicitud(),"Limpiar", "#ff9800", () -> limpiarFormulario(),ButtonGroup.ButtonAlignment.CENTER);
        box.setPadding(new Insets(10, 0, 0, 0));
        return box;
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private void procesarSolicitud() {
        if (!validarFormulario()) {
            return;
        }
        SolicitudInstalacion solicitud = new SolicitudInstalacion(usuario.getId(),cboTipoEstacion.getValue(),txtUbicacion.getText().trim(),EstadoSolicitud.PENDIENTE,20, new Date());

        utilidadEstaciones.insertarSolicitud(solicitud);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Solicitud enviada","Su solicitud fue registrada exitosamente. La ONG se pondrá en contacto pronto.");
        volverAlMenu();
    }

    private boolean validarFormulario() {
        if (txtUbicacion.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese la ubicación.");
            return false;
        }
        if (cboTipoEstacion.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione el tipo de estación.");
            return false;
        }
       
        return true;
    }

    private void limpiarFormulario() {
        txtUbicacion.clear();
        cboTipoEstacion.getSelectionModel().clearSelection();
    }

    private void volverAlMenu() {
        new MenuAgricultorView(stage, usuario).show();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}