package system.dev.marques.strategy.verification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.exception.custom.EmailExistingException;
import system.dev.marques.exception.custom.EmailValidationException;
import system.dev.marques.exception.custom.PasswordValidationException;
import system.dev.marques.strategy.verification.impl.EmailValidation;
import system.dev.marques.strategy.verification.impl.ExistingEmailValidation;
import system.dev.marques.strategy.verification.impl.PasswordLengthValidation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static system.dev.marques.util.UserCreatorStatic.createUserRequest;

@ExtendWith(MockitoExtension.class)
class UserValidationStrategyFactoryTest {

    @InjectMocks
    private UserValidationStrategyFactory factory;

    @Mock
    private EmailValidation emailValidationMock;

    @Mock
    private ExistingEmailValidation existingValidationMock;

    @Mock
    private PasswordLengthValidation passwordLengthMock;

    private static final String MESSAGE
            = "The password must have at least, 1 lower case char, 1 upper case char, 1 numeric char, 1 special char";

    @BeforeEach
    void setUp() {
        factory = new UserValidationStrategyFactory(
                List.of(emailValidationMock, existingValidationMock, passwordLengthMock));

    }

    @Test
    void validate_ThrowsEmailValidationException_WhenEmailIsInvalid() {
        doThrow(new EmailValidationException("Invalid email")).when(emailValidationMock).execute(any(UserRequest.class));

        UserRequest request = createUserRequest();

        assertThatExceptionOfType(EmailValidationException.class)
                .isThrownBy(() -> factory.validate(request))
                .withMessageContaining("Invalid email");
    }

    @Test
    void validate_ThrowsEmailExistingException_WhenEmailIsInUse() {
        doThrow(new EmailExistingException("This email is already in use")).when(existingValidationMock)
                .execute(any(UserRequest.class));

        UserRequest request = createUserRequest();

        assertThatExceptionOfType(EmailExistingException.class)
                .isThrownBy(() -> factory.validate(request))
                .withMessageContaining("This email is already in use");
    }

    @Test
    void validate_ThrowsPasswordValidationException_WhenPasswordIsInvalid() {
        doThrow(new PasswordValidationException(MESSAGE)).when(existingValidationMock)
                .execute(any(UserRequest.class));

        UserRequest request = createUserRequest();

        assertThatExceptionOfType(PasswordValidationException.class)
                .isThrownBy(() -> factory.validate(request))
                .withMessageContaining(MESSAGE);
    }

    @Test
    void validate_ValidatesUser_WhenSuccessful() {
        doNothing().when(emailValidationMock).execute(any(UserRequest.class));
        doNothing().when(existingValidationMock).execute(any(UserRequest.class));
        doNothing().when(passwordLengthMock).execute(any(UserRequest.class));


        UserRequest request = createUserRequest();

        assertThatCode(() -> factory.validate(request)).doesNotThrowAnyException();

    }

}