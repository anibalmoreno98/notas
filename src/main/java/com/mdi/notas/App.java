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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mdi.fxml"));
        loader.setControllerFactory(context::getBean);

        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("MDI Notas");
        stage.show();
    }

    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
