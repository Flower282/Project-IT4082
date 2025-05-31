package io.github.ktpm.bluemoonmanagement.util;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OtpQuenMatKhauTest {
    @Test
    void testGenerateOtpLengthAndRange() {
        for (int i = 0; i < 100; i++) {
            String otp = OtpUtil.generateOtp();
            assertEquals(6, otp.length());
            int value = Integer.parseInt(otp);
            assertTrue(value >= 100000 && value <= 999999);
        }
    }

    @SuppressWarnings("null")
    @Test
    void testSendEmailQuenMatKhauSendsMail() {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        String email = "test@example.com";
        String hoTen = "Nguyen Van A";
        String otp = "123456";

        // Only verify that MimeMessage is sent (HTML email)
        doNothing().when(mailSender).send(any(MimeMessage.class));

        EmailUtil.sendEmailQuenMatKhau(email, hoTen, otp, mailSender);
        verify(mailSender, atLeastOnce()).send(any(MimeMessage.class));
    }

    // To avoid future JDK issues with Mockito inline mocking, add the following JVM argument when running tests:
    // -javaagent:"C:/Users/ASUS/.m2/repository/org/mockito/mockito-core/5.2.0/mockito-core-5.2.0.jar"
    // (Hoặc thay đường dẫn cho đúng với máy của bạn)
    // Tham khảo thêm: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3
    // Đây chỉ là cảnh báo, test vẫn chạy bình thường trên JDK hiện tại.
}
