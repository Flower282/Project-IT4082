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

    @Override
    public ThongTinTaiKhoanDto dangNhap(DangNhapDto dangNhapDto) {
        // Lấy user từ repository bằng email (khóa chính)
        TaiKhoan taiKhoan = taiKhoanRepository.findById(dangNhapDto.getEmail()).orElse(null);
        if (taiKhoan == null) {
            return null; // Hoặc ném ra một ngoại lệ nếu không tìm thấy tài khoản
        }
        // So sánh mật khẩu
        boolean isMatch = HashPasswordUtil.verifyPassword(dangNhapDto.getMatKhau(), taiKhoan.getMatKhau());
        if (!isMatch) {
            return null; // Hoặc ném ra một ngoại lệ nếu mật khẩu không khớp
        }
        // Trả về thông tin tài khoản DTO
        return taiKhoanMapper.toThongTinTaiKhoanDto(taiKhoan);
    }

}


