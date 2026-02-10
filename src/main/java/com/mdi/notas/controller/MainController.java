package com.mdi.notas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MainController {

    // Vistas incluidas
    @FXML public VBox vistaCarpetas;
    @FXML public VBox vistaNotas;
    @FXML public VBox vistaNuevaNota;
    @FXML public VBox vistaNuevaCarpeta;
    @FXML public VBox vistaVerNota;

    // Controladores de las vistas incluidas
    @FXML public CarpetasController carpetasViewController;
    @FXML public NotasController notasViewController;
    @FXML public FavoritosController favoritosViewController;
    @FXML public VistaNotaController verNotaViewController;

    // Controles compartidos
    @FXML public ListView<String> listaCarpetas;
    @FXML public ListView<String> listaNotas;

    @FXML public Button btnAdd;
    @FXML public Button btnBack;

    @FXML public Label lblTitulo;
    @FXML public Label lblEstado;

    // Campos de Nueva Nota
    @FXML public TextField txtTituloNuevaNota;
    @FXML public TextArea txtContenidoNuevaNota;

    // Campos de Nueva Carpeta
    @FXML public TextField txtNombreNuevaCarpeta;

    // Campos de Ver Nota
    @FXML public Label lblTituloNota;
    @FXML public TextArea txtContenidoNota;

    @FXML public Button btnEliminar;
    @FXML public Button btnEliminarDefinitivo;
    @FXML public Button btnFavorito;
    @FXML public Button btnQuitarFavorito;

    // Estado global
    public String carpetaActual;
    public String carpetaRealDeLaNota;
    public String notaActualTitulo;

    @FXML
    public void initialize() {

        // Inyectar MainController en los subcontroladores
        carpetasViewController.setMain(this);
        notasViewController.setMain(this);
        favoritosViewController.setMain(this);
        verNotaViewController.setMain(this);

        // Mostrar vista inicial
        mostrarVista(vistaCarpetas);
    }

    public void mostrarVista(VBox vista) {
        vistaCarpetas.setVisible(false);
        vistaNotas.setVisible(false);
        vistaNuevaNota.setVisible(false);
        vistaNuevaCarpeta.setVisible(false);
        vistaVerNota.setVisible(false);

        vistaCarpetas.setManaged(false);
        vistaNotas.setManaged(false);
        vistaNuevaNota.setManaged(false);
        vistaNuevaCarpeta.setManaged(false);
        vistaVerNota.setManaged(false);

        vista.setVisible(true);
        vista.setManaged(true);
    }

    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }
}
