package com.mdi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Controlador principal de la aplicación.
 * Gestiona la navegación entre vistas, mantiene el estado global
 * y sirve como punto central de comunicación entre los distintos subcontroladores.
 *
 * <p>Este controlador es cargado por MainView.fxml y actúa como
 * coordinador de todas las vistas incluidas mediante {@code fx:include}.</p>
 */
public class MainController {

    // ============================
    // VISTAS INCLUIDAS
    // ============================

    /** Vista principal de carpetas. */
    @FXML public VBox vistaCarpetas;

    /** Vista que muestra las notas de una carpeta. */
    @FXML public VBox vistaNotas;

    /** Vista para crear una nueva nota. */
    @FXML public VBox vistaNuevaNota;

    /** Vista para crear una nueva carpeta. */
    @FXML public VBox vistaNuevaCarpeta;

    /** Vista para visualizar una nota existente. */
    @FXML public VBox vistaVerNota;


    // ============================
    // SUBCONTROLADORES
    // ============================

    /** Controlador de la vista de carpetas. */
    @FXML public CarpetasController carpetasViewController;

    /** Controlador de la vista de notas. */
    @FXML public NotasController notasViewController;

    /** Controlador de la vista de favoritos. */
    @FXML public FavoritosController favoritosViewController;

    /** Controlador de la vista de visualización de nota. */
    @FXML public VistaNotaController verNotaViewController;


    // ============================
    // CONTROLES COMPARTIDOS
    // ============================

    /** Lista de carpetas mostrada en la vista principal. */
    @FXML public ListView<String> listaCarpetas;

    /** Lista de notas mostrada en la vista de notas. */
    @FXML public ListView<String> listaNotas;

    /** Botón de acción principal (crear nota o carpeta según contexto). */
    @FXML public Button btnAdd;

    /** Botón para volver a la vista anterior. */
    @FXML public Button btnBack;

    /** Título superior que indica la vista o carpeta actual. */
    @FXML public Label lblTitulo;

    /** Etiqueta inferior que muestra mensajes de estado. */
    @FXML public Label lblEstado;


    // ============================
    // CAMPOS DE NUEVA NOTA
    // ============================

    @FXML public TextField txtTituloNuevaNota;
    @FXML public TextArea txtContenidoNuevaNota;


    // ============================
    // CAMPOS DE NUEVA CARPETA
    // ============================

    @FXML public TextField txtNombreNuevaCarpeta;


    // ============================
    // CAMPOS DE VER NOTA
    // ============================

    @FXML public Label lblTituloNota;
    @FXML public TextArea txtContenidoNota;

    @FXML public Button btnEliminar;
    @FXML public Button btnEliminarDefinitivo;
    @FXML public Button btnFavorito;
    @FXML public Button btnQuitarFavorito;


    // ============================
    // ESTADO GLOBAL
    // ============================

    /** Carpeta actualmente seleccionada. */
    public String carpetaActual;

    /** Carpeta real donde se encuentra la nota (útil para eliminadas o favoritos). */
    public String carpetaRealDeLaNota;

    /** Título de la nota actualmente seleccionada. */
    public String notaActualTitulo;


    /**
     * Método llamado automáticamente por JavaFX al cargar MainView.fxml.
     * Se encarga de inyectar este controlador en los subcontroladores
     * y mostrar la vista inicial.
     */
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

    /**
     * Cambia la vista visible en pantalla.
     * Oculta todas las vistas y muestra únicamente la indicada.
     *
     * @param vista la vista que debe mostrarse
     */
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

    /**
     * Actualiza el mensaje de estado mostrado en la parte inferior de la interfaz.
     *
     * @param msg mensaje a mostrar
     */
    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }
}
