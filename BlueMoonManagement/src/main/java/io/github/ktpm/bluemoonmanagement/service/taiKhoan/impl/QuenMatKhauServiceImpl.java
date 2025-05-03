package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import io.github.ktpm.bluemoonmanagement.model.entity.TaiKhoan;
import io.github.ktpm.bluemoonmanagement.repository.TaiKhoanRepository;
import io.github.ktpm.bluemoonmanagement.util.OtpUtil;
import io.github.ktpm.bluemoonmanagement.util.HashPasswordUtil;
import io.github.ktpm.bluemoonmanagement.model.dto.ResponseDto;
import io.github.ktpm.bluemoonmanagement.model.dto.taiKhoan.DatLaiMatKhauDto;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.QuenMatKhauService;
import io.github.ktpm.bluemoonmanagement.service.taiKhoan.EmailService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class QuenMatKhauServiceImpl implements QuenMatKhauService {
    private final TaiKhoanRepository taiKhoanRepository;
    private final EmailService emailService;

    public QuenMatKhauServiceImpl(TaiKhoanRepository taiKhoanRepository, OtpUtil otpUtil, HashPasswordUtil hashPasswordUtil, EmailService emailService) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.emailService = emailService;
    }

    @Override
    public ResponseDto guiMaOtp(DatLaiMatKhauDto dto) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(dto.getEmail()).orElse(null);
        if (taiKhoan == null) {
            return new ResponseDto(false, "Tài khoản không tồn tại");
        }
        String otp = OtpUtil.generateOtp();
        taiKhoan.setOtp(otp);
        taiKhoan.setThoiHanOtp(LocalDateTime.now().plusMinutes(5));
        taiKhoanRepository.save(taiKhoan);
        // Gửi email OTP
        String subject = "Mã xác thực OTP"; // Có thể thay đổi sau
        String content = "Mã OTP của bạn là: <b>" + otp + "</b> (có hiệu lực trong 5 phút)" +
                "<br><br>Lưu ý: Đây là email được gửi tự động, vui lòng không phản hồi lại email này.";
        emailService.sendEmail(dto.getEmail(), subject, content, true);
        return new ResponseDto(true, "OTP đã được gửi về email");
    }

    @Override
    public ResponseDto xacThucOtp(DatLaiMatKhauDto datLaiMatKhauDto) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(datLaiMatKhauDto.getEmail()).orElse(null);
        if (taiKhoan == null) {
            return new ResponseDto(false, "Tài khoản không tồn tại");
        }
        if (taiKhoan.getOtp() == null || !taiKhoan.getOtp().equals(datLaiMatKhauDto.getOtp())) {
            return new ResponseDto(false, "OTP không hợp lệ");
        }
        if (taiKhoan.getThoiHanOtp() == null || LocalDateTime.now().isAfter(taiKhoan.getThoiHanOtp())) {
            return new ResponseDto(false, "OTP đã hết hạn");
        }
        return new ResponseDto(true, "OTP hợp lệ");
    }

    @Override
    public ResponseDto datLaiMatKhau(DatLaiMatKhauDto dto) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(dto.getEmail()).orElse(null);
        if (!dto.getMatKhauMoi().equals(dto.getXacNhanMatKhauMoi())) {
            return new ResponseDto(false, "Mật khẩu mới và xác nhận mật khẩu không khớp");
        }
        taiKhoan.setMatKhau(HashPasswordUtil.hashPassword(dto.getMatKhauMoi()));
        taiKhoan.setOtp(null);
        taiKhoan.setThoiHanOtp(null);
        taiKhoan.setNgayCapNhat(LocalDateTime.now());
        taiKhoanRepository.save(taiKhoan);
        return new ResponseDto(true, "Đặt lại mật khẩu thành công");
    }
}
