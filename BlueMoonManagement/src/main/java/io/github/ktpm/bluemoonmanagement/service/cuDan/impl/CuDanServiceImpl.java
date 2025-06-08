package io.github.ktpm.bluemoonmanagement.service.cuDan.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CuDan;
import io.github.ktpm.bluemoonmanagement.model.mapper.CuDanMapper;
import io.github.ktpm.bluemoonmanagement.repository.CanHoRepository;
import io.github.ktpm.bluemoonmanagement.repository.CuDanRepository;
import io.github.ktpm.bluemoonmanagement.service.cuDan.CuDanService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.XlsxExportUtil;
import io.github.ktpm.bluemoonmanagement.util.XlxsFileUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CuDanServiceImpl implements CuDanService {

    private final CuDanRepository cuDanRepository;
    private final CanHoRepository canHoRepository;
    private final CuDanMapper cuDanMapper;
    
    @PersistenceContext
    private EntityManager entityManager;

    public CuDanServiceImpl(CuDanRepository cuDanRepository, CuDanMapper cuDanMapper, CanHoRepository canHoRepository) {
        this.cuDanRepository = cuDanRepository;
        this.cuDanMapper = cuDanMapper;
        this.canHoRepository = canHoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CudanDto> getAllCuDan() {
        // Sử dụng custom method để fetch join với căn hộ
        List<CuDan> cuDanList = cuDanRepository.findAllWithCanHo();
        
        return cuDanList.stream()
                // Tạm thời bỏ filter để kiểm tra hiển thị
                // .filter(cuDan -> cuDan.getNgayChuyenDi() == null) // Filter out soft-deleted residents
                .map(cuDanMapper::toCudanDto) // Hiển thị mã căn hộ bình thường, không ẩn
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseDto addCuDan(CudanDto cudanDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền thêm cư dân. Chỉ Tổ phó mới được phép.");
        }
        if (cuDanRepository.existsById(cudanDto.getMaDinhDanh())) {
            return new ResponseDto(false, "Cư dân đã tồn tại");
        }
        if (cudanDto.getMaCanHo() != null && !cudanDto.getMaCanHo().trim().isEmpty() && !canHoRepository.existsById(cudanDto.getMaCanHo())) {
            return new ResponseDto(false, "Không tìm thấy căn hộ");
        }
        if (cudanDto.getNgayChuyenDen() == null) {
            cudanDto.setNgayChuyenDen(LocalDate.now());
        }
        
        // Map DTO to Entity
        CuDan cuDan = cuDanMapper.fromCudanDto(cudanDto);
        
        // Properly set CanHo reference if maCanHo is provided
        if (cudanDto.getMaCanHo() != null && !cudanDto.getMaCanHo().trim().isEmpty()) {
            // Load existing CanHo entity from database
            io.github.ktpm.bluemoonmanagement.model.entity.CanHo canHo = 
                canHoRepository.findById(cudanDto.getMaCanHo()).orElse(null);
            cuDan.setCanHo(canHo);
            System.out.println("=== DEBUG: Adding resident to apartment ===");
            System.out.println("Resident: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ")");
            System.out.println("Apartment: " + cudanDto.getMaCanHo());
            System.out.println("Apartment entity found: " + (canHo != null));
        } else {
            // Set null if no apartment code provided
            cuDan.setCanHo(null);
            System.out.println("=== DEBUG: Adding resident without apartment ===");
            System.out.println("Resident: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ")");
        }
        
        cuDanRepository.save(cuDan);
        entityManager.flush(); // Force immediate flush to database
        System.out.println("=== DEBUG: Resident saved and flushed successfully ===");
        return new ResponseDto(true, "Thêm cư dân thành công");
    }

    @Override
    @Transactional
    public ResponseDto updateCuDan(CudanDto cudanDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền cập nhật cư dân. Chỉ Tổ phó mới được phép.");
        }
        if (!cuDanRepository.existsById(cudanDto.getMaDinhDanh())) {
            return new ResponseDto(false, "Không tìm thấy cư dân");
        }
        if (cudanDto.getMaCanHo() != null && !cudanDto.getMaCanHo().trim().isEmpty() && !canHoRepository.existsById(cudanDto.getMaCanHo())) {
            return new ResponseDto(false, "Không tìm thấy căn hộ");
        }
        
        // Lấy thông tin cư dân hiện tại từ database
        CuDan existingCuDan = cuDanRepository.findById(cudanDto.getMaDinhDanh()).orElse(null);
        
        // Map DTO to Entity
        CuDan cuDan = cuDanMapper.fromCudanDto(cudanDto);
        
        // Xử lý logic khi chọn "Đã chuyển đi" từ ComboBox
        if ("Đã chuyển đi".equals(cudanDto.getTrangThaiCuTru()) && 
            (existingCuDan == null || !"Đã chuyển đi".equals(existingCuDan.getTrangThaiCuTru()))) {
            // Chuyển sang trạng thái "Đã chuyển đi" từ trạng thái khác
            cuDan.setNgayChuyenDi(LocalDate.now()); // Set ngày chuyển đi = hôm nay
            cuDan.setNgayChuyenDen(null); // Clear ngày chuyển đến
            System.out.println("=== DEBUG: Chuyển cư dân sang trạng thái 'Đã chuyển đi' từ ComboBox ===");
        }
        
        // Kiểm tra nếu cư dân chuyển từ "Đã chuyển đi" và có mã căn hộ mới
        boolean hasNewApartment = false;
        if (existingCuDan != null && 
            "Đã chuyển đi".equals(existingCuDan.getTrangThaiCuTru()) 
            && cudanDto.getMaCanHo() != null && !cudanDto.getMaCanHo().trim().isEmpty()) {
            
            System.out.println("=== DEBUG: Cư dân đang ở trạng thái 'Đã chuyển đi' và được cập nhật mã căn hộ mới ===");
            System.out.println("Cư dân: " + cudanDto.getHoVaTen() + " (" + cudanDto.getMaDinhDanh() + ")");
            System.out.println("Trạng thái hiện tại: " + existingCuDan.getTrangThaiCuTru());
            System.out.println("Trạng thái mới từ form: " + cudanDto.getTrangThaiCuTru());
            System.out.println("Mã căn hộ mới: " + cudanDto.getMaCanHo());
            
            // Xử lý logic thay đổi trạng thái từ "Đã chuyển đi"
            if ("Đã chuyển đi".equals(cudanDto.getTrangThaiCuTru())) {
                // Nếu vẫn giữ trạng thái "Đã chuyển đi" - không thay đổi gì
                System.out.println("=== DEBUG: Giữ nguyên trạng thái 'Đã chuyển đi' ===");
            } else {
                // Chuyển từ "Đã chuyển đi" sang trạng thái khác
                cuDan.setNgayChuyenDi(null); // Clear ngày chuyển đi
                if ("Cư trú".equals(cudanDto.getTrangThaiCuTru())) {
                    cuDan.setNgayChuyenDen(LocalDate.now()); // Set ngày chuyển đến mới
                    System.out.println("=== DEBUG: Chuyển từ 'Đã chuyển đi' sang 'Cư trú' ===");
                } else {
                    cuDan.setNgayChuyenDen(null); // Clear ngày chuyển đến cho "Không cư trú"
                    System.out.println("=== DEBUG: Chuyển từ 'Đã chuyển đi' sang 'Không cư trú' ===");
                }
            }
            hasNewApartment = true;
        }
        
        // Properly set CanHo reference if maCanHo is provided
        if (cudanDto.getMaCanHo() != null && !cudanDto.getMaCanHo().trim().isEmpty()) {
            // Load existing CanHo entity from database
            io.github.ktpm.bluemoonmanagement.model.entity.CanHo canHo = 
                canHoRepository.findById(cudanDto.getMaCanHo()).orElse(null);
            cuDan.setCanHo(canHo);
            
            System.out.println("=== DEBUG: Cập nhật cư dân vào căn hộ ===");
            System.out.println("Cư dân: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ")");
            System.out.println("Căn hộ: " + cudanDto.getMaCanHo());
            System.out.println("Trạng thái mới: " + cuDan.getTrangThaiCuTru());
        } else {
            // Set null if no apartment code provided
            cuDan.setCanHo(null);
        }
        
        cuDanRepository.save(cuDan);
        entityManager.flush(); // Đảm bảo changes được commit ngay
        
        String successMessage = "Cập nhật cư dân thành công";
        if (hasNewApartment) {
            successMessage += ". Cư dân đã được gán vào căn hộ mới";
        }
        
        return new ResponseDto(true, successMessage);
    }

    @Override
    public ResponseDto deleteCuDan(CudanDto cudanDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền xóa cư dân. Chỉ Tổ phó mới được phép.");
        }
        if (!cuDanRepository.existsById(cudanDto.getMaDinhDanh())) {
            return new ResponseDto(false, "Không tìm thấy cư dân");
        }
        
        CuDan cuDan = cuDanRepository.findById(cudanDto.getMaDinhDanh()).orElse(null);
        cuDan.setNgayChuyenDi(LocalDate.now());
        cuDan.setTrangThaiCuTru("Đã chuyển đi");
        cuDan.setNgayChuyenDen(null); // Xóa ngày chuyển đến
        
        // GIỮ NGUYÊN mã căn hộ trong database để lưu lịch sử
        // Chỉ ẩn khi hiển thị thông qua mapper
        
        cuDanRepository.save(cuDan);
        entityManager.flush(); // Đảm bảo thay đổi được lưu ngay
        return new ResponseDto(true, "Xóa cư dân thành công");
    }

    @Override
    public ResponseDto importFromExcel(MultipartFile file) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền thêm cư dân. Chỉ Tổ phó mới được phép.");
        }
        try {
            File tempFile = File.createTempFile("cudan_temp", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            Function<Row, CudanDto> rowMapper = row -> {
                try {
                    CudanDto cudanDto = new CudanDto();
                    cudanDto.setMaDinhDanh(row.getCell(0).getStringCellValue());
                    cudanDto.setHoVaTen(row.getCell(1).getStringCellValue());
                    cudanDto.setGioiTinh(row.getCell(2).getStringCellValue());
                    cudanDto.setNgaySinh(row.getCell(3).getDateCellValue()
                        .toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                    cudanDto.setSoDienThoai(row.getCell(4).getStringCellValue());
                    cudanDto.setEmail(row.getCell(5).getStringCellValue());
                    cudanDto.setTrangThaiCuTru(row.getCell(6).getStringCellValue());
                    cudanDto.setNgayChuyenDen(row.getCell(7).getDateCellValue()
                        .toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                    cudanDto.setNgayChuyenDi(row.getCell(8).getDateCellValue()
                        .toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                    cudanDto.setMaCanHo(row.getCell(9).getStringCellValue());
                    return cudanDto;
                } catch (Exception e) {
                    return null;
                }
            };
            List<CudanDto> cudanDtoList = XlxsFileUtil.importFromExcel(tempFile.getAbsolutePath(), rowMapper);
            List<CuDan> cuDanList = cudanDtoList.stream()
                    .map(dto -> {
                        CuDan cuDan = cuDanMapper.fromCudanDto(dto);
                        // Properly set CanHo reference if maCanHo is provided
                        if (dto.getMaCanHo() != null && !dto.getMaCanHo().trim().isEmpty()) {
                            io.github.ktpm.bluemoonmanagement.model.entity.CanHo canHo = 
                                canHoRepository.findById(dto.getMaCanHo()).orElse(null);
                            cuDan.setCanHo(canHo);
                        } else {
                            cuDan.setCanHo(null);
                        }
                        return cuDan;
                    })
                    .collect(Collectors.toList());
            cuDanRepository.saveAll(cuDanList);
            tempFile.delete();
            return new ResponseDto(true, "Thêm cư dân thành công " + cuDanList.size() + " cư dân");
        } catch (Exception e) {
            return new ResponseDto(false, "Thêm cư dân thất bại: " + e.getMessage());
        }
    }
    @Override
    public ResponseDto exportToExcel(String filePath) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền xuất dữ liệu cư dân. Chỉ Kế toán mới được phép.");
        }
        List<CudanDto> cudanDtoList = getAllCuDan();
        String[] headers = {"Mã định danh", "Họ và tên", "Giới tính", "Ngày sinh", "Số điện thoại", "Email", "Trạng thái cư trú", "Ngày chuyển đến", "Ngày chuyển đi", "Mã căn hộ"};
        try {
            XlsxExportUtil.exportToExcel(filePath, headers, cudanDtoList, (row, cudanDto) -> {
                row.createCell(0).setCellValue(cudanDto.getMaDinhDanh());
                row.createCell(1).setCellValue(cudanDto.getHoVaTen());
                row.createCell(2).setCellValue(cudanDto.getGioiTinh());
                row.createCell(3).setCellValue(java.sql.Date.valueOf(cudanDto.getNgaySinh()));
                row.createCell(4).setCellValue(cudanDto.getSoDienThoai());
                row.createCell(5).setCellValue(cudanDto.getEmail());
                row.createCell(6).setCellValue(cudanDto.getTrangThaiCuTru());
                row.createCell(7).setCellValue(java.sql.Date.valueOf(cudanDto.getNgayChuyenDen()));
                row.createCell(8).setCellValue(java.sql.Date.valueOf(cudanDto.getNgayChuyenDi()));
                row.createCell(9).setCellValue(cudanDto.getMaCanHo());
            });
            return new ResponseDto(true, "Xuất cư dân thành công");
        } catch (Exception e) {
            return new ResponseDto(false, "Xuất cư dân thất bại: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean xoaMem(String maDinhDanh) {
        try {
            System.out.println("=== DEBUG: Xử lý xóa mềm cư dân ===");
            System.out.println("Mã định danh: " + maDinhDanh);
            
            // Kiểm tra user hiện tại
            if (Session.getCurrentUser() == null) {
                System.err.println("DEBUG: Người dùng chưa đăng nhập");
                return false;
            }
            
            System.out.println("DEBUG: Người dùng hiện tại: " + Session.getCurrentUser().getHoTen() + " (" + Session.getCurrentUser().getEmail() + ")");
            System.out.println("DEBUG: Vai trò: " + Session.getCurrentUser().getVaiTro());
            
            if (!"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
                System.err.println("DEBUG: Không có quyền xóa - cần vai trò 'Tổ phó'");
                return false;
            }
            
            // Kiểm tra cư dân tồn tại
            CuDan cuDan = cuDanRepository.findById(maDinhDanh).orElse(null);
            if (cuDan == null) {
                System.err.println("DEBUG: Không tìm thấy cư dân với mã: " + maDinhDanh);
                return false;
            }
            
            System.out.println("DEBUG: Tìm thấy cư dân: " + cuDan.getHoVaTen());
            System.out.println("DEBUG: Trạng thái hiện tại: " + cuDan.getTrangThaiCuTru());
            System.out.println("DEBUG: Căn hộ hiện tại: " + (cuDan.getCanHo() != null ? cuDan.getCanHo().getMaCanHo() : "NULL"));
            
            // Soft delete: cập nhật ngày chuyển đi, trạng thái và XÓA mối quan hệ với căn hộ
            cuDan.setNgayChuyenDi(LocalDate.now());
            cuDan.setTrangThaiCuTru("Đã chuyển đi");
            cuDan.setNgayChuyenDen(null); // Xóa ngày chuyển đến
            
            // GIỮ NGUYÊN mã căn hộ trong database để lưu lịch sử
            // Chỉ ẩn khi hiển thị thông qua mapper
            
            cuDanRepository.save(cuDan);
            entityManager.flush();
            
            // Verify lại sau khi save
            CuDan verifyAfterSave = cuDanRepository.findById(maDinhDanh).orElse(null);
            if (verifyAfterSave != null) {
                System.out.println("DEBUG: Sau khi save - Trạng thái: " + verifyAfterSave.getTrangThaiCuTru());
                System.out.println("DEBUG: Sau khi save - Ngày chuyển đi: " + verifyAfterSave.getNgayChuyenDi());
                System.out.println("DEBUG: Sau khi save - Căn hộ: " + (verifyAfterSave.getCanHo() != null ? verifyAfterSave.getCanHo().getMaCanHo() : "NULL"));
                
                // Test mapping để đảm bảo mapper hoạt động đúng
                io.github.ktpm.bluemoonmanagement.model.dto.cuDan.CudanDto testDto = cuDanMapper.toCudanDto(verifyAfterSave);
                System.out.println("DEBUG: Mapping test - DTO mã căn hộ: '" + testDto.getMaCanHo() + "'");
            }
            
            System.out.println("DEBUG: Xóa mềm thành công - Mã căn hộ hiển thị null!");
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa mềm cư dân: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}