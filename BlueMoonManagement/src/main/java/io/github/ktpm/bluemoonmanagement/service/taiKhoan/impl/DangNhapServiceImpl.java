package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangNhapDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.ThongTinTaiKhoanDto;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import io.github.ktpm.bluemoonmanagement.model.mapper.TaiKhoanMapper;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DangNhapServive;
import io.github.ktpm.bluemoonmanagement.util.HashPasswordUtil;

@Service
public class DangNhapServiceImpl implements DangNhapServive {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private TaiKhoanMapper taiKhoanMapper;

    @Autowired
    public DangNhapServiceImpl(TaiKhoanRepository taiKhoanRepository){
        this.taiKhoanRepository = taiKhoanRepository;
    }

    /**
     * Kiểm tra email có tồn tại trong hệ thống không (bước 1)
     */
    @Override
    public boolean kiemTraEmailTonTai(String email) {
        return taiKhoanRepository.existsById(email);
    }

    /**
     * Kiểm tra mật khẩu và trả về thông tin tài khoản nếu đúng (bước 2)
     */
    @Override
    public ThongTinTaiKhoanDto dangNhap(DangNhapDto dangNhapDto) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(dangNhapDto.getEmail()).orElse(null);
        if (taiKhoan == null) {
            return null;
        }
        boolean isMatch = HashPasswordUtil.verifyPassword(dangNhapDto.getMatKhau(), taiKhoan.getMatKhau());
        if (!isMatch) {
            return null;
        }
        return taiKhoanMapper.toThongTinTaiKhoanDto(taiKhoan);
    }

}


