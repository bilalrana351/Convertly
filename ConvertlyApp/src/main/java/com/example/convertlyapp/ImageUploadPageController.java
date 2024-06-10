package com.example.convertlyapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.concurrent.Task;

import java.awt.*;
import java.io.File;
import java.io.IOException;


public class ImageUploadPageController {
    // This will be the controller class for the image upload page
    @FXML
    private Button uploadImageButton;

    @FXML
    private Button nextImageButton;

    @FXML
    private Button getDocumentButton;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ProgressIndicator progressIndicator;

    private boolean isImageUploaded = false;

    private int clicksOnButton = 0;

    private double screenHeight;
    private double screenWidth;

    // This will be what will happen when the page is initialized
    @FXML
    public void initialize(){
        // We will set the next image button to be disabled, it will only be enabled when we are able to upload an image
        nextImageButton.setDisable(true);

        progressIndicator.setVisible(false);

        screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        accordingToScreenWidthAndHeight();

        // And if the user does not have any image in the ImageUploadPage as of right now, we will disable the getDocumentButton
        if (ImageUploadPage.numberOfPages == 1 && !isImageUploaded){
            getDocumentButton.setDisable(true);
        }

        // Set the action event handlers for the buttons
        uploadImageButton.setOnAction(event -> handleUploadImageButtonClick());
        nextImageButton.setOnAction(event -> handleNextImageButtonClick());
        getDocumentButton.setOnAction(event -> handleGetDocumentButtonClick());
        anchorPane.setOnMouseClicked(event -> {;});
    }

    // Handle the upload image button click
    private void handleUploadImageButtonClick() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Image File");
    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    File selectedFile = fileChooser.showOpenDialog(uploadImageButton.getScene().getWindow());
    if (selectedFile != null) {
        // If the selected file is not null we will add the path to the file
        SendAndGetImages.addPath(selectedFile.getAbsolutePath());
        // This will put up the image in the container for the imageView
        imageView.setImage(new Image(selectedFile.toURI().toString()));
        // This will enable the next image button, and will also do some other tasks
        isImageUploaded = true;
        nextImageButton.setDisable(false);
        if (ImageUploadPage.numberOfPages == 1)
            getDocumentButton.setDisable(false);
        }
    }

    // Handle the next image button click
    private void handleNextImageButtonClick() {
        // This will simply put call the ImageUploadPage yet again if this method is called
                    // When the button is clicked, we will load the WelcomePage
            try {
                ImageUploadPage imageUploadPage = new ImageUploadPage();
                imageUploadPage.start((Stage) uploadImageButton.getScene().getWindow());
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

    // Handle the get document button click
    private void handleGetDocumentButtonClick() {
        if (clicksOnButton != 0)
            return;

        clicksOnButton++;

        Task<File> task = new Task<>() {
            @Override
            protected File call() {
                progressIndicator.setVisible(true);
                File fileReceived = null;
                try {
                    // Move the long-running task to the call method of the Task
                    fileReceived = SendAndGetImages.sendImages();
                    progressIndicator.setVisible(false);
                    return fileReceived;
                }
                catch (Exception e){
                    progressIndicator.setVisible(false);
                    return fileReceived;
                }
            }
        };

        task.setOnSucceeded(event -> {
            // After the document button has been clicked, we will show a new window showing the loaded document
            File outputFile = task.getValue();

            // We will now open the document, and try to give the user access to save the document at the appropriate place
            if (outputFile != null) {
                // We will open the document
                try {
                    // This will open the document
                    Desktop.getDesktop().open(outputFile);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("An error occurred:");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    Platform.exit();
                }
            }
            // This will also set the count of the value of the number of generated instances in the image upload page to zero
            ImageUploadPage.newGeneration();
            // We will also set the value of the send and get images' arrayList to clear by
            SendAndGetImages.resetImagesPath();

            // This will now bring us back to the image upload page
            try {
                ImageUploadPage imageUploadPage = new ImageUploadPage();
                imageUploadPage.start(WelcomePage.stage);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("An error occurred:");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                Platform.exit();
            }
        });

        task.setOnFailed(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("An error occurred:");
            alert.setContentText(task.getException().getMessage());
            alert.showAndWait();
            Platform.exit();
        });

        new Thread(task).start();
    }

    // This will be the method that will inflate the ImageUploadPage, according to the screen width and height
    public void accordingToScreenWidthAndHeight(){
        anchorPane.setPrefWidth(screenWidth);
        anchorPane.setPrefHeight(screenHeight);
        uploadImageButton.setLayoutX(screenWidth * uploadImageButton.getLayoutX());
        uploadImageButton.setLayoutY(screenHeight * uploadImageButton.getLayoutY());
        uploadImageButton.setPrefWidth(screenWidth * uploadImageButton.getPrefWidth());
        uploadImageButton.setPrefHeight(screenHeight * uploadImageButton.getPrefHeight());
        nextImageButton.setLayoutX(screenWidth * nextImageButton.getLayoutX());
        nextImageButton.setLayoutY(screenHeight * nextImageButton.getLayoutY());
        nextImageButton.setPrefWidth(screenWidth * nextImageButton.getPrefWidth());
        nextImageButton.setPrefHeight(screenHeight * nextImageButton.getPrefHeight());
        getDocumentButton.setLayoutX(screenWidth * getDocumentButton.getLayoutX());
        getDocumentButton.setLayoutY(screenHeight * getDocumentButton.getLayoutY());
        getDocumentButton.setPrefWidth(screenWidth * getDocumentButton.getPrefWidth());
        getDocumentButton.setPrefHeight(screenHeight * getDocumentButton.getPrefHeight());
        imageView.setFitWidth(screenWidth * imageView.getFitWidth());
        imageView.setFitHeight(screenHeight * imageView.getFitHeight());
        imageView.setLayoutX(screenWidth * imageView.getLayoutX());
        imageView.setLayoutY(screenHeight * imageView.getLayoutY());
        progressIndicator.setLayoutX(screenWidth * progressIndicator.getLayoutX());
        progressIndicator.setLayoutY(screenHeight * progressIndicator.getLayoutY());
        progressIndicator.setPrefWidth(screenWidth * progressIndicator.getPrefWidth());
        progressIndicator.setPrefHeight(screenHeight * progressIndicator.getPrefHeight());
    }
}
