package system.dev.marques.mapper;

import org.mapstruct.Mapper;
import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.dto.ProposalNotificationDto;
import system.dev.marques.dto.UserReceiptDto;

@Mapper(componentModel = "spring")
public interface ProposalMapper {

    ProposalNotificationDto toProposalNotificationDto(ApprovedProposalDto approvedProposalDto);

    ProposalNotificationDto toProposalNotificationDto(UserReceiptDto userReceiptDto);


}
