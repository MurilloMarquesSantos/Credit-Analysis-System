package system.dev.marques.strategy.verification;

import org.apache.coyote.BadRequestException;
import system.dev.marques.domain.dto.requests.UserRequest;

public interface NewAccountValidationStrategy {

    void execute(UserRequest userRequest) throws BadRequestException;
}
