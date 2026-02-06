package com.mdi.notas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Clase principal de la aplicación.
 * <p>
 * Combina JavaFX con Spring para permitir la inyección de dependencias
 * en los controladores definidos en los archivos FXML.
 * </p>
 * <p>
 * El ciclo de vida es:
 * <ul>
 *     <li>{@link #init()} — Inicializa el contexto de Spring</li>
 *     <li>{@link #start(Stage)} — Carga la interfaz principal (mdi.fxml)</li>
 *     <li>{@link #main(String[])} — Punto de entrada estándar</li>
 * </ul>
 * </p>
 */
public class App extends Application {

    /** Contexto de Spring utilizado para gestionar los beans de la aplicación. */
    private static AnnotationConfigApplicationContext context;

    /**
     * Inicializa el contexto de Spring antes de que JavaFX cargue la interfaz.
     * Este método se ejecuta automáticamente antes de {@link #start(Stage)}.
     */
    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    /**
     * Inicia la aplicación JavaFX.
     * <p>
     * Carga el archivo FXML principal (mdi.fxml) y configura la fábrica de controladores
     * para que Spring gestione la creación de los mismos.
     * </p>
     *
     * @param stage ventana principal de la aplicación
     * @throws Exception si ocurre un error al cargar el archivo FXML
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mdi.fxml"));
        loader.setControllerFactory(context::getBean);

        stage.setScene(new Scene(loader.load(), 800, 600));
        stage.setTitle("MDI Notas");
        stage.show();
    }

    /**
     * Devuelve el contexto de Spring para permitir que otras clases accedan a los beans.
     *
     * @return contexto de Spring
     */
    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }

    /**
     * Método principal de la aplicación.
     * Lanza el ciclo de vida de JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
