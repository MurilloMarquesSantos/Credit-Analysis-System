package system.dev.marques.strategy.verification;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import system.dev.marques.domain.dto.requests.UserRequest;

import java.util.List;

@RequiredArgsConstructor
public class UserValidationStrategyFactory {

    private final List<NewAccountValidationStrategy> strategies;

    public void validate(UserRequest request) throws BadRequestException {
        for (NewAccountValidationStrategy strategy : strategies) {
            strategy.execute(request);
        }

    }
}
