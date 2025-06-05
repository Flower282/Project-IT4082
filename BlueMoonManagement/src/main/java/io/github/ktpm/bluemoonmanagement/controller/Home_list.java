package io.github.ktpm.bluemoonmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.QuanLyTaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
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
import javafx.scene.control.Alert;
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
    private ComboBox<?> comboBoxResultNumber;

    @FXML
    private ComboBox<?> comboBoxResultNumberCuDan;

    @FXML
    private ComboBox<?> comboBoxResultNumberKhoanThu;

    @FXML
    private ComboBox<?> comboBoxResultNumberKhoanThu1;

    @FXML
    private ComboBox<?> comboBoxResultNumberThuPhi;

    @FXML
    private ComboBox<?> comboBoxTang;

    @FXML
    private ComboBox<?> comboBoxToa;

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
    private TableColumn<?, ?> tableColumnNgayTao;

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

    private List<Node> allPanes;
    private KhungController parentController;

    private ObservableList<CanHoTableData> canHoList;
    private ObservableList<CanHoTableData> filteredList;
    
    private ObservableList<CuDanTableData> cuDanList;
    private ObservableList<CuDanTableData> filteredCuDanList;

    private ObservableList<TaiKhoanTableData> taiKhoanList;
    private ObservableList<TaiKhoanTableData> filteredTaiKhoanList;

    // Pagination variables
    private int currentPageCuDan = 1;
    private int itemsPerPageCuDan = 10;
    private int totalPagesCuDan = 1;
    private List<CuDanTableData> allCuDanData;

    // Reference to table view for CanHo được inject từ FXML

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Home_list controller được khởi tạo");
        System.out.println("CanHoService status: " + (canHoService != null ? "Đã inject" : "NULL - Chưa inject"));
        System.out.println("CuDanService status: " + (cuDanService != null ? "Đã inject" : "NULL - Chưa inject"));
        
        allPanes = List.of(gridPaneTrangChu, scrollPaneCanHo, scrollPaneCuDan, scrollPaneTaiKhoan, scrollPaneKhoanThu, scrollPaneLichSuThu, scrollPaneCanHo1);
        
        // Initialize data lists
        canHoList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();
        cuDanList = FXCollections.observableArrayList();
        filteredCuDanList = FXCollections.observableArrayList();
        
        // Setup tables
        setupCanHoTable();
        setupCuDanTable();
        setupTaiKhoanTable();
        // Load data
        loadData();
        loadCuDanData();
        loadTaiKhoanData();
        // Show default tab
        show("TrangChu");
        
        // Setup user info labels
        if (Session.getCurrentUser() != null) {
            hoTenLabel.setText("Họ Tên: " + Session.getCurrentUser().getHoTen());
            emailLabel.setText("Email: " + Session.getCurrentUser().getEmail());
            vaiTroLabel.setText("Vai trò: " + Session.getCurrentUser().getVaiTro());
        }
        
        System.out.println("Home_list đã được khởi tạo");
    }

    public void setParentController(KhungController controller) {
        this.parentController = controller;
    }

    public void show(String key) {
        for (Node pane : allPanes) {
            pane.setVisible(false);
        }

        switch (key) {
            case "TrangChu" -> gridPaneTrangChu.setVisible(true);
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
        
        // Setup table và load dữ liệu
        setupCanHoTable();
        loadData();
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
    }

    @FXML
    void gotoKhoanThu(ActionEvent event) {
        show("KhoanThu");
        if (parentController != null) {
            parentController.updateScreenLabel("Danh sách khoản thu");
        }
    }
    @FXML
    private void gotothemcanho(ActionEvent event) {
        try {
            System.out.println("Mở form thêm căn hộ...");
            
            // Sử dụng FXMLLoader thông thường cho JavaFX modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/them_can_ho.fxml"));
            
            // Load view
            Parent root = loader.load();
            
            // Get controller từ FXML
            ThemCanHoButton controller = loader.getController();
            
            // Inject service sau khi load
            if (canHoService != null && controller != null) {
                controller.setCanHoService(canHoService);
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
            
            // Sau khi đóng form, reload dữ liệu
            System.out.println("Form thêm căn hộ đã đóng, reload dữ liệu...");
            loadData();
            
        } catch (Exception e) {
            showError("Lỗi khi mở form thêm căn hộ", "Chi tiết: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Reloads data for all tables and views
     */
    private void loadData() {
        try {
            if (canHoService != null) {
                List<CanHoDto> canHoDtoList = canHoService.getAllCanHo();
                canHoList = FXCollections.observableArrayList();
                
                if (canHoDtoList != null) {
                    for (CanHoDto dto : canHoDtoList) {
                        // Kiểm tra xem căn hộ có cư dân còn đang ở không
                        if (shouldShowApartment(dto)) {
                            String chuHoName = dto.getChuHo() != null ? dto.getChuHo().getHoVaTen() : "";
                            CanHoTableData tableData = new CanHoTableData(
                                dto.getMaCanHo(),
                                dto.getToaNha(),
                                dto.getTang(),
                                dto.getSoNha(),
                                dto.getDienTich() + " m²",
                                chuHoName,
                                dto.getTrangThaiSuDung(),
                                dto.getTrangThaiKiThuat()
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
            } else {
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(title);
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
            
            // Thêm sự kiện single-click để xem chi tiết căn hộ
            typedTableView.setRowFactory(tv -> {
                javafx.scene.control.TableRow<CanHoTableData> row = new javafx.scene.control.TableRow<>();
                row.setOnMouseClicked(event -> {
                    // Single-click vào bất kỳ chỗ nào của dòng sẽ mở chi tiết
                    if (!row.isEmpty() && event.getClickCount() == 1) {
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

        public CanHoTableData(String maCanHo, String toaNha, String tang, String soNha, 
                            String dienTich, String chuHo, String trangThaiSuDung, String trangThaiKiThuat) {
            this.maCanHo = maCanHo;
            this.toaNha = toaNha;
            this.tang = tang;
            this.soNha = soNha;
            this.dienTich = dienTich;
            this.chuHo = chuHo;
            this.trangThaiSuDung = trangThaiSuDung;
            this.trangThaiKiThuat = trangThaiKiThuat;
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

        // Setters
        public void setMaCanHo(String maCanHo) { this.maCanHo = maCanHo; }
        public void setToaNha(String toaNha) { this.toaNha = toaNha; }
        public void setTang(String tang) { this.tang = tang; }
        public void setSoNha(String soNha) { this.soNha = soNha; }
        public void setDienTich(String dienTich) { this.dienTich = dienTich; }
        public void setChuHo(String chuHo) { this.chuHo = chuHo; }
        public void setTrangThaiSuDung(String trangThaiSuDung) { this.trangThaiSuDung = trangThaiSuDung; }
        public void setTrangThaiKiThuat(String trangThaiKiThuat) { this.trangThaiKiThuat = trangThaiKiThuat; }
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
    public class TaiKhoanTableData {
        private String email;
        private String hoVaTen;
        private String vaiTro;
        private String ngayTao;
        private String ngayCapNhat;

        public TaiKhoanTableData(String email, String hoVaTen, String vaiTro,String ngayTao ,String ngayCapNhat) {
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
        public String getNgayTao(){ return ngayTao; }
        // Setters
        public void setEmail(String email) { this.email = email; }
        public void setHoVaTen(String hoVaTen) { this.hoVaTen = hoVaTen; }
        public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
        public void setNgayTao(String ngayTao){this.ngayTao = ngayTao; }
        public void setNgayCapNhat(String ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }

    }

    public void setupTaiKhoanTable() {
        if( tabelViewTaiKhoan != null && tableColumnEmail != null) {
            // Cast table view to correct type
            TableView<TaiKhoanTableData> typedTableView = (TableView<TaiKhoanTableData>) tabelViewTaiKhoan;

            // Setup cell value factories - need to cast to proper types
            ((TableColumn<TaiKhoanTableData, String>) tableColumnEmail).setCellValueFactory(new PropertyValueFactory<>("email"));
            ((TableColumn<TaiKhoanTableData, String>) tableColumnHoVaTenTaiKhoan).setCellValueFactory(new PropertyValueFactory<>("hoVaTen"));
            ((TableColumn<TaiKhoanTableData, String>) tableColumnVaiTro).setCellValueFactory(new PropertyValueFactory<>("vaiTro"));
            ((TableColumn<TaiKhoanTableData, String>) tableColumnNgayTao).setCellValueFactory(new PropertyValueFactory<>("ngayTao"));
            ((TableColumn<TaiKhoanTableData, String>) tableColumnNgayCapNhat).setCellValueFactory(new PropertyValueFactory<>("ngayCapNhat"));

            // Thêm sự kiện single-click để xem chi tiết tài khoản
            typedTableView.setRowFactory(tv -> {
                javafx.scene.control.TableRow<TaiKhoanTableData> row = new javafx.scene.control.TableRow<>();
                row.setOnMouseClicked(event -> {
                    // Single-click vào bất kỳ chỗ nào của dòng sẽ mở chi tiết
                    if (!row.isEmpty() && event.getClickCount() == 1) {
                        TaiKhoanTableData rowData = row.getItem();
                        handleXemChiTietTaiKhoan(rowData);
                    }
                });
                return row;
            });
        }
    }

    private void handleXemChiTietTaiKhoan(TaiKhoanTableData rowData) {
//        try {
//            if (taiKhoanService != null) {
//
//                String email = rowData.getEmail();
//                String hoVaTen = rowData.getHoVaTen();
//                String vaiTro = rowData.getVaiTro();
//                String ngayTao = rowData.getNgayTao();
//                String ngayCapNhat = rowData.getNgayCapNhat();
//
//
//                ThongTinTaiKhoanDto thongTinTaiKhoanDto = new ThongTinTaiKhoanDto(email, hoVaTen, vaiTro);
//                if (thongTinTaiKhoanDto != null) {
//                    openChiTietTaiKhoan(thongTinTaiKhoanDto);
//                } else {
//                    showError("Lỗi", "Không tìm thấy thông tin chi tiết tài khoản");
//                }
//            } else {
//                showError("Lỗi", "Dịch vụ tài khoản không khả dụng");
//            }
//        } catch (Exception e) {
//            showError("Lỗi khi xem chi tiết", "Chi tiết: " + e.getMessage());
//        }
    }

//    private void openChiTietTaiKhoan(ThongTinTaiKhoanDto thongTinTaiKhoanDto) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/tai_khoan.fxml"));
//            Parent root = loader.load();
//
//            ChiTietTaiKhoanController controller = loader.getController();
//            controller.setTaiKhoanService(taiKhoanService);
//            controller.setThongTinTaiKhoan(thongTinTaiKhoanDto);
//
//            Stage stage = new Stage();
//            stage.setTitle("Chi tiết tài khoản - " + thongTinTaiKhoanDto.getEmail());
//            stage.setScene(new Scene(root, 600, 400));
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(tabelViewTaiKhoan.getScene().getWindow());
//            stage.show();
//        } catch (IOException e) {
//            showError("Lỗi mở chi tiết", "Không thể mở trang chi tiết tài khoản: " + e.getMessage());
//        }
//    }

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
            ((TableColumn<CuDanTableData, String>) tableColumnSoDienThoai).setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
            ((TableColumn<CuDanTableData, String>) tableColumnEmail).setCellValueFactory(new PropertyValueFactory<>("email"));
            ((TableColumn<CuDanTableData, String>) tableColumnMaCanHoCuDan).setCellValueFactory(new PropertyValueFactory<>("maCanHo"));
            ((TableColumn<CuDanTableData, String>) tableColumnTrangThaiCuDan).setCellValueFactory(new PropertyValueFactory<>("trangThaiCuTru"));
            ((TableColumn<CuDanTableData, String>) tableColumnNgayChuyenDen).setCellValueFactory(new PropertyValueFactory<>("ngayChuyenDen"));
            
            // Setup action column with delete button
            ((TableColumn<CuDanTableData, String>) tableColumnThaoTacCuDan).setCellFactory(col -> {
                return new javafx.scene.control.TableCell<CuDanTableData, String>() {
                    private final javafx.scene.control.Button deleteButton = new javafx.scene.control.Button("Xóa");
                    
                    {
                        deleteButton.getStyleClass().addAll("action-button", "button-red");
                        
                        // Kiểm tra quyền của người dùng và disable nút nếu không phải "Tổ phó"
                        boolean isToPhO = false;
                        try {
                            if (Session.getCurrentUser() != null) {
                                String vaiTro = Session.getCurrentUser().getVaiTro();
                                isToPhO = "Tổ phó".equals(vaiTro);
                            }
                        } catch (Exception e) {
                            System.err.println("Lỗi khi kiểm tra vai trò cho nút xóa: " + e.getMessage());
                            isToPhO = false;
                        }
                        
                        deleteButton.setDisable(!isToPhO);
                        
                        if (!isToPhO) {
                            // Thêm tooltip giải thích tại sao nút bị disable
                            javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                                "Chỉ người dùng có vai trò 'Tổ phó' mới có thể xóa cư dân");
                            javafx.scene.control.Tooltip.install(deleteButton, tooltip);
                        }
                        
                        deleteButton.setOnAction(event -> {
                            try {
                                // Double check quyền trước khi thực hiện xóa
                                if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
                                    showError("Lỗi quyền", "Bạn không có quyền xóa cư dân.\nChỉ người dùng có vai trò 'Tổ phó' mới được phép thực hiện thao tác này.");
                                    return;
                                }
                                
                                int index = getIndex();
                                if (index >= 0 && index < getTableView().getItems().size()) {
                                    CuDanTableData cuDan = getTableView().getItems().get(index);
                                    if (cuDan != null) {
                                        handleDeleteCuDan(cuDan);
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Lỗi khi xóa cư dân: " + e.getMessage());
                                e.printStackTrace();
                                showError("Lỗi", "Có lỗi xảy ra khi xóa cư dân: " + e.getMessage());
                            }
                        });
                    }
                    
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getIndex() < 0) {
                            setGraphic(null);
                        } else {
                            javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox();
                            hbox.setAlignment(javafx.geometry.Pos.CENTER);
                            hbox.getChildren().add(deleteButton);
                            setGraphic(hbox);
                        }
                    }
                };
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
            
            // Thiết lập chiều cao và chiều rộng giống với trang căn hộ
            typedTableView.setPrefHeight(400); // Giống với trang căn hộ
            typedTableView.setMaxHeight(400);
            typedTableView.setPrefWidth(970); // Giống với trang căn hộ
            typedTableView.setMaxWidth(970);
            
            // Cải thiện hiệu suất cuộn
            typedTableView.setRowFactory(tv -> {
                javafx.scene.control.TableRow<CuDanTableData> row = new javafx.scene.control.TableRow<>();
                return row;
            });
            
            // Cho phép cuộn bằng phím mũi tên và chuột
            typedTableView.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.UP || 
                    event.getCode() == javafx.scene.input.KeyCode.DOWN ||
                    event.getCode() == javafx.scene.input.KeyCode.PAGE_UP ||
                    event.getCode() == javafx.scene.input.KeyCode.PAGE_DOWN) {
                    // Các phím này sẽ tự động cuộn TableView
                }
            });
            
            // Thêm tooltip hướng dẫn
            javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                "Bảng hiển thị tối đa 15 dòng. Sử dụng chuột hoặc phím mũi tên để cuộn xem thêm dữ liệu");
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
                
                // Setup edit mode với dữ liệu cư dân hiện tại
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
     * Load dữ liệu cư dân từ service
     */
    private void loadCuDanData() {
        try {
            if (cuDanService != null) {
                List<io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto> cuDanDtoList = cuDanService.getAllCuDan();
                cuDanList.clear();
                
                if (cuDanDtoList != null) {
                    for (io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto dto : cuDanDtoList) {
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
                labelKetQuaHienThiCuDan.setText(String.format("Hiển thị %d đầu tiên / %d cư dân (cuộn để xem thêm)", visibleRows, total));
            }
        }
    }

    /**
     * Thiết lập quyền cho các nút dựa trên vai trò người dùng
     * Chỉ "Tổ phó" mới có thể thêm cư dân và căn hộ
     */
    private void setupButtonPermissions() {
        boolean isToPhO = false;

        try {
            if (Session.getCurrentUser() != null) {
                String vaiTro = Session.getCurrentUser().getVaiTro();
                isToPhO = "Tổ phó".equals(vaiTro);
                System.out.println("DEBUG: User role = " + vaiTro + ", isToPhO = " + isToPhO);
            } else {
                System.out.println("DEBUG: Không có user hiện tại");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra vai trò người dùng: " + e.getMessage());
            isToPhO = false; // Mặc định không có quyền
        }

        // Disable/enable các nút thêm dựa trên quyền
        if (buttonThemCuDan != null) {
            buttonThemCuDan.setDisable(!isToPhO);
            if (!isToPhO) {
                // Thêm tooltip giải thích tại sao nút bị disable
                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                    "Chỉ người dùng có vai trò 'Tổ phó' mới có thể thêm cư dân");
                javafx.scene.control.Tooltip.install(buttonThemCuDan, tooltip);
                System.out.println("DEBUG: Đã disable nút thêm cư dân");
            }
        }

        if (buttonThemCanHo != null) {
            buttonThemCanHo.setDisable(!isToPhO);
            if (!isToPhO) {
                // Thêm tooltip giải thích tại sao nút bị disable
                javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                    "Chỉ người dùng có vai trò 'Tổ phó' mới có thể thêm căn hộ");
                javafx.scene.control.Tooltip.install(buttonThemCanHo, tooltip);
                System.out.println("DEBUG: Đã disable nút thêm căn hộ");
            }
        }

        System.out.println("DEBUG: Hoàn thành thiết lập quyền nút");
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

}
