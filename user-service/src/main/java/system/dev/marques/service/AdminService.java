package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.rabbitmq.DeleteUserConfirmationDto;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final ProducerService producerService;

    private final UserMapper mapper;

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void deleteUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("This user does not exists or have been already deleted"));

        userRepository.deleteById(user.getId());

        notifyAll(user);
    }

    private void notifyAll(User user) {
        notifyUserDeletion(user);
        notifyDocumentDeletion(user);
        notifyProposalDelete(user);
    }

    private void notifyUserDeletion(User user) {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        DeleteUserConfirmationDto dto = mapper.toDeleUserConfirmationDto(user);
        dto.setDate(date);
        producerService.sendUserDeleteConfirmation(dto);
    }

    private void notifyDocumentDeletion(User user) {
        producerService.sendDocumentDeletion(user.getId());
    }

    private void notifyProposalDelete(User user) {
        producerService.sendProposalDeletion(user.getId());
    }

}
