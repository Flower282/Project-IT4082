package io.github.ktpm.bluemoonmanagement.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DoiMatKhauController {

    @FXML
    private Button buttonLuuMatKhau;

    @FXML
    private CheckBox checkBoxHienMatKhau;

    @FXML
    private CheckBox checkBoxHienXacNhanMatKhau;

    @FXML
    private PasswordField passwordFieldMatKhau;

    @FXML
    private PasswordField passwordFieldXacNhanMatKhau;

    @FXML
    private StackPane stackRoot;

    @FXML
    private Text textError;

    @FXML
    private TextField textFieldMatKhau;

    @FXML
    private TextField textFieldXacNhanMatKhau;

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
    void LuuMatKhauClicked(ActionEvent event) {
        // Lấy mật khẩu mới và xác nhận mật khẩu từ các trường nhập liệu
        String matKhau = passwordFieldMatKhau.getText().trim();
        String xacNhanMatKhau = passwordFieldXacNhanMatKhau.getText().trim();

        // Kiểm tra xem mật khẩu và xác nhận mật khẩu có khớp không
        if (!matKhau.equals(xacNhanMatKhau)) {
            textError.setText("Mật khẩu và xác nhận mật khẩu không khớp.");
            textError.setVisible(true);
            return;
        }

        // Kiểm tra độ dài mật khẩu (ví dụ: mật khẩu phải có ít nhất 6 ký tự)
        if (matKhau.length() < 6) {
            textError.setText("Mật khẩu phải có ít nhất 6 ký tự.");
            textError.setVisible(true);
            return;
        }

        // Giả sử bạn lưu mật khẩu vào cơ sở dữ liệu hoặc một nơi lưu trữ an toàn nào đó
        // Ví dụ: lưu mật khẩu thành công
        boolean isPasswordSaved = saveNewPassword(matKhau);  // Giả sử hàm saveNewPassword() lưu mật khẩu

        if (isPasswordSaved) {
            // Nếu lưu mật khẩu thành công
            textError.setText("Mật khẩu đã được lưu thành công.");
            textError.setStyle("-fx-fill: green;");
            textError.setVisible(true);
        } else {
            // Nếu có lỗi khi lưu mật khẩu
            textError.setText("Có lỗi xảy ra khi lưu mật khẩu.");
            textError.setStyle("-fx-fill: red;");
            textError.setVisible(true);
        }
    }

    private boolean saveNewPassword(String newPassword) {
        // hàm thay đôi mật khẩu trong database
        return true;  // Trả về true nếu lưu thành công, false nếu có lỗi
    }

}

