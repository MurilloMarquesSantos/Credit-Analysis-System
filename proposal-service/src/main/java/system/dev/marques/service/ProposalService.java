package system.dev.marques.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.ProposalDto;
import system.dev.marques.domain.enums.ProposalStatus;
import system.dev.marques.mapper.ProposalMapper;
import system.dev.marques.repository.ProposalRepository;

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
}
