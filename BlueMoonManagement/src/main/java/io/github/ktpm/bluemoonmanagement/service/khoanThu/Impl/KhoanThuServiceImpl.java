package io.github.ktpm.bluemoonmanagement.service.khoanThu.Impl;

import java.io.File;
import java.io.FileOutputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;
import io.github.ktpm.bluemoonmanagement.model.dto.phiGuiXe.PhiGuiXeDto;
import io.github.ktpm.bluemoonmanagement.model.entity.KhoanThu;
import io.github.ktpm.bluemoonmanagement.model.entity.PhiGuiXe;
import io.github.ktpm.bluemoonmanagement.model.mapper.KhoanThuMapper;
import io.github.ktpm.bluemoonmanagement.model.mapper.PhiGuiXeMapper;
import io.github.ktpm.bluemoonmanagement.repository.KhoanThuRepository;
import io.github.ktpm.bluemoonmanagement.repository.PhiGuiXeRepository;
import io.github.ktpm.bluemoonmanagement.service.khoanThu.KhoanThuService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.XlsxExportUtil;
import io.github.ktpm.bluemoonmanagement.util.XlxsFileUtil;

@Service
public class KhoanThuServiceImpl implements KhoanThuService {
    private final KhoanThuRepository khoanThuRepository;
    private final PhiGuiXeRepository phiGuiXeRepository;
    private final KhoanThuMapper khoanThuMapper;
    private final PhiGuiXeMapper phiGuiXeMapper;

