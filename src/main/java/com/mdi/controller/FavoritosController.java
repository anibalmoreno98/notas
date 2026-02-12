package com.mdi.controller;

import com.mdi.App;
import com.mdi.model.Nota;
import com.mdi.service.NotaService;

import java.util.List;

/**
 * Controlador encargado de gestionar la vista de notas marcadas como favoritas.
 * Se comunica con {@link MainController} para actualizar la interfaz y con
 * {@link NotaService} para obtener y modificar las notas almacenadas.
 */
public class FavoritosController {

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
     * Carga todas las notas marcadas como favoritas y actualiza la lista visual.
     * También configura el evento de doble clic para abrir una nota seleccionada.
     */
    public void cargarFavoritos() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            List<Nota> notas = service.listarTodas();

            main.listaNotas.getItems().clear();

            notas.stream()
                    .filter(Nota::isFavorita)
                    .forEach(n -> main.listaNotas.getItems().add(n.getTitulo()));

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
            main.setEstado("Error cargando favoritos");
        }
    }

    /**
     * Marca la nota actualmente abierta como favorita.
     * Actualiza el estado y guarda el cambio mediante el servicio correspondiente.
     */
    public void marcarFavorito() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            Nota nota = service.buscarEnTodas(main.notaActualTitulo);

            if (nota == null) {
                main.setEstado("No se encontró la nota para marcar como favorita");
                return;
            }

            nota.setFavorita(true);
            service.guardar(nota);

            main.setEstado("Añadida a favoritos");

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error al añadir a favoritos");
        }
    }

    /**
     * Quita la marca de favorito de la nota actualmente abierta.
     * Si el usuario está en la carpeta "Favoritos", la lista se refresca automáticamente.
     */
    public void desmarcarFavorito() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            Nota nota = service.buscarEnTodas(main.notaActualTitulo);

            if (nota == null) {
                main.setEstado("No se encontró la nota para quitar de favoritos");
                return;
            }

            nota.setFavorita(false);
            service.guardar(nota);

            main.setEstado("Quitada de favoritos");

            // Si estamos en la carpeta Favoritos, refrescar la lista
            if ("Favoritos".equals(main.carpetaActual)) {
                cargarFavoritos();
            }

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error al quitar de favoritos");
        }
    }
}
