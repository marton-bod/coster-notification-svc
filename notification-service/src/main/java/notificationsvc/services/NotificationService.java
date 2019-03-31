package notificationsvc.services;

import contract.domain.ForgotPasswordInfo;
import contract.domain.WelcomeInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPostRegisterationEmail(WelcomeInfo welcomeInfo) {
        String subject = "Welcome to Coster.io!";
        String text = String.format("Hey %s!\n\nGlad to see you joining the site.", welcomeInfo.getFirstName());
        sendSimpleEmail(welcomeInfo.getEmailAddress(), subject, text);
    }

    public void sendForgotPasswordEmail(ForgotPasswordInfo forgotPasswordInfo) {
        String subject = "Your password reset link for Coster.io";
        String text = String.format("Hey %s!\n\nHere is your password reset link: %s\nYou have 24 hours to act on it.",
                forgotPasswordInfo.getFirstName(), forgotPasswordInfo.getPasswordResetUrl());
        sendSimpleEmail(forgotPasswordInfo.getEmailAddress(), subject, text);
    }

    private void sendSimpleEmail(String to, String subject, String text) {
        if (fromEmail.equals("disabled")) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
