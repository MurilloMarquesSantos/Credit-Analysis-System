package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import system.dev.marques.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.when;
import static system.dev.marques.util.UserCreatorStatic.createSavedUser;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository userRepositoryMock;

    @Test
    void loadUserByUsername_ReturnsUserDetails_WhenSuccessful() {

        when(userRepositoryMock.findUserByEmail(anyString())).thenReturn(Optional.of(createSavedUser()));

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("murillo@gmail.com");

        assertThat(userDetails).isNotNull();

        assertThat(userDetails.getUsername()).isEqualTo("murillo@gmail.com");
    }

    @Test
    void loadUserByUsername_ThrowsUsernameNotFoundException_WhenUserNotFound() {

        when(userRepositoryMock.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userDetailsServiceImpl.loadUserByUsername("murillo@gmail.com"))
                .withMessageContaining("murillo@gmail.com");

    }

}