package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangKiDto;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import io.github.ktpm.bluemoonmanagement.model.mapper.TaiKhoanMapper;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DangKiService;
import io.github.ktpm.bluemoonmanagement.util.HashPasswordUtil;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class DangKiServiceImpl implements DangKiService {
    private final TaiKhoanRepository taiKhoanRepository;
    private final TaiKhoanMapper taiKhoanMapper;

    public DangKiServiceImpl(TaiKhoanRepository taiKhoanRepository, TaiKhoanMapper taiKhoanMapper) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.taiKhoanMapper = taiKhoanMapper;
    }

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
        taiKhoan.setNgayTao(LocalDateTime.now());
        taiKhoan.setNgayCapNhat(LocalDateTime.now());
        taiKhoanRepository.save(taiKhoan);
        // Trả về phản hồi thành công
        return new ResponseDto(true, "Đăng ký thành công");
    }
}
