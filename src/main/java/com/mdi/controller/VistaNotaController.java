package com.mdi.controller;

import org.springframework.stereotype.Controller;

import com.mdi.App;
import com.mdi.model.Nota;
import com.mdi.service.NotaService;

import javafx.fxml.FXML;

@Controller
public class VistaNotaController {

    private MainController main;

    public void setMain(MainController main) {
        this.main = main;
    }

    public void abrirNota(String titulo) {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            Nota nota;

            if ("Favoritos".equals(main.carpetaActual)) {
                nota = service.buscarEnTodas(titulo);
            } else {
                nota = service.buscarPorTitulo(main.carpetaActual, titulo);
            }

            if (nota == null) {
                main.setEstado("No se pudo abrir la nota");
                return;
            }

            main.notaActualTitulo = titulo;
            main.carpetaRealDeLaNota = nota.getCarpeta();

            main.lblTituloNota.setText(nota.getTitulo());
            main.txtContenidoNota.setText(nota.getContenido());

            boolean esFavorita = nota.isFavorita();

            main.btnFavorito.setVisible(!esFavorita);
            main.btnFavorito.setManaged(!esFavorita);

            main.btnQuitarFavorito.setVisible(esFavorita);
            main.btnQuitarFavorito.setManaged(esFavorita);

            if ("Eliminadas".equals(main.carpetaActual)) {
                main.btnEliminar.setVisible(false);
                main.btnEliminar.setManaged(false);

                main.btnEliminarDefinitivo.setVisible(true);
                main.btnEliminarDefinitivo.setManaged(true);
            } else {
                main.btnEliminar.setVisible(true);
                main.btnEliminar.setManaged(true);

                main.btnEliminarDefinitivo.setVisible(false);
                main.btnEliminarDefinitivo.setManaged(false);
            }

            main.mostrarVista(main.verNotaView);
            main.setEstado("Leyendo nota: " + titulo);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error abriendo nota");
        }
    }

    public void eliminarDefinitivamente() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);

            service.borrarDefinitivamente(main.carpetaRealDeLaNota, main.notaActualTitulo);

            main.setEstado("Nota eliminada definitivamente");

            // Recargar la carpeta Eliminadas
            main.notasController.cargarNotas("Eliminadas");  // ← CORREGIDO

            // Volver a la vista de notas
            main.mostrarVista(main.notasView);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error eliminando definitivamente");
        }
    }

    @FXML
    public void eliminarNotaActual() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);

            Nota nota = service.buscarPorTitulo(main.carpetaRealDeLaNota, main.notaActualTitulo);
            service.moverAEliminadas(nota);

            main.setEstado("Nota movida a Eliminadas");

            main.notasController.cargarNotas(main.carpetaActual);  // ← CORREGIDO
            main.mostrarVista(main.notasView);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error eliminando nota");
        }
    }

    @FXML
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

            main.btnFavorito.setVisible(false);
            main.btnFavorito.setManaged(false);

            main.btnQuitarFavorito.setVisible(true);
            main.btnQuitarFavorito.setManaged(true);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error al añadir a favoritos");
        }
    }

    @FXML
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

            main.btnFavorito.setVisible(true);
            main.btnFavorito.setManaged(true);

            main.btnQuitarFavorito.setVisible(false);
            main.btnQuitarFavorito.setManaged(false);

            if ("Favoritos".equals(main.carpetaActual)) {
                main.favoritosViewController.cargarFavoritos();
            }

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error al quitar de favoritos");
        }
    }
}
