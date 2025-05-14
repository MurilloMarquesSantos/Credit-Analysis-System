package system.dev.marques.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalDto;

@Mapper(componentModel = "spring")
public interface ProposalMapper {

    Proposal toProposal(ProposalDto proposalDto);

    @Mapping(target = "proposalId", source = "proposal.id")
    ProposalCreditDto toProposalCreditDto(Proposal proposal);
}
