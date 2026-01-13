package beerescue.ui.agricultor;

import beerescue.estaciones.Estacion;
import beerescue.reportes.EstadoReporte;
import beerescue.reportes.Severidad;
import beerescue.reportes.TipoProblema;
import beerescue.database.UtilidadReportes;
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
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportarIncidenteView {

    private final Stage stage;
    private final Usuario usuario;
    private final UtilidadReportes utilidadReportes = new UtilidadReportes();

    private final ObservableList<Estacion> estacionesObservable = FXCollections.observableArrayList();

    private ComboBox<Estacion> cboEstacion;
    private ComboBox<TipoProblema> cboTipoProblema;
    private ToggleGroup grupoSeveridad;
    private DatePicker dpFecha;
    private TextField txtHora;
    private TextArea txtDescripcion;
    private CheckBox chkUrgente;

    public ReportarIncidenteView(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        root.setTop(construirHeader());
        root.setCenter(construirContenido());

        cargarEstaciones();

        Scene scene = new Scene(root, 900, 750);
        stage.setScene(scene);
        stage.show();
    }

    // ----------------------------------------------------
    // UI
    // ----------------------------------------------------
    private HBox construirHeader() {
        return HeaderComponent.create("Reportar Incidente","Volver al menú","#ff9800", () -> volverAlMenu());
    }

    private Node construirContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.setPadding(new Insets(30));
        contenedor.getChildren().addAll(construirPanelAdvertencia(), construirFormulario());

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return scroll;
    }

    private Node construirPanelAdvertencia() {
        return AlertPanel.create(AlertPanel.AlertType.WARNING,"Reporte de Incidente", "Utilice este formulario para reportar cualquier problema con sus estaciones.");
    }

    private Node construirFormulario() {
        VBox card = FormCard.create(20);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        int fila = 0;

        grid.add(label("Estación afectada:*"), 0, fila);
        grid.add(comboEstaciones(), 1, fila++);

        grid.add(label("Tipo de incidente:*"), 0, fila);
        grid.add(comboTipoProblema(), 1, fila++);

        grid.add(label("Severidad:*"), 0, fila);
        grid.add(seccionSeveridad(), 1, fila++);

        grid.add(label("Fecha y hora:*"), 0, fila);
        grid.add(seccionFechaHora(), 1, fila++);

        grid.add(label("Descripción:*"), 0, fila);
        grid.add(areaDescripcion(), 1, fila++);

        grid.add(checkUrgente(), 1, fila++);

        Label lblNota = new Label("* Campos obligatorios");
        lblNota.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        card.getChildren().addAll(grid, lblNota, seccionBotones());
        return card;
    }

    private Label label(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-weight: bold;");
        return lbl;
    }

    private Node comboEstaciones() {
        cboEstacion = new ComboBox<>(estacionesObservable);
        cboEstacion.setPromptText("Seleccione la estación");
        cboEstacion.setPrefWidth(350);

        cboEstacion.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Estacion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Código " + item.getId() + " - " + item.getUbicacion());
                }
            }
        });
        cboEstacion.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Estacion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Código " + item.getId() + " - " + item.getUbicacion());
                }
            }
        });

        return cboEstacion;
    }

    private Node comboTipoProblema() {
        cboTipoProblema = new ComboBox<>();
        cboTipoProblema.getItems().setAll(TipoProblema.values());
        cboTipoProblema.setPromptText("Seleccione el tipo");
        cboTipoProblema.setPrefWidth(350);

        cboTipoProblema.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(TipoProblema item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatearEnum(item.name()));
            }
        });
        cboTipoProblema.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipoProblema item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatearEnum(item.name()));
            }
        });

        return cboTipoProblema;
    }

    private String formatearEnum(String name) {
        return name.replace('_', ' ').toUpperCase();
    }

    private Node seccionSeveridad() {
        grupoSeveridad = new ToggleGroup();

        RadioButton rbBaja = crearRadio("Baja", Severidad.BAJA);
        RadioButton rbMedia = crearRadio("Media", Severidad.MEDIA);
        RadioButton rbAlta = crearRadio("Alta", Severidad.ALTA);

        rbMedia.setSelected(true);

        HBox box = new HBox(15, rbBaja, rbMedia, rbAlta);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private RadioButton crearRadio(String texto, Severidad severidad) {
        RadioButton rb = new RadioButton(texto);
        rb.setUserData(severidad);
        rb.setToggleGroup(grupoSeveridad);
        return rb;
    }

    private Node seccionFechaHora() {
        dpFecha = new DatePicker(LocalDate.now());
        txtHora = new TextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        txtHora.setPrefWidth(100);
        txtHora.setPromptText("HH:mm");

        HBox box = new HBox(10, dpFecha, txtHora);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private Node areaDescripcion() {
        txtDescripcion = new TextArea();
        txtDescripcion.setPromptText("Describa lo ocurrido...");
        txtDescripcion.setPrefRowCount(4);
        txtDescripcion.setWrapText(true);
        return txtDescripcion;
    }

    private Node checkUrgente() {
        chkUrgente = new CheckBox("Requiere atención urgente");
        chkUrgente.setStyle("-fx-font-weight: bold; -fx-text-fill: #d32f2f;");
        return chkUrgente;
    }

    private HBox seccionBotones() {
        return ButtonGroup.create("Enviar reporte", "#d32f2f", () -> procesarReporte(),"Cancelar", "#757575", () -> volverAlMenu(), ButtonGroup.ButtonAlignment.RIGHT);
    }

    // ----------------------------------------------------
    // Logic
    // ----------------------------------------------------
    private void cargarEstaciones() {
        List<Estacion> estaciones = UtilsDB.obtenerEstacionesPorUsuario(usuario.getId());
        estacionesObservable.setAll(estaciones);

        if (estaciones.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin estaciones","No se encontraron estaciones para reportar incidentes.");
        }
    }

    private void procesarReporte() {
        if (!validarFormulario()) {
            return;
        }

        Estacion estacion = cboEstacion.getValue();
        TipoProblema tipo = cboTipoProblema.getValue();
        Severidad severidad = (Severidad) grupoSeveridad.getSelectedToggle().getUserData();

        LocalDate fecha = dpFecha.getValue();
        LocalTime hora = LocalTime.parse(txtHora.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

        StringBuilder descripcion = new StringBuilder(txtDescripcion.getText().trim());
        if (chkUrgente.isSelected()) {
            descripcion.append("\nReporte marcado como URGENTE.");
        }

        guardarIncidente(estacion, tipo, severidad, descripcion.toString(), fechaHora);

        mostrarAlerta(Alert.AlertType.INFORMATION, "Reporte registrado","El incidente ha sido reportado. Un especialista revisará la información.");
        volverAlMenu();
    }

    private boolean validarFormulario() {
        if (cboEstacion.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione la estación afectada.");
            return false;
        }
        if (cboTipoProblema.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione el tipo de incidente.");
            return false;
        }
        if (grupoSeveridad.getSelectedToggle() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione la severidad del incidente.");
            return false;
        }
        if (dpFecha.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione la fecha del incidente.");
            return false;
        }
        if (txtHora.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese la hora del incidente.");
            return false;
        }
        try {
            LocalTime.parse(txtHora.getText().trim(), DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "La hora debe tener formato HH:mm.");
            return false;
        }
        if (txtDescripcion.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Describa lo ocurrido.");
            return false;
        }
        return true;
    }

    private void guardarIncidente(Estacion estacion, TipoProblema tipo, Severidad severidad, String descripcion, LocalDateTime fechaHora) {

        EstadoReporte estado = chkUrgente.isSelected() ? EstadoReporte.EN_PROCESO : EstadoReporte.ABIERTO;
        utilidadReportes.insertarIncidente(Timestamp.valueOf(fechaHora), tipo, estado, severidad, descripcion, estacion.getId());
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