package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.AnalyzedDto;
import system.dev.marques.domain.dto.ProposalDto;
import system.dev.marques.domain.dto.reponse.ProposalHistoryResponse;
import system.dev.marques.domain.enums.ProposalStatus;
import system.dev.marques.mapper.ProposalMapper;
import system.dev.marques.repository.ProposalRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;

    private final ProposalMapper mapper;

    public Proposal save(ProposalDto dto) {
        Proposal proposal = mapper.toProposal(dto);
        proposal.setStatus(ProposalStatus.PENDING);
        return proposalRepository.save(proposal);
    }

    public void updateProposalStatus(AnalyzedDto dto) {
        Optional<Proposal> proposalOpt = proposalRepository.findById(dto.getProposalId());
        if (proposalOpt.isPresent()) {
            proposalOpt.get().setStatus(dto.getStatus());
            proposalRepository.save(proposalOpt.get());
        }
    }

    public List<ProposalHistoryResponse> getProposalHistory(Long userId) {
        List<Proposal> userHistory = proposalRepository.findByUserId(userId);
        return userHistory.stream()
                .map(mapper::toProposalHistoryResponse)
                .toList();
    }
}
