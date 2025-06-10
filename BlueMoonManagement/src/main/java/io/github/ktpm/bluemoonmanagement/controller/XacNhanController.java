package io.github.ktpm.bluemoonmanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller cho dialog xác nhận có sẵn
 */
public class XacNhanController implements Initializable {

    @FXML
    private Button button_close_up;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonConfirm;

    @FXML
    private Label labelXacNhan;

    @FXML
    private Text textNoiDung;

    private boolean confirmed = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirmed = false;
        
        // Đảm bảo các nút được enable và có thể tương tác
        if (buttonConfirm != null) {
            buttonConfirm.setDisable(false);
            buttonConfirm.setVisible(true);
            buttonConfirm.setManaged(true);
            
            // Thêm mouse event listener để test
            buttonConfirm.setOnMouseClicked(event -> {
                System.out.println("DEBUG: Confirm button MOUSE CLICKED detected!");
                handleConfirm(null);
            });
            
            buttonConfirm.setOnMouseEntered(event -> {
                System.out.println("DEBUG: Mouse entered Confirm button");
            });
            
            System.out.println("DEBUG: Confirm button enabled");
        }
        
        if (buttonCancel != null) {
            buttonCancel.setDisable(false);
            buttonCancel.setVisible(true);
            buttonCancel.setManaged(true);
            
            // Thêm mouse event listener để test
            buttonCancel.setOnMouseClicked(event -> {
                System.out.println("DEBUG: Cancel button MOUSE CLICKED detected!");
                handleCancel(null);
            });
            
            buttonCancel.setOnMouseEntered(event -> {
                System.out.println("DEBUG: Mouse entered Cancel button");
            });
            
            System.out.println("DEBUG: Cancel button enabled");
        }
        
        if (button_close_up != null) {
            button_close_up.setDisable(false);
            button_close_up.setVisible(true);
            button_close_up.setManaged(true);
            System.out.println("DEBUG: Close button enabled");
        }
    }

    /**
     * Thiết lập nội dung thông báo
     */
    public void setContent(String content) {
        if (textNoiDung != null) {
            textNoiDung.setText(content);
        }
        
        // Debug kiểm tra trạng thái các nút sau khi set content
        checkButtonStates();
    }

    /**
     * Thiết lập tiêu đề
     */
    public void setTitle(String title) {
        if (labelXacNhan != null) {
            labelXacNhan.setText(title);
        }
    }

    /**
     * Xử lý khi nhấn nút đóng (X)
     */
    @FXML
    void handleCloseUp(ActionEvent event) {
        confirmed = false;
        closeDialog();
    }

    /**
     * Xử lý khi nhấn nút "Hủy"
     */
    @FXML
    void handleCancel(ActionEvent event) {
        System.out.println("DEBUG: handleCancel() called - User clicked Cancel button");
        confirmed = false;
        closeDialog();
    }

    /**
     * Xử lý khi nhấn nút "Xác nhận"
     */
    @FXML
    void handleConfirm(ActionEvent event) {
        System.out.println("DEBUG: handleConfirm() called - User clicked Confirm button");
        confirmed = true;
        closeDialog();
    }

    /**
     * Đóng dialog
     */
    private void closeDialog() {
        try {
            Stage stage = (Stage) buttonCancel.getScene().getWindow();
            System.out.println("DEBUG: Closing dialog with confirmed = " + confirmed);
            stage.close();
        } catch (Exception e) {
            System.err.println("ERROR: Cannot close dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Kiểm tra xem người dùng có xác nhận không
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * Kiểm tra trạng thái các nút để debug
     */
    public void checkButtonStates() {
        System.out.println("=== DEBUG: Button States ===");
        if (buttonConfirm != null) {
            System.out.println("Confirm button - Disabled: " + buttonConfirm.isDisabled() + 
                             ", Visible: " + buttonConfirm.isVisible() + 
                             ", Managed: " + buttonConfirm.isManaged());
        } else {
            System.out.println("Confirm button is NULL");
        }
        
        if (buttonCancel != null) {
            System.out.println("Cancel button - Disabled: " + buttonCancel.isDisabled() + 
                             ", Visible: " + buttonCancel.isVisible() + 
                             ", Managed: " + buttonCancel.isManaged());
        } else {
            System.out.println("Cancel button is NULL");
        }
        
        if (button_close_up != null) {
            System.out.println("Close button - Disabled: " + button_close_up.isDisabled() + 
                             ", Visible: " + button_close_up.isVisible() + 
                             ", Managed: " + button_close_up.isManaged());
        } else {
            System.out.println("Close button is NULL");
        }
                 System.out.println("=========================");
    }
    
    /**
     * Force enable tất cả các nút
     */
    public void forceEnableButtons() {
        System.out.println("DEBUG: Force enabling all buttons...");
        
        if (buttonConfirm != null) {
            buttonConfirm.setDisable(false);
            buttonConfirm.setVisible(true);
            buttonConfirm.setManaged(true);
            buttonConfirm.setOpacity(1.0);
            buttonConfirm.setMouseTransparent(false);
        }
        
        if (buttonCancel != null) {
            buttonCancel.setDisable(false);
            buttonCancel.setVisible(true);
            buttonCancel.setManaged(true);
            buttonCancel.setOpacity(1.0);
            buttonCancel.setMouseTransparent(false);
        }
        
        if (button_close_up != null) {
            button_close_up.setDisable(false);
            button_close_up.setVisible(true);
            button_close_up.setManaged(true);
            button_close_up.setOpacity(1.0);
            button_close_up.setMouseTransparent(false);
        }
        
        System.out.println("DEBUG: All buttons force enabled");
    }
} 
