package io.github.ktpm.bluemoonmanagement.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.QuanLyTaiKhoanService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.springframework.context.ApplicationContext;

public class ChiTietTaiKhoanController {

    @FXML
    private Button buttonChinhSua;

    @FXML
    private FontAwesomeIcon buttonClose;

    @FXML
    private Button buttonLuu;

    @FXML
    private Button buttonXoaTaiKhoan;

    @FXML
    private Button button_close_up;

    @FXML
    private ComboBox<?> comboBoxVaiTro;

    @FXML
    private HBox hBoxChinhSuaXoaTaiKhoan;

    @FXML
    private Label labelTieuDe;

    @FXML
    private Text textError;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldHoVaTen;

    @FXML
    void ChinhXuaTaiKhoanClicked(ActionEvent event) {

    }

    @FXML
    void LuuClicked(ActionEvent event) {

    }

    @FXML
    void XoaTaiKhoanCicked(ActionEvent event) {

    }

    public void setApplicationContext(ApplicationContext applicationContext) {
    }

    public void setTaiKhoanService(QuanLyTaiKhoanService taiKhoanService) {
    }

    public void setTaiKhoanData(ThongTinTaiKhoanDto taiKhoanDto) {
    }
}
