package system.dev.marques.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequest userRequest);

    UserRequest toUserRequest(User user);

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
}
