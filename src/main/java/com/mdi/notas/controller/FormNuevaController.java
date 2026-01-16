package com.mdi.notas.controller;

import com.mdi.notas.model.Nota;
import com.mdi.notas.service.NotaService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class FormNuevaController {

    private final NotaService service;

    private NotaFxController mainController;

    public void setMainController(NotaFxController controller) {
        this.mainController = controller;
    }

    public FormNuevaController(NotaService service) {
        this.service = service;
    }

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextArea txtContenido;

    @FXML
    public void guardarNota() {
        try {
            Nota n = new Nota();
            n.setTitulo(txtTitulo.getText());
            n.setContenido(txtContenido.getText());

            service.guardar(n);

            txtTitulo.clear();
            txtContenido.clear();

            if (mainController != null) {
                mainController.refrescarUltimaNota();
                mainController.volverAtras();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void volverAtras() {
        if (mainController != null) {
            mainController.volverAtras();
        }
    }
}

