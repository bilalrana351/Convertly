package com.example.convertlyapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class WelcomePage extends Application {
    protected static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        WelcomePage.stage = stage;

        stage.setMaximized(true);

        URL url = WelcomePage.class.getResource("/com/example/convertlyapp/welcome-page.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Convertly App");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}