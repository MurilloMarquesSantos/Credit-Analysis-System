package system.dev.marques.strategy.verification.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.exception.custom.PasswordValidationException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static system.dev.marques.util.UserCreatorStatic.createUserRequest;

@ExtendWith(MockitoExtension.class)
class PasswordLengthValidationTest {

    @InjectMocks
    private PasswordLengthValidation validation;

    private static final String MESSAGE =
            "The password must have at least, 1 lower case char, 1 upper case char, 1 numeric char, 1 special char";

    @Test
    void execute_ValidatePassword_WhenSuccessful(){

        UserRequest request = createUserRequest();

        request.setPassword("Murillo@12");

        assertThatCode(() -> validation.execute(request)).doesNotThrowAnyException();
    }

    @Test
    void execute_ThrowsPasswordValidationException_WhenPasswordIsInvalid(){

        UserRequest request = createUserRequest();

        assertThatExceptionOfType(PasswordValidationException.class)
                .isThrownBy(() -> validation.execute(request))
                .withMessageContaining(MESSAGE);

    }

}