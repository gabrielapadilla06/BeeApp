package beerescue.ui.ong;

import beerescue.estaciones.Estacion;
import beerescue.estaciones.EstacionFloral;
import beerescue.estaciones.EstacionHidrica;
import beerescue.estaciones.EstadoEstacion;
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

import java.util.Date;

public class CrearEstacionView {

    private final Stage stage;
    private final Usuario usuario;
    private final UtilidadEstaciones utilidadEstaciones = new UtilidadEstaciones();

    private TextField txtUbicacion;
    private TextField txtIdDueno;
    private ComboBox<EstadoEstacion> cboEstadoOperativo;
    private CheckBox chkProblemas;
    private ComboBox<TipoEstacion> cboTipoEstacion;

    // campos florales
    private TextField txtFlores;
    private TextField txtNumPlantas;

    // campos hídricos
    private TextField txtCapacidad;
    private TextField txtNivelActual;

    private VBox panelEspecifico;

    public CrearEstacionView(Stage stage, Usuario usuario) {
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
        return HeaderComponent.create("BeeRescue - Crear Estación","Volver al menú","#00bcd4",() -> new MenuONGView(stage, usuario).show());
    }

    private Node construirContenido() {
        VBox contenedor = new VBox(20);
        contenedor.setAlignment(Pos.TOP_CENTER);
        contenedor.setPadding(new Insets(30));
        contenedor.setMaxWidth(700);

        Label lblTitulo = new Label("Registro de nueva estación");
        lblTitulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333;");

        VBox card = FormCard.create(20);

        card.getChildren().addAll(lblTitulo, construirFormulario(), construirBotones());

        ScrollPane scroll = new ScrollPane(card);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return scroll;
    }

    private VBox construirFormulario() {
        VBox formulario = new VBox(15);

        formulario.getChildren().addAll(
                crearLabelCampo("Ubicación:", txtUbicacion = texto("")),
                crearLabelCampo("ID del agricultor:", txtIdDueno = texto("")),
                crearLabelCampo("Estado operativo:", cboEstadoOperativo = comboEstados()),
                chkProblemas = new CheckBox("¿La estación presenta problemas?"),
                crearLabelCampo("Tipo de estación:", cboTipoEstacion = comboTipo()),
                panelEspecifico = crearPanelEspecifico()
        );

        chkProblemas.setStyle("-fx-font-weight: bold;");
        return formulario;
    }

    private Node construirBotones() {
        return ButtonGroup.create(
                "Crear estación", "#4caf50", () -> crearEstacion(),
                "Cancelar", "#f44336", () -> new MenuONGView(stage, usuario).show(),
                ButtonGroup.ButtonAlignment.RIGHT
        );
    }

