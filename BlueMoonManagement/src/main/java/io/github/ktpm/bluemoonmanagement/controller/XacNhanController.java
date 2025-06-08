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
    }

    /**
     * Thiết lập nội dung thông báo
     */
    public void setContent(String content) {
        if (textNoiDung != null) {
            textNoiDung.setText(content);
        }
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
        confirmed = false;
        closeDialog();
    }

    /**
     * Xử lý khi nhấn nút "Xác nhận"
     */
    @FXML
    void handleConfirm(ActionEvent event) {
        confirmed = true;
        closeDialog();
    }

    /**
     * Đóng dialog
     */
    private void closeDialog() {
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Kiểm tra xem người dùng có xác nhận không
     */
    public boolean isConfirmed() {
        return confirmed;
    }
} 
