package beerescue.ui.ong;

import beerescue.reportes.TipoActividad;
import beerescue.database.UtilidadReportes;
import beerescue.reportes.VisitaMantenimiento;
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

import java.util.Date;

public class CrearMantenimientoView {

    private final Stage stage;
    private final Usuario usuario;
    private final UtilidadReportes utilidadReportes = new UtilidadReportes();

    private TextField txtCodigoEstacion;
    private ToggleGroup grupoActividad;
    private TextArea txtObservaciones;
    private Label lblInfoActividad;
    private Label lblInfoEstacion;
    private Label lblFechaActual;

    public CrearMantenimientoView(Stage stage, Usuario usuario) {
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
    // UI BUILDERS
    // ------------------------------------------------------------------------

    private HBox construirHeader() {
        return HeaderComponent.create("BeeRescue - Crear mantenimiento","Volver al menú","#673ab7", () -> new MenuONGView(stage, usuario).show());
    }

    private Node construirContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.setPadding(new Insets(30));
        contenedor.setMaxWidth(720);

        Label lblTitulo = new Label("Registro de visita de mantenimiento");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        VBox card = FormCard.create(20);

        card.getChildren().addAll(
                construirPanelIntro(),
                construirSeccionEstacion(),
                new Separator(),
                construirSeccionActividad(),
                construirInfoActividad(),
                new Separator(),
                construirSeccionObservaciones(),
                new Separator(),
                construirSeccionFecha(),
                construirBotones()
        );

        ScrollPane scroll = new ScrollPane(card);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return new VBox(lblTitulo, scroll);
    }

    private Node construirPanelIntro() {
        return AlertPanel.createSimple(AlertPanel.AlertType.INFO,"Registre las actividades realizadas en cada estación para mantener un historial completo.");
    }

    private Node construirSeccionEstacion() {
        VBox box = new VBox(10);
        box.getChildren().addAll(
                etiqueta("Código de estación:"),
                crearCampoEstacion()
        );
        return box;
    }

    private Node crearCampoEstacion() {
        txtCodigoEstacion = new TextField();
        txtCodigoEstacion.setPromptText("Ingrese el código de la estación");
        txtCodigoEstacion.setPrefWidth(200);

        Button btnVerificar = new Button("Verificar");
        btnVerificar.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");
        btnVerificar.setOnAction(e -> verificarEstacion());

        lblInfoEstacion = new Label();
        lblInfoEstacion.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        VBox cont = new VBox(6,
                new HBox(10, txtCodigoEstacion, btnVerificar),
                lblInfoEstacion
        );
        return cont;
    }

    private Node construirSeccionActividad() {
        VBox box = new VBox(10);
        box.getChildren().add(etiqueta("Tipo de actividad realizada:"));

        grupoActividad = new ToggleGroup();

        VBox opciones = new VBox(12,
                radioActividad("Re-carga", TipoActividad.RECARGA, "Reabastecimiento de agua o recursos."),
                radioActividad("Poda", TipoActividad.PODA, "Recorte y mantenimiento de plantas."),
                radioActividad("Limpieza", TipoActividad.LIMPIEZA, "Limpieza general de la estación."),
                radioActividad("Reparación", TipoActividad.REPARACION, "Reparación de componentes o estructuras.")
        );
        box.getChildren().add(opciones);

        return box;
    }

    private RadioButton radioActividad(String texto, TipoActividad tipo, String descripcion) {
        RadioButton rb = new RadioButton(texto);
        rb.setUserData(tipo);
        rb.setToggleGroup(grupoActividad);
        rb.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Tooltip tooltip = new Tooltip(descripcion);
        rb.setTooltip(tooltip);
        return rb;
    }

    private Node construirInfoActividad() {
        lblInfoActividad = new Label();
        lblInfoActividad.setWrapText(true);
        lblInfoActividad.setStyle("-fx-font-size: 13px; -fx-font-style: italic;");

        VBox panel = new VBox(lblInfoActividad);
        panel.setPadding(new Insets(12));
        panel.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 6;");
        panel.setVisible(false);

        grupoActividad.selectedToggleProperty().addListener((obs, old, nuevo) -> {
            if (nuevo == null) {
                panel.setVisible(false);
                return;
            }
            panel.setVisible(true);
            TipoActividad actividad = (TipoActividad) nuevo.getUserData();
            lblInfoActividad.setText(descripcionActividad(actividad));
        });

        return panel;
    }

