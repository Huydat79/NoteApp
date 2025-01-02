module com.project.noteapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.project.controllers to javafx.fxml; // Cho phép JavaFX truy cập vào package controllers
    opens com.project.noteapp.views to javafx.fxml;      // Cho phép JavaFX truy cập vào package views
    opens com.project.noteapp.image to javafx.fxml;
    opens com.project.noteapp.css to javafx.fxml;

    exports com.project.controllers;              // Xuất package controllers nếu cần

    opens com.project.entity to javafx.fxml;
    exports com.project.entity;
    
    opens com.project.noteapp to javafx.fxml;
    exports com.project.noteapp;
    requires itextpdf;
}
