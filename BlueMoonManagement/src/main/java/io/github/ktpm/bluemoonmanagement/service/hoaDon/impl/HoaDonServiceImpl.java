package io.github.ktpm.bluemoonmanagement.service.hoaDon.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.canHo.CanHoHoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonTuNguyenDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuDto;
import io.github.ktpm.bluemoonmanagement.model.dto.khoanThu.KhoanThuTuNguyenDto;
import io.github.ktpm.bluemoonmanagement.model.dto.hoaDon.HoaDonDichVuDto;
import io.github.ktpm.bluemoonmanagement.model.entity.CanHo;
import io.github.ktpm.bluemoonmanagement.model.entity.HoaDon;
import io.github.ktpm.bluemoonmanagement.model.entity.KhoanThu;
import io.github.ktpm.bluemoonmanagement.model.mapper.CanHoMapper;
import io.github.ktpm.bluemoonmanagement.model.mapper.HoaDonMapper;
import io.github.ktpm.bluemoonmanagement.repository.CanHoRepository;
import io.github.ktpm.bluemoonmanagement.repository.HoaDonRepository;
import io.github.ktpm.bluemoonmanagement.repository.KhoanThuRepository;
import io.github.ktpm.bluemoonmanagement.service.hoaDon.HoaDonService;
import io.github.ktpm.bluemoonmanagement.session.Session;
import io.github.ktpm.bluemoonmanagement.util.XlxsFileUtil;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;
import java.util.function.Function;

@Service
public class HoaDonServiceImpl implements HoaDonService {
    
    private final HoaDonRepository hoaDonRepository;
    private final CanHoRepository canHoRepository;
    private final KhoanThuRepository khoanThuRepository;
    private final HoaDonMapper hoaDonMapper;
    private final CanHoMapper canHoMapper;
    
    public HoaDonServiceImpl(HoaDonRepository hoaDonRepository, HoaDonMapper hoaDonMapper, CanHoRepository canHoRepository, CanHoMapper canHoMapper, KhoanThuRepository khoanThuRepository) {
        this.hoaDonRepository = hoaDonRepository;
        this.canHoRepository = canHoRepository;
        this.hoaDonMapper = hoaDonMapper;
        this.canHoMapper = canHoMapper;
        this.khoanThuRepository = khoanThuRepository;
    }
    

    private List<CanHoHoaDonDto> getCanHoHoaDonDtoList() {
        List<CanHo> canHoList = canHoRepository.findAll();
        return canHoList.stream()
                .map(canHoMapper::toCanHoHoaDonDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto generateHoaDon(KhoanThuDto khoanThuDto) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền tạo hóa đơn. Chỉ Kế toán mới được phép.");
        }
        if(!khoanThuDto.isBatBuoc()) {
            return new ResponseDto(false, "Đây là khoản thu tự nguyện");
        }
        KhoanThu khoanThu = khoanThuRepository.findById(khoanThuDto.getMaKhoanThu()).orElse(null);
        if(khoanThu.isTaoHoaDon()) {
            return new ResponseDto(false, "Hóa đơn cho khoản thu này đã được tạo");
        }
        HoaDonDichVuDto hoaDonDichVuDto = new HoaDonDichVuDto();
        List<HoaDonDichVuDto> hoaDonList = new ArrayList<>();
        List<CanHoHoaDonDto> canHoList = getCanHoHoaDonDtoList();
        for (CanHoHoaDonDto canHo : canHoList) {
            hoaDonDichVuDto.setTenKhoanThu(khoanThuDto.getTenKhoanThu());
            hoaDonDichVuDto.setMaCanHo(canHo.getMaCanHo());
            String donViTinh = khoanThuDto.getDonViTinh();
            switch (donViTinh) {
                case "Diện tích":
                    if(khoanThuDto.getPhamVi().equals("Tất cả")) {
                        int soTien = (int)Math.ceil(khoanThuDto.getSoTien() * canHo.getDienTich());
                        hoaDonDichVuDto.setSoTien(soTien);
                    } else if(khoanThuDto.getPhamVi().equals("Căn hộ đang sử dụng")) {
                        if(canHo.getTrangThaiSuDung().equals("Đang sử dụng")) {
                            int soTien = (int)Math.ceil(khoanThuDto.getSoTien() * canHo.getDienTich());
                            hoaDonDichVuDto.setSoTien(soTien);
                        }else{
                            continue;
                        }
                    }
                    break;
                case "Phương tiện":
                    int countXeDap = (int)canHo.getPhuongTienList().stream()
                                        .filter(phuongTien -> phuongTien.getLoaiPhuongTien().equals("Xe đạp"))
                                        .count();
                    int soTienXeDap = khoanThuDto.getPhiGuiXeList().stream()
                                        .filter(phiGuiXe -> phiGuiXe.getLoaiXe().equals("Xe đạp"))
                                        .findFirst()
                                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                                        .orElse(0); 
                    int tienGuiXeDap = countXeDap * soTienXeDap;

                    int countXeMay = (int)canHo.getPhuongTienList().stream()
                                        .filter(phuongTien -> phuongTien.getLoaiPhuongTien().equals("Xe máy"))
                                        .count();
                    int soTienXeMay = khoanThuDto.getPhiGuiXeList().stream()
                                        .filter(phiGuiXe -> phiGuiXe.getLoaiXe().equals("Xe máy"))
                                        .findFirst()
                                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                                        .orElse(0);
                    int tienGuiXeMay = countXeMay * soTienXeMay;

                    int countOto = (int)canHo.getPhuongTienList().stream()
                                        .filter(phuongTien -> phuongTien.getLoaiPhuongTien().equals("Ô tô"))
                                        .count();
                    int soTienOto = khoanThuDto.getPhiGuiXeList().stream()
                                        .filter(phiGuiXe -> phiGuiXe.getLoaiXe().equals("Ô tô"))
                                        .findFirst()
                                        .map(phiGuiXe -> phiGuiXe.getSoTien())
                                        .orElse(0);
                    int tienGuiOto = countOto * soTienOto;
                    hoaDonDichVuDto.setSoTien(tienGuiXeDap + tienGuiXeMay + tienGuiOto);
                    break;
                default:
                    return new ResponseDto(false, "Đơn vị tính không hợp lệ");
            }
            hoaDonList.add(hoaDonDichVuDto);
        }
        khoanThu.setTaoHoaDon(true);
        khoanThuRepository.save(khoanThu);
        return new ResponseDto(true, "Hóa đơn đã được thêm thành công");
    }
    
