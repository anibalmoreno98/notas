package com.mdi.notas.controller;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import org.springframework.stereotype.Controller;

import com.mdi.notas.App;
import com.mdi.notas.model.Nota;
import com.mdi.notas.service.CarpetaService;
import com.mdi.notas.service.NotaService;

import java.util.List;

@Controller
public class CarpetaFxController {

    @FXML
    private Label lblTitulo;

    @FXML
    private Button btnAdd;

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
    private Button btnBack;

    @FXML
    private VBox vistaNuevaNota;

    @FXML
    private TextField txtTituloNuevaNota;

    @FXML
    private TextArea txtContenidoNuevaNota;

    @FXML
    private VBox vistaNuevaCarpeta;

    @FXML
    private TextField txtNombreNuevaCarpeta;

    @FXML
    private VBox vistaVerNota;

    @FXML
    private Label lblTituloNota;

    @FXML
    private TextArea txtContenidoNota;

    private ChangeListener<String> guardadoListener;

    private String notaActualTitulo;

    private boolean notaEsFavorita;

    @FXML
    private Button btnFavorito;

    @FXML
    private Button btnQuitarFavorito;

    private String carpetaRealDeLaNota;

    @FXML
    private Button btnEliminarDefinitivo;

    @FXML
    private Button btnEliminar;

    @FXML
    public void cancelarNuevaCarpeta() {
        volverAVistaCarpetas();
    }


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

