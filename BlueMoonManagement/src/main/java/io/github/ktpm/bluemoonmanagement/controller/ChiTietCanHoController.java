package io.github.ktpm.bluemoonmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanTrongCanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.dto.phuongTien.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.service.cache.CacheDataService;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Controller cho trang chi tiết căn hộ
 */
@Component
public class ChiTietCanHoController implements Initializable {

    // Static list to track open windows for refresh functionality
    private static final List<ChiTietCanHoController> openWindows = new ArrayList<>();

    // Header buttons - Tab navigation
    @FXML private Button buttonThongTin;
    @FXML private Button buttonCuDan;
    @FXML private Button buttonPhuongTien;
    @FXML private Button buttonThuPhi;
    @FXML private Button button_close_up;

    // Tab content panels
    @FXML private AnchorPane anchorPaneThongTin;
    @FXML private AnchorPane anchorPaneCuDan;
    @FXML private AnchorPane anchorPanePhuongTien;
    @FXML private AnchorPane anchorPaneThuPhi;

    // Thông tin căn hộ labels
    @FXML private Label labelMaCanHo;
    @FXML private Label labelSoNha;
    @FXML private Label labelTang;
    @FXML private Label labelToa;
    @FXML private Label labelDienTich;
    @FXML private Label labelTrangThaiCanHo;
    @FXML private Label labelTinhTrangSuDung;
    @FXML private Label labelTinhTrangKiThuat;

    // Thông tin chủ sở hữu labels
    @FXML private VBox vBoxChuSoHuu;
    @FXML private Label labelMaDinhDanh;
    @FXML private Label labelHoVaTen;
    @FXML private Label labelNgaySinh;
    @FXML private Label labelGioiTinh;
    @FXML private Label labelTrangThaiChuSoHuu;
    @FXML private Label labelSoDienThoai;
    @FXML private Label labelEmail;

    // Cư dân tab
    @FXML private TableView<CuDanTrongCanHoDto> tableViewCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnMaCanHoCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnMaDinhDanhCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnHoVaTenCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnGioiTinhCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnNgaySinhCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnSoDienThoaiCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnEmailCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnQuanHeChuHo;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnTrangThaiCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, Void> tableColumnThaoTacCuDan;
    @FXML private ComboBox<String> comboBoxSoKetQuaCuDan;
    @FXML private TextField textFieldTimKiemCuDan;
    @FXML private TextField textFieldMaDinhDanh;
    @FXML private Button buttonTimKiemCuDan;
    @FXML private Label labelHienThiKetQuaCuDan;

    // Phương tiện tab
    @FXML private TableView<PhuongTienDto> tableViewPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnMaPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnBienSoXe;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnLoaiPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnNgayDangKi;
    @FXML private TableColumn<PhuongTienDto, Void> tableColumnThaoTacPhuongTien;
    @FXML private TextField textFieldMaSoXe;
    @FXML private ComboBox<String> comboBoxLoaiPhuongTien;
    @FXML private Button buttonTimKiemPhuongTien;
    @FXML private ComboBox<String> comboBoxSoKetQuaPhuongTien;
    @FXML private Label labelHienThiKetQuaPhuongTien;
    @FXML private Button buttonThemPhuongTien;

    // Thu phí tab
    @FXML private TableView<HoaDonDto> tableViewThuPhi;
    @FXML private TableColumn<HoaDonDto, String> tableColumnMaHoaDon;
    @FXML private TableColumn<HoaDonDto, String> tableColumnTenKhoanThu;
    @FXML private TableColumn<HoaDonDto, Integer> tableColumnSoTien;
    @FXML private TableColumn<HoaDonDto, String> tableColumnHanThu;
    @FXML private TableColumn<HoaDonDto, String> tableColumnThaoTacThuPhi;
    @FXML private TextField textFieldTenKhoanThu;
    @FXML private ComboBox<String> comboBoxLoaiKhoanThu;
    @FXML private Button buttonTimKiemThuPhi;
    @FXML private Label labelTongSoTien;
    @FXML private Button buttonThuToanBo;
    @FXML private Button buttonXemLichSu;
    @FXML private CheckBox checkBoxKhongTinhBatBuoc;
    
    // Edit button in thông tin tab 
    @FXML private Button buttonChinhSua;

    // Data
    private CanHoChiTietDto currentCanHo;
    private ObservableList<CuDanTrongCanHoDto> cuDanList;
    private ObservableList<PhuongTienDto> phuongTienList;
    private ObservableList<HoaDonDto> hoaDonList;

    // Service dependency injection
    @Autowired
    private CanHoService canHoService;

    @Autowired
    private PhuongTienService phuongTienService;

    @Autowired
    private CacheDataService cacheDataService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Controller initialized
        
        // Add this instance to tracking list
        openWindows.add(this);
        
        // Initialize lists
        cuDanList = FXCollections.observableArrayList();
        phuongTienList = FXCollections.observableArrayList();
        hoaDonList = FXCollections.observableArrayList();
        
        // Đảm bảo services được khởi tạo nếu có ApplicationContext
        if (applicationContext != null) {
            ensureServicesAvailable();
        }
        
        // Debug cache status and table components
        System.out.println("=== DEBUG: ChiTietCanHoController initialized ===");
        System.out.println("  - cacheDataService: " + (cacheDataService != null ? "Available" : "NULL"));
        if (cacheDataService != null) {
            System.out.println("  - cache loaded: " + cacheDataService.isCacheLoaded());
        }
        
        // Debug table components injection
        System.out.println("=== DEBUG: Checking table components injection ===");
        System.out.println("tableViewCuDan: " + (tableViewCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnMaCanHoCuDan: " + (tableColumnMaCanHoCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnMaDinhDanhCuDan: " + (tableColumnMaDinhDanhCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnHoVaTenCuDan: " + (tableColumnHoVaTenCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnGioiTinhCuDan: " + (tableColumnGioiTinhCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnNgaySinhCuDan: " + (tableColumnNgaySinhCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnSoDienThoaiCuDan: " + (tableColumnSoDienThoaiCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnEmailCuDan: " + (tableColumnEmailCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnQuanHeChuHo: " + (tableColumnQuanHeChuHo != null ? "OK" : "NULL"));
        System.out.println("tableColumnTrangThaiCuDan: " + (tableColumnTrangThaiCuDan != null ? "OK" : "NULL"));
        System.out.println("tableColumnThaoTacCuDan: " + (tableColumnThaoTacCuDan != null ? "OK" : "NULL"));
        System.out.println("=== END DEBUG: Components injection ===");
        
        // Setup components
        setupTabNavigation();
        setupTables();
        setupComboBoxes();
        
        // Setup permissions for buttons
        setupButtonPermissions();
        
        // Default to first tab
        showThongTinTab();
    }

    /**
     * Thiết lập navigation giữa các tab
     */
    private void setupTabNavigation() {
        buttonThongTin.setOnAction(e -> showThongTinTab());
        buttonCuDan.setOnAction(e -> showCuDanTab());
        buttonPhuongTien.setOnAction(e -> showPhuongTienTab());
        buttonThuPhi.setOnAction(e -> showThuPhiTab());
        button_close_up.setOnAction(e -> handleClose());
    }

    /**
     * Thiết lập các table
     */
    private void setupTables() {
        System.out.println("=== DEBUG: Setting up tables ===");
        
        // Setup table cư dân
        if (tableColumnMaCanHoCuDan != null) {
            System.out.println("tableColumnMaCanHoCuDan found");
            tableColumnMaCanHoCuDan.setCellValueFactory(cellData -> {
                // Lấy mã căn hộ từ currentCanHo cho mỗi dòng
                String maCanHo = currentCanHo != null ? currentCanHo.getMaCanHo() : "";
                return new javafx.beans.property.SimpleStringProperty(maCanHo);
            });
        } else {
            System.out.println("WARNING: tableColumnMaCanHoCuDan is NULL!");
        }

        if (tableColumnMaDinhDanhCuDan != null) {
            System.out.println("tableColumnMaDinhDanhCuDan found");
            tableColumnMaDinhDanhCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMaDinhDanh()));
        } else {
            System.out.println("WARNING: tableColumnMaDinhDanhCuDan is NULL!");
        }
        
        if (tableColumnHoVaTenCuDan != null) {
            System.out.println("tableColumnHoVaTenCuDan found");
            tableColumnHoVaTenCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoVaTen()));
        } else {
            System.out.println("WARNING: tableColumnHoVaTenCuDan is NULL!");
        }
        
