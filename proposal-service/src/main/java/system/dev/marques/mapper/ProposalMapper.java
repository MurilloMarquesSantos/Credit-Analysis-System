package system.dev.marques.mapper;

import org.mapstruct.Mapper;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalDto;

@Mapper(componentModel = "spring")
public interface ProposalMapper {

    Proposal toProposal(ProposalDto proposalDto);

    ProposalCreditDto toProposalCreditDto(Proposal proposal);
}
