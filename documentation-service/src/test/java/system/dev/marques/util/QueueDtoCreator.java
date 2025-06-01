package system.dev.marques.util;

import system.dev.marques.dto.ApprovedProposalDto;
import system.dev.marques.dto.ProposalNotificationDto;
import system.dev.marques.dto.UserReceiptDto;

import java.time.LocalDateTime;

public class QueueDtoCreator {

    public static ApprovedProposalDto createApprovedProposalDto() {
        return ApprovedProposalDto.builder()
                .proposalId(1L)
                .userId(1L)
                .cpf("123456789")
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .userIncome(5000.00)
                .requestedAmount(20000.00)
                .installments(24)
                .createdAt(LocalDateTime.now())
                .purpose("buy a car")
                .build();
    }

    public static UserReceiptDto createUserReceiptDto() {
        return UserReceiptDto.builder()
                .proposalId(1L)
                .userId(1L)
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .build();
    }

    public static ProposalNotificationDto createProposalNotificationDto() {
        return ProposalNotificationDto.builder()
                .userEmail("murillo@gmail.com")
                .userName("Murillo")
                .url("http://localhost:8080")
                .build();
    }
}
