package system.dev.marques.strategy.verification.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.exception.custom.EmailExistingException;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.strategy.verification.NewAccountValidationStrategy;

@Component
@RequiredArgsConstructor
public class ExistingEmailValidation implements NewAccountValidationStrategy {

    private final UserRepository userRepository;

    @Override
    public void execute(UserRequest userRequest){

        if (isInvalidEmail(userRequest.getEmail())) {
            throw new EmailExistingException("This email is already in use");
        }

    }

    private boolean isInvalidEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