    public KhoanThuServiceImpl(KhoanThuRepository khoanThuRepository, 
                              PhiGuiXeRepository phiGuiXeRepository,
                              KhoanThuMapper khoanThuMapper,
                              PhiGuiXeMapper phiGuiXeMapper) {
        this.khoanThuRepository = khoanThuRepository;
        this.phiGuiXeRepository = phiGuiXeRepository;
        this.khoanThuMapper = khoanThuMapper;
        this.phiGuiXeMapper = phiGuiXeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhoanThuDto> getAllKhoanThu() {
        List<KhoanThu> khoanThuList = khoanThuRepository.findAllWithPhiGuiXe();
        return khoanThuList.stream()
                .map(khoanThuMapper::toKhoanThuDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseDto addKhoanThu(KhoanThuDto khoanThuDto) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền thêm khoản thu. Chỉ Kế toán mới được phép.");
        }
        
        // Tạo mã khoản thu tự động theo định dạng cũ: TN/BB-YYYYMM-XXX (13 ký tự)
        String maKhoanThu = generateMaKhoanThu(khoanThuDto);
        
        // DEBUG: In ra thông tin chi tiết
        System.out.println("DEBUG: Generated maKhoanThu = '" + maKhoanThu + "' (length: " + maKhoanThu.length() + ")");
        System.out.println("DEBUG: tenKhoanThu = '" + khoanThuDto.getTenKhoanThu() + "' (length: " + (khoanThuDto.getTenKhoanThu() != null ? khoanThuDto.getTenKhoanThu().length() : "null") + ")");
        System.out.println("DEBUG: donViTinh = '" + khoanThuDto.getDonViTinh() + "' (length: " + (khoanThuDto.getDonViTinh() != null ? khoanThuDto.getDonViTinh().length() : "null") + ")");
        System.out.println("DEBUG: phamVi = '" + khoanThuDto.getPhamVi() + "' (length: " + (khoanThuDto.getPhamVi() != null ? khoanThuDto.getPhamVi().length() : "null") + ")");
        System.out.println("DEBUG: ghiChu = '" + khoanThuDto.getGhiChu() + "' (length: " + (khoanThuDto.getGhiChu() != null ? khoanThuDto.getGhiChu().length() : "null") + ")");
        
        // Kiểm tra mã khoản thu đã tồn tại
        if (khoanThuRepository.existsById(maKhoanThu)) {
            return new ResponseDto(false, "Mã khoản thu đã tồn tại. Vui lòng thử lại.");
        }
        
        // Đảm bảo mã khoản thu có đúng 15 ký tự (database constraint)
        if (maKhoanThu.length() > 15) {
            System.out.println("WARNING: Generated maKhoanThu '" + maKhoanThu + "' is " + maKhoanThu.length() + " characters, truncating to 15");
            maKhoanThu = maKhoanThu.substring(0, 15);
            System.out.println("DEBUG: Truncated maKhoanThu to '" + maKhoanThu + "' (length: " + maKhoanThu.length() + ")");
        }
        
        // Gán mã khoản thu mới vào DTO
        khoanThuDto.setMaKhoanThu(maKhoanThu);
        
        // Truncate fields to fit database varchar(15) constraints (donViTinh field now has varchar(15))
        if (khoanThuDto.getDonViTinh() != null && khoanThuDto.getDonViTinh().length() > 15) {
            String originalDonViTinh = khoanThuDto.getDonViTinh();
            String truncatedDonViTinh = khoanThuDto.getDonViTinh().substring(0, 15);
            khoanThuDto.setDonViTinh(truncatedDonViTinh);
            System.out.println("DEBUG: Truncated donViTinh from '" + originalDonViTinh + "' to '" + truncatedDonViTinh + "'");
        }
        
        // BƯỚC 1: Lưu khoản thu trước (không bao gồm phiGuiXeList)
        System.out.println("DEBUG: 📝 STEP 1: Saving KhoanThu to khoan_thu table...");
        
        // Tạm thời lưu phiGuiXeList
        var phiGuiXeListTemp = khoanThuDto.getPhiGuiXeList();
        khoanThuDto.setPhiGuiXeList(new ArrayList<>()); // Đặt empty để không lưu cùng lúc
        
        // Chuyển đổi DTO thành entity và lưu vào bảng khoan_thu
        KhoanThu khoanThu = khoanThuMapper.fromKhoanThuDto(khoanThuDto);
        System.out.println("DEBUG: About to save KhoanThu entity with maKhoanThu = '" + khoanThu.getMaKhoanThu() + "'");
        
        khoanThuRepository.save(khoanThu);
        System.out.println("DEBUG: ✅ KhoanThu saved successfully to khoan_thu table");
        
        // BƯỚC 2: Lưu phí xe vào bảng phi_gui_xe (nếu có)
        if (phiGuiXeListTemp != null && !phiGuiXeListTemp.isEmpty()) {
            System.out.println("DEBUG: 🚗 STEP 2: Saving " + phiGuiXeListTemp.size() + " vehicle fees to phi_gui_xe table...");
            
            // Tạo thông báo hiển thị các đơn giá xe
            StringBuilder vehiclePriceMessage = new StringBuilder();
            vehiclePriceMessage.append("Thêm khoản thu phương tiện thành công với các đơn giá:\n");
            
            for (PhiGuiXeDto phiGuiXeDto : phiGuiXeListTemp) {
                // Set mã khoản thu đã được tạo
                phiGuiXeDto.setMaKhoanThu(maKhoanThu);
                
                // Chuyển đổi DTO thành entity
                PhiGuiXe phiGuiXe = phiGuiXeMapper.fromPhiGuiXeDto(phiGuiXeDto);
                // Set reference đến KhoanThu entity
                phiGuiXe.setKhoanThu(khoanThu);
                
                // Lưu vào database
                phiGuiXeRepository.save(phiGuiXe);
                
                // Thêm vào thông báo
                vehiclePriceMessage.append("• ").append(phiGuiXeDto.getLoaiXe())
                                  .append(": ").append(String.format("%,d", phiGuiXeDto.getSoTien()))
                                  .append(" VND\n");
                
                System.out.println("DEBUG: ✅ Saved vehicle fee: " + phiGuiXeDto.getLoaiXe() + " = " + phiGuiXeDto.getSoTien() + " VND (maKhoanThu: " + phiGuiXeDto.getMaKhoanThu() + ")");
            }
            
            System.out.println("DEBUG: ✅ All vehicle fees saved successfully to phi_gui_xe table");
            
            // Trả về thông báo với chi tiết các đơn giá xe
            return new ResponseDto(true, vehiclePriceMessage.toString().trim());
        } else {
            System.out.println("DEBUG: ℹ️  No vehicle fees to save (non-vehicle fee type)");
            // Thông báo bình thường cho khoản thu không phải phương tiện
            return new ResponseDto(true, "Thêm khoản thu thành công");
        }
    }
    
    /**
     * Tạo mã khoản thu theo định dạng cũ: TN/BB-YYYYMM-XXX (13 ký tự)
     * Ví dụ: BB-202506-002 (Bắt buộc, tháng 6/2025, số thứ tự 002)
     * hoặc TN-202506-001 (Tự nguyện, tháng 6/2025, số thứ tự 001)
     * 
     * Lưu ý: Sẽ truncate về 15 ký tự để phù hợp với database constraint
     * 
     * @param khoanThuDto Thông tin khoản thu cần tạo mã
     * @return Mã khoản thu được tạo theo định dạng cũ (truncated to 15 chars if needed)
     */
    private String generateMaKhoanThu(KhoanThuDto khoanThuDto) {
        // Phần 1: TN hoặc BB (Tự nguyện / Bắt buộc) - 2 ký tự
        boolean batBuoc = khoanThuDto.isBatBuoc();
        String loaiKhoanThu = batBuoc ? "BB" : "TN";

        // Phần 2: Thời điểm tạo yyMM (năm + tháng)
        LocalDateTime now = LocalDateTime.now();
        String thoiDiemTao = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));

