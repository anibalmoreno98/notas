package com.mdi.notas.informe;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Files;

/**
 * Controlador encargado de gestionar la ventana de informe.
 * Muestra un resumen del texto analizado y genera un gráfico de barras
 * con la frecuencia de palabras. También permite exportar el informe a un archivo TXT.
 */
public class InformeController {

    /** Área de texto donde se muestra el resumen generado. */
    @FXML
    private TextArea areaResumen;

    /** Gráfico de barras que representa la frecuencia de palabras. */
    @FXML
    private BarChart<String, Number> grafico;

    /** Generador de informes basado en el texto recibido. */
    private InformeGenerator generator;

    /**
     * Carga el texto a analizar, genera el resumen y construye el gráfico de frecuencias.
     *
     * @param texto contenido del documento a analizar
     */
    public void cargarTexto(String texto) {
        generator = new InformeGenerator(texto);

        // Mostrar resumen
        areaResumen.setText(generator.generarResumen());

        // Crear gráfico de frecuencias
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Frecuencia");

        generator.getFrecuenciaPalabras().forEach((palabra, freq) ->
                serie.getData().add(new XYChart.Data<>(palabra, freq))
        );

        grafico.getData().clear();
        grafico.getData().add(serie);
    }

    /**
     * Exporta el informe generado a un archivo de texto.
     * El archivo incluye el resumen y la tabla de frecuencias.
     */
    @FXML
    public void exportarTXT() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Guardar informe");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Archivo de texto", "*.txt")
            );

            chooser.setInitialDirectory(new File(System.getProperty("user.home")));

            chooser.setInitialFileName("informe.txt");

            Window window = areaResumen.getScene().getWindow();

            File archivo = chooser.showSaveDialog(window);
            if (archivo != null) {

                if (!archivo.getName().endsWith(".txt")) {
                    archivo = new File(archivo.getAbsolutePath() + ".txt");
                }

                Files.writeString(
                        archivo.toPath(),
                        generator.generarResumen() + "\n\n" + generator.getFrecuenciaPalabras()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}