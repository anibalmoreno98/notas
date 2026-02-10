package com.mdi.notas.controller;

import com.mdi.notas.App;
import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;

import java.util.List;

public class FavoritosController {

    private MainController main;

    public void setMain(MainController main) {
        this.main = main;
    }

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
