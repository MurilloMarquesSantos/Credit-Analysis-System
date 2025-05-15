package system.dev.marques.strategy.verification.impl;

import org.apache.coyote.BadRequestException;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.strategy.verification.NewAccountValidationStrategy;

public class EmailValidation implements NewAccountValidationStrategy {

    private static final String REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public void execute(UserRequest userRequest) throws BadRequestException {
        if (!userRequest.getEmail().matches(REGEX)) {
            throw new BadRequestException("Invalid email");
        }

    }
}
