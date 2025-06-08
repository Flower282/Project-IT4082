package io.github.ktpm.bluemoonmanagement.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;
import io.github.ktpm.bluemoonmanagement.model.dto.phiGuiXe.PhiGuiXeDto;
import io.github.ktpm.bluemoonmanagement.service.khoanThu.KhoanThuService;
import io.github.ktpm.bluemoonmanagement.session.Session;
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

import java.util.ArrayList;
import java.util.List;

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
    
    @Autowired
    private org.springframework.context.ApplicationContext applicationContext;

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
        // Kiểm tra quyền trước khi thực hiện
        if (!hasPermission()) {
            textError.setText("Chỉ Kế toán mới có quyền thêm khoản thu.");
            textError.setStyle("-fx-fill: red;");
            return;
        }

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

        // Gọi service để thêm khoản thu
        ResponseDto response = khoanThuService.addKhoanThu(khoanThuDto);
        if (response.isSuccess()) {
            textError.setText("Thêm khoản thu thành công!");
            textError.setStyle("-fx-fill: green;");

            // Refresh khoản thu table
            refreshKhoanThuTable();
            
            // Close window after successful addition
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(1500); // Show success message for 1.5 seconds
                    handleClose(null);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    handleClose(null);
                }
            });

        } else {
            textError.setText("Lỗi: " + response.getMessage());
            textError.setStyle("-fx-fill: red;");
        }
        // Tạo danh sách phiGuiXeDtos nếu đơn vị tính là "Phương tiện"
        String maKhoanThu = khoanThuDto.getMaKhoanThu();
        List<PhiGuiXeDto> phiGuiXeDtos= new ArrayList<>();
        if ("Phương tiện".equals(donViTinh)) {
            // Chỉ thêm các khoản phí nếu đơn vị tính là "Phương tiện"
            if (!giaXeDap.isEmpty()) {
                phiGuiXeDtos.add(new PhiGuiXeDto("Xe đạp", Integer.parseInt(giaXeDap),maKhoanThu));
            }
            if (!giaXeMay.isEmpty()) {
                phiGuiXeDtos.add(new PhiGuiXeDto("Xe máy", Integer.parseInt(giaXeMay),maKhoanThu));
            }
            if (!giaXeOTo.isEmpty()) {
                phiGuiXeDtos.add(new PhiGuiXeDto( "Ô tô", Integer.parseInt(giaXeOTo),maKhoanThu));
            }
        }
        // Gán danh sách phiGuiXeDtos vào khoanThuDto
        khoanThuDto.setPhiGuiXeList(phiGuiXeDtos);

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
    
    /**
     * Refresh khoản thu table in Home_list controller
     */
    private void refreshKhoanThuTable() {
        try {
            System.out.println("🔄 Refreshing fee table...");
            
            javafx.application.Platform.runLater(() -> {
                try {
                    // Find all windows and look for Home_list controller
                    for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                        if (window instanceof javafx.stage.Stage) {
                            javafx.stage.Stage stage = (javafx.stage.Stage) window;
                            javafx.scene.Scene scene = stage.getScene();
                            if (scene != null && scene.getRoot() != null) {
                                // Try to find the Home_list controller through scene graph
                                findAndRefreshKhoanThuController(scene.getRoot());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to refresh fee data: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("ERROR: Exception in refreshKhoanThuTable: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Find and refresh Home_list controller for fees
     */
    private void findAndRefreshKhoanThuController(javafx.scene.Node node) {
        try {
            // Check if the node has a controller property
            Object controller = node.getProperties().get("controller");
            if (controller instanceof io.github.ktpm.bluemoonmanagement.controller.Home_list) {
                io.github.ktpm.bluemoonmanagement.controller.Home_list homeListController = (io.github.ktpm.bluemoonmanagement.controller.Home_list) controller;
                
                // Switch to fees tab
                java.lang.reflect.Method gotoKhoanThuMethod = homeListController.getClass().getDeclaredMethod("gotoKhoanThu", javafx.event.ActionEvent.class);
                gotoKhoanThuMethod.setAccessible(true);
                gotoKhoanThuMethod.invoke(homeListController, (javafx.event.ActionEvent) null);
                
                // Refresh fee data (now public method)
                homeListController.refreshKhoanThuData();
                
                System.out.println("✅ Fee data refreshed successfully");
                return;
            }
            
            // Recursively search in children if it's a Parent node
            if (node instanceof javafx.scene.Parent) {
                javafx.scene.Parent parent = (javafx.scene.Parent) node;
                for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                    findAndRefreshKhoanThuController(child);
                }
            }
        } catch (Exception e) {
            // Silently continue searching - this is expected for most nodes
        }
    }

    private boolean hasPermission() {
        try {
            if (Session.getCurrentUser() == null) return false;
            
            // Sử dụng reflection để lấy vaiTro do vấn đề Lombok
            java.lang.reflect.Field vaiTroField = Session.getCurrentUser().getClass().getDeclaredField("vaiTro");
            vaiTroField.setAccessible(true);
            String vaiTro = (String) vaiTroField.get(Session.getCurrentUser());
            
            // Chỉ Kế toán mới có quyền thêm khoản thu
            return "Kế toán".equals(vaiTro);
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra quyền: " + e.getMessage());
            return false;
        }
    }
}
