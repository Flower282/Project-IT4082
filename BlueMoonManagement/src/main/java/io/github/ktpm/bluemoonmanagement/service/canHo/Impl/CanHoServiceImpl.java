package io.github.ktpm.bluemoonmanagement.service.canHo.Impl;

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
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;
import io.github.ktpm.bluemoonmanagement.model.entity.CuDan;
import io.github.ktpm.bluemoonmanagement.model.entity.HoaDon;
import io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien;
import io.github.ktpm.bluemoonmanagement.model.mapper.CanHoMapper;
import io.github.ktpm.bluemoonmanagement.model.mapper.PhuongTienMapper;
import io.github.ktpm.bluemoonmanagement.repository.CanHoRepository;
import io.github.ktpm.bluemoonmanagement.repository.CuDanRepository;
import io.github.ktpm.bluemoonmanagement.repository.HoaDonRepository;
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
    private final HoaDonRepository hoaDonRepository;
    
    @PersistenceContext
    private EntityManager entityManager;

    public CanHoServiceImpl(CanHoRepository canHoRepository, CuDanRepository cuDanRepository, CanHoMapper canHoMapper, PhuongTienRepository phuongTienRepository, PhuongTienMapper phuongTienMapper, HoaDonRepository hoaDonRepository) {
        this.canHoRepository = canHoRepository;
        this.cuDanRepository = cuDanRepository;
        this.canHoMapper = canHoMapper;
        this.phuongTienRepository = phuongTienRepository;
        this.phuongTienMapper = phuongTienMapper;
        this.hoaDonRepository = hoaDonRepository;
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
    @Transactional
    public ResponseDto updateCanHo(CanHoDto canHoDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền cập nhật căn hộ. Chỉ Tổ phó mới được phép.");
        }
        
        // Tìm căn hộ hiện có trong database
        CanHo existingCanHo = canHoRepository.findById(canHoDto.getMaCanHo()).orElse(null);
        if (existingCanHo == null) {
            return new ResponseDto(false, "Không tìm thấy căn hộ để cập nhật");
        }
        
        // Cập nhật thông tin căn hộ hiện có (không tạo entity mới)
        existingCanHo.setToaNha(canHoDto.getToaNha());
        existingCanHo.setTang(canHoDto.getTang());
        existingCanHo.setSoNha(canHoDto.getSoNha());
        existingCanHo.setDienTich(canHoDto.getDienTich());
        existingCanHo.setDaBanChua(canHoDto.isDaBanChua());
        existingCanHo.setTrangThaiKiThuat(canHoDto.getTrangThaiKiThuat());
        existingCanHo.setTrangThaiSuDung(canHoDto.getTrangThaiSuDung());
        
        // Xử lý cập nhật chủ hộ nếu có thay đổi
        if (canHoDto.getChuHo() != null && canHoDto.getChuHo().getMaDinhDanh() != null) {
            CuDan chuHo = cuDanRepository.findById(canHoDto.getChuHo().getMaDinhDanh()).orElse(null);
            if (chuHo != null) {
                existingCanHo.setChuHo(chuHo);
            }
        } else {
            existingCanHo.setChuHo(null);
        }
        
        // Lưu changes
        canHoRepository.save(existingCanHo);
        entityManager.flush(); // Đảm bảo changes được commit ngay
        
        System.out.println("DEBUG: Updated apartment " + canHoDto.getMaCanHo() + " successfully");
        
        return new ResponseDto(true, "Căn hộ đã được cập nhật thành công");
    }

    @Override
    @Transactional
    public ResponseDto deleteCanHo(CanHoDto canHoDto) {
        if (Session.getCurrentUser() == null || !"Tổ phó".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền xóa căn hộ. Chỉ Tổ phó mới được phép.");
        }
        
        try {
            String maCanHo = canHoDto.getMaCanHo();
            System.out.println("=== DEBUG: Bắt đầu xóa căn hộ và dữ liệu liên quan ===");
            System.out.println("Mã căn hộ: " + maCanHo);
            
            // 1. Xóa tất cả hóa đơn của căn hộ
            List<HoaDon> hoaDonList = hoaDonRepository.findAll().stream()
                .filter(hoaDon -> hoaDon.getCanHo() != null && maCanHo.equals(hoaDon.getCanHo().getMaCanHo()))
                .collect(java.util.stream.Collectors.toList());
            
            if (!hoaDonList.isEmpty()) {
                System.out.println("DEBUG: Tìm thấy " + hoaDonList.size() + " hóa đơn cần xóa");
                for (HoaDon hoaDon : hoaDonList) {
                    System.out.println("DEBUG: Xóa hóa đơn ID: " + hoaDon.getMaHoaDon());
                    hoaDonRepository.delete(hoaDon);
                }
                // Flush changes để đảm bảo hóa đơn được xóa trước
                entityManager.flush();
                System.out.println("DEBUG: Đã xóa tất cả hóa đơn");
            } else {
                System.out.println("DEBUG: Không có hóa đơn nào cần xóa");
            }

            // 2. Xóa HOÀN TOÀN tất cả phương tiện của căn hộ
            List<io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien> allPhuongTienList = 
                phuongTienRepository.findAll().stream()
                    .filter(pt -> pt.getCanHo() != null && maCanHo.equals(pt.getCanHo().getMaCanHo()))
                    .collect(java.util.stream.Collectors.toList());
            
            if (!allPhuongTienList.isEmpty()) {
                System.out.println("DEBUG: Tìm thấy " + allPhuongTienList.size() + " phương tiện cần xóa hoàn toàn");
                for (io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien phuongTien : allPhuongTienList) {
                    System.out.println("DEBUG: Xóa hoàn toàn phương tiện biển số: " + phuongTien.getBienSo());
                    phuongTienRepository.delete(phuongTien);
                }
                // Flush changes để đảm bảo phương tiện được xóa trước
                entityManager.flush();
                System.out.println("DEBUG: Đã xóa hoàn toàn tất cả phương tiện");
            } else {
                System.out.println("DEBUG: Không có phương tiện nào cần xóa");
            }
            
            // 3. Xóa HOÀN TOÀN tất cả cư dân trong căn hộ
            List<io.github.ktpm.bluemoonmanagement.model.entity.CuDan> allCuDanList = 
                cuDanRepository.findAll().stream()
                    .filter(cuDan -> cuDan.getCanHo() != null && 
                                   maCanHo.equals(cuDan.getCanHo().getMaCanHo()))
                    .collect(java.util.stream.Collectors.toList());
            
            if (!allCuDanList.isEmpty()) {
                System.out.println("DEBUG: Tìm thấy " + allCuDanList.size() + " cư dân cần xóa hoàn toàn");
                for (io.github.ktpm.bluemoonmanagement.model.entity.CuDan cuDan : allCuDanList) {
                    System.out.println("DEBUG: Xóa hoàn toàn cư dân: " + cuDan.getHoVaTen() + " (" + cuDan.getMaDinhDanh() + ")");
                    cuDanRepository.delete(cuDan);
                }
                // Flush changes để đảm bảo cư dân được xóa trước
                entityManager.flush();
                System.out.println("DEBUG: Đã xóa hoàn toàn tất cả cư dân");
            } else {
                System.out.println("DEBUG: Không có cư dân nào cần xóa");
            }
            
            // 4. Xóa căn hộ (đã xóa tất cả dữ liệu liên quan)
            System.out.println("DEBUG: Bắt đầu xóa căn hộ...");
            canHoRepository.deleteById(maCanHo);
            
            // Force flush để đảm bảo tất cả thay đổi được lưu
            entityManager.flush();
            
            System.out.println("DEBUG: Đã xóa hoàn toàn căn hộ và tất cả dữ liệu liên quan thành công!");
            return new ResponseDto(true, "Căn hộ cùng với tất cả hóa đơn, phương tiện và cư dân đã được xóa hoàn toàn thành công");
            
        } catch (Exception e) {
            System.err.println("ERROR: Lỗi khi xóa căn hộ: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Lỗi khi xóa căn hộ: " + e.getMessage());
        }
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
