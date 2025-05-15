package system.dev.marques.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.ValidUserDto;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendUserValidationLink(ValidUserDto dto) {
        log.info("sending user validation link");

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
}
