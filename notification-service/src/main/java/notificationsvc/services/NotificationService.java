package notificationsvc.services;

import contract.domain.ForgotPasswordInfo;
import contract.domain.WelcomeInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationsvc.services.model.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPostRegisterationEmail(WelcomeInfo welcomeInfo) {
        if (emailDisabled()) {
            log.info("Email sending disabled. Not sending post-registration email.");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("title", "Welcome to the site!");
        model.put("name", welcomeInfo.getFirstName());
        model.put("body1", "Glad to see you joining our community!");
        model.put("body2", "Start adding your expenses today, filter and search for specific items you're interested in. " +
                "Check also your dashboard for analytics and charts. We wish you a good time using the site!");

        Mail mail = Mail.builder()
                .from(fromEmail)
                .to(welcomeInfo.getEmailAddress())
                .subject("Welcome to Coster.io!")
                .model(model)
                .build();
        sendHtmlEmail(mail);
    }

    private boolean emailDisabled() {
        return fromEmail == null || fromEmail.isBlank() || fromEmail.equals("disabled");
    }

    public void sendForgotPasswordEmail(ForgotPasswordInfo forgotPasswordInfo) {
        if (emailDisabled()) {
            log.info("Email sending disabled. Not sending password reset email.");
            return;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("title", "Password Reset");
        model.put("name", forgotPasswordInfo.getFirstName());
        model.put("body1", "Here is your password reset link:" + forgotPasswordInfo.getPasswordResetUrl());
        model.put("body2", "You have 72 hours to activate the link before it expires.");

        Mail mail = Mail.builder()
                .from(fromEmail)
                .to(forgotPasswordInfo.getEmailAddress())
                .subject("Your password reset link for Coster.io")
                .model(model)
                .build();
        sendHtmlEmail(mail);
    }

    private void sendHtmlEmail(Mail mail) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(mail.getModel());
            String html = templateEngine.process("mailTemplate", context);

            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        emailSender.send(message);
    }

    private void sendSimpleEmail(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(mail.getTo());
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());
        emailSender.send(message);
    }
}
