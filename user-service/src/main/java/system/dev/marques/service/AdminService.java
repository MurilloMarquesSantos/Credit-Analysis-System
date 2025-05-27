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
import java.util.Optional;

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
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("This user does not exists or have been already deleted");
        }
        Optional<User> userOpt = userRepository.findById(userId);

        userRepository.deleteById(userId);

        userOpt.ifPresent(this::notifyUserDeletion);
    }

    private void notifyUserDeletion(User user) {
        LocalDate now = LocalDate.now();
        String date = now.toString();
        DeleteUserConfirmationDto dto = mapper.toDeleUserConfirmationDto(user);
        dto.setDate(date);
        producerService.sendUserDeleteConfirmation(dto);
    }

}
