package system.dev.marques.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.*;
import system.dev.marques.exception.EmailSendingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String email;

    private static final String ERROR_MESSAGE = "Unable to send email: ";

    private final JavaMailSender mailSender;

    public void sendUserValidationLink(ValidUserDto dto) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(dto.getEmail());
            helper.setSubject("Validation Link");
            helper.setText("Link for validation: " + dto.getUrl(), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException(ERROR_MESSAGE + e);
        }

    }

    public void sendUserCreatedEmail(CreatedUserDto dto) {
        MimeMessage message = mailSender.createMimeMessage();

        String emailBody = String.format(
                "<div style='font-family: Arial, sans-serif; color: #333;'>"
                        + "<h2>Account created</h2>"
                        + "<p>Hello %s,</p>"
                        + "<p>Your account has been created, but you must validate it!</p>"
                        + "<p><strong>Here is your link! It can only be accessed once and it expires in 10 minutes:</strong></p>"
                        + "<p>%s</p>"
                        + "<p>Best regards,</p>"
                        + "<p>Credit System</p>"
                        + "<br>"
                        + "</div>",
                dto.getName(),
                dto.getUrl()

        );
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(dto.getEmail());
            helper.setSubject("New Account Created");
            helper.setText(emailBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException(ERROR_MESSAGE + e);
        }
    }

    public void sendUserDeleteFormEmail(DeleteUserDto dto) {
        MimeMessage message = mailSender.createMimeMessage();

        String emailBody = String.format(
                "<div style='font-family: Arial, sans-serif; color: #333;'>"
                        + "<h2>Delete Request</h2>"
                        + "<p>Hello Admin,</p>"
                        + "<p>This user has requested an account deletion: %s</p>"
                        + "<p>User id: %d</p>"
                        + "<p>Request Reason:</p>"
                        + "<p><strong>%s</strong></p>"
                        + "<p>Best regards,</p>"
                        + "<p>Credit System</p>"
                        + "<br>"
                        + "</div>",
                dto.getUserEmail(),
                dto.getUserId(),
                dto.getReason()
        );
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setBcc(dto.getAdminEmails().toArray(new String[0]));
            helper.setSubject("User Account Deletion Request");
            helper.setText(emailBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException(ERROR_MESSAGE + e);
        }
    }

    public void sendDeletionConfirmation(DeleteUserConfirmationDto dto) {
        MimeMessage message = mailSender.createMimeMessage();

        String emailBody = String.format(
                "<div style='font-family: Arial, sans-serif; color: #333;'>"
                        + "<h2>Delete Request</h2>"
                        + "<p>Hello %s,</p>"
                        + "<p><strong>Your account has been successfully deleted</strong></p>"
                        + "<p>Date: %s</p>"
                        + "<p>Best regards,</p>"
                        + "<p>Credit System</p>"
                        + "<br>"
                        + "</div>",
                dto.getUserName(),
                dto.getDate()
        );
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(dto.getUserEmail());
            helper.setSubject("User Account Deletion Request");
            helper.setText(emailBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException(ERROR_MESSAGE + e);
        }
    }

    public void sendProposalStatusEmail(ProposalStatusEmailDto dto) {
        if (dto.getStatus().equals("REJECTED")) {
            dto.setStatus("REJECTED. Reason: " + dto.getRejectedReason());
        }
        MimeMessage message = mailSender.createMimeMessage();

        String emailBody = String.format(
                "<div style='font-family: Arial, sans-serif; color: #333;'>"
                        + "<h2>Proposal Status Changed!</h2>"
                        + "<p>Hello %s,</p>"
                        + "<p>Your proposal has been analyzed!</p>"
                        + "<p><strong>Proposal Info:</strong></p>"
                        + "<p>Requested Amount: %.2f</p>"
                        + "<p>Installments: %d</p>"
                        + "<p>Proposal Purpose: %s</p>"
                        + "<p>Proposal Decision: %s</p>"
                        + "<p>Best regards,</p>"
                        + "<p>Credit System</p>"
                        + "<br>"
                        + "</div>",
                dto.getUserName(),
                dto.getRequestedAmount(),
                dto.getInstallments(),
                dto.getPurpose(),
                dto.getStatus()
        );
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(dto.getUserEmail());
            helper.setSubject("PROPOSAL STATUS");
            helper.setText(emailBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException(ERROR_MESSAGE + e);
        }
    }

    public void sendProposalReceiptUrl(ProposalNotificationDto dto) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(dto.getUserEmail());
            String emailBody = String.format(
                    "<div style='font-family: Arial, sans-serif; color: #333;'>"
                            + "<p>Hello %s,</p>"
                            + "<p>Here is your link to download your receipt:</p>"
                            + "<p><strong>%s</strong></p>"
                            + "<p><strong>This link expires in 1 hour!</strong></p>"
                            + "<p>Best regards,</p>"
                            + "<p>Credit System</p>"
                            + "<br>"
                            + "</div>",
                    dto.getUserName(),
                    dto.getUrl()
            );
            helper.setSubject("PROPOSAL RECEIPT");
            helper.setText(emailBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException(ERROR_MESSAGE + e);
        }

    }

}
