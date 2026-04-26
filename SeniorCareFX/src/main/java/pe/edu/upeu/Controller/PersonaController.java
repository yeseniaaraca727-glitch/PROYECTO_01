package pe.edu.upeu.Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.enums.EstadoSalud;
import pe.edu.upeu.enums.TipoPension;
import pe.edu.upeu.model.Persona;
import pe.edu.upeu.service.PersonaService;
import pe.edu.upeu.service.PersonaServiceImpl;

// Esta clase controla toda la lógica de la interfaz (lo que el usuario hace)
public class PersonaController {

    // Campos del formulario
    @FXML private TextField txtNombre, txtEdad, txtCurp, txtDomicilio;

    // Campos para filtros por edad
    @FXML private TextField txtEdadMin, txtEdadMax;

    // ComboBox para seleccionar tipo de pensión y estado de salud
    @FXML private ComboBox<TipoPension> cbTipoPension, cbFiltroTipoPension;
    @FXML private ComboBox<EstadoSalud> cbEstadoSalud;

    // Tabla donde se muestran los registros
    @FXML private TableView<Persona> tblPersonas;

    // Columnas de la tabla
    @FXML private TableColumn<Persona, Integer> colId;
    @FXML private TableColumn<Persona, String> colNombre, colCurp, colDomicilio;
    @FXML private TableColumn<Persona, Integer> colEdad;
    @FXML private TableColumn<Persona, TipoPension> colTipoPension;
    @FXML private TableColumn<Persona, EstadoSalud> colEstadoSalud;

    // Label para mostrar mensajes rápidos
    @FXML private Label lblMensaje;

    // Servicio que conecta con la lógica del sistema
    private final PersonaService service = new PersonaServiceImpl();

    // Guarda la persona seleccionada en la tabla
    private Persona personaSeleccionada;

