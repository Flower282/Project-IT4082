package io.github.ktpm.bluemoonmanagement.controller;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangKiDto;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DangKiService;
import io.github.ktpm.bluemoonmanagement.util.FxViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThemTaiKhoanController {

    @FXML
    private Button buttonChinhSua;

    @FXML
    private FontAwesomeIcon buttonClose;

    @FXML
    private Button buttonLuu;

    @FXML
    private Button buttonThemCuDan;

    @FXML
    private Button buttonXoaTaiKhoan;

    @FXML
    private Button button_close_up;

    @FXML
    private CheckBox checkBoxHienMatKhau;

    @FXML
    private ComboBox<?> comboBoxVaiTro;

    @FXML
    private HBox hBoxChinhSuaXoaTaiKhoan;

    @FXML
    private Label labelTieuDe;

    @FXML
    private PasswordField passwordFieldMatKhau;

    @FXML
    private Text textError;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldHoVaTen;

    @FXML
    private TextField textFieldMatKhau;

    @Autowired
    private DangKiService dangKiService;
    @Autowired
    private FxViewLoader fxViewLoader;


    @FXML
    void HienMatKhauClicked(ActionEvent event) {
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
    void ThemTaiKhoanClicked(ActionEvent event) {
        String hoVaTen = textFieldHoVaTen.getText().trim();
        String email = textFieldEmail.getText().trim();
        String matKhau = passwordFieldMatKhau.getText().trim();
        String vaiTro = comboBoxVaiTro.getValue() != null ? comboBoxVaiTro.getValue().toString() : "Chưa chọn vai trò";

        DangKiDto dangKiDto = new DangKiDto(email,hoVaTen);
        ResponseDto response = dangKiService.dangKiTaiKhoan(dangKiDto);

        if (response.isSuccess()) {
            textError.setText(response.getMessage());
            textError.setVisible(true);



            // Reset các trường nhập liệu
            textFieldHoVaTen.clear();
            textFieldEmail.clear();
            passwordFieldMatKhau.clear();
            comboBoxVaiTro.getSelectionModel().clearSelection();
        } else if(response.getMessage().equals("Email đã tồn tại")){
            textError.setText(response.getMessage());
            textError.setVisible(true);
        }
        else if(response.getMessage().equals("Bạn không có quyền đăng ký tài khoản. Chỉ Tổ trưởng mới được phép.")){
            textError.setText(response.getMessage());
            textError.setVisible(true);
        }

    }

}
