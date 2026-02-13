package com.mdi.controller;

import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

@Controller
public class MainController {

    // NODOS INYECTADOS POR LOS INCLUDES
    @FXML public VBox carpetasView;
    @FXML public VBox notasView;
    @FXML public VBox nuevaNotaView;
    @FXML public VBox nuevaCarpetaView;
    @FXML public VBox verNotaView;

    // CONTROLADORES (NO FXML)
    public CarpetasController carpetasViewController;
    public NotasController notasController;
    public VistaNotaController verNotaViewController;

    public FavoritosController favoritosViewController = new FavoritosController();

    // CONTROLES
    @FXML public ListView<String> listaNotas;
    @FXML public Button btnAdd;
    @FXML public Button btnBack;
    @FXML public Label lblTitulo;
    @FXML public Label lblEstado;
    @FXML public Button btnGuardarCarpeta;
    @FXML public TextField txtNombreNuevaCarpeta;
    @FXML public Label lblTituloNota;
    @FXML public TextArea txtContenidoNota;
    @FXML public Button btnEliminar;
    @FXML public Button btnEliminarDefinitivo;
    @FXML public Button btnFavorito;
    @FXML public Button btnQuitarFavorito;

    // ESTADO
    public String carpetaActual;
    public String carpetaRealDeLaNota;
    public String notaActualTitulo;

    @FXML
    public void initialize() {

        // Cargar controladores manualmente
        carpetasViewController = loadController("/CarpetasView.fxml");
        notasController = loadController("/NotasView.fxml");
        verNotaViewController = loadController("/VerNotaView.fxml");

        carpetasViewController.setMain(this);
        notasController.setMain(this);
        verNotaViewController.setMain(this);
        favoritosViewController.setMain(this);

        btnGuardarCarpeta.setOnAction(e -> carpetasViewController.guardarCarpeta());

        btnBack.setVisible(false);
        btnBack.setManaged(false);
        btnBack.setOnAction(e -> {
            lblTitulo.setText("Carpetas");
            btnAdd.setText("+");
            btnAdd.setOnAction(ev -> mostrarVista(nuevaCarpetaView));
            mostrarVista(carpetasView);
            carpetasViewController.inicializarCarpetas();
        });

        mostrarVista(carpetasView);
        carpetasViewController.inicializarCarpetas();
    }

    private <T> T loadController(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            loader.load();
            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void mostrarVista(VBox vista) {

        carpetasView.setVisible(false);
        carpetasView.setManaged(false);

        notasView.setVisible(false);
        notasView.setManaged(false);

        nuevaNotaView.setVisible(false);
        nuevaNotaView.setManaged(false);

        nuevaCarpetaView.setVisible(false);
        nuevaCarpetaView.setManaged(false);

        verNotaView.setVisible(false);
        verNotaView.setManaged(false);

        vista.setVisible(true);
        vista.setManaged(true);
    }

    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }
}
