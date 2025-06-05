package io.github.ktpm.bluemoonmanagement.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanTrongCanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.dto.phuongTien.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
import java.io.IOException;

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
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnMaDinhDanhCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnHoVaTenCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnQuanHeChuHo;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnNgaySinhCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnTrangThaiCuDan;
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
    private ApplicationContext applicationContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ChiTietCanHoController được khởi tạo");
        
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
        
        // Setup components
        setupTabNavigation();
        setupTables();
        setupComboBoxes();
        
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
            tableColumnNgaySinhCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getNgaySinh() != null ? 
                    cellData.getValue().getNgaySinh().toString() : ""));
        }
        if (tableColumnTrangThaiCuDan != null) {
            tableColumnTrangThaiCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getTrangThaiCuTru() != null ? 
                    cellData.getValue().getTrangThaiCuTru() : ""));
        }

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
     * Load dữ liệu thật từ service cho căn hộ cụ thể
     */
    private void loadDataFromService(String maCanHo) {
        try {
            if (canHoService == null) {
                showError("Lỗi hệ thống", "Service chưa được khởi tạo. Vui lòng liên hệ admin.");
                return;
            }
            
            if (maCanHo == null || maCanHo.trim().isEmpty()) {
                showError("Lỗi dữ liệu", "Mã căn hộ không hợp lệ.");
                return;
            }

            CanHoDto canHoDto = new CanHoDto();
            canHoDto.setMaCanHo(maCanHo);
            
            CanHoChiTietDto chiTiet = canHoService.getCanHoChiTiet(canHoDto);
            
            if (chiTiet != null) {
                currentCanHo = chiTiet;
                
                // Load danh sách từ service data và lọc chỉ hiển thị cư dân chưa bị xóa (chưa có ngày chuyển đi)
                cuDanList.clear();
                if (chiTiet.getCuDanList() != null) {
                    // Lọc chỉ hiển thị cư dân chưa có ngày chuyển đi
                    chiTiet.getCuDanList().stream()
                        .filter(cuDan -> cuDan.getNgayChuyenDi() == null)
                        .forEach(cuDanList::add);
                    
                    System.out.println("=== DEBUG: UI loaded " + chiTiet.getCuDanList().size() + " total residents, " 
                                     + cuDanList.size() + " active residents ===");
                    for (CuDanTrongCanHoDto cuDan : cuDanList) {
                        System.out.println("- UI Active Resident: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ")");
                    }
                } else {
                    System.out.println("=== DEBUG: No residents in DTO ===");
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
            showError("Lỗi kết nối", "Không thể tải dữ liệu từ database: " + e.getMessage());
            clearAllData();
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
        
        // Ẩn thông tin chủ hộ
        if (vBoxChuSoHuu != null) vBoxChuSoHuu.setVisible(false);
    }

    /**
     * Set dữ liệu cho các table
     */
    private void setTableData() {
        System.out.println("=== DEBUG: Setting table data ===");
        
        // Set data for all tables
        if (tableViewCuDan != null && cuDanList != null) {
            tableViewCuDan.setItems(cuDanList);
            updateResultCount(); // Hiển thị số kết quả
            System.out.println("=== DEBUG: Set cư dân table data ===");
            System.out.println("Data size: " + cuDanList.size());
            for (CuDanTrongCanHoDto cuDan : cuDanList) {
                System.out.println("- " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ")");
            }
        }
        
        if (tableViewPhuongTien != null) {
            tableViewPhuongTien.setItems(phuongTienList);
        }
        if (tableViewThuPhi != null) {
            tableViewThuPhi.setItems(hoaDonList);
        }
        
        System.out.println("=== END DEBUG: Setting table data ===");
    }

    /**
     * Cập nhật thông tin căn hộ lên UI
     */
    private void updateThongTinCanHo() {
        if (currentCanHo == null) {
            clearThongTinCanHo();
            return;
        }

        if (labelMaCanHo != null) labelMaCanHo.setText(currentCanHo.getMaCanHo());
        if (labelSoNha != null) labelSoNha.setText(currentCanHo.getSoNha());
        if (labelTang != null) labelTang.setText(currentCanHo.getTang());
        if (labelToa != null) labelToa.setText(currentCanHo.getToaNha());
        if (labelDienTich != null) labelDienTich.setText(String.valueOf(currentCanHo.getDienTich()) + " m²");
        if (labelTinhTrangKiThuat != null) labelTinhTrangKiThuat.setText(currentCanHo.getTrangThaiKiThuat());
        if (labelTinhTrangSuDung != null) labelTinhTrangSuDung.setText(currentCanHo.getTrangThaiSuDung());
        
        // Cập nhật thông tin chủ sở hữu nếu có
        if (currentCanHo.getChuHo() != null) {
            if (labelMaDinhDanh != null) labelMaDinhDanh.setText(currentCanHo.getChuHo().getMaDinhDanh());
            if (labelHoVaTen != null) labelHoVaTen.setText(currentCanHo.getChuHo().getHoVaTen());
            
            // Hide fields that are not available in ChuHoDto
            if (labelNgaySinh != null) labelNgaySinh.setVisible(false);
            if (labelGioiTinh != null) labelGioiTinh.setVisible(false);
            
            if (labelTrangThaiChuSoHuu != null) labelTrangThaiChuSoHuu.setText(currentCanHo.getChuHo().getTrangThaiCuTru());
            if (labelSoDienThoai != null) labelSoDienThoai.setText(currentCanHo.getChuHo().getSoDienThoai());
            if (labelEmail != null) labelEmail.setText(currentCanHo.getChuHo().getEmail());
            
            // Hiển thị VBox chủ sở hữu
            if (vBoxChuSoHuu != null) vBoxChuSoHuu.setVisible(true);
        } else {
            // Ẩn VBox chủ sở hữu nếu chưa có chủ
            if (vBoxChuSoHuu != null) vBoxChuSoHuu.setVisible(false);
        }
    }

    // Tab navigation methods
    @FXML
    private void showThongTinTab() {
        hideAllTabs();
        if (anchorPaneThongTin != null) {
            anchorPaneThongTin.setVisible(true);
        }
        updateTabStyles("thongtin");
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
        
        // Debug table visibility
        if (tableViewCuDan != null) {
            System.out.println("tableViewCuDan visible: " + tableViewCuDan.isVisible());
            System.out.println("tableViewCuDan items: " + tableViewCuDan.getItems().size());
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
            if (currentCanHo != null) {
                loadDataFromService(currentCanHo.getMaCanHo());
            }

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
            
            // Refresh data sau khi đóng
            if (currentCanHo != null) {
                loadDataFromService(currentCanHo.getMaCanHo());
            }

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
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận xóa");
            confirmAlert.setHeaderText("Xóa phương tiện");
            confirmAlert.setContentText("Bạn có chắc chắn muốn xóa phương tiện " + phuongTien.getBienSo() + "?");
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
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
                                
                                // Refresh data
                                if (currentCanHo != null) {
                                    loadDataFromService(currentCanHo.getMaCanHo());
                                }
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
            });
            
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
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận thu phí");
        confirmAlert.setHeaderText("Thu toàn bộ phí");
        confirmAlert.setContentText(String.format("Tổng số tiền: %,d VNĐ\nBạn có chắc chắn muốn thu toàn bộ?", tongTien));
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Implement thu phí logic here
                showSuccess("Thành công", "Đã thu toàn bộ phí thành công");
                // Reload data after payment
                if (currentCanHo != null) {
                    loadDataFromService(currentCanHo.getMaCanHo());
                }
            }
        });
    }

    @FXML
    private void handleXemLichSu() {
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
            if (currentCanHo == null) {
                showError("Lỗi", "Không có thông tin căn hộ để chỉnh sửa");
                return;
            }
            
            System.out.println("DEBUG: Opening edit apartment form for: " + currentCanHo.getMaCanHo());
            
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/them_can_ho.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            // Get controller and setup for edit mode
            io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller = loader.getController();
            if (controller != null) {
                // Setup edit mode - hide unnecessary sections and change title
                setupEditMode(controller);
                
                // Populate apartment data
                populateApartmentData(controller);
                
                // Inject service if available
                if (canHoService != null) {
                    controller.setCanHoService(canHoService);
                    System.out.println("DEBUG: Injected CanHoService to edit form controller");
                }
            }
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Chỉnh sửa căn hộ - " + currentCanHo.getMaCanHo());
            stage.setScene(new javafx.scene.Scene(root, 750, 400)); // Smaller height since we hide sections
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            stage.initOwner(button_close_up.getScene().getWindow());
            stage.show();
            
            System.out.println("DEBUG: Edit apartment window opened successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR: Exception in handleChinhSuaCanHo: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi mở chỉnh sửa", "Không thể mở form chỉnh sửa căn hộ: " + e.getMessage());
        }
    }

    /**
     * Setup edit mode - hide unnecessary sections and change text
     */
    private void setupEditMode(io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller) {
        try {
            // Use reflection to access private fields
            java.lang.reflect.Field vBoxChuSoHuuField = controller.getClass().getDeclaredField("vBoxChuSoHuu");
            vBoxChuSoHuuField.setAccessible(true);
            javafx.scene.layout.VBox vBoxChuSoHuu = (javafx.scene.layout.VBox) vBoxChuSoHuuField.get(controller);
            
            java.lang.reflect.Field vBoxThongTinCuDanMoiField = controller.getClass().getDeclaredField("vBoxThongTinCuDanMoi");
            vBoxThongTinCuDanMoiField.setAccessible(true);
            javafx.scene.layout.VBox vBoxThongTinCuDanMoi = (javafx.scene.layout.VBox) vBoxThongTinCuDanMoiField.get(controller);
            
            java.lang.reflect.Field choiceBoxThemChuSoHuuField = controller.getClass().getDeclaredField("choiceBoxThemChuSoHuu");
            choiceBoxThemChuSoHuuField.setAccessible(true);
            javafx.scene.control.CheckBox choiceBoxThemChuSoHuu = (javafx.scene.control.CheckBox) choiceBoxThemChuSoHuuField.get(controller);
            
            java.lang.reflect.Field choiceBoxTaoCuDanMoiField = controller.getClass().getDeclaredField("choiceBoxTaoCuDanMoi");
            choiceBoxTaoCuDanMoiField.setAccessible(true);
            javafx.scene.control.CheckBox choiceBoxTaoCuDanMoi = (javafx.scene.control.CheckBox) choiceBoxTaoCuDanMoiField.get(controller);
            
            java.lang.reflect.Field buttonTaoCanHoField = controller.getClass().getDeclaredField("buttonTaoCanHo");
            buttonTaoCanHoField.setAccessible(true);
            javafx.scene.control.Button buttonTaoCanHo = (javafx.scene.control.Button) buttonTaoCanHoField.get(controller);
            
            java.lang.reflect.Field labelTitleField = controller.getClass().getDeclaredField("labelTitle");
            labelTitleField.setAccessible(true);
            javafx.scene.control.Label labelTitle = (javafx.scene.control.Label) labelTitleField.get(controller);
            
            // Hide unnecessary sections
            if (vBoxChuSoHuu != null) {
                vBoxChuSoHuu.setVisible(false);
                vBoxChuSoHuu.setManaged(false);
            }
            
            if (vBoxThongTinCuDanMoi != null) {
                vBoxThongTinCuDanMoi.setVisible(false);
                vBoxThongTinCuDanMoi.setManaged(false);
            }
            
            // Hide checkboxes
            if (choiceBoxThemChuSoHuu != null) {
                choiceBoxThemChuSoHuu.setVisible(false);
                choiceBoxThemChuSoHuu.setManaged(false);
            }
            
            if (choiceBoxTaoCuDanMoi != null) {
                choiceBoxTaoCuDanMoi.setVisible(false);
                choiceBoxTaoCuDanMoi.setManaged(false);
            }
            
            // Change button text and title
            if (buttonTaoCanHo != null) {
                buttonTaoCanHo.setText("Cập nhật căn hộ");
            }
            
            if (labelTitle != null) {
                labelTitle.setText("Chỉnh sửa căn hộ");
            }
            
            System.out.println("DEBUG: Edit mode setup completed - unnecessary sections hidden");
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot setup edit mode: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Populate apartment data into form fields
     */
    private void populateApartmentData(io.github.ktpm.bluemoonmanagement.controller.ThemCanHoButton controller) {
        try {
            if (currentCanHo == null) return;
            
            // Use reflection to access private fields and set values
            java.lang.reflect.Field textFieldToaField = controller.getClass().getDeclaredField("textFieldToa");
            textFieldToaField.setAccessible(true);
            javafx.scene.control.TextField textFieldToa = (javafx.scene.control.TextField) textFieldToaField.get(controller);
            
            java.lang.reflect.Field textFieldTangField = controller.getClass().getDeclaredField("textFieldTang");
            textFieldTangField.setAccessible(true);
            javafx.scene.control.TextField textFieldTang = (javafx.scene.control.TextField) textFieldTangField.get(controller);
            
            java.lang.reflect.Field textFieldSoNhaField = controller.getClass().getDeclaredField("textFieldSoNha");
            textFieldSoNhaField.setAccessible(true);
            javafx.scene.control.TextField textFieldSoNha = (javafx.scene.control.TextField) textFieldSoNhaField.get(controller);
            
            java.lang.reflect.Field textFieldDienTichField = controller.getClass().getDeclaredField("textFieldDienTich");
            textFieldDienTichField.setAccessible(true);
            javafx.scene.control.TextField textFieldDienTich = (javafx.scene.control.TextField) textFieldDienTichField.get(controller);
            
            java.lang.reflect.Field comboBoxTinhTrangKiThuatField = controller.getClass().getDeclaredField("comboBoxTinhTrangKiThuat");
            comboBoxTinhTrangKiThuatField.setAccessible(true);
            javafx.scene.control.ComboBox<String> comboBoxTinhTrangKiThuat = (javafx.scene.control.ComboBox<String>) comboBoxTinhTrangKiThuatField.get(controller);
            
            java.lang.reflect.Field comboBoxTinhTrangSuDungField = controller.getClass().getDeclaredField("comboBoxTinhTrangSuDung");
            comboBoxTinhTrangSuDungField.setAccessible(true);
            javafx.scene.control.ComboBox<String> comboBoxTinhTrangSuDung = (javafx.scene.control.ComboBox<String>) comboBoxTinhTrangSuDungField.get(controller);
            
            // Set values
            if (textFieldToa != null && currentCanHo.getToaNha() != null) {
                textFieldToa.setText(currentCanHo.getToaNha());
            }
            
            if (textFieldTang != null && currentCanHo.getTang() != null) {
                textFieldTang.setText(currentCanHo.getTang());
            }
            
            if (textFieldSoNha != null && currentCanHo.getSoNha() != null) {
                textFieldSoNha.setText(currentCanHo.getSoNha());
            }
            
            if (textFieldDienTich != null && currentCanHo.getDienTich() != 0.0) {
                textFieldDienTich.setText(String.valueOf(currentCanHo.getDienTich()));
            }
            
            if (comboBoxTinhTrangKiThuat != null && currentCanHo.getTrangThaiKiThuat() != null) {
                comboBoxTinhTrangKiThuat.setValue(currentCanHo.getTrangThaiKiThuat());
            }
            
            if (comboBoxTinhTrangSuDung != null && currentCanHo.getTrangThaiSuDung() != null) {
                comboBoxTinhTrangSuDung.setValue(currentCanHo.getTrangThaiSuDung());
                comboBoxTinhTrangSuDung.setDisable(false); // Enable it for editing
            }
            
            System.out.println("DEBUG: Apartment data populated successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR: Cannot populate apartment data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Thiết lập dữ liệu căn hộ từ bên ngoài
     */
    public void setCanHoData(CanHoChiTietDto canHoData) {
        try {
            System.out.println("DEBUG: setCanHoData called with: " + (canHoData != null ? canHoData.getMaCanHo() : "NULL"));
            
            if (canHoData != null) {
                System.out.println("DEBUG: About to call loadDataFromService for: " + canHoData.getMaCanHo());
                // Luôn luôn load dữ liệu đầy đủ từ service/database
                loadDataFromService(canHoData.getMaCanHo());
                System.out.println("DEBUG: loadDataFromService completed successfully");
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        } catch (Exception e) {
            System.err.println("ERROR: Cannot get services from ApplicationContext: " + e.getMessage());
        }
    }
    
    /**
     * Refresh dữ liệu từ database - gọi khi cần cập nhật sau khi thêm/sửa/xóa
     */
    public void refreshData() {
        if (currentCanHo != null && currentCanHo.getMaCanHo() != null) {
            System.out.println("Refreshing data for apartment: " + currentCanHo.getMaCanHo());
            loadDataFromService(currentCanHo.getMaCanHo());
        }
    }
    
    /**
     * Static method để refresh tất cả cửa sổ chi tiết đang mở cho một căn hộ
     */
    public static void refreshAllWindowsForApartment(String maCanHo) {
        System.out.println("=== DEBUG: Refreshing all windows for apartment: " + maCanHo + " ===");
        System.out.println("Number of open windows: " + openWindows.size());
        
        for (ChiTietCanHoController controller : new ArrayList<>(openWindows)) {
            if (controller.currentCanHo != null && 
                maCanHo.equals(controller.currentCanHo.getMaCanHo())) {
                System.out.println("Found matching window for apartment: " + maCanHo);
                controller.refreshData();
            }
        }
        System.out.println("=== END DEBUG ===");
    }
} 