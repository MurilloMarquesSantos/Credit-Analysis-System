package system.dev.marques.strategy.verification.impl;

import org.springframework.stereotype.Component;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.exception.PasswordValidationException;
import system.dev.marques.strategy.verification.NewAccountValidationStrategy;


@Component
public class PasswordLengthValidation implements NewAccountValidationStrategy {
    private static final String MESSAGE =
            "The password must have at least, 1 lower case char, 1 upper case char, 1 numeric char, 1 special char";

    private static final String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).+$";

    @Override
    public void execute(UserRequest request) {
        if (!request.getPassword().matches(REGEX)) {
            throw new PasswordValidationException(MESSAGE);
        }

    }
}