    private Node crearLabelCampo(String label, Node campo) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-weight: bold;");
        VBox box = new VBox(5, lbl, campo);
        return box;
    }

    private TextField texto(String placeholder) {
        TextField txt = new TextField();
        txt.setPromptText(placeholder);
        txt.setMaxWidth(Double.MAX_VALUE);
        return txt;
    }

    private ComboBox<EstadoEstacion> comboEstados() {
        ComboBox<EstadoEstacion> combo = new ComboBox<>();
        combo.getItems().addAll(EstadoEstacion.values());
        combo.setValue(EstadoEstacion.PENDIENTE);
        combo.setMaxWidth(Double.MAX_VALUE);
        return combo;
    }

    private ComboBox<TipoEstacion> comboTipo() {
        ComboBox<TipoEstacion> combo = new ComboBox<>();
        combo.getItems().addAll(TipoEstacion.values());
        combo.setValue(TipoEstacion.FLORAL);
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setOnAction(e -> actualizarPanelEspecifico());
        
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

    private VBox crearPanelEspecifico() {
        txtFlores = texto("");
        txtNumPlantas = texto("");
        txtCapacidad = texto("");
        txtNivelActual = texto("");

        VBox panel = new VBox(12);
        panel.getChildren().add(construirCamposFloral());
        return panel;
    }

    private Node construirCamposFloral() {
        VBox floral = new VBox(10);
        floral.getChildren().addAll(
                crearLabelCampo("Tipo de flores:", txtFlores),
                crearLabelCampo("Número de plantas:", txtNumPlantas)
        );
        return floral;
    }

    private Node construirCamposHidrica() {
        VBox hidrica = new VBox(10);
        hidrica.getChildren().addAll(
                crearLabelCampo("Capacidad de agua (L):", txtCapacidad),
                crearLabelCampo("Nivel actual (L):", txtNivelActual)
        );
        return hidrica;
    }

    private void actualizarPanelEspecifico() {
        panelEspecifico.getChildren().clear();
        if (cboTipoEstacion.getValue() == TipoEstacion.FLORAL) {
            panelEspecifico.getChildren().add(construirCamposFloral());
        } else if (cboTipoEstacion.getValue() == TipoEstacion.HIDRICA) {
            panelEspecifico.getChildren().add(construirCamposHidrica());
        }
    }

    // ------------------------------------------------------------------------
    // Logic
    // ------------------------------------------------------------------------

    private void crearEstacion() {
        if (!validarFormulario()) {
            return;
        }

        try {
            String ubicacion = txtUbicacion.getText().trim();
            int idDueno = Integer.parseInt(txtIdDueno.getText().trim());
            EstadoEstacion estado = cboEstadoOperativo.getValue();
            boolean tieneProblemas = chkProblemas.isSelected();
            Date fechaInstalacion = new Date();

            Estacion estacion;

            if (cboTipoEstacion.getValue() == TipoEstacion.FLORAL) {
                String flores = txtFlores.getText().trim();
                int numPlantas = Integer.parseInt(txtNumPlantas.getText().trim());
                estacion = new EstacionFloral(
                        ubicacion,
                        fechaInstalacion,
                        null,
                        estado,
                        tieneProblemas,
                        idDueno,
                        flores,
                        numPlantas
                );
            } else if (cboTipoEstacion.getValue() == TipoEstacion.HIDRICA) {
                double capacidad = Double.parseDouble(txtCapacidad.getText().trim());
                double nivel = Double.parseDouble(txtNivelActual.getText().trim());

                if (nivel > capacidad) {
                    mostrarAlerta(Alert.AlertType.WARNING,
                            "Validación",
                            "El nivel actual no puede superar la capacidad.");
                    return;
                }

                estacion = new EstacionHidrica(
                        ubicacion,
                        fechaInstalacion,
                        null,
                        estado,
                        tieneProblemas,
                        idDueno,
                        capacidad,
                        nivel
                );
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione un tipo de estación.");
                return;
            }

            utilidadEstaciones.insertar(estacion);
            mostrarAlerta(Alert.AlertType.INFORMATION,"Éxito","La estación se registró correctamente.");
            new MenuONGView(stage, usuario).show();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING,"Validación","Algunos campos numéricos contienen valores inválidos.");
        }
    }

    private boolean validarFormulario() {
        if (txtUbicacion.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese la ubicación.");
            return false;
        }
        if (txtIdDueno.getText().trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese el ID del agricultor.");
            return false;
        }
        try {
            int id = Integer.parseInt(txtIdDueno.getText().trim());
            if (id <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El ID del agricultor debe ser un número válido.");
            return false;
        }

        if (cboTipoEstacion.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Seleccione el tipo de estación.");
            return false;
        }
        
        if (cboTipoEstacion.getValue() == TipoEstacion.FLORAL) {
            if (txtFlores.getText().trim().isEmpty() || txtNumPlantas.getText().trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Complete los campos de estación floral.");
                return false;
            }
            try {
                if (Integer.parseInt(txtNumPlantas.getText().trim()) <= 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Validación", "El número de plantas debe ser mayor a cero.");
                    return false;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese un número válido de plantas.");
                return false;
            }
        } else if (cboTipoEstacion.getValue() == TipoEstacion.HIDRICA) {
            if (txtCapacidad.getText().trim().isEmpty() || txtNivelActual.getText().trim().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Complete los campos de estación hídrica.");
                return false;
            }
            try {
                double capacidad = Double.parseDouble(txtCapacidad.getText().trim());
                double nivel = Double.parseDouble(txtNivelActual.getText().trim());
                if (capacidad <= 0 || nivel < 0) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Los valores de agua deben ser positivos.");
                    return false;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.WARNING, "Validación", "Ingrese valores numéricos válidos para el agua.");
                return false;
            }
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