    private String descripcionActividad(TipoActividad actividad) {
        return switch (actividad) {
            case RECARGA -> "Se registrará una recarga de agua o insumos esenciales.";
            case PODA -> "Se registrará una poda o cuidado de plantas.";
            case LIMPIEZA -> "La estación quedará marcada con limpieza realizada.";
            case REPARACION -> "Se registrará una reparación de componentes.";
        };
    }

    private Node construirSeccionObservaciones() {
        txtObservaciones = new TextArea();
        txtObservaciones.setPromptText("Detalles de la visita, hallazgos y acciones realizadas...");
        txtObservaciones.setPrefRowCount(5);
        txtObservaciones.setWrapText(true);

        Label contador = new Label("0 caracteres");
        contador.setStyle("-fx-font-size: 11px; -fx-text-fill: #999;");

        txtObservaciones.textProperty().addListener((obs, old, nuevo) ->
                contador.setText(nuevo.length() + " caracteres"));

        VBox box = new VBox(10,
                etiqueta("Observaciones:"),
                txtObservaciones,
                contador
        );
        return box;
    }

    private Node construirSeccionFecha() {
        lblFechaActual = new Label(new Date().toString());
        lblFechaActual.setStyle("-fx-font-size: 13px; -fx-text-fill: #666; -fx-background-color: #f5f5f5; " +
                "-fx-padding: 8; -fx-background-radius: 5;");

        VBox box = new VBox(5, etiqueta("Fecha de registro:"), lblFechaActual);
        return box;
    }

    private Node construirBotones() {
        Button btnGuardar = new Button("Crear reporte");
        btnGuardar.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 15px;");
        btnGuardar.setOnAction(e -> guardarReporte());

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 15px;");
        btnCancelar.setOnAction(e -> new MenuONGView(stage, usuario).show());

        return ButtonGroup.create(ButtonGroup.ButtonAlignment.RIGHT, btnGuardar, btnCancelar);
    }

    private Label etiqueta(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        return lbl;
    }

    // ------------------------------------------------------------------------
    // LOGIC
    // ------------------------------------------------------------------------

    private void verificarEstacion() {
        if (txtCodigoEstacion.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese el código de estación.");
            return;
        }
        try {
            Integer.parseInt(txtCodigoEstacion.getText().trim());
            lblInfoEstacion.setText("Código válido. Asegúrese que corresponde a una estación existente.");
            lblInfoEstacion.setStyle("-fx-text-fill: #4caf50;");
        } catch (NumberFormatException e) {
            lblInfoEstacion.setText("El código debe ser un número.");
            lblInfoEstacion.setStyle("-fx-text-fill: #f44336;");
        }
    }

    private void guardarReporte() {
        if (!validarFormulario()) {
            return;
        }
        try {
            int codigoEstacion = Integer.parseInt(txtCodigoEstacion.getText().trim());
            TipoActividad actividad = (TipoActividad) grupoActividad.getSelectedToggle().getUserData();
            String observaciones = txtObservaciones.getText().trim();
            Date fecha = new Date();

            VisitaMantenimiento visita = new VisitaMantenimiento(
                    fecha,
                    actividad,
                    observaciones,
                    codigoEstacion
            );

            utilidadReportes.insertarMantenimiento(visita);

            mostrarAlerta(Alert.AlertType.INFORMATION,
                    "Reporte registrado",
                    "La visita de mantenimiento se registró correctamente.");
            new MenuONGView(stage, usuario).show();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "El código de estación debe ser numérico.");
        }
    }

    private boolean validarFormulario() {
        if (txtCodigoEstacion.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese el código de estación.");
            return false;
        }
        try {
            int codigo = Integer.parseInt(txtCodigoEstacion.getText().trim());
            if (codigo <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese un código válido.");
            return false;
        }

        if (grupoActividad.getSelectedToggle() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione el tipo de actividad.");
            return false;
        }
        if (txtObservaciones.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese las observaciones.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}