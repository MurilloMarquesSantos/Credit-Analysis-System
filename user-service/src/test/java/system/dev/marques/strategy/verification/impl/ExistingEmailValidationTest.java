package system.dev.marques.strategy.verification.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.exception.custom.EmailExistingException;
import system.dev.marques.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.UserCreatorStatic.createUserRequest;

@ExtendWith(MockitoExtension.class)
class ExistingEmailValidationTest {

    @InjectMocks
    private ExistingEmailValidation validator;

    @Mock
    private UserRepository userRepositoryMock;

    @Test
    void execute_ValidateEmail_WhenSuccessful() {
        when(userRepositoryMock.existsByEmail(anyString())).thenReturn(false);

        UserRequest request = createUserRequest();

        assertThatCode(() -> validator.execute(request)).doesNotThrowAnyException();
    }

    @Test
    void execute_ThrowsEmailExistingException_WhenEmailIsInUse() {
        when(userRepositoryMock.existsByEmail(anyString())).thenReturn(true);

        UserRequest request = createUserRequest();

        assertThatExceptionOfType(EmailExistingException.class)
                .isThrownBy(() -> validator.execute(request))
                .withMessageContaining("This email is already in use");
    }

}