        // Ghép các phần lại với nhau sử dụng StringBuilder
        StringBuilder maBuilder = new StringBuilder();
        maBuilder.append(loaiKhoanThu)
                .append(thoiDiemTao);
        return maBuilder.toString();
    }

    @Override
    @Transactional
    public ResponseDto updateKhoanThu(KhoanThuDto khoanThuDto) {
        System.out.println("DEBUG: 🔄 Starting updateKhoanThu for maKhoanThu: " + khoanThuDto.getMaKhoanThu());
        
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền cập nhật khoản thu. Chỉ Kế toán mới được phép.");
        }

        // Kiểm tra xem khoản thu có tồn tại không
        KhoanThu existingKhoanThu = khoanThuRepository.findById(khoanThuDto.getMaKhoanThu()).orElse(null);
        if (existingKhoanThu == null) {
            return new ResponseDto(false, "Không tìm thấy khoản thu với mã: " + khoanThuDto.getMaKhoanThu());
        }
        
        // Kiểm tra xem đã tạo hóa đơn chưa
        if (existingKhoanThu.isTaoHoaDon()) {
            return new ResponseDto(false, "Khoản thu đã tạo hóa đơn, không thể cập nhật");
        }

        // BƯỚC 1: Cập nhật thông tin khoản thu trong bảng khoan_thu
        System.out.println("DEBUG: 📝 STEP 1: Updating KhoanThu in khoan_thu table...");
        
        // Tạm thời lưu danh sách phí xe mới
        var newPhiGuiXeList = khoanThuDto.getPhiGuiXeList();
        khoanThuDto.setPhiGuiXeList(new ArrayList<>()); // Đặt empty để không lưu cùng lúc
        
        // Cập nhật thông tin cơ bản của khoản thu
        KhoanThu updatedKhoanThu = khoanThuMapper.fromKhoanThuDto(khoanThuDto);
        updatedKhoanThu.setTaoHoaDon(existingKhoanThu.isTaoHoaDon()); // Giữ nguyên trạng thái tạo hóa đơn
        khoanThuRepository.save(updatedKhoanThu);
        System.out.println("DEBUG: ✅ KhoanThu updated successfully in khoan_thu table");

        // BƯỚC 2: Cập nhật phí xe trong bảng phi_gui_xe (nếu là khoản thu phương tiện)
        if ("Phương tiện".equals(khoanThuDto.getDonViTinh())) {
            System.out.println("DEBUG: 🚗 STEP 2: Updating vehicle fees in phi_gui_xe table...");
            
            // Xóa tất cả phí xe cũ của khoản thu này
            System.out.println("DEBUG: 🗑️  Deleting old vehicle fees for maKhoanThu: " + khoanThuDto.getMaKhoanThu());
            phiGuiXeRepository.deleteByKhoanThu_MaKhoanThu(khoanThuDto.getMaKhoanThu());
            System.out.println("DEBUG: ✅ Old vehicle fees deleted");
            
            // Thêm phí xe mới (nếu có)
            if (newPhiGuiXeList != null && !newPhiGuiXeList.isEmpty()) {
                System.out.println("DEBUG: 💰 Adding " + newPhiGuiXeList.size() + " new vehicle fees...");
                
                StringBuilder vehiclePriceMessage = new StringBuilder();
                vehiclePriceMessage.append("Cập nhật khoản thu phương tiện thành công với các đơn giá mới:\n");
                
                for (PhiGuiXeDto phiGuiXeDto : newPhiGuiXeList) {
                    // Set mã khoản thu
                    phiGuiXeDto.setMaKhoanThu(khoanThuDto.getMaKhoanThu());
                    
                    // Chuyển đổi DTO thành entity
                    PhiGuiXe newPhiGuiXe = phiGuiXeMapper.fromPhiGuiXeDto(phiGuiXeDto);
                    // Set reference đến KhoanThu entity
                    newPhiGuiXe.setKhoanThu(updatedKhoanThu);
                    
                    // Lưu vào database
                    phiGuiXeRepository.save(newPhiGuiXe);
                    
                    // Thêm vào thông báo
                    vehiclePriceMessage.append("• ").append(phiGuiXeDto.getLoaiXe())
                                      .append(": ").append(String.format("%,d", phiGuiXeDto.getSoTien()))
                                      .append(" VND\n");
                    
                    System.out.println("DEBUG: ✅ Saved updated vehicle fee: " + phiGuiXeDto.getLoaiXe() + " = " + phiGuiXeDto.getSoTien() + " VND");
                }
                
                System.out.println("DEBUG: ✅ All vehicle fees updated successfully in phi_gui_xe table");
                
                // Trả về thông báo với chi tiết các đơn giá xe mới
                return new ResponseDto(true, vehiclePriceMessage.toString().trim());
            } else {
                System.out.println("DEBUG: ⚠️  No new vehicle fees provided - all old fees deleted");
                return new ResponseDto(true, "Cập nhật khoản thu phương tiện thành công - đã xóa tất cả phí xe cũ");
            }
        } else {
            System.out.println("DEBUG: ℹ️  Non-vehicle fee type - no vehicle fee update needed");
            return new ResponseDto(true, "Cập nhật khoản thu thành công");
        }
    }

    @Override
    public ResponseDto deleteKhoanThu(String maKhoanThu) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền xóa khoản thu. Chỉ Kế toán mới được phép.");
        }
        KhoanThu khoanThu = khoanThuRepository.findById(maKhoanThu).orElse(null);
        if (khoanThu != null && khoanThu.isTaoHoaDon()) {
            return new ResponseDto(false, "Khoản thu đã tạo hóa đơn, không thể xóa");
        }
        khoanThuRepository.deleteById(maKhoanThu);
        return new ResponseDto(true, "Xóa khoản thu thành công");
    }

    @Override
    public ResponseDto importFromExcel(MultipartFile file) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền thêm khoản thu. Chỉ Kế toán mới được phép.");
        }
        try {
            File tempFile = File.createTempFile("khoanthu_temp", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            Function<Row, KhoanThuDto> rowMapper = row -> {
                    KhoanThuDto dto = new KhoanThuDto();
                    dto.setMaKhoanThu(row.getCell(0).getStringCellValue());
                    dto.setTenKhoanThu(row.getCell(1).getStringCellValue());
                    dto.setBatBuoc(row.getCell(2).getBooleanCellValue());
                    dto.setDonViTinh(row.getCell(3).getStringCellValue());
                    dto.setSoTien((int) row.getCell(4).getNumericCellValue());
                    dto.setPhamVi(row.getCell(5).getStringCellValue());
                    dto.setNgayTao(row.getCell(6).getDateCellValue().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                    dto.setThoiHan(row.getCell(7).getDateCellValue().toInstant()
                        .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                    dto.setGhiChu(row.getCell(8).getStringCellValue());
                    dto.setPhiGuiXeList(null);
                    return dto;
            };
            List<KhoanThuDto> khoanThuDtoList = XlxsFileUtil.importFromExcel(tempFile.getAbsolutePath(), rowMapper);
            List<KhoanThu> khoanThuList = khoanThuDtoList.stream()
                    .map(khoanThuMapper::fromKhoanThuDto)
                    .collect(Collectors.toList());
            khoanThuRepository.saveAll(khoanThuList);
            tempFile.delete();
            return new ResponseDto(true, "Thêm khoản thu thành công " + khoanThuList.size() + " khoản thu");
        } catch (Exception e) {
            return new ResponseDto(false, "Thêm khoản thu thất bại: " + e.getMessage());
        }
    }
    @Override
    public long countKhoanThuByBatBuoc(boolean batBuoc) {
        System.out.println("📊 Querying count of fees with batBuoc = " + batBuoc);
        return khoanThuRepository.countByBatBuoc(batBuoc);
    }

    @Override 
    public long sumAmountByBatBuoc(boolean batBuoc) {
        System.out.println("💰 Querying sum of amounts with batBuoc = " + batBuoc);
        Long sum = khoanThuRepository.sumSoTienByBatBuoc(batBuoc);
        return sum != null ? sum : 0L;
    }

    @Override
    public ResponseDto exportToExcel(String filePath) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền xuất khoản thu. Chỉ Kế toán mới được phép.");
        }
        List<KhoanThu> khoanThuList = khoanThuRepository.findAll();
        String[] headers = {"Mã khoản thu", "Tên khoản thu", "Bắt buộc", "Đơn vị tính", "Số tiền", "Phạm vi", "Ngày tạo", "Thời hạn", "Ghi chú"};
        try {
            XlsxExportUtil.exportToExcel(filePath, headers, khoanThuList, (row, khoanThu) -> {
                row.createCell(0).setCellValue(khoanThu.getMaKhoanThu() != null ? khoanThu.getMaKhoanThu() : "");
                row.createCell(1).setCellValue(khoanThu.getTenKhoanThu() != null ? khoanThu.getTenKhoanThu() : "");
                row.createCell(2).setCellValue(khoanThu.isBatBuoc());
                row.createCell(3).setCellValue(khoanThu.getDonViTinh() != null ? khoanThu.getDonViTinh() : "");
                row.createCell(4).setCellValue(khoanThu.getSoTien());
                row.createCell(5).setCellValue(khoanThu.getPhamVi() != null ? khoanThu.getPhamVi() : "");
                if (khoanThu.getNgayTao() != null) {
                    row.createCell(6).setCellValue(java.sql.Date.valueOf(khoanThu.getNgayTao()));
                } else {
                    row.createCell(6).setCellValue("");
                }
                if (khoanThu.getThoiHan() != null) {
                    row.createCell(7).setCellValue(java.sql.Date.valueOf(khoanThu.getThoiHan()));
                } else {
                    row.createCell(7).setCellValue("");
                }
                row.createCell(8).setCellValue(khoanThu.getGhiChu() != null ? khoanThu.getGhiChu() : "");
            });
            return new ResponseDto(true, "Xuất file thành công");
        } catch (Exception e) {
            return new ResponseDto(false, "Xuất khoản thu thất bại: " + e.getMessage());
        }
    }
}
