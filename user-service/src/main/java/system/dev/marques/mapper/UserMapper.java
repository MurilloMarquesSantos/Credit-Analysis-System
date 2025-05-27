package system.dev.marques.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.proposal.ProposalRequest;
import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.domain.dto.rabbitmq.DeleteUserConfirmationDto;
import system.dev.marques.domain.dto.rabbitmq.DeleteUserDto;
import system.dev.marques.domain.dto.rabbitmq.UserReceiptDto;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.domain.dto.responses.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequest userRequest);

    User toUser(UserResponse userResponse);

    UserResponse toUserResponse(User user);

    UserEnabledResponse toUserEnabledResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "valid", ignore = true)
    void updateUserFromGoogleRequest(UserRequestGoogle request, @MappingTarget User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "valid", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    void updateUserFromUserEnableRequest(UserEnableRequest request, @MappingTarget User user);


    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cpf", source = "user.cpf")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userIncome", source = "user.income")
    @Mapping(target = "requestedAmount", source = "request.requestedAmount")
    @Mapping(target = "installments", source = "request.installments")
    @Mapping(target = "purpose", source = "request.purpose")
    ProposalUserInfo toProposalUserInfo(User user, ProposalRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    UserReceiptDto toUserReceiptDto(User user);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    DeleteUserDto toDeleteUserDto(User user);

    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "userEmail", source = "user.email")
    DeleteUserConfirmationDto toDeleUserConfirmationDto(User user);
}
