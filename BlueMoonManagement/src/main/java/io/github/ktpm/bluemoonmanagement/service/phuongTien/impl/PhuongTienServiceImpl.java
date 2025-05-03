package io.github.ktpm.bluemoonmanagement.service.phuongTien.impl;

import io.github.ktpm.bluemoonmanagement.model.dto.PhuongTienDto;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.entity.PhuongTien;
import io.github.ktpm.bluemoonmanagement.model.mapper.PhuongTienMapper;
import io.github.ktpm.bluemoonmanagement.repository.PhuongTienRepository;
import io.github.ktpm.bluemoonmanagement.service.phuongTien.PhuongTienService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhuongTienServiceImpl implements PhuongTienService {
    private final PhuongTienRepository phuongTienRepository;
    private final PhuongTienMapper phuongTienMapper;

    public PhuongTienServiceImpl(PhuongTienRepository phuongTienRepository, PhuongTienMapper phuongTienMapper) {
        this.phuongTienRepository = phuongTienRepository;
        this.phuongTienMapper = phuongTienMapper;
    }

    @Override
    public ResponseDto themPhuongTien(PhuongTienDto phuongTienDto) {
        if (phuongTienDto.getBienSo() != null && phuongTienRepository.existsByBienSo(phuongTienDto.getBienSo())) {
            return new ResponseDto(false, "Biển số xe đã tồn tại");
        }
        PhuongTien phuongTien = phuongTienMapper.fromPhuongTienDto(phuongTienDto);
        phuongTienRepository.save(phuongTien);
        return new ResponseDto(true, "Thêm phương tiện thành công");
    }

    @Override
    public ResponseDto capNhatPhuongTien(PhuongTienDto phuongTienDto) {
        PhuongTien phuongTien = phuongTienRepository.findById(phuongTienDto.getSoThuTu()).orElse(null);
        phuongTien.setBienSo(phuongTienDto.getBienSo());
        phuongTienRepository.save(phuongTien);
        return new ResponseDto(true, "Cập nhật phương tiện thành công");
    }

    @Override
    public ResponseDto xoaPhuongTien(PhuongTienDto phuongTienDto) {
        if (!phuongTienRepository.existsById(phuongTienDto.getSoThuTu())) {
            return new ResponseDto(false, "Phương tiện không tồn tại");
        }
        phuongTienRepository.deleteById(phuongTienDto.getSoThuTu());
        return new ResponseDto(true, "Xóa phương tiện thành công");
    }

    @Override
    public List<PhuongTienDto> layDanhSachPhuongTien() {
        return phuongTienRepository.findAll().stream()
                .map(phuongTienMapper::toPhuongTienDto)
                .collect(Collectors.toList());
    }
}
