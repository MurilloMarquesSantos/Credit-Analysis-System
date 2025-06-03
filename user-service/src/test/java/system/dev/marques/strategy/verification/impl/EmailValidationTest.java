package system.dev.marques.strategy.verification.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.exception.custom.EmailValidationException;
import system.dev.marques.util.UserCreatorStatic;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class EmailValidationTest {

    @InjectMocks
    private EmailValidation emailValidation;


    @Test
    void execute_Validates_WhenSuccessful() {
        UserRequest request = UserCreatorStatic.createUserRequest();

        assertThatCode(() -> emailValidation.execute(request)).doesNotThrowAnyException();
    }


    @Test
    void execute_ThrowsEmailValidationException_WhenEmailIsInvalid() {
        UserRequest request = UserCreatorStatic.createUserRequest();

        request.setEmail("invalid");

        assertThatExceptionOfType(EmailValidationException.class)
                .isThrownBy(() -> emailValidation.execute(request))
                .withMessageContaining("Invalid email");
    }


}