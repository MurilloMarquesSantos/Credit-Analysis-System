package system.dev.marques.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.AnalyzedDto;
import system.dev.marques.domain.dto.ApprovedProposalDto;
import system.dev.marques.domain.dto.ProposalDto;
import system.dev.marques.domain.dto.ProposalStatusEmailDto;
import system.dev.marques.domain.dto.reponse.ProposalHistoryResponse;
import system.dev.marques.domain.enums.ProposalStatus;
import system.dev.marques.mapper.ProposalMapper;
import system.dev.marques.repository.ProposalRepository;
import system.dev.marques.util.QueueDtoCreator;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doNothing;
import static system.dev.marques.util.ProposalCreator.*;

@ExtendWith(MockitoExtension.class)
class ProposalServiceTest {

    @InjectMocks
    private ProposalService proposalService;

    @Mock
    private ProposalRepository proposalRepositoryMock;

    @Mock
    private ProducerService producerServiceMock;

    @Mock
    private ProposalMapper proposalMapperMock;

    @Test
    void save_ReturnsProposal_WhenSuccessful() {
        when(proposalMapperMock.toProposal(any(ProposalDto.class))).thenReturn(createProposal());
        when(proposalRepositoryMock.save(any(Proposal.class))).thenReturn(createSavedProposal());

        ProposalDto dto = QueueDtoCreator.createProposalDto();
        Proposal savedProposal = proposalService.save(dto);

        assertThat(savedProposal.getId()).isNotNull();

        assertThat(savedProposal.getUserId()).isEqualTo(dto.getUserId());

    }

    @Test
    void updateProposalStatus_UpdatesProposalStatusAPPROVED_WhenSuccessful() {
        doNothing().when(producerServiceMock).sendApprovedProposal(any(ApprovedProposalDto.class));
        doNothing().when(producerServiceMock).sendProposalStatus(any(ProposalStatusEmailDto.class));
        when(proposalRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createSavedProposal()));
        when(proposalRepositoryMock.save(any(Proposal.class))).thenReturn(createSavedProposal());
        when(proposalMapperMock.toProposalStatusEmailDto(any(Proposal.class)))
                .thenReturn(QueueDtoCreator.createProposalStatusEmailDto());
        when(proposalMapperMock.toApprovedProposalDto(any(Proposal.class)))
                .thenReturn(QueueDtoCreator.createApprovedProposalDto());

        AnalyzedDto dto = QueueDtoCreator.createAnalyzedDto();

        assertThatCode(() -> proposalService.updateProposalStatus(dto)).doesNotThrowAnyException();

    }

    @Test
    void updateProposalStatus_UpdatesProposalStatusREJECTED_WhenSuccessful() {
        doNothing().when(producerServiceMock).sendProposalStatus(any(ProposalStatusEmailDto.class));
        when(proposalRepositoryMock.findById(anyLong())).thenReturn(Optional.of(createSavedProposal()));
        when(proposalRepositoryMock.save(any(Proposal.class))).thenReturn(createSavedProposal());
        when(proposalMapperMock.toProposalStatusEmailDto(any(Proposal.class)))
                .thenReturn(QueueDtoCreator.createProposalStatusEmailDto());

        AnalyzedDto dto = QueueDtoCreator.createAnalyzedDto();
        dto.setStatus(ProposalStatus.REJECTED);

        assertThatCode(() -> proposalService.updateProposalStatus(dto)).doesNotThrowAnyException();

    }

    @Test
    void updateProposalStatus_DoNothingWhenProposalNotFound() {
        when(proposalRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        AnalyzedDto dto = QueueDtoCreator.createAnalyzedDto();

        assertThatCode(() -> proposalService.updateProposalStatus(dto)).doesNotThrowAnyException();

    }


    @Test
    void getProposalHistory_ReturnsHistoryList_WhenSuccessful(){
        when(proposalRepositoryMock.findByUserId(anyLong())).thenReturn(List.of(createSavedProposal()));
        when(proposalMapperMock.toProposalHistoryResponse(any(Proposal.class)))
                .thenReturn(createProposalHistoryResponse());

        List<ProposalHistoryResponse> proposalHistory = proposalService.getProposalHistory(1L);

        assertThat(proposalHistory).isNotNull().hasSize(1);

        assertThat(proposalHistory.getFirst().getProposalId()).isEqualTo(1L);
    }

    @Test
    void getProposalHistory_ReturnsEmptyList_WhenHistoryDoesNotExist(){
        when(proposalRepositoryMock.findByUserId(anyLong())).thenReturn(List.of());

        List<ProposalHistoryResponse> proposalHistory = proposalService.getProposalHistory(1L);

        assertThat(proposalHistory).isNotNull().isEmpty();

    }

    @Test
    void deleteUserHistory_RemovesUserHistory_WhenSuccessful(){
        when(proposalRepositoryMock.findByUserId(anyLong())).thenReturn(List.of(createSavedProposal()));
        doNothing().when(proposalRepositoryMock).deleteById(anyLong());

        assertThatCode(() -> proposalService.deleteUserHistory(1L)).doesNotThrowAnyException();

    }

    @Test
    void deleteUserHistory_DoNothing_WhenHistoryIsNotFound(){
        when(proposalRepositoryMock.findByUserId(anyLong())).thenReturn(List.of());

        assertThatCode(() -> proposalService.deleteUserHistory(1L)).doesNotThrowAnyException();

    }


}