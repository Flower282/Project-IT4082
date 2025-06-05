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
    public List<CudanDto> getAllCuDan() {
        return cuDanRepository.findAll()
                .stream()
                // Tạm thời bỏ filter để kiểm tra hiển thị
                // .filter(cuDan -> cuDan.getNgayChuyenDi() == null) // Filter out soft-deleted residents
                .map(cuDanMapper::toCudanDto)
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
        
        // Map DTO to Entity
        CuDan cuDan = cuDanMapper.fromCudanDto(cudanDto);
        
        // Properly set CanHo reference if maCanHo is provided
        if (cudanDto.getMaCanHo() != null && !cudanDto.getMaCanHo().trim().isEmpty()) {
            // Load existing CanHo entity from database
            io.github.ktpm.bluemoonmanagement.model.entity.CanHo canHo = 
                canHoRepository.findById(cudanDto.getMaCanHo()).orElse(null);
            cuDan.setCanHo(canHo);
        } else {
            // Set null if no apartment code provided
            cuDan.setCanHo(null);
        }
        
        cuDanRepository.save(cuDan);
        return new ResponseDto(true, "Cập nhật cư dân thành công");
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
        cuDanRepository.save(cuDan);
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
            if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
                return false;
            }
            
            CuDan cuDan = cuDanRepository.findById(maDinhDanh).orElse(null);
            if (cuDan == null) {
                return false;
            }
            
            // Soft delete: set ngayChuyenDi và cập nhật trạng thái
            cuDan.setNgayChuyenDi(LocalDate.now());
            cuDan.setTrangThaiCuTru("Đã chuyển đi");
            cuDanRepository.save(cuDan);
            entityManager.flush();
            
            return true;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa mềm cư dân: " + e.getMessage());
            return false;
        }
    }
}