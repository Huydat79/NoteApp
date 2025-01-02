package com.project.controllers;


import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public abstract class Controller {
    protected double posX, posY;
    protected Stage stage;
    protected Scene scene;
    
    public abstract void init();
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public Optional<ButtonType> showAlert(Alert.AlertType alertType, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(String.valueOf(alertType));
        alert.setHeaderText(text);
        return alert.showAndWait();
    }
    
    public void setSceneMoveable() {
        posX = 0;
        posY = 0;
        scene.setOnMousePressed((MouseEvent mouseEvent) -> {
            posX = mouseEvent.getSceneX();
            posY = mouseEvent.getSceneY();
        });       
        scene.setOnMouseDragged((MouseEvent mouseEvent) -> {
            stage.setX(mouseEvent.getScreenX() - posX);
            stage.setY(mouseEvent.getScreenY() - posY);
        });
    }
           
    protected void close() {
        Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION,
                "Close NoteLite?");
        if(optional.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
