package system.dev.marques.strategy.verification.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.repository.UserRepository;
import system.dev.marques.strategy.verification.NewAccountValidationStrategy;

@RequiredArgsConstructor
public class ExistingEmailValidation implements NewAccountValidationStrategy {

    private final UserRepository userRepository;

    @Override
    public void execute(UserRequest userRequest) throws BadRequestException {

        if (isInvalidEmail(userRequest.getEmail())) {
            throw new BadRequestException("This email is already in use");
        }

    }
    private boolean isInvalidEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
