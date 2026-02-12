package com.mdi.controller;

import com.mdi.App;
import com.mdi.model.Nota;
import com.mdi.service.NotaService;

/**
 * Controlador responsable de gestionar la visualización de una nota.
 * Se encarga de cargar su contenido, actualizar la interfaz según el estado
 * de la nota (favorita, eliminada, etc.) y ejecutar acciones como la eliminación definitiva.
 */
public class VistaNotaController {

    /** Controlador principal que coordina todas las vistas. */
    private MainController main;

    /**
     * Asigna el controlador principal para permitir la comunicación entre vistas.
     *
     * @param main instancia del controlador principal
     */
    public void setMain(MainController main) {
        this.main = main;
    }

    /**
     * Abre una nota existente y actualiza la interfaz con su contenido.
     * Determina si la nota pertenece a Favoritos o a una carpeta normal,
     * ajusta los botones visibles según el contexto y cambia a la vista de lectura.
     *
     * @param titulo título de la nota que se desea abrir
     */
    public void abrirNota(String titulo) {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            Nota nota;

            // Buscar la nota según la carpeta actual
            if ("Favoritos".equals(main.carpetaActual)) {
                nota = service.buscarEnTodas(titulo);
            } else {
                nota = service.buscarPorTitulo(main.carpetaActual, titulo);
            }

            if (nota == null) {
                main.setEstado("No se pudo abrir la nota");
                return;
            }

            // Actualizar estado global
            main.notaActualTitulo = titulo;
            main.carpetaRealDeLaNota = nota.getCarpeta();

            // Mostrar contenido
            main.lblTituloNota.setText(nota.getTitulo());
            main.txtContenidoNota.setText(nota.getContenido());

            // Mostrar botones según si es favorita
            boolean esFavorita = nota.isFavorita();

            main.btnFavorito.setVisible(!esFavorita);
            main.btnFavorito.setManaged(!esFavorita);

            main.btnQuitarFavorito.setVisible(esFavorita);
            main.btnQuitarFavorito.setManaged(esFavorita);

            // Mostrar botones según si está en Eliminadas
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

            // Cambiar a la vista de lectura
            main.mostrarVista(main.vistaVerNota);
            main.setEstado("Leyendo nota: " + titulo);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error abriendo nota");
        }
    }

    /**
     * Elimina una nota de forma permanente.
     * Se utiliza únicamente cuando la nota se encuentra en la carpeta "Eliminadas".
     * Tras eliminarla, recarga la lista de notas y vuelve a la vista anterior.
     */
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
