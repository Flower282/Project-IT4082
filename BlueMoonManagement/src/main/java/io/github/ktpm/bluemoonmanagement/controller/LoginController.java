package io.github.ktpm.bluemoonmanagement.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangNhapDto;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DangNhapServive;
import io.github.ktpm.bluemoonmanagement.util.FxViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginController implements Initializable {

    @FXML
    private Button buttonDangNhap;

    @FXML
    private Button buttonNhanDienKhuonMat;

    @FXML
    private Button buttonQuenMatKhau;

    @FXML
    private CheckBox checkBoxHienMatKhau;

    @FXML
    private PasswordField passwordFieldMatKhau;

    @FXML
    private StackPane stackRoot;

    @FXML
    private Text textError;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldMatKhau;
    @FXML
    private Label labelScreenName;
    @Autowired
    private DangNhapServive dangNhapServive;
    @Autowired
    private FxViewLoader fxViewLoader;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login được khởi tạo");
        textFieldMatKhau.setVisible(false);
        passwordFieldMatKhau.setVisible(true);
        checkBoxHienMatKhau.setSelected(false);
    }

    @FXML
    void showPassword(ActionEvent event) {
        if (checkBoxHienMatKhau.isSelected()) {
            // Nếu được chọn thì hiển thị mật khẩu
            textFieldMatKhau.setText(passwordFieldMatKhau.getText());
            textFieldMatKhau.setVisible(true);
            passwordFieldMatKhau.setVisible(false);
        } else {
            // Nếu bỏ chọn thì ẩn mật khẩu
            passwordFieldMatKhau.setText(textFieldMatKhau.getText());
            passwordFieldMatKhau.setVisible(true);
            textFieldMatKhau.setVisible(false);
        }
    }
    @FXML
    void dangNhapPressed(ActionEvent event) {
        // Lấy tài khoản và mật khẩu từ các TextField
        String email = textFieldEmail.getText().trim();
        String password = passwordFieldMatKhau.getText().trim();

        if(email.isEmpty()) {
            textError.setText("Vui lòng nhập đầy đủ thông tin đăng nhập.");
            textError.setVisible(true);
            return;
        }
        DangNhapDto dangNhapDto = new DangNhapDto(email, password);
        ResponseDto response = dangNhapServive.dangNhap(dangNhapDto);

        if(response.isSuccess()) {
            try {
                // Tải file FXML mới (khung.fxml)
                Parent mainView = fxViewLoader.loadView("/view/khung.fxml");
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(mainView));
                stage.setTitle("Trang chính");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi nếu không thể tải file FXML
            }
        }
        else  {
            textError.setText(response.getMessage());
            textError.setVisible(true);
        }


        // Kiểm tra tài khoản và mật khẩu
        if (email.equals("abcde") && password.equals("12345")) {
            try {
                // Tải file FXML mới (khung.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/khung.fxml"));
                Parent root = loader.load();

                // Tạo cửa sổ mới (Stage)
                Stage newStage = new Stage();
                Scene newScene = new Scene(root);
                newStage.setScene(newScene);

                // Hiển thị cửa sổ mới
                newStage.show();

                // Đóng cửa sổ hiện tại (cửa sổ đăng nhập)
                Stage currentStage = (Stage) stackRoot.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý lỗi nếu không thể tải file FXML
            }
        } else {
            // Hiển thị thông báo sai tài khoản hoặc mật khẩu
            textError.setText("Sai tài khoản hoặc mật khẩu");
            textError.setVisible(true);
        }
    }
    @FXML
    void DangNhapBangOTPClicked(ActionEvent event) {
        try {
            // Tải file FXML của màn hình OTP
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dang_nhap_otp.fxml"));
            Parent root = loader.load();

            // Tạo cửa sổ mới (Stage) cho màn hình OTP
            Stage newStage = new Stage();
            Scene newScene = new Scene(root);
            newStage.setScene(newScene);

            // Hiển thị cửa sổ OTP
            newStage.show();

            // Đóng cửa sổ hiện tại (cửa sổ đăng nhập)
            Stage currentStage = (Stage) stackRoot.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu không thể tải file FXML của màn hình OTP
            textError.setText("Có lỗi xảy ra khi chuyển đến màn hình OTP.");
            textError.setVisible(true);
        }
    }
}
