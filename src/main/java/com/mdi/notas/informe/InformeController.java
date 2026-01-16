package com.mdi.notas.informe;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;

public class InformeController {

    @FXML
    private TextArea areaResumen;

    @FXML
    private BarChart<String, Number> grafico;

    private InformeGenerator generator;

    public void cargarTexto(String texto) {
        generator = new InformeGenerator(texto);

        // Mostrar resumen
        areaResumen.setText(generator.generarResumen());

        // Crear gráfico
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Frecuencia");

        generator.getFrecuenciaPalabras().forEach((palabra, freq) ->
                serie.getData().add(new XYChart.Data<>(palabra, freq))
        );

        grafico.getData().clear();
        grafico.getData().add(serie);
    }

    @FXML
    public void exportarTXT() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Guardar informe");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de texto", "*.txt"));

            File archivo = chooser.showSaveDialog(null);
            if (archivo != null) {
                Files.writeString(archivo.toPath(),
                        generator.generarResumen() + "\n\n" + generator.getFrecuenciaPalabras());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
