package io.github.ktpm.bluemoonmanagement.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

/**
 * Controller cho trang quản lý căn hộ
 */
@Component
public class QuanLyCanHoController implements Initializable {

    // Filter components
    @FXML private ComboBox<String> comboBoxToaNha;
    @FXML private ComboBox<String> comboBoxTang;
    @FXML private ComboBox<String> comboBoxTrangThai;
    @FXML private TextField textFieldTimKiem;
    
    // Action buttons
    @FXML private Button buttonThemCanHo;
    @FXML private Button buttonTimKiem;
    @FXML private Button buttonNhapExcel;
    @FXML private Button buttonXuatExcel;
    @FXML private Button buttonLamMoi;
    
    // Table and columns
    @FXML private TableView<CanHoTableData> tableViewCanHo;
    @FXML private TableColumn<CanHoTableData, String> columnMaCanHo;
    @FXML private TableColumn<CanHoTableData, String> columnToaNha;
    @FXML private TableColumn<CanHoTableData, String> columnTang;
    @FXML private TableColumn<CanHoTableData, String> columnSoNha;
    @FXML private TableColumn<CanHoTableData, String> columnDienTich;
    @FXML private TableColumn<CanHoTableData, String> columnChuHo;
    @FXML private TableColumn<CanHoTableData, String> columnTrangThaiSuDung;
    @FXML private TableColumn<CanHoTableData, String> columnTrangThaiKiThuat;
    @FXML private TableColumn<CanHoTableData, Void> columnThaoTac;
    
    // Footer
    @FXML private Label labelKetQua;

    // Data
    private ObservableList<CanHoTableData> canHoList;
    private ObservableList<CanHoTableData> filteredList;

    // Service dependency injection
    @Autowired
    private CanHoService canHoService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("QuanLyCanHoController được khởi tạo");
        
