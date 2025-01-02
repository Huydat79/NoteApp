package com.project.controllers;

import com.project.dataaccess.DataAccessException;
import com.project.entity.Note;
import com.project.entity.NoteFilter;
import com.project.entity.User;
import com.project.service.GetAllNotesService;
import com.project.service.SaveNoteService;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


/**
 * FXML Controller class
 */
public class EditNoteViewController extends Controller {
    //Các thuộc tính chung
    @FXML 
    private Label noteHeaderLabel;
    @FXML
    private Button homeMenuButton;
    @FXML
    private HBox editBox;
    //Các thuộc tính của edit Box
    @FXML
    private Button saveNoteButton;
    @FXML 
    private Button addFilterButton;
    @FXML
    private Button addTextBlockButton;
    @FXML
    private ComboBox<String> fontTypeComboBox; 
    @FXML
    private ComboBox<String> fontSizeComboBox;
    @FXML
    private ColorPicker colorPicker;
    //Các thuộc tính còn lại
    @FXML
    private TextArea textLayout;
    @FXML
    private GridPane filterGridLayout;
    @FXML
    private Button closeButton;
    
    private User myUser;
    private Note myNote;


    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }
    
    public void setMyNote(Note myNote) {
        this.myNote = myNote;
    }
    
    @Override
    public void init() {
        initView();
        closeButton.setOnAction((ActionEvent event) -> {
            close();
        });
        homeMenuButton.setOnAction((ActionEvent event) -> {
            openDashboard(myUser);
        });
        noteHeaderLabel.setOnMouseClicked((MouseEvent event) -> {
            changeHeaderLabel();
        });
        saveNoteButton.setOnAction((ActionEvent event) -> {
            saveMyNote();
        });
        addFilterButton.setOnAction((ActionEvent event) -> {
            addFilter();
        });
       
        addTextBlockButton.setOnAction((ActionEvent event) -> {
            textLayout.setEditable(true);
        });
    }
    
    protected void initView() {       
        noteHeaderLabel.setText(myNote.getHeader());
        loadFilter(myNote.getFilters(), 8);
        textLayout.setText(myNote.getContent());
        textLayout.setEditable(false); // Mặc định không cho chỉnh sửa
    }
    
//    public void setOnAutoUpdate() {
//        updateTimer = new Timer();
//        updateTask = new TimerTask() {
//            @Override
//            public void run() {
//                Platform.runLater(() -> {
//                    try {
//                        OpenNoteService noteService = new OpenNoteService(myNote.getId());
//                        myNote = noteService.execute();
//                        noteHeaderLabel.setText(myNote.getHeader());
//                        loadFilter(myNote.getFilters(), 8);
//                        System.out.println(System.currentTimeMillis());
//                    } catch (DataAccessException ex) {
//                        System.err.println(ex.getMessage());
//                    }
//                });
//            }
//        };
//        updateTimer.scheduleAtFixedRate(updateTask, 2000, 4000);
//    }
    
    @Override
    protected void close() {
        //saveMyNote();
        super.close();
    }
    
    protected void changeHeaderLabel() {
        //Mở dialog
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Change header for " + myNote.getHeader());
        inputDialog.setHeaderText("Input your new header");
        //Lấy kết quả và xử lý
        Optional<String> confirm = inputDialog.showAndWait();
        confirm.ifPresent(newNoteHeader -> {
            //Lấy tất cả các Note của myUser
            try { 
                GetAllNotesService noteCollectionService = new GetAllNotesService(myUser.getUsername());
                //Lấy thành công
                List<Note> myNotes = noteCollectionService.execute();
                for(Note note: myNotes) {
                    if(note.getHeader().equals(newNoteHeader)) {
                        showAlert(Alert.AlertType.ERROR, "This header exist");
                        return;
                    }
                }
                //Thiết lập note name vừa nhập cho Label   
                noteHeaderLabel.setText(newNoteHeader);
                saveMyNote();
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    protected void saveMyNote() {
        myNote.setContent(textLayout.getText());
        myNote.setHeader(noteHeaderLabel.getText());
        myNote.setLastModified(1);
        myNote.setLastModifiedDate(Date.valueOf(LocalDate.now()));
        try {
            SaveNoteService noteService = new SaveNoteService(myNote);
            noteService.execute();
            showAlert(Alert.AlertType.INFORMATION, "Successfully save for " + myNote.getHeader());
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    
    protected void addFilter() {
        //Hiện Dialog để nhập filter mới
        TextInputDialog inputDialog = new TextInputDialog();        
        inputDialog.setTitle("Add filter for " + myNote.getHeader());
        inputDialog.setHeaderText("Enter your new filter");   
        //Lấy kết quả
        Optional<String> confirm = inputDialog.showAndWait();
        //Xử lý kết quả khi nhấn OK
        confirm.ifPresent(newFilterStr -> {
            NoteFilter newFilter = new NoteFilter(newFilterStr);
            //Thiết lập và add tất cả các filter cũ     
            if(myNote.getFilters().contains(newFilter)) {
                //Nếu filter đã tồn tại thì thông báo lỗi
                showAlert(Alert.AlertType.ERROR, "Exist Filter");
            } else {
                //Thêm filter vào list
                myNote.getFilters().add(newFilter);
                //Load lại filter GUI
                loadFilter(myNote.getFilters(), 8);
            }
        }); 
    }
    
    protected void loadFilter(List<NoteFilter> filters, int maxColEachRow) {
        int column = 0;
        int row = 0;
        //Làm sạch filter layout
        filterGridLayout.getChildren().clear();
        if(filters.isEmpty()) {
            return;
        }
        //Thiết lập khoảng cách giữa các filter
        filterGridLayout.setHgap(8);
        filterGridLayout.setVgap(8);
        //Thiết lập filter layout
        try {
            for(int i = 0; i < filters.size(); i++) {
                //Load filter FXML
                FXMLLoader fXMLLoader = new FXMLLoader();
                String filterFXMLPath = "/com/project/noteapp/views/NoteFiltersView.fxml";
                fXMLLoader.setLocation(getClass().getResource(filterFXMLPath));
                HBox hbox = fXMLLoader.load();
                //Thiết lập dữ liệu cho filter
                NoteFiltersController filterFXMLController = fXMLLoader.getController();
                filterFXMLController.setData(filters.get(i).getFilterContent());
                filterFXMLController.getRemoveFilterView().setOnMouseClicked(event -> {
                    myNote.getFilters().remove(new NoteFilter(filterFXMLController.getFilter()));
                    loadFilter(myNote.getFilters(), 8);
                });
                //Chuyển hàng
                if(column == maxColEachRow){
                    column = 0;
                    row++;
                }
                //Thêm filter vừa tạo vào layout
                filterGridLayout.add(hbox, column++, row);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    
    protected void openDashboard(User user) {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            String dashboardViewPath = "/com/project/noteapp/views/DashboardView.fxml";
            fXMLLoader.setLocation(getClass().getResource(dashboardViewPath));

            scene = new Scene(fXMLLoader.load());

            DashboardController controller = fXMLLoader.getController();
            controller.setStage(stage);
            controller.setMyUser(myUser);
            controller.setCurrentNote(myNote);
            controller.init();

            setSceneMoveable();

            stage.setScene(scene);
        } catch (IOException ex) {
        showAlert(Alert.AlertType.ERROR, "Can't open dashboard");
        }
    }
}
