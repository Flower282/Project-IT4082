package io.github.ktpm.bluemoonmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Component;

@Component
public class KhungController implements Initializable{

    @FXML
    private Button buttonCanHo;

    @FXML
    private Button buttonCuDan;

    @FXML
    private Button buttonDangXuat;

    @FXML
    private Button buttonHoSo;

    @FXML
    private Button buttonKhoanThu;

    @FXML
    private Button buttonTaiKhoan;

    @FXML
    private Button buttonThuPhi;

    @FXML
    private Button buttonTrangChu;

    @FXML
    private Label labelAccountName;

    @FXML
    private Label labelScreenName;

    @FXML
    private BorderPane mainBorderPane;


    private Home_list centerController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("KhungController được khởi tạo");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/trang_chu_danh_sach.fxml"));
            Parent trangChuContent = loader.load();

            // Set content vào center của BorderPane
            mainBorderPane.setCenter(trangChuContent);
            centerController = loader.getController();
            centerController.setParentController(this);
            updateScreenLabel("Trang chủ");

        } catch (IOException e) {
            System.err.println("Không thể load trang chủ danh sách: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateScreenLabel(String screenName) {
        if (labelScreenName != null) {
            labelScreenName.setText(screenName);
        }
    }

    public void setAccountName(String accountName) {
        if (labelAccountName != null) {
            labelAccountName.setText("Xin chào, " + accountName);
        }
    }

    @FXML
    void goToCanHo(ActionEvent event) {
        centerController.show("CanHo");
        updateScreenLabel("Danh sách căn hộ");
    }

    @FXML
    void goToCuDan(ActionEvent event) {
        centerController.show("CuDan");
        updateScreenLabel("Danh sách cư dân");
    }

    @FXML
    void goToHoSo(ActionEvent event) {
    }

    @FXML
    void goToKhoanThu(ActionEvent event) {
        centerController.show("KhoanThu");
        updateScreenLabel("Danh sách khoản thu");
    }

    @FXML
    void goToTaiKhoan(ActionEvent event) {
        centerController.show("TaiKhoan");
        updateScreenLabel("Danh sách tài khoản");
    }

    @FXML
    void goToTrangChu(ActionEvent event) {
        centerController.show("TrangChu");
        updateScreenLabel("Trang chủ");
    }

    @FXML
    void gotoLichSuThu(ActionEvent event) {
        centerController.show("LichSuThu");
        updateScreenLabel("Lịch sử thu phí");
    }

    @FXML
    void handleDangXuat(ActionEvent event) {

    }

}
