package com.mdi.notas.controller;

import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

/**
 * Controlador encargado de gestionar la ventana que muestra la lista de notas.
 * Permite visualizar todas las notas ordenadas por fecha de creación (ID descendente),
 * abrir una nota mediante doble clic y volver a la pantalla principal del MDI.
 */
@Controller
public class ListaNotasController {

    /** Servicio encargado de gestionar las operaciones CRUD de las notas. */
    private final Carpeta service;

    /** Referencia al controlador principal del MDI para comunicación entre pantallas. */
    private NotaFxController mainController;

    /**
     * Establece el controlador principal para permitir comunicación con el MDI.
     *
     * @param mainController instancia del controlador principal
     */
    public void setMainController(NotaFxController mainController) {
        this.mainController = mainController;
    }

    /**
     * Constructor que recibe el servicio de notas mediante inyección de dependencias.
     *
     * @param service servicio de gestión de notas
     */
    public ListaNotasController(Carpeta service) {
        this.service = service;
    }

    @FXML
    private ListView<String> listaNotas;

    /**
     * Inicializa la lista de notas.
     * Carga todas las notas desde la base de datos, las ordena por ID descendente
     * y configura el evento de doble clic para abrir una nota.
     */
    @FXML
    public void initialize() {
        try {
            listaNotas.getItems().clear();

            var notas = service.listar();
            notas.sort((a, b) -> Long.compare(b.getId(), a.getId()));

            notas.forEach(n -> listaNotas.getItems().add(n.getTitulo()));

            listaNotas.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    abrirNotaSeleccionada();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre una nota seleccionada en una nueva ventana.
     * Carga el archivo FXML correspondiente, asigna los valores de la nota
     * y actualiza el contenido activo en el controlador principal.
     */
    private void abrirNotaSeleccionada() {
        try {
            String titulo = listaNotas.getSelectionModel().getSelectedItem();
            if (titulo == null) return;

            Nota nota = service.buscarPorTitulo(titulo);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VerNota.fxml"));
            Parent root = loader.load();

            Label lblTitulo = (Label) root.lookup("#lblTitulo");
            TextArea txtContenido = (TextArea) root.lookup("#txtContenido");

            if (lblTitulo != null) lblTitulo.setText(nota.getTitulo());
            if (txtContenido != null) txtContenido.setText(nota.getContenido());

            if (mainController != null) {
                mainController.setEstado("Nota cargada");
                mainController.setContenidoActual(nota.getContenido());
            }

            Stage stage = new Stage();
            stage.setTitle("Nota: " + nota.getTitulo());
            stage.setScene(new Scene(root, 500, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Vuelve a la pantalla principal del MDI.
     * Este método se ejecuta al pulsar el botón "Volver atrás".
     */
    @FXML
    public void volverAtras() {
        if (mainController != null) {
            mainController.volverAtras();
        }
    }
}

