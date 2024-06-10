package com.example.convertlyapp;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

public class WelcomePageController {
    @FXML
    private ImageView startupImage;

    @FXML
    private Button startButton;

    @FXML
    private ProgressIndicator progressIndicator;

    private double screenWidth;
    private double screenHeight;

    @FXML
    public void initialize() {
        // Set the screen to fullscreen
        screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        accordingToHeightAndWidth();

        // On initialization, we would set the button to be invisible, and we will set the progress bar to be visible
        startButton.setVisible(false);
        progressIndicator.setVisible(true);

        PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(5));
        pause.setOnFinished(event -> {
            // We will switch the visibilities here
            startButton.setVisible(true);
            progressIndicator.setVisible(false);
        });
        pause.play();

        startButton.setOnAction(event -> {
            // When the button is clicked, we will load the WelcomePage
            try {
                ImageUploadPage imageUploadPage = new ImageUploadPage();
                imageUploadPage.start(WelcomePage.stage);
            }
            catch (IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("An error occurred:");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                Platform.exit();
            }
        });
    }

    // This will be the method that will inflate the WelcomePage, according to the screen width and height
    public void accordingToHeightAndWidth(){
        startupImage.setFitWidth(screenWidth * startupImage.getFitWidth());
        startupImage.setFitHeight(screenHeight * startupImage.getFitHeight());
        startupImage.setLayoutX(screenWidth * startupImage.getLayoutX());
        startupImage.setLayoutY(screenHeight * startupImage.getLayoutY());
        startButton.setLayoutX(screenWidth * startButton.getLayoutX());
        startButton.setLayoutY(screenHeight * startButton.getLayoutY());
        startButton.setPrefHeight(screenHeight * startButton.getPrefHeight());
        startButton.setPrefWidth(screenWidth * startButton.getPrefWidth());
        progressIndicator.setLayoutX(screenWidth * progressIndicator.getLayoutX());
        progressIndicator.setLayoutY(screenHeight * progressIndicator.getLayoutY());
        progressIndicator.setPrefWidth(screenWidth * progressIndicator.getPrefWidth());
        progressIndicator.setPrefHeight(screenHeight * progressIndicator.getPrefHeight());
    }
}