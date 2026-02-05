package com.mdi.notas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

import com.mdi.notas.App;
import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;

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

    @FXML
    private ListView<String> listaCarpetas;

    private Node pantallaPrincipal;
    private String contenidoActual = "";

    @FXML
    public void initialize() {
        try {
            pantallaPrincipal = escritorio.getChildren().get(0);
            cargarUltimaNota();
            contenidoActual = txtContenidoUltimaNota.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        inicializarCarpetas();
    }

    private void inicializarCarpetas() {
        if (listaCarpetas != null) {
            listaCarpetas.getItems().addAll(
                "Notas",
                "Eliminadas"
            );

            listaCarpetas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, nuevaCarpeta) -> {
                    if (nuevaCarpeta != null) {
                        manejarCambioCarpeta(nuevaCarpeta);
                    }
                }
            );
        }
    }

    private void manejarCambioCarpeta(String carpeta) {
        switch (carpeta) {
            case "Notas" -> {
                abrirLista();
                setEstado("Mostrando notas");
            }
            case "Eliminadas" -> {
                setEstado("Mostrando notas eliminadas");
                // Aquí podrás cargar una vista futura de eliminadas
            }
            default -> {
                setEstado("Carpeta: " + carpeta);
            }
        }
    }

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

    public void refrescarUltimaNota() {
        cargarUltimaNota();
    }

    public void setContenidoActual(String texto) {
        this.contenidoActual = texto;
    }

    @FXML
    public void volverAtras() {
        escritorio.getChildren().setAll(pantallaPrincipal);
        setEstado("Volviste a la pantalla principal");
    }

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

    public void setEstado(String msg) {
        lblEstado.setText(msg);
    }

    private void mostrarPopup(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.show();
    }
}
