package com.mdi.notas.controller;

import com.mdi.notas.App;
import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;
import javafx.fxml.FXML;

import java.util.List;

public class NotasController {

    private MainController main;

    public void setMain(MainController main) {
        this.main = main;
    }

    @FXML
    public void initialize() {
        // Nada aquí. Se inicializa cuando MainController llama a setMain().
    }

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
