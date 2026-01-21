package com.netflix.serviceImpl;

import com.netflix.exception.EmailNotVerifiedException;
import com.netflix.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//Add your annotations here
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${app.frontendy.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private MailSender mailSender;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Netflix movies - Verify your Email");

            String verificationLink = frontendUrl + "/verify-email?token=" + token;

            String emailBody =
                    "Welcome to Netflix Movies!\n\n"
                    + "Thank you for registering. Please verify your email by clicking the link below : \n\n"
                    + verificationLink
                    + "\n\n"
                    + "This link will expire in 24 hours.\n\n"
                    + "If you didn't create this account, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "Villa Tech Team";

            message.setText(emailBody);
            mailSender.send(message);

            logger.info("Verification email send to {} : {} ", toEmail);
        } catch (Exception ex) {
            logger.error("Failed to send verification email to {} : {} ", toEmail, ex.getMessage(), ex);
            throw new EmailNotVerifiedException("Failed to send verification email");
        }
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Netflix Movies - Reset Password");

            String resetLink = frontendUrl + "/reset-password?token=" + token;

            String emailBody =
                    "Hi, \n\n"
                    + "We received a request to reset your password. Click the link below to reset it : \n\n"
                    + resetLink
                    + "\n\n"
                    + "This link will expire in 1 hour.\n\n"
                    + "If you did not request a password reset, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "Villa Tech Team";

            message.setText(emailBody);
            mailSender.send(message);

            logger.info("Password reset email sent to {} : {} ", toEmail);
        } catch (Exception ex) {
            logger.error("Failed to send password reset email to {} : {} ", toEmail, ex.getMessage(), ex);
            throw new RuntimeException("Failed to send reset email");
        }
    }
}
