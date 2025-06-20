package system.dev.marques.util;

import system.dev.marques.domain.Roles;
import system.dev.marques.domain.User;
import system.dev.marques.domain.dto.requests.UserEnableRequest;
import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.requests.UserRequestGoogle;
import system.dev.marques.domain.dto.responses.ProposalHistoryResponse;
import system.dev.marques.domain.dto.responses.UserAdminResponse;
import system.dev.marques.domain.dto.responses.UserEnabledResponse;
import system.dev.marques.domain.dto.responses.UserResponse;

import java.time.LocalDateTime;
import java.util.Set;

public class UserCreatorStatic {

    public static User createSavedUser() {
        return User.builder()
                .id(1L)
                .cpf("12345678909")
                .email("murillo@gmail.com")
                .name("Murillo Marques")
                .phoneNumber("11885786421")
                .password("Murillo@12345")
                .roles(Set.of(new Roles(1L, "ADMIN")))
                .valid(true)
                .build();
    }

    public static User createUser() {
        return User.builder()
                .cpf("12345678909")
                .email("murilloa@gmail.com")
                .name("Murillo Marques")
                .phoneNumber("11885786421")
                .password("Murillo@12345")
                .roles(Set.of(new Roles(1L, "ADMIN")))
                .valid(true)
                .build();
    }

public static UserResponse createUserResponse() {
    return UserResponse.builder()
            .id(1L)
            .name("Murillo")
            .email("murillo@gmail.com")
            .phoneNumber("")
            .password("")
            .build();
}

public static UserRequest createUserRequest() {
    return UserRequest.builder().email("murillo@gmail.com")
            .name("Murillo")
            .cpf("")
            .password("")
            .phoneNumber("")
            .build();
}

public static UserRequest createInvalidUserRequest() {
    return UserRequest.builder().email("")
            .name("")
            .cpf("")
            .password("")
            .phoneNumber("")
            .build();
}

public static ProposalHistoryResponse createProposalHistoryResponse() {
    return ProposalHistoryResponse.builder()
            .proposalId(1L)
            .cpf("")
            .requestedAmount(5000D)
            .status("APPROVED")
            .createdAt(LocalDateTime.now())
            .build();

}

public static UserRequestGoogle createUserRequestGoogle() {
    return UserRequestGoogle.builder()
            .name("Murillo")
            .password("")
            .income(5000D)
            .phoneNumber("")
            .cpf("")
            .build();
}

public static UserEnabledResponse createUserEnabledResponse() {
    return UserEnabledResponse.builder()
            .id(1L)
            .name("Murillo")
            .income(5000D)
            .cpf("")
            .phoneNumber("")
            .password("")
            .email("murillo@gmail.com")
            .build();

}

public static UserEnableRequest createUserEnableRequest() {
    return UserEnableRequest.builder()
            .income(5000D)
            .build();
}

public static UserAdminResponse createUserAdminResponse() {
    return UserAdminResponse.builder()
            .id(1L)
            .name("Murillo Marques")
            .email("murillo@gmail.com")
            .cpf("12345678900")
            .phoneNumber("11999999999")
            .income(5000D)
            .password("password")
            .roles(Set.of("ADMIN"))
            .valid(true)
            .lastProposalAt(LocalDateTime.now())
            .build();
}


}