    @Override
    public List<HoaDonDto> getAllHoaDon() {
        // Get all bills from repository
        List<HoaDon> hoaDonList = hoaDonRepository.findAll();
        // Convert entities to DTOs and return
        return hoaDonList.stream()
                .map(hoaDonMapper::toHoaDonDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto addHoaDonTuNguyen(HoaDonTuNguyenDto hoaDonTuNguyenDto, KhoanThuTuNguyenDto khoanThuTuNguyenDto) {
        if (Session.getCurrentUser() == null || !"Kế toán".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền thêm hóa đơn tự nguyện. Chỉ Kế toán mới được phép.");
        }
        hoaDonTuNguyenDto.setTenKhoanThu(khoanThuTuNguyenDto.getTenKhoanThu());
        HoaDon hoaDon = hoaDonMapper.fromHoaDonTuNguyenDto(hoaDonTuNguyenDto);
        hoaDon.setNgayNop(LocalDateTime.now());
        hoaDon.setDaNop(true);
        hoaDonRepository.save(hoaDon);
        KhoanThu khoanThu = khoanThuRepository.findById(khoanThuTuNguyenDto.getMaKhoanThu()).orElse(null);
        khoanThu.setTaoHoaDon(true);
        khoanThuRepository.save(khoanThu);
        return new ResponseDto(true, "Hóa đơn đã được thêm thành công");
    }

    @Override
    public int importFromExcel(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("hoadondichvu_import", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            Function<Row, HoaDonDichVuDto> rowMapper = row -> {
                try {
                    HoaDonDichVuDto dto = new HoaDonDichVuDto();
                    dto.setTenKhoanThu(row.getCell(0).getStringCellValue());
                    dto.setMaCanHo(row.getCell(1).getStringCellValue());
                    dto.setSoTien((int) row.getCell(2).getNumericCellValue());
                    // Add more fields if needed
                    return dto;
                } catch (Exception e) {
                    return null;
                }
            };
            List<HoaDonDichVuDto> hoaDonDichVuDtoList = XlxsFileUtil.importFromExcel(tempFile.getAbsolutePath(), rowMapper);
            List<HoaDon> hoaDonList = hoaDonDichVuDtoList.stream().map(hoaDonMapper::fromHoaDonDichVuDto).collect(Collectors.toList());
            hoaDonRepository.saveAll(hoaDonList);
            tempFile.delete();
            return hoaDonList.size();
        } catch (Exception e) {
            throw new RuntimeException("Failed to import HoaDon from Excel", e);
        }
    }
}
