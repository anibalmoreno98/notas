package com.mdi.notas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

import com.mdi.notas.App;
import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;
import com.mdi.notas.informe.InformeController;
import com.mdi.notas.informe.InformeGenerator;

/**
 * Controlador principal de la aplicación MDI.
 * Gestiona la pantalla principal, la carga dinámica de ventanas internas,
 * el análisis de texto, el estado de la interfaz y la nota actualmente activa.
 */
@Controller
public class NotaFxController {

    @FXML
    private StackPane escritorio;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblTituloUltimaNota;

    @FXML
    private TextArea txtContenidoUltimaNota;

    /** Nodo que representa la pantalla principal del MDI. */
    private Node pantallaPrincipal;

    /** Contenido de la nota actualmente seleccionada o activa. */
    private String contenidoActual = "";

    /**
     * Inicializa el controlador.
     * Guarda la pantalla principal, carga la última nota y establece el contenido activo.
     */
    @FXML
    public void initialize() {
        try {
            pantallaPrincipal = escritorio.getChildren().get(0);
            cargarUltimaNota();
            contenidoActual = txtContenidoUltimaNota.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la última nota creada según el ID más alto.
     * Actualiza la pantalla principal y el contenido activo.
     */
    private void cargarUltimaNota() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            var notas = service.listar();

            if (!notas.isEmpty()) {

                notas.sort((a, b) -> Long.compare(b.getId(), a.getId()));
                Nota ultima = notas.get(0);

                lblTituloUltimaNota.setText(ultima.getTitulo());
                txtContenidoUltimaNota.setText(ultima.getContenido());
                contenidoActual = ultima.getContenido();

            } else {
                lblTituloUltimaNota.setText("(ninguna)");
                txtContenidoUltimaNota.setText("");
                contenidoActual = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresca la última nota mostrada en la pantalla principal.
     */
    public void refrescarUltimaNota() {
        cargarUltimaNota();
    }

    /**
     * Establece el contenido de la nota actualmente activa.
     *
     * @param texto contenido de la nota seleccionada
     */
    public void setContenidoActual(String texto) {
        this.contenidoActual = texto;
    }

    /**
     * Vuelve a la pantalla principal del MDI.
     * Limpia el escritorio y restaura el nodo inicial.
     */
    @FXML
    public void volverAtras() {
        escritorio.getChildren().setAll(pantallaPrincipal);
        setEstado("Volviste a la pantalla principal");
    }

    /**
     * Carga una ventana interna dentro del MDI.
     * Asigna el controlador principal a los controladores hijos.
     *
     * @param ruta ruta del archivo FXML a cargar
     */
    private void cargarVentana(String ruta) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(ruta));
            loader.setControllerFactory(App.getContext()::getBean);

            Node ventana = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ListaNotasController listaController) {
                listaController.setMainController(this);
            }
            if (controller instanceof FormNuevaController formController) {
                formController.setMainController(this);
            }

            escritorio.getChildren().setAll(ventana);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre el formulario para crear una nueva nota.
     */
    @FXML
    public void abrirFormulario() {
        cargarVentana("/FormNueva.fxml");
    }

    /**
     * Abre la ventana que muestra la lista de notas.
     */
    @FXML
    public void abrirLista() {
        cargarVentana("/ListaNotas.fxml");
    }

    /**
     * Actualiza el mensaje de estado mostrado en la parte inferior de la interfaz.
     *
     * @param msg mensaje a mostrar
     */
    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }

    /**
     * Genera un informe basado en la nota activa y lo muestra en una nueva ventana.
     */
    @FXML
    public void generarInforme() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/informe.fxml"));
            Node root = loader.load();

            InformeController controller = loader.getController();
            controller.cargarTexto(contenidoActual);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Informe del documento");
            stage.setScene(new javafx.scene.Scene((javafx.scene.Parent) root, 900, 600));
            stage.show();

            setEstado("Informe generado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un analizador de texto basado en el contenido actual.
     *
     * @return instancia de InformeGenerator
     */
    private InformeGenerator crearAnalizador() {
        return new InformeGenerator(contenidoActual);
    }

    /**
     * Muestra el total de palabras del contenido activo.
     */
    @FXML
    public void mostrarTotalPalabras() {
        InformeGenerator gen = crearAnalizador();
        int total = gen.getTotalPalabras();
        setEstado("Total de palabras: " + total);
        mostrarPopup("Total de palabras", "El documento contiene " + total + " palabras.");
    }

    /**
     * Muestra el número de líneas del contenido activo.
     */
    @FXML
    public void mostrarNumeroLineas() {
        InformeGenerator gen = crearAnalizador();
        int total = gen.getNumeroLineas();
        setEstado("Número de líneas: " + total);
        mostrarPopup("Número de líneas", "El documento contiene " + total + " líneas.");
    }

    /**
     * Muestra el número de caracteres del contenido activo.
     */
    @FXML
    public void mostrarNumeroCaracteres() {
        InformeGenerator gen = crearAnalizador();
        int total = gen.getNumeroCaracteres();
        setEstado("Número de caracteres: " + total);
        mostrarPopup("Número de caracteres", "El documento contiene " + total + " caracteres.");
    }

    /**
     * Muestra la frecuencia de palabras del contenido activo.
     */
    @FXML
    public void mostrarFrecuenciaPalabras() {
        InformeGenerator gen = crearAnalizador();
        var freq = gen.getFrecuenciaPalabras();

        StringBuilder sb = new StringBuilder("Palabras más frecuentes:\n\n");
        freq.forEach((p, f) -> sb.append(p).append(": ").append(f).append("\n"));

        setEstado("Frecuencia calculada");
        mostrarPopup("Frecuencia de palabras", sb.toString());
    }

    /**
     * Muestra un cuadro de diálogo informativo.
     *
     * @param titulo    título de la ventana
     * @param contenido contenido del mensaje
     */
    private void mostrarPopup(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.show();
    }
}
