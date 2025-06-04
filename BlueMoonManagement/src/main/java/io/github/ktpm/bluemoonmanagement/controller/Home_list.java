package io.github.ktpm.bluemoonmanagement.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.FxView;
import io.github.ktpm.bluemoonmanagement.util.FxViewLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private TableColumn<?, ?> tableColumnNgayChuyenDen1;

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

    private CanHoService canHoService;

    private List<Node> allPanes;

    private KhungController parentController;
    
    // Data for CanHo table
    private ObservableList<CanHoTableData> canHoList;
    private ObservableList<CanHoTableData> filteredList;
    
    // Reference to table view for CanHo (update the existing @FXML annotation)
    private TableView<CanHoTableData> tableViewCanHo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Home_list controller được khởi tạo");
        allPanes = List.of(gridPaneTrangChu, scrollPaneCanHo, scrollPaneCuDan, scrollPaneTaiKhoan, scrollPaneKhoanThu, scrollPaneLichSuThu,scrollPaneCanHo1);
        show("TrangChu");


        hoTenLabel.setText("Họ Tên: " + Session.getCurrentUser().getHoTen());
        emailLabel.setText("Email: " + Session.getCurrentUser().getEmail());
        vaiTroLabel.setText("Vai Trò: " + Session.getCurrentUser().getVaiTro());



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
        parentController.updateScreenLabel("Danh sách căn hộ");
    }

    @FXML
    void goToHoSo(ActionEvent event) {
        show("HoSo");
        parentController.updateScreenLabel("HoSo");
    }

    @FXML
    void gotoCuDan(ActionEvent event) {
        show("CuDan");
        parentController.updateScreenLabel("Danh sách cư dân");
    }

    @FXML
    void gotoKhoanThu(ActionEvent event) {
        show("KhoanThu");
        parentController.updateScreenLabel("Danh sách khoản thu");
    }
    @FXML
    private void gotothemcanho(ActionEvent event) {
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
            
            // Thiết lập style để bỏ khung viền cửa sổ (undecorated)
            dialogStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            
            // Thiết lập modal
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
                if (tableViewCanHo != null) {
                    tableViewCanHo.setItems(filteredList);
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
     * Load dữ liệu mẫu khi service chưa sẵn sàng
     */
    private void loadSampleData() {
        canHoList = FXCollections.observableArrayList();
        
        filteredList = FXCollections.observableArrayList(canHoList);
        if (tableViewCanHo != null) {
            tableViewCanHo.setItems(filteredList);
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
        if (tableViewCanHo != null && tableColumnMaCanHo != null) {
            // Setup cell value factories - need to cast to proper types
            ((TableColumn<CanHoTableData, String>) tableColumnMaCanHo).setCellValueFactory(new PropertyValueFactory<>("maCanHo"));
            ((TableColumn<CanHoTableData, String>) tableColumnToaNha).setCellValueFactory(new PropertyValueFactory<>("toaNha"));
            ((TableColumn<CanHoTableData, String>) tableColumnTang).setCellValueFactory(new PropertyValueFactory<>("tang"));
            ((TableColumn<CanHoTableData, String>) tableColumnSoNha).setCellValueFactory(new PropertyValueFactory<>("soNha"));
            ((TableColumn<CanHoTableData, String>) tableColumnDienTich).setCellValueFactory(new PropertyValueFactory<>("dienTich"));
            ((TableColumn<CanHoTableData, String>) tableColumnChuSoHuu).setCellValueFactory(new PropertyValueFactory<>("chuHo"));
            ((TableColumn<CanHoTableData, String>) tableColumnSuDung).setCellValueFactory(new PropertyValueFactory<>("trangThaiSuDung"));
            ((TableColumn<CanHoTableData, String>) tableColumnKiThuat).setCellValueFactory(new PropertyValueFactory<>("trangThaiKiThuat"));
        }
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


}
