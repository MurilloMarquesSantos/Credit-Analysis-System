package system.dev.marques.util;

import system.dev.marques.dto.*;

import java.util.List;

public class QueueDtoCreator {

    public static ValidUserDto createValidUserDto() {
        return ValidUserDto.builder()
                .email("murillo@gmail.com")
                .url("http://localhost:8080/")
                .source("google")
                .build();
    }

    public static CreatedUserDto createCreatedUserDto() {
        return CreatedUserDto.builder()
                .email("murillo@gmail.com")
                .url("http://localhost:8080/")
                .name("Murillo")
                .build();
    }

    public static DeleteUserDto createDeleteUserDto() {
        return DeleteUserDto.builder()
                .userId(1L)
                .userEmail("murillo@gmail.com")
                .adminEmails(List.of())
                .reason("")
                .build();
    }

    public static ProposalStatusEmailDto createProposalStatusEmailDto() {
        return ProposalStatusEmailDto
                .builder()
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .requestedAmount(10000.0)
                .installments(12)
                .purpose("Buy a car")
                .rejectedReason("")
                .status("REJECTED")
                .build();
    }

    public static ProposalNotificationDto createProposalNotificationDto() {
        return ProposalNotificationDto.builder()
                .userName("Murillo")
                .userEmail("murillo@gmail.com")
                .url("http://localhost:8080")
                .build();
    }

    public static DeleteUserConfirmationDto createDeleteUserConfirmationDto() {
        return DeleteUserConfirmationDto.builder()
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .date("2025/20/01")
                .build();

    }
}