        setupTable();
        setupComboBoxes();
        loadData();
    }

    /**
     * Thiết lập table
     */
    private void setupTable() {
        // Setup cell value factories
        columnMaCanHo.setCellValueFactory(new PropertyValueFactory<>("maCanHo"));
        columnToaNha.setCellValueFactory(new PropertyValueFactory<>("toaNha"));
        columnTang.setCellValueFactory(new PropertyValueFactory<>("tang"));
        columnSoNha.setCellValueFactory(new PropertyValueFactory<>("soNha"));
        columnDienTich.setCellValueFactory(new PropertyValueFactory<>("dienTich"));
        columnChuHo.setCellValueFactory(new PropertyValueFactory<>("chuHo"));
        columnTrangThaiSuDung.setCellValueFactory(new PropertyValueFactory<>("trangThaiSuDung"));
        columnTrangThaiKiThuat.setCellValueFactory(new PropertyValueFactory<>("trangThaiKiThuat"));
        
        // Setup action column
        columnThaoTac.setCellFactory(param -> new TableCell<CanHoTableData, Void>() {
            private final Button btnXem = new Button("Xem");
            private final Button btnSua = new Button("Sửa");
            private final Button btnXoa = new Button("Xóa");
            
            {
                btnXem.setOnAction(event -> {
                    CanHoTableData canHo = getTableView().getItems().get(getIndex());
                    handleXemChiTiet(canHo);
                });
                
                btnSua.setOnAction(event -> {
                    CanHoTableData canHo = getTableView().getItems().get(getIndex());
                    handleSuaCanHo(canHo);
                });
                
                btnXoa.setOnAction(event -> {
                    CanHoTableData canHo = getTableView().getItems().get(getIndex());
                    handleXoaCanHo(canHo);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
                    hbox.getChildren().addAll(btnXem, btnSua, btnXoa);
                    setGraphic(hbox);
                }
            }
        });
    }

    /**
     * Thiết lập các ComboBox
     */
    private void setupComboBoxes() {
        // Setup ComboBox Tòa nhà
        comboBoxToaNha.setItems(FXCollections.observableArrayList("Tất cả", "Tòa A", "Tòa B", "Tòa C"));
        comboBoxToaNha.setValue("Tất cả");
        
        // Setup ComboBox Tầng
        comboBoxTang.setItems(FXCollections.observableArrayList("Tất cả", "1", "2", "3", "4", "5"));
        comboBoxTang.setValue("Tất cả");
        
        // Setup ComboBox Trạng thái
        comboBoxTrangThai.setItems(FXCollections.observableArrayList(
            "Tất cả", "Đang sử dụng", "Trống", "Đang sửa chữa"
        ));
        comboBoxTrangThai.setValue("Tất cả");
    }

    /**
     * Load dữ liệu từ service
     */
    private void loadData() {
        try {
            if (canHoService != null) {
                List<CanHoDto> canHoDtoList = canHoService.getAllCanHo();
                canHoList = FXCollections.observableArrayList();
                
                for (CanHoDto dto : canHoDtoList) {
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
                
                filteredList = FXCollections.observableArrayList(canHoList);
                tableViewCanHo.setItems(filteredList);
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
     * Load dữ liệu mẫu khi service chưa sẵn sàng
     */
    private void loadSampleData() {
        canHoList = FXCollections.observableArrayList();
        
        filteredList = FXCollections.observableArrayList(canHoList);
        tableViewCanHo.setItems(filteredList);
        updateKetQuaLabel();
    }

    /**
     * Cập nhật label kết quả
     */
    private void updateKetQuaLabel() {
        int total = filteredList != null ? filteredList.size() : 0;
        labelKetQua.setText(String.format("Hiển thị 1 - %d trên tổng số %d căn hộ", total, total));
    }

    // Event handlers
    @FXML
    private void handleThemCanHo(ActionEvent event) {
        try {
            System.out.println("Mở form thêm căn hộ...");
            
            // Load FXML cho form thêm căn hộ
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/them_can_ho.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            // Lấy controller từ FXML loader
            ThemCanHoButton controller = loader.getController();
            
            // Inject service nếu có
            if (canHoService != null) {
                controller.setCanHoService(canHoService);
            }
            
            // Tạo modal dialog
            javafx.stage.Stage dialogStage = new javafx.stage.Stage();
            dialogStage.setTitle("Thêm căn hộ mới");
            dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dialogStage.initOwner(buttonThemCanHo.getScene().getWindow());
            
            // Thiết lập scene với kích thước phù hợp
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 750, 650);
            dialogStage.setScene(scene);
            
            // Thiết lập kích thước cửa sổ
            dialogStage.setMinWidth(700);
            dialogStage.setMinHeight(600);
            dialogStage.setMaxWidth(800);
            dialogStage.setMaxHeight(700);
            dialogStage.setResizable(true);
            
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

    @FXML
    private void handleTimKiem(ActionEvent event) {
        String searchText = textFieldTimKiem.getText().toLowerCase().trim();
        String selectedToaNha = comboBoxToaNha.getValue();
        String selectedTang = comboBoxTang.getValue();
        String selectedTrangThai = comboBoxTrangThai.getValue();
        
        if (canHoList == null) return;
        
        List<CanHoTableData> filtered = canHoList.stream()
            .filter(canHo -> {
                // Filter by search text
                boolean matchesSearch = searchText.isEmpty() || 
                    canHo.getMaCanHo().toLowerCase().contains(searchText) ||
                    canHo.getSoNha().toLowerCase().contains(searchText) ||
                    canHo.getChuHo().toLowerCase().contains(searchText);
                
                // Filter by toa nha
                boolean matchesToaNha = "Tất cả".equals(selectedToaNha) || 
                    canHo.getToaNha().equals(selectedToaNha);
                
                // Filter by tang
                boolean matchesTang = "Tất cả".equals(selectedTang) || 
                    canHo.getTang().equals(selectedTang);
                
                // Filter by trang thai
                boolean matchesTrangThai = "Tất cả".equals(selectedTrangThai) || 
                    canHo.getTrangThaiSuDung().equals(selectedTrangThai);
                
                return matchesSearch && matchesToaNha && matchesTang && matchesTrangThai;
            })
            .collect(Collectors.toList());
        
        filteredList = FXCollections.observableArrayList(filtered);
        tableViewCanHo.setItems(filteredList);
        updateKetQuaLabel();
    }

    @FXML
    private void handleNhapExcel(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Chọn file Excel để nhập");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
            );
            
            File selectedFile = fileChooser.showOpenDialog(buttonNhapExcel.getScene().getWindow());
            if (selectedFile != null && canHoService != null) {
                // Tạo MultipartFile từ File
                try {
                    byte[] fileBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                    
                    // Tạo một MultipartFile implementation đơn giản
                    org.springframework.web.multipart.MultipartFile multipartFile = new org.springframework.web.multipart.MultipartFile() {
                        @Override
                        public String getName() { return "file"; }
                        
                        @Override
                        public String getOriginalFilename() { return selectedFile.getName(); }
                        
                        @Override
                        public String getContentType() { 
                            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; 
                        }
                        
                        @Override
                        public boolean isEmpty() { return fileBytes.length == 0; }
                        
                        @Override
                        public long getSize() { return fileBytes.length; }
                        
                        @Override
                        public byte[] getBytes() { return fileBytes; }
                        
                        @Override
                        public java.io.InputStream getInputStream() { 
                            return new java.io.ByteArrayInputStream(fileBytes); 
                        }
                        
                        @Override
                        public void transferTo(java.io.File dest) throws java.io.IOException {
                            java.nio.file.Files.write(dest.toPath(), fileBytes);
                        }
                    };
                    
                    ResponseDto response = canHoService.importFromExcel(multipartFile);
                    
                    if (response.isSuccess()) {
                        showSuccess("Thành công", response.getMessage());
                        loadData(); // Reload data
                    } else {
                        showError("Lỗi nhập file", response.getMessage());
                    }
                } catch (Exception e) {
                    showError("Lỗi đọc file", "Chi tiết: " + e.getMessage());
                }
            } else if (selectedFile != null) {
                showInfo("Thông báo", "Chức năng nhập Excel sẽ được triển khai khi service sẵn sàng");
            }
        } catch (Exception e) {
            showError("Lỗi nhập Excel", "Chi tiết: " + e.getMessage());
        }
    }

    @FXML
    private void handleXuatExcel(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu file Excel");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            
            File selectedFile = fileChooser.showSaveDialog(buttonXuatExcel.getScene().getWindow());
            if (selectedFile != null && canHoService != null) {
                ResponseDto response = canHoService.exportToExcel(selectedFile.getAbsolutePath());
                
                if (response.isSuccess()) {
                    showSuccess("Thành công", response.getMessage());
                } else {
                    showError("Lỗi xuất file", response.getMessage());
                }
            } else if (selectedFile != null) {
                showInfo("Thông báo", "Chức năng xuất Excel sẽ được triển khai khi service sẵn sàng");
            }
        } catch (Exception e) {
            showError("Lỗi xuất Excel", "Chi tiết: " + e.getMessage());
        }
    }

    @FXML
    private void handleLamMoi(ActionEvent event) {
        System.out.println("Làm mới dữ liệu");
        loadData();
        
        // Reset filters
        textFieldTimKiem.clear();
        comboBoxToaNha.setValue("Tất cả");
        comboBoxTang.setValue("Tất cả");
        comboBoxTrangThai.setValue("Tất cả");
    }

    private void handleXemChiTiet(CanHoTableData canHo) {
        try {
            if (canHoService != null) {
                CanHoDto canHoDto = new CanHoDto();
                canHoDto.setMaCanHo(canHo.getMaCanHo());
                
                CanHoChiTietDto chiTiet = canHoService.getCanHoChiTiet(canHoDto);
                
                if (chiTiet != null) {
                    // Mở trang chi tiết căn hộ
                    openChiTietCanHo(chiTiet);
                } else {
                    showError("Lỗi", "Không tìm thấy thông tin chi tiết căn hộ");
                }
            } else {
                // Tạo dữ liệu mẫu cho chi tiết căn hộ
                CanHoChiTietDto chiTietMau = createSampleChiTiet(canHo);
                openChiTietCanHo(chiTietMau);
            }
        } catch (Exception e) {
            showError("Lỗi khi xem chi tiết", "Chi tiết: " + e.getMessage());
        }
    }

    private void openChiTietCanHo(CanHoChiTietDto chiTiet) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/view/chi_tiet_can_ho.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            ChiTietCanHoController controller = loader.getController();
            controller.setCanHoData(chiTiet);
            
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Chi tiết căn hộ - " + chiTiet.getMaCanHo());
            stage.setScene(new javafx.scene.Scene(root, 1000, 700));
            stage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            stage.initOwner(tableViewCanHo.getScene().getWindow());
            stage.show();
        } catch (Exception e) {
            showError("Lỗi mở chi tiết", "Không thể mở trang chi tiết căn hộ: " + e.getMessage());
        }
    }

    private CanHoChiTietDto createSampleChiTiet(CanHoTableData canHo) {
        CanHoChiTietDto chiTiet = new CanHoChiTietDto();
        chiTiet.setMaCanHo(canHo.getMaCanHo());
        chiTiet.setToaNha(canHo.getToaNha());
        chiTiet.setTang(canHo.getTang());
        chiTiet.setSoNha(canHo.getSoNha());
        chiTiet.setDienTich(Double.parseDouble(canHo.getDienTich().replace(" m²", "")));
        chiTiet.setTrangThaiKiThuat(canHo.getTrangThaiKiThuat());
        chiTiet.setTrangThaiSuDung(canHo.getTrangThaiSuDung());
        return chiTiet;
    }

    private void handleSuaCanHo(CanHoTableData canHo) {
        System.out.println("Sửa căn hộ: " + canHo.getMaCanHo());
        // TODO: Mở dialog sửa căn hộ với service thật
        showInfo("Thông báo", "Chức năng sửa căn hộ sẽ được triển khai sau");
    }

    private void handleXoaCanHo(CanHoTableData canHo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa căn hộ này?");
        alert.setContentText("Căn hộ: " + canHo.getMaCanHo() + " - " + canHo.getSoNha());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (canHoService != null) {
                    try {
                        CanHoDto canHoDto = new CanHoDto();
                        canHoDto.setMaCanHo(canHo.getMaCanHo());
                        
                        ResponseDto result = canHoService.deleteCanHo(canHoDto);
                        
                        if (result.isSuccess()) {
                            showSuccess("Thành công", result.getMessage());
                            loadData(); // Reload data
                        } else {
                            showError("Lỗi khi xóa", result.getMessage());
                        }
                    } catch (Exception e) {
                        showError("Lỗi khi xóa căn hộ", "Chi tiết: " + e.getMessage());
                    }
                } else {
                    // Xóa từ danh sách local khi không có service
                    canHoList.remove(canHo);
                    filteredList.remove(canHo);
                    updateKetQuaLabel();
                    showSuccess("Thành công", "Đã xóa căn hộ: " + canHo.getMaCanHo());
                }
            }
        });
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
} 