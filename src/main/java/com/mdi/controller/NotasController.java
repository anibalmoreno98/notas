package com.mdi.controller;

import com.mdi.App;
import com.mdi.model.Nota;
import com.mdi.service.NotaService;
import javafx.fxml.FXML;

import java.util.List;

/**
 * Controlador encargado de gestionar la vista de notas dentro de una carpeta.
 * Se comunica con {@link MainController} para actualizar la interfaz y con
 * {@link NotaService} para realizar operaciones de negocio sobre las notas.
 */
public class NotasController {

    /** Referencia al controlador principal, inyectada por MainController. */
    private MainController main;

    /**
     * Asigna el controlador principal para permitir la comunicación con la interfaz.
     *
     * @param main instancia del controlador principal
     */
    public void setMain(MainController main) {
        this.main = main;
    }

    /**
     * Método llamado automáticamente por JavaFX al cargar el FXML.
     * No se utiliza porque la inicialización depende de que MainController
     * haya inyectado previamente todos los nodos.
     */
    @FXML
    public void initialize() {
        // Intencionalmente vacío
    }

    /**
     * Carga todas las notas pertenecientes a una carpeta específica y actualiza la lista visual.
     * También configura el evento de doble clic para abrir una nota seleccionada.
     *
     * @param carpeta nombre de la carpeta cuyas notas deben mostrarse
     */
    public void cargarNotas(String carpeta) {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            List<Nota> notas = service.listarPorCarpeta(carpeta);

            main.listaNotas.getItems().clear();
            notas.forEach(n -> main.listaNotas.getItems().add(n.getTitulo()));

            main.listaNotas.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    String titulo = main.listaNotas.getSelectionModel().getSelectedItem();
                    if (titulo != null) {
                        main.verNotaViewController.abrirNota(titulo);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error cargando notas");
        }
    }

    /**
     * Guarda una nueva nota utilizando los datos introducidos en la interfaz.
     * Tras guardar, actualiza la lista de notas y vuelve a la vista principal.
     */
    @FXML
    public void guardarNota() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);

            Nota n = new Nota();
            n.setTitulo(main.txtTituloNuevaNota.getText());
            n.setContenido(main.txtContenidoNuevaNota.getText());
            n.setCarpeta(main.carpetaActual);

            service.guardar(n);

            main.setEstado("Nota guardada");

            cargarNotas(main.carpetaActual);
            main.mostrarVista(main.vistaNotas);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error guardando nota");
        }
    }

    /**
     * Elimina (o mueve a Eliminadas) la nota actualmente abierta.
     * Actualiza la lista de notas tras completar la operación.
     */
    public void eliminarNotaActual() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);

            Nota nota = service.buscarPorTitulo(main.carpetaRealDeLaNota, main.notaActualTitulo);
            service.moverAEliminadas(nota);

            main.setEstado("Nota movida a Eliminadas");
            cargarNotas(main.carpetaActual);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error eliminando nota");
        }
    }
}
