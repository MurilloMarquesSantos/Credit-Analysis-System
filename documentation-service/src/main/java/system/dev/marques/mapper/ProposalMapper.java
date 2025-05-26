package system.dev.marques.mapper;

import org.mapstruct.Mapper;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.dto.ProposalNotificationDto;

@Mapper(componentModel = "spring")
public interface ProposalMapper {

    ProposalNotificationDto toProposalNotificationDto(ApprovedProposalDto approvedProposalDto);
}
