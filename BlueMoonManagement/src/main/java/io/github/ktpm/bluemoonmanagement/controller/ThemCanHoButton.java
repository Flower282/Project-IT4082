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
    private Button buttonTaoChuHo;

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
    
    // ApplicationContext để lấy các service khác
    private org.springframework.context.ApplicationContext applicationContext;
    
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
     * Setter để inject ApplicationContext từ bên ngoài
     */
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        System.out.println("DEBUG: ApplicationContext injected into ThemCanHoButton");
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
        
        // Tập trung vào textFieldMaDinhDanh khi được chọn
        if (isSelected) {
            textFieldMaDinhDanh.requestFocus();
        } else {
            textFieldMaDinhDanh.clear();
        }
        
        System.out.println("Thêm chủ sở hữu: " + isSelected);
        System.out.println("Tình trạng sử dụng enabled: " + !comboBoxTinhTrangSuDung.isDisabled());
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
                        
                        // Refresh main apartments table and switch to apartments tab
                        refreshMainApartmentsTable();
                        
                        showSuccessMessage(successMsg );
                        
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

    /**
     * Xử lý sự kiện khi bấm nút "Tạo chủ hộ"
     * Mở form tạo cư dân mới sử dụng cu_dan.fxml
     */
    @FXML
    private void handleTaoChuHo(ActionEvent event) {
        try {
            System.out.println("=== DEBUG: Opening resident creation form ===");
            
            // Thử lấy FxViewLoader từ ApplicationContext
            if (applicationContext != null) {
                try {
                    io.github.ktpm.bluemoonmanagement.util.FxViewLoader fxViewLoader = 
                        applicationContext.getBean(io.github.ktpm.bluemoonmanagement.util.FxViewLoader.class);
                    
                    // Load view + controller using FxViewLoader
                    io.github.ktpm.bluemoonmanagement.util.FxView<?> fxView = fxViewLoader.loadFxView("/view/cu_dan.fxml");
                    
                    // Get controller và inject ApplicationContext
                    Object controller = fxView.getController();
                    io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController cuDanController = null;
                    if (controller instanceof io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController) {
                        cuDanController = (io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController) controller;
                        
                        // Set ApplicationContext for controller
                        cuDanController.setApplicationContext(applicationContext);
                        System.out.println("=== DEBUG: ApplicationContext injected to ThemCuDanController ===");
                    }
                    
                    // Tạo stage mới cho form
                    Stage stage = new Stage();
                    stage.setTitle("Tạo cư dân mới");
                    stage.setScene(new Scene(fxView.getView()));
                    stage.setResizable(true);
                    stage.setMinWidth(700);
                    stage.setMinHeight(600);
                    stage.initOwner(buttonTaoChuHo.getScene().getWindow());
                    stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
                    
                    // Hiển thị form và đợi đóng
                    stage.showAndWait();
                    
                    System.out.println("=== DEBUG: Resident creation form closed ===");
                    
                    // Sau khi form đóng, lấy mã định danh của cư dân vừa tạo và điền vào form căn hộ
                    if (cuDanController != null) {
                        String newCuDanMaDinhDanh = cuDanController.getLastCreatedCuDanMaDinhDanh();
                        if (newCuDanMaDinhDanh != null && !newCuDanMaDinhDanh.trim().isEmpty()) {
                            // Tự động điền mã định danh vào field
                            if (textFieldMaDinhDanh != null) {
                                textFieldMaDinhDanh.setText(newCuDanMaDinhDanh);
                                System.out.println("=== DEBUG: Auto-filled resident ID: " + newCuDanMaDinhDanh + " ===");
                                
                                // Đảm bảo checkbox "Đã bán" được check
                                if (choiceBoxThemChuSoHuu != null) {
                                    choiceBoxThemChuSoHuu.setSelected(true);
                                    handleThemChuSoHuuChange(null); // Trigger để hiện section chủ sở hữu
                                }
                                
                            }
                        }
                    }
                    
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to use FxViewLoader, falling back to manual FXML loading: " + e.getMessage());
                    // Fallback to manual FXML loading
                    loadCuDanFormManually();
                }
            } else {
                System.err.println("WARNING: ApplicationContext is null, using manual FXML loading");
                loadCuDanFormManually();
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to open resident creation form: " + e.getMessage());
            e.printStackTrace();
            showErrorMessage("Không thể mở form tạo cư dân: " + e.getMessage());
        }
    }
    
    /**
     * Fallback method để load form cư dân thủ công
     */
    private void loadCuDanFormManually() throws Exception {
        // Load FXML form tạo cư dân
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cu_dan.fxml"));
        Parent root = loader.load();
        
        // Lấy controller của form tạo cư dân
        Object controller = loader.getController();
        io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController cuDanController = null;
        if (controller instanceof io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController) {
            cuDanController = (io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController) controller;
            
            // Inject ApplicationContext nếu có
            if (applicationContext != null) {
                cuDanController.setApplicationContext(applicationContext);
                System.out.println("=== DEBUG: ApplicationContext injected manually ===");
            }
        }
        
        // Tạo stage mới cho form
        Stage stage = new Stage();
        stage.setTitle("Tạo cư dân mới");
        stage.setScene(new Scene(root));
        stage.setResizable(true);
        stage.setMinWidth(700);
        stage.setMinHeight(600);
        stage.initOwner(buttonTaoChuHo.getScene().getWindow());
        stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Hiển thị form
        stage.showAndWait();
        
        // Sau khi form đóng, lấy mã định danh và điền vào form căn hộ
        if (cuDanController != null) {
            String newCuDanMaDinhDanh = cuDanController.getLastCreatedCuDanMaDinhDanh();
            if (newCuDanMaDinhDanh != null && !newCuDanMaDinhDanh.trim().isEmpty()) {
                // Tự động điền mã định danh vào field
                if (textFieldMaDinhDanh != null) {
                    textFieldMaDinhDanh.setText(newCuDanMaDinhDanh);
                    System.out.println("=== DEBUG: Auto-filled resident ID (manual): " + newCuDanMaDinhDanh + " ===");
                    
                    // Đảm bảo checkbox "Đã bán" được check
                    if (choiceBoxThemChuSoHuu != null) {
                        choiceBoxThemChuSoHuu.setSelected(true);
                        handleThemChuSoHuuChange(null); // Trigger để hiện section chủ sở hữu
                    }
                }
            }
        }
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
                            // Refresh main apartments table
                            refreshMainApartmentsTable();
                            
                            showSuccessMessage("Xóa căn hộ thành công! Bảng căn hộ đã được cập nhật.");
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
        // Kiểm tra các trường bắt buộc của căn hộ
        if (isBlank(textFieldSoNha.getText())) {
            showErrorMessage("Vui lòng nhập số nhà");
            textFieldSoNha.requestFocus();
            return false;
        }

        if (isBlank(textFieldTang.getText())) {
            showErrorMessage("Vui lòng nhập tầng");
            textFieldTang.requestFocus();
            return false;
        }

        if (isBlank(textFieldToa.getText())) {
            showErrorMessage("Vui lòng nhập tòa");
            textFieldToa.requestFocus();
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

        // Kiểm tra các ComboBox bắt buộc
        if (comboBoxTinhTrangKiThuat.getValue() == null) {
            showErrorMessage("Vui lòng chọn tình trạng kỹ thuật");
            comboBoxTinhTrangKiThuat.requestFocus();
            return false;
        }

        // Nếu checkbox "Đã bán" được chọn, kiểm tra thông tin chủ sở hữu
        if (choiceBoxThemChuSoHuu.isSelected()) {
            // Kiểm tra mã định danh chủ hộ
            if (isBlank(textFieldMaDinhDanh.getText())) {
                showErrorMessage("Vui lòng nhập mã định danh chủ hộ");
                textFieldMaDinhDanh.requestFocus();
                return false;
            }
            
            // Kiểm tra comboBoxTinhTrangSuDung khi "Đã bán"
            if (comboBoxTinhTrangSuDung.getValue() == null) {
                showErrorMessage("Vui lòng chọn tình trạng sử dụng khi đã bán");
                comboBoxTinhTrangSuDung.requestFocus();
                return false;
            }
        }

        return true;
    }

    private CanHoDto createCanHoDto() {
        CanHoDto canHoDto = new CanHoDto();
        
        // Thiết lập thông tin căn hộ
        if (isEditMode) {
            canHoDto.setMaCanHo(originalMaCanHo);
        } else {
            canHoDto.setMaCanHo(generateMaCanHo());
        }
        
        canHoDto.setSoNha(textFieldSoNha.getText().trim());
        canHoDto.setTang(textFieldTang.getText().trim());
        canHoDto.setToaNha(textFieldToa.getText().trim());
        canHoDto.setDienTich(Double.parseDouble(textFieldDienTich.getText().trim()));
        canHoDto.setTrangThaiKiThuat(comboBoxTinhTrangKiThuat.getValue());
        
        // Thiết lập tình trạng sử dụng
        if (choiceBoxThemChuSoHuu.isSelected() && comboBoxTinhTrangSuDung.getValue() != null) {
            canHoDto.setTrangThaiSuDung(comboBoxTinhTrangSuDung.getValue());
        } else {
            canHoDto.setTrangThaiSuDung("Trống");
        }
        
        // Xử lý thông tin chủ hộ
        if (isEditMode) {
            handleOwnerInEditMode(canHoDto);
        } else {
            handleOwnerInCreateMode(canHoDto);
        }
        
        System.out.println("DEBUG: Created CanHoDto: " + canHoDto.getMaCanHo() + 
                         ", chuHo: " + (canHoDto.getChuHo() != null ? canHoDto.getChuHo().getHoVaTen() : "null"));
        
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
        // Trong create mode, chỉ xử lý khi checkbox "Đã bán" được chọn
        if (choiceBoxThemChuSoHuu.isSelected()) {
            // Sử dụng mã định danh có sẵn
            ChuHoDto chuHoDto = createChuHoDtoFromExisting();
            canHoDto.setChuHo(chuHoDto);
            System.out.println("DEBUG: Set existing owner with ID: " + chuHoDto.getMaDinhDanh());
        } else {
            // Không có chủ hộ
            canHoDto.setChuHo(null);
            System.out.println("DEBUG: No owner set for apartment");
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
        // Xóa thông tin căn hộ
        textFieldSoNha.clear();
        textFieldTang.clear();
        textFieldToa.clear();
        textFieldDienTich.clear();
        
        // Reset ComboBox
        comboBoxTinhTrangKiThuat.setValue("Tốt");
        comboBoxTinhTrangSuDung.setValue("Trống");
        comboBoxTrangThai.setValue("Cư trú");
        
        // Reset checkbox
        choiceBoxThemChuSoHuu.setSelected(false);
        
        // Ẩn các vùng nhập liệu
        vBoxChuSoHuu.setVisible(false);
        vBoxChuSoHuu.setManaged(false);
        vBoxThongTinCuDanMoi.setVisible(false);
        vBoxThongTinCuDanMoi.setManaged(false);
        
        // Xóa thông tin chủ sở hữu và cư dân mới
        clearOwnerFields();
        clearCuDanMoiFields();
        
        // Xóa thông báo lỗi
        clearErrorMessage();
        
        System.out.println("DEBUG: Form cleared");
        
        // Enable form sau khi clear
        hideLoadingState();
        
        // Reset combo boxes to show enabled state
        if (comboBoxTinhTrangSuDung != null) {
            comboBoxTinhTrangSuDung.setDisable(true); // Disable until "Đã bán" is checked
        }
        
        // Show checkboxes again in case they were hidden in edit mode
        if (choiceBoxThemChuSoHuu != null) {
            choiceBoxThemChuSoHuu.setVisible(true);
            choiceBoxThemChuSoHuu.setManaged(true);
        }
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
    
    /**
     * Refresh main apartments table in Home_list controller and switch to apartments tab with loading indicator
     */
    private void refreshMainApartmentsTable() {
        try {
            System.out.println("=== DEBUG: Starting refresh main apartments table ===");
            
            // Use Platform.runLater to ensure this runs on JavaFX thread
            javafx.application.Platform.runLater(() -> {
                try {
                    // Show loading state first
                    showLoadingStateForApartments(true);
                    System.out.println("=== DEBUG: Loading state shown for apartments ===");
                    
                    // Switch to apartments tab and refresh data
                    new Thread(() -> {
                        try {
                            Thread.sleep(800); // Longer delay to see loading effect
                            
                            javafx.application.Platform.runLater(() -> {
                                try {
                                    // Try to find Home_list controller from scene graph
                                    refreshApartmentsTableDirectly();
                                    System.out.println("=== DEBUG: Apartments data refreshed ===");
                                    
                                    // Wait a bit more then hide loading
                                    Thread.sleep(200);
                                    javafx.application.Platform.runLater(() -> {
                                        showLoadingStateForApartments(false);
                                        System.out.println("=== DEBUG: Loading state hidden for apartments ===");
                                    });
                                    
                                } catch (Exception e) {
                                    javafx.application.Platform.runLater(() -> showLoadingStateForApartments(false));
                                    System.err.println("ERROR: Failed to refresh apartments data: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            javafx.application.Platform.runLater(() -> showLoadingStateForApartments(false));
                        }
                    }).start();
                    
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to refresh main apartments table: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("ERROR: Exception in refreshMainApartmentsTable: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Show/hide loading state on apartments table
     */
    private void showLoadingStateForApartments(boolean isLoading) {
        try {
            System.out.println("=== DEBUG: Setting apartments loading state: " + isLoading + " ===");
            
            // Find the main stage and Home_list controller
            javafx.stage.Stage mainStage = (javafx.stage.Stage) javafx.stage.Stage.getWindows().stream()
                .filter(window -> window instanceof javafx.stage.Stage)
                .filter(stage -> "Quản Lý Chung Cư Blue Moon".equals(((javafx.stage.Stage)stage).getTitle()))
                .findFirst().orElse(null);
                
            if (mainStage != null && mainStage.getScene() != null && mainStage.getScene().getRoot() != null) {
                // Look for elements in the scene graph
                javafx.scene.control.TableView<?> apartmentTable = (javafx.scene.control.TableView<?>) 
                    findNodeByFxId(mainStage.getScene().getRoot(), "tabelViewCanHo");
                javafx.scene.control.Label resultLabel = (javafx.scene.control.Label) 
                    findNodeByFxId(mainStage.getScene().getRoot(), "labelKetQuaHienThiCanHo");
                javafx.scene.control.Label displayLabel = (javafx.scene.control.Label) 
                    findNodeByFxId(mainStage.getScene().getRoot(), "labelHienThiKetQuaCanHo");
                
                if (isLoading) {
                    System.out.println("=== DEBUG: Showing loading state for apartments ===");
                    if (apartmentTable != null) {
                        apartmentTable.setDisable(true);
                        apartmentTable.setStyle("-fx-opacity: 0.5; -fx-background-color: #f0f0f0;");
                        System.out.println("=== DEBUG: Apartment table disabled and styled ===");
                    }
                    if (resultLabel != null) {
                        resultLabel.setText("🔄 Đang tải dữ liệu căn hộ...");
                        resultLabel.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold; -fx-font-size: 14px;");
                        System.out.println("=== DEBUG: Apartment result label updated ===");
                    }
                    if (displayLabel != null) {
                        displayLabel.setText("⏳ Đang xử lý...");
                        displayLabel.setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold; -fx-font-size: 14px;");
                        System.out.println("=== DEBUG: Apartment display label updated ===");
                    }
                } else {
                    System.out.println("=== DEBUG: Hiding loading state for apartments ===");
                    if (apartmentTable != null) {
                        apartmentTable.setDisable(false);
                        apartmentTable.setStyle("-fx-opacity: 1.0; -fx-background-color: white;");
                        System.out.println("=== DEBUG: Apartment table enabled and restored ===");
                    }
                    if (resultLabel != null) {
                        resultLabel.setStyle("-fx-text-fill: black; -fx-font-weight: normal; -fx-font-size: 14px;");
                        System.out.println("=== DEBUG: Apartment result label style restored ===");
                    }
                    if (displayLabel != null) {
                        displayLabel.setStyle("-fx-text-fill: black; -fx-font-weight: normal; -fx-font-size: 14px;");
                        System.out.println("=== DEBUG: Apartment display label style restored ===");
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: Failed to show/hide apartments loading state: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to find a node by fx:id
     */
    private javafx.scene.Node findNodeByFxId(javafx.scene.Node parent, String fxId) {
        if (fxId.equals(parent.getId())) {
            return parent;
        }
        if (parent instanceof javafx.scene.Parent) {
            for (javafx.scene.Node child : ((javafx.scene.Parent) parent).getChildrenUnmodifiable()) {
                javafx.scene.Node result = findNodeByFxId(child, fxId);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
    
    /**
     * Refresh apartments table by finding and calling the appropriate method
     */
    private void refreshApartmentsTableDirectly() {
        try {
            // Add delay for loading effect
            new Thread(() -> {
                try {
                    Thread.sleep(300); // 300ms delay for loading effect
                    
                    javafx.application.Platform.runLater(() -> {
                        try {
                            // Find all windows and look for Home_list controller
                            for (javafx.stage.Window window : javafx.stage.Window.getWindows()) {
                                if (window instanceof javafx.stage.Stage) {
                                    javafx.stage.Stage stage = (javafx.stage.Stage) window;
                                    javafx.scene.Scene scene = stage.getScene();
                                    if (scene != null && scene.getRoot() != null) {
                                        // Try to find the Home_list controller through scene graph
                                        findAndRefreshHomeListController(scene.getRoot());
                                    }
                                }
                            }
                            System.out.println("=== DEBUG: Apartments table refresh attempted ===");
                        } catch (Exception e) {
                            System.err.println("ERROR: Failed to refresh apartments data: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        } catch (Exception e) {
            System.err.println("ERROR: Exception in refreshApartmentsTableDirectly: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Find and refresh Home_list controller
     */
    private void findAndRefreshHomeListController(javafx.scene.Node node) {
        try {
            // Check if the node has a controller property
            Object controller = node.getProperties().get("controller");
            if (controller instanceof Home_list) {
                Home_list homeListController = (Home_list) controller;
                
                // Switch to apartments tab
                java.lang.reflect.Method goToCanHoMethod = homeListController.getClass().getDeclaredMethod("goToCanHo", javafx.event.ActionEvent.class);
                goToCanHoMethod.setAccessible(true);
                goToCanHoMethod.invoke(homeListController, (javafx.event.ActionEvent) null);
                
                // Refresh apartments data
                java.lang.reflect.Method loadDataMethod = homeListController.getClass().getDeclaredMethod("loadData");
                loadDataMethod.setAccessible(true);
                loadDataMethod.invoke(homeListController);
                
                System.out.println("=== DEBUG: Successfully refreshed apartments table ===");
                return;
            }
            
            // Recursively search in children if it's a Parent node
            if (node instanceof javafx.scene.Parent) {
                javafx.scene.Parent parent = (javafx.scene.Parent) node;
                for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                    findAndRefreshHomeListController(child);
                }
            }
        } catch (Exception e) {
            // Silently continue searching - this is expected for most nodes
        }
    }
}
