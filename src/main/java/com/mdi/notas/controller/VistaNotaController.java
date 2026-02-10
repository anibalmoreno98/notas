package com.mdi.notas.controller;

import com.mdi.notas.App;
import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;

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

            // Mostrar botones según carpeta
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

            main.mostrarVista(main.vistaVerNota);
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
            main.notasViewController.cargarNotas("Eliminadas");

            // Volver a la vista de notas
            main.mostrarVista(main.vistaNotas);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error eliminando definitivamente");
        }
    }
}
