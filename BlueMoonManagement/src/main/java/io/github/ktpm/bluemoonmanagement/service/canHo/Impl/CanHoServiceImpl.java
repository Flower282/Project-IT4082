package io.github.ktpm.bluemoonmanagement.service.canHo.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoChiTietDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;
import io.github.ktpm.bluemoonmanagement.model.entity.CuDan;
import io.github.ktpm.bluemoonmanagement.model.mapper.CanHoMapper;
import io.github.ktpm.bluemoonmanagement.repository.CanHoRepository;
import io.github.ktpm.bluemoonmanagement.repository.CuDanRepository;
import io.github.ktpm.bluemoonmanagement.service.canHo.CanHoService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.XlsxExportUtil;
import io.github.ktpm.bluemoonmanagement.util.XlxsFileUtil;

@Service
public class CanHoServiceImpl implements CanHoService {

    private final CanHoRepository canHoRepository;
    private final CuDanRepository cuDanRepository;
    private final CanHoMapper canHoMapper;

    public CanHoServiceImpl(CanHoRepository canHoRepository, CuDanRepository cuDanRepository, CanHoMapper canHoMapper) {
        this.canHoRepository = canHoRepository;
        this.cuDanRepository = cuDanRepository;
        this.canHoMapper = canHoMapper;
    }

    @Override
    public List<CanHoDto> getAllCanHo() {
        List<CanHo> canHoList = canHoRepository.findAll();
        return canHoList.stream()
                .map(canHoMapper::toCanHoDto)
                .collect(Collectors.toList());
    }

    @Override
    public CanHoChiTietDto getCanHoChiTiet(CanHoDto canHoDto) {
        CanHo canHo = canHoRepository.findById(canHoDto.getMaCanHo()).orElse(null);
        return canHoMapper.toCanHoChiTietDto(canHo);
    }

    @Override
    public ResponseDto addCanHo(CanHoDto canHoDto) {
        // Kiểm tra quyền: chỉ 'Tổ trưởng' và 'admin' mới được thêm căn hộ
        if (Session.getCurrentUser() == null || (!"Tổ trưởng".equals(Session.getCurrentUser().getVaiTro()) && !"admin".equals(Session.getCurrentUser().getVaiTro()))) {
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
