package system.dev.marques.util;

import system.dev.marques.domain.dto.proposal.ProposalUserInfo;
import system.dev.marques.domain.dto.rabbitmq.*;

import java.util.List;

public class QueueDtoCreator {

    public static ProposalUserInfo createProposalUserInfo() {
        return ProposalUserInfo.builder()
                .userId(1L)
                .cpf("01234567890")
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .userIncome(5000.0D)
                .requestedAmount(7000.0D)
                .installments(60)
                .purpose("Buy a car")
                .build();

    }

    public static ValidUserDto createValidUserDto() {
        return ValidUserDto.builder()
                .email("murillo@gmail.com")
                .url("http://murillo.com")
                .source("formlogin")
                .build();
    }

    public static CreatedUserDto createCreatedUserDto() {
        return CreatedUserDto.builder()
                .email("murillo@gmail.com")
                .name("Murillo Marques")
                .url("http://murillo.com")
                .build();
    }

    public static UserReceiptDto createUserReceiptDto() {
        return UserReceiptDto.builder()
                .proposalId(1L)
                .userEmail("murillo@gmail.com")
                .userName("Murillo Marques")
                .userId(1L)
                .build();
    }

    public static DeleteUserDto createDeleteUserDto() {
        return DeleteUserDto.builder()
                .userId(1L)
                .userEmail("murillo@gmail")
                .adminEmails(List.of("murilloadmin@gmail"))
                .reason("")
                .build();
    }

    public static DeleteUserConfirmationDto createDeleteUserConfirmationDto() {
        return DeleteUserConfirmationDto.builder()
                .userName("Murillo Marques")
                .userEmail("murilloa@gmail.com")
                .date("2025-05-28")
                .build();
    }
}
