package io.github.ktpm.bluemoonmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.FxView;
import io.github.ktpm.bluemoonmanagement.util.FxViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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

    @Autowired
    private FxViewLoader fxViewLoader;

    @Autowired
    private CanHoService canHoService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        buttonTaiKhoan.setDisable(true);
        
        System.out.println("KhungController được khởi tạo");
        System.out.println("CanHoService trong KhungController: " + (canHoService != null ? "Có" : "NULL"));
        
        String name = Session.getCurrentUser().getHoTen();
        setAccountName(name);
        try {
            FxView<Home_list> fxView = fxViewLoader.loadFxView("/view/trang_chu_danh_sach.fxml");
            mainBorderPane.setCenter(fxView.getView());
            this.centerController = fxView.getController(); // Gán đúng controller
            
            // Inject services vào Home_list controller
            if (this.centerController != null) {
                this.centerController.injectServices(canHoService);
                this.centerController.setParentController(this);
            }
            
            updateScreenLabel("Trang chủ");
        } catch (IOException e) {
            System.err.println("Không thể load trang chủ: " + e.getMessage());
        }

        if (Session.getCurrentUser().getVaiTro().equals("Tổ trưởng")) {
            buttonTaiKhoan.setDisable(false);
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
        centerController.show("HoSo");
        updateScreenLabel("Hồ sơ");
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
        try {
            // Tải file FXML mới (khung.fxml)
            Parent mainView = fxViewLoader.loadView("/view/dang_nhap.fxml");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainView));
            stage.setTitle("Trang chính");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu không thể tải file FXML
        }
    }


}
