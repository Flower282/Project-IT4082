package io.github.ktpm.bluemoonmanagement.service.canHo.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;
import io.github.ktpm.bluemoonmanagement.model.entity.CuDan;
import io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien;
import io.github.ktpm.bluemoonmanagement.model.mapper.CanHoMapper;
import io.github.ktpm.bluemoonmanagement.model.mapper.PhuongTienMapper;
import io.github.ktpm.bluemoonmanagement.repository.CanHoRepository;
import io.github.ktpm.bluemoonmanagement.repository.CuDanRepository;
import io.github.ktpm.bluemoonmanagement.repository.PhuongTienRepository;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.XlsxExportUtil;
import io.github.ktpm.bluemoonmanagement.util.XlxsFileUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class CanHoServiceImpl implements CanHoService {

    private final CanHoRepository canHoRepository;
    private final CuDanRepository cuDanRepository;
    private final CanHoMapper canHoMapper;
    private final PhuongTienRepository phuongTienRepository;
    private final PhuongTienMapper phuongTienMapper;
    
    @PersistenceContext
    private EntityManager entityManager;

    public CanHoServiceImpl(CanHoRepository canHoRepository, CuDanRepository cuDanRepository, CanHoMapper canHoMapper, PhuongTienRepository phuongTienRepository, PhuongTienMapper phuongTienMapper) {
        this.canHoRepository = canHoRepository;
        this.cuDanRepository = cuDanRepository;
        this.canHoMapper = canHoMapper;
        this.phuongTienRepository = phuongTienRepository;
        this.phuongTienMapper = phuongTienMapper;
    }

    @Override
    public List<CanHoDto> getAllCanHo() {
        List<CanHo> canHoList = canHoRepository.findAll();
        return canHoList.stream()
                .map(canHoMapper::toCanHoDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public CanHoChiTietDto getCanHoChiTiet(CanHoDto canHoDto) {
        // Clear entity manager cache to ensure fresh data
        entityManager.clear();
        
        // Debug: Check residents directly from database
        String jpql = "SELECT c FROM CuDan c WHERE c.canHo.maCanHo = :maCanHo";
        List<CuDan> residentsFromDb = entityManager.createQuery(jpql, CuDan.class)
            .setParameter("maCanHo", canHoDto.getMaCanHo())
            .getResultList();
        
        System.out.println("=== DEBUG: Direct DB query for residents ===");
        System.out.println("Apartment: " + canHoDto.getMaCanHo());
        System.out.println("Residents found in DB: " + residentsFromDb.size());
        for (CuDan resident : residentsFromDb) {
            System.out.println("- DB Resident: " + resident.getHoVaTen() + " (" + resident.getMaDinhDanh() + ") - Status: " + resident.getTrangThaiCuTru());
        }
        System.out.println("=== END DEBUG DB ===");
        
        CanHo canHo = canHoRepository.findById(canHoDto.getMaCanHo()).orElse(null);
        if (canHo != null) {
            // Force initialization of lazy collections
            canHo.getCuDanList().size(); // This will trigger lazy loading within transaction
            
            // THAY ĐỔI: Thay vì dùng canHo.getPhuongTienList(), lấy trực tiếp từ repository chỉ active vehicles
            List<PhuongTien> activePhuongTiens = phuongTienRepository.findActiveByCanHo_MaCanHo(canHoDto.getMaCanHo());
            
            // Debug logging
            System.out.println("=== DEBUG: Loading apartment details ===");
            System.out.println("Apartment code: " + canHo.getMaCanHo());
            System.out.println("Number of residents found: " + canHo.getCuDanList().size());
            for (CuDan cuDan : canHo.getCuDanList()) {
                System.out.println("- Resident: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ") - Status: " + cuDan.getTrangThaiCuTru());
            }
            System.out.println("Number of active vehicles: " + activePhuongTiens.size());
            System.out.println("=== END DEBUG ===");
            
            // Sử dụng mapper nhưng override danh sách phương tiện với active vehicles
            CanHoChiTietDto dto = canHoMapper.toCanHoChiTietDto(canHo);
            // Convert entities to DTOs và set lại list phương tiện chỉ với active vehicles
            List<io.github.ktpm.bluemoonmanagement.model.dto.phuongTien.PhuongTienDto> activePhuongTienDtos = 
                activePhuongTiens.stream()
                    .map(phuongTienMapper::toPhuongTienDto)
                    .collect(java.util.stream.Collectors.toList());
            dto.setPhuongTienList(activePhuongTienDtos);
            return dto;
        }
        return canHoMapper.toCanHoChiTietDto(canHo);
    }

    @Override
    public ResponseDto addCanHo(CanHoDto canHoDto) {
        // Kiểm tra quyền: chỉ 'Tổ phó' mới được thêm căn hộ
        if (Session.getCurrentUser() == null || (!"Tổ phó".equals(Session.getCurrentUser().getVaiTro()))) {
            return new ResponseDto(false, "Bạn không có quyền thêm căn hộ. Chỉ Tổ trưởng và Admin mới được phép.");
        }
        
        // Check if an apartment with this code already exists
        if (canHoRepository.existsById(canHoDto.getMaCanHo())) {
            return new ResponseDto(false, "Căn hộ đã tồn tại");
        }
        
        // Nếu có chủ hộ, xử lý theo logic phù hợp
        if (canHoDto.getChuHo() != null) {
            String maDinhDanh = canHoDto.getChuHo().getMaDinhDanh();
            
            // Kiểm tra xem cư dân có tồn tại không
            if (!cuDanRepository.existsById(maDinhDanh)) {
                // Nếu cư dân chưa tồn tại và có thông tin đầy đủ thì tạo mới
                if (canHoDto.getChuHo().getHoVaTen() != null && 
                    !canHoDto.getChuHo().getHoVaTen().trim().isEmpty()) {
                    
                    // Tạo cư dân mới
                    CuDan cuDanMoi = new CuDan();
                    cuDanMoi.setMaDinhDanh(maDinhDanh);
                    cuDanMoi.setHoVaTen(canHoDto.getChuHo().getHoVaTen());
                    cuDanMoi.setSoDienThoai(canHoDto.getChuHo().getSoDienThoai());
                    cuDanMoi.setEmail(canHoDto.getChuHo().getEmail());
                    cuDanMoi.setTrangThaiCuTru(canHoDto.getChuHo().getTrangThaiCuTru());
                    
                    // Set ngày chuyển đến nếu là cư trú
                    if ("Cư trú".equals(canHoDto.getChuHo().getTrangThaiCuTru())) {
                        cuDanMoi.setNgayChuyenDen(canHoDto.getChuHo().getNgayChuyenDen() != null ? 
                            canHoDto.getChuHo().getNgayChuyenDen() : LocalDate.now());
                    }
                    
                    // Lưu cư dân trước
                    cuDanRepository.save(cuDanMoi);
                    System.out.println("Đã tạo cư dân mới với mã định danh: " + maDinhDanh);
                } else {
                    // Chỉ có mã định danh, cư dân chưa tồn tại
                    return new ResponseDto(false, "Cư dân với mã định danh '" + maDinhDanh + "' không tồn tại trong hệ thống. Vui lòng tạo cư dân trước hoặc kiểm tra lại mã định danh.");
                }
            } else {
                System.out.println("Đã tìm thấy cư dân với mã định danh: " + maDinhDanh);
            }
        }
        
        // Convert DTO to entity using the mapper
        CanHo canHo = canHoMapper.fromCanHoDto(canHoDto);
        
        // Lưu căn hộ
        canHoRepository.save(canHo);
        
        return new ResponseDto(true, "Căn hộ đã được thêm thành công" + 
            (canHoDto.getChuHo() != null ? " với chủ hộ có mã: " + canHoDto.getChuHo().getMaDinhDanh() : ""));
    }

    @Override
    public ResponseDto updateCanHo(CanHoDto canHoDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền cập nhật căn hộ. Chỉ Tổ phó mới được phép.");
        }
        if (canHoRepository.existsById(canHoDto.getMaCanHo())) {
            return new ResponseDto(false, "Căn hộ đã tồn tại");
        }
        CanHo canHo = canHoMapper.fromCanHoDto(canHoDto);
        canHoRepository.save(canHo);
        return new ResponseDto(true, "Căn hộ đã được cập nhật thành công");
    }

    @Override
    public ResponseDto deleteCanHo(CanHoDto canHoDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền xóa căn hộ. Chỉ Tổ phó mới được phép.");
        }
        canHoRepository.deleteById(canHoDto.getMaCanHo());
        return new ResponseDto(true, "Căn hộ đã được xóa thành công");
    }

    @Override
    public ResponseDto importFromExcel(MultipartFile file) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền thêm hóa đơn tự nguyện. Chỉ Kế toán mới được phép.");
        }
        try {
            File tempFile = File.createTempFile("canho_temp", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            Function<Row, CanHoDto> rowMapper = row -> {
                CanHoDto canHoDto = new CanHoDto();
                canHoDto.setMaCanHo(row.getCell(0).getStringCellValue());
                canHoDto.setToaNha(row.getCell(1).getStringCellValue());
                canHoDto.setTang(row.getCell(2).getStringCellValue());
                canHoDto.setSoNha(row.getCell(3).getStringCellValue());
                canHoDto.setDienTich(row.getCell(4).getNumericCellValue());
                canHoDto.setChuHo(null);
                canHoDto.setDaBanChua(row.getCell(6).getBooleanCellValue());
                canHoDto.setTrangThaiKiThuat(row.getCell(7).getStringCellValue());
                canHoDto.setTrangThaiSuDung(row.getCell(8).getStringCellValue());
                return canHoDto;
            };
            List<CanHoDto> canHoDtoList = XlxsFileUtil.importFromExcel(tempFile.getAbsolutePath(), rowMapper);
            List<CanHo> canHoList = canHoDtoList.stream()
                    .map(canHoMapper::fromCanHoDto)
                    .collect(Collectors.toList());
            canHoRepository.saveAll(canHoList);
            tempFile.delete();
            return new ResponseDto(true, "Thêm căn hộ thành công" + canHoDtoList.size() + " căn hộ");
        } catch (Exception e) {
            return new ResponseDto(false, "Thêm căn hộ thất bại: " + e.getMessage());
        }
    }
    @Override
    public ResponseDto exportToExcel(String filePath) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền xuất căn hộ. Chỉ Kế toán mới được phép.");
        }
        List<CanHoDto> canHoDtoList = getAllCanHo();
        String[] headers = {"Mã căn hộ", "Tòa nhà", "Tầng", "Số nhà", "Diện tích", "Đã bán/chưa", "Trạng thái kỹ thuật", "Trạng thái sử dụng"};
        try {
            XlsxExportUtil.exportToExcel(filePath, headers, canHoDtoList, (row, canHoDto) -> {
                row.createCell(0).setCellValue(canHoDto.getMaCanHo());
                row.createCell(1).setCellValue(canHoDto.getToaNha());
                row.createCell(2).setCellValue(canHoDto.getTang());
                row.createCell(3).setCellValue(canHoDto.getSoNha());
                row.createCell(4).setCellValue(canHoDto.getDienTich());
                row.createCell(5).setCellValue(canHoDto.isDaBanChua());
                row.createCell(6).setCellValue(canHoDto.getTrangThaiKiThuat());
                row.createCell(7).setCellValue(canHoDto.getTrangThaiSuDung());
            });
            return new ResponseDto(true, "Xuất căn hộ thành công");
        } catch (Exception e) {
            return new ResponseDto(false, "Xuất căn hộ thất bại: " + e.getMessage());
        }
    }
}