        if (carpeta.equals("Favoritos")) {
            cargarNotasFavoritas();
        } else {
            cargarNotasDeCarpeta(carpeta);
        }
    }


    private void cargarNotasDeCarpeta(String carpeta) {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            List<Nota> notas = service.listarPorCarpeta(carpeta);

            listaNotas.getItems().clear();
            for (Nota n : notas) {
                listaNotas.getItems().add(n.getTitulo());
            }

            // Doble clic para abrir nota
            listaNotas.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    String titulo = listaNotas.getSelectionModel().getSelectedItem();
                    if (titulo != null) {
                        abrirNota(titulo);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error cargando notas");
        }
    }

    private void crearNota() {
        txtTituloNuevaNota.clear();
        txtContenidoNuevaNota.clear();
        mostrarVistaNuevaNota();
    }



    @FXML
    public void volverAtras() {
        mostrarVistaCarpetas();
        lblTitulo.setText("Carpetas");
        setEstado("Volviste a carpetas");
    }


    private void crearCarpeta() {
        txtNombreNuevaCarpeta.clear();
        mostrarVistaNuevaCarpeta();
    }



    public String getCarpetaActual() {
        return carpetaActual;
    }

    public void refrescarNotas() {
        cargarNotasDeCarpeta(carpetaActual);
    }


    private void mostrarVistaCarpetas() {
        ocultarTodasLasVistas();

        listaCarpetas.setVisible(true);
        listaCarpetas.setManaged(true);

        btnBack.setVisible(false);
        btnBack.setManaged(false);

        btnAdd.setVisible(true);
        btnAdd.setManaged(true);
        btnAdd.setText("+");
        btnAdd.setOnAction(e -> crearCarpeta());
    }


    private void mostrarVistaNotas() {
        ocultarTodasLasVistas();

        vistaNotas.setVisible(true);
        vistaNotas.setManaged(true);

        btnBack.setVisible(true);
        btnBack.setManaged(true);

        btnAdd.setVisible(true);
        btnAdd.setManaged(true);
        btnAdd.setText("+");
        btnAdd.setOnAction(e -> crearNota());
    }


    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }

    private void mostrarVistaNuevaNota() {
        ocultarTodasLasVistas();

        vistaNuevaNota.setVisible(true);
        vistaNuevaNota.setManaged(true);

        btnBack.setVisible(true);
        btnBack.setManaged(true);

        btnAdd.setVisible(false);
        btnAdd.setManaged(false);
    }


    private void volverAVistaNotas() {
        vistaNuevaNota.setVisible(false);
        vistaNuevaNota.setManaged(false);

        vistaNotas.setVisible(true);
        vistaNotas.setManaged(true);

        btnAdd.setVisible(true);
        btnAdd.setManaged(true);
    }

    @FXML
    public void guardarNotaDesdeVista() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);

            Nota n = new Nota();
            n.setTitulo(txtTituloNuevaNota.getText());
            n.setContenido(txtContenidoNuevaNota.getText());
            n.setCarpeta(carpetaActual);

            service.guardar(n);

            setEstado("Nota guardada");

            volverAVistaNotas();
            cargarNotasDeCarpeta(carpetaActual);

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error guardando nota");
        }
    }

    @FXML
    public void cancelarNuevaNota() {
        volverAVistaNotas();
    }

    private void mostrarVistaNuevaCarpeta() {
        ocultarTodasLasVistas();

        vistaNuevaCarpeta.setVisible(true);
        vistaNuevaCarpeta.setManaged(true);

        btnBack.setVisible(true);
        btnBack.setManaged(true);

        btnAdd.setVisible(false);
        btnAdd.setManaged(false);
    }


    private void volverAVistaCarpetas() {
        vistaNuevaCarpeta.setVisible(false);
        vistaNuevaCarpeta.setManaged(false);

        listaCarpetas.setVisible(true);
        listaCarpetas.setManaged(true);

        btnAdd.setVisible(true);
        btnAdd.setManaged(true);

        setEstado("Carpetas");
    }

    @FXML
    public void guardarCarpetaDesdeVista() {
        try {
            String nombre = txtNombreNuevaCarpeta.getText().trim();
            if (nombre.isEmpty()) {
                setEstado("El nombre no puede estar vacío");
                return;
            }

            CarpetaService carpetaService = App.getContext().getBean(CarpetaService.class);
            carpetaService.crear(nombre);

            listaCarpetas.getItems().add(nombre);

            setEstado("Carpeta creada: " + nombre);

            volverAVistaCarpetas();

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error guardando carpeta");
        }
    }

    private void mostrarVistaVerNota() {
        ocultarTodasLasVistas();

        vistaVerNota.setVisible(true);
        vistaVerNota.setManaged(true);

        btnBack.setVisible(true);
        btnBack.setManaged(true);

        btnAdd.setVisible(false);
        btnAdd.setManaged(false);
    }


    private void abrirNota(String titulo) {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            Nota nota;

            // Si estamos en Favoritos, buscar en todas las carpetas
            if ("Favoritos".equals(carpetaActual)) {
                nota = service.buscarEnTodas(titulo);
            } else {
                nota = service.buscarPorTitulo(carpetaActual, titulo);
            }

            if (nota == null) {
                setEstado("No se pudo abrir la nota");
                return;
            }

            // Guardamos título y carpeta real
            notaActualTitulo = titulo;
            carpetaRealDeLaNota = nota.getCarpeta();

            // Mostrar contenido
            lblTituloNota.setText(nota.getTitulo());
            txtContenidoNota.setText(nota.getContenido());

            txtContenidoNota.setStyle(
                    "-fx-font-size: 16px;" +
                    "-fx-text-fill: black;" +
                    "-fx-control-inner-background: white;" +
                    "-fx-background-insets: 0;" +
                    "-fx-padding: 10;"
            );

            txtContenidoNota.setFocusTraversable(false);

            // Listener de guardado automático
            if (guardadoListener != null) {
                txtContenidoNota.textProperty().removeListener(guardadoListener);
            }

            guardadoListener = (obs, oldVal, newVal) -> guardarNotaAuto(notaActualTitulo, newVal);
            txtContenidoNota.textProperty().addListener(guardadoListener);

            // Favoritos
            notaEsFavorita = nota.isFavorita();

            // Si estamos en Favoritos, nunca mostrar "Añadir a favoritos"
            if ("Favoritos".equals(carpetaActual)) {
                btnFavorito.setVisible(false);
                btnFavorito.setManaged(false);

                btnQuitarFavorito.setVisible(true);
                btnQuitarFavorito.setManaged(true);
            } else {
                btnFavorito.setVisible(!notaEsFavorita);
                btnFavorito.setManaged(!notaEsFavorita);

                btnQuitarFavorito.setVisible(notaEsFavorita);
                btnQuitarFavorito.setManaged(notaEsFavorita);
            }

            // Eliminadas: solo mostrar "Eliminar definitivamente"
            if ("Eliminadas".equals(carpetaActual)) {
                btnEliminar.setVisible(false);
                btnEliminar.setManaged(false);

                btnEliminarDefinitivo.setVisible(true);
                btnEliminarDefinitivo.setManaged(true);
            } else {
                btnEliminar.setVisible(true);
                btnEliminar.setManaged(true);

                btnEliminarDefinitivo.setVisible(false);
                btnEliminarDefinitivo.setManaged(false);
            }

            mostrarVistaVerNota();
            setEstado("Leyendo nota: " + titulo);

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error abriendo nota");
        }
    }





    private void ocultarTodasLasVistas() {
        listaCarpetas.setVisible(false);
        listaCarpetas.setManaged(false);

        vistaNotas.setVisible(false);
        vistaNotas.setManaged(false);

        vistaNuevaNota.setVisible(false);
        vistaNuevaNota.setManaged(false);

        vistaNuevaCarpeta.setVisible(false);
        vistaNuevaCarpeta.setManaged(false);

        vistaVerNota.setVisible(false);
        vistaVerNota.setManaged(false);
    }

    private void guardarNotaAuto(String titulo, String nuevoContenido) {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);

            Nota nota = new Nota();
            nota.setTitulo(titulo);
            nota.setContenido(nuevoContenido);
            nota.setCarpeta(carpetaRealDeLaNota);


            if (!"Favoritos".equals(carpetaActual) && !"Eliminadas".equals(carpetaActual)) {
                service.guardar(nota);
            }

            setEstado("Guardado automáticamente");

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error guardando automáticamente");
        }
    }

    @FXML
    private void eliminarNotaActual() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);

            // Buscar la nota actual
            Nota nota = service.buscarPorTitulo(carpetaRealDeLaNota, notaActualTitulo);

            // Moverla a la carpeta "Eliminadas"
            nota.setCarpeta("Eliminadas");
            service.guardar(nota);

            setEstado("Nota movida a Eliminadas");

            // Volver a la lista de notas
            mostrarVistaNotas();
            if ("Favoritos".equals(carpetaActual)) {
                cargarNotasFavoritas();
            } else {
                cargarNotasDeCarpeta(carpetaActual);
            }

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error eliminando nota");
        }
    }

    @FXML
    private void marcarFavorito() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            Nota nota = service.buscarEnTodas(notaActualTitulo);

            nota.setFavorita(true);
            service.guardar(nota);

            notaEsFavorita = true;

            btnFavorito.setVisible(false);
            btnFavorito.setManaged(false);

            btnQuitarFavorito.setVisible(true);
            btnQuitarFavorito.setManaged(true);

            setEstado("Añadida a favoritos");

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error al añadir a favoritos");
        }
    }


    @FXML
    private void desmarcarFavorito() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            Nota nota = service.buscarEnTodas(notaActualTitulo);

            nota.setFavorita(false);
            service.guardar(nota);

            notaEsFavorita = false;

            btnFavorito.setVisible(true);
            btnFavorito.setManaged(true);

            btnQuitarFavorito.setVisible(false);
            btnQuitarFavorito.setManaged(false);

            setEstado("Quitada de favoritos");

            // Si estás dentro de Favoritos, refresca la lista
            if ("Favoritos".equals(carpetaActual)) {
                cargarNotasFavoritas();
            }

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error al quitar de favoritos");
        }
    }



    private void cargarNotasFavoritas() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            List<Nota> notas = service.listarTodas();

            listaNotas.getItems().clear();

            for (Nota n : notas) {
                if (n.isFavorita()) {
                    listaNotas.getItems().add(n.getTitulo());
                }
            }

            listaNotas.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    String titulo = listaNotas.getSelectionModel().getSelectedItem();
                    if (titulo != null) {
                        abrirNota(titulo);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error cargando favoritos");
        }
    }

    @FXML
    private void eliminarDefinitivamente() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);

            // Eliminar archivo .txt y .fav
            service.borrarNota(carpetaRealDeLaNota, notaActualTitulo);

            setEstado("Nota eliminada definitivamente");

            // Volver a la lista de Eliminadas
            mostrarVistaNotas();
            cargarNotasDeCarpeta("Eliminadas");

        } catch (Exception e) {
            e.printStackTrace();
            setEstado("Error eliminando definitivamente");
        }
    }


}
