package io.github.ktpm.bluemoonmanagement.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;
import io.github.ktpm.bluemoonmanagement.model.dto.phiGuiXe.PhiGuiXeDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.service.khoanThu.KhoanThuService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private Button buttonChinhSua;

    @FXML
    private Button buttonLuu;

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
    private HBox hBoxDonGia;

    @FXML
    private HBox hBoxDonGiaPhuongTien;

    @FXML
    private VBox vBoxTenCoQuan;

    @FXML
    private VBox vBoxDonViTinhVaDonGia;

    @FXML
    private javafx.scene.control.Label labelTieuDe;

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

    // Fields for edit mode
    private boolean isEditMode = false;
    private String originalMaKhoanThu;
    private KhoanThuDto currentKhoanThu;

    public void initialize() {
        // Khởi tạo các ComboBox với dữ liệu mẫu
        comboBoxLoaiKhoanThu.getItems().addAll("Bắt buộc", "Tự nguyện");
        comboBoxLoaiKhoanThu.setValue("Bắt buộc"); // Mặc định chọn "Bắt buộc"
        
        comboBoxDonViTinh.getItems().addAll("Diện tích", "Căn hộ", "Phương tiện");
        
        comboBoxPhamVi.getItems().addAll(
            "Tất cả", 
            "Căn hộ đang sử dụng"
        );
        comboBoxPhamVi.setValue("Căn hộ đang sử dụng"); // Mặc định chọn phổ biến nhất
        
        comboBoxBoPhanQuanLy.getItems().addAll("Ban quản lý", "Bên thứ 3");
        comboBoxBoPhanQuanLy.setValue("Ban quản lý"); // Mặc định chọn "Ban quản lý"

        // Lắng nghe sự thay đổi của comboBoxDonViTinh để kiểm tra giá trị
        comboBoxDonViTinh.setOnAction(this::onDonViTinhChanged);

        // Lắng nghe sự thay đổi của comboBoxBoPhanQuanLy để hiển thị trường tên cơ quan
        comboBoxBoPhanQuanLy.setOnAction(this::onBoPhanQuanLyChanged);

        // Enable trường đơn giá ban đầu
        if (hBoxDonGia != null) {
            hBoxDonGia.setDisable(false);
        }
        if (textFieldDonGia != null) {
            textFieldDonGia.setDisable(false);
        }
        
        // Debug: Check if vehicle fields are initialized
        System.out.println("DEBUG INIT: textFieldGiaXeDap null? " + (textFieldGiaXeDap == null));
        System.out.println("DEBUG INIT: textFieldGiaXeMay null? " + (textFieldGiaXeMay == null));
        System.out.println("DEBUG INIT: textFieldGiaXeOTo null? " + (textFieldGiaXeOTo == null));
        System.out.println("DEBUG INIT: hBoxDonGiaPhuongTien null? " + (hBoxDonGiaPhuongTien == null));

        // Thiết lập format tiền cho các text field
        setupMoneyFormatting();

        // Gọi hàm xử lý ban đầu
        onDonViTinhChanged(null); // Kiểm tra ngay khi bắt đầu
        onBoPhanQuanLyChanged(null); // Kiểm tra bộ phận quản lý
        
        // Setup close button
        if (button_close_up != null) {
            button_close_up.setOnAction(this::handleClose);
        }
    }
    @FXML
    private void onDonViTinhChanged(ActionEvent event) {
        System.out.println("DEBUG: onDonViTinhChanged - Selected: " + comboBoxDonViTinh.getValue());
        
        // Kiểm tra nếu "Phương tiện" được chọn
        if ("Phương tiện".equals(comboBoxDonViTinh.getValue())) {
            System.out.println("DEBUG: Switching to vehicle mode - showing vehicle price fields");
            
            // Ẩn trường đơn giá chung
            if (hBoxDonGia != null) {
                hBoxDonGia.setVisible(false);
                hBoxDonGia.setDisable(true);
                System.out.println("DEBUG: ✅ hBoxDonGia hidden");
            }
            if (textFieldDonGia != null) {
                textFieldDonGia.setDisable(true);
                textFieldDonGia.clear();
                System.out.println("DEBUG: ✅ textFieldDonGia disabled and cleared");
            }
            
            // HIỂN THỊ và enable các ô giá xe
            if (hBoxDonGiaPhuongTien != null) {
                hBoxDonGiaPhuongTien.setVisible(true);
                hBoxDonGiaPhuongTien.setDisable(false);
                System.out.println("DEBUG: ✅ hBoxDonGiaPhuongTien shown and enabled");
            }
            
            // Enable và hiển thị tất cả ô giá xe 
            if (textFieldGiaXeDap != null) {
                textFieldGiaXeDap.setDisable(false);
            textFieldGiaXeDap.setVisible(true);
                System.out.println("DEBUG: ✅ textFieldGiaXeDap enabled and visible");
            }
            if (textFieldGiaXeMay != null) {
                textFieldGiaXeMay.setDisable(false);
            textFieldGiaXeMay.setVisible(true);
                System.out.println("DEBUG: ✅ textFieldGiaXeMay enabled and visible");
            }
            if (textFieldGiaXeOTo != null) {
                textFieldGiaXeOTo.setDisable(false);
            textFieldGiaXeOTo.setVisible(true);
                System.out.println("DEBUG: ✅ textFieldGiaXeOTo enabled and visible");
            }
            
            System.out.println("DEBUG: 🚗 Vehicle mode activated - vehicle price fields are now visible");
        } else {
            System.out.println("DEBUG: Switching to non-vehicle mode - showing general price field");
            
            // Hiển thị và enable trường đơn giá chung
            if (hBoxDonGia != null) {
                hBoxDonGia.setVisible(true);
                hBoxDonGia.setDisable(false);
                System.out.println("DEBUG: ✅ hBoxDonGia shown and enabled");
            }
            if (textFieldDonGia != null) {
                textFieldDonGia.setDisable(false);
                System.out.println("DEBUG: ✅ textFieldDonGia ENABLED for input");
            }
            
            // Ẩn và disable container phương tiện
            if (hBoxDonGiaPhuongTien != null) {
                hBoxDonGiaPhuongTien.setVisible(false);
                hBoxDonGiaPhuongTien.setDisable(true);
                System.out.println("DEBUG: ✅ hBoxDonGiaPhuongTien container hidden");
            }
            
            // DISABLE tất cả các ô giá xe và clear giá trị
            if (textFieldGiaXeDap != null) {
                textFieldGiaXeDap.setDisable(true);
                textFieldGiaXeDap.clear();
            textFieldGiaXeDap.setVisible(false);
                System.out.println("DEBUG: ✅ textFieldGiaXeDap disabled and hidden");
            }
            if (textFieldGiaXeMay != null) {
                textFieldGiaXeMay.setDisable(true);
                textFieldGiaXeMay.clear();
            textFieldGiaXeMay.setVisible(false);
                System.out.println("DEBUG: ✅ textFieldGiaXeMay disabled and hidden");
            }
            if (textFieldGiaXeOTo != null) {
                textFieldGiaXeOTo.setDisable(true);
                textFieldGiaXeOTo.clear();
            textFieldGiaXeOTo.setVisible(false);
                System.out.println("DEBUG: ✅ textFieldGiaXeOTo disabled and hidden");
            }
            
            System.out.println("DEBUG: 💰 General price mode activated - only general price field is enabled");
        }
    }

    @FXML
    private void onBoPhanQuanLyChanged(ActionEvent event) {
        System.out.println("DEBUG: onBoPhanQuanLyChanged - Selected: " + comboBoxBoPhanQuanLy.getValue());
        
        // Kiểm tra nếu "Bên thứ 3" được chọn
        if ("Bên thứ 3".equals(comboBoxBoPhanQuanLy.getValue())) {
            System.out.println("DEBUG: Switching to 3rd party mode - disabling Ban quản lý controls");
            
            // Hiển thị phần tên cơ quan
            if (vBoxTenCoQuan != null) {
                vBoxTenCoQuan.setVisible(true);
                vBoxTenCoQuan.setDisable(false);
                System.out.println("DEBUG: vBoxTenCoQuan enabled and visible");
            }
            // Enable trường nhập tên cơ quan
            if (textFieldTenCoQuan != null) {
                textFieldTenCoQuan.setDisable(false);
                System.out.println("DEBUG: textFieldTenCoQuan enabled");
            }
            
            // Ẩn và disable phần đơn vị tính và đơn giá (các nút của Ban quản lý)
            if (vBoxDonViTinhVaDonGia != null) {
                vBoxDonViTinhVaDonGia.setVisible(false);
                vBoxDonViTinhVaDonGia.setDisable(true);
                System.out.println("DEBUG: vBoxDonViTinhVaDonGia hidden and disabled (Ban quản lý controls)");
            }
            
            // Clear và disable các combobox của Ban quản lý
            if (comboBoxDonViTinh != null) {
                comboBoxDonViTinh.setValue(null);
                comboBoxDonViTinh.setDisable(true);
                System.out.println("DEBUG: comboBoxDonViTinh cleared and disabled");
            }
            
        } else {
            System.out.println("DEBUG: Switching to Ban quản lý mode - enabling controls");
            
            // Ẩn phần tên cơ quan
            if (vBoxTenCoQuan != null) {
                vBoxTenCoQuan.setVisible(false);
                vBoxTenCoQuan.setDisable(true);
                System.out.println("DEBUG: vBoxTenCoQuan hidden and disabled");
            }
            // Disable và clear trường nhập tên cơ quan
            if (textFieldTenCoQuan != null) {
                textFieldTenCoQuan.setDisable(true);
                textFieldTenCoQuan.clear();
                System.out.println("DEBUG: textFieldTenCoQuan cleared and disabled");
            }
            
            // Hiển thị và enable lại phần đơn vị tính và đơn giá
            if (vBoxDonViTinhVaDonGia != null) {
                vBoxDonViTinhVaDonGia.setVisible(true);
                vBoxDonViTinhVaDonGia.setDisable(false);
                System.out.println("DEBUG: vBoxDonViTinhVaDonGia shown and enabled");
            }
            
            // Enable lại combobox đơn vị tính
            if (comboBoxDonViTinh != null) {
                comboBoxDonViTinh.setDisable(false);
                System.out.println("DEBUG: comboBoxDonViTinh enabled");
            }
            
            // Đảm bảo trường đơn giá được enable khi quay lại chế độ bình thường
            if (hBoxDonGia != null) {
                hBoxDonGia.setDisable(false);
                System.out.println("DEBUG: hBoxDonGia enabled");
            }
            if (textFieldDonGia != null) {
                textFieldDonGia.setDisable(false);
                System.out.println("DEBUG: textFieldDonGia enabled");
            }
            
            // Trigger lại logic đơn vị tính để hiển thị đúng trường đơn giá
            onDonViTinhChanged(null);
        }
    }
    @FXML
    void ThemKhoanThuClicked(ActionEvent event) {
        // Kiểm tra quyền trước khi thực hiện
        if (!hasPermission()) {
            String action = isEditMode ? "chỉnh sửa" : "thêm";
            textError.setText("Chỉ Kế toán mới có quyền " + action + " khoản thu.");
            textError.setStyle("-fx-fill: red;");
            return;
        }
        
        // Nếu ở chế độ edit, gọi method cập nhật
        if (isEditMode) {
            handleUpdateKhoanThu();
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
        String phamVi = comboBoxPhamVi.getValue().toString();  // Phạm vi
        
        // Xử lý đơn vị tính và số tiền tùy theo bộ phận quản lý
        String donViTinh;
        int soTien;
        
        if ("Bên thứ 3".equals(boPhanQuanLy)) {
            // Nếu là bên thứ 3, có thể set giá trị mặc định hoặc để trống
            donViTinh = "Theo hóa đơn";
            soTien = 0; // hoặc giá trị mặc định khác
        } else {
            donViTinh = comboBoxDonViTinh.getValue().toString();  // Đơn vị tính
            
            // Nếu đơn vị tính là "Phương tiện", không cần đọc từ textFieldDonGia
            if ("Phương tiện".equals(donViTinh)) {
                soTien = 0; // Để 0 vì giá sẽ được lưu riêng trong bảng phi_gui_xe
            } else {
                soTien = Integer.parseInt(textFieldDonGia.getText());  // Số tiền từ ô đơn giá chung
            }
        }
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
        // Sử dụng tên cơ quan nếu có, nếu không thì dùng bộ phận quản lý
        String ghiChu = boPhanQuanLy;
        if ("Bên thứ 3".equals(boPhanQuanLy) && textFieldTenCoQuan != null && !textFieldTenCoQuan.getText().isEmpty()) {
            ghiChu = textFieldTenCoQuan.getText();
        }
        khoanThuDto.setGhiChu(ghiChu);

        // Nếu đơn vị tính là "Phương tiện", tạo PhiGuiXeDto từ các text field
        if ("Phương tiện".equals(donViTinh)) {
            System.out.println("DEBUG: 🚗 Vehicle mode - creating vehicle fee list from form");
            ArrayList<PhiGuiXeDto> phiGuiXeList = new ArrayList<>();
            
            // Thêm xe đạp nếu có giá
            if (!giaXeDap.trim().isEmpty()) {
                int price = Integer.parseInt(giaXeDap.trim());
                phiGuiXeList.add(new PhiGuiXeDto("Xe đạp", price, null)); // maKhoanThu sẽ được set sau
                System.out.println("DEBUG: ✅ Added Xe đạp: " + price + " VND");
            }
            
            // Thêm xe máy nếu có giá
            if (!giaXeMay.trim().isEmpty()) {
                int price = Integer.parseInt(giaXeMay.trim());
                phiGuiXeList.add(new PhiGuiXeDto("Xe máy", price, null)); // maKhoanThu sẽ được set sau
                System.out.println("DEBUG: ✅ Added Xe máy: " + price + " VND");
            }
            
            // Thêm xe ô tô nếu có giá
            if (!giaXeOTo.trim().isEmpty()) {
                int price = Integer.parseInt(giaXeOTo.trim());
                phiGuiXeList.add(new PhiGuiXeDto("Ô tô", price, null)); // maKhoanThu sẽ được set sau
                System.out.println("DEBUG: ✅ Added Ô tô: " + price + " VND");
            }
            
            khoanThuDto.setPhiGuiXeList(phiGuiXeList);
            System.out.println("DEBUG: Created " + phiGuiXeList.size() + " vehicle fee entries");
        } else {
            System.out.println("DEBUG: 💰 Non-vehicle mode - no vehicle details needed");
            khoanThuDto.setPhiGuiXeList(new ArrayList<>());
        }

        // Gọi service để thêm khoản thu
        ResponseDto response = khoanThuService.addKhoanThu(khoanThuDto);
        if (response.isSuccess()) {
            textError.setText("Thêm khoản thu thành công!");
            textError.setStyle("-fx-fill: green;");

            // Refresh khoản thu table ngay lập tức
            refreshKhoanThuTable();
            
            // Close window after successful addition với delay ngắn hơn
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(1000); // Show success message for 1 second
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

    }
    
    /**
     * Xử lý cập nhật khoản thu khi ở chế độ edit
     */
    private void handleUpdateKhoanThu() {
        // Hiển thị dialog xác nhận có sẵn
        if (!showConfirmationDialog()) {
            textError.setText("Đã hủy chỉnh sửa khoản thu.");
            textError.setStyle("-fx-fill: orange;");
            return;
        }
        
        if (isAnyFieldEmpty()) {
            textError.setText("Vui lòng điền đầy đủ thông tin!");
            textError.setStyle("-fx-fill: red;");
            return;
        }
        
        try {
            // Tạo DTO với thông tin mới  
            KhoanThuDto updatedDto = createKhoanThuDto();
            updatedDto.setMaKhoanThu(originalMaKhoanThu); // Giữ nguyên mã khoản thu
            
            // Gọi service để cập nhật
            ResponseDto response = khoanThuService.updateKhoanThu(updatedDto);
            
            if (response.isSuccess()) {
                System.out.println("✅ Cập nhật khoản thu thành công!");
                
                // Tự động refresh bảng và chuyển về tab khoản thu
                refreshKhoanThuTableAndGoToTab();
                
                // Refresh cache để đảm bảo dữ liệu phí gửi xe được cập nhật
                refreshCacheAndVehicleFees();
                
                // Đóng tất cả các tab detail đang mở
                closeAllOpenDetailTabs();
                
                // Đóng form hiện tại
                handleClose(null);
                
            } else {
                textError.setText("Lỗi: " + response.getMessage());
                textError.setStyle("-fx-fill: red;");
            }
            
        } catch (Exception e) {
            textError.setText("Lỗi khi cập nhật: " + e.getMessage());
            textError.setStyle("-fx-fill: red;");
        }
    }
    
    /**
     * Hiển thị dialog xác nhận có sẵn
     */
    private boolean showConfirmationDialog() {
        try {
            // Load FXML và controller xác nhận
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/xac_nhan.fxml"));
            javafx.scene.layout.AnchorPane confirmView = loader.load();
            XacNhanController confirmController = loader.getController();
            
            // Thiết lập nội dung
            confirmController.setTitle("Xác nhận chỉnh sửa");
            confirmController.setContent("Bạn có chắc chắn muốn chỉnh sửa khoản thu này?\n" +
                                       "Hành động này không thể hoàn tác.");
            
            // Tạo Stage mới cho dialog
            javafx.stage.Stage confirmStage = new javafx.stage.Stage();
            confirmStage.setScene(new javafx.scene.Scene(confirmView));
            confirmStage.setTitle("Xác nhận");
            confirmStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            
            // Gán owner là cửa sổ hiện tại
            javafx.stage.Stage currentStage = (javafx.stage.Stage) buttonThemKhoanThu.getScene().getWindow();
            confirmStage.initOwner(currentStage);
            
            // Hiển thị dialog và chờ
            confirmStage.showAndWait();
            
            // Trả về kết quả xác nhận
            return confirmController.isConfirmed();
            
        } catch (Exception e) {
            System.err.println("Error showing confirmation dialog: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean isAnyFieldEmpty() {
        System.out.println("=== DEBUG: VALIDATION CHECK START ===");
        
        // Kiểm tra từng trường một cách chi tiết
        System.out.println("DEBUG: LoaiKhoanThu: " + comboBoxLoaiKhoanThu.getValue() + " (null? " + (comboBoxLoaiKhoanThu.getValue() == null) + ")");
        System.out.println("DEBUG: PhamVi: " + comboBoxPhamVi.getValue() + " (null? " + (comboBoxPhamVi.getValue() == null) + ")");
        System.out.println("DEBUG: BoPhanQuanLy: " + comboBoxBoPhanQuanLy.getValue() + " (null? " + (comboBoxBoPhanQuanLy.getValue() == null) + ")");
        
        // Kiểm tra các trường bắt buộc cơ bản
        if (comboBoxLoaiKhoanThu.getValue() == null) {
            System.out.println("❌ MISSING: Loại khoản thu");
            return true;
        }
        if (comboBoxPhamVi.getValue() == null) {
            System.out.println("❌ MISSING: Phạm vi");
            return true;
        }
        if (comboBoxBoPhanQuanLy.getValue() == null) {
            System.out.println("❌ MISSING: Bộ phận quản lý");
            return true;
        }

        // Kiểm tra tên khoản thu
        String tenKhoanThu = textFieldTenKhoanThu.getText();
        System.out.println("DEBUG: TenKhoanThu: '" + tenKhoanThu + "' (empty? " + tenKhoanThu.isEmpty() + ", length: " + tenKhoanThu.length() + ")");
        if (tenKhoanThu.isEmpty()) {
            System.out.println("❌ MISSING: Tên khoản thu");
            return true;
        }

        // Kiểm tra DatePicker có giá trị không
        System.out.println("DEBUG: HanNop: " + datePickerHanNop.getValue() + " (null? " + (datePickerHanNop.getValue() == null) + ")");
        if (datePickerHanNop.getValue() == null) {
            System.out.println("❌ MISSING: Hạn nộp");
            return true;
        }

        // Nếu chọn "Bên thứ 3" thì chỉ cần kiểm tra tên cơ quan
        if ("Bên thứ 3".equals(comboBoxBoPhanQuanLy.getValue())) {
            System.out.println("DEBUG: Validating for 'Bên thứ 3' mode");
            String tenCoQuan = textFieldTenCoQuan.getText();
            System.out.println("DEBUG: TenCoQuan: '" + tenCoQuan + "' (empty? " + tenCoQuan.isEmpty() + ", length: " + tenCoQuan.length() + ")");
            if (tenCoQuan.isEmpty()) {
                System.out.println("❌ MISSING: Tên cơ quan (required for Bên thứ 3)");
                return true;
            }
            System.out.println("✅ All fields valid for 'Bên thứ 3' mode");
            return false;
        }

        // Nếu không phải "Bên thứ 3" thì kiểm tra đơn vị tính và đơn giá
        System.out.println("DEBUG: DonViTinh: " + comboBoxDonViTinh.getValue());
        if (comboBoxDonViTinh.getValue() == null) {
            System.out.println("DEBUG: Missing đơn vị tính");
            return true;
        }

        // Kiểm tra đơn giá tùy theo đơn vị tính
        if ("Phương tiện".equals(comboBoxDonViTinh.getValue())) {
            System.out.println("DEBUG: 🚗 Vehicle mode - validating vehicle prices");
            
            // Kiểm tra ít nhất 1 loại xe phải có giá
            String giaXeDap = textFieldGiaXeDap.getText().trim();
            String giaXeMay = textFieldGiaXeMay.getText().trim();
            String giaXeOTo = textFieldGiaXeOTo.getText().trim();
            
            System.out.println("DEBUG: Vehicle prices - Xe đạp: '" + giaXeDap + "', Xe máy: '" + giaXeMay + "', Xe ô tô: '" + giaXeOTo + "'");
            
            if (giaXeDap.isEmpty() && giaXeMay.isEmpty() && giaXeOTo.isEmpty()) {
                System.out.println("❌ MISSING: Phải nhập giá cho ít nhất một loại xe");
                return true;
            }
            
            // Kiểm tra format số cho các giá xe được nhập (sử dụng method format tiền)
            if (!giaXeDap.isEmpty()) {
                int value = getNumberFromFormattedMoney(giaXeDap);
                if (value <= 0) {
                    System.out.println("❌ INVALID: Giá xe đạp không hợp lệ: " + giaXeDap);
                    return true;
                }
            }
            if (!giaXeMay.isEmpty()) {
                int value = getNumberFromFormattedMoney(giaXeMay);
                if (value <= 0) {
                    System.out.println("❌ INVALID: Giá xe máy không hợp lệ: " + giaXeMay);
                    return true;
                }
            }
            if (!giaXeOTo.isEmpty()) {
                int value = getNumberFromFormattedMoney(giaXeOTo);
                if (value <= 0) {
                    System.out.println("❌ INVALID: Giá xe ô tô không hợp lệ: " + giaXeOTo);
                    return true;
                }
            }
            
            System.out.println("✅ Vehicle prices validation passed");
        } else {
            // Kiểm tra đơn giá chung
            if (textFieldDonGia.getText().isEmpty()) {
                System.out.println("❌ MISSING: đơn giá - value: '" + textFieldDonGia.getText() + 
                                 "', disabled: " + textFieldDonGia.isDisabled() + 
                                 ", visible: " + textFieldDonGia.isVisible());
                return true;
            }
            
            // Kiểm tra format số cho đơn giá chung (sử dụng method format tiền)
            int value = getNumberFromFormattedMoney(textFieldDonGia.getText());
            if (value <= 0) {
                System.out.println("❌ INVALID: Đơn giá không hợp lệ: " + textFieldDonGia.getText());
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
     * Mở popup để nhập chi tiết giá từng loại xe
     */
    private void openVehicleDetailsPopup(String maKhoanThu, String tenKhoanThu) {
        try {
            System.out.println("DEBUG: 🚗 Creating vehicle details popup for: " + tenKhoanThu + " (ID: " + maKhoanThu + ")");
            
            // Tạo dialog popup
            javafx.scene.control.Dialog<java.util.List<PhiGuiXeDto>> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Chi tiết phí gửi xe");
            dialog.setHeaderText("Nhập giá từng loại xe cho khoản thu: " + tenKhoanThu);
            
            // Tạo layout cho popup
            javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
            content.setPadding(new javafx.geometry.Insets(20));
            
            // Label hướng dẫn
            javafx.scene.control.Label label = new javafx.scene.control.Label(
                "Vui lòng nhập giá cho các loại xe (có thể bỏ trống nếu không có):");
            label.setStyle("-fx-font-weight: bold;");
            
            // Tạo các TextField cho từng loại xe
            javafx.scene.layout.HBox xeDapBox = new javafx.scene.layout.HBox(10);
            javafx.scene.control.Label xeDapLabel = new javafx.scene.control.Label("Xe đạp:");
            xeDapLabel.setPrefWidth(80);
            javafx.scene.control.TextField xeDapField = new javafx.scene.control.TextField();
            xeDapField.setPromptText("Nhập giá xe đạp (VND)");
            xeDapBox.getChildren().addAll(xeDapLabel, xeDapField);
            
            javafx.scene.layout.HBox xeMayBox = new javafx.scene.layout.HBox(10);
            javafx.scene.control.Label xeMayLabel = new javafx.scene.control.Label("Xe máy:");
            xeMayLabel.setPrefWidth(80);
            javafx.scene.control.TextField xeMayField = new javafx.scene.control.TextField();
            xeMayField.setPromptText("Nhập giá xe máy (VND)");
            xeMayBox.getChildren().addAll(xeMayLabel, xeMayField);
            
            javafx.scene.layout.HBox xeOToBox = new javafx.scene.layout.HBox(10);
            javafx.scene.control.Label xeOToLabel = new javafx.scene.control.Label("Xe ô tô:");
            xeOToLabel.setPrefWidth(80);
            javafx.scene.control.TextField xeOToField = new javafx.scene.control.TextField();
            xeOToField.setPromptText("Nhập giá xe ô tô (VND)");
            xeOToBox.getChildren().addAll(xeOToLabel, xeOToField);
            
            content.getChildren().addAll(label, xeDapBox, xeMayBox, xeOToBox);
            dialog.getDialogPane().setContent(content);
            
            // Thêm buttons
            javafx.scene.control.ButtonType saveButtonType = new javafx.scene.control.ButtonType(
                "Lưu", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
            javafx.scene.control.ButtonType cancelButtonType = javafx.scene.control.ButtonType.CANCEL;
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);
            
            // Xử lý kết quả
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    java.util.List<PhiGuiXeDto> phiGuiXeList = new java.util.ArrayList<>();
                    
                    // Thêm xe đạp nếu có giá
                    if (!xeDapField.getText().isEmpty()) {
                        try {
                            int price = Integer.parseInt(xeDapField.getText());
                            phiGuiXeList.add(new PhiGuiXeDto("Xe đạp", price, maKhoanThu));
                        } catch (NumberFormatException e) {
                            System.err.println("Giá xe đạp không hợp lệ: " + xeDapField.getText());
                        }
                    }
                    
                    // Thêm xe máy nếu có giá
                    if (!xeMayField.getText().isEmpty()) {
                        try {
                            int price = Integer.parseInt(xeMayField.getText());
                            phiGuiXeList.add(new PhiGuiXeDto("Xe máy", price, maKhoanThu));
                        } catch (NumberFormatException e) {
                            System.err.println("Giá xe máy không hợp lệ: " + xeMayField.getText());
                        }
                    }
                    
                    // Thêm xe ô tô nếu có giá
                    if (!xeOToField.getText().isEmpty()) {
                        try {
                            int price = Integer.parseInt(xeOToField.getText());
                            phiGuiXeList.add(new PhiGuiXeDto("Ô tô", price, maKhoanThu));
                        } catch (NumberFormatException e) {
                            System.err.println("Giá xe ô tô không hợp lệ: " + xeOToField.getText());
                        }
                    }
                    
                    return phiGuiXeList;
                }
                return null;
            });
            
            // Hiển thị dialog và xử lý kết quả
            java.util.Optional<java.util.List<PhiGuiXeDto>> result = dialog.showAndWait();
            result.ifPresent(phiGuiXeList -> {
                if (!phiGuiXeList.isEmpty()) {
                    System.out.println("DEBUG: 💾 Saving " + phiGuiXeList.size() + " vehicle fee entries");
                    // Ở đây bạn có thể gọi service để lưu danh sách phí xe
                    // Ví dụ: phiGuiXeService.savePhiGuiXeList(phiGuiXeList);
                    
                    for (PhiGuiXeDto dto : phiGuiXeList) {
                        System.out.println("DEBUG: ✅ " + dto.getLoaiXe() + ": " + dto.getSoTien() + " VND");
                    }
                } else {
                    System.out.println("DEBUG: ⚠️ No vehicle fees entered");
                }
                
                // Đóng form chính sau khi xử lý xong
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(500);
                        handleClose(null);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        handleClose(null);
                    }
                });
            });
            
        } catch (Exception e) {
            System.err.println("Lỗi khi mở popup chi tiết xe: " + e.getMessage());
            e.printStackTrace();
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
            ThongTinTaiKhoanDto currentUser = Session.getCurrentUser();
            if (currentUser == null) {
                return false;
            }
            String vaiTro = currentUser.getVaiTro();
            return !"Tổ trưởng".equals(vaiTro);
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra quyền: " + e.getMessage());
            return false;
        }
    }

    // Method to setup edit mode
    public void setupEditMode(io.github.ktpm.bluemoonmanagement.controller.Home_list.KhoanThuTableData khoanThuData) {
        isEditMode = true;
        originalMaKhoanThu = khoanThuData.getMaKhoanThu();
        
        // Chuyển đổi UI để hiển thị chế độ chỉnh sửa
        if (labelTieuDe != null) {
            labelTieuDe.setText("Chi tiết khoản thu");
        }
        
        // Thay đổi text button từ "Thêm khoản thu" thành "Chỉnh sửa"
        if (buttonThemKhoanThu != null) {
            buttonThemKhoanThu.setText("Chỉnh sửa");
        }
        
        // Ẩn các button khác 
        if (buttonChinhSua != null) {
            buttonChinhSua.setVisible(false);
        }
        if (buttonLuu != null) {
            buttonLuu.setVisible(false);
        }
        
        // Điền dữ liệu vào form
        fillFormWithData(khoanThuData);
        
        // Enable tất cả fields để chỉnh sửa, NGOẠI TRỪ đơn vị tính
        setFieldsEditable(true);
        
        // LUÔN LUÔN disable đơn vị tính trong chế độ edit
        if (comboBoxDonViTinh != null) {
            comboBoxDonViTinh.setDisable(true);
        }
    }
    
    private void fillFormWithData(io.github.ktpm.bluemoonmanagement.controller.Home_list.KhoanThuTableData data) {
        if (textFieldTenKhoanThu != null) {
            textFieldTenKhoanThu.setText(data.getTenKhoanThu());
        }
        
        if (comboBoxLoaiKhoanThu != null) {
            comboBoxLoaiKhoanThu.setValue(data.getLoaiKhoanThu());
        }
        
        if (textFieldDonGia != null) {
            // Parse số tiền từ chuỗi có format và format lại
            String soTienText = data.getSoTien();
            if (soTienText != null && !soTienText.trim().isEmpty()) {
                // Loại bỏ định dạng cũ để lấy số
                String numbersOnly = soTienText.replaceAll("[^0-9]", "");
                if (!numbersOnly.isEmpty()) {
                    try {
                        int amount = Integer.parseInt(numbersOnly);
                                    textFieldDonGia.setText(String.valueOf(amount));
                    } catch (NumberFormatException e) {
                        textFieldDonGia.setText(soTienText); // Fallback to original
                    }
                } else {
                    textFieldDonGia.setText(soTienText);
                }
            }
        }
        
        if (comboBoxDonViTinh != null) {
            comboBoxDonViTinh.setValue(data.getDonViTinh());
        }
        
        if (comboBoxPhamVi != null) {
            comboBoxPhamVi.setValue(data.getPhamVi());
        }
        
        // Parse date if available
        if (datePickerHanNop != null && data.getThoiHan() != null && !data.getThoiHan().isEmpty()) {
            try {
                datePickerHanNop.setValue(java.time.LocalDate.parse(data.getThoiHan()));
            } catch (Exception e) {
                System.err.println("Lỗi parse ngày: " + e.getMessage());
            }
        }
        
        if (comboBoxBoPhanQuanLy != null) {
            // Nếu ghiChu không phải là "Ban quản lý" hoặc "Bên thứ 3", 
            // nghĩa là đây có thể là tên cơ quan cụ thể
            String ghiChu = data.getGhiChu();
            if ("Ban quản lý".equals(ghiChu) || "Bên thứ 3".equals(ghiChu)) {
                comboBoxBoPhanQuanLy.setValue(ghiChu);
            } else {
                // Đây là tên cơ quan, set bộ phận là "Bên thứ 3" và điền tên cơ quan
                comboBoxBoPhanQuanLy.setValue("Bên thứ 3");
                if (textFieldTenCoQuan != null) {
                    textFieldTenCoQuan.setText(ghiChu);
                }
            }
        }
        
        // Trigger sự kiện để hiển thị trường tên cơ quan nếu cần
        onBoPhanQuanLyChanged(null);
        
        // Nếu là loại phương tiện, load và hiển thị giá các loại xe
        if ("Phương tiện".equals(data.getDonViTinh()) && khoanThuService != null) {
            System.out.println("DEBUG: Loading vehicle prices for fee: " + data.getMaKhoanThu());
            try {
                // Lấy chi tiết khoản thu từ service để có thông tin phí xe
                List<io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto> khoanThuList = khoanThuService.getAllKhoanThu();
                io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto khoanThuDto = khoanThuList.stream()
                    .filter(kt -> kt.getMaKhoanThu().equals(data.getMaKhoanThu()))
                    .findFirst()
                    .orElse(null);
                    
                if (khoanThuDto != null && khoanThuDto.getPhiGuiXeList() != null && !khoanThuDto.getPhiGuiXeList().isEmpty()) {
                    System.out.println("DEBUG: Found " + khoanThuDto.getPhiGuiXeList().size() + " vehicle fees");
                    
                    // Clear existing values
                    if (textFieldGiaXeDap != null) textFieldGiaXeDap.clear();
                    if (textFieldGiaXeMay != null) textFieldGiaXeMay.clear();
                    if (textFieldGiaXeOTo != null) textFieldGiaXeOTo.clear();
                    
                    // Điền giá cho từng loại xe
                    for (io.github.ktpm.bluemoonmanagement.model.dto.phiGuiXe.PhiGuiXeDto phiXe : khoanThuDto.getPhiGuiXeList()) {
                        String loaiXe = phiXe.getLoaiXe();
                        String gia = String.valueOf(phiXe.getSoTien());
                        
                        System.out.println("DEBUG: Setting price for " + loaiXe + ": " + gia);
                        
                        if ("Xe đạp".equals(loaiXe) && textFieldGiaXeDap != null) {
                            textFieldGiaXeDap.setText(String.valueOf(phiXe.getSoTien()));
                        } else if ("Xe máy".equals(loaiXe) && textFieldGiaXeMay != null) {
                            textFieldGiaXeMay.setText(String.valueOf(phiXe.getSoTien()));
                        } else if ("Ô tô".equals(loaiXe) && textFieldGiaXeOTo != null) {
                            textFieldGiaXeOTo.setText(String.valueOf(phiXe.getSoTien()));
                        }
                    }
                    
                    // Trigger sự kiện để hiển thị các field xe
                    onDonViTinhChanged(null);
                } else {
                    System.out.println("DEBUG: No vehicle fees found for this fee");
                }
            } catch (Exception e) {
                System.err.println("Error loading vehicle prices: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void setFieldsEditable(boolean editable) {
        if (textFieldTenKhoanThu != null) {
            textFieldTenKhoanThu.setDisable(!editable);
        }
        if (comboBoxLoaiKhoanThu != null) {
            comboBoxLoaiKhoanThu.setDisable(!editable);
        }
        if (textFieldDonGia != null) {
            textFieldDonGia.setDisable(!editable);
        }
        if (hBoxDonGia != null) {
            hBoxDonGia.setDisable(!editable);
        }
        if (comboBoxDonViTinh != null) {
            comboBoxDonViTinh.setDisable(!editable);
        }
        if (comboBoxPhamVi != null) {
            comboBoxPhamVi.setDisable(!editable);
        }
        if (comboBoxBoPhanQuanLy != null) {
            comboBoxBoPhanQuanLy.setDisable(!editable);
        }
        if (datePickerHanNop != null) {
            datePickerHanNop.setDisable(!editable);
        }
        if (textFieldGiaXeDap != null) {
            textFieldGiaXeDap.setDisable(!editable);
        }
        if (textFieldGiaXeMay != null) {
            textFieldGiaXeMay.setDisable(!editable);
        }
        if (textFieldGiaXeOTo != null) {
            textFieldGiaXeOTo.setDisable(!editable);
        }
        if (textFieldTenCoQuan != null) {
            textFieldTenCoQuan.setDisable(!editable);
        }
    }
    
    @FXML
    private void handleChinhSua(ActionEvent event) {
        // Kiểm tra quyền
        if (!hasPermission()) {
            textError.setText("Bạn không có quyền chỉnh sửa khoản thu.");
            textError.setStyle("-fx-fill: red;");
            return;
        }
        
        // Enable tất cả fields để chỉnh sửa
        setFieldsEditable(true);
        
        // Ẩn button chỉnh sửa, hiện button lưu
        if (buttonChinhSua != null) {
            buttonChinhSua.setVisible(false);
        }
        if (buttonLuu != null) {
            buttonLuu.setVisible(true);
        }
        
        textError.setText("Đang trong chế độ chỉnh sửa. Vui lòng thay đổi thông tin và nhấn Lưu.");
        textError.setStyle("-fx-fill: blue;");
    }
    
    @FXML
    private void handleLuu(ActionEvent event) {
        // Kiểm tra quyền
        if (!hasPermission()) {
            textError.setText("Bạn không có quyền lưu thay đổi.");
            textError.setStyle("-fx-fill: red;");
            return;
        }
        
        if (isAnyFieldEmpty()) {
            textError.setText("Vui lòng điền đầy đủ thông tin!");
            textError.setStyle("-fx-fill: red;");
            return;
        }
        
        try {
            // Tạo DTO với thông tin mới
            KhoanThuDto updatedDto = createKhoanThuDto();
            updatedDto.setMaKhoanThu(originalMaKhoanThu); // Giữ nguyên mã khoản thu
            
            // Gọi service để cập nhật
            ResponseDto response = khoanThuService.updateKhoanThu(updatedDto);
            
            if (response.isSuccess()) {
                textError.setText("Cập nhật khoản thu thành công!");
                textError.setStyle("-fx-fill: green;");
                
                // Refresh table
                refreshKhoanThuTable();
                
                // Disable fields và chuyển về chế độ xem
                setFieldsEditable(false);
                if (buttonLuu != null) {
                    buttonLuu.setVisible(false);
                }
                if (buttonChinhSua != null) {
                    buttonChinhSua.setVisible(true);
                }
                
                // Close window after successful update với delay ngắn hơn
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(1000); // Show success message for 1 second
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
            
        } catch (Exception e) {
            textError.setText("Lỗi khi cập nhật: " + e.getMessage());
            textError.setStyle("-fx-fill: red;");
        }
    }
    
    private KhoanThuDto createKhoanThuDto() {
        String tenKhoanThu = textFieldTenKhoanThu.getText();
        String loaiKhoanThu = comboBoxLoaiKhoanThu.getValue().toString();
        String boPhanQuanLy = comboBoxBoPhanQuanLy.getValue().toString();
        String thoiHanNop = datePickerHanNop.getValue().toString();
        String phamVi = comboBoxPhamVi.getValue().toString();
        
        // Xử lý đơn vị tính và số tiền tùy theo bộ phận quản lý
        String donViTinh;
        int soTien;
        
        if ("Bên thứ 3".equals(boPhanQuanLy)) {
            // Nếu là bên thứ 3, có thể set giá trị mặc định hoặc để trống
            donViTinh = "Theo hóa đơn";
            soTien = 0; // hoặc giá trị mặc định khác
        } else {
            donViTinh = comboBoxDonViTinh.getValue().toString();
            if ("Phương tiện".equals(donViTinh)) {
                soTien = 0; // For vehicle fees, individual prices are in PhiGuiXeList
            } else {
                soTien = getNumberFromFormattedMoney(textFieldDonGia.getText());
            }
        }
        
        KhoanThuDto khoanThuDto = new KhoanThuDto();
        khoanThuDto.setTenKhoanThu(tenKhoanThu);
        khoanThuDto.setBatBuoc("Bắt buộc".equals(loaiKhoanThu));
        khoanThuDto.setDonViTinh(donViTinh);
        khoanThuDto.setSoTien(soTien);
        khoanThuDto.setPhamVi(phamVi);
        // Giữ nguyên ngày tạo gốc nếu đang ở chế độ edit
        if (isEditMode && currentKhoanThu != null && currentKhoanThu.getNgayTao() != null) {
            khoanThuDto.setNgayTao(currentKhoanThu.getNgayTao());
            System.out.println("DEBUG: Keeping original creation date: " + currentKhoanThu.getNgayTao());
        } else {
            khoanThuDto.setNgayTao(java.time.LocalDate.now());
            System.out.println("DEBUG: Setting new creation date: " + java.time.LocalDate.now());
        }
        khoanThuDto.setThoiHan(java.time.LocalDate.parse(thoiHanNop));
        // Sử dụng tên cơ quan nếu có, nếu không thì dùng bộ phận quản lý
        String ghiChu = boPhanQuanLy;
        if ("Bên thứ 3".equals(boPhanQuanLy) && textFieldTenCoQuan != null && !textFieldTenCoQuan.getText().isEmpty()) {
            ghiChu = textFieldTenCoQuan.getText();
        }
        khoanThuDto.setGhiChu(ghiChu);
        
        // *** XỬ LÝ GIÁ XE CHO PHƯƠNG TIỆN ***
        if ("Phương tiện".equals(donViTinh)) {
            System.out.println("DEBUG: 🚗 Creating vehicle fees for update...");
            List<PhiGuiXeDto> phiGuiXeList = new ArrayList<>();
            
            // Thêm giá xe đạp nếu có
            if (textFieldGiaXeDap != null && !textFieldGiaXeDap.getText().trim().isEmpty()) {
                int giaXeDap = getNumberFromFormattedMoney(textFieldGiaXeDap.getText());
                if (giaXeDap > 0) {
                    PhiGuiXeDto phiXeDap = new PhiGuiXeDto();
                    phiXeDap.setLoaiXe("Xe đạp");
                    phiXeDap.setSoTien(giaXeDap);
                    phiGuiXeList.add(phiXeDap);
                    System.out.println("DEBUG: Added bike price: " + String.format("%,d VNĐ", giaXeDap));
                }
            }
            
            // Thêm giá xe máy nếu có
            if (textFieldGiaXeMay != null && !textFieldGiaXeMay.getText().trim().isEmpty()) {
                int giaXeMay = getNumberFromFormattedMoney(textFieldGiaXeMay.getText());
                if (giaXeMay > 0) {
                    PhiGuiXeDto phiXeMay = new PhiGuiXeDto();
                    phiXeMay.setLoaiXe("Xe máy");
                    phiXeMay.setSoTien(giaXeMay);
                    phiGuiXeList.add(phiXeMay);
                    System.out.println("DEBUG: Added motorcycle price: " + String.format("%,d VNĐ", giaXeMay));
                }
            }
            
            // Thêm giá xe ô tô nếu có
            if (textFieldGiaXeOTo != null && !textFieldGiaXeOTo.getText().trim().isEmpty()) {
                int giaXeOTo = getNumberFromFormattedMoney(textFieldGiaXeOTo.getText());
                if (giaXeOTo > 0) {
                    PhiGuiXeDto phiXeOTo = new PhiGuiXeDto();
                    phiXeOTo.setLoaiXe("Ô tô");
                    phiXeOTo.setSoTien(giaXeOTo);
                    phiGuiXeList.add(phiXeOTo);
                    System.out.println("DEBUG: Added car price: " + String.format("%,d VNĐ", giaXeOTo));
                }
            }
            
            khoanThuDto.setPhiGuiXeList(phiGuiXeList);
            System.out.println("DEBUG: 💰 Vehicle mode - created " + phiGuiXeList.size() + " vehicle fees");
        } else {
            System.out.println("DEBUG: 💰 Non-vehicle mode - no vehicle details needed");
            khoanThuDto.setPhiGuiXeList(new ArrayList<>());
        }
        
        return khoanThuDto;
    }
    

    /**
     * Refresh bảng khoản thu và chuyển về tab khoản thu
     */
    private void refreshKhoanThuTableAndGoToTab() {
        try {
            System.out.println("🔄 Refreshing fee table and switching to KhoanThu tab...");
            
            // Find all windows and look for Home_list controller
            for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                if (window instanceof javafx.stage.Stage) {
                    javafx.stage.Stage stage = (javafx.stage.Stage) window;
                    javafx.scene.Scene scene = stage.getScene();
                    if (scene != null && scene.getRoot() != null) {
                        // Try to find the Home_list controller through scene graph
                        findAndRefreshKhoanThuControllerForceTab(scene.getRoot());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to refresh fee data and switch tab: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Find and refresh Home_list controller for fees with forced tab switch
     */
    private void findAndRefreshKhoanThuControllerForceTab(javafx.scene.Node node) {
        try {
            // Check if the node has a controller property
            Object controller = node.getProperties().get("controller");
            if (controller instanceof io.github.ktpm.bluemoonmanagement.controller.Home_list) {
                io.github.ktpm.bluemoonmanagement.controller.Home_list homeListController = (io.github.ktpm.bluemoonmanagement.controller.Home_list) controller;
                
                // Force switch to fees tab first
                java.lang.reflect.Method gotoKhoanThuMethod = homeListController.getClass().getDeclaredMethod("gotoKhoanThu", javafx.event.ActionEvent.class);
                gotoKhoanThuMethod.setAccessible(true);
                gotoKhoanThuMethod.invoke(homeListController, (javafx.event.ActionEvent) null);
                
                // Then refresh fee data 
                homeListController.refreshKhoanThuData();
                
                System.out.println("✅ Fee data refreshed successfully and switched to KhoanThu tab");
                return;
            }
            
            // Recursively search in children if it's a Parent node
            if (node instanceof javafx.scene.Parent) {
                javafx.scene.Parent parent = (javafx.scene.Parent) node;
                for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                    findAndRefreshKhoanThuControllerForceTab(child);
                }
            }
        } catch (Exception e) {
            // Silently continue searching - this is expected for most nodes
        }
    }
    
    /**
     * Refresh cache và cập nhật dữ liệu phí gửi xe
     */
    private void refreshCacheAndVehicleFees() {
        try {
            System.out.println("🔄 Refreshing cache and vehicle fees data...");
            
            javafx.application.Platform.runLater(() -> {
                try {
                    // Find all windows and refresh relevant components
                    for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                        if (window instanceof javafx.stage.Stage) {
                            javafx.stage.Stage stage = (javafx.stage.Stage) window;
                            javafx.scene.Scene scene = stage.getScene();
                            if (scene != null && scene.getRoot() != null) {
                                // Try to find controllers and refresh cache
                                refreshCacheInScene(scene.getRoot());
                            }
                        }
                    }
                    
                    System.out.println("✅ Cache and vehicle fees refreshed successfully");
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to refresh cache and vehicle fees: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("ERROR: Exception in refreshCacheAndVehicleFees: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tìm và refresh cache trong scene graph
     */
    private void refreshCacheInScene(javafx.scene.Node node) {
        try {
            // Check if the node has a controller property
            Object controller = node.getProperties().get("controller");
            
            // Refresh cache for different controller types
            if (controller instanceof io.github.ktpm.bluemoonmanagement.controller.Home_list) {
                io.github.ktpm.bluemoonmanagement.controller.Home_list homeListController = (io.github.ktpm.bluemoonmanagement.controller.Home_list) controller;
                // Home_list controller có thể có cache service
                try {
                    java.lang.reflect.Field cacheField = homeListController.getClass().getDeclaredField("cacheDataService");
                    cacheField.setAccessible(true);
                    Object cacheService = cacheField.get(homeListController);
                    if (cacheService != null) {
                        java.lang.reflect.Method refreshMethod = cacheService.getClass().getMethod("refreshCacheData");
                        refreshMethod.invoke(cacheService);
                        System.out.println("✅ Refreshed cache in Home_list controller");
                    }
                } catch (Exception e) {
                    // Silently continue - cache refresh is optional
                }
            }
            
            else if (controller instanceof io.github.ktpm.bluemoonmanagement.controller.ChiTietCanHoController) {
                io.github.ktpm.bluemoonmanagement.controller.ChiTietCanHoController detailController = (io.github.ktpm.bluemoonmanagement.controller.ChiTietCanHoController) controller;
                // Refresh data in apartment detail windows
                try {
                    java.lang.reflect.Method refreshMethod = detailController.getClass().getDeclaredMethod("refreshData");
                    refreshMethod.setAccessible(true);
                    refreshMethod.invoke(detailController);
                    System.out.println("✅ Refreshed data in ChiTietCanHoController");
                } catch (Exception e) {
                    // Silently continue - refresh is optional
                }
            }
            
            // Recursively search in children if it's a Parent node
            if (node instanceof javafx.scene.Parent) {
                javafx.scene.Parent parent = (javafx.scene.Parent) node;
                for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                    refreshCacheInScene(child);
                }
            }
        } catch (Exception e) {
            // Silently continue searching - this is expected for most nodes
        }
    }
    
    /**
     * Đóng tất cả các tab detail đang mở (form chỉnh sửa, chi tiết căn hộ, etc.)
     */
    private void closeAllOpenDetailTabs() {
        try {
            System.out.println("🚪 Closing all open detail tabs...");
            
            // Get all open windows
            java.util.List<javafx.stage.Window> openWindows = new java.util.ArrayList<>(javafx.stage.Window.getWindows());
            
            for (javafx.stage.Window window : openWindows) {
                if (window instanceof javafx.stage.Stage) {
                    javafx.stage.Stage stage = (javafx.stage.Stage) window;
                    
                    // Skip the main window - only close detail/form windows
                    if (isDetailOrFormWindow(stage)) {
                        System.out.println("🚪 Closing detail window: " + stage.getTitle());
                        javafx.application.Platform.runLater(() -> {
                            try {
                                stage.close();
                            } catch (Exception e) {
                                System.err.println("Error closing window: " + e.getMessage());
                            }
                        });
                    }
                }
            }
            
            System.out.println("✅ All detail tabs closed");
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to close detail tabs: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra xem window có phải là detail/form window không
     */
    private boolean isDetailOrFormWindow(javafx.stage.Stage stage) {
        try {
            String title = stage.getTitle();
            if (title == null) return false;
            
            // Đóng các window có title chứa các từ khóa này
            return title.contains("Chi tiết") || 
                   title.contains("Chỉnh sửa") || 
                   title.contains("Thêm") ||
                   title.contains("Xác nhận") ||
                   title.contains("Detail") ||
                   title.contains("Edit") ||
                   title.contains("Add") ||
                   title.toLowerCase().contains("form");
                   
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Thiết lập format tiền cho các text field
     */
    private void setupMoneyFormatting() {
        // Format cho đơn giá chung
        if (textFieldDonGia != null) {
            setupMoneyFormattingForField(textFieldDonGia);
        }
        
        // Format cho giá xe đạp
        if (textFieldGiaXeDap != null) {
            setupMoneyFormattingForField(textFieldGiaXeDap);
        }
        
        // Format cho giá xe máy
        if (textFieldGiaXeMay != null) {
            setupMoneyFormattingForField(textFieldGiaXeMay);
        }
        
        // Format cho giá xe ô tô
        if (textFieldGiaXeOTo != null) {
            setupMoneyFormattingForField(textFieldGiaXeOTo);
        }
        
        System.out.println("✅ Money formatting setup completed for all price fields");
    }
    
    /**
     * Thiết lập format tiền cho một text field cụ thể - chỉ cho phép số
     */
    private void setupMoneyFormattingForField(javafx.scene.control.TextField textField) {
        // Chỉ cho phép nhập số
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Chỉ giữ lại các ký tự số
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    
    /**
     * Format input khi đang nhập (real-time)
     */
    private String formatMoneyInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        
        // Loại bỏ tất cả ký tự không phải số
        String numbersOnly = input.replaceAll("[^0-9]", "");
        
        if (numbersOnly.isEmpty()) {
            return "";
        }
        
        try {
            long number = Long.parseLong(numbersOnly);
            if (number == 0) {
                return "";
            }
            
            // Format với dấu phẩy
            String formatted = String.format("%,d", number);
            return formatted + " VNĐ";
            
        } catch (NumberFormatException e) {
            return input; // Trả về giá trị gốc nếu không format được
        }
    }
    
    /**
     * Format hoàn chỉnh khi hoàn thành nhập
     */
    private String formatMoneyComplete(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        
        // Loại bỏ tất cả ký tự không phải số
        String numbersOnly = input.replaceAll("[^0-9]", "");
        
        if (numbersOnly.isEmpty()) {
            return "";
        }
        
        try {
            long number = Long.parseLong(numbersOnly);
            if (number == 0) {
                return "";
            }
            
            // Format với dấu phẩy và VNĐ
            return String.format("%,d VNĐ", number);
            
        } catch (NumberFormatException e) {
            return input; // Trả về giá trị gốc nếu không format được
        }
    }
    
    /**
     * Kiểm tra xem text đã được format chưa
     */
    private boolean isFormattedMoney(String text) {
        if (text == null) return false;
        
        // Kiểm tra có kết thúc bằng " VNĐ" và có dấu phẩy
        return text.endsWith(" VNĐ") && text.contains(",");
    }
    
    /**
     * Lấy giá trị số từ text (chỉ là số thuần)
     */
    private int getNumberFromFormattedMoney(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
