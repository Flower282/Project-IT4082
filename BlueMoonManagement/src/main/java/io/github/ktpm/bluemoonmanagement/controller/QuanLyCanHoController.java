package hometech.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;

/**
 * Controller cho trang quản lý căn hộ
 */
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("QuanLyCanHoController được khởi tạo");
        
        setupTable();
        setupComboBoxes();
        loadSampleData();
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
     * Load dữ liệu mẫu
     */
    private void loadSampleData() {
        canHoList = FXCollections.observableArrayList();
        
        // Thêm dữ liệu mẫu
        canHoList.addAll(
            new CanHoTableData("CH001", "Tòa A", "1", "101", "75.5 m²", "Nguyễn Văn A", "Đang sử dụng", "Tốt"),
            new CanHoTableData("CH002", "Tòa A", "1", "102", "80.0 m²", "Trần Thị B", "Đang sử dụng", "Tốt"),
            new CanHoTableData("CH003", "Tòa A", "2", "201", "75.5 m²", "", "Trống", "Cần sửa chữa"),
            new CanHoTableData("CH004", "Tòa B", "1", "101", "90.0 m²", "Lê Văn C", "Đang sử dụng", "Tốt"),
            new CanHoTableData("CH005", "Tòa B", "2", "202", "85.0 m²", "Phạm Thị D", "Đang sử dụng", "Tốt")
        );
        
        tableViewCanHo.setItems(canHoList);
        updateKetQuaLabel();
    }

    /**
     * Cập nhật label kết quả
     */
    private void updateKetQuaLabel() {
        int total = canHoList.size();
        labelKetQua.setText(String.format("Hiển thị 1 - %d trên tổng số %d căn hộ", total, total));
    }

    // Event handlers
    @FXML
    private void handleThemCanHo(ActionEvent event) {
        try {
            System.out.println("Mở form thêm căn hộ...");
            
            // Load FXML cho form thêm căn hộ
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/HomeTech/them_can_ho.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            // Lấy controller từ FXML loader
            ThemCanHoButton controller = loader.getController();
            
            // TODO: Inject real service - hiện tại dùng mock service
            // Tạo mock service để test (trong thực tế sẽ inject service thật)
            hometech.service.canHo.CanHoService mockService = createMockCanHoService();
            controller.setCanHoService(mockService);
            
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
            dialogStage.setResizable(true); // Cho phép resize trong giới hạn
            
            // Căn giữa cửa sổ
            dialogStage.centerOnScreen();
            
            // Hiển thị dialog và chờ đóng
            dialogStage.showAndWait();
            
            // Sau khi đóng form, reload dữ liệu (nếu có thay đổi)
            System.out.println("Form thêm căn hộ đã đóng, reload dữ liệu...");
            loadSampleData(); // Reload để hiển thị căn hộ mới (nếu có)
            
        } catch (Exception e) {
            System.err.println("Lỗi khi mở form thêm căn hộ: " + e.getMessage());
            e.printStackTrace();
            
            // Hiển thị alert lỗi
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể mở form thêm căn hộ");
            alert.setContentText("Chi tiết lỗi: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleTimKiem(ActionEvent event) {
        System.out.println("Tìm kiếm căn hộ: " + textFieldTimKiem.getText());
        // TODO: Implement search functionality
    }

    @FXML
    private void handleNhapExcel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file Excel để nhập");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
        );
        
        File selectedFile = fileChooser.showOpenDialog(buttonNhapExcel.getScene().getWindow());
        if (selectedFile != null) {
            System.out.println("Nhập từ file: " + selectedFile.getPath());
            // TODO: Implement Excel import
        }
    }

    @FXML
    private void handleXuatExcel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file Excel");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        
        File selectedFile = fileChooser.showSaveDialog(buttonXuatExcel.getScene().getWindow());
        if (selectedFile != null) {
            System.out.println("Xuất ra file: " + selectedFile.getPath());
            // TODO: Implement Excel export
        }
    }

    @FXML
    private void handleLamMoi(ActionEvent event) {
        System.out.println("Làm mới dữ liệu");
        loadSampleData();
    }

    private void handleXemChiTiet(CanHoTableData canHo) {
        System.out.println("Xem chi tiết căn hộ: " + canHo.getMaCanHo());
        // TODO: Mở trang chi tiết căn hộ
    }

    private void handleSuaCanHo(CanHoTableData canHo) {
        System.out.println("Sửa căn hộ: " + canHo.getMaCanHo());
        // TODO: Mở dialog sửa căn hộ
    }

    private void handleXoaCanHo(CanHoTableData canHo) {
        System.out.println("Xóa căn hộ: " + canHo.getMaCanHo());
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa căn hộ này?");
        alert.setContentText("Căn hộ: " + canHo.getMaCanHo() + " - " + canHo.getSoNha());
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                canHoList.remove(canHo);
                updateKetQuaLabel();
                System.out.println("Đã xóa căn hộ: " + canHo.getMaCanHo());
            }
        });
    }

    /**
     * Tạo mock service để test form thêm căn hộ
     * TODO: Thay thế bằng dependency injection thực sự
     */
    private hometech.service.canHo.CanHoService createMockCanHoService() {
        return new hometech.service.canHo.CanHoService() {
            @Override
            public java.util.List<hometech.model.dto.canHo.CanHoDto> getAllCanHo() {
                return new java.util.ArrayList<>();
            }
            
            @Override
            public hometech.model.dto.canHo.CanHoChiTietDto getCanHoChiTiet(hometech.model.dto.canHo.CanHoDto canHoDto) {
                return null;
            }
            
            @Override
            public hometech.model.dto.ResponseDto addCanHo(hometech.model.dto.canHo.CanHoDto canHoDto) {
                try {
                    // Mock implementation
                    System.out.println("=== MOCK THÊM CĂN HỘ ===");
                    
                    // Dùng reflection để lấy dữ liệu từ DTO
                    String maCanHo = getFieldValue(canHoDto, "maCanHo", String.class);
                    String toaNha = getFieldValue(canHoDto, "toaNha", String.class);
                    String tang = getFieldValue(canHoDto, "tang", String.class);
                    String soNha = getFieldValue(canHoDto, "soNha", String.class);
                    Double dienTich = getFieldValue(canHoDto, "dienTich", Double.class);
                    String trangThaiKiThuat = getFieldValue(canHoDto, "trangThaiKiThuat", String.class);
                    String trangThaiSuDung = getFieldValue(canHoDto, "trangThaiSuDung", String.class);
                    
                    System.out.println("Mã căn hộ: " + maCanHo);
                    System.out.println("Tòa nhà: " + toaNha);
                    System.out.println("Tầng: " + tang);
                    System.out.println("Số nhà: " + soNha);
                    System.out.println("Diện tích: " + dienTich);
                    System.out.println("Trạng thái kỹ thuật: " + trangThaiKiThuat);
                    System.out.println("Trạng thái sử dụng: " + trangThaiSuDung);
                    
                    // Kiểm tra chủ hộ
                    hometech.model.dto.cuDan.ChuHoDto chuHo = getFieldValue(canHoDto, "chuHo", hometech.model.dto.cuDan.ChuHoDto.class);
                    if (chuHo != null) {
                        System.out.println("=== THÔNG TIN CHỦ HỘ ===");
                        System.out.println("Mã định danh: " + getFieldValue(chuHo, "maDinhDanh", String.class));
                        System.out.println("Họ và tên: " + getFieldValue(chuHo, "hoVaTen", String.class));
                        System.out.println("SĐT: " + getFieldValue(chuHo, "soDienThoai", String.class));
                        System.out.println("Email: " + getFieldValue(chuHo, "email", String.class));
                        System.out.println("Trạng thái: " + getFieldValue(chuHo, "trangThaiCuTru", String.class));
                    }
                    
                    // Giả lập thêm vào danh sách (trong thực tế sẽ lưu vào DB)
                    String chuHoName = chuHo != null ? getFieldValue(chuHo, "hoVaTen", String.class) : "";
                    
                    CanHoTableData newCanHo = new CanHoTableData(
                        maCanHo, toaNha, tang, soNha, 
                        dienTich + " m²", chuHoName, 
                        trangThaiSuDung, trangThaiKiThuat
                    );
                    
                    // Thêm vào danh sách hiện tại
                    canHoList.add(newCanHo);
                    
                    return createResponseDto(true, "Thêm căn hộ thành công (Mock)");
                    
                } catch (Exception e) {
                    return createResponseDto(false, "Lỗi: " + e.getMessage());
                }
            }
            
            @Override
            public hometech.model.dto.ResponseDto updateCanHo(hometech.model.dto.canHo.CanHoDto canHoDto) {
                return createResponseDto(true, "Cập nhật thành công");
            }
            
            @Override
            public hometech.model.dto.ResponseDto deleteCanHo(hometech.model.dto.canHo.CanHoDto canHoDto) {
                return createResponseDto(true, "Xóa thành công");
            }
            
            @Override
            public hometech.model.dto.ResponseDto importFromExcel(org.springframework.web.multipart.MultipartFile file) {
                return createResponseDto(true, "Import thành công");
            }
            
            @Override
            public hometech.model.dto.ResponseDto exportToExcel(String filePath) {
                return createResponseDto(true, "Export thành công");
            }
            
            // Helper method để lấy field value bằng reflection
            @SuppressWarnings("unchecked")
            private <T> T getFieldValue(Object obj, String fieldName, Class<T> fieldType) {
                try {
                    java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return (T) field.get(obj);
                } catch (Exception e) {
                    return null;
                }
            }
            
            // Helper method để tạo ResponseDto bằng reflection
            private hometech.model.dto.ResponseDto createResponseDto(boolean success, String message) {
                try {
                    hometech.model.dto.ResponseDto response = new hometech.model.dto.ResponseDto();
                    
                    // Set success field
                    java.lang.reflect.Field successField = hometech.model.dto.ResponseDto.class.getDeclaredField("success");
                    successField.setAccessible(true);
                    successField.set(response, success);
                    
                    // Set message field  
                    java.lang.reflect.Field messageField = hometech.model.dto.ResponseDto.class.getDeclaredField("message");
                    messageField.setAccessible(true);
                    messageField.set(response, message);
                    
                    return response;
                } catch (Exception e) {
                    System.err.println("Lỗi khi tạo ResponseDto: " + e.getMessage());
                    return null;
                }
            }
        };
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