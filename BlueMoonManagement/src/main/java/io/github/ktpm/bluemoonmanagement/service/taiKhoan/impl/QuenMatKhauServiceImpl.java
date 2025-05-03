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
    private final OtpUtil otpUtil;
    private final HashPasswordUtil hashPasswordUtil;
    private final EmailService emailService;

    public QuenMatKhauServiceImpl(TaiKhoanRepository taiKhoanRepository, OtpUtil otpUtil, HashPasswordUtil hashPasswordUtil, EmailService emailService) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.otpUtil = otpUtil;
        this.hashPasswordUtil = hashPasswordUtil;
        this.emailService = emailService;
    }

    @Override
    public ResponseDto guiMaOtp(String email) {
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findById(email);
        if (optionalTaiKhoan.isEmpty()) {
            return new ResponseDto(false, "Tài khoản không tồn tại");
        }
        TaiKhoan taiKhoan = optionalTaiKhoan.get();
        String otp = otpUtil.generateOtp();
        taiKhoan.setOtp(otp);
        taiKhoan.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        taiKhoanRepository.save(taiKhoan);
        // Gửi email OTP
        String subject = "Mã xác thực OTP"; // Có thể thay đổi sau
        String content = "Mã OTP của bạn là: " + otp + " (có hiệu lực trong 5 phút)";
        emailService.sendEmail(email, subject, content);
        return new ResponseDto(true, "OTP đã được gửi về email");
    }

    @Override
    public ResponseDto xacThucOtp(String otp) {
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findAll().stream()
            .filter(tk -> otp.equals(tk.getOtp()) && tk.getOtpExpiry() != null && tk.getOtpExpiry().isAfter(LocalDateTime.now()))
            .findFirst();
        if (optionalTaiKhoan.isEmpty()) {
            return new ResponseDto(false, "OTP không hợp lệ hoặc đã hết hạn");
        }
        return new ResponseDto(true, "OTP hợp lệ");
    }

    @Override
    public ResponseDto datLaiMatKhau(DatLaiMatKhauDto dto) {
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findById(dto.getEmail());
        if (optionalTaiKhoan.isEmpty()) {
            return new ResponseDto(false, "Tài khoản không tồn tại");
        }
        TaiKhoan taiKhoan = optionalTaiKhoan.get();
        if (taiKhoan.getOtp() == null || !taiKhoan.getOtp().equals(dto.getOtp())) {
            return new ResponseDto(false, "OTP không đúng");
        }
        if (taiKhoan.getOtpExpiry() == null || taiKhoan.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return new ResponseDto(false, "OTP đã hết hạn");
        }
        if (!dto.getMatKhauMoi().equals(dto.getXacNhanMatKhauMoi())) {
            return new ResponseDto(false, "Mật khẩu mới và xác nhận mật khẩu không khớp");
        }
        taiKhoan.setMatKhau(hashPasswordUtil.hashPassword(dto.getMatKhauMoi()));
        taiKhoan.setOtp(null);
        taiKhoan.setOtpExpiry(null);
        taiKhoanRepository.save(taiKhoan);
        return new ResponseDto(true, "Đặt lại mật khẩu thành công");
    }
}
