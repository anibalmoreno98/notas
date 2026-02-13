package com.mdi.controller;

import com.mdi.App;
import com.mdi.model.Nota;
import com.mdi.service.NotaService;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * Controlador encargado de gestionar la vista de notas marcadas como favoritas.
 * Se comunica con {@link MainController} para actualizar la interfaz y con
 * {@link NotaService} para obtener y modificar las notas almacenadas.
 */
@Controller
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

}
