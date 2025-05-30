package io.github.ktpm.bluemoonmanagement.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanTrongCanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.dto.phuongTien.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

/**
 * Controller cho trang chi tiết căn hộ
 */
@Component
public class ChiTietCanHoController implements Initializable {

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
    @FXML private Button buttonTimKiemCuDan;

    // Phương tiện tab
    @FXML private TableView<PhuongTienDto> tableViewPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnMaPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnBienSoXe;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnLoaiPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnNgayDangKi;
    @FXML private TextField textFieldMaSoXe;
    @FXML private ComboBox<String> comboBoxLoaiPhuongTien;
    @FXML private Button buttonTimKiemPhuongTien;
    @FXML private ComboBox<String> comboBoxSoKetQuaPhuongTien;
    @FXML private Label labelHienThiKetQuaPhuongTien;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo tab navigation
        setupTabNavigation();
        
        // Khởi tạo tables
        setupTables();
        
        // Khởi tạo combo boxes
        setupComboBoxes();
        
        // Khởi tạo danh sách rỗng
        cuDanList = FXCollections.observableArrayList();
        phuongTienList = FXCollections.observableArrayList();
        hoaDonList = FXCollections.observableArrayList();
        
        // Hiển thị tab thông tin đầu tiên
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
        // Setup table cư dân
        if (tableColumnMaDinhDanhCuDan != null) {
            tableColumnMaDinhDanhCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMaDinhDanh()));
        }
        if (tableColumnHoVaTenCuDan != null) {
            tableColumnHoVaTenCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoVaTen()));
        }
        if (tableColumnQuanHeChuHo != null) {
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
                    cellData.getValue().getGioiTinh() != null ? 
                    cellData.getValue().getGioiTinh() : ""));
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
                
                // Load danh sách từ service data
                cuDanList.clear();
                if (chiTiet.getCuDanList() != null) {
                    cuDanList.addAll(chiTiet.getCuDanList());
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
        if (tableViewCuDan != null) {
            tableViewCuDan.setItems(cuDanList);
        }
        if (tableViewPhuongTien != null) {
            tableViewPhuongTien.setItems(phuongTienList);
        }
        if (tableViewThuPhi != null) {
            tableViewThuPhi.setItems(hoaDonList);
        }
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
        hideAllTabs();
        if (anchorPaneCuDan != null) {
            anchorPaneCuDan.setVisible(true);
        }
        updateTabStyles("cudan");
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
        String keyword = textFieldTimKiemCuDan != null ? textFieldTimKiemCuDan.getText().trim() : "";
        if (keyword.isEmpty()) {
            setTableData(); // Reset to full list
            return;
        }
        
        // Filter cư dân list based on search keyword
        if (cuDanList != null) {
            ObservableList<CuDanTrongCanHoDto> filteredList = cuDanList.stream()
                .filter(cuDan -> 
                    cuDan.getHoVaTen().toLowerCase().contains(keyword.toLowerCase()) ||
                    cuDan.getMaDinhDanh().toLowerCase().contains(keyword.toLowerCase())
                )
                .collect(FXCollections::observableArrayList, 
                        ObservableList::add, 
                        ObservableList::addAll);
            
            if (tableViewCuDan != null) {
                tableViewCuDan.setItems(filteredList);
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
        // Đóng cửa sổ hiện tại
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) button_close_up.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showError("Lỗi", "Không thể đóng cửa sổ: " + e.getMessage());
        }
    }

    /**
     * Thiết lập dữ liệu căn hộ từ bên ngoài
     */
    public void setCanHoData(CanHoChiTietDto canHoData) {
        if (canHoData != null) {
            // Luôn luôn load dữ liệu đầy đủ từ service/database
            loadDataFromService(canHoData.getMaCanHo());
        } else {
            showError("Lỗi dữ liệu", "Không có thông tin căn hộ để hiển thị");
            clearAllData();
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
    }
} 