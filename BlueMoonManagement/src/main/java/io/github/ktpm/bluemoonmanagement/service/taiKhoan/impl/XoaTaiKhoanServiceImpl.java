package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.XoaTaiKhoanService;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import org.springframework.stereotype.Service;

@Service
public class XoaTaiKhoanServiceImpl implements XoaTaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;

    public XoaTaiKhoanServiceImpl(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    public void xoaTaiKhoan(ThongTinTaiKhoanDto thongTinTaiKhoanDto) {
        taiKhoanRepository.deleteById(thongTinTaiKhoanDto.getEmail());
    }
}
