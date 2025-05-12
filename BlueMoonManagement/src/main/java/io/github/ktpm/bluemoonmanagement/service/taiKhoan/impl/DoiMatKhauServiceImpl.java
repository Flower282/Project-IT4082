package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DoiMatKhauDto;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DoiMatKhauService;
import io.github.ktpm.bluemoonmanagement.util.HashPasswordUtil;
import io.github.ktpm.bluemoonmanagement.session.Session;
import org.springframework.stereotype.Service;

@Service
public class DoiMatKhauServiceImpl implements DoiMatKhauService {
    private final TaiKhoanRepository taiKhoanRepository;

    public DoiMatKhauServiceImpl(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    public ResponseDto doiMatKhau(DoiMatKhauDto doiMatKhauDto) {
        // Lấy email từ phiên đăng nhập (Session)
        String email = Session.getCurrentUser().getEmail();
        // Truy vấn tài khoản (không cần kiểm tra null vì đã đăng nhập)
        TaiKhoan taiKhoan = taiKhoanRepository.findById(email).orElse(null);
        // So sánh mật khẩu cũ
        if (!HashPasswordUtil.verifyPassword(doiMatKhauDto.getMatKhauCu(), taiKhoan.getMatKhau())) {
            return new ResponseDto(false, "Mật khẩu hiện tại không đúng");
        }
        // So sánh mật khẩu mới và xác nhận
        if (!doiMatKhauDto.getMatKhauMoi().equals(doiMatKhauDto.getXacNhanMatKhauMoi())) {
            return new ResponseDto(false, "Mật khẩu mới và xác nhận không khớp");
        }
        // Cập nhật mật khẩu mới
        taiKhoan.setMatKhau(HashPasswordUtil.hashPassword(doiMatKhauDto.getMatKhauMoi()));
        taiKhoanRepository.save(taiKhoan);
        return new ResponseDto(true, "Đổi mật khẩu thành công");
    }
}
