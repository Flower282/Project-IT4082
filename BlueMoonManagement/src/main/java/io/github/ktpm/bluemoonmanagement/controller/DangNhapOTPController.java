package io.github.ktpm.bluemoonmanagement.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DangNhapOTPController {

    @FXML
    private Button buttonGuiOTP;

    @FXML
    private Button buttonQuayLaiDangNhap;

    @FXML
    private Button buttonTiepTheo;

    @FXML
    private StackPane stackRoot;

    @FXML
    private Text textError;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldOTP;

    @FXML
    void QuayLaiDangNhapClicked(ActionEvent event) {
        try {
            // Tải lại file fxml của màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dang_nhap.fxml"));
            Scene scene = new Scene(loader.load());

            // Lấy Stage hiện tại (cửa sổ đang hiển thị OTP)
            Stage stage = (Stage) buttonQuayLaiDangNhap.getScene().getWindow();

            // Đóng cửa sổ OTP hiện tại
            stage.close();

            // Tạo cửa sổ mới cho màn hình đăng nhập
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            textError.setText("Có lỗi xảy ra khi quay lại màn hình đăng nhập.");
        }
    }


}

