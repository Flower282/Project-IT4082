package io.github.ktpm.bluemoonmanagement.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.ChuHoDto;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ThemCanHoButton implements Initializable {

    @FXML
    private FontAwesomeIcon buttonClose;

    @FXML
    private Button buttonTaoCanHo;

    @FXML
    private Button button_close_up;

    @FXML
    private Button buttonTaoChuSoHuuMoi;

    @FXML
    private CheckBox choiceBoxThemChuSoHuu;

    @FXML
    private CheckBox choiceBoxTaoCuDanMoi;

    @FXML
    private ComboBox<String> comboBoxGioiTinh;

    @FXML
    private ComboBox<String> comboBoxTinhTrangKiThuat;

    @FXML
    private ComboBox<String> comboBoxTinhTrangSuDung;

    @FXML
    private ComboBox<String> comboBoxTrangThai;

    @FXML
    private DatePicker datePickerNgaySinh;

    @FXML
    private Label labelHuongDan;

    @FXML
    private Text textError;

    @FXML
    private TextField textFieldDienTich;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldHoVaTen;

    @FXML
    private TextField textFieldMaDinhDanh;

    @FXML
    private TextField textFieldMaDinhDanhMoi;

    @FXML
    private TextField textFieldSoDienThoai;

    @FXML
    private TextField textFieldSoNha;

    @FXML
    private TextField textFieldTang;

    @FXML
    private TextField textFieldToa;

    @FXML
    private VBox vBoxChuSoHuu;

    @FXML
    private VBox vBoxThongTinCuDanMoi;

    // Service instance - sẽ được inject từ bên ngoài
    private CanHoService canHoService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupUI();
    }

    /**
     * Setter để inject service từ bên ngoài
     */
    public void setCanHoService(CanHoService canHoService) {
        this.canHoService = canHoService;
    }

    private void setupUI() {
        // Khởi tạo dữ liệu ComboBox
        initializeComboBoxes();
        
        // Thiết lập event handlers
        setupEventHandlers();
        
        // Thiết lập ràng buộc UI
        setupUIBindings();
        
        // Ẩn error text ban đầu
        textError.setVisible(false);
    }

    private void initializeComboBoxes() {
        // ComboBox Giới tính
        comboBoxGioiTinh.getItems().addAll("Nam", "Nữ", "Khác");
        
        // ComboBox Tình trạng kỹ thuật
        comboBoxTinhTrangKiThuat.getItems().addAll("Tốt", "Khá", "Trung bình", "Cần sửa chữa", "Hỏng");
        
        // ComboBox Tình trạng sử dụng
        comboBoxTinhTrangSuDung.getItems().addAll("Đang sử dụng", "Trống", "Cho thuê", "Bảo trì");
        
        // ComboBox Trạng thái cư trú
        comboBoxTrangThai.getItems().addAll("Cư trú", "Tạm vắng", "Chuyển đi");
        
        // Thiết lập giá trị mặc định
        comboBoxTinhTrangKiThuat.setValue("Tốt");
        comboBoxTinhTrangSuDung.setValue("Trống");
        comboBoxTrangThai.setValue("Cư trú");
    }

    private void setupEventHandlers() {
        // Xử lý sự kiện cho checkbox thêm chủ sở hữu
        choiceBoxThemChuSoHuu.setOnAction(this::handleThemChuSoHuuChange);
        
        // Xử lý sự kiện cho checkbox tạo cư dân mới
        choiceBoxTaoCuDanMoi.setOnAction(this::handleTaoCuDanMoiChange);
        
        // Xử lý nút tạo căn hộ
        buttonTaoCanHo.setOnAction(this::handleTaoCanHo);
        
        // Xử lý nút đóng
        button_close_up.setOnAction(this::handleClose);
    }

    private void setupUIBindings() {
        // Hiển thị/ẩn vùng nhập thông tin chủ sở hữu
        vBoxChuSoHuu.setVisible(false);
        vBoxChuSoHuu.setManaged(false);
        
        // Hiển thị/ẩn vùng nhập thông tin cư dân mới
        vBoxThongTinCuDanMoi.setVisible(false);
        vBoxThongTinCuDanMoi.setManaged(false);
    }

    @FXML
    private void handleThemChuSoHuuChange(ActionEvent event) {
        boolean isSelected = choiceBoxThemChuSoHuu.isSelected();
        vBoxChuSoHuu.setVisible(isSelected);
        vBoxChuSoHuu.setManaged(isSelected);
        
        // Nếu chọn thêm chủ sở hữu, thì bỏ chọn tạo cư dân mới
        if (isSelected) {
            choiceBoxTaoCuDanMoi.setSelected(false);
            vBoxThongTinCuDanMoi.setVisible(false);
            vBoxThongTinCuDanMoi.setManaged(false);
            textFieldMaDinhDanh.requestFocus();
        } else {
            textFieldMaDinhDanh.clear();
        }
        
        System.out.println("Thêm chủ sở hữu: " + isSelected);
    }

    @FXML
    private void handleTaoCuDanMoiChange(ActionEvent event) {
        boolean isSelected = choiceBoxTaoCuDanMoi.isSelected();
        vBoxThongTinCuDanMoi.setVisible(isSelected);
        vBoxThongTinCuDanMoi.setManaged(isSelected);
        
        // Nếu chọn tạo cư dân mới, thì bỏ chọn thêm chủ sở hữu
        if (isSelected) {
            choiceBoxThemChuSoHuu.setSelected(false);
            vBoxChuSoHuu.setVisible(false);
            vBoxChuSoHuu.setManaged(false);
            textFieldMaDinhDanhMoi.requestFocus();
        } else {
            clearCuDanMoiFields();
        }
        
        System.out.println("Tạo cư dân mới: " + isSelected);
    }

    @FXML
    private void handleTaoCanHo(ActionEvent event) {
        try {
            // Xóa thông báo lỗi trước đó
            clearErrorMessage();
            
            // Kiểm tra service đã được inject chưa
            if (canHoService == null) {
                showErrorMessage("Lỗi hệ thống: Service chưa được khởi tạo");
                return;
            }
            
            // Kiểm tra quyền truy cập
            if (!hasPermission()) {
                showErrorMessage("Bạn không có quyền thêm căn hộ. Chỉ Tổ phó mới được phép.");
                return;
            }
            
            // Validate dữ liệu đầu vào
            if (!validateInput()) {
                return;
            }
            
            // Tạo DTO căn hộ
            CanHoDto canHoDto = createCanHoDto();
            
            // Gọi service để thêm căn hộ
            ResponseDto response = canHoService.addCanHo(canHoDto);
            
            // Sử dụng reflection để truy cập các field do vấn đề Lombok
            boolean isSuccess = getResponseSuccess(response);
            String message = getResponseMessage(response);
            
            if (isSuccess) {
                showSuccessMessage("Thêm căn hộ thành công: " + message);
                clearForm();
                closeWindow();
            } else {
                showErrorMessage("Lỗi: " + message);
            }
            
        } catch (Exception e) {
            showErrorMessage("Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void handleTaoChuSoHuuMoi(ActionEvent event) {
        try {
            System.out.println("Mở form thêm cư dân...");
            
            // Sử dụng FXMLLoader để mở form thêm cư dân
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/them_cu_dan.fxml"));
            
            Parent root = loader.load();
            
            // Tạo modal dialog
            Stage dialogStage = new Stage();
            dialogStage.initStyle(javafx.stage.StageStyle.DECORATED);
            dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialogStage.initOwner(buttonTaoChuSoHuuMoi.getScene().getWindow());
            
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setTitle("Thêm cư dân mới");
            dialogStage.setResizable(true);
            
            // Hiển thị dialog
            dialogStage.showAndWait();
            
        } catch (Exception e) {
            showErrorMessage("Lỗi khi mở form thêm cư dân: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean hasPermission() {
        try {
            if (Session.getCurrentUser() == null) return false;
            
            // Sử dụng reflection để lấy vaiTro do vấn đề Lombok
            java.lang.reflect.Field vaiTroField = Session.getCurrentUser().getClass().getDeclaredField("vaiTro");
            vaiTroField.setAccessible(true);
            String vaiTro = (String) vaiTroField.get(Session.getCurrentUser());
            
            return "Tổ trưởng".equals(vaiTro)|| "admin".equals(vaiTro);
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra quyền: " + e.getMessage());
            return false;
        }
    }

    private boolean validateInput() {
        // Validate thông tin căn hộ
        if (isBlank(textFieldToa.getText())) {
            showErrorMessage("Vui lòng nhập tòa nhà");
            textFieldToa.requestFocus();
            return false;
        }
        
        if (isBlank(textFieldTang.getText())) {
            showErrorMessage("Vui lòng nhập tầng");
            textFieldTang.requestFocus();
            return false;
        }
        
        if (isBlank(textFieldSoNha.getText())) {
            showErrorMessage("Vui lòng nhập số nhà");
            textFieldSoNha.requestFocus();
            return false;
        }
        
        if (isBlank(textFieldDienTich.getText())) {
            showErrorMessage("Vui lòng nhập diện tích");
            textFieldDienTich.requestFocus();
            return false;
        }
        
        // Validate diện tích là số
        try {
            double dienTich = Double.parseDouble(textFieldDienTich.getText().trim());
            if (dienTich <= 0) {
                showErrorMessage("Diện tích phải lớn hơn 0");
                textFieldDienTich.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Diện tích phải là số hợp lệ");
            textFieldDienTich.requestFocus();
            return false;
        }
        
        // Validate ComboBox selections
        if (comboBoxTinhTrangKiThuat.getValue() == null) {
            showErrorMessage("Vui lòng chọn tình trạng kỹ thuật");
            return false;
        }
        
        if (comboBoxTinhTrangSuDung.getValue() == null) {
            showErrorMessage("Vui lòng chọn tình trạng sử dụng");
            return false;
        }
        
        // Validate mã định danh chủ sở hữu nếu được chọn
        if (choiceBoxThemChuSoHuu.isSelected()) {
            if (isBlank(textFieldMaDinhDanh.getText())) {
                showErrorMessage("Vui lòng nhập mã định danh chủ sở hữu");
                textFieldMaDinhDanh.requestFocus();
                return false;
            }
        }
        
        // Validate thông tin cư dân mới nếu được chọn
        if (choiceBoxTaoCuDanMoi.isSelected()) {
            if (isBlank(textFieldMaDinhDanhMoi.getText())) {
                showErrorMessage("Vui lòng nhập mã định danh cho cư dân mới");
                textFieldMaDinhDanhMoi.requestFocus();
                return false;
            }
            
            if (isBlank(textFieldHoVaTen.getText())) {
                showErrorMessage("Vui lòng nhập họ và tên");
                textFieldHoVaTen.requestFocus();
                return false;
            }
            
            if (isBlank(textFieldSoDienThoai.getText())) {
                showErrorMessage("Vui lòng nhập số điện thoại");
                textFieldSoDienThoai.requestFocus();
                return false;
            }
            
            if (isBlank(textFieldEmail.getText())) {
                showErrorMessage("Vui lòng nhập email");
                textFieldEmail.requestFocus();
                return false;
            }
            
            if (comboBoxGioiTinh.getValue() == null) {
                showErrorMessage("Vui lòng chọn giới tính");
                return false;
            }
            
            if (datePickerNgaySinh.getValue() == null) {
                showErrorMessage("Vui lòng chọn ngày sinh");
                return false;
            }
            
            if (comboBoxTrangThai.getValue() == null) {
                showErrorMessage("Vui lòng chọn trạng thái cư trú");
                return false;
            }
            
            // Validate email format
            if (!isValidEmail(textFieldEmail.getText().trim())) {
                showErrorMessage("Email không hợp lệ");
                textFieldEmail.requestFocus();
                return false;
            }
            
            // Validate phone number format
            if (!isValidPhoneNumber(textFieldSoDienThoai.getText().trim())) {
                showErrorMessage("Số điện thoại không hợp lệ (10-11 chữ số)");
                textFieldSoDienThoai.requestFocus();
                return false;
            }
        }
        
        return true;
    }

    private CanHoDto createCanHoDto() {
        // Tạo mã căn hộ từ thông tin tòa, tầng, số nhà
        String maCanHo = generateMaCanHo();
        
        // Tạo DTO căn hộ bằng constructor no-args và setter
        CanHoDto canHoDto = new CanHoDto();
        
        // Thiết lập thông tin căn hộ bằng cách trực tiếp gán vào các field
        try {
            // Sử dụng reflection để set các field nếu Lombok setter không hoạt động
            java.lang.reflect.Field[] fields = CanHoDto.class.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                switch (field.getName()) {
                    case "maCanHo":
                        field.set(canHoDto, maCanHo);
                        break;
                    case "toaNha":
                        field.set(canHoDto, textFieldToa.getText().trim());
                        break;
                    case "tang":
                        field.set(canHoDto, textFieldTang.getText().trim());
                        break;
                    case "soNha":
                        field.set(canHoDto, textFieldSoNha.getText().trim());
                        break;
                    case "dienTich":
                        field.set(canHoDto, Double.parseDouble(textFieldDienTich.getText().trim()));
                        break;
                    case "trangThaiKiThuat":
                        field.set(canHoDto, comboBoxTinhTrangKiThuat.getValue());
                        break;
                    case "trangThaiSuDung":
                        field.set(canHoDto, comboBoxTinhTrangSuDung.getValue());
                        break;
                    case "daBanChua":
                        field.set(canHoDto, false);
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi set field cho CanHoDto: " + e.getMessage());
        }
        
        // Tạo ChuHoDto nếu có chủ hộ
        if (choiceBoxThemChuSoHuu.isSelected()) {
            System.out.println("Thêm mã định danh chủ hộ có sẵn...");
            ChuHoDto chuHoDto = createChuHoDtoFromExisting();
            
            if (chuHoDto != null) {
                try {
                    java.lang.reflect.Field chuHoField = CanHoDto.class.getDeclaredField("chuHo");
                    chuHoField.setAccessible(true);
                    chuHoField.set(canHoDto, chuHoDto);
                    System.out.println("Đã thêm chủ hộ có sẵn với mã: " + textFieldMaDinhDanh.getText().trim());
                } catch (Exception e) {
                    System.err.println("Lỗi khi set chuHo cho CanHoDto: " + e.getMessage());
                }
            }
        } else if (choiceBoxTaoCuDanMoi.isSelected()) {
            System.out.println("Tạo cư dân mới làm chủ hộ...");
            ChuHoDto chuHoDto = createChuHoDtoFromNew();
            
            if (chuHoDto != null) {
                try {
                    java.lang.reflect.Field chuHoField = CanHoDto.class.getDeclaredField("chuHo");
                    chuHoField.setAccessible(true);
                    chuHoField.set(canHoDto, chuHoDto);
                    System.out.println("Đã tạo cư dân mới làm chủ hộ với mã: " + textFieldMaDinhDanhMoi.getText().trim());
                } catch (Exception e) {
                    System.err.println("Lỗi khi set chuHo cho CanHoDto: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Không thêm chủ hộ cho căn hộ này");
        }
        
        return canHoDto;
    }

    /**
     * Tạo ChuHoDto từ mã định danh có sẵn - chỉ cần mã để tham chiếu đến cư dân đã có
     */
    private ChuHoDto createChuHoDtoFromExisting() {
        try {
            ChuHoDto chuHoDto = new ChuHoDto();
            
            // Chỉ set mã định danh - service sẽ tìm thông tin cư dân từ DB
            java.lang.reflect.Field maDinhDanhField = ChuHoDto.class.getDeclaredField("maDinhDanh");
            maDinhDanhField.setAccessible(true);
            maDinhDanhField.set(chuHoDto, textFieldMaDinhDanh.getText().trim());
            
            System.out.println("Tạo ChuHoDto với mã định danh có sẵn: " + textFieldMaDinhDanh.getText().trim());
            
            return chuHoDto;
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo ChuHoDto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tạo ChuHoDto từ thông tin cư dân mới
     */
    private ChuHoDto createChuHoDtoFromNew() {
        try {
            ChuHoDto chuHoDto = new ChuHoDto();
            
            // Thiết lập thông tin cư dân mới đầy đủ
            java.lang.reflect.Field[] fields = ChuHoDto.class.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                switch (field.getName()) {
                    case "maDinhDanh":
                        field.set(chuHoDto, textFieldMaDinhDanhMoi.getText().trim());
                        break;
                    case "hoVaTen":
                        field.set(chuHoDto, textFieldHoVaTen.getText().trim());
                        break;
                    case "soDienThoai":
                        field.set(chuHoDto, textFieldSoDienThoai.getText().trim());
                        break;
                    case "email":
                        field.set(chuHoDto, textFieldEmail.getText().trim());
                        break;
                    case "trangThaiCuTru":
                        field.set(chuHoDto, comboBoxTrangThai.getValue());
                        break;
                    case "ngayChuyenDen":
                        if ("Cư trú".equals(comboBoxTrangThai.getValue())) {
                            field.set(chuHoDto, LocalDate.now());
                        }
                        break;
                }
            }
            
            System.out.println("Tạo ChuHoDto từ thông tin mới:");
            System.out.println("   - Mã định danh: " + textFieldMaDinhDanhMoi.getText().trim());
            System.out.println("   - Họ và tên: " + textFieldHoVaTen.getText().trim());
            System.out.println("   - SĐT: " + textFieldSoDienThoai.getText().trim());
            System.out.println("   - Email: " + textFieldEmail.getText().trim());
            System.out.println("   - Trạng thái: " + comboBoxTrangThai.getValue());
            
            return chuHoDto;
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo ChuHoDto từ thông tin mới: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method để lấy field value bằng reflection
     */
    private <T> T getFieldValue(Object obj, String fieldName, Class<T> fieldType) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return fieldType.cast(field.get(obj));
        } catch (Exception e) {
            return null;
        }
    }

    private String generateMaCanHo() {
        // Tạo mã căn hộ theo format: TOA-TANG-SONHA
        String toa = textFieldToa.getText().trim().toUpperCase();
        String tang = textFieldTang.getText().trim();
        String soNha = textFieldSoNha.getText().trim();
        
        return toa + "-" + tang + "-" + soNha;
    }

    private void clearForm() {
        // Clear apartment fields
        textFieldToa.clear();
        textFieldTang.clear();
        textFieldSoNha.clear();
        textFieldDienTich.clear();
        
        // Reset combo boxes to default
        comboBoxTinhTrangKiThuat.setValue("Tốt");
        comboBoxTinhTrangSuDung.setValue("Trống");
        
        // Clear owner fields
        clearOwnerFields();
        clearCuDanMoiFields();
        
        // Reset checkboxes
        choiceBoxThemChuSoHuu.setSelected(false);
        choiceBoxTaoCuDanMoi.setSelected(false);
        
        // Hide sections
        vBoxChuSoHuu.setVisible(false);
        vBoxChuSoHuu.setManaged(false);
        vBoxThongTinCuDanMoi.setVisible(false);
        vBoxThongTinCuDanMoi.setManaged(false);
    }

    private void clearOwnerFields() {
        textFieldMaDinhDanh.clear();
    }

    private void clearCuDanMoiFields() {
        textFieldMaDinhDanhMoi.clear();
        textFieldHoVaTen.clear();
        textFieldSoDienThoai.clear();
        textFieldEmail.clear();
        comboBoxGioiTinh.setValue(null);
        datePickerNgaySinh.setValue(null);
        comboBoxTrangThai.setValue("Cư trú");
    }

    private void clearErrorMessage() {
        textError.setVisible(false);
        textError.setText("");
    }

    private void showErrorMessage(String message) {
        textError.setText(message);
        textError.setVisible(true);
        textError.getStyleClass().add("error-text");
    }

    private void showSuccessMessage(String message) {
        // In thực tế, có thể hiển thị thông báo thành công
        System.out.println("Success: " + message);
    }

    private void closeWindow() {
        Stage stage = (Stage) buttonTaoCanHo.getScene().getWindow();
        stage.close();
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^[0-9]{10,11}$");
    }

    /**
     * Helper method để lấy success field từ ResponseDto bằng reflection
     */
    private boolean getResponseSuccess(ResponseDto response) {
        try {
            java.lang.reflect.Field field = ResponseDto.class.getDeclaredField("success");
            field.setAccessible(true);
            return (Boolean) field.get(response);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy success từ ResponseDto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method để lấy message field từ ResponseDto bằng reflection
     */
    private String getResponseMessage(ResponseDto response) {
        try {
            java.lang.reflect.Field field = ResponseDto.class.getDeclaredField("message");
            field.setAccessible(true);
            return (String) field.get(response);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy message từ ResponseDto: " + e.getMessage());
            return "Lỗi không xác định";
        }
    }
}
