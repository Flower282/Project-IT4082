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
import io.github.ktpm.bluemoonmanagement.model.mapper.CanHoMapper;
import io.github.ktpm.bluemoonmanagement.model.mapper.HoaDonMapper;
import io.github.ktpm.bluemoonmanagement.repository.CanHoRepository;
import io.github.ktpm.bluemoonmanagement.repository.HoaDonRepository;
import io.github.ktpm.bluemoonmanagement.service.hoaDon.HoaDonService;

@Service
public class HoaDonServiceImpl implements HoaDonService {
    
    private final HoaDonRepository hoaDonRepository;
    private final CanHoRepository canHoRepository;
    private final HoaDonMapper hoaDonMapper;
    private final CanHoMapper canHoMapper;
    
    public HoaDonServiceImpl(HoaDonRepository hoaDonRepository, HoaDonMapper hoaDonMapper, CanHoRepository canHoRepository, CanHoMapper canHoMapper) {
        this.hoaDonRepository = hoaDonRepository;
        this.canHoRepository = canHoRepository;
        this.hoaDonMapper = hoaDonMapper;
        this.canHoMapper = canHoMapper;
    }
    

    private List<CanHoHoaDonDto> getCanHoHoaDonDtoList() {
        List<CanHo> canHoList = canHoRepository.findAll();
        return canHoList.stream()
                .map(canHoMapper::toCanHoHoaDonDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDto generateHoaDon(KhoanThuDto khoanThuDto) {
        if(!khoanThuDto.isBatBuoc()) {
            return new ResponseDto(false, "Đây là khoản thu tự nguyện");
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
                case "Xe đạp":
                    int countXeDap = (int)canHo.getPhuongTienList().stream()
                                        .filter(phuongTien -> phuongTien.getLoaiPhuongTien().equals("Xe đạp"))
                                        .count();
                    hoaDonDichVuDto.setSoTien(countXeDap * khoanThuDto.getSoTien());
                    break;
                case "Xe máy":
                    int countXeMay = (int)canHo.getPhuongTienList().stream()
                                        .filter(phuongTien -> phuongTien.getLoaiPhuongTien().equals("Xe máy"))
                                        .count();
                    hoaDonDichVuDto.setSoTien(countXeMay * khoanThuDto.getSoTien());
                    break;
                case "Ô tô":
                    int countOto = (int)canHo.getPhuongTienList().stream()
                                        .filter(phuongTien -> phuongTien.getLoaiPhuongTien().equals("Ô tô"))
                                        .count();
                    hoaDonDichVuDto.setSoTien(countOto * khoanThuDto.getSoTien());
                    break;
                default:
                    return new ResponseDto(false, "Đơn vị tính không hợp lệ");
            }
            hoaDonList.add(hoaDonDichVuDto);
        }
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
        hoaDonTuNguyenDto.setTenKhoanThu(khoanThuTuNguyenDto.getTenKhoanThu());
        HoaDon hoaDon = hoaDonMapper.fromHoaDonTuNguyenDto(hoaDonTuNguyenDto);
        hoaDon.setNgayNop(LocalDateTime.now());
        hoaDon.setDaNop(true);
        hoaDonRepository.save(hoaDon);
        return new ResponseDto(true, "Hóa đơn đã được thêm thành công");
    }
}
