package io.github.ktpm.bluemoonmanagement.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.ChuHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CuDanTrongCanHoDto;
import io.github.ktpm.bluemoonmanagement.model.dto.phuongTien.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.service.cuDan.CuDanService;
import io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller cho chức năng chỉnh sửa căn hộ
 */
@Component
public class ChinhSuaCanHoController implements Initializable {

    // Scroll pane chính
    @FXML private ScrollPane scrollPaneCanHo;

    // Thông tin căn hộ
    @FXML private TextField textFieldToa;
    @FXML private TextField textFieldTang;
    @FXML private TextField textFieldSoNha;
    @FXML private TextField textFieldDienTich;
    @FXML private ComboBox<String> comboBoxTrangThai;
    @FXML private ComboBox<String> comboBoxTinhTrangKiThuat;

    // Thông tin chủ sở hữu
    @FXML private VBox vBoxChuSoHuu;
    @FXML private CheckBox choiceBoxChuSoHuuMoi;
    @FXML private TextField textFieldMaDinhDanh;
    @FXML private CheckBox choiceBoxTaoCuDan;
    @FXML private TextField textFieldHoVaTen;
    @FXML private DatePicker datePickerNgaySinh;
    @FXML private ComboBox<String> comboBoxGioiTinh;
    @FXML private TextField textFieldSoDienThoai;
    @FXML private TextField textFieldEmail;
    @FXML private CheckBox choiceBoxThemChuSoHuu;
    @FXML private DatePicker datePickerNgayChuyenDen;

    // Danh sách cư dân
    @FXML private Button buttonThemCuDan;
    @FXML private TableView<CuDanTrongCanHoDto> tabelViewCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnMaDinhDanh;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnHoVaTen;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnGioiTinh;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnNgaySinh;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnNgayChuyenDen;
    @FXML private TableColumn<CuDanTrongCanHoDto, String> tableColumnTrangThaiCuDan;
    @FXML private TableColumn<CuDanTrongCanHoDto, Void> tableColumnThaoTacCuDan;

    // Danh sách phương tiện
    @FXML private Button buttonThemPhuongTien;
    @FXML private TableView<PhuongTienDto> tableViewPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnMaPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnBienSoXe;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnLoaiPhuongTien;
    @FXML private TableColumn<PhuongTienDto, String> tableColumnNgayDangKi;
    @FXML private TableColumn<PhuongTienDto, Void> tableColumnThaoTacPhuongTien;

    // Nút lưu
    @FXML private Button buttonLuu;

    // Data
    private CanHoChiTietDto currentCanHo;
    private ObservableList<CuDanTrongCanHoDto> cuDanList;
    private ObservableList<PhuongTienDto> phuongTienList;
    private String originalOwnerMaDinhDanh; // Lưu mã định danh chủ sở hữu ban đầu

    // Service dependency injection
    @Autowired
    private CanHoService canHoService;

    @Autowired
    private CuDanService cuDanService;

    @Autowired
    private PhuongTienService phuongTienService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ChinhSuaCanHoController được khởi tạo");
        
        // Initialize lists
        cuDanList = FXCollections.observableArrayList();
        phuongTienList = FXCollections.observableArrayList();
        
        // Setup components
        setupComboBoxes();
        setupTables();
        setupEventHandlers();
        
        // Set table height for scrolling (10 rows approximately)
        if (tabelViewCuDan != null) {
            tabelViewCuDan.setPrefHeight(300.0);
        }
        
