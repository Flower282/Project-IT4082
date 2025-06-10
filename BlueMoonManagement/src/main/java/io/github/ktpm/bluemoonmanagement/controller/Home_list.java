package io.github.ktpm.bluemoonmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.QuanLyTaiKhoanService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.FxView;
import io.github.ktpm.bluemoonmanagement.util.FxViewLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class Home_list implements Initializable {
    @FXML
    private BarChart<?, ?> barChartDanCu;

    @FXML
    private Button buttonDoiMatKhau;

    @FXML
    private Button buttonNhapExcelCanHo;

    @FXML
    private Button buttonNhapExcelCuDan;

    @FXML
    private Button buttonNhapExcelTaiKhoan;

    @FXML
    private Button buttonSeeAllCanHo;

    @FXML
    private Button buttonSeeAllCuDan;

    @FXML
    private Button buttonSeeAllKhoanThu;

    @FXML
    private Button buttonThemCanHo;

    @FXML
    private Button buttonThemCuDan;

    @FXML
    private Button buttonThemKhoanThu;

    @FXML
    private Button buttonThemTaiKhoan;

    @FXML
    private Button buttonTimKiemCanHo;

    @FXML
    private Button buttonTimKiemCuDan;

    @FXML
    private Button buttonTimKiemCuDan11;

    @FXML
    private Button buttonTimKiemKhoanThu;

    @FXML
    private Button buttonTimKiemThuPhi;

    @FXML
    private Button buttonXuatExcelCanHo;

    @FXML
    private Button buttonXuatExcelCuDan;

    @FXML
    private Button buttonXuatExcelKhoanThu;

    @FXML
    private Button buttonXuatExcelTaiKhoan;

    @FXML
    private Button buttonXuatExcelThuPhi;

    @FXML
    private ComboBox<?> comboBoxLoaiKhoanThu;

    @FXML
    private ComboBox<?> comboBoxLoaiKhoanThu1;

    @FXML
    private TextField textFieldTang;

    @FXML
    private TextField textFieldToa;

    @FXML
    private ComboBox<?> comboBoxTrangThai;

    @FXML
    private ComboBox<?> comboBoxTrangThaiCuDan;

    @FXML
    private ComboBox<?> comboBoxTrangThaiHoaDon;

    @FXML
    private ComboBox<?> comboBoxTrangThaiTaiKhoan;

    @FXML
    private ComboBox<?> comboBoxVaiTro;

    @FXML
    private ComboBox<?> comboBoxVaiTroHoSo;

    @FXML
    private DatePicker datePickerNgayNop;

    @FXML
    private GridPane gridPaneTrangChu;


    @FXML
    private Label labelBienDongDanCu;

    @FXML
    private Label labelCanHo;

    @FXML
    private Label labelCanHoNumber;

    @FXML
    private Label labelCuDanNumber;

    @FXML
    private Label labelCuDanNumber1;

    @FXML
    private Label labelDanCu;

    @FXML
    private Label labelDanCu1;

    @FXML
    private Label labelHienThiKetQua;

    @FXML
    private Label labelHienThiKetQua1;

    @FXML
    private Label labelHienThiKetQuaCuDan;

    @FXML
    private Label labelHienThiKetQuaKhoanThu;

    @FXML
    private Label labelHienThiKetQuaKhoanThu1;

    @FXML
    private Label labelKetQuaHienThi;

    @FXML
    private Label labelKetQuaHienThiCuDan;

    @FXML
    private Label labelKetQuaHienThiKhoanThu;

    @FXML
    private Label labelKetQuaHienThiKhoanThu1;

    @FXML
    private Label labelKhoanThuThangNay;

    @FXML
    private Label labelTongSoCan;

    @FXML
    private Label labelTongSoCuDan;

    @FXML
    private Label labelTongSoCuDan1;

    @FXML
    private PieChart pieChartKhoanThu;

    @FXML
    private ScrollPane scrollPaneCanHo;

    @FXML
    private ScrollPane scrollPaneCanHo1;

    @FXML
    private ScrollPane scrollPaneCuDan;

    @FXML
    private ScrollPane scrollPaneKhoanThu;

    @FXML
    private ScrollPane scrollPaneLichSuThu;

    @FXML
    private ScrollPane scrollPaneTaiKhoan;

    @FXML
    private TableView<?> tabelViewCanHo;

    @FXML
    private TableView<?> tabelViewCuDan;

    @FXML
    private TableView<?> tabelViewKhoanThu;

    @FXML
    private TableView<?> tabelViewTaiKhoan;

    @FXML
    private TableView<?> tabelViewThuPhi;

    @FXML
    private TableColumn<?, ?> tableColumnBoPhanQuanLy;

    @FXML
    private TableColumn<?, ?> tableColumnChuSoHuu;

    @FXML
    private TableColumn<?, ?> tableColumnDienTich;

    @FXML
    private TableColumn<?, ?> tableColumnEmail;

    @FXML
    private TableColumn<?, ?> tableColumnGioiTinh;

    @FXML
    private TableColumn<?, ?> tableColumnHoVaTen;

    @FXML
    private TableColumn<?, ?> tableColumnHoVaTenTaiKhoan;

    @FXML
    private TableColumn<?, ?> tableColumnKiThuat;

    @FXML
    private TableColumn<?, ?> tableColumnLoaiKhoanThu;

    @FXML
    private TableColumn<?, ?> tableColumnLoaiKhoanThuThuPhi;

    @FXML
    private TableColumn<?, ?> tableColumnMaCanHo;

    @FXML
    private TableColumn<?, ?> tableColumnMaCanHoCuDan;

    @FXML
    private TableColumn<?, ?> tableColumnMaCanHoThuPhi;

    @FXML
    private TableColumn<?, ?> tableColumnMaDinhDanh;

    @FXML
    private TableColumn<?, ?> tableColumnMaHoaDon;

    @FXML
    private TableColumn<?, ?> tableColumnMaKhoanThu;

    @FXML
    private TableColumn<?, ?> tableColumnNgayCapNhat;

    @FXML
    private TableColumn<?, ?> tableColumnNgayChuyenDen;

    @FXML
    private TableColumn<?, ?> tableColumnSoDienThoai;

    @FXML
    private TableColumn<?, ?> tableColumnNgayNop;

    @FXML
    private TableColumn<?, ?> tableColumnNgaySinh;

    @FXML
    private TableColumn<?, ?> tableColumnNgayTaoTK;

    @FXML
    private TableColumn<?, ?> tableColumnNgayTao1;

    @FXML
    private TableColumn<?, ?> tableColumnSoHuu;

    @FXML
    private TableColumn<?, ?> tableColumnSoNha;

    @FXML
    private TableColumn<?, ?> tableColumnSoTien;

    @FXML
    private TableColumn<?, ?> tableColumnSuDung;

    @FXML
    private TableColumn<?, ?> tableColumnTang;

    @FXML
    private TableColumn<?, ?> tableColumnTenKhoanThu;

    @FXML
    private TableColumn<?, ?> tableColumnTenKhoanThuThuPhi;

    @FXML
    private TableColumn<?, ?> tableColumnThaoTacLichSuThu;

    @FXML
    private TableColumn<?, ?> tableColumnThaoTacTaiKhoan;

    @FXML
    private TableColumn<?, ?> tableColumnToaNha;

    @FXML
    private TableColumn<?, ?> tableColumnTrangThai;

    @FXML
    private TableColumn<?, ?> tableColumnTrangThaiCuDan;

    @FXML
    private TableColumn<?, ?> tableColumnTrangThaiHoaDon;

    @FXML
    private TableColumn<?, ?> tableColumnTrangThaiTaiKhoan;

    @FXML
    private TableColumn<?, ?> tableColumnVaiTro;

    @FXML
    private TableColumn<?, ?> tableColumnThaoTacCuDan;

    @FXML
    private TextField textFieldBoPhanQuanLy;

    @FXML
    private TextField textFieldChuSoHuu;

    @FXML
    private TextField textFieldSoNha;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldEmailHoSo;

    @FXML
    private TextField textFieldHoVaTen;

    @FXML
    private TextField textFieldHoVaTenHoSo;

    @FXML
    private TextField textFieldHoVaTenTaiKhoan;

    @FXML
    private TextField textFieldMaCanHo;

    @FXML
    private TextField textFieldMaCanHoCuDan;

    @FXML
    private TextField textFieldMaCanHoThuPhi;

    @FXML
    private TextField textFieldMaDinhDanh;

    @FXML
    private TextField textFieldMaHoaDon;

    @FXML
    private TextField textFieldMaKhoanThu;

    @FXML
    private TextField textFieldTenKhoanThu;

    @FXML
    private TextField textFieldTenKhoanThu1;

    @FXML
    private Label hoTenLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label vaiTroLabel;

    @FXML
    private Button buttonPreviousPageCuDan;

    @FXML
    private Button buttonNextPageCuDan;

    @FXML
    private Label labelCurrentPageCuDan;

    @Autowired
    private CanHoService canHoService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QuanLyTaiKhoanService taiKhoanService;

    @Autowired
    private io.github.ktpm.bluemoonmanagement.service.cuDan.CuDanService cuDanService;
    
    @Autowired
    private io.github.ktpm.bluemoonmanagement.service.cache.CacheDataService cacheDataService;
    
    @Autowired
    private io.github.ktpm.bluemoonmanagement.service.khoanThu.KhoanThuService khoanThuService;

    private List<Node> allPanes;
    private KhungController parentController;

    private ObservableList<CanHoTableData> canHoList;
    private ObservableList<CanHoTableData> filteredList;
    
    private ObservableList<CuDanTableData> cuDanList;
    private ObservableList<CuDanTableData> filteredCuDanList;

    private ObservableList<TaiKhoanTableData> taiKhoanList;
    private ObservableList<TaiKhoanTableData> filteredTaiKhoanList;
    
    private ObservableList<KhoanThuTableData> khoanThuList;
    private ObservableList<KhoanThuTableData> filteredKhoanThuList;

    // Pagination variables
    private int currentPageCuDan = 1;
    private int itemsPerPageCuDan = 10;
    private int totalPagesCuDan = 1;
    private List<CuDanTableData> allCuDanData;

    // Reference to table view for CanHo được inject từ FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Controller initialization completed
        
        allPanes = List.of(gridPaneTrangChu, scrollPaneCanHo, scrollPaneCuDan, scrollPaneTaiKhoan, scrollPaneKhoanThu, scrollPaneLichSuThu, scrollPaneCanHo1);
        
        // Initialize data lists
        canHoList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();
        cuDanList = FXCollections.observableArrayList();
        filteredCuDanList = FXCollections.observableArrayList();
        
        // Setup ComboBoxes
        setupComboBoxes();
        
        // Setup tables
        setupCanHoTable();
        setupCuDanTable();
        setupTaiKhoanTable();
        setupKhoanThuTable(); // Setup fee table
        
        // Setup right-click refresh functionality with new method names
        setupRightClickRefresh();
        
        // Load data
        loadData();
        loadCuDanData();
        loadTaiKhoanData();
        loadKhoanThuData(); // Load fee data as well
        
        // Cập nhật tổng số liệu sau khi load data
        updateTotalStatistics();
        
        // Show default tab
        show("TrangChu");
        
        // Setup user info labels
        if (Session.getCurrentUser() != null) {
            hoTenLabel.setText("Họ Tên: " + Session.getCurrentUser().getHoTen());
            emailLabel.setText("Email: " + Session.getCurrentUser().getEmail());
            vaiTroLabel.setText("Vai trò: " + Session.getCurrentUser().getVaiTro());
        }
        
        // Setup button permissions based on user role
        setupButtonPermissions();
        
        // Setup search listeners
        setupSearchListeners();
        
        // Home_list initialization completed
    }

    /**
     * Thiết lập các ComboBox
     */
    private void setupComboBoxes() {
        // Thiết lập ComboBox trạng thái căn hộ
        if (comboBoxTrangThai != null) {
            @SuppressWarnings("unchecked")
            ComboBox<String> trangThaiCombo = (ComboBox<String>) comboBoxTrangThai;
            trangThaiCombo.setItems(javafx.collections.FXCollections.observableArrayList(
                "Tất cả", "Sử dụng", "Trống"
            ));
            trangThaiCombo.setValue("Tất cả");
        }
        
        // Thiết lập ComboBox trạng thái cư dân
        if (comboBoxTrangThaiCuDan != null) {
            @SuppressWarnings("unchecked")
            ComboBox<String> trangThaiCuDanCombo = (ComboBox<String>) comboBoxTrangThaiCuDan;
            trangThaiCuDanCombo.setItems(javafx.collections.FXCollections.observableArrayList(
                "Tất cả", "Cư trú", "Không cư trú", "Đã chuyển đi"
            ));
            trangThaiCuDanCombo.setValue("Tất cả");
        }
        
        // Thiết lập ComboBox loại khoản thu
        if (comboBoxLoaiKhoanThu != null) {
            @SuppressWarnings("unchecked")
            ComboBox<String> loaiKhoanThuCombo = (ComboBox<String>) comboBoxLoaiKhoanThu;
            loaiKhoanThuCombo.setItems(javafx.collections.FXCollections.observableArrayList(
                "Tất cả", "Bắt buộc", "Tự nguyện"
            ));
            loaiKhoanThuCombo.setValue("Tất cả");
        }
        
        // Thiết lập ComboBox trạng thái hóa đơn
        if (comboBoxTrangThaiHoaDon != null) {
            @SuppressWarnings("unchecked")
            ComboBox<String> trangThaiHoaDonCombo = (ComboBox<String>) comboBoxTrangThaiHoaDon;
            trangThaiHoaDonCombo.setItems(javafx.collections.FXCollections.observableArrayList(
                "Tất cả", "Đã thanh toán", "Chưa thanh toán"
            ));
            trangThaiHoaDonCombo.setValue("Tất cả");
        }
        
        // Thiết lập ComboBox trạng thái tài khoản
        if (comboBoxTrangThaiTaiKhoan != null) {
            @SuppressWarnings("unchecked")
            ComboBox<String> trangThaiTaiKhoanCombo = (ComboBox<String>) comboBoxTrangThaiTaiKhoan;
            trangThaiTaiKhoanCombo.setItems(javafx.collections.FXCollections.observableArrayList(
                "Tất cả", "Hoạt động", "Không hoạt động"
            ));
            trangThaiTaiKhoanCombo.setValue("Tất cả");
        }
    }

    public void setParentController(KhungController controller) {
        this.parentController = controller;
    }

    public void show(String key) {
        for (Node pane : allPanes) {
            pane.setVisible(false);
        }

        switch (key) {
            case "TrangChu" -> {
                gridPaneTrangChu.setVisible(true);
                // Refresh data khi quay về trang chủ
                System.out.println("🔄 Refreshing data when returning to homepage...");
                refreshAllDataForHomepage();
            }
            case "CanHo" -> scrollPaneCanHo.setVisible(true);
            case "CuDan" -> scrollPaneCuDan.setVisible(true);
            case "KhoanThu" -> scrollPaneKhoanThu.setVisible(true);
            case "LichSuThu" -> scrollPaneLichSuThu.setVisible(true);
            case "TaiKhoan" -> scrollPaneTaiKhoan.setVisible(true);
            case "HoSo" -> scrollPaneCanHo1.setVisible(true);
        }
    }
    @FXML
    void goToCanHo(ActionEvent event) {
        show("CanHo");
        if (parentController != null) {
            parentController.updateScreenLabel("Danh sách căn hộ");
        }
        
        // Auto-refresh apartment data when entering tab
        System.out.println("🔄 Auto-refreshing apartment data on tab switch...");
        refreshApartmentData();
        setupCanHoTable();
    }

    @FXML
    void goToHoSo(ActionEvent event) {
        show("HoSo");
        parentController.updateScreenLabel("HoSo");
    }

    @FXML
    void gotoCuDan(ActionEvent event) {
        show("CuDan");
        if (parentController != null) {
            parentController.updateScreenLabel("Danh sách cư dân");
        }
        
        // Auto-refresh resident data when entering tab
        System.out.println("🔄 Auto-refreshing resident data on tab switch...");
        refreshCuDanData();
        setupCuDanTable();
    }

    @FXML
    void gotoKhoanThu(ActionEvent event) {
        show("KhoanThu");
        if (parentController != null) {
            parentController.updateScreenLabel("Danh sách khoản thu");
        }
        
        // Auto-refresh fee data when entering tab
        System.out.println("🔄 Auto-refreshing fee data on tab switch...");
        refreshKhoanThuData();
        setupKhoanThuTable();
    }

    @FXML
    void gotoTaiKhoan(ActionEvent event) {
        show("TaiKhoan");
        if (parentController != null) {
            parentController.updateScreenLabel("Danh sách tài khoản");
        }
        
        // Auto-refresh account data when entering tab
        System.out.println("🔄 Auto-refreshing account data on tab switch...");
        refreshTaiKhoanData();
        setupTaiKhoanTable();
    }
    @FXML
    private void gotothemcanho(ActionEvent event) {
        try {
            // Opening apartment form
            
            // Sử dụng FXMLLoader thông thường cho JavaFX modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/them_can_ho.fxml"));
            
            // Load view
            Parent root = loader.load();
            
            // Get controller từ FXML
            ThemCanHoButton controller = loader.getController();
            
            // Inject service và ApplicationContext sau khi load
            if (controller != null) {
                if (canHoService != null) {
                    controller.setCanHoService(canHoService);
                }
                if (applicationContext != null) {
                    controller.setApplicationContext(applicationContext);
                    // ApplicationContext injected to form controller
                }
            }
            
            // Tạo modal dialog
            Stage dialogStage = new Stage();
            
            // Thiết lập style để bỏ khung viền cửa sổ (undecorated)
            dialogStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            
            // Thiết lập modal
            dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialogStage.initOwner(buttonThemCanHo.getScene().getWindow());
            
            // Thiết lập scene với kích thước phù hợp
            Scene scene = new Scene(root, 750, 650);
            dialogStage.setScene(scene);
            
            // Thiết lập kích thước cửa sổ
            dialogStage.setMinWidth(700);
            dialogStage.setMinHeight(600);
            dialogStage.setMaxWidth(800);
            dialogStage.setMaxHeight(700);
            dialogStage.setResizable(false); // Tắt resize vì không có khung cửa sổ
            
            // Căn giữa cửa sổ
            dialogStage.centerOnScreen();
            
            // Hiển thị dialog và chờ đóng
            dialogStage.showAndWait();
            
            // Sau khi đóng form thêm căn hộ, reload dữ liệu để cập nhật danh sách
            // Form closed, reloading data
            refreshApartmentData();
            
        } catch (Exception e) {
            showError("Lỗi khi mở form thêm căn hộ", "Chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Refresh apartment data after add/edit/delete operations
     */
    public void refreshApartmentData() {
        // Clear cache to ensure fresh data
        if (cacheDataService != null) {
            cacheDataService.refreshCacheData();
        }
        
        // Reload data from database
        loadData();
    }
    
    /**
     * Loads data from cache (no database refresh)
     */
    private void loadDataFromCache() {
        try {
            if (cacheDataService != null) {
                // Loading apartment data from cache
                List<CanHoDto> canHoDtoList = canHoService.getAllCanHo();
                canHoList = FXCollections.observableArrayList();
                
                if (canHoDtoList != null) {
                    for (CanHoDto dto : canHoDtoList) {
                        // Tạm thời hiển thị tất cả căn hộ (tắt logic ẩn căn hộ)
                        if (true || shouldShowApartmentFromCache(dto)) {
                            String chuHoName = dto.getChuHo() != null ? dto.getChuHo().getHoVaTen() : "";
                            // Cột "Sử dụng" chỉ hiển thị "Trống" hoặc "Đang sử dụng"
                            String trangThaiSuDung = dto.getTrangThaiSuDung(); // Không hiển thị "Đã bán" ở cột này
                            // Cột "Tình trạng" hiển thị trạng thái bán
                            String trangThaiBan = dto.isDaBanChua() ? "Đã bán" : "Chưa bán";
                            CanHoTableData tableData = new CanHoTableData(
                                dto.getMaCanHo(),
                                dto.getToaNha(),
                                dto.getTang(),
                                dto.getSoNha(),
                                dto.getDienTich() + " m²",
                                chuHoName,
                                trangThaiSuDung,
                                dto.getTrangThaiKiThuat(),
                                trangThaiBan
                            );
                            canHoList.add(tableData);
                        }
                    }
                }
                
                filteredList = FXCollections.observableArrayList(canHoList);
                if (tabelViewCanHo != null) {
                    ((TableView<CanHoTableData>) tabelViewCanHo).setItems(filteredList);
                }
                updateKetQuaLabel();
                // Successfully loaded apartments from cache
            } else {
                // Cache service not available, falling back to database load
                loadData();
            }
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load data from cache: " + e.getMessage());
            // Fallback to database load
            loadData();
        }
    }

    /**
     * Reloads data for all tables and views
     */
    private void loadData() {
        try {
            if (canHoService != null) {
                List<CanHoDto> canHoDtoList = canHoService.getAllCanHo();
                System.out.println("=== DEBUG: loadData() called ===");
                System.out.println("DEBUG: canHoService available, got " + (canHoDtoList != null ? canHoDtoList.size() : 0) + " apartments from service");
                canHoList = FXCollections.observableArrayList();
                
                if (canHoDtoList != null) {
                    for (CanHoDto dto : canHoDtoList) {
                        // Tạm thời hiển thị tất cả căn hộ (tắt logic ẩn căn hộ)
                        if (true || shouldShowApartment(dto)) {
                            String chuHoName = dto.getChuHo() != null ? dto.getChuHo().getHoVaTen() : "";
                            // Cột "Sử dụng" chỉ hiển thị "Trống" hoặc "Đang sử dụng"
                            String trangThaiSuDung = dto.getTrangThaiSuDung(); // Không hiển thị "Đã bán" ở cột này
                            // Cột "Tình trạng" hiển thị trạng thái bán
                            String trangThaiBan = dto.isDaBanChua() ? "Đã bán" : "Chưa bán";
                            CanHoTableData tableData = new CanHoTableData(
                                dto.getMaCanHo(),
                                dto.getToaNha(),
                                dto.getTang(),
                                dto.getSoNha(),
                                dto.getDienTich() + " m²",
                                chuHoName,
                                trangThaiSuDung,
                                dto.getTrangThaiKiThuat(),
                                trangThaiBan
                            );
                            canHoList.add(tableData);
                        }
                    }
                }
                
                filteredList = FXCollections.observableArrayList(canHoList);
                if (tabelViewCanHo != null) {
                    ((TableView<CanHoTableData>) tabelViewCanHo).setItems(filteredList);
                    System.out.println("DEBUG: Set " + filteredList.size() + " apartments to table view");
                } else {
                    System.err.println("ERROR: tabelViewCanHo is null!");
                }
                updateKetQuaLabel();
                System.out.println("=== DEBUG: loadData() completed with " + canHoList.size() + " apartments ===");
            } else {
                System.err.println("ERROR: canHoService is null! Using sample data");
                // Fallback to sample data nếu service chưa có
                loadSampleData();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải dữ liệu từ service: " + e.getMessage());
            // Fallback to sample data
            loadSampleData();
        }
    }
    
    /**
     * Kiểm tra xem căn hộ có nên hiển thị trong danh sách hay không (sử dụng cache)
     * Ẩn những căn hộ có tất cả cư dân đã chuyển đi
     */
    private boolean shouldShowApartmentFromCache(io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto dto) {
        try {
            // Sử dụng cache service thay vì gọi database
            io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto chiTiet = cacheDataService.getCanHoChiTietFromCache(dto.getMaCanHo());
            
            if (chiTiet == null || chiTiet.getCuDanList() == null || chiTiet.getCuDanList().isEmpty()) {
                // Nếu không có cư dân thì vẫn hiển thị (căn hộ trống)
                return true;
            }
            
            // Kiểm tra xem có cư dân nào còn đang ở không
            for (io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanTrongCanHoDto cuDan : chiTiet.getCuDanList()) {
                String trangThai = cuDan.getTrangThaiCuTru();
                // Nếu có ít nhất 1 cư dân chưa chuyển đi thì hiển thị căn hộ
                if (trangThai != null && !trangThai.equals("Đã chuyển đi")) {
                    return true;
                }
            }
            
            // Tất cả cư dân đều đã chuyển đi thì ẩn căn hộ
            System.out.println("DEBUG: Hiding apartment " + dto.getMaCanHo() + " - all residents moved out (from cache)");
            return false;
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot check apartment residents from cache for " + dto.getMaCanHo() + ": " + e.getMessage());
            // Nếu cache có lỗi thì fallback về method cũ
            return shouldShowApartment(dto);
        }
    }

    /**
     * Kiểm tra xem căn hộ có nên hiển thị trong danh sách hay không
     * Ẩn những căn hộ có tất cả cư dân đã chuyển đi
     */
    private boolean shouldShowApartment(io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto dto) {
        try {
            // Lấy thông tin chi tiết căn hộ để kiểm tra cư dân
            io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto chiTiet = canHoService.getCanHoChiTiet(dto);
            
            if (chiTiet == null || chiTiet.getCuDanList() == null || chiTiet.getCuDanList().isEmpty()) {
                // Nếu không có cư dân thì vẫn hiển thị (căn hộ trống)
                return true;
            }
            
            // Kiểm tra xem có cư dân nào còn đang ở không
            for (io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanTrongCanHoDto cuDan : chiTiet.getCuDanList()) {
                String trangThai = cuDan.getTrangThaiCuTru();
                // Nếu có ít nhất 1 cư dân chưa chuyển đi thì hiển thị căn hộ
                if (trangThai != null && !trangThai.equals("Đã chuyển đi")) {
                    return true;
                }
            }
            
            // Tất cả cư dân đều đã chuyển đi thì ẩn căn hộ
            System.out.println("DEBUG: Hiding apartment " + dto.getMaCanHo() + " - all residents moved out");
            return false;
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot check apartment residents for " + dto.getMaCanHo() + ": " + e.getMessage());
            // Nếu có lỗi thì vẫn hiển thị để tránh mất dữ liệu
            return true;
        }
    }

    /**
     * Load dữ liệu mẫu khi service chưa sẵn sàng
     */
    private void loadSampleData() {
        canHoList = FXCollections.observableArrayList();
        
        filteredList = FXCollections.observableArrayList(canHoList);
        if (tabelViewCanHo != null) {
            ((TableView<CanHoTableData>) tabelViewCanHo).setItems(filteredList);
        }
        updateKetQuaLabel();
    }

    /**
     * Cập nhật label kết quả
     */
    private void updateKetQuaLabel() {
        int total = filteredList != null ? filteredList.size() : 0;
        if (labelKetQuaHienThi != null) {
            labelKetQuaHienThi.setText(String.format("Hiển thị 1 - %d trên tổng số %d căn hộ", total, total));
        }
    }
    
    /**
     * Shows error dialog to user
     */
    private void showError(String title, String message) {
        ThongBaoController.showError(title != null ? title : "Lỗi", message);
    }
    
    private void showSuccess(String title, String message) {
        ThongBaoController.showSuccess(title, message);
    }

    private void showInfo(String title, String message) {
        ThongBaoController.showInfo(title, message);
    }

    /**
     * Setup table for CanHo
     */
    private void setupCanHoTable() {
        if (tabelViewCanHo != null && tableColumnMaCanHo != null) {
            // Cast table view to correct type
            TableView<CanHoTableData> typedTableView = (TableView<CanHoTableData>) tabelViewCanHo;
            
            // Setup cell value factories - need to cast to proper types
            ((TableColumn<CanHoTableData, String>) tableColumnMaCanHo).setCellValueFactory(new PropertyValueFactory<>("maCanHo"));
            ((TableColumn<CanHoTableData, String>) tableColumnToaNha).setCellValueFactory(new PropertyValueFactory<>("toaNha"));
            ((TableColumn<CanHoTableData, String>) tableColumnTang).setCellValueFactory(new PropertyValueFactory<>("tang"));
            ((TableColumn<CanHoTableData, String>) tableColumnSoNha).setCellValueFactory(new PropertyValueFactory<>("soNha"));
            ((TableColumn<CanHoTableData, String>) tableColumnDienTich).setCellValueFactory(new PropertyValueFactory<>("dienTich"));
            ((TableColumn<CanHoTableData, String>) tableColumnChuSoHuu).setCellValueFactory(new PropertyValueFactory<>("chuHo"));
            ((TableColumn<CanHoTableData, String>) tableColumnSuDung).setCellValueFactory(new PropertyValueFactory<>("trangThaiSuDung"));
            ((TableColumn<CanHoTableData, String>) tableColumnKiThuat).setCellValueFactory(new PropertyValueFactory<>("trangThaiKiThuat"));
            
            // Map cột "Tình trạng" (tableColumnSoHuu trong FXML) với dữ liệu trangThaiBan
            if (tableColumnSoHuu != null) {
                ((TableColumn<CanHoTableData, String>) tableColumnSoHuu).setCellValueFactory(new PropertyValueFactory<>("trangThaiBan"));
            }
            
            // Thêm sự kiện LEFT-click để xem chi tiết căn hộ (chỉ chuột trái)
            typedTableView.setRowFactory(tv -> {
                javafx.scene.control.TableRow<CanHoTableData> row = new javafx.scene.control.TableRow<>();
                row.setOnMouseClicked(event -> {
                    // CHỈ LEFT-CLICK để mở chi tiết (tránh conflict với right-click refresh)
                    if (!row.isEmpty() && event.getClickCount() == 1 && event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                        CanHoTableData rowData = row.getItem();
                        handleXemChiTietCanHo(rowData);
                    }
                });
                return row;
            });
        }
        
        System.out.println("CanHo table setup completed");
    }
    
    /**
     * Xử lý sự kiện xem chi tiết căn hộ
     */
    private void handleXemChiTietCanHo(CanHoTableData canHo) {
        try {
            if (canHoService != null) {
                CanHoDto canHoDto = new CanHoDto();
                canHoDto.setMaCanHo(canHo.getMaCanHo());
                
                CanHoChiTietDto chiTiet = canHoService.getCanHoChiTiet(canHoDto);
                
                if (chiTiet != null) {
                    openChiTietCanHo(chiTiet);
                } else {
                    showError("Lỗi", "Không tìm thấy thông tin chi tiết căn hộ");
                }
            } else {
                CanHoChiTietDto chiTietMau = createSampleChiTiet(canHo);
                openChiTietCanHo(chiTietMau);
            }
        } catch (Exception e) {
            showError("Lỗi khi xem chi tiết", "Chi tiết: " + e.getMessage());
        }
    }

    /**
     * Mở cửa sổ chi tiết căn hộ
     */
    private void openChiTietCanHo(CanHoChiTietDto chiTiet) {
        try {
            System.out.println("DEBUG: Starting to open chi tiet can ho for: " + chiTiet.getMaCanHo());
            
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/chi_tiet_can_ho.fxml")
            );
            System.out.println("DEBUG: FXML Loader created successfully");
            
            javafx.scene.Parent root = loader.load();
            System.out.println("DEBUG: FXML loaded successfully");
            
            ChiTietCanHoController controller = loader.getController();
            System.out.println("DEBUG: Controller obtained: " + (controller != null ? "SUCCESS" : "NULL"));
            
            // Inject ApplicationContext trước tiên
            if (applicationContext != null) {
                System.out.println("DEBUG: Injecting ApplicationContext from Home_list to ChiTietCanHoController");
                controller.setApplicationContext(applicationContext);
            } else {
                System.err.println("ERROR: ApplicationContext is null in Home_list!");
            }
            
            // Inject CanHoService
            if (canHoService != null) {
                controller.setCanHoService(canHoService);
            }
            
            // Inject PhuongTienService từ ApplicationContext
            try {
                if (applicationContext != null) {
                    io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService phuongTienService = 
                        applicationContext.getBean(io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService.class);
                    controller.setPhuongTienService(phuongTienService);
                    System.out.println("DEBUG: Injected PhuongTienService from Home_list to ChiTietCanHoController");
                }
            } catch (Exception e) {
                System.err.println("ERROR: Cannot get PhuongTienService from ApplicationContext in Home_list: " + e.getMessage());
            }
            
            // Set data sau khi đã inject services
            System.out.println("DEBUG: Setting CanHo data to controller");
            controller.setCanHoData(chiTiet);
            System.out.println("DEBUG: CanHo data set successfully");
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Chi tiết căn hộ - " + chiTiet.getMaCanHo());
            stage.setScene(new javafx.scene.Scene(root, 1000, 700));
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            stage.initOwner(tabelViewCanHo.getScene().getWindow());
            System.out.println("DEBUG: About to show stage");
            stage.show();
            System.out.println("DEBUG: Stage shown successfully");
        } catch (Exception e) {
            System.err.println("ERROR: Exception in openChiTietCanHo: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi mở chi tiết", "Không thể mở trang chi tiết căn hộ: " + e.getMessage());
        }
    }

    /**
     * Tạo dữ liệu mẫu cho chi tiết căn hộ
     */
    private CanHoChiTietDto createSampleChiTiet(CanHoTableData canHo) {
        try {
            CanHoChiTietDto chiTiet = new CanHoChiTietDto();
            chiTiet.setMaCanHo(canHo.getMaCanHo());
            chiTiet.setToaNha(canHo.getToaNha());
            chiTiet.setTang(canHo.getTang());
            chiTiet.setSoNha(canHo.getSoNha());
            
            // Safe parsing of area
            try {
                String dienTichStr = canHo.getDienTich();
                if (dienTichStr != null) {
                    dienTichStr = dienTichStr.replace(" m²", "").replace("m²", "").trim();
                    chiTiet.setDienTich(Double.parseDouble(dienTichStr));
                } else {
                    chiTiet.setDienTich(0.0);
                }
            } catch (NumberFormatException e) {
                System.err.println("Cannot parse area: " + canHo.getDienTich() + ", using default 0.0");
                chiTiet.setDienTich(0.0);
            }
            
            chiTiet.setTrangThaiKiThuat(canHo.getTrangThaiKiThuat());
            chiTiet.setTrangThaiSuDung(canHo.getTrangThaiSuDung());
            
            // Set default status
            chiTiet.setDaBanChua(false); // Default to not sold
            
            return chiTiet;
        } catch (Exception e) {
            System.err.println("Error creating sample CanHoChiTietDto: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Setter for dependency injection
    public void setCanHoService(CanHoService canHoService) {
        this.canHoService = canHoService;
        System.out.println("CanHoService đã được set thủ công: " + (canHoService != null));
    }

    /**
     * Method để inject services từ parent controller nếu Spring DI không hoạt động
     */
    public void injectServices(CanHoService canHoService) {
        if (this.canHoService == null && canHoService != null) {
            this.canHoService = canHoService;
            System.out.println("Đã inject CanHoService thủ công từ parent controller");
        }
    }
    
    /**
     * Method để inject ApplicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        System.out.println("DEBUG: Đã inject ApplicationContext vào Home_list");
    }

    /**
     * Inner class để hiển thị dữ liệu trong table
     */
    public static class CanHoTableData {
        private String maCanHo;
        private String toaNha;
        private String tang;
        private String soNha;
        private String dienTich;
        private String chuHo;
        private String trangThaiSuDung;
        private String trangThaiKiThuat;
        private String trangThaiBan; // Thêm trường trạng thái bán

        public CanHoTableData(String maCanHo, String toaNha, String tang, String soNha, 
                            String dienTich, String chuHo, String trangThaiSuDung, String trangThaiKiThuat, String trangThaiBan) {
            this.maCanHo = maCanHo;
            this.toaNha = toaNha;
            this.tang = tang;
            this.soNha = soNha;
            this.dienTich = dienTich;
            this.chuHo = chuHo;
            this.trangThaiSuDung = trangThaiSuDung;
            this.trangThaiKiThuat = trangThaiKiThuat;
            this.trangThaiBan = trangThaiBan;
        }

        // Getters
        public String getMaCanHo() { return maCanHo; }
        public String getToaNha() { return toaNha; }
        public String getTang() { return tang; }
        public String getSoNha() { return soNha; }
        public String getDienTich() { return dienTich; }
        public String getChuHo() { return chuHo; }
        public String getTrangThaiSuDung() { return trangThaiSuDung; }
        public String getTrangThaiKiThuat() { return trangThaiKiThuat; }
        public String getTrangThaiBan() { return trangThaiBan; }

        // Setters
        public void setMaCanHo(String maCanHo) { this.maCanHo = maCanHo; }
        public void setToaNha(String toaNha) { this.toaNha = toaNha; }
        public void setTang(String tang) { this.tang = tang; }
        public void setSoNha(String soNha) { this.soNha = soNha; }
        public void setDienTich(String dienTich) { this.dienTich = dienTich; }
        public void setChuHo(String chuHo) { this.chuHo = chuHo; }
        public void setTrangThaiSuDung(String trangThaiSuDung) { this.trangThaiSuDung = trangThaiSuDung; }
        public void setTrangThaiKiThuat(String trangThaiKiThuat) { this.trangThaiKiThuat = trangThaiKiThuat; }
        public void setTrangThaiBan(String trangThaiBan) { this.trangThaiBan = trangThaiBan; }
    }

    /**
     * Inner class để hiển thị dữ liệu cư dân trong table
     */
    public static class CuDanTableData {
        private String maDinhDanh;
        private String hoVaTen;
        private String gioiTinh;
        private String ngaySinh;
        private String soDienThoai;
        private String email;
        private String maCanHo;
        private String trangThaiCuTru;
        private String ngayChuyenDen;

        public CuDanTableData(String maDinhDanh, String hoVaTen, String gioiTinh, String ngaySinh,
                             String soDienThoai, String email, String maCanHo, String trangThaiCuTru, String ngayChuyenDen) {
            this.maDinhDanh = maDinhDanh;
            this.hoVaTen = hoVaTen;
            this.gioiTinh = gioiTinh;
            this.ngaySinh = ngaySinh;
            this.soDienThoai = soDienThoai;
            this.email = email;
            this.maCanHo = maCanHo;
            this.trangThaiCuTru = trangThaiCuTru;
            this.ngayChuyenDen = ngayChuyenDen;
        }

        // Getters
        public String getMaDinhDanh() { return maDinhDanh; }
        public String getHoVaTen() { return hoVaTen; }
        public String getGioiTinh() { return gioiTinh; }
        public String getNgaySinh() { return ngaySinh; }
        public String getSoDienThoai() { return soDienThoai; }
        public String getEmail() { return email; }
        public String getMaCanHo() { return maCanHo; }
        public String getTrangThaiCuTru() { return trangThaiCuTru; }
        public String getNgayChuyenDen() { return ngayChuyenDen; }

        // Setters
        public void setMaDinhDanh(String maDinhDanh) { this.maDinhDanh = maDinhDanh; }
        public void setHoVaTen(String hoVaTen) { this.hoVaTen = hoVaTen; }
        public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
        public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
        public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
        public void setEmail(String email) { this.email = email; }
        public void setMaCanHo(String maCanHo) { this.maCanHo = maCanHo; }
        public void setTrangThaiCuTru(String trangThaiCuTru) { this.trangThaiCuTru = trangThaiCuTru; }
        public void setNgayChuyenDen(String ngayChuyenDen) { this.ngayChuyenDen = ngayChuyenDen; }
    }
    
    public static class TaiKhoanTableData {
        private String email;
        private String hoVaTen;
        private String vaiTro;
        private String ngayTao;
        private String ngayCapNhat;

        public TaiKhoanTableData(String email, String hoVaTen, String vaiTro, String ngayTao, String ngayCapNhat) {
            this.email = email;
            this.hoVaTen = hoVaTen;
            this.vaiTro = vaiTro;
            this.ngayTao = ngayTao;
            this.ngayCapNhat = ngayCapNhat;
        }
        
        // Getters
        public String getEmail() { return email; }
        public String getHoVaTen() { return hoVaTen; }
        public String getVaiTro() { return vaiTro; }
        public String getNgayCapNhat() { return ngayCapNhat; }
        public String getNgayTao() { return ngayTao; }
        
        // Setters
        public void setEmail(String email) { this.email = email; }
        public void setHoVaTen(String hoVaTen) { this.hoVaTen = hoVaTen; }
        public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
        public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }
        public void setNgayCapNhat(String ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }
    }
    
    public static class KhoanThuTableData {
        private String maKhoanThu;
        private String tenKhoanThu;
        private String loaiKhoanThu;
        private String donViTinh;
        private String soTien;
        private String phamVi;
        private String ngayTao;
        private String thoiHan;
        private String ghiChu;
        
        public KhoanThuTableData(String maKhoanThu, String tenKhoanThu, String loaiKhoanThu, 
                                String donViTinh, String soTien, String phamVi, 
                                String ngayTao, String thoiHan, String ghiChu) {
            this.maKhoanThu = maKhoanThu;
            this.tenKhoanThu = tenKhoanThu;
            this.loaiKhoanThu = loaiKhoanThu;
            this.donViTinh = donViTinh;
            this.soTien = soTien;
            this.phamVi = phamVi;
            this.ngayTao = ngayTao;
            this.thoiHan = thoiHan;
            this.ghiChu = ghiChu;
        }
        
        // Getters
        public String getMaKhoanThu() { return maKhoanThu; }
        public String getTenKhoanThu() { return tenKhoanThu; }
        public String getLoaiKhoanThu() { return loaiKhoanThu; }
        public String getDonViTinh() { return donViTinh; }
        public String getSoTien() { return soTien; }
        public String getPhamVi() { return phamVi; }
        public String getNgayTao() { return ngayTao; }
        public String getThoiHan() { return thoiHan; }
        public String getGhiChu() { return ghiChu; }
        
        // Setters
        public void setMaKhoanThu(String maKhoanThu) { this.maKhoanThu = maKhoanThu; }
        public void setTenKhoanThu(String tenKhoanThu) { this.tenKhoanThu = tenKhoanThu; }
        public void setLoaiKhoanThu(String loaiKhoanThu) { this.loaiKhoanThu = loaiKhoanThu; }
        public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }
        public void setSoTien(String soTien) { this.soTien = soTien; }
        public void setPhamVi(String phamVi) { this.phamVi = phamVi; }
        public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }
        public void setThoiHan(String thoiHan) { this.thoiHan = thoiHan; }
        public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    }

    public void setupTaiKhoanTable() {
        if( tabelViewTaiKhoan != null && tableColumnEmail != null) {
            // Cast table view to correct type
            TableView<TaiKhoanTableData> typedTableView = (TableView<TaiKhoanTableData>) tabelViewTaiKhoan;

            // Setup cell value factories - need to cast to proper types
            ((TableColumn<TaiKhoanTableData, String>) tableColumnEmail).setCellValueFactory(new PropertyValueFactory<>("email"));
            ((TableColumn<TaiKhoanTableData, String>) tableColumnHoVaTenTaiKhoan).setCellValueFactory(new PropertyValueFactory<>("hoVaTen"));
            ((TableColumn<TaiKhoanTableData, String>) tableColumnVaiTro).setCellValueFactory(new PropertyValueFactory<>("vaiTro"));
            ((TableColumn<TaiKhoanTableData, String>) tableColumnNgayTaoTK).setCellValueFactory(new PropertyValueFactory<>("ngayTao"));
            ((TableColumn<TaiKhoanTableData, String>) tableColumnNgayCapNhat).setCellValueFactory(new PropertyValueFactory<>("ngayCapNhat"));



            // Thêm sự kiện double-click để mở form sửa/xóa tài khoản
            typedTableView.setRowFactory(tv -> {
                javafx.scene.control.TableRow<TaiKhoanTableData> row = new javafx.scene.control.TableRow<>();
                row.setOnMouseClicked(event -> {
                    // DOUBLE-CLICK để mở form sửa/xóa tài khoản
                    if (!row.isEmpty() && event.getClickCount() == 2 && event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                        TaiKhoanTableData rowData = row.getItem();
                        handleEditTaiKhoan(rowData);
                    }
                });
                return row;
            });
        }
    }

    private void handleXemChiTietTaiKhoan(TaiKhoanTableData rowData) {
        try {
            if (taiKhoanService != null) {

                String email = rowData.getEmail();
                String hoVaTen = rowData.getHoVaTen();
                String vaiTro = rowData.getVaiTro();

                LocalDateTime ngayTao = LocalDateTime.parse(rowData.getNgayTao());
                LocalDateTime ngayCapNhat = LocalDateTime.parse(rowData.getNgayCapNhat());

                ThongTinTaiKhoanDto taiKhoanDto = new ThongTinTaiKhoanDto(email, hoVaTen, vaiTro, ngayTao, ngayCapNhat);
                if (taiKhoanDto != null) {
                    openChiTietTaiKhoan(taiKhoanDto);
                } else {
                    showError("Lỗi", "Không tìm thấy thông tin chi tiết tài khoản");
                }
            } else {
                showError("Lỗi", "Dịch vụ quản lý tài khoản không khả dụng");
            }
        } catch (Exception e) {
            showError("Lỗi khi xem chi tiết", "Chi tiết: " + e.getMessage());
        }
    }

    private void openChiTietTaiKhoan(ThongTinTaiKhoanDto taiKhoanDto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/tai_khoan.fxml"));
            Parent root = loader.load();

            ChiTietTaiKhoanController controller = loader.getController();
            // Inject ApplicationContext trước tiên
            if (applicationContext != null) {
                System.out.println("DEBUG: Injecting ApplicationContext from Home_list to ChiTietTaiKhoanController");
                controller.setApplicationContext(applicationContext);
            } else {
                System.err.println("ERROR: ApplicationContext is null in Home_list!");
            }
            // Inject TaiKhoanService
            if (taiKhoanService != null) {
                controller.setTaiKhoanService(taiKhoanService);
            } else {
                System.err.println("ERROR: TaiKhoanService is null in Home_list!");
            }
            // Set data sau khi đã inject services
            controller.setTaiKhoanData(taiKhoanDto);
            // Tạo cửa sổ mới
            Stage stage = new Stage();
            stage.setTitle("Chi tiết tài khoản - " + taiKhoanDto.getEmail());
            stage.setScene(new Scene(root, 800, 600));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tabelViewTaiKhoan.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            showError("Lỗi mở chi tiết", "Không thể mở trang chi tiết tài khoản: " + e.getMessage());
        }
    }

    /**
     * Handle edit account action - opens edit form with edit/delete options
     */
    private void handleEditTaiKhoan(TaiKhoanTableData rowData) {
        try {
            if (taiKhoanService != null) {
                String email = rowData.getEmail();
                String hoVaTen = rowData.getHoVaTen();
                String vaiTro = rowData.getVaiTro();

                LocalDateTime ngayTao = LocalDateTime.parse(rowData.getNgayTao());
                LocalDateTime ngayCapNhat = LocalDateTime.parse(rowData.getNgayCapNhat());

                ThongTinTaiKhoanDto taiKhoanDto = new ThongTinTaiKhoanDto(email, hoVaTen, vaiTro, ngayTao, ngayCapNhat);
                if (taiKhoanDto != null) {
                    openEditTaiKhoan(taiKhoanDto);
                } else {
                    showError("Lỗi", "Không tìm thấy thông tin tài khoản");
                }
            } else {
                showError("Lỗi", "Dịch vụ quản lý tài khoản không khả dụng");
            }
        } catch (Exception e) {
            showError("Lỗi khi mở form sửa", "Chi tiết: " + e.getMessage());
        }
    }

    /**
     * Open edit account form
     */
    private void openEditTaiKhoan(ThongTinTaiKhoanDto taiKhoanDto) {
        try {
            // Sử dụng form tài khoản có sẵn với mode edit
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/tai_khoan.fxml"));
            Parent root = loader.load();

            ChiTietTaiKhoanController controller = loader.getController();
            // Inject ApplicationContext và services
            if (applicationContext != null) {
                controller.setApplicationContext(applicationContext);
            }
            if (taiKhoanService != null) {
                controller.setTaiKhoanService(taiKhoanService);
            }
            // Set data cho form
            controller.setTaiKhoanData(taiKhoanDto);
            
            // Tạo cửa sổ mới
            Stage stage = new Stage();
            stage.setTitle("Chỉnh sửa tài khoản - " + taiKhoanDto.getEmail());
            stage.setScene(new Scene(root, 800, 600));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tabelViewTaiKhoan.getScene().getWindow());
            
            // Refresh table sau khi đóng
            stage.setOnHidden(e -> refreshTaiKhoanData());
            
            stage.show();
        } catch (IOException e) {
            showError("Lỗi mở form sửa", "Không thể mở form chỉnh sửa tài khoản: " + e.getMessage());
        }
    }


    private void loadTaiKhoanData() {
        try {
            if (taiKhoanService != null) {
                List<ThongTinTaiKhoanDto> taiKhoanDtoList = taiKhoanService.layDanhSachTaiKhoan();
                taiKhoanList = FXCollections.observableArrayList();

                if (taiKhoanDtoList != null) {
                    for (ThongTinTaiKhoanDto dto : taiKhoanDtoList) {
                        TaiKhoanTableData tableData = new TaiKhoanTableData(
                            dto.getEmail(),
                            dto.getHoTen(),
                            dto.getVaiTro(),
                            dto.getNgayTao() != null ? dto.getNgayTao().toString() : "",
                            dto.getNgayCapNhat() != null ? dto.getNgayCapNhat().toString() : ""

                        );

                        taiKhoanList.add(tableData);
                    }
                }

                filteredTaiKhoanList = FXCollections.observableArrayList(taiKhoanList);
                ((TableView<TaiKhoanTableData>) tabelViewTaiKhoan).setItems(filteredTaiKhoanList);
            } else {
                System.err.println("TaiKhoanService is not available, cannot load data.");
            }
        } catch (Exception e) {
            System.err.println("Error loading TaiKhoan data: " + e.getMessage());
        }
    }

    @Autowired
    private FxViewLoader fxViewLoader;
    @FXML
    public void themTaiKhoanClick(ActionEvent event) {
        try {
            // Load view + controller
            FxView<?> fxView = fxViewLoader.loadFxView("/view/them_tai_khoan.fxml");

            // Tạo cửa sổ mới
            Stage newStage = new Stage();
            newStage.setScene(new Scene(fxView.getView()));
            newStage.setTitle("Thêm tài khoản");

            // Tuỳ chọn: không cho tương tác cửa sổ cha khi đang mở
            newStage.initModality(Modality.APPLICATION_MODAL);

            // Tuỳ chọn: gán owner là cửa sổ hiện tại (giúp bố cục và quản lý tốt hơn)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            newStage.initOwner(currentStage);

            // Hiển thị cửa sổ mới
            newStage.show();

        } catch (IOException e) {
            System.err.println("Không thể mở cửa sổ Thêm tài khoản:");
            e.printStackTrace();
        }
    }
    public void DoiMatKhauClicked(ActionEvent event){
        try {
            // Load view + controller
            FxView<?> fxView = fxViewLoader.loadFxView("/view/doi_mat_khau_ho_so.fxml");

            // Tạo cửa sổ mới
            Stage newStage = new Stage();
            newStage.setScene(new Scene(fxView.getView()));
            newStage.setTitle("Đổi mật khẩu");

            // Tuỳ chọn: không cho tương tác cửa sổ cha khi đang mở
            newStage.initModality(Modality.APPLICATION_MODAL);

            // Tuỳ chọn: gán owner là cửa sổ hiện tại (giúp bố cục và quản lý tốt hơn)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            newStage.initOwner(currentStage);

            // Hiển thị cửa sổ mới
            newStage.show();

        } catch (IOException e) {
            System.err.println("Không thể mở cửa sổ Đổi mật khẩu:");
            e.printStackTrace();
        }
    }
    public void ThemKhoanThuClicked(ActionEvent event) {
        try {
            // Load view + controller
            FxView<?> fxView = fxViewLoader.loadFxView("/view/khoan_thu.fxml");

            // Tạo cửa sổ mới
            Stage newStage = new Stage();
            newStage.setScene(new Scene(fxView.getView()));
            newStage.setTitle("Thêm khoản thu");

            // Tuỳ chọn: không cho tương tác cửa sổ cha khi đang mở
            newStage.initModality(Modality.APPLICATION_MODAL);

            // Tuỳ chọn: gán owner là cửa sổ hiện tại (giúp bố cục và quản lý tốt hơn)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            newStage.initOwner(currentStage);

            // Hiển thị cửa sổ mới
            newStage.show();

        } catch (IOException e) {
            System.err.println("Không thể mở cửa sổ Thêm khoản thu:");
            e.printStackTrace();
        }
    }

    @FXML
    public void themCuDanClicked(ActionEvent event) {
        try {
            System.out.println("Mở form thêm cư dân...");
            
            // Load view + controller using FxViewLoader
            FxView<?> fxView = fxViewLoader.loadFxView("/view/cu_dan.fxml");

            // Tạo cửa sổ mới
            Stage newStage = new Stage();
            newStage.setScene(new Scene(fxView.getView()));
            newStage.setTitle("Thêm cư dân");

            // Thiết lập modal
            newStage.initModality(Modality.APPLICATION_MODAL);

            // Gán owner là cửa sổ hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            newStage.initOwner(currentStage);

            // Thiết lập kích thước cửa sổ
            newStage.setMinWidth(700);
            newStage.setMinHeight(600);
            newStage.setResizable(true);

            // Hiển thị cửa sổ mới
            newStage.show();

        } catch (IOException e) {
            System.err.println("Không thể mở cửa sổ Thêm cư dân:");
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form thêm cư dân: " + e.getMessage());
        }
    }

    /**
     * Setup table for CuDan
     */
    private void setupCuDanTable() {
        if (tabelViewCuDan != null) {
            // Cast table view to correct type
            TableView<CuDanTableData> typedTableView = (TableView<CuDanTableData>) tabelViewCuDan;
            
            // Setup cell value factories
            ((TableColumn<CuDanTableData, String>) tableColumnMaDinhDanh).setCellValueFactory(new PropertyValueFactory<>("maDinhDanh"));
            ((TableColumn<CuDanTableData, String>) tableColumnHoVaTen).setCellValueFactory(new PropertyValueFactory<>("hoVaTen"));
            ((TableColumn<CuDanTableData, String>) tableColumnGioiTinh).setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
            ((TableColumn<CuDanTableData, String>) tableColumnNgaySinh).setCellValueFactory(new PropertyValueFactory<>("ngaySinh"));
            ((TableColumn<CuDanTableData, String>) tableColumnMaCanHoCuDan).setCellValueFactory(new PropertyValueFactory<>("maCanHo"));
            ((TableColumn<CuDanTableData, String>) tableColumnSoDienThoai).setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
            ((TableColumn<CuDanTableData, String>) tableColumnNgayChuyenDen).setCellValueFactory(new PropertyValueFactory<>("ngayChuyenDen"));
            ((TableColumn<CuDanTableData, String>) tableColumnTrangThaiCuDan).setCellValueFactory(new PropertyValueFactory<>("trangThaiCuTru"));
            
            // Setup LEFT double-click event để mở popup chỉnh sửa cư dân (chỉ chuột trái)
            typedTableView.setRowFactory(tv -> {
                javafx.scene.control.TableRow<CuDanTableData> row = new javafx.scene.control.TableRow<>();
                row.setOnMouseClicked(event -> {
                    // CHỈ LEFT DOUBLE-CLICK để mở chỉnh sửa (tránh conflict với right-click refresh)
                    if (event.getClickCount() == 2 && !row.isEmpty() && event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                        CuDanTableData rowData = row.getItem();
                        handleEditCuDan(rowData);
                    }
                });
                return row;
            });
            
            // Setup scroll bars - enable vertical scrolling, disable horizontal scrolling
            javafx.application.Platform.runLater(() -> {
                for (javafx.scene.Node node : typedTableView.lookupAll(".scroll-bar")) {
                    if (node instanceof javafx.scene.control.ScrollBar) {
                        javafx.scene.control.ScrollBar scrollBar = (javafx.scene.control.ScrollBar) node;
                        if (scrollBar.getOrientation() == javafx.geometry.Orientation.HORIZONTAL) {
                            // Ẩn scroll bar ngang
                            scrollBar.setVisible(false);
                            scrollBar.setDisable(true);
                        } else {
                            // Giữ scroll bar dọc và cho phép cuộn
                            scrollBar.setVisible(true);
                            scrollBar.setDisable(false);
                        }
                    }
                }
            });
            
            // Thiết lập chiều rộng (không cố định chiều cao để cho phép dynamic sizing)
            typedTableView.setPrefWidth(970);
            typedTableView.setMaxWidth(970);
            
            // Cho phép cuộn bằng phím mũi tên và chuột
            typedTableView.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.UP || 
                    event.getCode() == javafx.scene.input.KeyCode.DOWN ||
                    event.getCode() == javafx.scene.input.KeyCode.PAGE_UP ||
                    event.getCode() == javafx.scene.input.KeyCode.PAGE_DOWN) {
                    // Các phím này sẽ tự động cuộn TableView
                }
            });
            
            // Thêm tooltip hướng dẫn cho bảng khoản thu
            javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                "Double-click để xem/chỉnh sửa khoản thu\n" +
                "• Kế toán: Có thể chỉnh sửa và tạo hóa đơn\n" +
                "• Vai trò khác: Chỉ có thể xem chi tiết\n" +
                "Right-click để refresh dữ liệu");
            javafx.scene.control.Tooltip.install(typedTableView, tooltip);
            
            // Đảm bảo TableView có thể focus để nhận sự kiện phím
            typedTableView.setFocusTraversable(true);
            
            System.out.println("CuDan table setup completed");
        } else {
            System.out.println("WARNING: tabelViewCuDan is null");
        }
    }
    
    /**
     * Xử lý chỉnh sửa cư dân
     */
    private void handleEditCuDan(CuDanTableData cuDan) {
        try {
            // Kiểm tra quyền
            if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
                showError("Lỗi quyền", "Bạn không có quyền chỉnh sửa cư dân.\nChỉ người dùng có vai trò 'Tổ phó' mới được phép thực hiện thao tác này.");
                return;
            }
            
            System.out.println("Mở form chỉnh sửa cư dân: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ")");
            
            // Load view + controller using FxViewLoader
            FxView<?> fxView = fxViewLoader.loadFxView("/view/cu_dan.fxml");
            
            // Get controller and setup edit mode
            Object controller = fxView.getController();
            if (controller instanceof io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController) {
                io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController cuDanController = 
                    (io.github.ktpm.bluemoonmanagement.controller.ThemCuDanController) controller;
                
                // Set ApplicationContext for controller
                cuDanController.setApplicationContext(applicationContext);
                
                // Setup edit mode với dữ liệu cư dân hiện tại (CuDanTableData)
                cuDanController.setupEditMode(cuDan);
            }

            // Tạo cửa sổ mới
            Stage newStage = new Stage();
            newStage.setScene(new Scene(fxView.getView()));
            newStage.setTitle("Chỉnh sửa cư dân - " + cuDan.getHoVaTen());

            // Thiết lập modal
            newStage.initModality(Modality.APPLICATION_MODAL);

            // Gán owner là cửa sổ hiện tại
            Stage currentStage = (Stage) scrollPaneCuDan.getScene().getWindow();
            newStage.initOwner(currentStage);

            // Thiết lập kích thước cửa sổ
            newStage.setMinWidth(700);
            newStage.setMinHeight(600);
            newStage.setResizable(true);

            // Hiển thị cửa sổ mới và chờ đóng
            newStage.showAndWait();
            
            // Reload dữ liệu sau khi đóng form chỉnh sửa
            System.out.println("Form chỉnh sửa cư dân đã đóng, reload dữ liệu...");
            loadCuDanData();

        } catch (IOException e) {
            System.err.println("Không thể mở cửa sổ chỉnh sửa cư dân:");
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form chỉnh sửa cư dân: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khi xử lý chỉnh sửa cư dân: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi", "Có lỗi xảy ra khi chỉnh sửa cư dân: " + e.getMessage());
        }
    }

    /**
     * Xử lý xóa mềm cư dân
     */
    private void handleDeleteCuDan(CuDanTableData cuDan) {
        try {
            // Hiển thị dialog xác nhận tùy chỉnh
            boolean confirmed = showCustomConfirmDialog(cuDan.getHoVaTen(), cuDan.getMaDinhDanh());
            
            if (confirmed) {
                if (cuDanService != null) {
                    // Kiểm tra quyền trước khi xóa
                    if (Session.getCurrentUser() == null) {
                        showError("Lỗi quyền", "Bạn chưa đăng nhập. Vui lòng đăng nhập lại!");
                        return;
                    }

                    if (!"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
                        showError("Lỗi quyền", "Bạn không có quyền xóa cư dân.\nChỉ người dùng có vai trò 'Tổ phó' mới được phép thực hiện thao tác này.");
                        return;
                    }

                    // Gọi service để xóa mềm cư dân
                    System.out.println("DEBUG Controller: Gọi service xóa mềm với mã: " + cuDan.getMaDinhDanh());
                    boolean deleted = cuDanService.xoaMem(cuDan.getMaDinhDanh());
                    System.out.println("DEBUG Controller: Kết quả xóa: " + deleted);

                    if (deleted) {
                        showSuccess("Thành công", "Đã xóa cư dân thành công!");
                        // Reload dữ liệu để cập nhật danh sách
                        loadCuDanData();
                    } else {
                        System.err.println("DEBUG Controller: Xóa thất bại - hiển thị thông báo lỗi");
                        showError("Lỗi xóa cư dân",
                            "Không thể xóa cư dân: " + cuDan.getHoVaTen() + "\n\n" +
                            "Có thể do:\n" +
                            "• Cư dân không tồn tại trong hệ thống\n" +
                            "• Lỗi kết nối cơ sở dữ liệu\n" +
                            "• Quyền truy cập không đủ\n\n" +
                            "Vui lòng kiểm tra console để xem chi tiết lỗi.");
                    }
                } else {
                    showError("Lỗi hệ thống", "Service không khả dụng. Vui lòng liên hệ quản trị viên!");
                }
            }
        } catch (Exception e) {
            showError("Lỗi", "Có lỗi xảy ra khi xóa cư dân: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Public method to refresh residents data (called by other controllers)
     */
    public void refreshCuDanData() {
        // Clear cache and reload residents data
        if (cacheDataService != null) {
            cacheDataService.refreshCacheData();
        }
        loadCuDanData();
        System.out.println("✅ Residents data refreshed");
    }
    
    /**
     * Load dữ liệu cư dân từ service
     */
    private void loadCuDanData() {
        try {
            if (cuDanService != null) {
                List<io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto> cuDanDtoList = cuDanService.getAllCuDan();
                cuDanList.clear();
                
                System.out.println("=== DEBUG: Kiểm tra dữ liệu mã căn hộ từ database ===");
                System.out.println("Tổng số cư dân từ service: " + (cuDanDtoList != null ? cuDanDtoList.size() : 0));
                
                if (cuDanDtoList != null) {
                    for (io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto dto : cuDanDtoList) {
                        System.out.println("Cư dân: " + dto.getHoVaTen() + " (" + dto.getMaDinhDanh() + ") - Mã căn hộ: '" + dto.getMaCanHo() + "'");
                        
                        CuDanTableData tableData = new CuDanTableData(
                            dto.getMaDinhDanh(),
                            dto.getHoVaTen(),
                            dto.getGioiTinh(),
                            dto.getNgaySinh() != null ? dto.getNgaySinh().toString() : "",
                            dto.getSoDienThoai(),
                            dto.getEmail(),
                            dto.getMaCanHo() != null ? dto.getMaCanHo() : "",
                            dto.getTrangThaiCuTru(),
                            dto.getNgayChuyenDen() != null ? dto.getNgayChuyenDen().toString() : ""
                        );
                        cuDanList.add(tableData);
                    }
                }
                
                filteredCuDanList = FXCollections.observableArrayList(cuDanList);
                if (tabelViewCuDan != null) {
                    ((TableView<CuDanTableData>) tabelViewCuDan).setItems(filteredCuDanList);
                }
                updateCuDanKetQuaLabel();
                System.out.println("Loaded " + cuDanList.size() + " residents from service");
            } else {
                System.err.println("CuDanService is null");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải dữ liệu cư dân từ service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật label kết quả cho cư dân
     */
    private void updateCuDanKetQuaLabel() {
        int total = filteredCuDanList != null ? filteredCuDanList.size() : 0;
        if (labelKetQuaHienThiCuDan != null) {
            // Với chiều cao 400px, có thể hiển thị khoảng 15-16 dòng
            int visibleRows = Math.min(total, 15);
            if (total <= 15) {
                labelKetQuaHienThiCuDan.setText(String.format("Hiển thị %d/%d cư dân", total, total));
            } else {
                labelKetQuaHienThiCuDan.setText(String.format("Hiển thị %d đầu tiên / %d cư dân", visibleRows, total));
            }
        }
    }

    /**
     * Thiết lập quyền cho các nút dựa trên vai trò người dùng
     * - Tổ trưởng: Disable tất cả nút thêm
     * - Kế toán: Chỉ disable thêm căn hộ và thêm cư dân, được phép thêm khoản thu
     */
    private void setupButtonPermissions() {
        boolean isToTruong = false;
        boolean isKeToan = false;
        String vaiTro = "";

        try {
            if (Session.getCurrentUser() != null) {
                vaiTro = Session.getCurrentUser().getVaiTro();
                isToTruong = "Tổ trưởng".equals(vaiTro);
                isKeToan = "Kế toán".equals(vaiTro);
                System.out.println("DEBUG: User role = " + vaiTro + ", isToTruong = " + isToTruong + ", isKeToan = " + isKeToan);
            } else {
                System.out.println("DEBUG: Không có user hiện tại");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra vai trò người dùng: " + e.getMessage());
            isToTruong = false;
            isKeToan = false;
        }

        // Disable nút thêm cư dân cho Tổ trưởng và Kế toán
        if (buttonThemCuDan != null) {
            boolean shouldDisableCuDan = isToTruong || isKeToan;
            buttonThemCuDan.setDisable(shouldDisableCuDan);
            if (shouldDisableCuDan) {
                String reason = isToTruong ? "Tổ trưởng không có quyền thêm cư dân" : 
                                           "Kế toán không có quyền thêm cư dân";
                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(reason);
                javafx.scene.control.Tooltip.install(buttonThemCuDan, tooltip);
                System.out.println("DEBUG: Đã disable nút thêm cư dân cho " + vaiTro);
            }
        }

        // Disable nút thêm căn hộ cho Tổ trưởng và Kế toán  
        if (buttonThemCanHo != null) {
            boolean shouldDisableCanHo = isToTruong || isKeToan;
            buttonThemCanHo.setDisable(shouldDisableCanHo);
            if (shouldDisableCanHo) {
                String reason = isToTruong ? "Tổ trưởng không có quyền thêm căn hộ" : 
                                           "Kế toán không có quyền thêm căn hộ";
                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(reason);
                javafx.scene.control.Tooltip.install(buttonThemCanHo, tooltip);
                System.out.println("DEBUG: Đã disable nút thêm căn hộ cho " + vaiTro);
            }
        }

        // Disable nút thêm khoản thu cho tất cả trừ Kế toán (chỉ Kế toán được phép)
        if (buttonThemKhoanThu != null) {
            boolean shouldDisableKhoanThu = !isKeToan; // Chỉ Kế toán được phép
            buttonThemKhoanThu.setDisable(shouldDisableKhoanThu);
            if (shouldDisableKhoanThu) {
                String reason;
                if (isToTruong) {
                    reason = "Tổ trưởng không có quyền thêm khoản thu";
                } else {
                    reason = "Chỉ Kế toán mới có quyền thêm khoản thu";
                }
                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(reason);
                javafx.scene.control.Tooltip.install(buttonThemKhoanThu, tooltip);
                System.out.println("DEBUG: Đã disable nút thêm khoản thu cho " + vaiTro);
            } else {
                System.out.println("DEBUG: Kế toán được phép thêm khoản thu - nút enable");
            }
        }

        System.out.println("DEBUG: Hoàn thành thiết lập quyền nút");
    }
    
    // ============= SEARCH FUNCTIONS =============
    
    /**
     * Xử lý tìm kiếm căn hộ
     */
    @FXML
    private void handleTimKiemCanHo() {
        String maCanHo = textFieldMaCanHo != null ? textFieldMaCanHo.getText().trim() : "";
        String chuSoHuu = textFieldChuSoHuu != null ? textFieldChuSoHuu.getText().trim() : "";
        String soNha = textFieldSoNha != null ? textFieldSoNha.getText().trim() : "";
        String tang = textFieldTang != null ? textFieldTang.getText().trim() : "";
        String toa = textFieldToa != null ? textFieldToa.getText().trim() : "";
        String trangThai = comboBoxTrangThai != null && comboBoxTrangThai.getValue() != null ? comboBoxTrangThai.getValue().toString() : "";
        
        // Nếu tất cả các điều kiện tìm kiếm đều trống thì hiển thị toàn bộ
        if (maCanHo.isEmpty() && chuSoHuu.isEmpty() && soNha.isEmpty() &&
            tang.isEmpty() && 
            toa.isEmpty() && 
            ("Tất cả".equals(trangThai) || trangThai.isEmpty())) {
            
            filteredList = FXCollections.observableArrayList(canHoList);
            if (tabelViewCanHo != null) {
                ((TableView<CanHoTableData>) tabelViewCanHo).setItems(filteredList);
            }
            updateKetQuaLabel();
            return;
        }
        
        // Lọc dữ liệu dựa trên các tiêu chí tìm kiếm
        if (canHoList != null) {
            ObservableList<CanHoTableData> searchResults = canHoList.stream()
                .filter(canHo -> {
                    boolean matchesMaCanHo = maCanHo.isEmpty() || 
                        canHo.getMaCanHo().toLowerCase().contains(maCanHo.toLowerCase());
                    boolean matchesChuSoHuu = chuSoHuu.isEmpty() || 
                        canHo.getChuHo().toLowerCase().contains(chuSoHuu.toLowerCase());
                    boolean matchesSoNha = soNha.isEmpty() || 
                        canHo.getSoNha().toLowerCase().contains(soNha.toLowerCase());
                    boolean matchesTang = tang.isEmpty() ||
                        canHo.getTang().toLowerCase().contains(tang.toLowerCase());
                    boolean matchesToa = toa.isEmpty() ||
                        canHo.getToaNha().toLowerCase().contains(toa.toLowerCase());
                    
                    // Xử lý logic tìm kiếm trạng thái căn hộ
                    boolean matchesTrangThai = true;
                    if (!"Tất cả".equals(trangThai) && !trangThai.isEmpty()) {
                        String actualTrangThai = canHo.getTrangThaiSuDung();
                        if ("Sử dụng".equals(trangThai)) {
                            // "Sử dụng" sẽ match với các trạng thái khác "Trống"
                            matchesTrangThai = actualTrangThai != null && 
                                             !actualTrangThai.equalsIgnoreCase("Trống") &&
                                             !actualTrangThai.equalsIgnoreCase("Không sử dụng");
                        } else if ("Trống".equals(trangThai)) {
                            // "Trống" sẽ match với trạng thái "Trống" hoặc "Không sử dụng"
                            matchesTrangThai = actualTrangThai != null && 
                                             (actualTrangThai.equalsIgnoreCase("Trống") ||
                                              actualTrangThai.equalsIgnoreCase("Không sử dụng"));
                        } else {
                            // Tìm kiếm chính xác theo tên trạng thái
                            matchesTrangThai = actualTrangThai != null && actualTrangThai.equals(trangThai);
                        }
                    }
                    
                    return matchesMaCanHo && matchesChuSoHuu && matchesSoNha && matchesTang && matchesToa && matchesTrangThai;
                })
                .collect(FXCollections::observableArrayList, 
                        ObservableList::add, 
                        ObservableList::addAll);
            
            filteredList = searchResults;
            if (tabelViewCanHo != null) {
                ((TableView<CanHoTableData>) tabelViewCanHo).setItems(filteredList);
            }
            updateKetQuaLabel();
            
            System.out.println("🔍 Apartment search completed:");
            System.out.println("  - Search criteria: MaCanHo=" + maCanHo + ", ChuSoHuu=" + chuSoHuu + 
                             ", SoNha=" + soNha + ", Tang=" + tang + ", Toa=" + toa + ", TrangThai=" + trangThai);
            System.out.println("  - Results: " + searchResults.size() + "/" + canHoList.size());
        }
    }
    
    /**
     * Xử lý tìm kiếm cư dân
     */
    @FXML
    private void handleTimKiemCuDan() {
        String maDinhDanh = textFieldMaDinhDanh != null ? textFieldMaDinhDanh.getText().trim() : "";
        String hoVaTen = textFieldHoVaTen != null ? textFieldHoVaTen.getText().trim() : "";
        String maCanHo = textFieldMaCanHoCuDan != null ? textFieldMaCanHoCuDan.getText().trim() : "";
        String email = textFieldEmail != null ? textFieldEmail.getText().trim() : "";
        String trangThai = comboBoxTrangThaiCuDan != null && comboBoxTrangThaiCuDan.getValue() != null ? 
                          comboBoxTrangThaiCuDan.getValue().toString() : "";
        
        // Nếu tất cả các điều kiện tìm kiếm đều trống thì hiển thị toàn bộ
        if (maDinhDanh.isEmpty() && hoVaTen.isEmpty() && maCanHo.isEmpty() && email.isEmpty() &&
            ("Tất cả".equals(trangThai) || trangThai.isEmpty())) {
            
            filteredCuDanList = FXCollections.observableArrayList(cuDanList);
            if (tabelViewCuDan != null) {
                ((TableView<CuDanTableData>) tabelViewCuDan).setItems(filteredCuDanList);
            }
            updateCuDanKetQuaLabel();
            return;
        }
        
        // Lọc dữ liệu dựa trên các tiêu chí tìm kiếm
        if (cuDanList != null) {
            ObservableList<CuDanTableData> searchResults = cuDanList.stream()
                .filter(cuDan -> {
                    boolean matchesMaDinhDanh = maDinhDanh.isEmpty() || 
                        cuDan.getMaDinhDanh().toLowerCase().contains(maDinhDanh.toLowerCase());
                    boolean matchesHoVaTen = hoVaTen.isEmpty() || 
                        cuDan.getHoVaTen().toLowerCase().contains(hoVaTen.toLowerCase());
                    boolean matchesMaCanHo = maCanHo.isEmpty() || 
                        cuDan.getMaCanHo().toLowerCase().contains(maCanHo.toLowerCase());
                    boolean matchesEmail = email.isEmpty() || 
                        cuDan.getEmail().toLowerCase().contains(email.toLowerCase());
                    boolean matchesTrangThai = "Tất cả".equals(trangThai) || trangThai.isEmpty() ||
                        cuDan.getTrangThaiCuTru().equals(trangThai);
                    
                    return matchesMaDinhDanh && matchesHoVaTen && matchesMaCanHo && matchesEmail && matchesTrangThai;
                })
                .collect(FXCollections::observableArrayList, 
                        ObservableList::add, 
                        ObservableList::addAll);
            
            filteredCuDanList = searchResults;
            if (tabelViewCuDan != null) {
                ((TableView<CuDanTableData>) tabelViewCuDan).setItems(filteredCuDanList);
            }
            updateCuDanKetQuaLabel();
            
            System.out.println("🔍 Resident search completed:");
            System.out.println("  - Search criteria: MaDinhDanh=" + maDinhDanh + ", HoVaTen=" + hoVaTen + 
                             ", MaCanHo=" + maCanHo + ", Email=" + email + ", TrangThai=" + trangThai);
            System.out.println("  - Results: " + searchResults.size() + "/" + cuDanList.size());
        }
    }
    
    /**
     * Xử lý tìm kiếm khoản thu
     */
    @FXML
    private void handleTimKiemKhoanThu() {
        String maKhoanThu = textFieldMaKhoanThu != null ? textFieldMaKhoanThu.getText().trim() : "";
        String tenKhoanThu = textFieldTenKhoanThu != null ? textFieldTenKhoanThu.getText().trim() : "";
        String loaiKhoanThu = comboBoxLoaiKhoanThu != null && comboBoxLoaiKhoanThu.getValue() != null ? 
                             comboBoxLoaiKhoanThu.getValue().toString() : "";
        
        // Nếu tất cả các điều kiện tìm kiếm đều trống thì hiển thị toàn bộ
        if (maKhoanThu.isEmpty() && tenKhoanThu.isEmpty() && 
            ("Tất cả".equals(loaiKhoanThu) || loaiKhoanThu.isEmpty())) {
            
            filteredKhoanThuList = FXCollections.observableArrayList(khoanThuList);
            if (tabelViewKhoanThu != null) {
                ((TableView<KhoanThuTableData>) tabelViewKhoanThu).setItems(filteredKhoanThuList);
            }
            updateKhoanThuKetQuaLabel();
            return;
        }
        
        // Lọc dữ liệu dựa trên các tiêu chí tìm kiếm
        if (khoanThuList != null) {
            ObservableList<KhoanThuTableData> searchResults = khoanThuList.stream()
                .filter(khoanThu -> {
                    boolean matchesMaKhoanThu = maKhoanThu.isEmpty() || 
                        khoanThu.getMaKhoanThu().toLowerCase().contains(maKhoanThu.toLowerCase());
                    boolean matchesTenKhoanThu = tenKhoanThu.isEmpty() || 
                        khoanThu.getTenKhoanThu().toLowerCase().contains(tenKhoanThu.toLowerCase());
                    boolean matchesLoaiKhoanThu = "Tất cả".equals(loaiKhoanThu) || loaiKhoanThu.isEmpty() ||
                        khoanThu.getLoaiKhoanThu().equals(loaiKhoanThu) ||
                        khoanThu.getLoaiKhoanThu().toLowerCase().contains(loaiKhoanThu.toLowerCase());
                    
                    return matchesMaKhoanThu && matchesTenKhoanThu && matchesLoaiKhoanThu;
                })
                .collect(FXCollections::observableArrayList, 
                        ObservableList::add, 
                        ObservableList::addAll);
            
            filteredKhoanThuList = searchResults;
            if (tabelViewKhoanThu != null) {
                ((TableView<KhoanThuTableData>) tabelViewKhoanThu).setItems(filteredKhoanThuList);
            }
            updateKhoanThuKetQuaLabel();
            
            System.out.println("🔍 Fee search completed:");
            System.out.println("  - Search criteria: MaKhoanThu=" + maKhoanThu + ", TenKhoanThu=" + tenKhoanThu + 
                             ", LoaiKhoanThu=" + loaiKhoanThu);
            System.out.println("  - Results: " + searchResults.size() + "/" + khoanThuList.size());
        }
    }
    
    /**
     * Xử lý tìm kiếm thu phí (trang lịch sử thu)
     */
    @FXML
    private void handleTimKiemThuPhi() {
        String maCanHo = textFieldMaCanHoThuPhi != null ? textFieldMaCanHoThuPhi.getText().trim() : "";
        String maHoaDon = textFieldMaHoaDon != null ? textFieldMaHoaDon.getText().trim() : "";
        String tenKhoanThu = textFieldTenKhoanThu1 != null ? textFieldTenKhoanThu1.getText().trim() : "";
        String loaiKhoanThu = comboBoxLoaiKhoanThu1 != null && comboBoxLoaiKhoanThu1.getValue() != null ? 
                             comboBoxLoaiKhoanThu1.getValue().toString() : "";
        String trangThaiHoaDon = comboBoxTrangThaiHoaDon != null && comboBoxTrangThaiHoaDon.getValue() != null ? 
                                comboBoxTrangThaiHoaDon.getValue().toString() : "";
        
        System.out.println("🔍 Payment history search initiated:");
        System.out.println("  - MaCanHo: " + maCanHo);
        System.out.println("  - MaHoaDon: " + maHoaDon);
        System.out.println("  - TenKhoanThu: " + tenKhoanThu);
        System.out.println("  - LoaiKhoanThu: " + loaiKhoanThu);
        System.out.println("  - TrangThaiHoaDon: " + trangThaiHoaDon);
        
        // Hiện tại trang thu phí chưa có dữ liệu, chỉ log thông tin tìm kiếm
        showInfo("Tìm kiếm thu phí", "Chức năng tìm kiếm thu phí đang được phát triển.\n" +
                "Tiêu chí tìm kiếm:\n" +
                "- Mã căn hộ: " + (maCanHo.isEmpty() ? "Tất cả" : maCanHo) + "\n" +
                "- Mã hóa đơn: " + (maHoaDon.isEmpty() ? "Tất cả" : maHoaDon) + "\n" +
                "- Tên khoản thu: " + (tenKhoanThu.isEmpty() ? "Tất cả" : tenKhoanThu) + "\n" +
                "- Loại khoản thu: " + (loaiKhoanThu.isEmpty() ? "Tất cả" : loaiKhoanThu) + "\n" +
                "- Trạng thái hóa đơn: " + (trangThaiHoaDon.isEmpty() ? "Tất cả" : trangThaiHoaDon));
    }
    
    /**
     * Setup search listeners for auto-search on text input
     */
    private void setupSearchListeners() {
        // Căn hộ search listeners
        if (textFieldMaCanHo != null) {
            textFieldMaCanHo.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCanHo());
        }
        if (textFieldChuSoHuu != null) {
            textFieldChuSoHuu.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCanHo());
        }
        if (textFieldSoNha != null) {
            textFieldSoNha.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCanHo());
        }
        if (textFieldTang != null) {
            textFieldTang.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCanHo());
        }
        if (textFieldToa != null) {
            textFieldToa.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCanHo());
        }
        if (comboBoxTrangThai != null) {
            comboBoxTrangThai.valueProperty().addListener((obs, oldValue, newValue) -> handleTimKiemCanHo());
        }
        
        // Cư dân search listeners
        if (textFieldMaDinhDanh != null) {
            textFieldMaDinhDanh.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCuDan());
        }
        if (textFieldHoVaTen != null) {
            textFieldHoVaTen.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCuDan());
        }
        if (textFieldMaCanHoCuDan != null) {
            textFieldMaCanHoCuDan.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCuDan());
        }
        if (textFieldEmail != null) {
            textFieldEmail.textProperty().addListener((obs, oldText, newText) -> handleTimKiemCuDan());
        }
        if (comboBoxTrangThaiCuDan != null) {
            comboBoxTrangThaiCuDan.valueProperty().addListener((obs, oldValue, newValue) -> handleTimKiemCuDan());
        }
        
        // Khoản thu search listeners
        if (textFieldMaKhoanThu != null) {
            textFieldMaKhoanThu.textProperty().addListener((obs, oldText, newText) -> handleTimKiemKhoanThu());
        }
        if (textFieldTenKhoanThu != null) {
            textFieldTenKhoanThu.textProperty().addListener((obs, oldText, newText) -> handleTimKiemKhoanThu());
        }
        if (comboBoxLoaiKhoanThu != null) {
            comboBoxLoaiKhoanThu.valueProperty().addListener((obs, oldValue, newValue) -> handleTimKiemKhoanThu());
        }
        
        // Thu phí search listeners
        if (textFieldMaCanHoThuPhi != null) {
            textFieldMaCanHoThuPhi.textProperty().addListener((obs, oldText, newText) -> handleTimKiemThuPhi());
        }
        if (textFieldMaHoaDon != null) {
            textFieldMaHoaDon.textProperty().addListener((obs, oldText, newText) -> handleTimKiemThuPhi());
        }
        if (textFieldTenKhoanThu1 != null) {
            textFieldTenKhoanThu1.textProperty().addListener((obs, oldText, newText) -> handleTimKiemThuPhi());
        }
        if (comboBoxLoaiKhoanThu1 != null) {
            comboBoxLoaiKhoanThu1.valueProperty().addListener((obs, oldValue, newValue) -> handleTimKiemThuPhi());
        }
        if (comboBoxTrangThaiHoaDon != null) {
            comboBoxTrangThaiHoaDon.valueProperty().addListener((obs, oldValue, newValue) -> handleTimKiemThuPhi());
        }
        
        System.out.println("✅ Search listeners setup completed");
    }

    /**
     * Hiển thị dialog xác nhận tùy chỉnh
     */
    private boolean showCustomConfirmDialog(String hoVaTen, String maDinhDanh) {
        try {
            // Load FXML file
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/xac_nhan.fxml"));
            javafx.scene.Parent root = loader.load();

            // Lấy controller
            XacNhanController controller = loader.getController();

            // Thiết lập nội dung
            controller.setTitle("Xác nhận xóa cư dân");
            controller.setContent("Bạn có chắc chắn muốn xóa cư dân " + hoVaTen + " (Mã: " + maDinhDanh + ") không?");

            // Tạo stage
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            stage.initOwner(tabelViewCuDan.getScene().getWindow());

            // Thiết lập scene
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();

            // Hiển thị và chờ
            stage.showAndWait();

            return controller.isConfirmed();
        } catch (Exception e) {
            System.err.println("Lỗi khi hiển thị dialog xác nhận: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Setup right-click refresh functionality for all panes
     */
    private void setupRightClickRefresh() {
        System.out.println("=== DEBUG: Setting up right-click refresh functionality ===");
        
        // Add right-click event handler for each pane
        for (Node pane : allPanes) {
            if (pane != null) {
                pane.setOnMouseClicked(event -> {
                    if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                        System.out.println("🔄 RIGHT CLICK DETECTED - Refreshing page...");
                        handleRightClickRefresh();
                        event.consume(); // Prevent context menu
                    }
                });
                System.out.println("✅ Added right-click handler to: " + pane.getClass().getSimpleName());
            }
        }
        
        System.out.println("=== DEBUG: Right-click refresh setup completed ===");
    }
    
    /**
     * Handle right-click refresh based on current visible pane
     */
    private void handleRightClickRefresh() {
        try {
            System.out.println("🔄 Starting right-click refresh...");
            
            // Determine which pane is currently visible and refresh accordingly using right-click methods
            if (gridPaneTrangChu.isVisible()) {
                System.out.println("📊 Refreshing Dashboard...");
                refreshDashboard();
            } else if (scrollPaneCanHo.isVisible()) {
                System.out.println("🏠 Refreshing Apartments data...");
                refreshApartmentDataRightClick();
            } else if (scrollPaneCuDan.isVisible()) {
                System.out.println("👥 Refreshing Residents data...");
                refreshResidentDataRightClick();
            } else if (scrollPaneTaiKhoan.isVisible()) {
                System.out.println("👤 Refreshing Accounts data...");
                refreshAccountDataRightClick();
            } else if (scrollPaneKhoanThu.isVisible()) {
                System.out.println("💰 Refreshing Fee data...");
                refreshFeeDataRightClick();
            } else {
                System.out.println("🔄 Refreshing all data...");
                refreshAllDataRightClick();
            }
            
            // Show success feedback
            
            
        } catch (Exception e) {
            System.err.println("❌ ERROR during right-click refresh: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Refresh dashboard data
     */
    private void refreshDashboard() {
        // Reload all basic data for dashboard
        loadData();
        loadCuDanData();
        loadTaiKhoanData();
        System.out.println("✅ Dashboard refreshed");
    }
    
    /**
     * Refresh apartment data for right-click (separate from main refresh)
     */
    private void refreshApartmentDataRightClick() {
        System.out.println("🖱️ Right-click refresh: Apartments");
        // Clear cache and reload apartments data
        if (cacheDataService != null) {
            cacheDataService.refreshCacheData();
        }
        loadData();
        System.out.println("✅ Apartments data refreshed via right-click");
    }
    
    /**
     * Refresh residents data for right-click (separate from main refresh)
     */
    private void refreshResidentDataRightClick() {
        System.out.println("🖱️ Right-click refresh: Residents");
        // Clear cache and reload residents data
        if (cacheDataService != null) {
            cacheDataService.refreshCacheData();
        }
        loadCuDanData();
        System.out.println("✅ Residents data refreshed via right-click");
    }
    
    /**
     * Refresh accounts data for right-click
     */
    private void refreshAccountDataRightClick() {
        System.out.println("🖱️ Right-click refresh: Accounts");
        loadTaiKhoanData();
        System.out.println("✅ Accounts data refreshed via right-click");
    }
    
    /**
     * Refresh fee data for right-click
     */
    private void refreshFeeDataRightClick() {
        System.out.println("🖱️ Right-click refresh: Fees");
        refreshKhoanThuDataInternal();
        System.out.println("✅ Fee data refreshed via right-click");
    }
    
    /**
     * Refresh all data for right-click
     */
    private void refreshAllDataRightClick() {
        System.out.println("🖱️ Right-click refresh: All data");
        refreshApartmentDataRightClick();
        refreshResidentDataRightClick();
        refreshAccountDataRightClick();
        refreshFeeDataRightClick();
        System.out.println("✅ All data refreshed via right-click");
    }

    
    /**
     * Refresh accounts data
     */
    public void refreshTaiKhoanData() {
        loadTaiKhoanData();
        System.out.println("✅ Accounts data refreshed");
    }
    
    /**
     * Public method to refresh fee data - can be called from other controllers
     */
    public void refreshKhoanThuData() {
        // Clear cache and reload fee data
        if (cacheDataService != null) {
            cacheDataService.refreshCacheData();
        }
        loadKhoanThuData();
        System.out.println("✅ Fee data refreshed");
    }
    
    /**
     * Private method for internal refresh fee data
     */
    private void refreshKhoanThuDataInternal() {
        loadKhoanThuData();
        System.out.println("✅ Fee data refreshed internally");
    }
    
    /**
     * Setup Khoản Thu table
     */
    public void setupKhoanThuTable() {
        if (tabelViewKhoanThu != null && tableColumnMaKhoanThu != null) {
            // Cast table view to correct type
            TableView<KhoanThuTableData> typedTableView = (TableView<KhoanThuTableData>) tabelViewKhoanThu;

            // Setup cell value factories - Map với đúng columns trong FXML
            ((TableColumn<KhoanThuTableData, String>) tableColumnMaKhoanThu).setCellValueFactory(new PropertyValueFactory<>("maKhoanThu"));
            ((TableColumn<KhoanThuTableData, String>) tableColumnTenKhoanThu).setCellValueFactory(new PropertyValueFactory<>("tenKhoanThu"));
            ((TableColumn<KhoanThuTableData, String>) tableColumnLoaiKhoanThu).setCellValueFactory(new PropertyValueFactory<>("loaiKhoanThu"));
            
            // Setup cột "Số tiền" với nút "Xem thêm" cho khoản thu phương tiện
            if (tableColumnSoTien != null) {
                TableColumn<KhoanThuTableData, String> soTienColumn = (TableColumn<KhoanThuTableData, String>) tableColumnSoTien;
                soTienColumn.setCellFactory(column -> new javafx.scene.control.TableCell<KhoanThuTableData, String>() {
                    private final javafx.scene.control.Button btnXemThem = new javafx.scene.control.Button("Xem thêm");
                    
                    {
                        btnXemThem.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                        btnXemThem.setOnAction(event -> {
                            KhoanThuTableData rowData = getTableView().getItems().get(getIndex());
                            handleXemChiTietPhiXe(rowData);
                        });
                    }
                    
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            KhoanThuTableData rowData = getTableView().getItems().get(getIndex());
                            if ("Phương tiện".equals(rowData.getDonViTinh())) {
                                // Hiển thị nút "Xem thêm" cho khoản thu phương tiện
                                setGraphic(btnXemThem);
                                setText(null);
                            } else {
                                // Hiển thị số tiền bình thường cho khoản thu khác
                                setGraphic(null);
                                setText(rowData.getSoTien());
                            }
                        }
                    }
                });
            }
            
            // "Bộ phận quản lý" -> map với ghiChu 
            if (tableColumnBoPhanQuanLy != null) {
                ((TableColumn<KhoanThuTableData, String>) tableColumnBoPhanQuanLy).setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
            }
            
            // Bỏ cột "Ngày tạo" trong bảng khoản thu - không cần hiển thị nữa
            
            // "Hạn nộp" (tableColumnNgayTao1) -> map với thoiHan
            if (tableColumnNgayTao1 != null) {
                ((TableColumn<KhoanThuTableData, String>) tableColumnNgayTao1).setCellValueFactory(new PropertyValueFactory<>("thoiHan"));
            }
            
            // "Trạng thái hóa đơn" -> tạm thời hiển thị "Chưa tạo" (vì không có trong DTO)
            if (tableColumnTrangThaiHoaDon != null) {
                ((TableColumn<KhoanThuTableData, String>) tableColumnTrangThaiHoaDon).setCellValueFactory(cellData -> {
                    return new javafx.beans.property.SimpleStringProperty("Chưa tạo");
                });
            }

            // Setup row click handler
            typedTableView.setRowFactory(tv -> {
                javafx.scene.control.TableRow<KhoanThuTableData> row = new javafx.scene.control.TableRow<>();
                row.setOnMouseClicked(event -> {
                    System.out.println("🖱️ Mouse clicked on KhoanThu table row");
                    System.out.println("   - Button: " + event.getButton());
                    System.out.println("   - Click count: " + event.getClickCount());
                    System.out.println("   - Row empty: " + row.isEmpty());
                    
                    if (!row.isEmpty() && event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                        KhoanThuTableData rowData = row.getItem();
                        System.out.println("   - Row data: " + (rowData != null ? rowData.getTenKhoanThu() : "null"));
                        
                        if (event.getClickCount() == 2) {
                            // Double click - kiểm tra quyền để quyết định hành động
                            System.out.println("   - Double click detected");
                            boolean hasEditPermission = hasKhoanThuEditPermission();
                            System.out.println("   - Has edit permission: " + hasEditPermission);
                            
                            if (hasEditPermission) {
                                // Kế toán: mở form chỉnh sửa
                                System.out.println("   - Opening edit form");
                                handleEditKhoanThu(rowData);
                            } else {
                                // Các vị trí khác: chỉ xem chi tiết
                                System.out.println("   - No permission - showing detail view");
                                handleXemChiTietKhoanThu(rowData);
                            }
                        } else if (event.getClickCount() == 1) {
                            System.out.println("   - Single click - selecting row only");
                        }
                    }
                });
                return row;
            });
            
            System.out.println("DEBUG: setupKhoanThuTable completed successfully");
        } else {
            System.out.println("ERROR: setupKhoanThuTable failed - table or column is null");
            System.out.println("DEBUG: tabelViewKhoanThu = " + (tabelViewKhoanThu != null ? "OK" : "NULL"));
            System.out.println("DEBUG: tableColumnMaKhoanThu = " + (tableColumnMaKhoanThu != null ? "OK" : "NULL"));
        }
    }
    
    /**
     * Kiểm tra quyền chỉnh sửa khoản thu (chỉ Kế toán mới được chỉnh sửa)
     */
    private boolean hasKhoanThuEditPermission() {
        try {
            io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto currentUser = 
                io.github.ktpm.bluemoonmanagement.session.Session.getCurrentUser();
            
            if (currentUser != null && currentUser.getVaiTro() != null) {
                String vaiTro = currentUser.getVaiTro();
                return "Kế toán".equals(vaiTro);
            }
            return false;
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra quyền: " + e.getMessage());
            return false;
        }
    }

    /**
     * Handle Xem chi tiết phí xe - chỉ hiển thị thông tin phí xe
     */
    private void handleXemChiTietPhiXe(KhoanThuTableData rowData) {
        try {
            if (!"Phương tiện".equals(rowData.getDonViTinh())) {
                showInfo("Thông tin", "Đây không phải là khoản thu phương tiện.");
                return;
            }
            
            StringBuilder phiXeDetails = new StringBuilder();
            phiXeDetails.append("📋 CHI TIẾT PHÍ GỬI XE - ").append(rowData.getTenKhoanThu()).append("\n");
            phiXeDetails.append("Mã khoản thu: ").append(rowData.getMaKhoanThu()).append("\n\n");
            
            if (khoanThuService != null) {
                try {
                    // Lấy thông tin chi tiết khoản thu từ service
                    List<io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto> khoanThuList = khoanThuService.getAllKhoanThu();
                    io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto khoanThuDto = khoanThuList.stream()
                        .filter(kt -> kt.getMaKhoanThu().equals(rowData.getMaKhoanThu()))
                        .findFirst()
                        .orElse(null);
                        
                    if (khoanThuDto != null && khoanThuDto.getPhiGuiXeList() != null && !khoanThuDto.getPhiGuiXeList().isEmpty()) {
                        phiXeDetails.append("🚗 BẢNG GIÁ PHÍ GỬI XE:");
                        phiXeDetails.append("\n" + "=".repeat(35));
                        
                        for (io.github.ktpm.bluemoonmanagement.model.dto.phiGuiXe.PhiGuiXeDto phiXe : khoanThuDto.getPhiGuiXeList()) {
                            phiXeDetails.append("\n🔸 ").append(phiXe.getLoaiXe())
                                      .append(": ").append(String.format("%,d", phiXe.getSoTien()))
                                      .append(" VND");
                        }
                        phiXeDetails.append("\n" + "=".repeat(35));
                        
                        // Thêm ghi chú
                        phiXeDetails.append("\n\n📝 Ghi chú: Phí được tính theo tháng cho mỗi loại phương tiện.");
                    } else {
                        phiXeDetails.append("⚠️ Chưa có thông tin chi tiết phí xe.");
                        phiXeDetails.append("\nVui lòng liên hệ ban quản lý để biết thêm thông tin.");
                    }
                } catch (Exception ex) {
                    System.err.println("Error loading vehicle fee details: " + ex.getMessage());
                    phiXeDetails.append("❌ Không thể tải thông tin chi tiết phí xe.");
                    phiXeDetails.append("\nLỗi: ").append(ex.getMessage());
                }
            } else {
                phiXeDetails.append("❌ Dịch vụ không khả dụng.");
            }
            
            showInfo("Chi tiết phí gửi xe", phiXeDetails.toString());
        } catch (Exception e) {
            showError("Lỗi khi xem chi tiết phí xe", "Chi tiết: " + e.getMessage());
        }
    }

    /**
     * Handle Khoản Thu detail view
     */
    private void handleXemChiTietKhoanThu(KhoanThuTableData rowData) {
        try {
            StringBuilder details = new StringBuilder();
            details.append(" Mã khoản thu: ").append(rowData.getMaKhoanThu()).append("\n");
            details.append(" Tên: ").append(rowData.getTenKhoanThu()).append("\n");
            details.append(" Loại: ").append(rowData.getLoaiKhoanThu()).append("\n");
            details.append(" Số tiền: ").append(rowData.getSoTien()).append("\n");
            details.append(" Đơn vị tính: ").append(rowData.getDonViTinh()).append("\n");
            details.append(" Phạm vi: ").append(rowData.getPhamVi()).append("\n");
            details.append(" Bộ phận quản lý: ").append(rowData.getGhiChu() != null ? rowData.getGhiChu() : "Không có").append("\n");
            details.append(" Ngày tạo: ").append(rowData.getNgayTao()).append("\n");
            details.append(" Thời hạn: ").append(rowData.getThoiHan());
            
            // Nếu là khoản thu phương tiện, hiển thị chi tiết phí xe luôn
            if ("Phương tiện".equals(rowData.getDonViTinh()) && khoanThuService != null) {
                try {
                    // Lấy thông tin chi tiết khoản thu từ service
                    List<io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto> khoanThuList = khoanThuService.getAllKhoanThu();
                    io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto khoanThuDto = khoanThuList.stream()
                        .filter(kt -> kt.getMaKhoanThu().equals(rowData.getMaKhoanThu()))
                        .findFirst()
                        .orElse(null);
                        
                    if (khoanThuDto != null && khoanThuDto.getPhiGuiXeList() != null && !khoanThuDto.getPhiGuiXeList().isEmpty()) {
                        details.append("\n\n📋 CHI TIẾT PHÍ GỬI XE:");
                        details.append("\n" + "=".repeat(30));
                        
                        for (io.github.ktpm.bluemoonmanagement.model.dto.phiGuiXe.PhiGuiXeDto phiXe : khoanThuDto.getPhiGuiXeList()) {
                            details.append("\n• ").append(phiXe.getLoaiXe())
                                  .append(": ").append(String.format("%,d", phiXe.getSoTien()))
                                  .append(" VND");
                        }
                        details.append("\n" + "=".repeat(30));
                    } else {
                        details.append("\n\n⚠️ Chưa có thông tin chi tiết phí xe.");
                    }
                } catch (Exception ex) {
                    System.err.println("Error loading vehicle fee details: " + ex.getMessage());
                    details.append("\n\n❌ Không thể tải thông tin chi tiết phí xe.");
                }
            }
            
            showInfo("Chi tiết khoản thu", details.toString());
        } catch (Exception e) {
            showError("Lỗi khi xem chi tiết", "Chi tiết: " + e.getMessage());
        }
    }
    

    
    /**
     * Handle Khoản Thu edit
     */
    private void handleEditKhoanThu(KhoanThuTableData rowData) {
        try {
            System.out.println("Mở form chỉnh sửa khoản thu: " + rowData.getTenKhoanThu() + " (" + rowData.getMaKhoanThu() + ")");
            
            // Load view + controller using FxViewLoader
            FxView<?> fxView = fxViewLoader.loadFxView("/view/khoan_thu.fxml");
            
            // Get controller and setup edit mode (bỏ phần set ApplicationContext gây lỗi)
            Object controller = fxView.getController();
            if (controller instanceof io.github.ktpm.bluemoonmanagement.controller.ThemKhoanThuController) {
                io.github.ktpm.bluemoonmanagement.controller.ThemKhoanThuController khoanThuController = 
                    (io.github.ktpm.bluemoonmanagement.controller.ThemKhoanThuController) controller;
                
                // Setup edit mode với dữ liệu khoản thu hiện tại
                khoanThuController.setupEditMode(rowData);
            }

            // Tạo cửa sổ mới
            Stage newStage = new Stage();
            newStage.setScene(new Scene(fxView.getView()));
            newStage.setTitle("Chỉnh sửa khoản thu - " + rowData.getTenKhoanThu());

            // Thiết lập modal
            newStage.initModality(Modality.APPLICATION_MODAL);

            // Gán owner là cửa sổ hiện tại
            Stage currentStage = (Stage) scrollPaneKhoanThu.getScene().getWindow();
            newStage.initOwner(currentStage);

            // Thiết lập kích thước cửa sổ
            newStage.setMinWidth(700);
            newStage.setMinHeight(600);
            newStage.setResizable(true);

            // Hiển thị cửa sổ mới và chờ đóng
            newStage.showAndWait();
            
            // Reload dữ liệu sau khi đóng form chỉnh sửa
            System.out.println("Form chỉnh sửa khoản thu đã đóng, reload dữ liệu...");
            refreshKhoanThuData();

        } catch (IOException e) {
            System.err.println("Không thể mở cửa sổ chỉnh sửa khoản thu:");
            e.printStackTrace();
            showError("Lỗi", "Không thể mở form chỉnh sửa khoản thu: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khi xử lý chỉnh sửa khoản thu: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi", "Lỗi khi xử lý chỉnh sửa khoản thu: " + e.getMessage());
        }
    }
    
    /**
     * Load Khoản Thu data
     */
    private void loadKhoanThuData() {
        System.out.println("=== DEBUG: loadKhoanThuData() called ===");
        System.out.println("DEBUG: khoanThuService = " + (khoanThuService != null ? "OK" : "NULL"));
        
        try {
            if (khoanThuService != null) {
                List<KhoanThuDto> khoanThuDtoList = khoanThuService.getAllKhoanThu();
                System.out.println("DEBUG: Got " + (khoanThuDtoList != null ? khoanThuDtoList.size() : 0) + " fees from service");
                khoanThuList = FXCollections.observableArrayList();

                if (khoanThuDtoList != null) {
                    for (KhoanThuDto dto : khoanThuDtoList) {
                        String ngayTao = dto.getNgayTao() != null ? dto.getNgayTao().toString() : "";
                        String thoiHan = dto.getThoiHan() != null ? dto.getThoiHan().toString() : "";
                        String soTien = String.format("%,d VNĐ", dto.getSoTien());
                        String loaiKhoanThu = dto.isBatBuoc() ? "Bắt buộc" : "Tự nguyện";
                        
                        KhoanThuTableData tableData = new KhoanThuTableData(
                            dto.getMaKhoanThu(),
                            dto.getTenKhoanThu(),
                            loaiKhoanThu,
                            dto.getDonViTinh() != null ? dto.getDonViTinh() : "",
                            soTien,
                            dto.getPhamVi() != null ? dto.getPhamVi() : "Tất cả",
                            ngayTao,
                            thoiHan,
                            dto.getGhiChu() != null ? dto.getGhiChu() : ""
                        );
                        khoanThuList.add(tableData);
                    }
                }

                filteredKhoanThuList = FXCollections.observableArrayList(khoanThuList);
                if (tabelViewKhoanThu != null) {
                    ((TableView<KhoanThuTableData>) tabelViewKhoanThu).setItems(filteredKhoanThuList);
                    System.out.println("DEBUG: Set " + filteredKhoanThuList.size() + " fees to table view");
                } else {
                    System.err.println("ERROR: tabelViewKhoanThu is null!");
                }
                updateKhoanThuKetQuaLabel();
                System.out.println("=== DEBUG: loadKhoanThuData() completed with " + khoanThuList.size() + " fees ===");
            } else {
                System.err.println("KhoanThuService is not available, cannot load data.");
                // Load sample data if service is not available
                loadSampleKhoanThuData();
            }
        } catch (Exception e) {
            System.err.println("Error loading KhoanThu data: " + e.getMessage());
            e.printStackTrace();
            // Load sample data on error
            loadSampleKhoanThuData();
        }
    }
    
    /**
     * Load sample khoản thu data for testing
     */
    private void loadSampleKhoanThuData() {
        khoanThuList = FXCollections.observableArrayList();
        
        // Add sample data
        khoanThuList.add(new KhoanThuTableData("KT001", "Phí quản lý", "Bắt buộc", "VNĐ/m²", "15,000 VNĐ", "Tất cả", "2024-01-01", "2024-01-31", "Ban quản lý"));
        khoanThuList.add(new KhoanThuTableData("KT002", "Phí điện", "Bắt buộc", "VNĐ/kWh", "3,500 VNĐ", "Căn hộ đang sử dụng", "2024-01-01", "2024-01-31", "Bên thứ 3"));
        khoanThuList.add(new KhoanThuTableData("KT003", "Phí nước", "Bắt buộc", "VNĐ/m³", "25,000 VNĐ", "Căn hộ đang sử dụng", "2024-01-01", "2024-01-31", "Bên thứ 3"));
        khoanThuList.add(new KhoanThuTableData("KT004", "Phí gửi xe", "Tự nguyện", "VNĐ/tháng", "100,000 VNĐ", "Tất cả", "2024-01-01", "2024-01-31", "Ban quản lý"));
        
        filteredKhoanThuList = FXCollections.observableArrayList(khoanThuList);
        if (tabelViewKhoanThu != null) {
            ((TableView<KhoanThuTableData>) tabelViewKhoanThu).setItems(filteredKhoanThuList);
        }
        updateKhoanThuKetQuaLabel();
    }
    
    /**
     * Update khoản thu result label
     */
    private void updateKhoanThuKetQuaLabel() {
        if (labelKetQuaHienThiKhoanThu != null && filteredKhoanThuList != null) {
            labelKetQuaHienThiKhoanThu.setText("Hiển thị " + filteredKhoanThuList.size() + " khoản thu");
        }
    }
    
    /**
     * Refresh all data
     */
    private void refreshAllData() {
        refreshApartmentData();
        refreshCuDanData();
        refreshTaiKhoanData();
        refreshKhoanThuDataInternal();
        System.out.println("✅ All data refreshed");
    }
    
    /**
     * Show visual feedback for successful refresh
     */
    private void showRefreshSuccess() {
        javafx.application.Platform.runLater(() -> {
            try {
                // Show success message using existing showSuccess method
                showSuccess("Refresh thành công", "🔄 Trang đã được làm mới!");
            } catch (Exception e) {
                System.err.println("Could not show refresh feedback: " + e.getMessage());
            }
        });
    }
    
    /**
     * Show visual feedback for successful right-click refresh
     */
    private void showRefreshSuccessRightClick() {
        javafx.application.Platform.runLater(() -> {
            try {
                // Show success message for right-click refresh
                showSuccess("Refresh bằng chuột phải", "🖱️ Dữ liệu đã được làm mới!");
            } catch (Exception e) {
                System.err.println("Could not show right-click refresh feedback: " + e.getMessage());
            }
        });
    }

    // Ensure updateTotalStatistics method is defined
    private void updateTotalStatistics() {
        System.out.println("📊 Updating total statistics...");
        
        try {
            // Tính tổng số căn hộ từ ArrayList (hiển thị 0 nếu database rỗng)
            int totalApartments = (canHoList != null && !canHoList.isEmpty()) ? canHoList.size() : 0;
            
            // Tính tổng số cư dân từ ArrayList (hiển thị 0 nếu database rỗng)
            int totalResidents = (cuDanList != null && !cuDanList.isEmpty()) ? cuDanList.size() : 0;
            
            // Tính tổng số khoản thu từ ArrayList (hiển thị 0 nếu database rỗng)
            int totalFees = (khoanThuList != null && !khoanThuList.isEmpty()) ? khoanThuList.size() : 0;
            
            // Cập nhật labelCanHoNumber = tổng số căn hộ
            if (labelCanHoNumber != null) {
                labelCanHoNumber.setText(String.valueOf(totalApartments));
                System.out.println("🏠 Updated labelCanHoNumber with total apartments: " + totalApartments);
            }
            
            // Cập nhật labelCuDanNumber = tổng số cư dân
            if (labelCuDanNumber != null) {
                labelCuDanNumber.setText(String.valueOf(totalResidents));
                System.out.println("👥 Updated labelCuDanNumber with total residents: " + totalResidents);
            }
            
            // Cập nhật labelCuDanNumber1 = tổng số khoản thu
            if (labelCuDanNumber1 != null) {
                labelCuDanNumber1.setText(String.valueOf(totalFees));
                System.out.println("💰 Updated labelCuDanNumber1 with total fees: " + totalFees);
            }
            
            System.out.println("✅ Total statistics updated successfully");
            
            // Hiển thị thông báo nếu database trống
            if (totalApartments == 0 && totalResidents == 0 && totalFees == 0) {
                System.out.println("⚠️ Database appears to be empty - all counts are 0");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error updating statistics: " + e.getMessage());
            e.printStackTrace();
            
            // Trong trường hợp lỗi, vẫn hiển thị 0 cho 3 label chính
            if (labelCanHoNumber != null) labelCanHoNumber.setText("0");
            if (labelCuDanNumber != null) labelCuDanNumber.setText("0");
            if (labelCuDanNumber1 != null) labelCuDanNumber1.setText("0");
        }
    }

    /**
     * Refresh all data for homepage
     */
    private void refreshAllDataForHomepage() {
        try {
            // Refresh all data
            loadData();           // Load apartment data
            loadCuDanData();      // Load resident data  
            loadTaiKhoanData();   // Load account data
            loadKhoanThuData();   // Load fee data
            
            // Update total statistics after loading data
            updateTotalStatistics();
            
            System.out.println("✅ Homepage data refreshed successfully");
            
        } catch (Exception e) {
            System.err.println("❌ Error refreshing homepage data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
