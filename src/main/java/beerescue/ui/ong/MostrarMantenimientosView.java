package beerescue.ui.ong;

import beerescue.database.UtilsDB;
import beerescue.reportes.VisitaMantenimiento;
import beerescue.ui.MenuONGView;
import beerescue.ui.components.*;
import java.util.Objects;
import beerescue.usuarios.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MostrarMantenimientosView {

    private final Stage stage;
    private final Usuario usuario;

    private final ObservableList<MantenimientoData> mantenimientos = FXCollections.observableArrayList();

    private ComboBox<String> cboActividad;
    private TextField txtCodigoEstacion;
    private Label lblTotales;
    private Label lblTotalGeneral;
    private Label lblTotalRecarga;
    private Label lblTotalPoda;
    private Label lblTotalLimpieza;
    private Label lblTotalReparacion;
    private TableView<MantenimientoData> tabla;

    public MostrarMantenimientosView(Stage stage, Usuario usuario) {
        this.stage = stage;
        this.usuario = usuario;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        root.setTop(construirHeader());
        root.setCenter(construirContenido());

        cargarMantenimientos();

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.show();
    }

    // == UI builders ==

    private HBox construirHeader() {
        return HeaderComponent.create("BeeRescue - Mantenimientos","←Volver al menú","#9c27b0", () -> new MenuONGView(stage, usuario).show());
    }

    private VBox construirContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setPadding(new Insets(30));

        Label lblTitulo = new Label("Visitas de mantenimiento registradas");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        HBox stats = construirPanelEstadisticas();
        HBox filtros = construirPanelFiltros();
        tabla = construirTabla();
        HBox botones = construirBotones();

        contenedor.getChildren().addAll(lblTitulo, stats, filtros, tabla, botones);
        return contenedor;
    }

    private HBox construirPanelEstadisticas() {
        HBox panel = new HBox(12);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        lblTotalGeneral = crearStat("Total visitas", "#2196f3");
        lblTotalRecarga = crearStat("Recargas", "#03a9f4");
        lblTotalPoda = crearStat("Podas", "#4caf50");
        lblTotalLimpieza = crearStat("Limpiezas", "#ff9800");
        lblTotalReparacion = crearStat("Reparaciones", "#e91e63");

        panel.getChildren().addAll(lblTotalGeneral, lblTotalRecarga, lblTotalPoda, lblTotalLimpieza, lblTotalReparacion);
        return panel;
    }

    private Label crearStat(String titulo, String color) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 15, 10, 15));
        box.setStyle("-fx-background-color: " + color + "15; -fx-background-radius: 8;");

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 11px; -fx-text-fill: #555;");

        Label lblValor = new Label("0");
        lblValor.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        box.getChildren().addAll(lblTitulo, lblValor);
        return lblValor;
    }

    private HBox construirPanelFiltros() {
        HBox panel = new HBox(15);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        cboActividad = new ComboBox<>();
        cboActividad.getItems().addAll("TODAS", "RECARGA", "PODA", "LIMPIEZA", "REPARACION");
        cboActividad.setValue("TODAS");

        txtCodigoEstacion = new TextField();
        txtCodigoEstacion.setPromptText("Código estación (opcional)");
        txtCodigoEstacion.setPrefWidth(150);

        Button btnFiltrar = new Button("Aplicar filtros");
        btnFiltrar.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");
        btnFiltrar.setOnAction(e -> aplicarFiltros());

        Button btnLimpiar = new Button("Limpiar");
        btnLimpiar.setStyle("-fx-background-color: #757575; -fx-text-fill: white;");
        btnLimpiar.setOnAction(e -> {
            cboActividad.setValue("TODAS");
            txtCodigoEstacion.clear();
            aplicarFiltros();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        lblTotales = new Label("Total: 0 visitas");
        lblTotales.setStyle("-fx-font-weight: bold;");

        panel.getChildren().addAll(
                new Label("Actividad:"), cboActividad,
                new Label("Estación:"), txtCodigoEstacion,
                btnFiltrar, btnLimpiar,
                spacer,
                lblTotales
        );
        return panel;
    }

    private TableView<MantenimientoData> construirTabla() {
        tabla = new TableView<>();              // asignamos el campo
        tabla.setItems(mantenimientos);
        tabla.setPrefHeight(420);
        tabla.setPlaceholder(new Label("No hay visitas que coincidan con el filtro."));
    
        TableColumn<MantenimientoData, Integer> colId = crearColumna("ID", "id", 70);
        TableColumn<MantenimientoData, String> colFecha = crearColumna("Fecha", "fecha", 130);
        TableColumn<MantenimientoData, String> colActividad = crearColumna("Actividad", "tipoActividad", 150);
        TableColumn<MantenimientoData, Integer> colEstacion = crearColumna("Estación", "codigoEstacion", 110);
        TableColumn<MantenimientoData, String> colObs = crearColumna("Observaciones", "observaciones", 300);
    
        colActividad.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String actividad, boolean empty) {
                super.updateItem(actividad, empty);
                if (empty || actividad == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(actividad);
                    setStyle(switch (actividad) {
                        case "RECARGA" -> "-fx-background-color: #e3f2fd; -fx-font-weight: bold;";
                        case "PODA" -> "-fx-background-color: #e8f5e9; -fx-font-weight: bold;";
                        case "LIMPIEZA" -> "-fx-background-color: #fff3e0; -fx-font-weight: bold;";
                        case "REPARACION" -> "-fx-background-color: #fce4ec; -fx-font-weight: bold;";
                        default -> "";
                    });
                }
            }
        });
    
        agregarColumnas(colId, colFecha, colActividad, colEstacion, colObs);
        return tabla;
    }

    private <T> TableColumn<MantenimientoData, T> crearColumna(String titulo, String propiedad, double ancho) {
        TableColumn<MantenimientoData, T> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setPrefWidth(ancho);
        return col;
    }

    private HBox construirBotones() {
        Button btnDetalle = new Button("Ver detalle");
        btnDetalle.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
        btnDetalle.setDisable(true);
        btnDetalle.setOnAction(e -> verDetalle());

        Button btnNuevo = new Button("Registrar visita");
        btnNuevo.setStyle("-fx-background-color: #9c27b0; -fx-text-fill: white;");
        btnNuevo.setOnAction(e -> new CrearMantenimientoView(stage, usuario).show());

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, nuevo) ->
                btnDetalle.setDisable(nuevo == null));

        HBox box = new HBox(12, btnDetalle, btnNuevo);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    // == Logic ==

    private void cargarMantenimientos() {
        List<VisitaMantenimiento> lista = UtilsDB.obtenerMantenimientos();
        mantenimientos.setAll(lista.stream().map(this::mapearMantenimiento).toList());
        actualizarResumen();
        aplicarFiltros();
    }

    private MantenimientoData mapearMantenimiento(VisitaMantenimiento visita) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return new MantenimientoData(
                visita.getId(),
                visita.getFecha() != null ? df.format(visita.getFecha()) : "-",
                visita.getTipoActividad().name(),
                visita.getObservaciones() != null ? visita.getObservaciones() : "Sin observaciones",
                visita.getCodigoEstacion()
        );
    }

    private void aplicarFiltros() {
        String actividad = cboActividad.getValue();
        Integer codigo = null;
    
        if (!txtCodigoEstacion.getText().trim().isEmpty()) {
            try {
                codigo = Integer.parseInt(txtCodigoEstacion.getText().trim());
            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El código de estación debe ser numérico.");
                return;
            }
        }
    
        final Integer filtroCodigo = codigo; // make it effectively final
    
        List<MantenimientoData> filtrados = mantenimientos.stream()
                .filter(m -> "TODAS".equals(actividad) || m.getTipoActividad().equals(actividad))
                .filter(m -> filtroCodigo == null || Objects.equals(m.getCodigoEstacion(), filtroCodigo))
                .toList();
    
        tabla.setItems(FXCollections.observableArrayList(filtrados));
        lblTotales.setText("Total: " + filtrados.size() + " visitas");
    }

    private void actualizarResumen() {
        long total = mantenimientos.size();
        long recarga = mantenimientos.stream().filter(m -> "RECARGA".equals(m.getTipoActividad())).count();
        long poda = mantenimientos.stream().filter(m -> "PODA".equals(m.getTipoActividad())).count();
        long limpieza = mantenimientos.stream().filter(m -> "LIMPIEZA".equals(m.getTipoActividad())).count();
        long reparacion = mantenimientos.stream().filter(m -> "REPARACION".equals(m.getTipoActividad())).count();

        lblTotalGeneral.setText(String.valueOf(total));
        lblTotalRecarga.setText(String.valueOf(recarga));
        lblTotalPoda.setText(String.valueOf(poda));
        lblTotalLimpieza.setText(String.valueOf(limpieza));
        lblTotalReparacion.setText(String.valueOf(reparacion));
    }

    private void verDetalle() {
        MantenimientoData m = tabla.getSelectionModel().getSelectedItem();
        if (m == null) return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalle del mantenimiento");
        alert.setHeaderText("Visita #" + m.getId() + " - " + m.getTipoActividad());

        TextArea txt = new TextArea("""
                Fecha: %s
                Actividad: %s
                Estación: %d

                Observaciones:
                %s
                """.formatted(m.getFecha(), m.getTipoActividad(), m.getCodigoEstacion(), m.getObservaciones()));
        txt.setEditable(false);
        txt.setWrapText(true);

        alert.getDialogPane().setContent(txt);
        alert.showAndWait();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @SafeVarargs
    private final void agregarColumnas(TableColumn<MantenimientoData, ?>... columnas) {
        tabla.getColumns().addAll(columnas);
    }

    // == DTO interno ==

    public static class MantenimientoData {
        private final Integer id;
        private final String fecha;
        private final String tipoActividad;
        private final String observaciones;
        private final Integer codigoEstacion;

        public MantenimientoData(Integer id, String fecha, String tipoActividad,
                                 String observaciones, Integer codigoEstacion) {
            this.id = id;
            this.fecha = fecha;
            this.tipoActividad = tipoActividad;
            this.observaciones = observaciones;
            this.codigoEstacion = codigoEstacion;
        }

        public Integer getId() { return id; }
        public String getFecha() { return fecha; }
        public String getTipoActividad() { return tipoActividad; }
        public String getObservaciones() { return observaciones; }
        public Integer getCodigoEstacion() { return codigoEstacion; }
    }
}