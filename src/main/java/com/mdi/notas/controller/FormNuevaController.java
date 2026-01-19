package com.mdi.notas.controller;

import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

/**
 * Controlador encargado de gestionar el formulario para crear nuevas notas.
 * Permite introducir un título y contenido, guardar la nota en la base de datos
 * y notificar al controlador principal para actualizar la interfaz.
 */
@Controller
public class FormNuevaController {

    /** Servicio encargado de gestionar las operaciones CRUD de las notas. */
    private final NotaService service;

    /** Referencia al controlador principal del MDI para comunicación entre pantallas. */
    private NotaFxController mainController;

    /**
     * Establece el controlador principal para permitir comunicación con el MDI.
     *
     * @param controller instancia del controlador principal
     */
    public void setMainController(NotaFxController controller) {
        this.mainController = controller;
    }

    /**
     * Constructor que recibe el servicio de notas mediante inyección de dependencias.
     *
     * @param service servicio de gestión de notas
     */
    public FormNuevaController(NotaService service) {
        this.service = service;
    }

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextArea txtContenido;

    /**
     * Guarda una nueva nota utilizando los datos introducidos en el formulario.
     * Tras guardar:
     * <ul>
     *     <li>Limpia los campos del formulario</li>
     *     <li>Actualiza la nota activa en el controlador principal</li>
     *     <li>Refresca la última nota mostrada en la pantalla principal</li>
     *     <li>Vuelve automáticamente a la pantalla principal del MDI</li>
     * </ul>
     */
    @FXML
    public void guardarNota() {
        try {
            Nota n = new Nota();
            n.setTitulo(txtTitulo.getText());
            n.setContenido(txtContenido.getText());

            service.guardar(n);

            txtTitulo.clear();
            txtContenido.clear();

            if (mainController != null) {
                mainController.setContenidoActual(n.getContenido());
                mainController.refrescarUltimaNota();
                mainController.volverAtras();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Vuelve a la pantalla principal del MDI sin guardar cambios.
     * Este método se ejecuta al pulsar el botón "Volver atrás".
     */
    @FXML
    public void volverAtras() {
        if (mainController != null) {
            mainController.volverAtras();
        }
    }
}
