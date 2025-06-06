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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ThemCanHoButton implements Initializable {

    @FXML
    private FontAwesomeIcon buttonClose;

    @FXML
    private Button buttonTaoCanHo;

    @FXML
    private Button buttonXoaCanHo;

    @FXML
    private Label labelTitle;

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
    
    // Edit mode tracking
    private boolean isEditMode = false;
    private String originalMaCanHo;
    private Long originalChuHoId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Reset về create mode mỗi khi initialize
        resetToCreateMode();
        setupUI();
    }
    
    /**
     * Reset về chế độ tạo mới
     */
    private void resetToCreateMode() {
        this.isEditMode = false;
        this.originalMaCanHo = null;
        this.originalChuHoId = null;
        
        // Reset UI elements
        if (labelTitle != null) {
            labelTitle.setText("Thêm căn hộ mới");
        }
        if (buttonTaoCanHo != null) {
            buttonTaoCanHo.setText("Tạo căn hộ");
        }
        if (buttonXoaCanHo != null) {
            buttonXoaCanHo.setVisible(false);
            buttonXoaCanHo.setManaged(false);
        }
        
        System.out.println("DEBUG: Reset to create mode");
    }

    /**
     * Setter để inject service từ bên ngoài
     */
    public void setCanHoService(CanHoService canHoService) {
        this.canHoService = canHoService;
    }
    
    /**
     * Setup edit mode cho căn hộ
     */
    public void setupEditMode(CanHoDto canHoDto) {
        this.isEditMode = true;
        this.originalMaCanHo = canHoDto.getMaCanHo();
        
        // Lưu ID chủ hộ hiện tại nếu có
        if (canHoDto.getChuHo() != null) {
            try {
                java.lang.reflect.Field idField = canHoDto.getChuHo().getClass().getDeclaredField("id");
                idField.setAccessible(true);
                this.originalChuHoId = (Long) idField.get(canHoDto.getChuHo());
                System.out.println("DEBUG: Saved original ChuHo ID: " + this.originalChuHoId);
            } catch (Exception e) {
                System.err.println("ERROR: Cannot get ChuHo ID: " + e.getMessage());
                this.originalChuHoId = null;
            }
        }
        
        // Populate form với dữ liệu hiện có
        populateForm(canHoDto);
        
        // Thay đổi UI
        if (labelTitle != null) {
            labelTitle.setText("Chỉnh sửa căn hộ");
        }
        if (buttonTaoCanHo != null) {
            buttonTaoCanHo.setText("Cập nhật căn hộ");
        }
        if (buttonXoaCanHo != null) {
            buttonXoaCanHo.setVisible(true);
            buttonXoaCanHo.setManaged(true);
        }
        
        // Ẩn các phần không cần thiết khi edit
        hideUnnecessarySections();
        
        System.out.println("DEBUG: Edit mode setup completed for apartment: " + canHoDto.getMaCanHo());
    }
    
    /**
     * Populate form với dữ liệu căn hộ
     */
    private void populateForm(CanHoDto canHoDto) {
        try {
            if (textFieldToa != null) textFieldToa.setText(canHoDto.getToaNha());
            if (textFieldTang != null) textFieldTang.setText(canHoDto.getTang());
            if (textFieldSoNha != null) textFieldSoNha.setText(canHoDto.getSoNha());
            if (textFieldDienTich != null) textFieldDienTich.setText(String.valueOf(canHoDto.getDienTich()));
            if (comboBoxTinhTrangKiThuat != null) comboBoxTinhTrangKiThuat.setValue(canHoDto.getTrangThaiKiThuat());
            if (comboBoxTinhTrangSuDung != null) {
                comboBoxTinhTrangSuDung.setValue(canHoDto.getTrangThaiSuDung());
                comboBoxTinhTrangSuDung.setDisable(false); // Enable for editing
            }
            
            // Luôn hiển thị section chủ hộ trong edit mode
            if (vBoxChuSoHuu != null) {
                vBoxChuSoHuu.setVisible(true);
                vBoxChuSoHuu.setManaged(true);
            }
            
            // Hiển thị thông tin chủ hộ hiện tại nếu có cho chế độ edit
            if (canHoDto.getChuHo() != null) {
                if (textFieldMaDinhDanh != null) {
                    textFieldMaDinhDanh.setText(canHoDto.getChuHo().getMaDinhDanh());
                    textFieldMaDinhDanh.setPromptText("ID hiện tại: " + canHoDto.getChuHo().getMaDinhDanh() + " (để trống = giữ nguyên, nhập ID mới = thay thế)");
                }
            } else {
                // Không có chủ hộ hiện tại
                if (textFieldMaDinhDanh != null) {
                    textFieldMaDinhDanh.clear();
                    textFieldMaDinhDanh.setPromptText("Nhập ID chủ hộ mới (tùy chọn)");
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: Cannot populate form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Ẩn các section không cần thiết khi edit
     */
    private void hideUnnecessarySections() {
        // Ẩn phần tạo cư dân mới (không cần thiết trong edit mode)
        if (vBoxThongTinCuDanMoi != null) {
            vBoxThongTinCuDanMoi.setVisible(false);
            vBoxThongTinCuDanMoi.setManaged(false);
        }
        
        // Ẩn checkbox tạo cư dân mới (không cần thiết trong edit mode)
        if (choiceBoxTaoCuDanMoi != null) {
            choiceBoxTaoCuDanMoi.setVisible(false);
            choiceBoxTaoCuDanMoi.setManaged(false);
        }
        
        // Ẩn checkbox thêm chủ sở hữu (vì trong edit mode luôn hiển thị field ID)
        if (choiceBoxThemChuSoHuu != null) {
            choiceBoxThemChuSoHuu.setVisible(false);
            choiceBoxThemChuSoHuu.setManaged(false);
        }
        
        // KHÔNG ẩn vBoxChuSoHuu vì cần hiển thị field nhập ID chủ hộ trong edit mode
        // vBoxChuSoHuu sẽ được hiển thị trong populateForm()
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
        comboBoxTinhTrangKiThuat.getItems().addAll("Bình thường", "Đang bảo trì", "Hỏng");
        
        // ComboBox Tình trạng sử dụng
        comboBoxTinhTrangSuDung.getItems().addAll("Đang sử dụng", "Trống");
        
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
        
        // Enable/disable comboBoxTinhTrangSuDung based on "Đã bán" selection
        comboBoxTinhTrangSuDung.setDisable(!isSelected);
        if (!isSelected) {
            comboBoxTinhTrangSuDung.setValue(null); // Clear selection when disabled
        }
        
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
        System.out.println("Tình trạng sử dụng enabled: " + !comboBoxTinhTrangSuDung.isDisabled());
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
        
        // Validate dữ liệu đầu vào ngay lập tức
        if (!validateInput()) {
            return;
        }
        
        // Hiển thị loading và disable button ngay lập tức
        showLoadingState();
        
        // Tạo DTO căn hộ
        CanHoDto canHoDto = createCanHoDto();
        
        // Chạy xử lý ở background thread
        javafx.concurrent.Task<ResponseDto> task = new javafx.concurrent.Task<ResponseDto>() {
            @Override
            protected ResponseDto call() throws Exception {
                // Gọi service để thêm hoặc cập nhật căn hộ
                if (isEditMode) {
                    return canHoService.updateCanHo(canHoDto);
                } else {
                    return canHoService.addCanHo(canHoDto);
                }
            }
            
            @Override
            protected void succeeded() {
                // Chạy trên UI thread
                javafx.application.Platform.runLater(() -> {
                    hideLoadingState();
                    
                    ResponseDto response = getValue();
                    boolean isSuccess = getResponseSuccess(response);
                    String message = getResponseMessage(response);
                    
                    if (isSuccess) {
                        String successMsg = isEditMode ? "Cập nhật căn hộ thành công!" : "Thêm căn hộ thành công!";
                        showSuccessMessage(successMsg);
                        
                        // Đóng window ngay lập tức
                        closeWindow();
                    } else {
                        showErrorMessage("Lỗi: " + message);
                    }
                });
            }
            
            @Override
            protected void failed() {
                // Chạy trên UI thread
                javafx.application.Platform.runLater(() -> {
                    hideLoadingState();
                    Throwable exception = getException();
                    showErrorMessage("Đã xảy ra lỗi: " + exception.getMessage());
                    exception.printStackTrace();
                });
            }
        };
        
        // Chạy task ở background
        Thread taskThread = new Thread(task);
        taskThread.setDaemon(true);
        taskThread.start();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        closeWindow();
    }
    
    @FXML
    private void handleXoaCanHo(ActionEvent event) {
        try {
            if (!isEditMode || originalMaCanHo == null) {
                showErrorMessage("Chỉ có thể xóa căn hộ trong chế độ chỉnh sửa");
                return;
            }
            
            // Kiểm tra quyền
            if (!hasPermission()) {
                showErrorMessage("Bạn không có quyền xóa căn hộ. Chỉ Tổ phó mới được phép.");
                return;
            }
            
            // Hiển thị dialog xác nhận
            boolean confirmed = showDeleteConfirmation();
            if (!confirmed) {
                return;
            }
            
            // Hiển thị loading
            showLoadingState();
            
            // Xóa căn hộ ở background thread
            javafx.concurrent.Task<ResponseDto> deleteTask = new javafx.concurrent.Task<ResponseDto>() {
                @Override
                protected ResponseDto call() throws Exception {
                    // Tạo CanHoDto chỉ với mã căn hộ để xóa
                    CanHoDto canHoToDelete = new CanHoDto();
                    try {
                        java.lang.reflect.Field maCanHoField = CanHoDto.class.getDeclaredField("maCanHo");
                        maCanHoField.setAccessible(true);
                        maCanHoField.set(canHoToDelete, originalMaCanHo);
                    } catch (Exception e) {
                        System.err.println("ERROR: Cannot set apartment code for delete: " + e.getMessage());
                        throw new RuntimeException("Cannot prepare apartment data for deletion");
                    }
                    return canHoService.deleteCanHo(canHoToDelete);
                }
                
                @Override
                protected void succeeded() {
                    javafx.application.Platform.runLater(() -> {
                        hideLoadingState();
                        
                        ResponseDto response = getValue();
                        boolean isSuccess = getResponseSuccess(response);
                        String message = getResponseMessage(response);
                        
                        if (isSuccess) {
                            showSuccessMessage("Xóa căn hộ thành công!");
                            closeWindow();
                        } else {
                            showErrorMessage("Lỗi xóa căn hộ: " + message);
                        }
                    });
                }
                
                @Override
                protected void failed() {
                    javafx.application.Platform.runLater(() -> {
                        hideLoadingState();
                        Throwable exception = getException();
                        showErrorMessage("Đã xảy ra lỗi khi xóa: " + exception.getMessage());
                        exception.printStackTrace();
                    });
                }
            };
            
            Thread deleteThread = new Thread(deleteTask);
            deleteThread.setDaemon(true);
            deleteThread.start();
            
        } catch (Exception e) {
            showErrorMessage("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Hiển thị dialog xác nhận xóa căn hộ
     */
    private boolean showDeleteConfirmation() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/xac_nhan.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            XacNhanController controller = loader.getController();
            controller.setTitle("Xác nhận xóa căn hộ");
            controller.setContent("Bạn có chắc chắn muốn xóa căn hộ '" + originalMaCanHo + "' không?\n\n" +
                                "Hành động này không thể hoàn tác!");
            
            javafx.stage.Stage confirmStage = new javafx.stage.Stage();
            confirmStage.setTitle("Xác nhận xóa");
            confirmStage.setScene(new javafx.scene.Scene(root));
            confirmStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            confirmStage.initOwner(buttonXoaCanHo.getScene().getWindow());
            confirmStage.setResizable(false);
            
            confirmStage.showAndWait();
            
            return controller.isConfirmed();
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot show delete confirmation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
            
            return "Tổ phó".equals(vaiTro);
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
                        // Set trangThaiSuDung based on logic
                        String trangThaiSuDung = comboBoxTinhTrangSuDung.getValue();
                        if (trangThaiSuDung == null || trangThaiSuDung.isEmpty()) {
                            // Nếu không có chủ hộ thì mặc định là "Trống"
                            trangThaiSuDung = "Trống";
                        }
                        field.set(canHoDto, trangThaiSuDung);
                        break;
                    case "daBanChua":
                        field.set(canHoDto, choiceBoxThemChuSoHuu.isSelected());
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi set field cho CanHoDto: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Debug logging
        System.out.println("DEBUG: Created CanHoDto:");
        System.out.println("  - Mã căn hộ: " + maCanHo);
        System.out.println("  - Tòa nhà: " + textFieldToa.getText().trim());
        System.out.println("  - Tầng: " + textFieldTang.getText().trim());
        System.out.println("  - Số nhà: " + textFieldSoNha.getText().trim());
        System.out.println("  - Diện tích: " + textFieldDienTich.getText().trim());
        System.out.println("  - Tình trạng kỹ thuật: " + comboBoxTinhTrangKiThuat.getValue());
        System.out.println("  - Tình trạng sử dụng: " + comboBoxTinhTrangSuDung.getValue());
        System.out.println("  - Đã bán: " + choiceBoxThemChuSoHuu.isSelected());
        System.out.println("  - IsEditMode: " + isEditMode);
        
        // Xử lý chủ hộ dựa trên mode
        if (isEditMode) {
            // Chế độ edit: xử lý chủ hộ thông minh
            handleOwnerInEditMode(canHoDto);
        } else {
            // Chế độ create: logic cũ
            handleOwnerInCreateMode(canHoDto);
        }
        
        return canHoDto;
    }
    
    /**
     * Xử lý chủ hộ trong chế độ edit
     */
    private void handleOwnerInEditMode(CanHoDto canHoDto) {
        String currentMaDinhDanh = textFieldMaDinhDanh.getText().trim();
        
        if (currentMaDinhDanh.isEmpty()) {
            // Giữ nguyên chủ hộ hiện tại (nếu có)
            if (originalChuHoId != null) {
                ChuHoDto chuHoDto = new ChuHoDto();
                try {
                    // Set ID của chủ hộ hiện tại để giữ nguyên
                    java.lang.reflect.Field idField = ChuHoDto.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(chuHoDto, originalChuHoId);
                    
                    java.lang.reflect.Field chuHoField = CanHoDto.class.getDeclaredField("chuHo");
                    chuHoField.setAccessible(true);
                    chuHoField.set(canHoDto, chuHoDto);
                    
                    System.out.println("DEBUG: Giữ nguyên chủ hộ hiện tại với ID: " + originalChuHoId);
                } catch (Exception e) {
                    System.err.println("ERROR: Cannot preserve current owner: " + e.getMessage());
                }
            } else {
                System.out.println("DEBUG: Không có chủ hộ để giữ nguyên");
            }
        } else {
            // Thay thế bằng chủ hộ mới
            ChuHoDto chuHoDto = createChuHoDtoFromExisting();
            if (chuHoDto != null) {
                try {
                    java.lang.reflect.Field chuHoField = CanHoDto.class.getDeclaredField("chuHo");
                    chuHoField.setAccessible(true);
                    chuHoField.set(canHoDto, chuHoDto);
                    System.out.println("DEBUG: Thay thế chủ hộ mới với ID: " + currentMaDinhDanh);
                } catch (Exception e) {
                    System.err.println("ERROR: Cannot set new owner: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Xử lý chủ hộ trong chế độ create
     */
    private void handleOwnerInCreateMode(CanHoDto canHoDto) {
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
        // Nếu đang ở edit mode, sử dụng mã căn hộ gốc
        if (isEditMode && originalMaCanHo != null) {
            return originalMaCanHo;
        }
        
        // Tạo mã căn hộ theo format: TOA-TANG-SONHA cho create mode
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
        
        // Disable tình trạng sử dụng when form is cleared
        comboBoxTinhTrangSuDung.setDisable(true);
        
        // Clear owner fields
        clearOwnerFields();
        clearCuDanMoiFields();
        
        // Reset checkboxes
        choiceBoxThemChuSoHuu.setSelected(false);
        choiceBoxTaoCuDanMoi.setSelected(false);
        
        // Show checkboxes (in case they were hidden in edit mode)
        if (choiceBoxThemChuSoHuu != null) {
            choiceBoxThemChuSoHuu.setVisible(true);
            choiceBoxThemChuSoHuu.setManaged(true);
        }
        
        if (choiceBoxTaoCuDanMoi != null) {
            choiceBoxTaoCuDanMoi.setVisible(true);
            choiceBoxTaoCuDanMoi.setManaged(true);
        }
        
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
        ThongBaoController.showError("Lỗi", message);
    }

    private void showSuccessMessage(String message) {
        ThongBaoController.showSuccess("Thành công", message);
    }
    
    /**
     * Hiển thị trạng thái loading
     */
    private void showLoadingState() {
        if (buttonTaoCanHo != null) {
            buttonTaoCanHo.setDisable(true);
            buttonTaoCanHo.setText(isEditMode ? "Đang cập nhật..." : "Đang tạo...");
        }
        
        if (textError != null) {
            textError.setText(isEditMode ? "Đang cập nhật căn hộ..." : "Đang tạo căn hộ...");
            textError.setStyle("-fx-fill: blue; -fx-font-style: italic;");
            textError.setVisible(true);
        }
    }
    
    /**
     * Ẩn trạng thái loading
     */
    private void hideLoadingState() {
        if (buttonTaoCanHo != null) {
            buttonTaoCanHo.setDisable(false);
            buttonTaoCanHo.setText(isEditMode ? "Cập nhật căn hộ" : "Tạo căn hộ");
        }
        
        if (textError != null) {
            textError.setText("");
            textError.setVisible(false);
        }
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
    @FXML
    void daBanClicked(ActionEvent event) {

    }
}
