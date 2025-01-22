package com.example.connect.domain.user.service;

import com.example.connect.global.util.VerifyKeyCreator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendEmail(String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            String key = VerifyKeyCreator.createKey();

            helper.setTo(to);
            helper.setSubject("이메일 인증 코드");

            Context context = new Context();
            context.setVariable("code", key);

            String html = templateEngine.process("email-template", context);
            helper.setText(html, true);

            helper.addInline("logoImage", new ClassPathResource("static/images/logo.png"));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
