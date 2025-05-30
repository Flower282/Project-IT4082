package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DangKiDto;
import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import io.github.ktpm.bluemoonmanagement.model.mapper.TaiKhoanMapper;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.DangKiService;
import io.github.ktpm.bluemoonmanagement.util.PasswordUtil;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.EmailService;
import io.github.ktpm.bluemoonmanagement.session.Session;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class DangKiServiceImpl implements DangKiService {
    private final TaiKhoanRepository taiKhoanRepository;
    private final TaiKhoanMapper taiKhoanMapper;
    private final EmailService emailService;

    public DangKiServiceImpl(TaiKhoanRepository taiKhoanRepository, TaiKhoanMapper taiKhoanMapper, EmailService emailService) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.taiKhoanMapper = taiKhoanMapper;
        this.emailService = emailService;
    }

    @Override
    public ResponseDto dangKiTaiKhoan(DangKiDto dangKiDto) {
        // Kiểm tra quyền: chỉ 'Tổ trưởng' mới được đăng ký tài khoản
        if (Session.getCurrentUser() == null || !"Tổ trưởng".equals(Session.getCurrentUser().getVaiTro())) {
            return new ResponseDto(false, "Bạn không có quyền đăng ký tài khoản. Chỉ Tổ trưởng mới được phép.");
        }
        // Kiểm tra email đã tồn tại chưa
        if (taiKhoanRepository.existsById(dangKiDto.getEmail())) {
            return new ResponseDto(false, "Email đã tồn tại");
        }
        // Tạo mật khẩu ngẫu nhiên
        String matKhau = PasswordUtil.generatePassword();
        // Tạo tài khoản mới
        TaiKhoan taiKhoan = taiKhoanMapper.fromDangKiDto(dangKiDto);
        taiKhoan.setMatKhau(PasswordUtil.hashPassword(matKhau));
        taiKhoan.setNgayTao(LocalDateTime.now());
        taiKhoan.setNgayCapNhat(LocalDateTime.now());
        taiKhoanRepository.save(taiKhoan);
        // Gửi email thông tin tài khoản cho người dùng (HTML)
        String subject = "Thông tin tài khoản BlueMoonManagement";
        String content = String.format(
            """
            <div style='font-family:Arial,sans-serif;'>
                <h2>Chào %s,</h2>
                <p>Tài khoản của bạn đã được tạo thành công.</p>
                <ul>
                    <li><b>Họ tên:</b> %s</li>
                    <li><b>Tên đăng nhập (Email):</b> %s</li>
                    <li><b>Mật khẩu:</b> %s</li>
                </ul>
                <p style='color:red;'><b>Lưu ý:</b> Không cung cấp thông tin tài khoản cho bất kỳ ai. Không phản hồi email này.</p>
                <p>Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu.</p>
                <p>Trân trọng!</p>
            </div>
            """,
            dangKiDto.getHoTen(),
            dangKiDto.getHoTen(),
            dangKiDto.getEmail(),
            matKhau
        );
        emailService.sendEmail(dangKiDto.getEmail(), subject, content, true);
        // Trả về phản hồi thành công
        return new ResponseDto(true, "Đăng kí thành công");
    }
}
