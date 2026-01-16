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

    // Guardamos la pantalla principal
    private Node pantallaPrincipal;

    @FXML
    public void initialize() {
        try {
            // Guardar la pantalla principal (primer hijo del StackPane)
            pantallaPrincipal = escritorio.getChildren().get(0);

            cargarUltimaNota();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // CARGAR ÚLTIMA NOTA
    // ---------------------------------------------------------
    private void cargarUltimaNota() {
        try {
            NotaService service = App.getContext().getBean(NotaService.class);
            var notas = service.listar();

            if (!notas.isEmpty()) {
                Nota ultima = notas.get(notas.size() - 1);
                lblTituloUltimaNota.setText(ultima.getTitulo());
                txtContenidoUltimaNota.setText(ultima.getContenido());
            } else {
                lblTituloUltimaNota.setText("(ninguna)");
                txtContenidoUltimaNota.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refrescarUltimaNota() {
        cargarUltimaNota();
    }

    // ---------------------------------------------------------
    // VOLVER ATRÁS (MDI)
    // ---------------------------------------------------------
    @FXML
    public void volverAtras() {
        // Restaurar la pantalla principal
        escritorio.getChildren().setAll(pantallaPrincipal);
        setEstado("Volviste a la pantalla principal");
    }

    // ---------------------------------------------------------
    // CARGA DE VENTANAS MDI
    // ---------------------------------------------------------
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

    @FXML
    public void abrirFormulario() {
        cargarVentana("/FormNueva.fxml");
    }

    @FXML
    public void abrirLista() {
        cargarVentana("/ListaNotas.fxml");
    }

    // ---------------------------------------------------------
    // ESTADO
    // ---------------------------------------------------------
    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }

    // ---------------------------------------------------------
    // INFORME
    // ---------------------------------------------------------
    @FXML
    public void generarInforme() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/informe.fxml"));
            Node root = loader.load();

            InformeController controller = loader.getController();
            controller.cargarTexto(txtContenidoUltimaNota.getText());

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Informe del documento");
            stage.setScene(new javafx.scene.Scene((javafx.scene.Parent) root, 900, 600));
            stage.show();

            setEstado("Informe generado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // ANÁLISIS DE TEXTO
    // ---------------------------------------------------------
    private InformeGenerator crearAnalizador() {
        return new InformeGenerator(txtContenidoUltimaNota.getText());
    }

    @FXML
    public void mostrarTotalPalabras() {
        InformeGenerator gen = crearAnalizador();
        int total = gen.getTotalPalabras();
        setEstado("Total de palabras: " + total);
        mostrarPopup("Total de palabras", "El documento contiene " + total + " palabras.");
    }

    @FXML
    public void mostrarNumeroLineas() {
        InformeGenerator gen = crearAnalizador();
        int total = gen.getNumeroLineas();
        setEstado("Número de líneas: " + total);
        mostrarPopup("Número de líneas", "El documento contiene " + total + " líneas.");
    }

    @FXML
    public void mostrarNumeroCaracteres() {
        InformeGenerator gen = crearAnalizador();
        int total = gen.getNumeroCaracteres();
        setEstado("Número de caracteres: " + total);
        mostrarPopup("Número de caracteres", "El documento contiene " + total + " caracteres.");
    }

    @FXML
    public void mostrarFrecuenciaPalabras() {
        InformeGenerator gen = crearAnalizador();
        var freq = gen.getFrecuenciaPalabras();

        StringBuilder sb = new StringBuilder("Palabras más frecuentes:\n\n");
        freq.forEach((p, f) -> sb.append(p).append(": ").append(f).append("\n"));

        setEstado("Frecuencia calculada");
        mostrarPopup("Frecuencia de palabras", sb.toString());
    }

    private void mostrarPopup(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.show();
    }
}