        if (tableColumnGioiTinhCuDan != null) {
            System.out.println("tableColumnGioiTinhCuDan found");
            tableColumnGioiTinhCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getGioiTinh() != null ? 
                    cellData.getValue().getGioiTinh() : ""));
        } else {
            System.out.println("WARNING: tableColumnGioiTinhCuDan is NULL!");
        }
        
        if (tableColumnSoDienThoaiCuDan != null) {
            System.out.println("tableColumnSoDienThoaiCuDan found");
            tableColumnSoDienThoaiCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getSoDienThoai() != null ? 
                    cellData.getValue().getSoDienThoai() : ""));
        } else {
            System.out.println("WARNING: tableColumnSoDienThoaiCuDan is NULL!");
        }
        
        if (tableColumnEmailCuDan != null) {
            System.out.println("tableColumnEmailCuDan found");
            tableColumnEmailCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getEmail() != null ? 
                    cellData.getValue().getEmail() : ""));
        } else {
            System.out.println("WARNING: tableColumnEmailCuDan is NULL!");
        }
        
        if (tableColumnQuanHeChuHo != null) {
            System.out.println("tableColumnQuanHeChuHo found");
            tableColumnQuanHeChuHo.setCellValueFactory(cellData -> {
                // Determine relationship based on whether this person is the apartment owner
                String relationship = "Cư dân";
                if (currentCanHo != null && currentCanHo.getChuHo() != null) {
                    if (cellData.getValue().getMaDinhDanh().equals(currentCanHo.getChuHo().getMaDinhDanh())) {
                        relationship = "Chủ hộ";
                    }
                }
                return new javafx.beans.property.SimpleStringProperty(relationship);
            });
        } else {
            System.out.println("WARNING: tableColumnQuanHeChuHo is NULL!");
        }
        
        if (tableColumnNgaySinhCuDan != null) {
            System.out.println("tableColumnNgaySinhCuDan found");
            tableColumnNgaySinhCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getNgaySinh() != null ? 
                    cellData.getValue().getNgaySinh().toString() : ""));
        } else {
            System.out.println("WARNING: tableColumnNgaySinhCuDan is NULL!");
        }
        
        if (tableColumnTrangThaiCuDan != null) {
            System.out.println("tableColumnTrangThaiCuDan found");
            tableColumnTrangThaiCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getTrangThaiCuTru() != null ? 
                    cellData.getValue().getTrangThaiCuTru() : ""));
        } else {
            System.out.println("WARNING: tableColumnTrangThaiCuDan is NULL!");
        }

        // Thêm cột thao tác cho cư dân
        if (tableColumnThaoTacCuDan != null) {
            System.out.println("tableColumnThaoTacCuDan found");
            setupCuDanActions();
        } else {
            System.out.println("WARNING: tableColumnThaoTacCuDan is NULL!");
        }
        
        System.out.println("=== DEBUG: Finished setting up cư dân table columns ===");

        // Setup table phương tiện
        if (tableColumnMaPhuongTien != null) {
            tableColumnMaPhuongTien.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getSoThuTu())));
        }
        if (tableColumnBienSoXe != null) {
            tableColumnBienSoXe.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBienSo()));
        }
        if (tableColumnLoaiPhuongTien != null) {
            tableColumnLoaiPhuongTien.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLoaiPhuongTien()));
        }
        if (tableColumnNgayDangKi != null) {
            tableColumnNgayDangKi.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getNgayDangKy() != null ? 
                    cellData.getValue().getNgayDangKy().toString() : ""));
        }
        
        // Thiết lập cell factory cho cột thao tác
        tableColumnThaoTacPhuongTien.setCellFactory(param -> new javafx.scene.control.TableCell<PhuongTienDto, Void>() {
            private final javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("Xóa");

            {
                deleteButton.getStyleClass().addAll("action-button", "button-red");
                deleteButton.setOnAction(event -> {
                    PhuongTienDto phuongTien = getTableView().getItems().get(getIndex());
                    handleDeletePhuongTien(phuongTien);
                });
                
                // Disable nút xóa cho tất cả trừ Tổ phó (chỉ Tổ phó được phép xóa phương tiện)
                try {
                    String userRole = getCurrentUserRole();
                    if (!"Tổ phó".equals(userRole)) {
                        deleteButton.setDisable(true);
                        deleteButton.setOpacity(0.5);
                    }
                } catch (Exception e) {
                    System.err.println("ERROR: Cannot setup delete button permission: " + e.getMessage());
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });

        // Setup table thu phí
        if (tableColumnMaHoaDon != null) {
            tableColumnMaHoaDon.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getMaHoaDon())));
        }
        if (tableColumnTenKhoanThu != null) {
            tableColumnTenKhoanThu.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTenKhoanThu()));
        }
        if (tableColumnSoTien != null) {
            tableColumnSoTien.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getSoTien()));
        }
        if (tableColumnHanThu != null) {
            tableColumnHanThu.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getNgayNop() != null ? 
                    cellData.getValue().getNgayNop().toString() : ""));
        }

        // Setup search functionality for Cư dân tab
        if (textFieldTimKiemCuDan != null) {
            textFieldTimKiemCuDan.textProperty().addListener((observable, oldValue, newValue) -> {
                handleTimKiemCuDan();
            });
        }
        
        if (textFieldMaDinhDanh != null) {
            textFieldMaDinhDanh.textProperty().addListener((observable, oldValue, newValue) -> {
                handleTimKiemCuDan();
            });
        }
    }

    /**
     * Thiết lập cột thao tác cho bảng cư dân (Sửa, Xóa)
     */
    private void setupCuDanActions() {
        tableColumnThaoTacCuDan.setCellFactory(param -> new javafx.scene.control.TableCell<CuDanTrongCanHoDto, Void>() {
            private final javafx.scene.control.Button editButton = new javafx.scene.control.Button("Sửa");
            private final javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("Xóa");
            private final javafx.scene.layout.HBox pane = new javafx.scene.layout.HBox(editButton, deleteButton);

            {
                pane.setSpacing(10);
                editButton.getStyleClass().addAll("action-button", "button-yellow");
                deleteButton.getStyleClass().addAll("action-button", "button-red");

                editButton.setOnAction(event -> {
                    CuDanTrongCanHoDto cuDan = getTableView().getItems().get(getIndex());
                    handleEditCuDan(cuDan);
                });

                deleteButton.setOnAction(event -> {
                    CuDanTrongCanHoDto cuDan = getTableView().getItems().get(getIndex());
                    handleDeleteCuDan(cuDan);
                });

                // Disable nút cho tất cả trừ Tổ phó (chỉ Tổ phó được phép sửa/xóa cư dân)
                try {
                    String userRole = getCurrentUserRole();
                    if (!"Tổ phó".equals(userRole)) {
                        editButton.setDisable(true);
                        deleteButton.setDisable(true);
                        editButton.setOpacity(0.5);
                        deleteButton.setOpacity(0.5);
                    }
                } catch (Exception e) {
                    System.err.println("ERROR: Cannot setup cu dan action button permissions: " + e.getMessage());
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });
    }

    /**
     * Xử lý sự kiện chỉnh sửa cư dân
     */
    private void handleEditCuDan(CuDanTrongCanHoDto cuDan) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/them_cu_dan.fxml") // Tái sử dụng form thêm cư dân
            );
            javafx.scene.Parent root = loader.load();
            
            ThemCuDanController controller = loader.getController();
            
            // QUAN TRỌNG: Inject application context và mã căn hộ
            if (applicationContext != null) {
                controller.setApplicationContext(applicationContext);
            }
            // Tự động gán mã căn hộ hiện tại
            controller.setupEditModeFromApartmentDetail(cuDan, currentCanHo.getMaCanHo());
            
            Stage stage = new Stage();
            stage.setTitle("Chỉnh sửa cư dân - " + cuDan.getHoVaTen());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(button_close_up.getScene().getWindow());
            
            stage.showAndWait();
            
            // Refresh lại dữ liệu sau khi đóng form
            refreshData();

        } catch (Exception e) {
            showError("Lỗi", "Không thể mở form chỉnh sửa cư dân: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện xóa cư dân (xóa mềm)
     */
    private void handleDeleteCuDan(CuDanTrongCanHoDto cuDan) {
        boolean confirm = ThongBaoController.showConfirmation("Xác nhận xóa", "Bạn có chắc chắn muốn xóa cư dân " + cuDan.getHoVaTen() + "?\nHành động này sẽ ghi nhận ngày chuyển đi của cư dân.");

        if (confirm) {
            try {
                // Lấy service từ application context nếu chưa có
                if (applicationContext != null) {
                    io.github.ktpm.bluemoonmanagement.service.cuDan.CuDanService cuDanService = applicationContext.getBean(io.github.ktpm.bluemoonmanagement.service.cuDan.CuDanService.class);
                    
                    io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto cudanDto = new io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto();
                    cudanDto.setMaDinhDanh(cuDan.getMaDinhDanh());

                    ResponseDto response = cuDanService.deleteCuDan(cudanDto);

                    if (response.isSuccess()) {
                        showSuccess("Thành công", response.getMessage());
                        refreshData();
                    } else {
                        showError("Lỗi", response.getMessage());
                    }
                } else {
                    showError("Lỗi hệ thống", "ApplicationContext không khả dụng.");
                }
            } catch (Exception e) {
                showError("Lỗi", "Không thể xóa cư dân: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Thiết lập các ComboBox
     */
    private void setupComboBoxes() {
        // Setup ComboBox số kết quả cư dân
        if (comboBoxSoKetQuaCuDan != null) {
            comboBoxSoKetQuaCuDan.setItems(FXCollections.observableArrayList("10", "25", "50", "100"));
            comboBoxSoKetQuaCuDan.setValue("10");
        }

        // Setup ComboBox loại phương tiện - will be populated from actual data after loading
        if (comboBoxLoaiPhuongTien != null) {
            comboBoxLoaiPhuongTien.setItems(FXCollections.observableArrayList("Tất cả"));
            comboBoxLoaiPhuongTien.setValue("Tất cả");
        }

        // Setup ComboBox số kết quả phương tiện
        if (comboBoxSoKetQuaPhuongTien != null) {
            comboBoxSoKetQuaPhuongTien.setItems(FXCollections.observableArrayList("10", "25", "50", "100"));
            comboBoxSoKetQuaPhuongTien.setValue("10");
        }

        // Setup ComboBox loại khoản thu - will be populated from actual data after loading
        if (comboBoxLoaiKhoanThu != null) {
            comboBoxLoaiKhoanThu.setItems(FXCollections.observableArrayList("Tất cả"));
            comboBoxLoaiKhoanThu.setValue("Tất cả");
        }
    }

    /**
     * Thiết lập quyền cho các nút dựa trên vai trò người dùng
     */
    private void setupButtonPermissions() {
        try {
            System.out.println("=== DEBUG: setupButtonPermissions() called ===");
            
            // Debug current user
            if (Session.getCurrentUser() == null) {
                System.out.println("DEBUG: getCurrentUser() returns NULL!");
            } else {
                System.out.println("DEBUG: getCurrentUser() = " + Session.getCurrentUser());
                System.out.println("DEBUG: Email: " + Session.getCurrentUser().getEmail());
                System.out.println("DEBUG: HoTen: " + Session.getCurrentUser().getHoTen());
                System.out.println("DEBUG: VaiTro: '" + Session.getCurrentUser().getVaiTro() + "'");
            }
            
            String userRole = getCurrentUserRole();
            System.out.println("DEBUG: getCurrentUserRole() returns: '" + userRole + "'");
            
            boolean isToTruong = "Tổ trưởng".equals(userRole);
            boolean isKeToan = "Kế toán".equals(userRole);
            boolean isToPho = "Tổ phó".equals(userRole);
            boolean shouldDisableButtons = isToTruong || isKeToan || isToPho; // Disable cho tất cả vai trò có logic riêng
            
            System.out.println("DEBUG: isToTruong = " + isToTruong);
            System.out.println("DEBUG: isKeToan = " + isKeToan);
            System.out.println("DEBUG: isToPho = " + isToPho);
            System.out.println("DEBUG: shouldDisableButtons = " + shouldDisableButtons);
            
            if (shouldDisableButtons) {
                // Disable/làm mờ các nút tùy theo vai trò
                disableButtonsForRestrictedRoles(userRole);
            } else {
                System.out.println("DEBUG: ❌ NOT disabling buttons - User role: '" + userRole + "' - all buttons enabled");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Cannot setup button permissions: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lấy vai trò người dùng hiện tại
     */
    private String getCurrentUserRole() {
        try {
            if (Session.getCurrentUser() == null) {
                return null;
            }
            return Session.getCurrentUser().getVaiTro();
        } catch (Exception e) {
            System.err.println("ERROR: Cannot get current user role: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Disable các nút tùy theo vai trò:
     * - Tổ trưởng: Disable tất cả (chỉ xem)
     * - Kế toán: Disable căn hộ/cư dân/phương tiện, được phép khoản thu
     * - Tổ phó: Disable khoản thu, được phép căn hộ/cư dân/phương tiện
     */
    private void disableButtonsForRestrictedRoles(String userRole) {
        System.out.println("=== DEBUG: disableButtonsForRestrictedRoles() called for role: " + userRole + " ===");
        
        boolean isToTruong = "Tổ trưởng".equals(userRole);
        boolean isKeToan = "Kế toán".equals(userRole);
        boolean isToPho = "Tổ phó".equals(userRole);
        
        // Phần thông tin căn hộ - chỉ Tổ phó và Kế toán bị disable chỉnh sửa
        if (buttonChinhSua != null) {
            boolean shouldDisableEdit = isToTruong || isKeToan;
            buttonChinhSua.setDisable(shouldDisableEdit);
            if (shouldDisableEdit) {
                buttonChinhSua.setOpacity(0.5);
                System.out.println("DEBUG: ✅ Disabled buttonChinhSua for " + userRole);
            }
        } else {
            System.out.println("DEBUG: ❌ buttonChinhSua is NULL!");
        }
        
        // Phần cư dân - chỉ Tổ phó được phép (nút trong table sẽ được xử lý riêng)
        
        // Phần phương tiện - chỉ Tổ phó được phép
        if (buttonThemPhuongTien != null) {
            boolean shouldDisableVehicle = isToTruong || isKeToan;
            buttonThemPhuongTien.setDisable(shouldDisableVehicle);
            if (shouldDisableVehicle) {
                buttonThemPhuongTien.setOpacity(0.5);
                System.out.println("DEBUG: ✅ Disabled buttonThemPhuongTien for " + userRole);
            }
        } else {
            System.out.println("DEBUG: ❌ buttonThemPhuongTien is NULL!");
        }
        
        // Phần khoản thu - chỉ Kế toán được phép
        if (!isKeToan) {
            // Disable nút thu toàn bộ và xem lịch sử cho tất cả trừ Kế toán
            if (buttonThuToanBo != null) {
                buttonThuToanBo.setDisable(true);
                buttonThuToanBo.setOpacity(0.5);
                System.out.println("DEBUG: ✅ Disabled buttonThuToanBo for " + userRole);
            } else {
                System.out.println("DEBUG: ❌ buttonThuToanBo is NULL!");
            }
            
            if (buttonXemLichSu != null) {
                buttonXemLichSu.setDisable(true);
                buttonXemLichSu.setOpacity(0.5);
                System.out.println("DEBUG: ✅ Disabled buttonXemLichSu for " + userRole);
            } else {
                System.out.println("DEBUG: ❌ buttonXemLichSu is NULL!");
            }
        } else {
            System.out.println("DEBUG: ✅ Chỉ Kế toán được phép thao tác với khoản thu - buttons enabled");
        }
        
        // Note: Nút tìm kiếm (buttonTimKiemCuDan, buttonTimKiemPhuongTien, buttonTimKiemThuPhi) 
        // sẽ KHÔNG bị disable theo yêu cầu
        
        System.out.println("DEBUG: ✅ COMPLETED disabling action buttons for " + userRole);
    }

    /**
     * Cập nhật ComboBox với dữ liệu thực từ database
     */
    private void updateComboBoxesWithRealData() {
        // Update vehicle types from loaded data
        if (comboBoxLoaiPhuongTien != null && phuongTienList != null) {
            ObservableList<String> vehicleTypes = FXCollections.observableArrayList("Tất cả");
            phuongTienList.stream()
                .map(PhuongTienDto::getLoaiPhuongTien)
                .filter(type -> type != null && !type.trim().isEmpty())
                .distinct()
                .forEach(vehicleTypes::add);
            comboBoxLoaiPhuongTien.setItems(vehicleTypes);
            if (!vehicleTypes.contains(comboBoxLoaiPhuongTien.getValue())) {
                comboBoxLoaiPhuongTien.setValue("Tất cả");
            }
        }

        // Update fee types from loaded data  
        if (comboBoxLoaiKhoanThu != null && hoaDonList != null) {
            ObservableList<String> feeTypes = FXCollections.observableArrayList("Tất cả");
            hoaDonList.stream()
                .map(HoaDonDto::getTenKhoanThu)
                .filter(type -> type != null && !type.trim().isEmpty())
                .distinct()
                .forEach(feeTypes::add);
            comboBoxLoaiKhoanThu.setItems(feeTypes);
            if (!feeTypes.contains(comboBoxLoaiKhoanThu.getValue())) {
                comboBoxLoaiKhoanThu.setValue("Tất cả");
            }
        }
    }

    /**
     * Load dữ liệu từ cache hoặc service tùy theo mode
     * @param maCanHo mã căn hộ cần load
     * @param forceFromService true để bắt buộc load từ service, false để ưu tiên cache
     */
    private void loadData(String maCanHo, boolean forceFromService) {
        try {
            if (maCanHo == null || maCanHo.trim().isEmpty()) {
                showError("Lỗi dữ liệu", "Mã căn hộ không hợp lệ.");
                return;
            }

            CanHoChiTietDto chiTiet = null;
            
            System.out.println("=== DEBUG: loadData called ===");
            System.out.println("  - maCanHo: " + maCanHo);
            System.out.println("  - forceFromService: " + forceFromService);
            System.out.println("  - cacheDataService != null: " + (cacheDataService != null));
            if (cacheDataService != null) {
                System.out.println("  - cacheDataService.isCacheLoaded(): " + cacheDataService.isCacheLoaded());
            }
            
            if (!forceFromService && cacheDataService != null && cacheDataService.isCacheLoaded()) {
                System.out.println("=== DEBUG: Attempting to load data from cache for: " + maCanHo + " ===");
                chiTiet = cacheDataService.getCanHoChiTietFromCache(maCanHo);
                if (chiTiet != null) {
                    System.out.println("✅ SUCCESS: Found data in cache for: " + maCanHo);
                } else {
                    System.out.println("❌ FAIL: Cache returned null for: " + maCanHo);
                }
            } else {
                System.out.println("=== DEBUG: Skipping cache, reasons: ===");
                if (forceFromService) System.out.println("  - forceFromService = true");
                if (cacheDataService == null) System.out.println("  - cacheDataService is null");
                if (cacheDataService != null && !cacheDataService.isCacheLoaded()) System.out.println("  - cache not loaded yet");
            }
            
            // Fallback to service if cache fails or forced
            if (chiTiet == null) {
                System.out.println("=== DEBUG: Loading data from service for: " + maCanHo + " ===");
                chiTiet = loadDataFromService(maCanHo);
                if (chiTiet != null) {
                    System.out.println("✅ SUCCESS: Loaded from service for: " + maCanHo);
                } else {
                    System.out.println("❌ FAIL: Service also returned null for: " + maCanHo);
                }
            }
            
            if (chiTiet != null) {
                System.out.println("=== DEBUG: Data loaded from service ===");
                System.out.println("Apartment: " + chiTiet.getMaCanHo());
                System.out.println("Technical status: " + chiTiet.getTrangThaiKiThuat());
                System.out.println("Usage status: " + chiTiet.getTrangThaiSuDung());
                System.out.println("Owner: " + (chiTiet.getChuHo() != null ? 
                    chiTiet.getChuHo().getHoVaTen() + " (" + chiTiet.getChuHo().getMaDinhDanh() + ")" : "NULL"));
                
                currentCanHo = chiTiet;
                
                // Load danh sách từ data và lọc chỉ hiển thị cư dân chưa bị xóa (chưa có ngày chuyển đi)
                cuDanList.clear();
                System.out.println("=== DEBUG: Processing residents data ===");
                System.out.println("CanHoChiTietDto.getCuDanList() == null: " + (chiTiet.getCuDanList() == null));
                
                if (chiTiet.getCuDanList() != null) {
                    System.out.println("CanHoChiTietDto.getCuDanList().size(): " + chiTiet.getCuDanList().size());
                    
                    // Debug: Print all residents before filtering
                    System.out.println("=== DEBUG: All residents from DTO ===");
                    for (CuDanTrongCanHoDto cuDan : chiTiet.getCuDanList()) {
                        System.out.println("- DTO Resident: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ") - " 
                                         + cuDan.getTrangThaiCuTru() + " - NgayChuyenDi: " + cuDan.getNgayChuyenDi());
                    }
                    System.out.println("=== END DTO residents ===");
                    
                    // Chỉ hiển thị cư dân có trạng thái "Cư trú"
                    chiTiet.getCuDanList().stream()
                        .filter(cuDan -> "Cư trú".equals(cuDan.getTrangThaiCuTru()))
                        .forEach(cuDanList::add);
                    
                    System.out.println("=== DEBUG: UI loaded " + chiTiet.getCuDanList().size() + " total residents, " 
                                     + cuDanList.size() + " displayed residents (chỉ Cư trú) ===");
                    for (CuDanTrongCanHoDto cuDan : cuDanList) {
                        System.out.println("- UI Displayed Resident: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ") - " + cuDan.getTrangThaiCuTru());
                    }
                } else {
                    System.out.println("=== DEBUG: No residents in DTO - chiTiet.getCuDanList() is NULL ===");
                }
                
                phuongTienList.clear();
                if (chiTiet.getPhuongTienList() != null) {
                    phuongTienList.addAll(chiTiet.getPhuongTienList());
                }
                
                hoaDonList.clear();
                if (chiTiet.getHoaDonList() != null) {
                    hoaDonList.addAll(chiTiet.getHoaDonList());
                }
                
                // Cập nhật UI
                updateThongTinCanHo();
                setTableData();
                updateTongSoTien();
                
                // Cập nhật ComboBox với dữ liệu thực
                updateComboBoxesWithRealData();
                
                System.out.println("=== DEBUG: UI update completed ===");
            } else {
                showError("Không tìm thấy dữ liệu", "Không tìm thấy thông tin chi tiết cho căn hộ: " + maCanHo);
                clearAllData();
            }
        } catch (Exception e) {
            showError("Lỗi kết nối", "Không thể tải dữ liệu: " + e.getMessage());
            clearAllData();
        }
    }
    
    /**
     * Load dữ liệu từ service (chỉ khi cần thiết)
     */
    private CanHoChiTietDto loadDataFromService(String maCanHo) {
        try {
            if (canHoService == null) {
                System.err.println("CanHoService is null");
                return null;
            }

            CanHoDto canHoDto = new CanHoDto();
            canHoDto.setMaCanHo(maCanHo);
            
            return canHoService.getCanHoChiTiet(canHoDto);
        } catch (Exception e) {
            System.err.println("Error loading from service: " + e.getMessage());
            return null;
        }
    }

    /**
     * Xóa tất cả dữ liệu khi có lỗi
     */
    private void clearAllData() {
        currentCanHo = null;
        cuDanList.clear();
        phuongTienList.clear();
        hoaDonList.clear();
        
        // Clear UI
        clearThongTinCanHo();
        setTableData();
        if (labelTongSoTien != null) {
            labelTongSoTien.setText("0 VNĐ");
        }
    }

    /**
     * Xóa thông tin căn hộ trên UI
     */
    private void clearThongTinCanHo() {
        if (labelMaCanHo != null) labelMaCanHo.setText("-");
        if (labelSoNha != null) labelSoNha.setText("-");
        if (labelTang != null) labelTang.setText("-");
        if (labelToa != null) labelToa.setText("-");
        if (labelDienTich != null) labelDienTich.setText("- m²");
        if (labelTinhTrangKiThuat != null) labelTinhTrangKiThuat.setText("-");
        if (labelTinhTrangSuDung != null) labelTinhTrangSuDung.setText("-");
        
        // Clear thông tin chủ hộ
        if (labelMaDinhDanh != null) labelMaDinhDanh.setText("-");
        if (labelHoVaTen != null) labelHoVaTen.setText("-");
        if (labelNgaySinh != null) {
            labelNgaySinh.setText("-");
            labelNgaySinh.setVisible(false);
        }
        if (labelGioiTinh != null) {
            labelGioiTinh.setText("-");
            labelGioiTinh.setVisible(false);
        }
        if (labelTrangThaiChuSoHuu != null) labelTrangThaiChuSoHuu.setText("-");
        if (labelSoDienThoai != null) labelSoDienThoai.setText("-");
        if (labelEmail != null) labelEmail.setText("-");
        
        // Ẩn thông tin chủ hộ
        if (vBoxChuSoHuu != null) vBoxChuSoHuu.setVisible(false);
    }

    /**
     * Set dữ liệu cho các table
     */
    private void setTableData() {
        System.out.println("=== DEBUG: Setting table data ===");
        
        // Ensure this runs on JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            try {
                // Set data for all tables
                if (tableViewCuDan != null && cuDanList != null) {
                    System.out.println("=== DEBUG: Setting cư dân table data ===");
                    System.out.println("Table visible: " + tableViewCuDan.isVisible());
                    System.out.println("Data size: " + cuDanList.size());
                    
                    tableViewCuDan.setItems(cuDanList);
                    tableViewCuDan.refresh(); // Force refresh
                    
                    // Force column refresh
                    if (!tableViewCuDan.getColumns().isEmpty()) {
                        tableViewCuDan.getColumns().get(0).setVisible(false);
                        tableViewCuDan.getColumns().get(0).setVisible(true);
                    }
                    
                    updateResultCount(); // Hiển thị số kết quả
                    
                    for (CuDanTrongCanHoDto cuDan : cuDanList) {
                        System.out.println("- " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ")");
                    }
                    System.out.println("=== END DEBUG: Set cư dân table data ===");
                } else {
                    System.out.println("WARNING: tableViewCuDan or cuDanList is NULL!");
                    if (tableViewCuDan == null) System.out.println("  - tableViewCuDan is NULL");
                    if (cuDanList == null) System.out.println("  - cuDanList is NULL");
                }
                
                if (tableViewPhuongTien != null && phuongTienList != null) {
                    tableViewPhuongTien.setItems(phuongTienList);
                    tableViewPhuongTien.refresh();
                    System.out.println("DEBUG: Set phương tiện table data - " + phuongTienList.size() + " items");
                }
                
                if (tableViewThuPhi != null && hoaDonList != null) {
                    tableViewThuPhi.setItems(hoaDonList);
                    tableViewThuPhi.refresh();
                    System.out.println("DEBUG: Set hóa đơn table data - " + hoaDonList.size() + " items");
                }
                
                System.out.println("=== END DEBUG: Setting table data ===");
            } catch (Exception e) {
                System.err.println("ERROR: Exception in setTableData: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Cập nhật thông tin căn hộ lên UI
     */
    private void updateThongTinCanHo() {
        if (currentCanHo == null) {
            clearThongTinCanHo();
            return;
        }

        System.out.println("=== DEBUG: updateThongTinCanHo called ===");
        System.out.println("Apartment: " + currentCanHo.getMaCanHo());
        System.out.println("Owner: " + (currentCanHo.getChuHo() != null ? 
            currentCanHo.getChuHo().getHoVaTen() + " (" + currentCanHo.getChuHo().getMaDinhDanh() + ")" : "NULL"));

        if (labelMaCanHo != null) labelMaCanHo.setText(currentCanHo.getMaCanHo());
        if (labelSoNha != null) labelSoNha.setText(currentCanHo.getSoNha());
        if (labelTang != null) labelTang.setText(currentCanHo.getTang());
        if (labelToa != null) labelToa.setText(currentCanHo.getToaNha());
        if (labelDienTich != null) labelDienTich.setText(String.valueOf(currentCanHo.getDienTich()) + " m²");
        if (labelTinhTrangKiThuat != null) {
            labelTinhTrangKiThuat.setText(currentCanHo.getTrangThaiKiThuat());
            System.out.println("DEBUG: Set tình trạng kỹ thuật: " + currentCanHo.getTrangThaiKiThuat());
        }
        if (labelTinhTrangSuDung != null) {
            labelTinhTrangSuDung.setText(currentCanHo.getTrangThaiSuDung());
            System.out.println("DEBUG: Set tình trạng sử dụng: " + currentCanHo.getTrangThaiSuDung());
        }
        
        // Cập nhật thông tin chủ sở hữu nếu có
        if (currentCanHo.getChuHo() != null) {
            System.out.println("=== DEBUG: Updating owner information ===");
            if (labelMaDinhDanh != null) {
                labelMaDinhDanh.setText(currentCanHo.getChuHo().getMaDinhDanh());
                System.out.println("DEBUG: Set owner ID: " + currentCanHo.getChuHo().getMaDinhDanh());
            }
            if (labelHoVaTen != null) {
                labelHoVaTen.setText(currentCanHo.getChuHo().getHoVaTen());
                System.out.println("DEBUG: Set owner name: " + currentCanHo.getChuHo().getHoVaTen());
            }
            
            // Hiển thị ngày sinh và giới tính nếu có
            if (labelNgaySinh != null) {
                if (currentCanHo.getChuHo().getNgaySinh() != null) {
                    labelNgaySinh.setText(currentCanHo.getChuHo().getNgaySinh().toString());
                    labelNgaySinh.setVisible(true);
                    System.out.println("DEBUG: Set owner birth date: " + currentCanHo.getChuHo().getNgaySinh());
                } else {
                    labelNgaySinh.setText("Chưa cập nhật");
                    labelNgaySinh.setVisible(true);
                }
            }
            if (labelGioiTinh != null) {
                if (currentCanHo.getChuHo().getGioiTinh() != null && !currentCanHo.getChuHo().getGioiTinh().trim().isEmpty()) {
                    labelGioiTinh.setText(currentCanHo.getChuHo().getGioiTinh());
                    labelGioiTinh.setVisible(true);
                    System.out.println("DEBUG: Set owner gender: " + currentCanHo.getChuHo().getGioiTinh());
                } else {
                    labelGioiTinh.setText("Chưa cập nhật");
                    labelGioiTinh.setVisible(true);
                }
            }
            
            if (labelTrangThaiChuSoHuu != null) labelTrangThaiChuSoHuu.setText(currentCanHo.getChuHo().getTrangThaiCuTru());
            if (labelSoDienThoai != null) labelSoDienThoai.setText(currentCanHo.getChuHo().getSoDienThoai());
            if (labelEmail != null) labelEmail.setText(currentCanHo.getChuHo().getEmail());
            
            // Hiển thị VBox chủ sở hữu
            if (vBoxChuSoHuu != null) vBoxChuSoHuu.setVisible(true);
            System.out.println("DEBUG: Owner VBox set to visible");
        } else {
            System.out.println("=== DEBUG: No owner - hiding owner VBox ===");
            // Ẩn VBox chủ sở hữu nếu chưa có chủ
            if (vBoxChuSoHuu != null) vBoxChuSoHuu.setVisible(false);
        }
        System.out.println("=== END DEBUG: updateThongTinCanHo ===");
    }

    // Tab navigation methods
    @FXML
    private void showThongTinTab() {
        System.out.println("=== DEBUG: Showing Thong Tin tab ===");
        hideAllTabs();
        if (anchorPaneThongTin != null) {
            anchorPaneThongTin.setVisible(true);
        }
        updateTabStyles("thongtin");
        
        // Refresh dữ liệu thông tin căn hộ mỗi khi mở tab để đảm bảo hiển thị dữ liệu mới nhất
        if (currentCanHo != null && currentCanHo.getMaCanHo() != null) {
            System.out.println("=== DEBUG: Refreshing Thong Tin data for apartment: " + currentCanHo.getMaCanHo() + " ===");
            loadData(currentCanHo.getMaCanHo(), true);
        }
        System.out.println("=== END DEBUG: Thong Tin tab ===");
    }

    @FXML
    private void showCuDanTab() {
        System.out.println("=== DEBUG: Showing Cu Dan tab ===");
        hideAllTabs();
        if (anchorPaneCuDan != null) {
            anchorPaneCuDan.setVisible(true);
            System.out.println("anchorPaneCuDan set to visible");
        } else {
            System.out.println("WARNING: anchorPaneCuDan is NULL!");
        }
        updateTabStyles("cudan");
        
        // Refresh dữ liệu cư dân mỗi khi mở tab
        if (currentCanHo != null && currentCanHo.getMaCanHo() != null) {
            System.out.println("=== DEBUG: Refreshing Cu Dan data for apartment: " + currentCanHo.getMaCanHo() + " ===");
            
            // Load lại dữ liệu từ service để đảm bảo có dữ liệu mới nhất
            loadData(currentCanHo.getMaCanHo(), true);
            
            // Clear search fields để hiển thị tất cả dữ liệu
            if (textFieldTimKiemCuDan != null) {
                textFieldTimKiemCuDan.clear();
            }
            if (textFieldMaDinhDanh != null) {
                textFieldMaDinhDanh.clear();
            }
            
            // Update result count
            updateResultCount();
        }
        
        // Debug table visibility and data
        if (tableViewCuDan != null) {
            System.out.println("tableViewCuDan visible: " + tableViewCuDan.isVisible());
            System.out.println("tableViewCuDan items: " + tableViewCuDan.getItems().size());
            System.out.println("cuDanList size: " + (cuDanList != null ? cuDanList.size() : "NULL"));
            System.out.println("currentCanHo: " + (currentCanHo != null ? currentCanHo.getMaCanHo() : "NULL"));
            
            if (cuDanList != null && !cuDanList.isEmpty()) {
                System.out.println("=== DEBUG: Current cuDanList contents after refresh ===");
                for (CuDanTrongCanHoDto cuDan : cuDanList) {
                    System.out.println("- " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ") - " + cuDan.getTrangThaiCuTru());
                }
                System.out.println("=== END cuDanList contents ===");
            } else {
                System.out.println("cuDanList is EMPTY or NULL after refresh!");
            }
        }
        System.out.println("=== END DEBUG: Cu Dan tab ===");
    }

    @FXML
    private void showPhuongTienTab() {
        hideAllTabs();
        if (anchorPanePhuongTien != null) {
            anchorPanePhuongTien.setVisible(true);
        }
        updateTabStyles("phuongtien");
    }

    @FXML
    private void showThuPhiTab() {
        hideAllTabs();
        if (anchorPaneThuPhi != null) {
            anchorPaneThuPhi.setVisible(true);
        }
        updateTabStyles("thuphi");
    }

    private void hideAllTabs() {
        if (anchorPaneThongTin != null) anchorPaneThongTin.setVisible(false);
        if (anchorPaneCuDan != null) anchorPaneCuDan.setVisible(false);
        if (anchorPanePhuongTien != null) anchorPanePhuongTien.setVisible(false);
        if (anchorPaneThuPhi != null) anchorPaneThuPhi.setVisible(false);
    }

    private void updateTabStyles(String activeTab) {
        // Reset all tab styles
        if (buttonThongTin != null) buttonThongTin.getStyleClass().removeAll("active-tab");
        if (buttonCuDan != null) buttonCuDan.getStyleClass().removeAll("active-tab");
        if (buttonPhuongTien != null) buttonPhuongTien.getStyleClass().removeAll("active-tab");
        if (buttonThuPhi != null) buttonThuPhi.getStyleClass().removeAll("active-tab");

        // Set active tab style
        switch (activeTab) {
            case "thongtin":
                if (buttonThongTin != null) buttonThongTin.getStyleClass().add("active-tab");
                break;
            case "cudan":
                if (buttonCuDan != null) buttonCuDan.getStyleClass().add("active-tab");
                break;
            case "phuongtien":
                if (buttonPhuongTien != null) buttonPhuongTien.getStyleClass().add("active-tab");
                break;
            case "thuphi":
                if (buttonThuPhi != null) buttonThuPhi.getStyleClass().add("active-tab");
                break;
        }
    }

    // Search and filter methods
    @FXML
    private void handleTimKiemCuDan() {
        String keywordHoTen = textFieldTimKiemCuDan != null ? textFieldTimKiemCuDan.getText().trim() : "";
        String keywordMaDinhDanh = textFieldMaDinhDanh != null ? textFieldMaDinhDanh.getText().trim() : "";
        
        // Nếu cả 2 ô tìm kiếm đều trống thì hiển thị toàn bộ danh sách
        if (keywordHoTen.isEmpty() && keywordMaDinhDanh.isEmpty()) {
            setTableData(); // Reset to full list
            updateResultCount();
            return;
        }
        
        // Filter cư dân list based on search keywords
        if (cuDanList != null) {
            ObservableList<CuDanTrongCanHoDto> filteredList = cuDanList.stream()
                .filter(cuDan -> {
                    boolean matchesHoTen = keywordHoTen.isEmpty() || 
                        cuDan.getHoVaTen().toLowerCase().contains(keywordHoTen.toLowerCase());
                    boolean matchesMaDinhDanh = keywordMaDinhDanh.isEmpty() ||
                        cuDan.getMaDinhDanh().toLowerCase().contains(keywordMaDinhDanh.toLowerCase());
                    // Cả 2 điều kiện phải thỏa mãn (AND logic)
                    return matchesHoTen && matchesMaDinhDanh;
                })
                .collect(FXCollections::observableArrayList, 
                        ObservableList::add, 
                        ObservableList::addAll);
            
            if (tableViewCuDan != null) {
                tableViewCuDan.setItems(filteredList);
                updateResultCount(filteredList.size(), cuDanList.size());
                System.out.println("=== DEBUG: Search completed ===");
                System.out.println("Search by name: '" + keywordHoTen + "'");
                System.out.println("Search by ID: '" + keywordMaDinhDanh + "'");
                System.out.println("Results: " + filteredList.size() + "/" + cuDanList.size());
            }
        }
    }

    @FXML
    private void handleTimKiemPhuongTien() {
        String maSoXe = textFieldMaSoXe != null ? textFieldMaSoXe.getText().trim() : "";
        String loaiPhuongTien = comboBoxLoaiPhuongTien != null ? comboBoxLoaiPhuongTien.getValue() : "";
        
        if (maSoXe.isEmpty() && "Tất cả".equals(loaiPhuongTien)) {
            setTableData(); // Reset to full list
            return;
        }
        
        // Filter phương tiện list
        if (phuongTienList != null) {
            ObservableList<PhuongTienDto> filteredList = phuongTienList.stream()
                .filter(pt -> {
                    boolean matchesBienSo = maSoXe.isEmpty() || 
                        pt.getBienSo().toLowerCase().contains(maSoXe.toLowerCase());
                    boolean matchesLoai = "Tất cả".equals(loaiPhuongTien) || 
                        pt.getLoaiPhuongTien().equals(loaiPhuongTien);
                    return matchesBienSo && matchesLoai;
                })
                .collect(FXCollections::observableArrayList, 
                        ObservableList::add, 
                        ObservableList::addAll);
            
            if (tableViewPhuongTien != null) {
                tableViewPhuongTien.setItems(filteredList);
            }
        }
    }

    @FXML
    private void handleThemPhuongTien() {
        try {
            // Kiểm tra quyền
            String userRole = getCurrentUserRole();
            if ("Tổ trưởng".equals(userRole)) {
                showError("Không có quyền", "Bạn không có quyền thêm phương tiện. Chỉ có Tổ phó mới có thể thêm.");
                return;
            }
            
            if (currentCanHo == null || currentCanHo.getMaCanHo() == null) {
                showError("Lỗi", "Không có thông tin căn hộ để thêm phương tiện");
                return;
            }

            // Sử dụng FXMLLoader và để FXML tự tạo controller
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/phuong_tien.fxml")
            );
            
            // Load view - FXML sẽ tự tạo controller
            javafx.scene.Parent root = loader.load();
            
            // Get controller được tạo bởi FXML
            io.github.ktpm.bluemoonmanagement.controller.ThemPhuongTien controller = loader.getController();
            
            // Inject ApplicationContext trước tiên
            if (applicationContext != null) {
                System.out.println("DEBUG: Injecting ApplicationContext into ThemPhuongTien");
                controller.setApplicationContext(applicationContext);
            } else {
                System.err.println("ERROR: ApplicationContext is null in ChiTietCanHoController!");
            }
            
            // Inject services
            if (phuongTienService != null) {
                System.out.println("DEBUG: Injecting existing PhuongTienService");
                controller.setPhuongTienService(phuongTienService);
            } else {
                System.out.println("DEBUG: PhuongTienService is null, relying on ApplicationContext injection");
            }
            
            // Set mã căn hộ
            controller.setMaCanHo(currentCanHo.getMaCanHo());
            
            // Set chế độ thêm mới
            controller.setAddMode();

            // Tạo cửa sổ mới
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Thêm phương tiện - Căn hộ " + currentCanHo.getMaCanHo());

            // Không cho tương tác cửa sổ cha khi đang mở
            newStage.initModality(Modality.APPLICATION_MODAL);

            // Gán owner là cửa sổ hiện tại
            Stage currentStage = (Stage) button_close_up.getScene().getWindow();
            newStage.initOwner(currentStage);

            // Hiển thị cửa sổ và đợi đóng
            newStage.showAndWait();
            
            // Refresh data sau khi đóng form thêm phương tiện
            System.out.println("DEBUG: Refreshing data after adding vehicle");
            refreshData();

        } catch (IOException e) {
            showError("Lỗi", "Không thể mở cửa sổ thêm phương tiện: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Lỗi", "Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện chỉnh sửa phương tiện
     */
    private void handleEditPhuongTien(PhuongTienDto phuongTien) {
        try {
            // Sử dụng FXMLLoader và để FXML tự tạo controller
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/phuong_tien.fxml")
            );
            
            // Load view - FXML sẽ tự tạo controller
            javafx.scene.Parent root = loader.load();
            
            // Get controller được tạo bởi FXML
            io.github.ktpm.bluemoonmanagement.controller.ThemPhuongTien controller = loader.getController();
            
            // Inject ApplicationContext và services
            if (applicationContext != null) {
                controller.setApplicationContext(applicationContext);
            }
            if (phuongTienService != null) {
                controller.setPhuongTienService(phuongTienService);
            }
            
            // Set chế độ chỉnh sửa
            controller.setEditMode(phuongTien);

            // Tạo cửa sổ mới
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Chỉnh sửa phương tiện - " + phuongTien.getBienSo());

            // Không cho tương tác cửa sổ cha khi đang mở
            newStage.initModality(Modality.APPLICATION_MODAL);

            // Gán owner là cửa sổ hiện tại
            Stage currentStage = (Stage) button_close_up.getScene().getWindow();
            newStage.initOwner(currentStage);

            // Hiển thị cửa sổ và đợi đóng
            newStage.showAndWait();
            
            // Refresh data sau khi đóng form chỉnh sửa phương tiện
            System.out.println("DEBUG: Refreshing data after editing vehicle");
            refreshData();

        } catch (Exception e) {
            showError("Lỗi", "Không thể mở cửa sổ chỉnh sửa phương tiện: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Xử lý sự kiện xóa phương tiện
     */
    private void handleDeletePhuongTien(PhuongTienDto phuongTien) {
        try {
            // Hiển thị dialog xác nhận
            boolean confirm = ThongBaoController.showConfirmation("Xác nhận xóa", "Bạn có chắc chắn muốn xóa phương tiện " + phuongTien.getBienSo() + "?");

            if (confirm) {
                if (phuongTienService != null) {
                    // Gọi service để xóa phương tiện
                    ResponseDto result = phuongTienService.xoaPhuongTien(phuongTien);

                    // Sử dụng reflection để lấy kết quả
                    try {
                        java.lang.reflect.Field successField = ResponseDto.class.getDeclaredField("success");
                        successField.setAccessible(true);
                        boolean success = (Boolean) successField.get(result);

                        java.lang.reflect.Field messageField = ResponseDto.class.getDeclaredField("message");
                        messageField.setAccessible(true);
                        String message = (String) messageField.get(result);

                        if (success) {
                            showSuccess("Thành công", "Đã xóa phương tiện thành công: " + message);

                            // Refresh data sau khi xóa phương tiện
                            System.out.println("DEBUG: Refreshing data after deleting vehicle");
                            refreshData();
                        } else {
                            showError("Lỗi", "Không thể xóa phương tiện: " + message);
                        }
                    } catch (Exception e) {
                        showError("Lỗi", "Lỗi khi xử lý phản hồi: " + e.getMessage());
                    }
                } else {
                    showError("Lỗi", "Service chưa được khởi tạo");
                }
            }

        } catch (Exception e) {
            showError("Lỗi", "Đã xảy ra lỗi khi xóa phương tiện: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTimKiemThuPhi() {
        String tenKhoanThu = textFieldTenKhoanThu != null ? textFieldTenKhoanThu.getText().trim() : "";
        String loaiKhoanThu = comboBoxLoaiKhoanThu != null ? comboBoxLoaiKhoanThu.getValue() : "";
        
        if (tenKhoanThu.isEmpty() && "Tất cả".equals(loaiKhoanThu)) {
            setTableData(); // Reset to full list
            return;
        }
        
        // Filter hóa đơn list
        if (hoaDonList != null) {
            ObservableList<HoaDonDto> filteredList = hoaDonList.stream()
                .filter(hd -> {
                    boolean matchesTen = tenKhoanThu.isEmpty() || 
                        hd.getTenKhoanThu().toLowerCase().contains(tenKhoanThu.toLowerCase());
                    boolean matchesLoai = "Tất cả".equals(loaiKhoanThu) || 
                        hd.getTenKhoanThu().contains(loaiKhoanThu);
                    return matchesTen && matchesLoai;
                })
                .collect(FXCollections::observableArrayList, 
                        ObservableList::add, 
                        ObservableList::addAll);
            
            if (tableViewThuPhi != null) {
                tableViewThuPhi.setItems(filteredList);
            }
        }
    }

    // Thu phí actions
    @FXML
    private void handleThuToanBo() {
        // Kiểm tra quyền
        try {
            String userRole = getCurrentUserRole();
            if ("Tổ trưởng".equals(userRole)) {
                showError("Không có quyền", "Bạn không có quyền thực hiện thu toàn bộ. Chỉ có Tổ phó mới có thể thực hiện.");
                return;
            }
        } catch (Exception e) {
            System.err.println("ERROR: Cannot check user permission: " + e.getMessage());
        }
        
        boolean baoGomBatBuoc = checkBoxKhongTinhBatBuoc != null ? !checkBoxKhongTinhBatBuoc.isSelected() : true;
        
        if (hoaDonList == null || hoaDonList.isEmpty()) {
            showError("Không có dữ liệu", "Không có hóa đơn nào để thu");
            return;
        }
        
        // Tính tổng tiền cần thu
        int tongTien = 0;
        for (HoaDonDto hoaDon : hoaDonList) {
            if (hoaDon.getSoTien() != null) {
                tongTien += hoaDon.getSoTien();
            }
        }
        
        if (tongTien <= 0) {
            showInfo("Thông báo", "Không có khoản phí nào cần thu");
            return;
        }
        
        boolean confirm = ThongBaoController.showConfirmation("Xác nhận thu phí", String.format("Tổng số tiền: %,d VNĐ\nBạn có chắc chắn muốn thu toàn bộ?", tongTien));

        if (confirm) {
            // Implement thu phí logic here
            showSuccess("Thành công", "Đã thu toàn bộ phí thành công");
            // Reload data after payment
            if (currentCanHo != null) {
                loadDataFromService(currentCanHo.getMaCanHo());
            }
        }
    }
    

    @FXML
    private void handleXemLichSu() {
        // Kiểm tra quyền
        try {
            String userRole = getCurrentUserRole();
            if ("Tổ trưởng".equals(userRole)) {
                showError("Không có quyền", "Bạn không có quyền xem lịch sử thu phí. Chỉ có Tổ phó mới có thể xem.");
                return;
            }
        } catch (Exception e) {
            System.err.println("ERROR: Cannot check user permission: " + e.getMessage());
        }
        
        if (currentCanHo == null) {
            showError("Lỗi", "Không có thông tin căn hộ");
            return;
        }
        
        showInfo("Lịch sử thu phí", "Chức năng xem lịch sử thu phí cho căn hộ " + currentCanHo.getMaCanHo());
    }

    @FXML
    private void handleClose() {
        try {
            // Remove this instance from tracking list
            openWindows.remove(this);
            
            javafx.stage.Stage stage = (javafx.stage.Stage) button_close_up.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            System.err.println("Lỗi khi đóng cửa sổ chi tiết căn hộ: " + e.getMessage());
        }
    }

    @FXML
    private void handleChinhSuaCanHo() {
        try {
            System.out.println("=== DEBUG: EDIT BUTTON CLICKED ===");
            
            // Kiểm tra quyền
            String userRole = getCurrentUserRole();
            System.out.println("DEBUG: User role: " + userRole);
            if ("Tổ trưởng".equals(userRole) || "Kế toán".equals(userRole)) {
                System.out.println("DEBUG: Access denied for role: " + userRole);
                showError("Không có quyền", "Bạn không có quyền chỉnh sửa căn hộ. Chỉ được xem thông tin.");
                return;
            }
            
            if (currentCanHo == null) {
                System.out.println("ERROR: currentCanHo is null");
                showError("Lỗi", "Không có thông tin căn hộ để chỉnh sửa");
                return;
            }
            
            System.out.println("DEBUG: Current apartment data:");
            System.out.println("  - MaCanHo: " + currentCanHo.getMaCanHo());
            System.out.println("  - ToaNha: " + currentCanHo.getToaNha());
            System.out.println("  - Tang: " + currentCanHo.getTang());
            System.out.println("  - SoNha: " + currentCanHo.getSoNha());
            System.out.println("  - Owner: " + (currentCanHo.getChuHo() != null ? 
                currentCanHo.getChuHo().getHoVaTen() : "NULL"));
            
            System.out.println("DEBUG: About to open edit form...");
            
            // Sử dụng form thêm căn hộ cho chỉnh sửa
            openEditFormUsingAddForm();
            
            System.out.println("DEBUG: Edit form opening completed successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR: Exception in handleChinhSuaCanHo: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi mở chỉnh sửa", "Không thể mở form chỉnh sửa căn hộ: " + e.getMessage());
        }
    }
    
    /**
     * Mở form chỉnh sửa bằng cách sử dụng form thêm căn hộ
     */
    private void openEditFormUsingAddForm() {
        try {
            System.out.println("=== DEBUG: Starting openEditFormUsingAddForm ===");
            
            System.out.println("DEBUG: Loading FXML...");
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/them_can_ho.fxml")
            );
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML loaded successfully");
            
            // Get controller
            System.out.println("DEBUG: Getting controller from loader...");
            io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller = loader.getController();
            System.out.println("DEBUG: Controller: " + (controller != null ? "Found" : "NULL"));
            if (controller != null) {
                // Inject services
                if (canHoService != null) {
                    controller.setCanHoService(canHoService);
                    System.out.println("DEBUG: Injected CanHoService to edit form controller");
                }
                
                // Inject ApplicationContext
                if (applicationContext != null) {
                    controller.setApplicationContext(applicationContext);
                    System.out.println("DEBUG: Injected ApplicationContext to edit form controller");
                }
                
                // Convert current data to CanHoDto for edit mode
                System.out.println("DEBUG: Converting to CanHoDto...");
                CanHoDto canHoDto = convertToCanHoDto();
                
                // Setup edit mode
                System.out.println("DEBUG: Setting up edit mode...");
                controller.setupEditMode(canHoDto);
                
                // Setup form untuk chỉnh sửa
                System.out.println("DEBUG: Setting up edit form...");
                setupEditForm(controller);
            }
            
            // Create and show stage
            System.out.println("DEBUG: Creating stage...");
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Chỉnh sửa căn hộ - " + currentCanHo.getMaCanHo());
            stage.setScene(new javafx.scene.Scene(root, 900, 650));
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            stage.initOwner(button_close_up.getScene().getWindow());
            
            // Add event handler để refresh data khi form đóng
            stage.setOnHiding(event -> {
                System.out.println("=== DEBUG: Edit form closing, refreshing current detail window ===");
                // Refresh lại dữ liệu sau khi form đóng
                javafx.application.Platform.runLater(() -> {
                    try {
                        Thread.sleep(500); // Delay nhỏ để đảm bảo database được cập nhật
                        refreshData();
                        System.out.println("=== DEBUG: Refresh completed after edit form closed ===");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
            
            System.out.println("DEBUG: Showing stage...");
            stage.show();
            
            System.out.println("DEBUG: Edit apartment window opened successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot open edit form: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form chỉnh sửa: " + e.getMessage());
        }
    }
    
    /**
     * Convert CanHoChiTietDto to CanHoDto
     */
    private CanHoDto convertToCanHoDto() {
        CanHoDto dto = new CanHoDto();
        dto.setMaCanHo(currentCanHo.getMaCanHo());
        dto.setToaNha(currentCanHo.getToaNha());
        dto.setTang(currentCanHo.getTang());
        dto.setSoNha(currentCanHo.getSoNha());
        dto.setDienTich(currentCanHo.getDienTich());
        dto.setDaBanChua(currentCanHo.isDaBanChua());
        dto.setTrangThaiKiThuat(currentCanHo.getTrangThaiKiThuat());
        dto.setTrangThaiSuDung(currentCanHo.getTrangThaiSuDung());
        dto.setChuHo(currentCanHo.getChuHo());
        return dto;
    }
    
    /**
     * Setup form để phù hợp với chỉnh sửa
     */
    private void setupEditForm(io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller) {
        try {
            // Access và disable các field không được chỉnh sửa bằng reflection
            disableReadOnlyFields(controller);
            
            // Thay đổi title và button text
            updateFormLabels(controller);
            
            System.out.println("DEBUG: Edit form setup completed");
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot setup edit form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Disable các field không được chỉnh sửa
     */
    private void disableReadOnlyFields(io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller) {
        try {
            // Disable tòa nhà
            setFieldDisabled(controller, "textFieldToa", true);
            
            // Disable tầng  
            setFieldDisabled(controller, "textFieldTang", true);
            
            // Disable số nhà
            setFieldDisabled(controller, "textFieldSoNha", true);
            
            System.out.println("DEBUG: Read-only fields disabled");
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot disable read-only fields: " + e.getMessage());
        }
    }
    
    /**
     * Set field disabled bằng reflection
     */
    private void setFieldDisabled(io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller, 
                                  String fieldName, boolean disabled) {
        try {
            java.lang.reflect.Field field = controller.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldObject = field.get(controller);
            
            if (fieldObject instanceof javafx.scene.control.TextField) {
                ((javafx.scene.control.TextField) fieldObject).setDisable(disabled);
            } else if (fieldObject instanceof javafx.scene.control.ComboBox) {
                ((javafx.scene.control.ComboBox<?>) fieldObject).setDisable(disabled);
            }
            
        } catch (Exception e) {
            System.err.println("WARNING: Cannot disable field " + fieldName + ": " + e.getMessage());
        }
    }
    
    /**
     * Update form labels cho chỉnh sửa
     */
    private void updateFormLabels(io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller) {
        try {
            // Change title
            setLabelText(controller, "labelTitle", "Chỉnh sửa căn hộ");
            
            // Change button text
            setButtonText(controller, "buttonTaoCanHo", "Cập nhật căn hộ");
            
            System.out.println("DEBUG: Form labels updated for edit mode");
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot update form labels: " + e.getMessage());
        }
    }
    
    /**
     * Set label text bằng reflection
     */
    private void setLabelText(io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller, 
                              String fieldName, String text) {
        try {
            java.lang.reflect.Field field = controller.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldObject = field.get(controller);
            
            if (fieldObject instanceof javafx.scene.control.Label) {
                ((javafx.scene.control.Label) fieldObject).setText(text);
            }
            
        } catch (Exception e) {
            System.err.println("WARNING: Cannot set label text for " + fieldName + ": " + e.getMessage());
        }
    }
    
    /**
     * Set button text bằng reflection
     */
    private void setButtonText(io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller, 
                               String fieldName, String text) {
        try {
            java.lang.reflect.Field field = controller.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object fieldObject = field.get(controller);
            
            if (fieldObject instanceof javafx.scene.control.Button) {
                ((javafx.scene.control.Button) fieldObject).setText(text);
            }
            
        } catch (Exception e) {
            System.err.println("WARNING: Cannot set button text for " + fieldName + ": " + e.getMessage());
        }
    }

    /**
     * Thiết lập dữ liệu căn hộ từ bên ngoài
     */
    public void setCanHoData(CanHoChiTietDto canHoData) {
        try {
            System.out.println("DEBUG: setCanHoData called with: " + (canHoData != null ? canHoData.getMaCanHo() : "NULL"));
            
            if (canHoData != null) {
                System.out.println("DEBUG: About to call loadData for: " + canHoData.getMaCanHo());
                // Load từ cache khi xem, chỉ từ service khi cần refresh
                loadData(canHoData.getMaCanHo(), false);
                System.out.println("DEBUG: loadData completed successfully");
            } else {
                System.err.println("ERROR: CanHoData is null");
                showError("Lỗi dữ liệu", "Không có thông tin căn hộ để hiển thị");
                clearAllData();
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception in setCanHoData: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi thiết lập dữ liệu", "Không thể thiết lập dữ liệu căn hộ: " + e.getMessage());
        }
    }

    /**
     * Cập nhật tổng số tiền cần thu
     */
    private void updateTongSoTien() {
        if (hoaDonList != null && labelTongSoTien != null) {
            int tongTien = 0;
            for (HoaDonDto hoaDon : hoaDonList) {
                if (hoaDon.getSoTien() != null) {
                    tongTien += hoaDon.getSoTien();
                }
            }
            labelTongSoTien.setText(String.format("%,d VNĐ", tongTien));
        }
    }

    /**
     * Cập nhật số kết quả hiển thị
     */
    private void updateResultCount() {
        if (cuDanList != null && labelHienThiKetQuaCuDan != null) {
            labelHienThiKetQuaCuDan.setText("Hiển thị " + cuDanList.size() + "/" + cuDanList.size() + " kết quả");
        }
    }
    
    /**
     * Cập nhật số kết quả hiển thị với số đã lọc
     */
    private void updateResultCount(int filtered, int total) {
        if (labelHienThiKetQuaCuDan != null) {
            labelHienThiKetQuaCuDan.setText("Hiển thị " + filtered + "/" + total + " kết quả");
        }
    }

    // Utility methods for showing alerts
    private void showError(String title, String message) {
        ThongBaoController.showError(title, message);
    }

    private void showSuccess(String title, String message) {
        ThongBaoController.showSuccess(title, message);
    }

    private void showInfo(String title, String message) {
        ThongBaoController.showInfo(title, message);
    }

    // Setter for dependency injection
    public void setCanHoService(CanHoService canHoService) {
        this.canHoService = canHoService;
        System.out.println("CanHoService đã được inject vào ChiTietCanHoController");
    }
    
    /**
     * Setter để inject ApplicationContext từ bên ngoài
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        System.out.println("DEBUG: Setting ApplicationContext in ChiTietCanHoController");
        this.applicationContext = applicationContext;
        
        // Sau khi có ApplicationContext, thử lấy các service nếu chưa có
        ensureServicesAvailable();
    }
    
    /**
     * Setter để inject PhuongTienService từ bên ngoài
     */
    public void setPhuongTienService(io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService phuongTienService) {
        System.out.println("DEBUG: Setting PhuongTienService in ChiTietCanHoController");
        this.phuongTienService = phuongTienService;
    }
    
    /**
     * Đảm bảo các service luôn có sẵn
     */
    private void ensureServicesAvailable() {
        if (applicationContext == null) {
            System.err.println("ERROR: ApplicationContext is null, cannot get services");
            return;
        }
        
        try {
            if (canHoService == null) {
                canHoService = applicationContext.getBean(CanHoService.class);
                System.out.println("DEBUG: Got CanHoService from ApplicationContext");
            }
            
            if (phuongTienService == null) {
                phuongTienService = applicationContext.getBean(io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService.class);
                System.out.println("DEBUG: Got PhuongTienService from ApplicationContext");
            }
            
            if (cacheDataService == null) {
                cacheDataService = applicationContext.getBean(CacheDataService.class);
                System.out.println("DEBUG: Got CacheDataService from ApplicationContext");
            }
        } catch (Exception e) {
            System.err.println("ERROR: Cannot get services from ApplicationContext: " + e.getMessage());
        }
    }
    
    /**
     * Refresh dữ liệu từ database - gọi khi cần cập nhật sau khi thêm/sửa/xóa
     * QUAN TRỌNG: Không refresh nếu căn hộ đã bị xóa (currentCanHo = null)
     */
    public void refreshData() {
        if (currentCanHo == null) {
            System.out.println("=== DEBUG: Cannot refresh - apartment has been deleted (currentCanHo is null) ===");
            return;
        }
        
        if (currentCanHo.getMaCanHo() != null) {
            String maCanHo = currentCanHo.getMaCanHo();
            System.out.println("=== DEBUG: Refreshing data for apartment: " + maCanHo + " ===");
            System.out.println("Before refresh - Owner: " + (currentCanHo.getChuHo() != null ? 
                currentCanHo.getChuHo().getHoVaTen() + " (" + currentCanHo.getChuHo().getMaDinhDanh() + ")" : "NULL"));
            
            // Clear cache for this apartment to ensure fresh data
            if (cacheDataService != null) {
                System.out.println("=== DEBUG: Clearing cache for apartment: " + maCanHo + " ===");
                cacheDataService.refreshCacheData();
            }
            
            // Load fresh data from service (force refresh)
            loadData(maCanHo, true);
            
            System.out.println("After refresh - Owner: " + (currentCanHo.getChuHo() != null ? 
                currentCanHo.getChuHo().getHoVaTen() + " (" + currentCanHo.getChuHo().getMaDinhDanh() + ")" : "NULL"));
            
            // Force table refresh on JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                try {
                    System.out.println("=== DEBUG: Platform.runLater - Force refreshing table views ===");
                    
                    // Force refresh table views
                    if (tableViewCuDan != null && cuDanList != null) {
                        tableViewCuDan.refresh();
                        tableViewCuDan.getColumns().get(0).setVisible(false);
                        tableViewCuDan.getColumns().get(0).setVisible(true);
                        System.out.println("DEBUG: Forced refresh cư dân table with " + cuDanList.size() + " items");
                    }
                    
                    if (tableViewPhuongTien != null && phuongTienList != null) {
                        tableViewPhuongTien.refresh();
                        System.out.println("DEBUG: Forced refresh phương tiện table with " + phuongTienList.size() + " items");
                    }
                    
                    if (tableViewThuPhi != null && hoaDonList != null) {
                        tableViewThuPhi.refresh();
                        System.out.println("DEBUG: Forced refresh hóa đơn table with " + hoaDonList.size() + " items");
                    }
                    
                    System.out.println("=== DEBUG: Table refresh completed ===");
                } catch (Exception e) {
                    System.err.println("ERROR: Exception during table refresh: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }
    
    /**
     * Static method để refresh tất cả cửa sổ chi tiết đang mở cho một căn hộ
     * CẢNH BÁO: Không gọi method này cho căn hộ đã bị xóa!
     */
    public static void refreshAllWindowsForApartment(String maCanHo) {
        System.out.println("=== DEBUG: Refreshing all windows for apartment: " + maCanHo + " ===");
        System.out.println("Number of open windows: " + openWindows.size());
        
        int refreshedCount = 0;
        for (ChiTietCanHoController controller : new ArrayList<>(openWindows)) {
            if (controller.currentCanHo != null && 
                maCanHo.equals(controller.currentCanHo.getMaCanHo())) {
                System.out.println("Found matching window for apartment: " + maCanHo + " - Refreshing...");
                try {
                    controller.refreshData();
                    refreshedCount++;
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to refresh window for apartment " + maCanHo + ": " + e.getMessage());
                    // Nếu refresh thất bại, có thể căn hộ đã bị xóa, đóng cửa sổ
                    controller.handleClose();
                }
            } else {
                System.out.println("Window for different apartment: " + 
                    (controller.currentCanHo != null ? controller.currentCanHo.getMaCanHo() : "NULL"));
            }
        }
        
        System.out.println("=== END DEBUG: Refreshed " + refreshedCount + " windows for apartment " + maCanHo + " ===");
    }
    
    /**
     * Static method để đóng tất cả cửa sổ chi tiết đang mở cho một căn hộ (khi căn hộ bị xóa)
     */
    public static void closeAllWindowsForApartment(String maCanHo) {
        System.out.println("=== DEBUG: Closing all windows for deleted apartment: " + maCanHo + " ===");
        System.out.println("Number of open windows: " + openWindows.size());
        
        // Tạo copy của list để tránh ConcurrentModificationException
        List<ChiTietCanHoController> controllersToClose = new ArrayList<>();
        
        for (ChiTietCanHoController controller : openWindows) {
            if (controller.currentCanHo != null && 
                maCanHo.equals(controller.currentCanHo.getMaCanHo())) {
                System.out.println("Found matching window for deleted apartment: " + maCanHo + " - Marking for deletion...");
                // QUAN TRỌNG: Vô hiệu hóa controller trước khi đóng để tránh refresh
                controller.currentCanHo = null; // Xóa reference để tránh refresh lỗi
                controllersToClose.add(controller);
            }
        }
        
        // Đóng các cửa sổ NGAY LẬP TỨC trên UI thread
        javafx.application.Platform.runLater(() -> {
            try {
                for (ChiTietCanHoController controller : controllersToClose) {
                    // Remove khỏi list trước để tránh reference
                    openWindows.remove(controller);
                    
                    if (controller.button_close_up != null) {
                        javafx.stage.Stage stage = (javafx.stage.Stage) controller.button_close_up.getScene().getWindow();
                        if (stage != null) {
                            stage.close();
                            System.out.println("DEBUG: Closed detail window for deleted apartment: " + maCanHo);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("ERROR: Failed to close windows for apartment: " + maCanHo + " - " + e.getMessage());
            }
        });
        
        System.out.println("=== END DEBUG: Initiated close for " + controllersToClose.size() + " windows for deleted apartment " + maCanHo + " ===");
    }
} 