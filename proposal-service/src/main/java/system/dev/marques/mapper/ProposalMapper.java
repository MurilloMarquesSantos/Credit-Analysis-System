package system.dev.marques.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import system.dev.marques.domain.Proposal;
import system.dev.marques.domain.dto.ApprovedProposalDto;
import system.dev.marques.domain.dto.ProposalCreditDto;
import system.dev.marques.domain.dto.ProposalDto;
import system.dev.marques.domain.dto.ProposalStatusEmailDto;
import system.dev.marques.domain.dto.reponse.ProposalHistoryResponse;

@Mapper(componentModel = "spring")
public interface ProposalMapper {

    Proposal toProposal(ProposalDto proposalDto);

    @Mapping(target = "proposalId", source = "proposal.id")
    @Mapping(target = "installments", source = "proposal.installments")
    ProposalCreditDto toProposalCreditDto(Proposal proposal);

    @Mapping(target = "proposalId", source = "proposal.id")
    ProposalHistoryResponse toProposalHistoryResponse(Proposal proposal);

    ProposalStatusEmailDto toProposalStatusEmailDto(Proposal proposal);

    @Mapping(target = "proposalId", source = "proposal.id")
    ApprovedProposalDto toApprovedProposalDto(Proposal proposal);
}
