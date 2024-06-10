package com.example.convertlyapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ImageUploadPage extends Application {
    protected static int numberOfPages = 0;

    @Override
    public void start(Stage stage) throws IOException{
        try{
            stage.setMaximized(true);

            // In case the method is called, yet again we will increment the number of pages, that the user has entered
            numberOfPages++;
            URL url = ImageUploadPage.class.getResource("/com/example/convertlyapp/image-upload-page.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("An error occurred:");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            Platform.exit();
        }
    }

    public static void newGeneration(){
        numberOfPages = 0;
    }
}
