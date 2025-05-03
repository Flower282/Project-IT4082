package io.github.ktpm.bluemoonmanagement.service.taiKhoan.impl;

import io.github.ktpm.bluemoonmanagement.service.taiKhoan.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject); // subject sẽ bổ sung sau
        message.setText(content);    // content sẽ bổ sung sau
        mailSender.send(message);
    }
}
