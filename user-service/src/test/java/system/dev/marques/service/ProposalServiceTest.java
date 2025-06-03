package system.dev.marques.service;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.proposal.ProposalRequest;
import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.mapper.UserMapper;
import system.dev.marques.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.*;
import static system.dev.marques.util.QueueDtoCreator.createProposalUserInfo;
import static system.dev.marques.util.RequestCreator.createProposalRequest;
import static system.dev.marques.util.UserCreatorStatic.createSavedUser;

@ExtendWith(MockitoExtension.class)
class ProposalServiceTest {

    @InjectMocks
    private ProposalService proposalService;

    @Mock
    private UserService userServiceMock;

    @Mock
    private UserMapper mapperMock;

    @Mock
    private ProducerService producerServiceMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Test
    void propose_ReturnsStringMessage_WhenSuccessful() throws BadRequestException {

        User savedUser = createSavedUser();

        savedUser.setLastProposalAt(LocalDateTime.now().minusDays(1));

        when(userServiceMock.findUserById(anyLong())).thenReturn(savedUser);
        when(mapperMock.toProposalUserInfo(any(User.class), any(ProposalRequest.class)))
                .thenReturn(createProposalUserInfo());
        doNothing().when(producerServiceMock).sendProposal(any(ProposalUserInfo.class));

        String response = proposalService.propose(createProposalRequest(), () -> "1");

        assertThat(response).isNotBlank()
                .isEqualTo("Proposal sent for review. Please monitor your email inbox for further updates.");

        verify(userRepositoryMock, times(1)).save(any(User.class));
    }

    @Test
    void propose_ReturnsStringMessage_WhenSuccessfulWithNoSubmittedTime() throws BadRequestException {

        User savedUser = createSavedUser();

        when(userServiceMock.findUserById(anyLong())).thenReturn(savedUser);
        when(mapperMock.toProposalUserInfo(any(User.class), any(ProposalRequest.class)))
                .thenReturn(createProposalUserInfo());
        doNothing().when(producerServiceMock).sendProposal(any(ProposalUserInfo.class));


        String response = proposalService.propose(createProposalRequest(), () -> "1");

        assertThat(response).isNotBlank()
                .isEqualTo("Proposal sent for review. Please monitor your email inbox for further updates.");

        verify(userRepositoryMock, times(1)).save(any(User.class));

    }

    @Test
    void propose_ReturnsBadRequestException_WhenProposalIsSubmittedInLast24hours() {

        User savedUser = createSavedUser();

        savedUser.setLastProposalAt(LocalDateTime.now());

        when(userServiceMock.findUserById(anyLong())).thenReturn(savedUser);

        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> proposalService.propose(createProposalRequest(), () -> "1"))
                .withMessageContaining("You already submitted a proposal in the last 24 hours." +
                        " Try again in 24 hour(s) and 0 minute(s).");

    }

}