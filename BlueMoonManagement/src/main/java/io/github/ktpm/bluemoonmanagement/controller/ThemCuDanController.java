package io.github.ktpm.bluemoonmanagement.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto;
import io.github.ktpm.bluemoonmanagement.service.cuDan.CuDanService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@Component
public class ThemCuDanController implements Initializable {

    // FXML Controls
    @FXML private Button button_close_up;
    @FXML private Label labelTieuDe;
    @FXML private TextField textFieldHoVaTen;
    @FXML private TextField textFieldMaDinhDanh;
    @FXML private ComboBox<String> comboBoxGioiTinh;
    @FXML private DatePicker datePickerNgaySinh;
    @FXML private TextField textFieldSoDienThoai;
    @FXML private TextField textFieldEmail;
    @FXML private TextField textFieldMaCanHo;
    @FXML private ComboBox<String> comboBoxTrangThai;
    @FXML private DatePicker datePickerNgayChuyenDen;
    @FXML private DatePicker datePickerNgayChuyenDi;
    @FXML private Text textError;
    @FXML private Button buttonThemCuDan;
    @FXML private Button buttonLuu;
    @FXML private Button buttonChinhSua;
    @FXML private Button buttonXoa;
    
    // HBox containers for date fields (to hide entire rows)
    @FXML private javafx.scene.layout.HBox hBoxNgayChuyenDen;
    @FXML private javafx.scene.layout.HBox hBoxNgayChuyenDi;

    // Services
    @Autowired
    private CuDanService cuDanService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ThemCuDanController được khởi tạo");
        
        // Setup ComboBoxes
        setupComboBoxes();
        
        // Setup event handlers
        setupEventHandlers();
        
        // Default setup for ADD mode
        setupAddMode();
        
