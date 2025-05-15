package system.dev.marques.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import system.dev.marques.dto.CreatedUserDto;
import system.dev.marques.dto.ValidUserDto;
import system.dev.marques.service.EmailService;

@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationListener {

    private final EmailService emailService;

    @RabbitListener(queues = "queue.notification.user.validation")
    public void listenValidationQueue(ValidUserDto dto) {
        emailService.sendUserValidationLink(dto);
        log.info(dto.toString());
    }

    @RabbitListener(queues = "queue.notification.user.created")
    public void listenCreateQueue(CreatedUserDto dto) {
        log.info(dto.toString());
    }
}
