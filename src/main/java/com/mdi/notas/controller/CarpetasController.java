package com.mdi.notas.controller;

import com.mdi.notas.App;
import com.mdi.notas.service.CarpetaService;
import javafx.fxml.FXML;

public class CarpetasController {

    private MainController main;

    public void setMain(MainController main) {
        this.main = main;
        inicializarCarpetas(); // Se ejecuta cuando MainController ya está listo
    }

    @FXML
    public void initialize() {
        // No hacemos nada aquí porque main todavía no está asignado.
    }

    /**
     * Inicializa la lista de carpetas y su listener.
     * Se ejecuta cuando MainController ya ha inyectado todos los nodos.
     */
    private void inicializarCarpetas() {
        main.listaCarpetas.getItems().setAll(
                "General",
                "Eliminadas",
                "Favoritos"
        );

        main.listaCarpetas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, nueva) -> {
                    if (nueva != null) abrirCarpeta(nueva);
                }
        );
    }

    private void abrirCarpeta(String carpeta) {
        main.carpetaActual = carpeta;
        main.lblTitulo.setText(carpeta);
        main.setEstado("Carpeta abierta: " + carpeta);

        // Botón + crea notas por defecto
        main.btnAdd.setText("+");
        main.btnAdd.setOnAction(e -> main.mostrarVista(main.vistaNuevaNota));

        // Mostrar vista de notas
        main.mostrarVista(main.vistaNotas);

        if ("Favoritos".equals(carpeta)) {
            main.favoritosViewController.cargarFavoritos();
        } else {
            main.notasViewController.cargarNotas(carpeta);
        }
    }

    @FXML
    public void crearCarpeta() {
        main.mostrarVista(main.vistaNuevaCarpeta);
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

            main.listaCarpetas.getItems().add(nombre);
            main.setEstado("Carpeta creada: " + nombre);

            // Volver a la vista de carpetas
            main.mostrarVista(main.vistaCarpetas);

        } catch (Exception e) {
            e.printStackTrace();
            main.setEstado("Error creando carpeta");
        }
    }
}
