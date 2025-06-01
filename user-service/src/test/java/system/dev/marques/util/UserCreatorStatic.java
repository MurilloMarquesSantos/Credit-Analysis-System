package system.dev.marques.util;

import system.dev.marques.domain.dto.requests.UserRequest;
import system.dev.marques.domain.dto.responses.ProposalHistoryResponse;
import system.dev.marques.domain.dto.responses.UserResponse;

import java.time.LocalDateTime;

public class UserCreatorStatic {

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


}
