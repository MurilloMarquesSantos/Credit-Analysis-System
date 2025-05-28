package system.dev.marques.integrationTests.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import system.dev.marques.domain.EnableUserToken;
import system.dev.marques.integrationTests.AbstractIntegration;
import system.dev.marques.repository.TokenRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static system.dev.marques.util.EnableTokenCreator.createUserToken;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TokenRepositoryIT extends AbstractIntegration {

    @Autowired
    private TokenRepository tokenRepository;

    private static EnableUserToken enableUserToken;

    @AfterEach
    void tearDown() {
        tokenRepository.deleteAll();
    }

    @Test
    void save_PersistsToken_WhenSuccessful() {

        enableUserToken = createUserToken();

        EnableUserToken savedToken = tokenRepository.save(enableUserToken);

        assertThat(savedToken).isNotNull();

        assertThat(savedToken.getId()).isNotNull();

        assertThat(savedToken.getToken()).isEqualTo(enableUserToken.getToken());
    }


    @Test
    void findByToken_ReturnsTokenOptional_WhenSuccessful() {

        enableUserToken = createUserToken();

        tokenRepository.save(enableUserToken);

        Optional<EnableUserToken> tokenOpt = tokenRepository.findByToken(enableUserToken.getToken());

        assertThat(tokenOpt).isPresent();

        assertThat(tokenOpt.get().getToken()).isEqualTo(enableUserToken.getToken());

    }

    @Test
    void findByToken_ReturnsEmptyTokenOptional_WhenTokenNotFound() {

        enableUserToken = createUserToken();

        tokenRepository.save(enableUserToken);

        Optional<EnableUserToken> tokenOpt = tokenRepository.findByToken("");

        assertThat(tokenOpt).isNotPresent();

    }

    @Test
    void deleteById_RemovesToken_WhenSuccessful() {

        enableUserToken = createUserToken();

        EnableUserToken savedToken = tokenRepository.save(enableUserToken);

        tokenRepository.deleteById(savedToken.getId());

        Optional<EnableUserToken> tokenOpt = tokenRepository.findById(savedToken.getId());

        assertThat(tokenOpt).isNotPresent();

    }
}
