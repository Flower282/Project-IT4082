package io.github.ktpm.bluemoonmanagement.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;
import io.github.ktpm.bluemoonmanagement.model.dto.phiGuiXe.PhiGuiXeDto;
import io.github.ktpm.bluemoonmanagement.service.khoanThu.KhoanThuService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ThemKhoanThuController {

    @FXML
    private FontAwesomeIcon buttonClose;

    @FXML
    private Button buttonThemKhoanThu;

    @FXML
    private Button button_close_up;



    @FXML
    private ComboBox<String> comboBoxDonViTinh;

    @FXML
    private ComboBox<String> comboBoxLoaiKhoanThu;

    @FXML
    private ComboBox<String> comboBoxPhamVi;

    @FXML
    private ComboBox<String> comboBoxBoPhanQuanLy;

    @FXML
    private DatePicker datePickerHanNop;

    @FXML
    private HBox text;

    @FXML
    private HBox text11;

    @FXML
    private HBox phuongTienHbox;

    @FXML
    private Text textError;

    @FXML
    private TextField textFieldDonGia;

    @FXML
    private TextField textFieldGiaXeDap;

    @FXML
    private TextField textFieldGiaXeMay;

    @FXML
    private TextField textFieldGiaXeOTo;

    @FXML
    private TextField textFieldTenCoQuan;

    @FXML
    private TextField textFieldTenKhoanThu;


    @Autowired
    private KhoanThuService khoanThuService;

    public void initialize() {
        // Khởi tạo các ComboBox với dữ liệu mẫu
        comboBoxLoaiKhoanThu.getItems().addAll("Bắt buộc", "Tự nguyện");
        comboBoxDonViTinh.getItems().addAll("Diện tích", "Căn hộ", "Phương tiện");
        comboBoxPhamVi.getItems().addAll(
            "Tất cả", 
            "Căn hộ đang sử dụng"
        );
        comboBoxBoPhanQuanLy.getItems().addAll("Ban quản lý", "Bên thứ 3");

        // Lắng nghe sự thay đổi của comboBoxDonViTinh để kiểm tra giá trị
        comboBoxDonViTinh.setOnAction(this::onDonViTinhChanged);

        // Gọi hàm xử lý ban đầu
        onDonViTinhChanged(null); // Kiểm tra ngay khi bắt đầu
        
        // Setup close button
        if (button_close_up != null) {
            button_close_up.setOnAction(this::handleClose);
        }
    }
    @FXML
    private void onDonViTinhChanged(ActionEvent event) {
        // Kiểm tra nếu "Phương tiện" được chọn
        if ("Phương tiện".equals(comboBoxDonViTinh.getValue())) {
            phuongTienHbox.setVisible(true);
            // Hiển thị các trường nhập liệu cho phương tiện và cho phép nhập
            textFieldGiaXeDap.setDisable(false);
            textFieldGiaXeMay.setDisable(false);
            textFieldGiaXeOTo.setDisable(false);

            textFieldGiaXeDap.setVisible(true);
            textFieldGiaXeMay.setVisible(true);
            textFieldGiaXeOTo.setVisible(true);
        } else {
            phuongTienHbox.setVisible(false);
            // Ẩn các trường nhập liệu cho phương tiện và vô hiệu hóa chúng
            textFieldGiaXeDap.setDisable(true);
            textFieldGiaXeMay.setDisable(true);
            textFieldGiaXeOTo.setDisable(true);

            textFieldGiaXeDap.setVisible(false);
            textFieldGiaXeMay.setVisible(false);
            textFieldGiaXeOTo.setVisible(false);
        }
    }
    @FXML
    void ThemKhoanThuClicked(ActionEvent event) {

        if (isAnyFieldEmpty()) {
            textError.setText("Vui lòng điền đầy đủ thông tin!");
            textError.setStyle("-fx-fill: red;");
            return; // Ngừng xử lý nếu có trường trống
        }
        // Lấy thông tin từ các trường nhập liệu
        String tenKhoanThu = textFieldTenKhoanThu.getText();  // Tên khoản thu
        String loaiKhoanThu = comboBoxLoaiKhoanThu.getValue().toString();  // Loại khoản thu
        String boPhanQuanLy = comboBoxBoPhanQuanLy.getValue().toString();  // Bộ phận quản lý
        String thoiHanNop = datePickerHanNop.getValue().toString();  // Thời hạn (ngày nộp)
        String donViTinh = comboBoxDonViTinh.getValue().toString();  // Đơn vị tính
        int soTien = Integer.parseInt(textFieldDonGia.getText());  // Số tiền
        String phamVi = comboBoxPhamVi.getValue().toString();  // Phạm vi
        // Lấy số tiền từ các TextField
        String giaXeDap = textFieldGiaXeDap.getText();  // Giá xe đạp
        String giaXeMay = textFieldGiaXeMay.getText();  // Giá xe máy
        String giaXeOTo = textFieldGiaXeOTo.getText();  // Giá xe ô tô
        String donGia = textFieldDonGia.getText();  // Đơn giá

        // Lấy ngày tạo (hôm nay)
        String ngayTao = java.time.LocalDate.now().toString();  // Ngày tạo (hôm nay)

        KhoanThuDto khoanThuDto = new KhoanThuDto();
        khoanThuDto.setTenKhoanThu(tenKhoanThu);
        khoanThuDto.setBatBuoc("Bắt buộc".equals(loaiKhoanThu));  // Kiểm tra loại khoản thu
        khoanThuDto.setDonViTinh(donViTinh);
        khoanThuDto.setSoTien(soTien);
        khoanThuDto.setPhamVi(phamVi);
        khoanThuDto.setNgayTao(java.time.LocalDate.parse(ngayTao));
        khoanThuDto.setThoiHan(java.time.LocalDate.parse(thoiHanNop));
        khoanThuDto.setGhiChu(boPhanQuanLy); // Sử dụng bộ phận quản lý làm ghi chú

        // Tạo danh sách các khoản phí gửi xe
        PhiGuiXeDto phiGuiXeDto = new PhiGuiXeDto();
//        phiGuiXeDto.setGiaXeDap(Integer.parseInt(giaXeDap));
//        phiGuiXeDto.setGiaXeMay(Integer.parseInt(giaXeMay));
//        phiGuiXeDto.setGiaXeOTo(Integer.parseInt(giaXeOTo));
//        phiGuiXeDto.setDonGia(Integer.parseInt(donGia));
        khoanThuDto.setPhiGuiXeList(java.util.Collections.singletonList(phiGuiXeDto));
        // Gọi service để thêm khoản thu


        ResponseDto response = khoanThuService.addKhoanThu(khoanThuDto);
        if (response.isSuccess()) {
            textError.setText("Thêm khoản thu thành công!");
            textError.setStyle("-fx-fill: green;");

        } else {
            textError.setText("Lỗi: " + response.getMessage());
            textError.setStyle("-fx-fill: red;");
        }
    }
    private boolean isAnyFieldEmpty() {
        // Kiểm tra các trường ComboBox có giá trị không
        if (comboBoxLoaiKhoanThu.getValue() == null ||
                comboBoxDonViTinh.getValue() == null ||
                comboBoxPhamVi.getValue() == null ||
                comboBoxBoPhanQuanLy.getValue() == null) {
            return true;
        }

        // Kiểm tra các TextField có giá trị không
        if (textFieldTenKhoanThu.getText().isEmpty() ||
                textFieldDonGia.getText().isEmpty()) {
            return true;
        }

        // Kiểm tra DatePicker có giá trị không
        if (datePickerHanNop.getValue() == null) {
            return true;
        }

        // Kiểm tra nếu giá trị xe đạp, xe máy, và ô tô có bị trống không nếu bạn muốn kiểm tra chúng
        if ("Phương tiện".equals(comboBoxDonViTinh.getValue())) {
            if (textFieldGiaXeDap.getText().isEmpty() ||
                    textFieldGiaXeMay.getText().isEmpty() ||
                    textFieldGiaXeOTo.getText().isEmpty()) {
                return true;
            }
        }

        return false; // Tất cả các trường đều có giá trị
    }
    
    @FXML
    private void handleClose(ActionEvent event) {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) button_close_up.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("Lỗi khi đóng cửa sổ: " + e.getMessage());
        }
    }
}
