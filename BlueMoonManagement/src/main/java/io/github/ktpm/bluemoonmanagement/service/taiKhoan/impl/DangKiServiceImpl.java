package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangKiDto;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import io.github.ktpm.bluemoonmanagement.model.mapper.TaiKhoanMapper;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DangKiService;
import io.github.ktpm.bluemoonmanagement.util.HashPasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DangKiServiceImpl implements DangKiService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private TaiKhoanMapper taiKhoanMapper;

    @Override
    public ResponseDto dangKiTaiKhoan(DangKiDto dangKiDto) {
        // Kiểm tra email đã tồn tại chưa
        if (taiKhoanRepository.existsById(dangKiDto.getEmail())) {
            return new ResponseDto(false, "Email đã tồn tại");
        }
        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (!dangKiDto.getMatKhau().equals(dangKiDto.getXacNhanMatKhau())) {
            return new ResponseDto(false, "Mật khẩu và xác nhận mật khẩu không khớp");
        }
        // Tạo tài khoản mới
        TaiKhoan taiKhoan = taiKhoanMapper.fromDangKiDto(dangKiDto);
        taiKhoan.setMatKhau(HashPasswordUtil.hashPassword(dangKiDto.getMatKhau()));
        taiKhoanRepository.save(taiKhoan);
        // Trả về phản hồi thành công
        return new ResponseDto(true, "Đăng ký thành công");
    }
}