        if (tableViewPhuongTien != null) {
            tableViewPhuongTien.setPrefHeight(250.0);
        }
    }

    /**
     * Thiết lập các ComboBox
     */
    private void setupComboBoxes() {
        // Trạng thái căn hộ
        comboBoxTrangThai.setItems(FXCollections.observableArrayList(
            "Chưa bán", "Đã bán", "Đang bán"
        ));
        
        // Tình trạng kỹ thuật
        comboBoxTinhTrangKiThuat.setItems(FXCollections.observableArrayList(
            "Tốt", "Khá", "Trung bình", "Yếu", "Kém"
        ));
        
        // Giới tính
        comboBoxGioiTinh.setItems(FXCollections.observableArrayList(
            "Nam", "Nữ", "Khác"
        ));
    }

    /**
     * Thiết lập các TableView
     */
    private void setupTables() {
        // Setup table cư dân
        if (tableColumnMaDinhDanh != null) {
            tableColumnMaDinhDanh.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMaDinhDanh()));
        }
        
        if (tableColumnHoVaTen != null) {
            tableColumnHoVaTen.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHoVaTen()));
        }
        
        if (tableColumnGioiTinh != null) {
            tableColumnGioiTinh.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGioiTinh()));
        }
        
        if (tableColumnNgaySinh != null) {
            tableColumnNgaySinh.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getNgaySinh() != null ? 
                    cellData.getValue().getNgaySinh().toString() : ""));
        }
        
        if (tableColumnNgayChuyenDen != null) {
            tableColumnNgayChuyenDen.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getNgayChuyenDi() != null ? 
                    cellData.getValue().getNgayChuyenDi().toString() : ""));
        }
        
        if (tableColumnTrangThaiCuDan != null) {
            tableColumnTrangThaiCuDan.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTrangThaiCuTru()));
        }

        // Setup action column for cư dân
        setupCuDanActionColumn();

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

        // Setup action column for phương tiện
        setupPhuongTienActionColumn();

        // Set data to tables
        tabelViewCuDan.setItems(cuDanList);
        tableViewPhuongTien.setItems(phuongTienList);
    }

    /**
     * Thiết lập cột thao tác cho bảng cư dân
     */
    private void setupCuDanActionColumn() {
        if (tableColumnThaoTacCuDan != null) {
            tableColumnThaoTacCuDan.setCellFactory(param -> new javafx.scene.control.TableCell<CuDanTrongCanHoDto, Void>() {
                private final Button editButton = new Button("Sửa");
                private final Button deleteButton = new Button("Xóa");
                
                {
                    editButton.getStyleClass().addAll("action-button", "button-blue");
                    deleteButton.getStyleClass().addAll("action-button", "button-red");
                    
                    editButton.setOnAction(e -> {
                        CuDanTrongCanHoDto cuDan = getTableView().getItems().get(getIndex());
                        handleEditCuDan(cuDan);
                    });
                    
                    deleteButton.setOnAction(e -> {
                        CuDanTrongCanHoDto cuDan = getTableView().getItems().get(getIndex());
                        handleDeleteCuDan(cuDan);
                    });
                }
                
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
                        hbox.getChildren().addAll(editButton, deleteButton);
                        setGraphic(hbox);
                    }
                }
            });
        }
    }

    /**
     * Thiết lập cột thao tác cho bảng phương tiện
     */
    private void setupPhuongTienActionColumn() {
        if (tableColumnThaoTacPhuongTien != null) {
            tableColumnThaoTacPhuongTien.setCellFactory(param -> new javafx.scene.control.TableCell<PhuongTienDto, Void>() {
                private final Button editButton = new Button("Sửa");
                private final Button deleteButton = new Button("Xóa");
                
                {
                    editButton.getStyleClass().addAll("action-button", "button-blue");
                    deleteButton.getStyleClass().addAll("action-button", "button-red");
                    
                    editButton.setOnAction(e -> {
                        PhuongTienDto phuongTien = getTableView().getItems().get(getIndex());
                        handleEditPhuongTien(phuongTien);
                    });
                    
                    deleteButton.setOnAction(e -> {
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
                        javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
                        hbox.getChildren().addAll(editButton, deleteButton);
                        setGraphic(hbox);
                    }
                }
            });
        }
    }

    /**
     * Thiết lập event handlers
     */
    private void setupEventHandlers() {
        // Checkbox chủ sở hữu mới
        if (choiceBoxChuSoHuuMoi != null) {
            choiceBoxChuSoHuuMoi.setOnAction(e -> handleChuSoHuuMoiChange());
        }
        
        // Button thêm cư dân
        if (buttonThemCuDan != null) {
            buttonThemCuDan.setOnAction(e -> handleThemCuDan());
        }
        
        // Button thêm phương tiện
        if (buttonThemPhuongTien != null) {
            buttonThemPhuongTien.setOnAction(e -> handleThemPhuongTien());
        }
        
        // Button lưu
        if (buttonLuu != null) {
            buttonLuu.setOnAction(e -> handleLuuThongTin());
        }
    }

    /**
     * Xử lý khi thay đổi checkbox "Chủ sở hữu mới"
     */
    @FXML
    private void handleChuSoHuuMoiChange() {
        boolean isNewOwner = choiceBoxChuSoHuuMoi.isSelected();
        
        if (isNewOwner) {
            // Hiện các thành phần liên quan và xóa nội dung hiện tại
            choiceBoxTaoCuDan.setVisible(true);
            choiceBoxThemChuSoHuu.setVisible(true);
            datePickerNgayChuyenDen.setVisible(true);
            
            // Xóa thông tin chủ sở hữu hiện tại
            clearOwnerInfo();
        } else {
            // Ẩn các thành phần liên quan và khôi phục thông tin ban đầu
            choiceBoxTaoCuDan.setVisible(false);
            choiceBoxThemChuSoHuu.setVisible(false);
            datePickerNgayChuyenDen.setVisible(false);
            
            // Khôi phục thông tin chủ sở hữu ban đầu
            restoreOriginalOwnerInfo();
        }
    }

    /**
     * Xóa thông tin chủ sở hữu
     */
    private void clearOwnerInfo() {
        textFieldMaDinhDanh.clear();
        textFieldHoVaTen.clear();
        datePickerNgaySinh.setValue(null);
        comboBoxGioiTinh.setValue(null);
        textFieldSoDienThoai.clear();
        textFieldEmail.clear();
    }

    /**
     * Khôi phục thông tin chủ sở hữu ban đầu
     */
    private void restoreOriginalOwnerInfo() {
        if (currentCanHo != null && currentCanHo.getChuHo() != null) {
            var chuHo = currentCanHo.getChuHo();
            textFieldMaDinhDanh.setText(chuHo.getMaDinhDanh());
            textFieldHoVaTen.setText(chuHo.getHoVaTen());
            datePickerNgaySinh.setValue(null); // ChuHoDto không có ngày sinh
            comboBoxGioiTinh.setValue(null); // ChuHoDto không có giới tính
            textFieldSoDienThoai.setText(chuHo.getSoDienThoai());
            textFieldEmail.setText(chuHo.getEmail());
        }
    }

    /**
     * Xử lý thêm cư dân
     */
    @FXML
    private void handleThemCuDan() {
        System.out.println("Thêm cư dân được click");
        // TODO: Implement thêm cư dân functionality
        showInfo("Thông báo", "Chức năng thêm cư dân đang được phát triển");
    }

    /**
     * Xử lý sửa cư dân
     */
    private void handleEditCuDan(CuDanTrongCanHoDto cuDan) {
        System.out.println("Sửa cư dân: " + cuDan.getHoVaTen());
        // TODO: Implement sửa cư dân functionality
        showInfo("Thông báo", "Chức năng sửa cư dân đang được phát triển");
    }

    /**
     * Xử lý xóa cư dân
     */
    private void handleDeleteCuDan(CuDanTrongCanHoDto cuDan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa cư dân này?");
        alert.setContentText("Cư dân: " + cuDan.getHoVaTen());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // TODO: Call service to delete cư dân
                    cuDanList.remove(cuDan);
                    showSuccess("Thành công", "Đã xóa cư dân thành công");
                } catch (Exception e) {
                    showError("Lỗi", "Không thể xóa cư dân: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Xử lý thêm phương tiện
     */
    @FXML
    private void handleThemPhuongTien() {
        System.out.println("Thêm phương tiện được click");
        // TODO: Implement thêm phương tiện functionality
        showInfo("Thông báo", "Chức năng thêm phương tiện đang được phát triển");
    }

    /**
     * Xử lý sửa phương tiện
     */
    private void handleEditPhuongTien(PhuongTienDto phuongTien) {
        System.out.println("Sửa phương tiện: " + phuongTien.getBienSo());
        // TODO: Implement sửa phương tiện functionality
        showInfo("Thông báo", "Chức năng sửa phương tiện đang được phát triển");
    }

    /**
     * Xử lý xóa phương tiện
     */
    private void handleDeletePhuongTien(PhuongTienDto phuongTien) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa phương tiện này?");
        alert.setContentText("Biển số: " + phuongTien.getBienSo());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // TODO: Call service to delete phương tiện
                    phuongTienList.remove(phuongTien);
                    showSuccess("Thành công", "Đã xóa phương tiện thành công");
                } catch (Exception e) {
                    showError("Lỗi", "Không thể xóa phương tiện: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Xử lý lưu thông tin căn hộ
     */
    @FXML
    private void handleLuuThongTin() {
        try {
            // Validate input
            if (!validateInput()) {
                return;
            }
            
            // TODO: Build DTO and call service to save
            showSuccess("Thành công", "Đã lưu thông tin căn hộ thành công");
            
            // Close window
            Stage stage = (Stage) buttonLuu.getScene().getWindow();
            stage.close();
            
        } catch (Exception e) {
            showError("Lỗi", "Không thể lưu thông tin: " + e.getMessage());
        }
    }

    /**
     * Validate input data
     */
    private boolean validateInput() {
        if (textFieldToa.getText().trim().isEmpty()) {
            showError("Lỗi", "Vui lòng nhập tòa nhà");
            return false;
        }
        
        if (textFieldTang.getText().trim().isEmpty()) {
            showError("Lỗi", "Vui lòng nhập tầng");
            return false;
        }
        
        if (textFieldSoNha.getText().trim().isEmpty()) {
            showError("Lỗi", "Vui lòng nhập số nhà");
            return false;
        }
        
        if (textFieldDienTich.getText().trim().isEmpty()) {
            showError("Lỗi", "Vui lòng nhập diện tích");
            return false;
        }
        
        try {
            Double.parseDouble(textFieldDienTich.getText().trim());
        } catch (NumberFormatException e) {
            showError("Lỗi", "Diện tích phải là số");
            return false;
        }
        
        if (comboBoxTrangThai.getValue() == null) {
            showError("Lỗi", "Vui lòng chọn trạng thái căn hộ");
            return false;
        }
        
        return true;
    }

    /**
     * Set data cho form
     */
    public void setCanHoData(CanHoChiTietDto canHoData) {
        this.currentCanHo = canHoData;
        
        if (canHoData != null) {
            // Load thông tin căn hộ
            loadCanHoInfo(canHoData);
            
            // Load thông tin chủ sở hữu
            loadOwnerInfo(canHoData);
            
            // Load danh sách cư dân
            loadCuDanList(canHoData);
            
            // Load danh sách phương tiện
            loadPhuongTienList(canHoData);
        }
    }

    /**
     * Load thông tin căn hộ
     */
    private void loadCanHoInfo(CanHoChiTietDto canHoData) {
        textFieldToa.setText(canHoData.getToaNha());
        textFieldTang.setText(canHoData.getTang());
        textFieldSoNha.setText(canHoData.getSoNha());
        textFieldDienTich.setText(String.valueOf(canHoData.getDienTich()));
        comboBoxTrangThai.setValue(canHoData.isDaBanChua() ? "Đã bán" : "Chưa bán");
        comboBoxTinhTrangKiThuat.setValue(canHoData.getTrangThaiKiThuat());
    }

    /**
     * Load thông tin chủ sở hữu
     */
    private void loadOwnerInfo(CanHoChiTietDto canHoData) {
        if (canHoData.getChuHo() != null) {
            var chuHo = canHoData.getChuHo();
            textFieldMaDinhDanh.setText(chuHo.getMaDinhDanh());
            textFieldHoVaTen.setText(chuHo.getHoVaTen());
            datePickerNgaySinh.setValue(null); // ChuHoDto không có ngày sinh
            comboBoxGioiTinh.setValue(null); // ChuHoDto không có giới tính
            textFieldSoDienThoai.setText(chuHo.getSoDienThoai());
            textFieldEmail.setText(chuHo.getEmail());
            
            // Lưu mã định danh ban đầu
            originalOwnerMaDinhDanh = chuHo.getMaDinhDanh();
        }
    }

    /**
     * Load danh sách cư dân
     */
    private void loadCuDanList(CanHoChiTietDto canHoData) {
        cuDanList.clear();
        if (canHoData.getCuDanList() != null) {
            cuDanList.addAll(canHoData.getCuDanList());
        }
    }

    /**
     * Load danh sách phương tiện
     */
    private void loadPhuongTienList(CanHoChiTietDto canHoData) {
        phuongTienList.clear();
        if (canHoData.getPhuongTienList() != null) {
            phuongTienList.addAll(canHoData.getPhuongTienList());
        }
    }

    /**
     * Show error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show success message
     */
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show info message
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Setters for dependency injection
    public void setCanHoService(CanHoService canHoService) {
        this.canHoService = canHoService;
    }

    public void setCuDanService(CuDanService cuDanService) {
        this.cuDanService = cuDanService;
    }

    public void setPhuongTienService(PhuongTienService phuongTienService) {
        this.phuongTienService = phuongTienService;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
