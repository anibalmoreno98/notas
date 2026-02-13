package com.mdi.controller;

import com.mdi.App;
import com.mdi.service.CarpetaService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class CarpetasController {

    @FXML public VBox root;
    @FXML private ListView<String> listaCarpetas;

    private MainController main;

    public void setMain(MainController main) {
        this.main = main;
    }

    @FXML
    public void initialize() {
        inicializarCarpetas();
    }

    public void inicializarCarpetas() {
        listaCarpetas.getItems().setAll(
                "General",
                "Eliminadas",
                "Favoritos"
        );

        listaCarpetas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, nueva) -> {
                    if (nueva != null) abrirCarpeta(nueva);
                }
        );
    }

    private void abrirCarpeta(String carpeta) {

        main.carpetaActual = carpeta;
        main.lblTitulo.setText(carpeta);
        main.setEstado("Carpeta abierta: " + carpeta);

        main.mostrarVista(main.notasView);

        main.btnBack.setVisible(true);
        main.btnBack.setManaged(true);
        main.btnBack.setOnAction(e ->
                main.mostrarVista(main.carpetasView)
        );

        main.btnAdd.setText("+");
        main.btnAdd.setOnAction(e -> main.mostrarVista(main.nuevaNotaView));

        if ("Favoritos".equals(carpeta)) {
            main.favoritosViewController.cargarFavoritos();
        } else {
            main.notasController.cargarNotas(carpeta);
        }
    }

    @FXML
    public void crearCarpeta() {
        main.mostrarVista(main.nuevaCarpetaView);
    }

    @FXML
    public void guardarCarpeta() {
        try {
            String nombre = main.txtNombreNuevaCarpeta.getText().trim();
            if (nombre.isEmpty()) {
                main.setEstado("El nombre no puede estar vacío");
                return;
            }

            CarpetaService service = App.getContext().getBean(CarpetaService.class);
            service.crear(nombre);

            listaCarpetas.getItems().add(nombre);
            main.setEstado("Carpeta creada: " + nombre);

            main.mostrarVista(main.carpetasView);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error creando carpeta");
        }
    }
}
