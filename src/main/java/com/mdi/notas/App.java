package com.mdi.notas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App extends Application {

    private static AnnotationConfigApplicationContext context;

    @Override
    public void init() {
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

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

    @Override
    public void stop() {
        context.close();
    }

    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
