package system.dev.marques.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.CreatedUserDto;
import system.dev.marques.dto.ProposalStatusEmailDto;
import system.dev.marques.dto.ValidUserDto;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {

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
            log.error("Enable to send email: {}", e.getMessage());
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
            log.error("Enable to send email: {}", e.getMessage());
        }
    }

    public void sendProposalStatusEmail(ProposalStatusEmailDto dto) {
        log.info(dto);
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
            log.error("Enable to send email: {}", e.getMessage());
        }
    }

}