    @FXML
    public void initialize() {

        // Enlazamos cada columna con el atributo correspondiente de la clase Persona
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colDomicilio.setCellValueFactory(new PropertyValueFactory<>("domicilio"));
        colTipoPension.setCellValueFactory(new PropertyValueFactory<>("tipoPension"));
        colEstadoSalud.setCellValueFactory(new PropertyValueFactory<>("estadoSalud"));

        // Cargamos los valores de los enums en los ComboBox
        cbTipoPension.setItems(FXCollections.observableArrayList(TipoPension.values()));
        cbFiltroTipoPension.setItems(FXCollections.observableArrayList(TipoPension.values()));
        cbEstadoSalud.setItems(FXCollections.observableArrayList(EstadoSalud.values()));

        // Cargamos los datos iniciales en la tabla
        cargarTabla();

        // Listener: cuando selecciono una fila, llena automáticamente el formulario
        tblPersonas.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                personaSeleccionada = sel;

                txtNombre.setText(sel.getNombreCompleto());
                txtEdad.setText(String.valueOf(sel.getEdad()));
                txtCurp.setText(sel.getCurp());
                txtDomicilio.setText(sel.getDomicilio());
                cbTipoPension.setValue(sel.getTipoPension());
                cbEstadoSalud.setValue(sel.getEstadoSalud());
            }
        });
    }

    // Método que carga todos los registros en la tabla
    private void cargarTabla() {
        tblPersonas.setItems(FXCollections.observableArrayList(service.listarTodos()));
    }

    // MÉTODO GUARDAR (Create)
    @FXML
    private void onGuardar() {
        try {
            // Validamos que ningún campo esté vacío
            if (txtNombre.getText().isEmpty() ||
                    txtEdad.getText().isEmpty() ||
                    txtCurp.getText().isEmpty() ||
                    txtDomicilio.getText().isEmpty() ||
                    cbTipoPension.getValue() == null ||
                    cbEstadoSalud.getValue() == null) {

                mostrarAlerta("Todos los campos son obligatorios", Alert.AlertType.WARNING);
                return;
            }

            int edad = Integer.parseInt(txtEdad.getText());

            // Validamos que sea una persona de la tercera edad
            if (edad < 60) {
                mostrarAlerta("La edad debe ser mayor o igual a 60", Alert.AlertType.WARNING);
                return;
            }

            // Validación simple de CURP
            if (txtCurp.getText().length() < 10) {
                mostrarAlerta("CURP inválido", Alert.AlertType.WARNING);
                return;
            }

            // Creamos el objeto Persona con los datos ingresados
            Persona p = new Persona(
                    0,
                    txtNombre.getText(),
                    edad,
                    txtCurp.getText(),
                    txtDomicilio.getText(),
                    cbTipoPension.getValue(),
                    cbEstadoSalud.getValue()
            );

            // Guardamos en el sistema
            service.guardar(p);

            // Actualizamos tabla y limpiamos formulario
            cargarTabla();
            limpiarFormulario();

            mostrarAlerta("✔ Persona registrada correctamente", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            // Error si edad no es número
            mostrarAlerta("La edad debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // MÉTODO ACTUALIZAR (Update)
    @FXML
    private void onActualizar() {

        // Verificamos que haya una selección
        if (personaSeleccionada == null) {
            mostrarMensaje("Seleccione una persona.");
            return;
        }

        // Actualizamos los datos con lo que hay en el formulario
        personaSeleccionada.setNombreCompleto(txtNombre.getText());
        personaSeleccionada.setEdad(Integer.parseInt(txtEdad.getText()));
        personaSeleccionada.setCurp(txtCurp.getText());
        personaSeleccionada.setDomicilio(txtDomicilio.getText());
        personaSeleccionada.setTipoPension(cbTipoPension.getValue());
        personaSeleccionada.setEstadoSalud(cbEstadoSalud.getValue());

        // Guardamos cambios
        service.actualizar(personaSeleccionada);

        cargarTabla();
        limpiarFormulario();

        mostrarMensaje("✔ Registro actualizado.");
    }

    // MÉTODO ELIMINAR (Delete)
    @FXML
    private void onEliminar() {

        if (personaSeleccionada == null) {
            mostrarAlerta("Seleccione una persona.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación antes de eliminar
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Seguro que deseas eliminar este registro?");

        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.OK) {

                service.eliminar(personaSeleccionada.getId());

                cargarTabla();
                limpiarFormulario();

                mostrarAlerta("✔ Registro eliminado.", Alert.AlertType.INFORMATION);
            }
        });
    }

    // FILTRO SIMPLE POR EDAD
    @FXML
    private void onFiltrarEdad() {
        try {
            int min = Integer.parseInt(txtEdadMin.getText());
            int max = Integer.parseInt(txtEdadMax.getText());

            tblPersonas.setItems(
                    FXCollections.observableArrayList(service.filtrarPorEdad(min, max))
            );

            mostrarMensaje("Filtrado por edad: " + min + " - " + max);

        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese valores numéricos válidos.");
        }
    }

    // FILTRO POR TIPO DE PENSIÓN
    @FXML
    private void onFiltrarPension() {

        TipoPension tipo = cbFiltroTipoPension.getValue();

        if (tipo == null) {
            mostrarMensaje("Seleccione un tipo de pensión.");
            return;
        }

        tblPersonas.setItems(
                FXCollections.observableArrayList(service.filtrarPorPension(tipo))
        );

        mostrarMensaje("Filtrado por pensión: " + tipo);
    }

    // LIMPIAR FILTROS
    @FXML
    private void onLimpiarFiltros() {
        txtEdadMin.clear();
        txtEdadMax.clear();
        cbFiltroTipoPension.setValue(null);

        cargarTabla();

        mostrarMensaje("Filtros eliminados.");
    }

    // FILTRO AVANZADO (edad + pensión al mismo tiempo)
    @FXML
    private void onFiltrarAvanzado() {
        try {
            String edadMinTxt = txtEdadMin.getText();
            String edadMaxTxt = txtEdadMax.getText();
            TipoPension tipo = cbFiltroTipoPension.getValue();

            int min = edadMinTxt.isEmpty() ? 0 : Integer.parseInt(edadMinTxt);
            int max = edadMaxTxt.isEmpty() ? 200 : Integer.parseInt(edadMaxTxt);

            var lista = service.listarTodos();

            // Uso de streams para filtrar de forma más limpia
            var filtrada = lista.stream()
                    .filter(p -> p.getEdad() >= min && p.getEdad() <= max)
                    .filter(p -> tipo == null || p.getTipoPension() == tipo)
                    .toList();

            tblPersonas.setItems(FXCollections.observableArrayList(filtrada));

            mostrarAlerta("Filtro aplicado correctamente", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error en filtros", Alert.AlertType.ERROR);
        }
    }

    // Limpia todos los campos del formulario
    private void limpiarFormulario() {
        txtNombre.clear();
        txtEdad.clear();
        txtCurp.clear();
        txtDomicilio.clear();
        cbTipoPension.setValue(null);
        cbEstadoSalud.setValue(null);
        personaSeleccionada = null;
    }

    // Muestra alertas (ventanas emergentes)
    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }

    // Muestra mensajes en el label inferior
    private void mostrarMensaje(String msg) {
        lblMensaje.setText(msg);
    }
}