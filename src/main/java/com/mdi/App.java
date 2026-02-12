package com.mdi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Punto de entrada principal de la aplicación.
 * Combina JavaFX para la interfaz gráfica y Spring para la gestión de dependencias.
 *
 * <p>El ciclo de vida de la aplicación sigue estas fases:</p>
 * <ul>
 *     <li><b>init()</b>: inicializa el contexto de Spring.</li>
 *     <li><b>start()</b>: carga la vista principal y muestra la ventana.</li>
 *     <li><b>stop()</b>: cierra el contexto de Spring al finalizar.</li>
 * </ul>
 */
public class App extends Application {

    /** Contexto de Spring utilizado para la inyección de dependencias. */
    private static AnnotationConfigApplicationContext context;

    /**
     * Inicializa el contexto de Spring antes de que JavaFX cargue la interfaz.
     * Se ejecuta una única vez al inicio del ciclo de vida de la aplicación.
     */
    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    /**
     * Carga la vista principal desde el archivo FXML, configura el controlador
     * mediante Spring y muestra la ventana principal de la aplicación.
     *
     * @param stage ventana principal proporcionada por JavaFX
     * @throws Exception si ocurre un error al cargar el archivo FXML
     */
    @Override
    public void start(Stage stage) throws Exception {

        var url = getClass().getResource("/MainView.fxml");

        if (url == null) {
            throw new IllegalStateException("No se encontró MainView.fxml en el classpath");
        }

        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(context::getBean);

        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Notas");
        stage.show();
    }

    /**
     * Cierra el contexto de Spring al finalizar la aplicación.
     * Se ejecuta automáticamente cuando JavaFX detiene la aplicación.
     */
    @Override
    public void stop() {
        context.close();
    }

    /**
     * Devuelve el contexto de Spring para permitir que otras clases
     * obtengan beans gestionados por el contenedor.
     *
     * @return contexto de Spring
     */
    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }

    /**
     * Método principal que inicia la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
