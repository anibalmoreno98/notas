package com.mdi.controller;

import com.mdi.App;
import com.mdi.model.Nota;
import com.mdi.service.NotaService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class NotasController {

    @FXML public VBox root;            // ← NODO RAÍZ DEL FXML
    @FXML public ListView<String> listaNotas;

    @FXML private TextField txtTituloNuevaNota;
    @FXML private TextArea txtContenidoNuevaNota;

    private MainController main;

    public void setMain(MainController main) {
        this.main = main;
    }

    @FXML
    public void initialize() {
        // vacío a propósito
    }

    public void cargarNotas(String carpeta) {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            List<Nota> notas = service.listarPorCarpeta(carpeta);

            listaNotas.getItems().clear();
            notas.forEach(n -> listaNotas.getItems().add(n.getTitulo()));

            listaNotas.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    String titulo = listaNotas.getSelectionModel().getSelectedItem();
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
            n.setTitulo(txtTituloNuevaNota.getText());
            n.setContenido(txtContenidoNuevaNota.getText());
            n.setCarpeta(main.carpetaActual);

            service.guardar(n);

            main.setEstado("Nota guardada");

            txtTituloNuevaNota.clear();
            txtContenidoNuevaNota.clear();

            cargarNotas(main.carpetaActual);
            main.mostrarVista(main.notasView);

            main.btnBack.setVisible(true);
            main.btnBack.setManaged(true);

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
