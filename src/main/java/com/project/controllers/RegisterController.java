package com.project.controllers;

import com.project.dataaccess.DataAccessException;
import com.project.entity.User;
import com.project.service.CreateUserService;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class cho Register GUI
 */
public class RegisterController extends Controller {
    //Các thuộc tính FXML    
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private TextField schoolField;
    @FXML
    private TextField usernameField;  
    @FXML
    private TextField dayOfBirthField;
    @FXML
    private TextField monthOfBirthField;
    @FXML
    private TextField yearOfBirthField;
    @FXML
    private RadioButton genderMale;
    @FXML
    private RadioButton genderFemale;
    @FXML
    private RadioButton genderOther;
    @FXML
    private Label errorNameFieldLabel;
    @FXML
    private Label errorUsernameFieldLabel;
    @FXML
    private Label errorPasswordFieldLabel;
    @FXML
    private Label errorBirthdayFieldLabel;
    @FXML
    private Label backLoginLabel;
    @FXML
    private Button closeButton;
    
    @Override
    public void init() {
        initScene();
        registerButton.setOnAction((ActionEvent event) -> {
            register();
        }); 
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        }); 
        backLoginLabel.setOnMouseClicked((MouseEvent event) -> {
            openLogin();
        });
    }
    
    protected void initScene() {
        //Ẩn các error label
        errorNameFieldLabel.setVisible(false);
        errorUsernameFieldLabel.setVisible(false);
        errorPasswordFieldLabel.setVisible(false);
        errorBirthdayFieldLabel.setVisible(false);        
    }
    
    @Override
    protected void close() {
        Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION,
                "Close NoteLite?");
        if(optional.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    protected void register() {
        initScene();
        //Thiết lập các thuộc tính cho new user
        User newUser = new User();
        //Láy thông tin name
        if("".equals(nameField.getText())) {
            errorNameFieldLabel.setVisible(true);
        }
        newUser.setName(nameField.getText());
        //Lấy username
        if("".equals(usernameField.getText())) {
            errorUsernameFieldLabel.setVisible(true);
        }
        newUser.setUsername(usernameField.getText());
        //Lấy password
        if("".equals(passwordField.getText())) {
            errorPasswordFieldLabel.setVisible(true);
        }
        newUser.setPassword(passwordField.getText());
        //Lấy school
        newUser.setSchool(schoolField.getText());
        //Lấy thông tin về birth
        int dayOfBirth = LocalDate.now().getDayOfMonth();
        int monthOfBirth = LocalDate.now().getMonthValue();
        int yearOfBirth = LocalDate.now().getYear();
        int cnt = 0;
        if(dayOfBirthField.getText().matches("^[0-9]{1,2}$")) {
            dayOfBirth = Integer.parseInt(dayOfBirthField.getText());
        } else {
            errorBirthdayFieldLabel.setVisible(true);
            cnt++;
        }
        if(monthOfBirthField.getText().matches("^[0-9]{1,2}$")) {
            monthOfBirth = Integer.parseInt(monthOfBirthField.getText());
        } else {
            errorBirthdayFieldLabel.setVisible(true);
            cnt++;
        }
        if(yearOfBirthField.getText().matches("^[0-9]{4}$")) {
            yearOfBirth = Integer.parseInt(yearOfBirthField.getText());
        } else {
            errorBirthdayFieldLabel.setVisible(true);
            cnt++;
        } 
        if(cnt == 0 || cnt == 3) {
            newUser.setBirthday(Date.valueOf(LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth)));
            errorBirthdayFieldLabel.setVisible(false);
        }
        //Lấy gender
        if(genderMale.isSelected()) {
            newUser.setGender(User.Gender.MALE);
        } else if (genderFemale.isSelected()) {
            newUser.setGender(User.Gender.FEMALE);
        } else {
            newUser.setGender(User.Gender.OTHER);
        }
        //Kiểm tra xem có lỗi nào không
        if(errorNameFieldLabel.isVisible() || errorUsernameFieldLabel.isVisible()
                || errorPasswordFieldLabel.isVisible() || errorBirthdayFieldLabel.isVisible()) {
            return;
        }
        
        try { 
            //Tạo User mới thành công
            CreateUserService userService = new CreateUserService(newUser);
            userService.execute();
            showAlert(Alert.AlertType.INFORMATION, "Successfully create");
            Optional<ButtonType> optional = showAlert(Alert.AlertType.CONFIRMATION, "Back to Login");
            if(optional.get() == ButtonType.OK) {
                //Quay về trang đăng nhập
                openLogin();
            }          
        } catch (DataAccessException ex) {
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    protected void openLogin() {
        try {
            //Load Login GUI
            FXMLLoader fXMLLoader = new FXMLLoader();
            String loginFXMLPath = "/com/project/noteapp/views/LoginView.fxml";
            fXMLLoader.setLocation(getClass().getResource(loginFXMLPath));
            //Chuyển sang GUI Login
            scene = new Scene(fXMLLoader.load());
            LoginController loginController = fXMLLoader.getController();
            loginController.setStage(stage);
            loginController.init();
            
            setSceneMoveable();
            
            stage.setScene(scene);  
        } catch (IOException ex) {
            showAlert(Alert.AlertType.ERROR, "Can't open login");
        }
    }
}