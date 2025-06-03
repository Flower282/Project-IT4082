package io.github.ktpm.bluemoonmanagement.controller;


import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangKiDto;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DangKiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThemTaiKhoanController {

    @FXML
    private Text textError;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldHoVaTen;

    @FXML
    private Button buttonThemCuDan;

    @Autowired
    private DangKiService dangKiService;

    @FXML
    void ThemTaiKhoanClicked(ActionEvent event) {
        buttonThemCuDan.setDisable(true);
        String hoVaTen = textFieldHoVaTen.getText().trim();
        String email = textFieldEmail.getText().trim();
        if(hoVaTen.isEmpty() || email.isEmpty()) {
            textError.setText("Vui lòng nhập đầy đủ thông tin!");
            textError.setVisible(true);
            buttonThemCuDan.setDisable(false);
            return;
        }
        DangKiDto dangKiDto = new DangKiDto(email,hoVaTen);
        ResponseDto response = dangKiService.dangKiTaiKhoan(dangKiDto);

        if (response.isSuccess()) {
            textError.setText(response.getMessage());
            textError.setVisible(true);
            // Reset các trường nhập liệu
            textFieldHoVaTen.clear();
            textFieldEmail.clear();
        } else {
            textError.setText(response.getMessage());
            textError.setVisible(true);
        }
        buttonThemCuDan.setDisable(false);
    }

}
