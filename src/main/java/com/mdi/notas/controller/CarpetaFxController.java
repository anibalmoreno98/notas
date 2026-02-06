package com.mdi.notas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Controller;

import com.mdi.notas.App;
import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;

import java.util.List;

@Controller
public class CarpetaFxController {

    @FXML
    private Label lblTitulo;

    @FXML
    private Button btnAdd;

    @FXML
    private StackPane panelCentral;

    @FXML
    private ListView<String> listaCarpetas;

    @FXML
    private VBox vistaNotas;

    @FXML
    private ListView<String> listaNotas;

    @FXML
    private Label lblEstado;

    private String carpetaActual = null;

    @FXML
    public void initialize() {
        inicializarCarpetas();
        mostrarVistaCarpetas();
    }

    private void inicializarCarpetas() {
        listaCarpetas.getItems().setAll(
                "General",
                "Eliminadas",
                "Favoritos"
        );

        listaCarpetas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, nueva) -> {
                    if (nueva != null) {
                        abrirCarpeta(nueva);
                    }
                }
        );
    }

    private void abrirCarpeta(String carpeta) {
        carpetaActual = carpeta;

        lblTitulo.setText(carpeta);
        setEstado("Carpeta abierta: " + carpeta);

        btnAdd.setText("+");
        btnAdd.setOnAction(e -> crearNota());

        mostrarVistaNotas();
        cargarNotasDeCarpeta(carpeta);
    }

    private void cargarNotasDeCarpeta(String carpeta) {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            List<Nota> notas = service.listarPorCarpeta(carpeta);

            listaNotas.getItems().clear();
            for (Nota n : notas) {
                listaNotas.getItems().add(n.getTitulo());
            }

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error cargando notas");
        }
    }

    private void crearNota() {
        setEstado("Crear nueva nota en " + carpetaActual);
        // cargarVentana("/FormNueva.fxml");
    }

    @FXML
    public void volverAtras() {
        mostrarVistaCarpetas();
        lblTitulo.setText("Carpetas");
        btnAdd.setText("+");
        btnAdd.setOnAction(e -> crearCarpeta());
        setEstado("Volviste a carpetas");
    }

    private void crearCarpeta() {
        setEstado("Crear nueva carpeta");
        // cargarVentana("/FormCarpeta.fxml");
    }

    private void mostrarVistaCarpetas() {
        listaCarpetas.setVisible(true);
        listaCarpetas.setManaged(true);

        vistaNotas.setVisible(false);
        vistaNotas.setManaged(false);
    }

    private void mostrarVistaNotas() {
        listaCarpetas.setVisible(false);
        listaCarpetas.setManaged(false);

        vistaNotas.setVisible(true);
        vistaNotas.setManaged(true);
    }

    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }
}
