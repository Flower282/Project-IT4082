package io.github.ktpm.bluemoonmanagement.service.taiKhoan;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
