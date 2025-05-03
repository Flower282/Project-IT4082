package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.QuanLyTaiKhoanService;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import java.time.LocalDateTime;

public class QuanLyTaiKhoanServiceImpl implements QuanLyTaiKhoanService {
    private final TaiKhoanRepository taiKhoanRepository;

    public QuanLyTaiKhoanServiceImpl(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    public void thayDoiThongTinTaiKhoan(ThongTinTaiKhoanDto dto) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(dto.getEmail()).orElse(null);
        taiKhoan.setHoTen(dto.getHoTen());
        taiKhoan.setVaiTro(dto.getVaiTro());
        taiKhoan.setNgayCapNhat(LocalDateTime.now());
        taiKhoanRepository.save(taiKhoan);
    }

    @Override
    public void xoaTaiKhoan(ThongTinTaiKhoanDto thongTinTaiKhoanDto) {
        taiKhoanRepository.deleteById(thongTinTaiKhoanDto.getEmail());
    }
}

