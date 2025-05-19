package system.dev.marques.strategy.verification.impl;

import org.springframework.stereotype.Component;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.exception.custom.EmailValidationException;
import system.dev.marques.strategy.verification.NewAccountValidationStrategy;

@Component
public class EmailValidation implements NewAccountValidationStrategy {

    private static final String REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public void execute(UserRequest userRequest) {
        if (!userRequest.getEmail().matches(REGEX)) {
            throw new EmailValidationException("Invalid email");
        }
    }
}
