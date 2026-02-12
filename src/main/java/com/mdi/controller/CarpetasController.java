package com.mdi.controller;

import com.mdi.App;
import com.mdi.service.CarpetaService;
import javafx.fxml.FXML;

/**
 * Controlador encargado de gestionar la vista de carpetas.
 * Se comunica con {@link MainController} para actualizar la interfaz
 * y con {@link CarpetaService} para realizar operaciones de negocio.
 */
public class CarpetasController {

    /** Referencia al controlador principal, inyectado por MainController. */
    private MainController main;

    /**
     * Asigna el controlador principal y ejecuta la inicialización
     * que depende de que todos los nodos ya estén cargados.
     *
     * @param main instancia del controlador principal
     */
    public void setMain(MainController main) {
        this.main = main;
        inicializarCarpetas();
    }

    /**
     * Método llamado automáticamente por JavaFX al cargar el FXML.
     * No se usa porque el controlador principal aún no está disponible.
     */
    @FXML
    public void initialize() {
        // Intencionalmente vacío
    }

    /**
     * Inicializa la lista de carpetas y configura el listener
     * que detecta cambios en la selección.
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

    /**
     * Abre la carpeta seleccionada y actualiza la interfaz:
     * título, estado, botón de acción y vista mostrada.
     *
     * @param carpeta nombre de la carpeta seleccionada
     */
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

    /**
     * Cambia la vista a la pantalla de creación de carpetas.
     */
    @FXML
    public void crearCarpeta() {
        main.mostrarVista(main.vistaNuevaCarpeta);
    }

    /**
     * Guarda una nueva carpeta utilizando el servicio correspondiente.
     * Valida el nombre, actualiza la lista y vuelve a la vista principal.
     */
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
