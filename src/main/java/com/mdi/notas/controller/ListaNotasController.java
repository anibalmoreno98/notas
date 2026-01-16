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

@Controller
public class ListaNotasController {

    private final NotaService service;

    private NotaFxController mainController;

    public void setMainController(NotaFxController mainController) {
        this.mainController = mainController;
    }

    public ListaNotasController(NotaService service) {
        this.service = service;
    }

    @FXML
    private ListView<String> listaNotas;

    @FXML
    public void initialize() {
        try {
            listaNotas.getItems().clear();
            service.listar().forEach(n -> listaNotas.getItems().add(n.getTitulo()));

            listaNotas.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    abrirNotaSeleccionada();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            }

            Stage stage = new Stage();
            stage.setTitle("Nota: " + nota.getTitulo());
            stage.setScene(new Scene(root, 500, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void volverAtras() {
        if (mainController != null) {
            mainController.volverAtras();
        }
    }
}