        // Clear any error messages
        clearErrorMessage();
    }

    /**
     * Thiết lập các ComboBox
     */
    private void setupComboBoxes() {
        // Setup giới tính
        if (comboBoxGioiTinh != null) {
            comboBoxGioiTinh.setItems(FXCollections.observableArrayList(
                "Nam", "Nữ"
            ));
        }

        // Setup trạng thái cư trú
        if (comboBoxTrangThai != null) {
            comboBoxTrangThai.setItems(FXCollections.observableArrayList(
                "Cư trú", "Không cư trú"
            ));
            comboBoxTrangThai.setValue("Cư trú"); // Default value
        }
    }

    /**
     * Thiết lập event handlers
     */
    private void setupEventHandlers() {
        // Close button
        if (button_close_up != null) {
            button_close_up.setOnAction(this::handleClose);
        }

        // Add button
        if (buttonThemCuDan != null) {
            buttonThemCuDan.setOnAction(this::handleThemCuDan);
        }

        // Trạng thái change handler để enable/disable các field ngày
        if (comboBoxTrangThai != null) {
            comboBoxTrangThai.setOnAction(e -> {
                String trangThai = comboBoxTrangThai.getValue();
                handleTrangThaiChange(trangThai);
            });
        }
    }

    /**
     * Xử lý thay đổi trạng thái cư trú
     */
    private void handleTrangThaiChange(String trangThai) {
        if (trangThai == null) return;
        
        boolean isCuTru = "Cư trú".equals(trangThai);
        
        // Hiển thị HBox ngày chuyển đến chỉ khi trạng thái là "Cư trú"
        if (hBoxNgayChuyenDen != null) {
            hBoxNgayChuyenDen.setVisible(isCuTru);
            hBoxNgayChuyenDen.setDisable(!isCuTru);
        }
        
        // Set giá trị cho datePickerNgayChuyenDen
        if (datePickerNgayChuyenDen != null) {
            if (isCuTru) {
                // Set ngày hiện tại làm mặc định
                datePickerNgayChuyenDen.setValue(LocalDate.now());
            } else {
                // Xóa giá trị khi ẩn
                datePickerNgayChuyenDen.setValue(null);
            }
        }
        
        // Luôn ẩn HBox ngày chuyển đi
        if (hBoxNgayChuyenDi != null) {
            hBoxNgayChuyenDi.setVisible(false);
            hBoxNgayChuyenDi.setDisable(true);
        }
        
        // Clear giá trị ngày chuyển đi
        if (datePickerNgayChuyenDi != null) {
            datePickerNgayChuyenDi.setValue(null);
        }
    }

    /**
     * Setup cho ADD mode
     */
    private void setupAddMode() {
        if (labelTieuDe != null) labelTieuDe.setText("Thêm cư dân");
        if (buttonThemCuDan != null) buttonThemCuDan.setVisible(true);
        if (buttonLuu != null) buttonLuu.setVisible(false);
        if (buttonChinhSua != null) buttonChinhSua.setVisible(false);
        
        // Áp dụng logic hiển thị field ngày dựa trên trạng thái mặc định
        handleTrangThaiChange("Cư trú"); // Default value
    }

    /**
     * Setup cho EDIT mode với dữ liệu cư dân
     */
    public void setupEditMode(io.github.ktpm.bluemoonmanagement.controller.Home_list.CuDanTableData cuDanData) {
        try {
            System.out.println("Setup edit mode cho cư dân: " + cuDanData.getHoVaTen());
            
            // Thay đổi UI cho edit mode
            if (labelTieuDe != null) {
                labelTieuDe.setText("Chỉnh sửa cư dân");
                System.out.println("DEBUG: Title changed to 'Chỉnh sửa cư dân'");
            }
            if (buttonThemCuDan != null) {
                buttonThemCuDan.setVisible(false);
                System.out.println("DEBUG: buttonThemCuDan hidden");
            }
            if (buttonLuu != null) {
                buttonLuu.setVisible(true);
                buttonLuu.setOnAction(this::handleCapNhatCuDan);
                System.out.println("DEBUG: buttonLuu shown and handler set");
            } else {
                System.out.println("ERROR: buttonLuu is null!");
            }
            if (buttonChinhSua != null) {
                buttonChinhSua.setVisible(false);
                System.out.println("DEBUG: buttonChinhSua hidden");
            }
            if (buttonXoa != null) {
                buttonXoa.setVisible(true);
                buttonXoa.setOnAction(this::handleXoaCuDan);
                System.out.println("DEBUG: buttonXoa shown and handler set");
            } else {
                System.out.println("ERROR: buttonXoa is null!");
            }
            
            // Populate form với dữ liệu hiện tại
            populateFormWithData(cuDanData);
            
        } catch (Exception e) {
            System.err.println("Lỗi khi setup edit mode: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Populate form với dữ liệu cư dân
     */
    private void populateFormWithData(io.github.ktpm.bluemoonmanagement.controller.Home_list.CuDanTableData cuDanData) {
        try {
            // Fill form fields
            if (textFieldMaDinhDanh != null) {
                textFieldMaDinhDanh.setText(cuDanData.getMaDinhDanh());
                textFieldMaDinhDanh.setEditable(false); // Không cho phép chỉnh sửa mã định danh
            }
            
            if (textFieldHoVaTen != null) {
                textFieldHoVaTen.setText(cuDanData.getHoVaTen());
            }
            
            if (comboBoxGioiTinh != null) {
                comboBoxGioiTinh.setValue(cuDanData.getGioiTinh());
            }
            
            if (datePickerNgaySinh != null && cuDanData.getNgaySinh() != null && !cuDanData.getNgaySinh().isEmpty()) {
                try {
                    datePickerNgaySinh.setValue(LocalDate.parse(cuDanData.getNgaySinh()));
                } catch (Exception e) {
                    System.err.println("Lỗi parse ngày sinh: " + e.getMessage());
                }
            }
            
            if (textFieldSoDienThoai != null) {
                textFieldSoDienThoai.setText(cuDanData.getSoDienThoai());
            }
            
            if (textFieldEmail != null) {
                textFieldEmail.setText(cuDanData.getEmail());
            }
            
            if (textFieldMaCanHo != null) {
                textFieldMaCanHo.setText(cuDanData.getMaCanHo());
            }
            
            if (comboBoxTrangThai != null) {
                comboBoxTrangThai.setValue(cuDanData.getTrangThaiCuTru());
            }
            
            if (datePickerNgayChuyenDen != null && cuDanData.getNgayChuyenDen() != null && !cuDanData.getNgayChuyenDen().isEmpty()) {
                try {
                    datePickerNgayChuyenDen.setValue(LocalDate.parse(cuDanData.getNgayChuyenDen()));
                } catch (Exception e) {
                    System.err.println("Lỗi parse ngày chuyển đến: " + e.getMessage());
                }
            }
            
            // Apply trạng thái change logic
            handleTrangThaiChange(cuDanData.getTrangThaiCuTru());
            
        } catch (Exception e) {
            System.err.println("Lỗi khi populate form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý xóa cư dân (edit mode)
     */
    @FXML
    private void handleXoaCuDan(ActionEvent event) {
        try {
            // Check permission
            if (!hasPermission()) {
                showErrorMessage("Bạn không có quyền xóa cư dân. Chỉ Tổ phó mới được phép.");
                return;
            }

            String maDinhDanh = textFieldMaDinhDanh.getText();
            String hoVaTen = textFieldHoVaTen.getText();
            
            if (maDinhDanh == null || maDinhDanh.trim().isEmpty()) {
                showErrorMessage("Không thể xóa: Mã định danh không hợp lệ.");
                return;
            }

            // Hiển thị dialog xác nhận tùy chỉnh
            boolean confirmed = showCustomConfirmDialog(hoVaTen, maDinhDanh);
            
            if (confirmed) {
                // Call service to delete
                boolean deleted = cuDanService.xoaMem(maDinhDanh);

                if (deleted) {
                    showSuccessMessage("Xóa cư dân thành công!");
                    
                    // Close window after successful deletion
                    javafx.application.Platform.runLater(() -> {
                        try {
                            Thread.sleep(1000); // Show success message for 1 second
                            closeWindow();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            closeWindow();
                        }
                    });
                } else {
                    showErrorMessage("Không thể xóa cư dân. Vui lòng thử lại.");
                }
            }

        } catch (Exception e) {
            showErrorMessage("Có lỗi xảy ra khi xóa cư dân: " + e.getMessage());
            System.err.println("Error in handleXoaCuDan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý cập nhật cư dân (edit mode)
     */
    @FXML
    private void handleCapNhatCuDan(ActionEvent event) {
        try {
            clearErrorMessage();
            
            // Validate input
            if (!validateInput()) {
                return;
            }

            // Check permission
            if (!hasPermission()) {
                showErrorMessage("Bạn không có quyền cập nhật cư dân. Chỉ Tổ phó mới được phép.");
                return;
            }

            // Create DTO
            CudanDto cuDanDto = createCuDanDto();

            // Call service to update
            ResponseDto response = cuDanService.updateCuDan(cuDanDto);

            if (response.isSuccess()) {
                // Close window immediately after successful update
                closeWindow();
            } else {
                showErrorMessage("Lỗi: " + response.getMessage());
            }

        } catch (Exception e) {
            showErrorMessage("Có lỗi xảy ra: " + e.getMessage());
            System.err.println("Error in handleCapNhatCuDan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện thêm cư dân
     */
    @FXML
    private void handleThemCuDan(ActionEvent event) {
        try {
            clearErrorMessage();
            
            // Validate input
            if (!validateInput()) {
                return;
            }

            // Check permission
            if (!hasPermission()) {
                showErrorMessage("Bạn không có quyền thêm cư dân. Chỉ Tổ phó mới được phép.");
                return;
            }

            // Create DTO
            CudanDto cuDanDto = createCuDanDto();

            // Call service
            ResponseDto response = cuDanService.addCuDan(cuDanDto);

            if (response.isSuccess()) {
                showSuccessMessage("Thêm cư dân thành công!");
                
                // Refresh apartment detail windows if apartment code was provided
                if (cuDanDto.getMaCanHo() != null && !cuDanDto.getMaCanHo().trim().isEmpty()) {
                    // Small delay to ensure transaction is committed
                    try {
                        Thread.sleep(100); // 100ms delay
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    ChiTietCanHoController.refreshAllWindowsForApartment(cuDanDto.getMaCanHo());
                }
                
                clearForm();
                closeWindow();
            } else {
                showErrorMessage("Lỗi: " + response.getMessage());
            }

        } catch (Exception e) {
            showErrorMessage("Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện đóng
     */
    @FXML
    private void handleClose(ActionEvent event) {
        closeWindow();
    }

    /**
     * Validate input data
     */
    private boolean validateInput() {
        // Validate họ và tên
        if (isBlank(textFieldHoVaTen.getText())) {
            showErrorMessage("Vui lòng nhập họ và tên");
            textFieldHoVaTen.requestFocus();
            return false;
        }

        // Validate mã định danh
        if (isBlank(textFieldMaDinhDanh.getText())) {
            showErrorMessage("Vui lòng nhập mã định danh");
            textFieldMaDinhDanh.requestFocus();
            return false;
        }

        // Validate giới tính
        if (comboBoxGioiTinh.getValue() == null) {
            showErrorMessage("Vui lòng chọn giới tính");
            return false;
        }

        // Validate ngày sinh
        if (datePickerNgaySinh.getValue() == null) {
            showErrorMessage("Vui lòng chọn ngày sinh");
            return false;
        }

        // Validate ngày sinh không được trong tương lai
        if (datePickerNgaySinh.getValue().isAfter(LocalDate.now())) {
            showErrorMessage("Ngày sinh không được trong tương lai");
            return false;
        }

        // Validate tuổi hợp lý (không quá 150 tuổi)
        if (datePickerNgaySinh.getValue().isBefore(LocalDate.now().minusYears(150))) {
            showErrorMessage("Ngày sinh không hợp lý");
            return false;
        }

        // Validate số điện thoại
        if (isBlank(textFieldSoDienThoai.getText())) {
            showErrorMessage("Vui lòng nhập số điện thoại");
            textFieldSoDienThoai.requestFocus();
            return false;
        }

        // Validate phone number format (Vietnam)
        if (!isValidPhoneNumber(textFieldSoDienThoai.getText().trim())) {
            showErrorMessage("Số điện thoại không hợp lệ");
            textFieldSoDienThoai.requestFocus();
            return false;
        }

        // Validate email
        if (isBlank(textFieldEmail.getText())) {
            showErrorMessage("Vui lòng nhập email");
            textFieldEmail.requestFocus();
            return false;
        }

        if (!isValidEmail(textFieldEmail.getText().trim())) {
            showErrorMessage("Email không hợp lệ");
            textFieldEmail.requestFocus();
            return false;
        }

        // Mã căn hộ không bắt buộc - có thể để trống
        // Removed validation for mã căn hộ to make it optional

        // Validate trạng thái
        if (comboBoxTrangThai.getValue() == null) {
            showErrorMessage("Vui lòng chọn trạng thái cư trú");
            return false;
        }

        // Validate ngày chuyển đến - chỉ yêu cầu khi trạng thái là "Cư trú"
        if ("Cư trú".equals(comboBoxTrangThai.getValue())) {
            if (datePickerNgayChuyenDen.getValue() == null) {
                showErrorMessage("Vui lòng chọn ngày chuyển đến khi trạng thái là 'Cư trú'");
                return false;
            }
        }

        return true;
    }

    /**
     * Tạo CuDanDto từ form data
     */
    private CudanDto createCuDanDto() {
        CudanDto cuDanDto = new CudanDto();
        
        cuDanDto.setMaDinhDanh(textFieldMaDinhDanh.getText().trim());
        cuDanDto.setHoVaTen(textFieldHoVaTen.getText().trim());
        cuDanDto.setGioiTinh(comboBoxGioiTinh.getValue());
        cuDanDto.setNgaySinh(datePickerNgaySinh.getValue());
        cuDanDto.setSoDienThoai(textFieldSoDienThoai.getText().trim());
        cuDanDto.setEmail(textFieldEmail.getText().trim());
        
        // Mã căn hộ có thể null hoặc rỗng
        String maCanHo = textFieldMaCanHo.getText();
        cuDanDto.setMaCanHo(isBlank(maCanHo) ? null : maCanHo.trim());
        
        cuDanDto.setTrangThaiCuTru(comboBoxTrangThai.getValue());
        cuDanDto.setNgayChuyenDen(datePickerNgayChuyenDen.getValue());

        return cuDanDto;
    }

    /**
     * Check user permission
     */
    private boolean hasPermission() {
        return Session.getCurrentUser() != null && 
               "Tổ phó".equals(Session.getCurrentUser().getVaiTro());
    }

    /**
     * Kiểm tra string có blank không
     */
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }

    /**
     * Validate Vietnam phone number format
     */
    private boolean isValidPhoneNumber(String phone) {
        // Remove all spaces and special characters
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        
        // Check Vietnam phone patterns
        return cleanPhone.matches("^(0[3|5|7|8|9])[0-9]{8}$") || // Mobile numbers
               cleanPhone.matches("^(84[3|5|7|8|9])[0-9]{8}$");   // International format
    }

    /**
     * Clear form
     */
    private void clearForm() {
        if (textFieldHoVaTen != null) textFieldHoVaTen.clear();
        if (textFieldMaDinhDanh != null) textFieldMaDinhDanh.clear();
        if (comboBoxGioiTinh != null) comboBoxGioiTinh.setValue(null);
        if (datePickerNgaySinh != null) datePickerNgaySinh.setValue(null);
        if (textFieldSoDienThoai != null) textFieldSoDienThoai.clear();
        if (textFieldEmail != null) textFieldEmail.clear();
        if (textFieldMaCanHo != null) textFieldMaCanHo.clear();
        if (comboBoxTrangThai != null) comboBoxTrangThai.setValue("Cư trú");
        
        // Áp dụng logic hiển thị field ngày sau khi reset về "Cư trú"
        handleTrangThaiChange("Cư trú");
        
        clearErrorMessage();
    }

    /**
     * Error message handling
     */
    private void showErrorMessage(String message) {
        if (textError != null) {
            textError.setText(message);
            textError.setVisible(true);
        }
    }

    private void clearErrorMessage() {
        if (textError != null) {
            textError.setText("");
            textError.setVisible(false);
        }
    }

    /**
     * Success message
     */
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Close window
     */
    private void closeWindow() {
        try {
            Stage stage = (Stage) button_close_up.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("Lỗi khi đóng cửa sổ: " + e.getMessage());
        }
    }

    /**
     * Hiển thị dialog xác nhận tùy chỉnh
     */
    private boolean showCustomConfirmDialog(String hoVaTen, String maDinhDanh) {
        try {
            // Load FXML file
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/xac_nhan.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            // Get controller
            XacNhanController controller = loader.getController();
            
            // Set content
            controller.setTitle("Xác nhận xóa cư dân");
            controller.setContent("Bạn có chắc chắn muốn xóa cư dân:\n" + 
                                hoVaTen + " (" + maDinhDanh + ")?");
            
            // Create and show dialog
            javafx.stage.Stage dialogStage = new javafx.stage.Stage();
            dialogStage.setTitle("Xác nhận");
            dialogStage.setScene(new javafx.scene.Scene(root));
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            
            // Set owner
            javafx.stage.Stage ownerStage = (javafx.stage.Stage) buttonXoa.getScene().getWindow();
            dialogStage.initOwner(ownerStage);
            
            // Set position to center of parent
            dialogStage.setX(ownerStage.getX() + (ownerStage.getWidth() - 450) / 2);
            dialogStage.setY(ownerStage.getY() + (ownerStage.getHeight() - 220) / 2);
            
            // Show and wait
            dialogStage.showAndWait();
            
            // Return result
            return controller.isConfirmed();
            
        } catch (Exception e) {
            System.err.println("Lỗi khi hiển thị dialog xác nhận: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to default dialog
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Xóa cư dân");
            alert.setContentText("Bạn có chắc chắn muốn xóa cư dân:\n" + 
                               hoVaTen + " (" + maDinhDanh + ")?");
            Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
        }
    }

    // Setter for dependency injection
    public void setCuDanService(CuDanService cuDanService) {
        this.cuDanService = cuDanService;
    }
} 